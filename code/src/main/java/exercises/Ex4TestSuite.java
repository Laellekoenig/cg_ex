package exercises;

import exercises.ex4Tests.*;
import testSuite.VisualTestSuite;

public class Ex4TestSuite {

  public static void main(String args[]) {

    String dir = "data/resources/ex4";

    new VisualTestSuite(
        /* == Lighting == */
        new TeapotLambertTest(dir + "/LambertRenderer.png", "Lambert Renderer"),
        new TeapotPhongShadingTest(dir + "/PhongReflectanceModel.png", "Phong Reflectance Model"),
        new TeapotReflectanceLambert(dir + "/LambertRenderer.png", "Reflectance Renderer: Lambert",
            false),
        new TeapotReflectanceLambert(dir + "/ReflectanceRendererMultipleLightSources.png",
            "Reflectance Renderer: Lambert with multiple lightsources", true),
        new TeapotReflectanceCookTorranceTest(dir + "/Cook-Torrance.png",
            "Cook-Torrance: Lambert + Cook-Torrance"),
        new TeapotReflectanceOrenNayarTest(dir + "/OrenNayar.png", "OrenNayar"),
        new TeapotCookTorranceOrenNayar(dir + "/Cook-TorranceOrenNayar.png",
            "Cook-Torrance + OrenNayar"),

        /* == Shadows == */
        //Phong
        new PhongTest(dir + "/PhongTest.png", "Phong: Rendering"),
        new PhongFromLight(dir + "/PhongFromLight.png", "Phong: Directon of LightSource"),
        new PhongShadowMap(dir + "/ShadowMap.png", "Phong: ShadowMap"),
        new PhongShadow(dir + "/PhongShadow.png", "Phong: Shadow Renderer"),

        //Reflecance
        new ReflectanceLambert(dir + "/ReflectanceTest.png", "Reflectance: Rendering"),
        new ReflectanceLambertFromLight(dir + "/DirectonOfLightSource.png",
            "Reflectance: Direction of LightSource"),
        new ReflectanceShadowMap(dir + "/ShadowMap.png", "Reflectance: ShadowMap"), //unnötig
        new ReflectanceShadow(dir + "/ReflectanceShadow.png", "Reflectance: Shadow Renderer"),

        //Soft Shadows
        new SoftShadowPhongRenderer(dir + "/SoftShadowPhong.png", "Soft Shadow: Phong Renderer"),
        new SoftShadowReflectanceRenderer(dir + "/SoftShadowReflectance.png",
            "Soft Shadow: Reflectance Renderer"),

        //Soft Shadow Extended
        new SoftShadowReflectanceExtended(dir + "/SoftShadowExtended.png",
            "Soft Shadow: Cook-Torrance + OrenNayar")
    );
  }
}
