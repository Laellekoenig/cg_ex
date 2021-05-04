package occlusion;

import illumination.PointLight;
import image.Image;
import mesh.Mesh;
import projection.PinholeProjection;
import projection.Projection;
import rasterization.Correspondence;
import rasterization.MeshRasterizer;
import rasterization.TriangleRasterizer;
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

    shadowProjection = new PinholeProjection(width, height);
    shadowProjection.setView(projection.getViewMatrixOfLightSource(lightSource));

    shadowMap = new Image<Correspondence>(width, height);

    for(Mesh mesh : meshes) {
      for(int i = 0; i < mesh.vertices.length; i++) {
        Vector3 projection3d = shadowProjection.project(mesh.vertices[i]);
        Correspondence c = new Correspondence();
        c.depth = projection3d.z;
        if (projection3d.x < width && projection3d.y < height) {
          shadowMap.set((int) projection3d.x, (int) projection3d.y, c);
        }
      }
    }
  }

  /**
   * Returns if the position is in shadow or not:
   * 1 = completely visible
   * 0 = completely in the shadow(s)
   */
  public double inShadow(Vector3 position) {

    //TODO: Blatt 4, Aufgabe 6 c)
    //TODO: Blatt 4, Aufgabe 7

    return 1.0;
  }
}
