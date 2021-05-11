package occlusion;

import illumination.PointLight;
import image.Image;
import mesh.Mesh;
import projection.PinholeProjection;
import projection.Projection;
import rasterization.Correspondence;
import rasterization.MeshRasterizer;
import rasterization.TriangleRasterizer;
import renderer.MeshRenderer;
import utils.*;

public class Occlusion {

  public Image<Correspondence> shadowMap;
  private PinholeProjection shadowProjection;

  private int height, width;
  private double shadowBias;
  private int pcfMask;
  private ShadowType shadowType;

  public Occlusion(int height, int width, ShadowType shadowType, double shadowBias, int pcfMask) {
    this.height = height;
    this.width = width;
    this.shadowType = shadowType;
    this.pcfMask = pcfMask;
    this.shadowBias = shadowBias;
  }

  public void generateShadowMap(Projection projection, PointLight lightSource, Mesh[] meshes) {

    //TODO: Blatt 4, Aufgabe 6 b)
    // The map in the suite looks a bit off, the colours are too bright.

    shadowProjection = new PinholeProjection(width, height);
    shadowProjection.setView(projection.getViewMatrixOfLightSource(lightSource));

    MeshRasterizer rasterizer = new MeshRasterizer(width, height);

    shadowMap = rasterizer.rasterize(shadowProjection, meshes);
  }

  /**
   * Returns if the position is in shadow or not:
   * 1 = completely visible
   * 0 = completely in the shadow(s)
   */
  public double inShadow(Vector3 position) {

    //TODO: Blatt 4, Aufgabe 6 c)
    //TODO: Blatt 4, Aufgabe 7

    //Vector3 pixel = position.times(1 / position.z);

    Vector3 projected = shadowProjection.project(position);
    projected = projected.times(1 / projected.z);

    if (shadowMap.get((int) projected.x, (int) projected.y) != null && position.z < shadowMap.get((int) projected.x, (int) projected.y).depth) {
      return 0.0;
    }

    return 1.0;
  }
}
