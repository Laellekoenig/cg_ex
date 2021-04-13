package renderer;

import image.RGBA;
import mesh.Mesh;
import rasterization.Correspondence;

public class MeshRendererWrongColorShading extends MeshRenderer {

  public MeshRendererWrongColorShading(int w, int h, Mesh[] meshes) {
    super(w, h, meshes);
  }


  @Override
  protected void callShader(int x, int y, Correspondence c) {
    //give each triangle its own grey color:
    //grey = triangle number / number of all triangles
    double greyColor = (double) (c.triangle) / c.mesh.tvi.length;
    RGBA color = new RGBA(greyColor, greyColor, greyColor);
    RGBA[] colors = {color, color, color};
    shader.setTriangleColors(colors);
    shader.handleTrianglePixel(x, y, c.triCoords);
  }

}
