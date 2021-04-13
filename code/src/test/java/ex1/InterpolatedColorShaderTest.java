package ex1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import image.Image;
import image.ImageUtils;
import image.RGBA;
import java.io.IOException;
import org.junit.Test;
import renderer.SimpleRenderer;
import shader.InterpolatedColorShader;
import utils.BarycentricCoordinateTransform;
import utils.BarycentricCoordinates;
import utils.Vector2;

public class InterpolatedColorShaderTest {

  private InterpolatedColorShader shader = new InterpolatedColorShader();
  private RGBA red = new RGBA(1, 0, 0);
  private RGBA green = new RGBA(0, 1, 0);
  private RGBA blue = new RGBA(0, 0, 1);

  @Test
  public void testLineEndpointsCorrectColor() {
    int width = 10;
    int height = 15;
    Image<RGBA> img = drawLine(width, height);
    assertEquals("Starting point has wrong color when line goes from left to right.", red,
        img.get(1, 1));
    assertEquals("End point has wrong color when line goes from left to right.", blue,
        img.get(width, height));
  }

  @Test
  public void testInvertedLineEndpointsCorrectColor() {
    int width = -10;
    int height = 15;
    Image<RGBA> img = drawLine(width, height);
    assertEquals("Starting point has wrong color when line goes from right to left.", red,
        img.get(-width, 1));
    assertEquals("End point has wrong color when line goes from right to left.", blue,
        img.get(1, height));
  }

  @Test
  public void testHorizontalLineInterpolation() {
    int width = 9;
    Image<RGBA> img = drawLine(width, 0);
    assertEquals("Horizontal line centerpoint is not correctly interpolated.",
        red.times(.5).plus(blue.times(.5)), img.get(width / 2 + 1, 1));
  }

  @Test
  public void testVerticalLineInterpolation() {
    int height = 9;
    Image<RGBA> img = drawLine(0, height);
    assertEquals("Vertical line centerpoint is not correctly interpolated.",
        red.times(.5).plus(blue.times(.5)), img.get(1, height / 2 + 1));
  }

  @Test
  public void testTriangleCornersCorrectColor() {
    int width = 20;
    int height = 30;
    Image<RGBA> img = drawTriangle(width, height);
    assertEquals("Triangle corner color is not pure.", red, img.get(1, 1));
    assertEquals("Triangle corner color is not pure.", green, img.get(width / 2 + 1, height));
    assertEquals("Triangle corner color is not pure.", blue, img.get(width, height / 2 + 1));
  }

  private Image<RGBA> drawTriangle(int x, int y) {
    int width = Math.abs(x) + 2;
    int height = Math.abs(y) + 2;
    Vector2[] triangle = computeTriangle(x, y);
    RGBA[] colors = {red, green, blue};

    SimpleRenderer simpleRenderer = new SimpleRenderer(width, height, shader);
    simpleRenderer.drawTriangle(triangle, colors);
    return simpleRenderer.getImg();
  }

  private Vector2[] computeTriangle(int x, int y) {
    x = Math.max(Math.abs(x), 10);
    y = Math.max(Math.abs(y), 10);
    int width = Math.abs(x) + 2;
    int height = Math.abs(y) + 2;
    Vector2[] triangle = {new Vector2(1, 1), new Vector2(width / 2, y), new Vector2(x, height / 2)};
    return triangle;
  }

  private Image<RGBA> drawLine(int x, int y) {
    int width = Math.abs(x) + 2;
    int height = Math.abs(y) + 2;
    if (x == 0) {
      ++width;
      ++x;
    }
    if (y == 0) {
      ++height;
      ++y;
    }

    SimpleRenderer simpleRenderer = new SimpleRenderer(width, height, shader);
    Vector2 start, end;
    if (x > 0) {
      if (y > 0) {
        start = new Vector2(1, 1);
        end = new Vector2(x, y);
      } else {
        start = new Vector2(1, -y);
        end = new Vector2(x, 1);
      }
    } else {
      if (y > 0) {
        start = new Vector2(-x, 1);
        end = new Vector2(1, y);
      } else {
        start = new Vector2(-x, -y);
        end = new Vector2(1, 1);
      }
    }
    Vector2[] line = {start, end};
    RGBA[] colors = {red, blue};
    simpleRenderer.drawLine(line, colors);

    return simpleRenderer.getImg();
  }
}
