package exercises.ex5Tests;

import image.Image;
import image.RGBA;
import testSuite.testTemplates.VisualTest;
import utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Test1Intersections extends VisualTest {

  private final List<Intersectable> objects = new ArrayList<>();

  public Test1Intersections(String gsFileName, String title) {
    super(gsFileName, title);

    final double scale = 70;

    Vector3 center = new Vector3(w / 2, h / 2, 0);
    Vector3 right = new Vector3(1, 0, 0);
    Vector3 forward = new Vector3(0, 0, 1);

    Vector3 p1 = center.minus(right.times(1.25 * scale));
    Vector3 p2 = center.minus(right.times(-1.25 * scale)).minus(forward.times(25));

    objects.add(new Sphere(p1, scale));
    objects.add(new Triangle(
        p2.plus(new Vector3(0, 1, 0).times(scale)),
        p2.plus(new Vector3(-1.2, -1, 0).times(scale)),
        p2.plus(new Vector3(1.2, -1, 0).times(scale))));
  }

  protected void _draw() {
    drawn = drawTestImage();
    fireDrawEvent();
  }

  private Image<RGBA> drawTestImage() {
    Image<RGBA> image = new Image<>(w, h);

    for (int x = 0; x < w; ++x) {
      for (int y = 0; y < h; ++y) {
        Ray ray = new Ray(new Vector3(x + 0.5, y + 0.5, -100), new Vector3(0, 0, 1));

        Optional<Double> distance = getDistance(ray);

        Optional<RGBA> color = distance.map(d -> 1.0 - d / 100.0)
            .map(d -> Math.min(d, 1))
            .map(d -> Math.max(d, 0))
            .map(d -> d * 0.7 + 0.3)
            .map(RGBA.white::times);

        image.set(x, y, color.orElse(RGBA.black));
      }
    }

    return image;
  }

  private Optional<Double> getDistance(Ray ray) {
    for (Intersectable object : objects) {
      Optional<Intersection> intersect = object.intersect(ray, 0);

      if (intersect.isPresent()) {
        return intersect.map(intersection -> intersection.t);
      }
    }

    return Optional.empty();
  }
}
