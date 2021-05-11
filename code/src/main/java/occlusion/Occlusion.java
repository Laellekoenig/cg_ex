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

    Vector3 tPosition = shadowProjection.project(position);

    // point of position in shadow map
    Correspondence pointInSm = shadowMap.get((int) tPosition.x, (int) tPosition.y);

    // if the shadow map has the point and the depth of the analyzed position is greater than that point, the position
    // isn't lighted up.
    if (pointInSm != null && pointInSm.depth <= (-1) * tPosition.z) {
      return 0;
    }

    return 1.0;
  }
}
