package exercises;

import exercises.ex5Tests.*;
import illumination.DirectionalLight;
import image.RGBA;
import raytracing.*;
import testSuite.VisualTestSuite;
import testSuite.testTemplates.VisualTest;
import utils.Vector3;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Supplier;

public class Ex5TestSuite {

  private static final String EX5_DIR = "data/resources/ex5/";
  private static final String GS_DIR = EX5_DIR + "gs/";

  private static final boolean USE_HD_CUBEMAPS = true;

  public static void main(String args[]) {
    final String cubeMapPath = EX5_DIR + "cubemaps-" + (USE_HD_CUBEMAPS ? "1024" : "512") + "/";

    final Lazy<EnvironmentMap<RGBA>> cubeMapBeach = loadCubeMapLazily(
        cubeMapPath + "NissiBeach2/%s.jpg");
    final Lazy<EnvironmentMap<RGBA>> cubeMapStairs = loadCubeMapLazily(
        cubeMapPath + "Stairs/%s.jpg");
    final Lazy<EnvironmentMap<RGBA>> cubeMapLake = loadCubeMapLazily(
        cubeMapPath + "Lycksele/%s.jpg");

    final DirectionalLight lightSource = new DirectionalLight(new Vector3(1, -1, 0), RGBA.white);
    final DirectionalLight lightSource1 = new DirectionalLight(new Vector3(1, -1, 0.5), RGBA.white);

    VisualTest[] tests = {
        new Test1Intersections(GS_DIR + "intersections.png", "Raycasting 101: Intersections"),
        new Test2IntersectionsScene(GS_DIR + "intersections-sorted.png",
            "Depth-Sorted Intersections"),
        new RayTracingTestBase(GS_DIR + "ray-casting-unlit.png", "Ray-Cast Scene (Unlit)") {
          @Override
          protected void buildWidget(JPanel panel) {
            addRotationSpinners(panel);
            addZSpinner(panel, -10, 10);
          }
        },
        new RayTracingTestBase(GS_DIR + "ray-casting.png", "Ray-Cast Scene") {
          @Override
          protected void setupRayTracer() {
            rayTracer.setLightSource(Optional.of(lightSource));
          }
        },
        new RayTracingTestBase(GS_DIR + "ray-tracing.png", "Ray-Traced Scene") {
          @Override
          protected void setupRayTracer() {
            rayTracer.setLightSource(Optional.of(lightSource));
            rayTracer.setRayTracingEnabled(true);
            rayTracer.setRayTraceDepth(5);
          }

          @Override
          protected void buildWidget(JPanel panel) {
            addRotationSpinners(panel);
            addZSpinner(panel);
            addRecursionDepthSpinner(panel);
          }
        },
        new RayTracingTestBase(GS_DIR + "environment-mapping.png", "Environment Mapping") {
          @Override
          protected void setupRayTracer() {
            rayTracer.setLightSource(Optional.of(lightSource));
            rayTracer.setEnvironmentMap(Optional.of(cubeMapBeach.get()));
            rayTracer.setRayTracingEnabled(true);
            rayTracer.setRayTraceDepth(5);
          }

          @Override
          protected void buildWidget(JPanel panel) {
            addRotationSpinners(panel);
            addZSpinner(panel);
            addRecursionDepthSpinner(panel);
          }
        },
        new ShadowingTestBase(GS_DIR + "shadows.png", "Shadows"),
        new ShadowingTestBase(GS_DIR + "shadows-soft.png", "Shadows (Soft)") {
          @Override
          protected void setupRayTracer() {
            super.setupRayTracer();
            rayTracer.setSoftShadowsEnabled(true);
          }

          @Override
          protected void buildWidget(JPanel panel) {
            super.buildWidget(panel);

            panel.add(new Label("Shadow Softness"));
            panel.add(createSpinner(rayTracer.getShadowSoftness(), 0, 0.2, 0.01,
                d -> rayTracer.setShadowSoftness(d)));

            panel.add(new Label("Soft Shadow Samples"));
            panel.add(createSpinner(rayTracer.getSoftShadowSamples(), 5, 100, 5,
                d -> rayTracer.setSoftShadowSamples((int) (double) d)));

          }
        },
        new RayTracingTestBase(GS_DIR + "refractions.png", "Refraction") {
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

          @Override
          protected void buildWidget(JPanel panel) {
            super.buildWidget(panel);

            panel.add(new Label("Material Density"));
            panel.add(createSpinner(transparentMaterial.getDensity(), 0.1, 2, 0.1,
                transparentMaterial::setDensity));
          }
        },
        new RayTracingTestBase(GS_DIR + "rough-reflections.png", "Rough Reflections") {
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

          @Override
          protected void buildWidget(JPanel panel) {
            super.buildWidget(panel);

            panel.add(new Label("Reflection Roughness"));
            panel.add(createSpinner(rayTracer.getRoughReflectionRoughness(), 0, 0.5, 0.05,
                d -> rayTracer.setRoughReflectionRoughness(d)));

            panel.add(new Label("Rough Reflection Samples"));
            panel.add(createSpinner(rayTracer.getRoughReflectionSamples(), 5, 100, 5,
                d -> rayTracer.setRoughReflectionSamples((int) (double) d)));

          }
        },
        new RayTracingTestBase(GS_DIR + "depth-of-field.png", "Depth of Field") {
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

          @Override
          protected void buildWidget(JPanel panel) {
            super.buildWidget(panel);

            panel.add(new Label("DOF Strength"));
            panel.add(createSpinner(rayTracer.getDepthOfField(), 0.05, 0.2, 0.01,
                d -> rayTracer.setDepthOfField(d)));

            panel.add(new Label("DOF Focal Length"));
            panel.add(createSpinner(rayTracer.getDepthOfFieldFocalLength(), 0.1, 10, 1,
                d -> rayTracer.setDepthOfFieldFocalLength(d)));

            panel.add(new Label("DOF Samples"));
            panel.add(createSpinner(rayTracer.getDepthOfFieldSamples(), 5, 100, 5,
                d -> rayTracer.setDepthOfFieldSamples((int) (double) d)));

          }
        },
        new RayTracingTestBase(GS_DIR + "supersampling.png", "Adaptive Supersampling") {
          @Override
          protected void setupRayTracer() {
            rayTracer.setLightSource(Optional.of(lightSource));
            rayTracer.setRayTracingEnabled(true);
            rayTracer.setRayTraceDepth(5);
            rayTracer.setAdaptiveSupersamplingEnabled(true);
          }

          @Override
          protected void buildWidget(JPanel panel) {
            addRotationSpinners(panel);

            panel.add(new Label("Threshold"));
            panel.add(createSpinner(rayTracer.getAdaptiveSupersamplingThreshold(), 0.0, 1, 0.1,
                d -> rayTracer.setAdaptiveSupersamplingThreshold(d)));

            panel.add(new Label("Samples"));
            panel.add(createSpinner(rayTracer.getAdaptiveSupersamplingSamples(), 3, 8, 1,
                d -> rayTracer.setAdaptiveSupersamplingSamples((int) (double) d)));
          }
        },
    };

    new VisualTestSuite(tests);
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


}
