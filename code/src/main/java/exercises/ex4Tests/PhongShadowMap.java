package exercises.ex4Tests;

import image.Image;
import image.RGBA;
import mesh.Mesh;
import mesh.MeshReader;
import occlusion.Occlusion;
import occlusion.ShadowType;
import rasterization.Correspondence;
import renderer.PhongMeshRenderer;
import testSuite.testTemplates.MeshTest;

public class PhongShadowMap extends MeshTest {

  protected double shadowBias = 0.05;

  public PhongShadowMap(String gsFileName, String title) {
    super(gsFileName, title);
  }

  @Override
  protected void _draw() {
    PhongMeshRenderer renderer = new PhongMeshRenderer(gs.getWidth(), gs.getHeight(), meshes,
        new RGBA(1, 1, 1), 1);

    Occlusion o = new Occlusion(480, 480, ShadowType.HARD, shadowBias, 1);
    o.generateShadowMap(projection, renderer.getLightSource(), meshes);

    Image<Correspondence> shadowMap = o.shadowMap;

    this.drawn = getShadowMapImage(shadowMap);
    fireDrawEvent();
  }


  protected Image<RGBA> getShadowMapImage(Image<Correspondence> shadowMap) {
    double min = 0;
    double max = 0;

    Image<RGBA> drawing = new Image<RGBA>(shadowMap.rows(), shadowMap.cols());
    double greyValue;
    for (int x = 0; x < shadowMap.rows(); ++x) {
      for (int y = 0; y < shadowMap.cols(); ++y) {
        if (shadowMap.get(x, y) != null) {
          greyValue = -shadowMap.get(x, y).depth;
          if (greyValue > max) {
            max = greyValue;
          }
          if (greyValue < min) {
            min = greyValue;
          }
        }
      }
    }
    for (int x = 0; x < shadowMap.rows(); ++x) {
      for (int y = 0; y < shadowMap.cols(); ++y) {
        if (shadowMap.get(x, y) != null) {
          greyValue = 1.0 - ((-shadowMap.get(x, y).depth) - min) / (max - min);
          drawing.set(x, y, new RGBA(greyValue, greyValue, greyValue));
        }
      }
    }
    return drawing;
  }

  @Override
  protected Mesh[] createMeshes() {
    Mesh mesh = MeshReader.loadObj("data/resources/Teapot.obj").get(0);
    Mesh mesh2 = MeshReader.loadObj("data/resources/TeapotCube.obj").get(1);
    return new Mesh[]{mesh, mesh2};
  }

}
