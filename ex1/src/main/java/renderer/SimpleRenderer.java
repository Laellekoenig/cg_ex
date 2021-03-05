package renderer;

import rasterization.LineRasterizer;
import rasterization.TriangleRasterizer;
import shader.PixelShader;
import image.Image;
import image.RGBA;
import utils.Vector2;

public class SimpleRenderer {

  private PixelShader shader;
  private LineRasterizer lineRasterizer;
  private TriangleRasterizer triangleRasterizer;
  private int width, height;


  public SimpleRenderer(int width, int height, PixelShader pixelShader) {
    this.width = width;
    this.height = height;
    this.lineRasterizer = new LineRasterizer(pixelShader, width, height);
    this.triangleRasterizer = new TriangleRasterizer(pixelShader, width, height);
    this.shader = pixelShader;
    this.shader.setImgSize(width, height);
  }

  /**
   * Draws a line with a constant color
   */
  public void drawPlainLine(Vector2[] line, RGBA color) {
    RGBA[] colors = {color, color};
    drawLine(line, colors);
  }

  /**
   * Draws a triangle filled with a constant color
   */
  public void drawPlainTriangle(Vector2[] triangle, RGBA color) {
    RGBA[] colors = {color, color, color};
    drawTriangle(triangle, colors);
  }

  /**
   * Draws a a line with interpolated Colors at the endpoints of the line
   *
   * @param line   Points which will be connected through lines (can be more than two points)
   * @param colors RGBA color of the start and endpoint of the/all lines.
   */
  public void drawLine(Vector2[] line, RGBA[] colors) {
    assert (line.length >= 2);
    assert (colors.length == 2);

    shader.setLineColors(colors);
    lineRasterizer.rasterLine(line);
  }

  public void drawTriangle(Vector2[] triangle, RGBA[] colors) {
    assert (triangle.length == 3);
    assert (colors.length == 3);

    shader.setTriangleColors(colors);
    triangleRasterizer.rasterTriangle(triangle);
  }


  public Image<RGBA> getImg() {
    return shader.getImage();
  }

  public int getHeight() {
    return this.height;
  }

  public int getWidth() {
    return this.width;
  }

}
