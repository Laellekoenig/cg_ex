package ex1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import image.Image;
import image.RGBA;
import org.junit.Test;
import renderer.SimpleRenderer;
import shader.ConstantColorShader;
import utils.Vector2;

public class BresenhamTest {

  private RGBA color = new RGBA(1, 0, 0);

  @Test
  public void testLeftToRightAscendingSmallGradient() {
    Image<RGBA> img = draw(100, -20);
    int y = img.rows() - 2;
    for (int x = 1; x < img.cols() - 2; ++x) {
      assertEquals("Line is not continuous.", color, img.get(x, y));
      assertNull("Ascending line has descending part.", img.get(x + 1, y + 1));
      assertTrue("Line is not continuous or multiple pixels in the same column are colored.",
          img.get(x + 1, y) == color ^ img.get(x + 1, y - 1) == color);
      if (img.get(x + 1, y) == null) {
        --y;
      }
    }
  }

  @Test
  public void testLeftToRightAscendingLargeGradient() {
    Image<RGBA> img = draw(50, -100);
    int x = 1;
    for (int y = img.rows() - 2; y > 1; --y) {
      assertEquals("Line is not continuous.", color, img.get(x, y));
      assertNull("Ascending line has descending part.", img.get(x - 1, y - 1));
      assertTrue("Line is not continuous or multiple pixels in the same row are colored.",
          img.get(x, y - 1) == color ^ img.get(x + 1, y - 1) == color);
      if (img.get(x, y - 1) == null) {
        ++x;
      }
    }
  }

  @Test
  public void testRightToLeftAscendingSmallGradient() {
    Image<RGBA> img = draw(-100, -20);
    int y = img.rows() - 2;
    for (int x = img.cols() - 2; x > 1; --x) {
      assertEquals("Line is not continuous.", color, img.get(x, y));
      assertNull("Ascending line has descending part.", img.get(x - 1, y + 1));
      assertTrue("Line is not continuous or multiple pixels in the same column are colored.",
          img.get(x - 1, y) == color ^ img.get(x - 1, y - 1) == color);
      if (img.get(x - 1, y) == null) {
        --y;
      }
    }
  }

  @Test
  public void testRightToLeftAscendingLargeGradient() {
    Image<RGBA> img = draw(-50, -100);
    int x = img.cols() - 2;
    for (int y = img.rows() - 2; y > 1; --y) {
      assertEquals("Line is not continuous.", color, img.get(x, y));
      assertNull("Ascending line has descending part.", img.get(x + 1, y - 1));
      assertTrue("Line is not continuous or multiple pixels in the same row are colored.",
          img.get(x, y - 1) == color ^ img.get(x - 1, y - 1) == color);
      if (img.get(x, y - 1) == null) {
        --x;
      }
    }
  }

  @Test
  public void testLeftToRightDescendingSmallGradient() {
    Image<RGBA> img = draw(100, 20);
    int y = 1;
    for (int x = 1; x < img.cols() - 2; ++x) {
      assertEquals("Line is not continuous.", color, img.get(x, y));
      assertNull("Descending line has ascending part.", img.get(x + 1, y - 1));
      assertTrue("Line is not continuous or multiple pixels in the same column are colored.",
          img.get(x + 1, y) == color ^ img.get(x + 1, y + 1) == color);
      if (img.get(x + 1, y) == null) {
        ++y;
      }
    }
  }

  @Test
  public void testLeftToRightDescendingLargeGradient() {
    Image<RGBA> img = draw(50, 100);
    int x = 1;
    for (int y = 1; y < img.rows() - 2; ++y) {
      assertEquals("Line is not continuous.", color, img.get(x, y));
      assertNull("Descending line has ascending part.", img.get(x - 1, y + 1));
      assertTrue("Line is not continuous or multiple pixels in the same row are colored.",
          img.get(x, y + 1) == color ^ img.get(x + 1, y + 1) == color);
      if (img.get(x, y + 1) == null) {
        ++x;
      }
    }
  }

  @Test
  public void testRightToLeftDescendingSmallGradient() {
    Image<RGBA> img = draw(-100, 20);
    int y = 1;
    for (int x = img.cols() - 2; x > 1; --x) {
      assertEquals("Line is not continuous.", color, img.get(x, y));
      assertNull("Descending line has ascending part.", img.get(x - 1, y - 1));
      assertTrue("Line is not continuous or multiple pixels in the same column are colored.",
          img.get(x - 1, y) == color ^ img.get(x - 1, y + 1) == color);
      if (img.get(x - 1, y) == null) {
        ++y;
      }
    }
  }

  @Test
  public void testRightToLeftDescendingLargeGradient() {
    Image<RGBA> img = draw(-50, 100);
    int x = img.cols() - 2;
    for (int y = 1; y < img.rows() - 2; ++y) {
      assertEquals("Line is not continuous.", color, img.get(x, y));
      assertNull("Descending line has ascending part.", img.get(x + 1, y + 1));
      assertTrue("Line is not continuous or multiple pixels in the same row are colored.",
          img.get(x, y + 1) == color ^ img.get(x - 1, y + 1) == color);
      if (img.get(x, y + 1) == null) {
        --x;
      }
    }
  }

  @Test
  public void testVerticalAscending() {
    Image<RGBA> img = draw(0, -50);
    assertNull(img.get(0, img.rows() - 1));
    assertNull("Line starts too early.", img.get(1, img.rows() - 1));
    assertNull(img.get(2, img.rows() - 1));
    for (int y = img.rows() - 2; y > 1; --y) {
      assertEquals("Vertical line is not continuous.", color, img.get(1, y));
      assertNull("Line is not straight.", img.get(0, y));
      assertNull("Line is not straight.", img.get(2, y));
    }
    assertNull(img.get(0, 0));
    assertNull("Line stops too late.", img.get(1, 0));
    assertNull(img.get(2, 0));
  }

  @Test
  public void testVerticalDescending() {
    Image<RGBA> img = draw(0, 50);
    assertNull(img.get(0, 0));
    assertNull("Line starts too early.", img.get(1, 0));
    assertNull(img.get(2, 0));
    for (int y = 1; y < img.rows() - 2; ++y) {
      assertEquals("Vertical line is not continuous.", color, img.get(1, y));
      assertNull("Line is not straight.", img.get(0, y));
      assertNull("Line is not straight.", img.get(2, y));
    }
    assertNull(img.get(0, img.rows() - 1));
    assertNull("Line stops too late.", img.get(1, img.rows() - 1));
    assertNull(img.get(2, img.rows() - 1));
  }

  @Test
  public void testHorizontalRightToLeft() {
    Image<RGBA> img = draw(-50, 0);
    assertNull(img.get(img.cols() - 1, 0));
    assertNull("Line starts too early.", img.get(img.cols() - 1, 1));
    assertNull(img.get(img.cols() - 1, 2));
    for (int x = img.cols() - 2; x > 1; --x) {
      assertEquals("Horizontal line is not continuous.", color, img.get(x, 1));
      assertNull("Line is not straight.", img.get(x, 0));
      assertNull("Line is not straight.", img.get(x, 2));
    }
    assertNull(img.get(0, 0));
    assertNull("Line stops too late.", img.get(0, 1));
    assertNull(img.get(0, 2));
  }

  @Test
  public void testHorizontalLeftToRight() {
    Image<RGBA> img = draw(50, 0);
    assertNull(img.get(0, 0));
    assertNull("Line starts too early.", img.get(0, 1));
    assertNull(img.get(0, 2));
    for (int x = 1; x < img.cols() - 2; ++x) {
      assertEquals("Horizontal line is not continuous.", color, img.get(x, 1));
      assertNull("Line is not straight.", img.get(x, 0));
      assertNull("Line is not straight.", img.get(x, 2));
    }
    assertNull(img.get(img.cols() - 1, 0));
    assertNull("Line stops too late.", img.get(img.cols() - 1, 1));
    assertNull(img.get(img.cols() - 1, 2));
  }

  @Test
  public void testClippingHorizontalLine() {
    SimpleRenderer simpleRenderer = new SimpleRenderer(10, 2, new ConstantColorShader());
    Vector2[] line = {new Vector2(5, 0), new Vector2(15, 0)};
    simpleRenderer.drawPlainLine(line, color);
    Image<RGBA> img = simpleRenderer.getImg();

    for (int x = 0; x < 5; ++x) {
      assertNull("Unexpected line overflow.", img.get(x, 1));
    }
    for (int x = 5; x < 10; ++x) {
      assertEquals("Line is not present where it should be.", color, img.get(x, 0));
    }
  }

  @Test
  public void testClippingVerticalLine() {
    SimpleRenderer simpleRenderer = new SimpleRenderer(2, 10, new ConstantColorShader());
    Vector2[] line = {new Vector2(0, 5), new Vector2(0, 15)};
    simpleRenderer.drawPlainLine(line, color);
    Image<RGBA> img = simpleRenderer.getImg();

    for (int y = 0; y < 5; ++y) {
      assertNull("Unexpected line overflow.", img.get(1, y));
    }
    for (int y = 5; y < 10; ++y) {
      assertEquals("Line is not present where it should be.", color, img.get(0, y));
    }
  }

  @Test
  public void testClippingCountOfShaderCalls() {
    int begin = 5;
    ClippingColorShader horizontalShader = new ClippingColorShader();
    SimpleRenderer horizontalRenderer = new SimpleRenderer(10, 1, horizontalShader);
    Vector2[] horizontalLine = {new Vector2(begin, 0), new Vector2(15, 0)};
    horizontalRenderer.drawPlainLine(horizontalLine, color);
    assertEquals("Unexpected number of shader calls when drawing horizontal line.",
        horizontalRenderer.getImg().cols() - begin, horizontalShader.getLineCount());

    ClippingColorShader verticalShader = new ClippingColorShader();
    SimpleRenderer verticalRenderer = new SimpleRenderer(1, 10, verticalShader);
    Vector2[] verticalLine = {new Vector2(0, begin), new Vector2(0, 15)};
    verticalRenderer.drawPlainLine(verticalLine, color);
    assertEquals("Unexpected number of shader calls when drawing vertical line.",
        verticalRenderer.getImg().rows() - begin, verticalShader.getLineCount());
  }

  private Image<RGBA> draw(int x, int y) {
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

    SimpleRenderer simpleRenderer = new SimpleRenderer(width, height, new ConstantColorShader());

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
    simpleRenderer.drawPlainLine(line, color);

    return simpleRenderer.getImg();
  }
}
