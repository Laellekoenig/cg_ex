package rasterization;

import image.Image;
import mesh.Mesh;
import projection.Projection;
import utils.BarycentricCoordinates;
import utils.Vector2;
import utils.Vector3;
import utils.Triplet;

/**
 * Rasterizes meshes into correspondence images
 */
public class MeshRasterizer implements TrianglePixelHandler {

  public static enum ZDirection {
    Forward,
    Backward;
  }

  protected Image<Correspondence> correspondenceImage;
  protected ZDirection zDir;
  protected double zd;
  protected Mesh currentMesh;
  protected int currentTriangle;
  protected double[] currentDepths;
  protected float cNear;
  protected boolean perspectiveCorrect;
  protected int width, height;

  public MeshRasterizer(int w, int h) {
    width = w;
    height = h;
    zDir = ZDirection.Backward;
    zd = (zDir == ZDirection.Forward) ? 1.0 : -1.0;
    currentDepths = new double[3];
    cNear = 1.0f;
    perspectiveCorrect = false;
  }

  /**
   * Rasterizes a single mesh into the correspondence image
   */
  public Image<Correspondence> rasterize(Projection p, Mesh[] meshes) {
    correspondenceImage = new Image<Correspondence>(width, height);
    TriangleRasterizer r = new TriangleRasterizer(this, width, height);
    return rasterize(p, r, meshes);
  }

  public Image<Correspondence> rasterize(Projection p, TriangleRasterizer r, Mesh[] meshes) {

    for (Mesh mesh : meshes) {
      //TODO: Blatt 3, Aufgabe 3 a)
      //TODO: Blatt 3, Aufgabe 4
    }

    return correspondenceImage;
  }

  public void enablePerspectiveCorrect(boolean b) {
    perspectiveCorrect = b;
  }

  @Override
  public void handleTrianglePixel(int x, int y, BarycentricCoordinates triCoords) {

    //TODO: Blatt 3, Aufgabe 3 b)
  }

  private BarycentricCoordinates getWorldLambda(BarycentricCoordinates oldLambda) {

    double w0 = currentDepths[0];
    double w1 = currentDepths[1];
    double w2 = currentDepths[2];

    double d = w1 * w2 + w2 * oldLambda.y * (w0 - w1) + w1 * oldLambda.z * (w0 - w2);

    if (d == 0) {
      return oldLambda;
    }

    double y = w0 * w2 * oldLambda.y / d;
    double z = w0 * w1 * oldLambda.z / d;
    double x = 1 - y - z;

    return new BarycentricCoordinates(x, y, z);
  }
}
