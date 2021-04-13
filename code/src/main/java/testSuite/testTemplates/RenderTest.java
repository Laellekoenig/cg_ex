package testSuite.testTemplates;

import renderer.SimpleRenderer;
import shader.PixelShader;
import image.RGBA;
import utils.Vector2;

public abstract class RenderTest extends VisualTest {

  protected SimpleRenderer renderer;

  public RenderTest(String gsFileName, String title, PixelShader shader) {
    super(gsFileName, title);
    this.renderer = new SimpleRenderer(gs.getWidth(), gs.getHeight(), shader);
  }

  @Override
  public void _draw() {
    renderScene();
    this.drawn = renderer.getImg();
    fireDrawEvent();
  }

  public abstract void renderScene();

  /**
   * Draw a red cross
   */
  public void cross(double x0, double y0, double x1, double y1) {
    x1 -= 1.d;
    y1 -= 1.d;

    RGBA color = new RGBA(1.0f, 0.0f, 0.0f);
    Vector2[] line1 = {new Vector2(x0, y0), new Vector2(x1, y1)};
    Vector2[] line2 = {new Vector2(x1, y0), new Vector2(x0, y1)};

    renderer.drawPlainLine(line1, color);
    renderer.drawPlainLine(line2, color);
  }


  /**
   * Draw a spiral made up of lines
   */
  public void spiral(double x0, double y0, double x1, double y1) {
    x1 -= 1.d;
    y1 -= 1.d;

    Vector2 center = ((new Vector2(x0, y0)).plus(new Vector2(x1, y1))).times(.5f);

    Vector2 pt = (new Vector2(x0, y0)).minus(new Vector2(x1, y1));
    double radius = pt.normLInf() * 0.5f;

    RGBA c0 = new RGBA(0.f, 1.f, 0.f);
    RGBA c1 = new RGBA(1.f, 0.f, 0.f);

    for (float a = 1.0f; a > 0.0; a -= 0.005) {
      float alpha = 2.5f * a * 3.1415f * 2.f;

      Vector2 add = (new Vector2(Math.cos(alpha), Math.sin(alpha))).times(a * radius);
      Vector2 p = center.plus(add);

      Vector2[] line = {center, p};
      RGBA[] colors = {c0, c1};

      renderer.drawLine(line, colors);
    }
  }

  /**
   * Draw a filled checkerboard
   */
  public void checkerboard(double x0, double y0, double tileSize, int tiles) {
    double endX = x0 + tiles * tileSize;
    double endY = y0 + tiles * tileSize;
    boolean blue = true;
    int i = 1;

    for (double xStep = x0; xStep < endX; xStep += tileSize) {
      blue = (i % 2 == 0) ? false : true;
      for (double yStep = y0; yStep < endY; yStep += tileSize) {
        tile(xStep, yStep, tileSize, blue);
        blue = !blue;
      }
      i++;
    }
  }

  public void tile(double x0, double y0, double size, boolean blue) {
    RGBA color = blue ? new RGBA(0.0f, 0.0f, 1.0f) : new RGBA(1.0f, 1.0f, 1.0f);

    Vector2[] triangle1 = {new Vector2(x0, y0), new Vector2(x0 + size, y0),
        new Vector2(x0, y0 + size)};
    Vector2[] triangle2 = {new Vector2(x0 + size, y0), new Vector2(x0 + size, y0 + size),
        new Vector2(x0, y0 + size)};

    renderer.drawPlainTriangle(triangle1, color);
    renderer.drawPlainTriangle(triangle2, color);
  }

  public void sierpinsky(double x0, double y0, double sideLength) {
    RGBA baseColor = new RGBA(178 / 255f, 255 / 255f, 102 / 255f);
    RGBA color = new RGBA((float) y0 / 2, (float) x0 / 2, 0.0f);

    double sideLength2 = sideLength * sideLength;
    double height = Math.sqrt(sideLength2 - sideLength2 / 4);
    double quarterSize = sideLength / 4;

    Vector2[] triangle1 = {new Vector2(x0, y0), new Vector2(x0 - sideLength / 2, y0 + height),
        new Vector2(x0 + sideLength / 2, y0 + height)};
    Vector2[] triangle2 = {new Vector2(x0, y0 + height),
        new Vector2(x0 - quarterSize, y0 + height / 2),
        new Vector2(x0 + quarterSize, y0 + height / 2)};

    renderer.drawPlainTriangle(triangle1, baseColor);
    renderer.drawPlainTriangle(triangle2, color);

    sierpinskyStep(x0 - quarterSize, y0 + height / 2, sideLength / 2, height / 2);
  }

  /**
   * Recursively creates three triangles that surround the given triangle. (x0, y0) is the top left
   * corner of the given triangle. The parameter height is handed over for computational reasons.
   */
  private void sierpinskyStep(double x0, double y0, double sideLength, double height) {
    if (height < 5) {
      return;
    }

    RGBA color = new RGBA((float) x0 / 2, (float) y0 / 2, 0.0f);

    Vector2[] triangleAbove = {new Vector2(x0 + sideLength / 2, y0),
        new Vector2(x0 + sideLength / 4, y0 - height / 2),
        new Vector2(x0 + 3 * sideLength / 4, y0 - height / 2)};
    Vector2[] triangleLeft = {new Vector2(x0, y0 + height),
        new Vector2(x0 - sideLength / 4, y0 + height / 2),
        new Vector2(x0 + sideLength / 4, y0 + height / 2)};
    Vector2[] triangleRight = {new Vector2(x0 + 3 * sideLength / 4, y0 + height / 2),
        new Vector2(x0 + sideLength, y0 + height),
        new Vector2(x0 + sideLength + sideLength / 4, y0 + height / 2)};

    renderer.drawPlainTriangle(triangleAbove, color);
    renderer.drawPlainTriangle(triangleLeft, color);
    renderer.drawPlainTriangle(triangleRight, color);

    //recursion
    sierpinskyStep(x0 + sideLength / 4, y0 - height / 2, sideLength / 2, height / 2);
    sierpinskyStep(x0 - sideLength / 4, y0 + height / 2, sideLength / 2, height / 2);
    sierpinskyStep(x0 + 3 * sideLength / 4, y0 + height / 2, sideLength / 2, height / 2);
  }

  public void triangles(double x0, double y0, double x1, double y1, double size) {
    x1 -= 1.d;
    y1 -= 1.d;

    RGBA c0 = new RGBA(1f, 0f, 0f);
    RGBA c1 = new RGBA(0f, 1f, 0f);
    RGBA c2 = new RGBA(0f, 0f, 1f);
    RGBA c3 = new RGBA(1f, 1f, 0f);

    for (double x = x0; x < x1 - size; x += size) {
      for (double y = y0; y < y1 - size; y += size) {

        Vector2[] triangle1 = {new Vector2(x, y), new Vector2(x + size, y),
            new Vector2(x + size, y + size)};
        RGBA[] colors1 = {c0, c1, c2};

        Vector2[] triangle2 = {new Vector2(x, y), new Vector2(x + size, y + size),
            new Vector2(x, y + size)};
        RGBA[] colors2 = {c0, c2, c3};

        renderer.drawTriangle(triangle1, colors1);
        renderer.drawTriangle(triangle2, colors2);
      }
    }
  }

  /**
   * Draw a flower made up of triangles
   */
  public void flower(double x0, double y0, double x1, double y1) {
    x1 -= 1.d;
    y1 -= 1.d;

    Vector2 center = ((new Vector2(x0, y0)).plus(new Vector2(x1, y1))).times(0.5f);

    Vector2 pt = (new Vector2(x0, y0)).minus(new Vector2(x1, y1));
    double radius = pt.normLInf() * 0.5f;

    RGBA c0 = new RGBA(1.f, 1.f, 0.f);
    RGBA c1 = new RGBA(1.f, 0.f, 0.f);
    RGBA c2 = new RGBA(0.f, 0.f, 1.f);

    for (float a = 0; a < 1.0; a += 0.05) {
      float alpha = a * 3.1415f * 2.f;

      Vector2 add0 = (new Vector2(Math.cos(alpha), Math.sin(alpha))).times(0.8f * radius);
      Vector2 add1 = (new Vector2(Math.cos(alpha + 0.4), Math.sin(alpha + 0.4))).times(radius);

      Vector2 p0 = center.plus(add0);
      Vector2 p1 = center.plus(add1);

      Vector2[] triangle = {center, p0, p1};
      RGBA[] colors = {c0, c1, c2};

      renderer.drawTriangle(triangle, colors);
    }
  }

  public void degenerateTriangles(double x0, double y0, double x1, double y1, double size) {
    x1 -= 1.d;
    y1 -= 1.d;

    RGBA c0 = new RGBA(1.f, 1.f, 0.f);
    RGBA c1 = new RGBA(1.f, 0.f, 0.f);
    RGBA c2 = new RGBA(0.f, 0.f, 1.f);

    RGBA[] colors = {c0, c1, c2};

    for (double y = y0; y < y1 - size; y += Math.max(size, 1.d)) {

      Vector2[] triangle1 = {new Vector2(x0, y0), new Vector2(x1, y), new Vector2(x1, y + size)};
      Vector2[] triangle2 = {new Vector2(x1, y1), new Vector2(x0, y), new Vector2(x0, y + size)};

      renderer.drawTriangle(triangle1, colors);
      renderer.drawTriangle(triangle2, colors);
    }
  }

}
