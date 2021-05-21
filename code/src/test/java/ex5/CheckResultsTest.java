package ex5;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import exercises.ex5Tests.RayTracingTestBase;
import exercises.ex5Tests.Test1Intersections;
import exercises.ex5Tests.Test2IntersectionsScene;
import exercises.ex5Tests.TestSceneBuilder;
import illumination.DirectionalLight;
import image.Image;
import image.ImageUtils;
import image.RGBA;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Supplier;
import org.junit.Test;
import raytracing.CubeMap;
import raytracing.EnvironmentMap;
import raytracing.RayTracingMaterial;
import testSuite.testTemplates.VisualTest;
import utils.Vector3;

public class CheckResultsTest {

  private String pathToResources = "data/resources/ex5/";
  private String pathToGoldstandard = pathToResources + "gs/";

  final DirectionalLight lightSource = new DirectionalLight(new Vector3(1, -1, 0), RGBA.white);
  final DirectionalLight lightSource1 = new DirectionalLight(new Vector3(1, -1, 0.5), RGBA.white);

  final String cubeMapPath = pathToResources + "cubemaps-1024/";

  final Lazy<EnvironmentMap<RGBA>> cubeMapBeach = loadCubeMapLazily(
      cubeMapPath + "NissiBeach2/%s.jpg");
  final Lazy<EnvironmentMap<RGBA>> cubeMapStairs = loadCubeMapLazily(
      cubeMapPath + "Stairs/%s.jpg");
  final Lazy<EnvironmentMap<RGBA>> cubeMapLake = loadCubeMapLazily(
      cubeMapPath + "Lycksele/%s.jpg");

  private static class Lazy<T> implements Supplier<T> {

    private final Supplier<T> supplier;

    private boolean available = false;
    private T value = null;

    Lazy(Supplier<T> supplier) {
      this.supplier = supplier;
    }

    @Override
    public T get() {
      if (!available) {
        value = supplier.get();
      }
      available = true;
      return value;
    }
  }

  private void testScene(RayTracingTestBase test) {
    try {
      Method m = RayTracingTestBase.class.getDeclaredMethod("setupTestIfNotSetup");
      m.setAccessible(true);
      m.invoke(test);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
      fail();
    }

    Image<RGBA> img = test.renderImage();
    try {
      ImageUtils.write(img, "raytracer_test.png");
      System.out.println("Wrote file");
    } catch (IOException e) {
      e.printStackTrace();
    }

    Image<RGBA> groundTruth = null;
    try {
      groundTruth = ImageUtils.read(test.getgsName());
    } catch (IOException e) {
      // TODO: handle exception
      fail();
    }

    for (int x = 0; x < img.cols(); ++x) {
      for (int y = 0; y < img.rows(); ++y) {
        assertEquals("Failed at " + x + "/" + y, groundTruth.get(x, y), img.get(x, y));
      }
    }
  }

  private void testIntersection(VisualTest test) {
    Image<RGBA> img = null;
    try {
      Method m = test.getClass().getDeclaredMethod("drawTestImage");
      m.setAccessible(true);

      img = (Image<RGBA>) m.invoke(test);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
      fail();
    }

    Image<RGBA> groundTruth = null;
    try {
      groundTruth = ImageUtils.read(test.getgsName());
    } catch (IOException e) {
      // TODO: handle exception
      fail();
    }

    for (int x = 0; x < img.cols(); ++x) {
      for (int y = 0; y < img.rows(); ++y) {
        assertEquals(groundTruth.get(x, y), img.get(x, y));
      }
    }
  }

  @Test
  public void intersectionsTest() {
    testIntersection(new Test1Intersections(pathToGoldstandard + "intersections.png", "title"));
  }

  @Test
  public void intersectionsSceneTest() {
    testIntersection(
        new Test2IntersectionsScene(pathToGoldstandard + "intersections-sorted.png", "title"));
  }

  @Test
  public void rayCastingUnlitTest() {
    testScene(new RayTracingTestBase(pathToGoldstandard + "ray-casting-unlit.png", "title"));
  }

  @Test
  public void rayCastingTest() {
    testScene(new RayTracingTestBase(pathToGoldstandard + "ray-casting.png", "title") {
      @Override
      protected void setupRayTracer() {
        rayTracer.setLightSource(Optional.of(lightSource));
      }
    });
  }

  @Test
  public void rayTracingTest() {
    testScene(new RayTracingTestBase(pathToGoldstandard + "ray-tracing.png", "title") {
      @Override
      protected void setupRayTracer() {
        rayTracer.setLightSource(Optional.of(lightSource));
        rayTracer.setRayTracingEnabled(true);
        rayTracer.setRayTraceDepth(5);
      }
    });
  }

  @Test
  public void environmentMappingTest() {
    testScene(new RayTracingTestBase(pathToGoldstandard + "environment-mapping.png", "title") {
      @Override
      protected void setupRayTracer() {
        rayTracer.setLightSource(Optional.of(lightSource));
        rayTracer.setEnvironmentMap(Optional.of(cubeMapBeach.get()));
        rayTracer.setRayTracingEnabled(true);
        rayTracer.setRayTraceDepth(5);
      }
    });
  }

  @Test
  public void shadowsTest() {
    testScene(new ShadowingTestBase(pathToGoldstandard + "shadows.png", "title"));
  }

  @Test
  public void softShadowsTest() {
    testScene(new ShadowingTestBase(pathToGoldstandard + "shadows-soft.png", "title") {
      @Override
      protected void setupRayTracer() {
        super.setupRayTracer();
        rayTracer.setSoftShadowsEnabled(true);
      }
    });
  }

  @Test
  public void refractionsTest() {
    testScene(new RayTracingTestBase(pathToGoldstandard + "refractions.png", "title") {
      private final RayTracingMaterial transparentMaterial =
          RayTracingMaterial.Transparent(0.95, 0.4, 1.2);

      @Override
      protected void setupView() {
        rotationX = 20 / 180.0 * Math.PI;
        rotationY = 70 / 180.0 * Math.PI;
        translation = new Vector3(0, 0, -11);
      }

      @Override
      protected void setupRayTracer() {
        rayTracer.setLightSource(Optional.of(lightSource1));
        rayTracer.setEnvironmentMap(Optional.of(cubeMapStairs.get()));
        rayTracer.setRayTracingEnabled(true);
        rayTracer.setRayTraceDepth(5);
      }

      @Override
      protected void setupScene(TestSceneBuilder sceneBuilder) {
        sceneBuilder.addSphere(new Vector3(-3, 0, 0), 1.4, transparentMaterial);
        sceneBuilder.addCube(new Vector3(3, 0, 0), Vector3.one, transparentMaterial);
//                        sceneBuilder.addSphere(new Vector3(3, 0, 0), 1.4, transparentMaterial);

        sceneBuilder.addCube(Vector3.zero, Vector3.one, TestSceneBuilder.COLORS);
      }
    });
  }

  @Test
  public void roughReflectionsTest() {
    testScene(new RayTracingTestBase(pathToGoldstandard + "rough-reflections.png", "title") {
      @Override
      protected void setupRayTracer() {
        super.setupRayTracer();
        rayTracer.setLightSource(
            Optional.of(new DirectionalLight(new Vector3(1, -1, 0).normalize(), RGBA.white)));
        rayTracer.setEnvironmentMap(Optional.of(cubeMapLake.get()));
        rayTracer.setRayTracingEnabled(true);
        rayTracer
            .setRayTraceDepth(2); // Avoid deep recursion when using many reflection samples

        rayTracer.setRoughReflectionsEnabled(true);
      }

      @Override
      protected void setupScene(TestSceneBuilder sceneBuilder) {
        sceneBuilder
            .addCheckerBoard(Vector3.zero, new Vector3(1.2, 0, 0), new Vector3(0, 0, 1.2), 5,
                RayTracingMaterial.DefaultColored(RGBA.grey)
                    .setReflectance(RGBA.white.times(0.75)),
                RayTracingMaterial.DefaultColored(RGBA.black)
                    .setReflectance(RGBA.white.times(0.75)));

        sceneBuilder.addPlanetStyleSpheres(new Vector3(0, 1.5, 0), 18, 1, 0.1,
            Materials.GOLD, Materials.SILVER);
      }
    });
  }

  @Test
  public void depthOfFieldTest() {
    testScene(new RayTracingTestBase(pathToGoldstandard + "depth-of-field.png", "title"){
      @Override
      protected void setupView() {
        rotationX = 20 / 180.0 * Math.PI;
        rotationY = 75 / 180.0 * Math.PI;
        translation = new Vector3(0, 0, -6);
      }

      @Override
      protected void setupRayTracer() {
        rayTracer.setLightSource(Optional.of(lightSource));
        rayTracer.setEnvironmentMap(Optional.of(cubeMapBeach.get()));
        rayTracer.setRayTracingEnabled(true);
        rayTracer.setRayTraceDepth(5);
        rayTracer.setDepthOfFieldEnabled(true);
      }

      @Override
      protected void setupScene(TestSceneBuilder sceneBuilder) {
        sceneBuilder.addSphere(new Vector3(-3, 0, 0), 1.5, Materials.SILVER);
        sceneBuilder.addSphere(new Vector3(3, 0, 0), 1.5, Materials.GOLD);
        sceneBuilder.addCube(Vector3.zero, Vector3.one, TestSceneBuilder.COLORS);
      }
    });
  }

  @Test
  public void antialiasingTest() {
    testScene(new RayTracingTestBase(pathToGoldstandard + "supersampling.png", "title"){
      @Override
      protected void setupRayTracer() {
        rayTracer.setLightSource(Optional.of(lightSource));
        rayTracer.setRayTracingEnabled(true);
        rayTracer.setRayTraceDepth(5);
        rayTracer.setAdaptiveSupersamplingEnabled(true);
      }
    });
  }

  // Test Helper Code...
  private static class ShadowingTestBase extends RayTracingTestBase {

    ShadowingTestBase(String gsFileName, String title) {
      super(gsFileName, title);
    }

    @Override
    protected void setupScene(TestSceneBuilder sceneBuilder) {
      RGBA reflectance = RGBA.white.times(0.1);

      sceneBuilder.addCheckerBoard(Vector3.zero, new Vector3(1.2, 0, 0), new Vector3(0, 0, 1.2), 5,
          RayTracingMaterial.DefaultColored(RGBA.white).setReflectance(reflectance),
          RayTracingMaterial.DefaultColored(RGBA.black).setReflectance(reflectance));

      sceneBuilder.addPlanetStyleSpheres(new Vector3(0, 1.5, 0), 24, 1, 0.1,
          RayTracingMaterial.DefaultColored(RGBA.red).setReflectance(reflectance),
          RayTracingMaterial.DefaultColored(RGBA.blue).setReflectance(reflectance));
    }

    @Override
    protected void setupRayTracer() {
      rayTracer.setLightSource(
          Optional.of(new DirectionalLight(new Vector3(1, -1, 0.5).normalize(), RGBA.white)));
      rayTracer.setRayTracingEnabled(true);
      rayTracer.setRayTraceDepth(5);
      rayTracer.setShadowsEnabled(true);
      rayTracer.setAmbientLight(0.2);
    }
  }

  private static final class Materials {

    static final RayTracingMaterial GOLD = RayTracingMaterial
        .DefaultColored(new RGBA(0xFFD700).times(0.1))
        .setReflectance(new RGBA(0xFFD700).times(0.9));

    static final RayTracingMaterial SILVER = RayTracingMaterial
        .DefaultColored(RGBA.white.times(0.2))
        .setReflectance(RGBA.white.times(0.5));
  }

  // Tries to load a cubemap. Will return a dummy environment map if cubemap couldn't be loaded.
  private static Lazy<EnvironmentMap<RGBA>> loadCubeMapLazily(String pathPattern) {
    return new Lazy<>(() -> {
      EnvironmentMap<RGBA> environmentMap = EnvironmentMap.uniform(RGBA.magenta);

      try {
        environmentMap = CubeMap.load(pathPattern);
      } catch (IOException e) {
        e.printStackTrace();
        System.err.println("[ERROR] Couldn't load cube-map!");
        System.err.println("[INFO]Will use dummy environment map!");
      }

      return environmentMap;
    });
  }
}
