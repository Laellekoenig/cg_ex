package rasterization;

import image.Image;
import mesh.Mesh;
import projection.Projection;
import utils.*;

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

      currentMesh = mesh;

      //go through triangles in mesh
      for (int i = 0; i < mesh.tvi.length; i++) {
        currentTriangle = i;

        //get points of current triangle
        Vector3 one = mesh.vertices[mesh.tvi[i].get(0)];
        Vector3 two = mesh.vertices[mesh.tvi[i].get(1)];
        Vector3 three = mesh.vertices[mesh.tvi[i].get(2)];

        //apply projection: world space -> screen space
        one = p.project(one);
        two = p.project(two);
        three = p.project(three);

        //apply clipping
        if (!(one.z > (-1 * cNear) || two.z > (-1 * cNear) || three.z > (-1 * cNear))) {

          //save for z-buffer
          currentDepths[0] = one.z;
          currentDepths[1] = two.z;
          currentDepths[2] = three.z;

          //now "inhomogenize" z values, since this was not done in projection -> always = 1
          //(this was done so the pyramid wireframe would still render)
          Vector3 pointOne = new Vector3(one.x, one.y, 1);
          Vector3 pointTwo = new Vector3(two.x, two.y, 1);
          Vector3 pointThree = new Vector3(three.x, three.y, 1);

          BarycentricCoordinateTransform bct = new BarycentricCoordinateTransform(new Vector2(pointOne.x, pointOne.y), new Vector2(pointTwo.x, pointTwo.y),
                  new Vector2(pointThree.x, pointThree.y));

          // Terrible runtime, but maybe it works?
          for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
              BarycentricCoordinates bc = bct.getBarycentricCoordinates(x, y);
              handleTrianglePixel(x, y, bc);
            }
          }

          Vector2[] triangle = new Vector2[] {new Vector2(pointOne.x, pointOne.y), new Vector2(pointTwo.x, pointTwo.y),
                  new Vector2(pointThree.x, pointThree.y)};

          r.rasterTriangle(triangle);
        }
      }
    }

    return correspondenceImage;
  }

  public void enablePerspectiveCorrect(boolean b) {
    perspectiveCorrect = b;
  }

  @Override
  public void handleTrianglePixel(int x, int y, BarycentricCoordinates triCoords) {

    //TODO: Blatt 3, Aufgabe 3 b)

    //interpolate depth as described with the help of the previously saved z values
    double interpolatedDepth = triCoords.interpolate(currentDepths[0], currentDepths[1], currentDepths[2]);
    interpolatedDepth *= zd;

    //dont draw behind camera
    if (interpolatedDepth < 0) return;

    //check if pixel is already drawn
    //if already drawn, compare interpolated z values and keep the one that is closest to the camera
    if ((correspondenceImage.get(x, y) == null || correspondenceImage.get(x, y).depth > interpolatedDepth)) {
      correspondenceImage.set(x, y, new Correspondence(currentMesh, currentTriangle, triCoords, interpolatedDepth));
    }
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
