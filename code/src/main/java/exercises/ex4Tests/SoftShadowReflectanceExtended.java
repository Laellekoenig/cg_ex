package exercises.ex4Tests;

import illumination.PointLight;
import image.RGBA;
import occlusion.ShadowType;
import reflectance.CookTorrance;
import reflectance.LambertBrdf;
import utils.Vector3;

public class SoftShadowReflectanceExtended extends SoftShadowReflectanceRenderer {


  public SoftShadowReflectanceExtended(String gsFileName, String title) {
    super(gsFileName, title);

    renderer.enableShadow();
    renderer.setShadowBias(shadowBias);
    renderer.setPCFMaskSize(pcfMask);
    renderer.setShadowType(ShadowType.SOFT);
    ;

    renderer.matBrdf.clear();
    renderer.matBrdf.add(new CookTorrance(new RGBA(0.1, 0.1, 0.1), 0.1, 0.2));
    renderer.matBrdf.add(new LambertBrdf(new RGBA(0.4, 0.4, 0.4)));
    renderer.lightSources.clear();
    renderer.lightSources.add(new PointLight(new Vector3(-4, 4, 5), new RGBA(0.5, 0.4, 0.4)));
    renderer.lightSources.add(new PointLight(new Vector3(-3.5, 4, 5), new RGBA(0.5, 0.4, 0.4)));
    renderer.lightSources.add(new PointLight(new Vector3(-3, 4, 5), new RGBA(0.5, 0.4, 0.4)));
    renderer.lightSources.add(new PointLight(new Vector3(4, 4, 5), new RGBA(0.4, 0.4, 0.5)));
  }

}
