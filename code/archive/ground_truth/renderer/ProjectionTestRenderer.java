package renderer;

import image.Image;
import image.RGBA;
import projection.PinholeProjection;
import projection.TurnableRenderer;
import rasterization.LineRasterizer;
import shader.PixelShader;
import utils.Matrix4;
import utils.Vector2;
import utils.Vector3;

/**
 * Used to be able to test the correctness of the pinhole projection. Works very similar to the
 * SimpleRenderer as at this point in the exercises, the concept of Meshes is not yet known.
 *
 * @author Manuel Kaufmann
 */
public class ProjectionTestRenderer implements TurnableRenderer {

  private PinholeProjection projection;

  private LineRasterizer lineRasterizer;
  private PixelShader shader;

  public ProjectionTestRenderer(int w, int h, PixelShader shader) {
    this.lineRasterizer = new LineRasterizer(shader, w, h);
    this.shader = shader;
    this.shader.setImgSize(w, h);
    this.projection = new PinholeProjection(w, h);
  }

  public PinholeProjection getProjection() {
    return projection;
  }

  public void drawPlainLine(Vector3[] line, RGBA color) {
    RGBA[] colors = {color, color};
    drawLine(line, colors);
  }


  public void drawLine(Vector3[] line, RGBA[] colors) {
    Vector2[] projectedLine = new Vector2[2];

    for (int i = 0; i < 2; i++) {
      Vector3 pp = projection.project(line[i]);
      projectedLine[i] = new Vector2(pp.x, pp.y);
    }

    shader.setLineColors(colors);
    lineRasterizer.rasterLine(projectedLine);
  }

  public Image<RGBA> getFinalImg() {
    return shader.getImage();
  }

  public void clearImg() {
    this.shader.clearImage();
  }

  @Override
  public void setProjectionView(Matrix4 currentView) {
    projection.setView(currentView);
  }

  @Override
  public void rotateLights(Matrix4 rotation) {
    //nothing to do
  }

}
