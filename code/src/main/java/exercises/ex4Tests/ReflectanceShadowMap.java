package exercises.ex4Tests;

import image.Image;
import occlusion.Occlusion;
import occlusion.ShadowType;
import rasterization.Correspondence;
import renderer.ReflectanceMeshRenderer;

public class ReflectanceShadowMap extends PhongShadowMap {


  public ReflectanceShadowMap(String string, String string2) {
    super(string, string2);
  }

  @Override
  protected void _draw() {
    ReflectanceMeshRenderer renderer = new ReflectanceMeshRenderer(gs.getWidth(), gs.getHeight(),
        meshes);

    Occlusion o = new Occlusion(480, 480, ShadowType.HARD, shadowBias, 1);
    o.generateShadowMap(projection, renderer.lightSources.get(0), meshes);

    Image<Correspondence> shadowMap = o.shadowMap;

    this.drawn = getShadowMapImage(shadowMap);
    fireDrawEvent();
  }

}
