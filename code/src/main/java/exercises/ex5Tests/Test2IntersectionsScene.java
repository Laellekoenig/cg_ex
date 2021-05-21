package exercises.ex5Tests;

import image.Image;
import image.RGBA;
import raytracing.RayTracingMaterial;
import raytracing.Scene;
import testSuite.testTemplates.VisualTest;
import utils.*;

import java.util.*;

public class Test2IntersectionsScene extends VisualTest {

  private final SceneForTesting scene = new SceneForTesting();

  private final static double W = 500;
  private final static double H = 480;

  private final static RGBA Orange = new RGBA(0xEFD816);
  private final static RGBA Pink = new RGBA(0xBE3A61);
  private final static RGBA Purple = new RGBA(0xFF3AFF);
  private final static RGBA DarkPurple = new RGBA(0x601C38).times(0.75);
  private final static RGBA DarkBlue = new RGBA(0x005989);
  private final static RGBA Blue = new RGBA(0x1C477C);

  private final static Colorizer Background = r ->
      lerp(lerp(DarkPurple, DarkBlue.times(0.2), r.origin.y / H * 1),
          Blue, r.origin.x / W * 0.2);

  private final static Colorizer ForegroundWow = r ->
      lerp(Purple, Blue, ((r.origin.y - (r.origin.x - W / 2) / 4)) / 100 + 0.2);

  private final static Colorizer Sun = r -> lerp(Orange, Pink, (r.origin.y / H - 0.3) * 3);
  private final static Colorizer SunGlow = r ->
      Background.colorize(r).plus(Sun.colorize(r)
          .times(Math.pow(
              1 - clamp01((r.origin.minus(new Vector3(W / 2, H / 2, 0)).length() - 100) / 60), 3))
          .times(0.5));

  public Test2IntersectionsScene(String gsFileName, String title) {
    super(gsFileName, title);

    final double w2 = w / 2;
    final double h2 = h / 2;

    // Background
    scene.addObjects(getQuad(0, 0, 10000, w * 2, h * 2), Background);

    // Water
    for (int i = 180; i > 5; ) {
      int b = i / 5;
      scene.addObjects(getQuad(w2, h2 + i, 50, 1000, b), Background);
      i -= b + 2;
    }

    // Sun
    scene.addObject(new Sphere(new Vector3(w2, h2, 1000), 100), Sun);
    scene.addObject(new Sphere(new Vector3(w2, h2, 1100), 160), SunGlow);

    // WOW
    scene.addObject(new Sphere(new Vector3(w2, 50, 0), 35), Background);
    scene.addObject(new Sphere(new Vector3(w2, 50, 100), 50), ForegroundWow);

    scene.addObjects(getWAt(100, 0, 20), ForegroundWow);
    scene.addObjects(getWAt(300, 0, 20), ForegroundWow);

    scene.addObjects(getWAt(100, -50, 10), Background);
    scene.addObjects(getWAt(300, -50, 10), Background);

    // Mountains
    mountain(30, h2 + 40, 20, 160, 70, r -> RGBA.black);
    mountain(100, h2 + 10, 30, 120, 60, r -> RGBA.black);
    mountain(w2 + 100, h2 + 10, 30, 100, 40, r -> RGBA.black);
    mountain(w2 + 160, h2 + 20, 25, 75, 30, r -> RGBA.black);
    mountain(w2 + 200, h2 + 50, 20, 200, 70, r -> RGBA.black);
  }

  private void mountain(double x, double y, double z, double w, double h, Colorizer c) {
    scene.addObject(new Triangle(
        new Vector3(x - w / 2, y, z),
        new Vector3(x, y - h, z),
        new Vector3(x + w / 2, y, z)), c);

    scene.addObject(new Triangle(
        new Vector3(x - w / 2, y, z + 80),
        new Vector3(x, y + h * 1.5, z + 80),
        new Vector3(x + w / 2, y, z + 80)), r -> lerp(Background.colorize(r), c.colorize(r), 0.5));
  }

  protected void _draw() {
    drawn = drawTestImage();
    fireDrawEvent();
  }

  private Image<RGBA> drawTestImage() {
    Image<RGBA> image = new Image<>(w, h);

    for (int x = 0; x < w; ++x) {
      for (int y = 0; y < h; ++y) {
        Ray ray = new Ray(new Vector3(x + 0.5, y + 0.5 - 75, 0), new Vector3(0, 0, 1));

        RGBA color = scene.getColor(ray).orElse(RGBA.black);
        RGBA desaturated = RGBA.white.times((color.r + color.g + color.b) / 3.0);

        double scanlineEffect = (y % 2); // (Math.sin(y * 2) * 0.5 + 0.5);

        image.set(x, y, lerp(color, desaturated, 0.75 * scanlineEffect));
      }
    }

    return image;
  }

  private static List<Triangle> getQuad(double x, double y, double z, double w, double h) {
    Vector3 up = new Vector3(0, h / 2, 0);
    Vector3 right = new Vector3(w / 2, 0, 0);
    Vector3 center = new Vector3(x, y, z);

    return Arrays.asList(
        new Triangle(
            center.plus(up).plus(right),
            center.plus(up).minus(right),
            center.minus(up).minus(right)),
        new Triangle(
            center.minus(up).minus(right),
            center.minus(up).plus(right),
            center.plus(up).plus(right))
    );
  }

  private static List<Triangle> getWAt(int x, int y, int z) {
    Vector3 offset = new Vector3(x, y, z);

    return Arrays.asList(
        new Triangle(
            new Vector3(0, 0, 0).plus(offset),
            new Vector3(60, 0, 0).plus(offset),
            new Vector3(30, 100, 0).plus(offset)),
        new Triangle(
            new Vector3(40, 0, 0).plus(offset),
            new Vector3(100, 0, 0).plus(offset),
            new Vector3(70, 100, 0).plus(offset))
    );
  }

  private static RGBA lerp(RGBA from, RGBA to, double t) {
    t = clamp01(t);
    return from.times(1.0 - t).plus(to.times(t));
  }

  private static double clamp01(double t) {
    t = Math.max(t, 0);
    t = Math.min(t, 1);
    return t;
  }

  // Helper Classes
  private static final class SceneForTesting extends Scene {

    private final HashMap<Intersectable, Colorizer> colorizers = new HashMap<>();

    void addObject(Intersectable geometry, Colorizer colorizer) {
      colorizers.put(geometry, colorizer);
      addObject(geometry, RayTracingMaterial.Default());
    }

    void addObjects(Collection<? extends Intersectable> geometry, Colorizer colorizer) {
      for (Intersectable g : geometry) {
        addObject(g, colorizer);
      }
    }

    Optional<RGBA> getColor(Ray ray) {
      return rayCastScene(ray, 0) // Testing happens here
          .map(r -> colorizers.get(r.object.getGeometry()))
          .map(c -> c.colorize(ray));
    }
  }

  private interface Colorizer {

    RGBA colorize(Ray r);
  }

}
