package ex1;

import image.Image;
import image.ImageUtils;
import image.RGBA;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;
import renderer.SimpleRenderer;
import shader.AdditiveColorShader;
import shader.ConstantColorShader;
import utils.BarycentricCoordinateTransform;
import utils.Vector2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TriangleTest {

  private Vector2 a, b, c, d;
  private BarycentricCoordinateTransform bctLeft, bctRight;

  @Before
  public void setup() {
    a = new Vector2(0, 0);
    b = new Vector2(20, 30);
    c = new Vector2(30, -5);
    d = new Vector2(10, -20);

    bctLeft = new BarycentricCoordinateTransform(a, b, c);
    bctRight = new BarycentricCoordinateTransform(a, d, c);
  }

  @Test
  public void testBarycentricCoordinatesOfCornersInside() {
    assertTrue("Corner 1 is not inside (counterclockwise) triangle even though it should be.",
        bctLeft.getBarycentricCoordinates(a.x, a.y).isInside());
    assertTrue("Corner 2 is not inside (counterclockwise) triangle even though it should be.",
        bctLeft.getBarycentricCoordinates(b.x, b.y).isInside());
    assertTrue("Corner 3 is not inside (counterclockwise) triangle even though it should be.",
        bctLeft.getBarycentricCoordinates(c.x, c.y).isInside());
    assertTrue("Corner 1 is not inside (clockwise) triangle even though it should be.",
        bctRight.getBarycentricCoordinates(a.x, a.y).isInside());
    assertTrue("Corner 2 is not inside (clockwise) triangle even though it should be.",
        bctRight.getBarycentricCoordinates(d.x, d.y).isInside());
    assertTrue("Corner 3 is not inside (clockwise) triangle even though it should be.",
        bctRight.getBarycentricCoordinates(c.x, c.y).isInside());
  }

  @Test
  public void testBarycentricCoordinatesOfEdgeInside() {
    for (double x = a.x; x <= c.x; ++x) {
      double y = (a.y + (c.y - a.y) * x / (c.x - a.x));
      assertTrue("Edge between two triangles contains gaps.",
          bctLeft.getBarycentricCoordinates(x, y).isInside() ||
              bctRight.getBarycentricCoordinates(x, y).isInside());
    }
  }

  @Test
  public void testPlasteredImage() {
    int width = 250;
    int height = 240;
    SimpleRenderer renderer = new SimpleRenderer(width, height, new AdditiveColorShader());

    for (double x = 0; x < width; x += 10) {
      for (double y = 0; y < height; y += 10) {
        Vector2[] triangle = {new Vector2(x - 0.49, y - 0.49), new Vector2(x + 9.99, y - 0.9),
            new Vector2(x, y + 9)};
        renderer.drawPlainTriangle(triangle, null);

        Vector2[] triangle2 = {new Vector2(x + 9.25, y + 0.75), new Vector2(x + 9.49, y + 10),
            new Vector2(x + 0.51, y + 9.4)};
        renderer.drawPlainTriangle(triangle2, null);
      }
    }

    renderer.drawPlainLine(new Vector2[]{new Vector2(40, 40), new Vector2(180, 200)}, new RGBA(1, 0, 0));

    Image<RGBA> img = renderer.getImg();
    String imgName = "plastered.png";
    try {
      ImageUtils.write(img, imgName);
    } catch (IOException e) {
      e.printStackTrace();
    }
    for (int x = 0; x < width; ++x) {
      for (int y = 0; y < height; ++y) {
        assertNotNull("Plastered image contains gaps.", img.get(x, y));
      }
    }
    File plastered = new File(imgName);
    plastered.delete();
  }
}
