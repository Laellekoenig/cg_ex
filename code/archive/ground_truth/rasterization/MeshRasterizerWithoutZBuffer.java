package rasterization;

import image.Image;
import mesh.Mesh;
import projection.Projection;
import utils.BarycentricCoordinates;

/**
 * Rasterizes meshes into correspondence images
 */
public class MeshRasterizerWithoutZBuffer extends MeshRasterizer {

  public MeshRasterizerWithoutZBuffer(int w, int h) {
    super(w, h);
  }

  @Override
  public Image<Correspondence> rasterize(Projection p, Mesh[] meshes) {
    correspondenceImage = new Image<Correspondence>(width, height);
    TriangleRasterizer r = new TriangleRasterizer(this, width, height);
    return rasterize(p, r, meshes);
  }

  @Override
  public void handleTrianglePixel(int x, int y, BarycentricCoordinates triCoords) {

    double d = triCoords.interpolate(currentDepths[0], currentDepths[1], currentDepths[2]);

    correspondenceImage.set(x, y, new Correspondence(currentMesh, currentTriangle, triCoords, d));
  }


}
