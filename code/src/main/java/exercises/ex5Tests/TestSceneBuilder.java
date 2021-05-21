package exercises.ex5Tests;

import image.RGBA;
import raytracing.RayTracingMaterial;
import raytracing.Scene;
import utils.Sphere;
import utils.Triangle;
import utils.Vector3;

import java.util.Iterator;

public class TestSceneBuilder {

  public final Scene scene;

  public TestSceneBuilder() {
    this(new Scene());
  }

  public TestSceneBuilder(Scene scene) {
    this.scene = scene;
  }

  public void addPlanetStyleSpheres(Vector3 center, int moonCount, double planetSize,
      double moonSize,
      RayTracingMaterial materialPlanet, RayTracingMaterial materialMoons) {
    addSphere(center, planetSize, materialPlanet);

    for (int i = 0; i < moonCount; ++i) {
      double w = 2.0 * Math.PI * i / (double) moonCount;
      double s = Math.sin(w);
      double c = Math.cos(w);

      Vector3 x = new Vector3(1, -0.5, 0).normalize().times(2);
      Vector3 y = new Vector3(0, 0, 1).normalize().times(2);
      Vector3 p = center.plus(x.times(s)).plus(y.times(c));

      addSphere(p, moonSize, materialMoons);
    }
  }

  public void addCheckerBoard(Vector3 center, Vector3 ext1, Vector3 ext2, int n,
      RayTracingMaterial material1, RayTracingMaterial material2) {

    Vector3 ext1Half = ext1.times(0.5);
    Vector3 ext2Half = ext2.times(0.5);

    center = center
        .minus(ext1Half.times(n - 1))
        .minus(ext2Half.times(n - 1));

    for (int x = 0; x < n; ++x) {
      for (int y = 0; y < n; ++y) {
        addQuad(center.plus(ext1.times(x))
                .plus(ext2.times(y)),
            ext1Half, ext2Half, true,
            (x + y) % 2 == 0 ? material1 : material2);
      }
    }
  }

  public void addSphere(Vector3 center, double radius, RayTracingMaterial material) {
    scene.addObject(new Sphere(center, radius), material);
  }

  public void addQuad(Vector3 center, Vector3 ext1, Vector3 ext2, boolean flipFace,
      RayTracingMaterial material) {
    if (flipFace) {
      scene.addObject(new Triangle(
          center.minus(ext1).plus(ext2),
          center.minus(ext1).minus(ext2),
          center.plus(ext1).plus(ext2)
      ), material);

      scene.addObject(new Triangle(
          center.plus(ext1).minus(ext2),
          center.plus(ext1).plus(ext2),
          center.minus(ext1).minus(ext2)
      ), material);
    } else {
      scene.addObject(new Triangle(
          center.minus(ext1).minus(ext2),
          center.minus(ext1).plus(ext2),
          center.plus(ext1).plus(ext2)
      ), material);

      scene.addObject(new Triangle(
          center.plus(ext1).plus(ext2),
          center.plus(ext1).minus(ext2),
          center.minus(ext1).minus(ext2)
      ), material);
    }
  }

  public void addCube(Vector3 center, Vector3 extent, RayTracingMaterial... faceMaterials) {
    Vector3 x = new Vector3(1, 0, 0).multElementWise(extent);
    Vector3 y = new Vector3(0, 1, 0).multElementWise(extent);
    Vector3 z = new Vector3(0, 0, 1).multElementWise(extent);

    Iterator<RayTracingMaterial> materials = new Iterator<RayTracingMaterial>() {
      private int i = 0;

      @Override
      public boolean hasNext() {
        return false;
      }

      @Override
      public RayTracingMaterial next() {
        return faceMaterials[(i++) % faceMaterials.length];
      }
    };

    addQuad(center.plus(x), y, z, false, materials.next());
    addQuad(center.minus(x), y, z, true, materials.next());

    addQuad(center.plus(y), x, z, true, materials.next());
    addQuad(center.minus(y), x, z, false, materials.next());

    addQuad(center.plus(z), x, y, false, materials.next());
    addQuad(center.minus(z), x, y, true, materials.next());
  }

  public static final RayTracingMaterial[] COLORS = {
      RayTracingMaterial.DefaultColored(new RGBA(0xFF61CBF4)),
      RayTracingMaterial.DefaultColored(new RGBA(0xFFA374D1)),  // Z+
      RayTracingMaterial.DefaultColored(new RGBA(0xFFACF562)),  // Y+
      RayTracingMaterial.DefaultColored(new RGBA(0xFFF27B60)),
      RayTracingMaterial.DefaultColored(new RGBA(0xFF286651)),  // X+
      RayTracingMaterial.DefaultColored(new RGBA(0xFFE5DB6B))
  };
}
