package image.processing.scaling;

import image.Image;
import image.RGBA;

public class BiCubicInterpolation implements Interpolation {

  private Image<RGBA> img;

  @Override
  public void setImage(Image<RGBA> img) {
    this.img = img;
  }

  @Override
  public RGBA access(double x, double y) {

    // check if calculation necessary
    if (x - (int) x == 0 && y - (int) y == 0) {
      // exact point, skip calculation
      return img.get((int) x, (int) y);
    }

    RGBA res = new RGBA(0.0f, 0.0f, 0.0f);

    //TODO: Blatt 2, Aufgabe 2 c)

    // calculate possible x and y values
    int x0 = (int) x - 1;
    int x1 = x0 + 1;
    int x2 = x0 + 2;
    int x3 = x0 + 3;

    int y0 = (int) y - 1;
    int y1 = y0 + 1;
    int y2 = y0 + 2;
    int y3 = y0 + 3;

    // top row, a:
    RGBA a1 = img.get(x0, y0);
    RGBA a2 = img.get(x1, y0);
    RGBA a3 = img.get(x2, y0);
    RGBA a4 = img.get(x3, y0);

    // second row from the top, b:
    RGBA b1 = img.get(x0, y1);
    RGBA b2 = img.get(x1, y1);
    RGBA b3 = img.get(x2, y1);
    RGBA b4 = img.get(x3, y1);

    // third row from the top, c:
    RGBA c1 = img.get(x0, y2);
    RGBA c2 = img.get(x1, y2);
    RGBA c3 = img.get(x2, y2);
    RGBA c4 = img.get(x3, y2);

    // bottom row, d:
    RGBA d1 = img.get(x0, y3);
    RGBA d2 = img.get(x1, y3);
    RGBA d3 = img.get(x2, y3);
    RGBA d4 = img.get(x3, y3);

    // calculate percentage, get digits after comma   ->  3.456 ->  .456  ->  45.6%
    float percentage = (float) (x - (int) x);

    // interpolate rows
    RGBA a = interpolateCubically(a1, a2, a3, a4, percentage);
    RGBA b = interpolateCubically(b1, b2, b3, b4, percentage);
    RGBA c = interpolateCubically(c1, c2, c3, c4, percentage);
    RGBA d = interpolateCubically(d1, d2, d3, d4, percentage);

    // calculate y progress, get digits after comma   ->  percentage
    percentage = (float) (y - (int) y);

    // now interpolate over interpolated values
    res = interpolateCubically(a, b, c, d, percentage);
    return res;
  }

  /**
   * Bicubic interpolation as follows: f(x) = px³ + qx² + rx + b, with p = (c3 - c2) - (c0 - c1) q =
   * 2(c0 - c1) - (c3 - c2) r = c2 - c0 Note: clamping of result is important!
   * <p>
   * TEACHING: remove this method
   */
  private RGBA interpolateCubically(RGBA a, RGBA b, RGBA c, RGBA d, float x) {

    RGBA res = new RGBA(0.0f, 0.0f, 0.0f);

    //TODO: Blatt 2, Aufgabe 2 c)

    // calculate components
    RGBA p = (d.minus(c)).minus(a.minus(b));              // (d - c) - (a -b)
    RGBA q = ((a.minus(b)).times(2)).minus(d.minus(c));   // 2(a - b) - (d - c)
    RGBA r = c.minus(a);                                  // c - a

    // calculate polynomial
    RGBA one = p.times(Math.pow(x, 3));   // px^3
    RGBA two = q.times(Math.pow(x, 2));   // qx^2
    RGBA three = r.times(x);              // rx

    res = one.plus(two).plus(three).plus(b);

    res.clamp();
    return res;
  }
}
