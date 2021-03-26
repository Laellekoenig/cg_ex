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

    RGBA res = new RGBA(0.0f, 0.0f, 0.0f);

    //TODO: Blatt 2, Aufgabe 2 c)

    // top row, a:
    RGBA a1 = img.get((int) x - 1,(int) y - 1);
    RGBA a2 = img.get((int) x, (int) y - 1);
    RGBA a3 = img.get((int) x + 1, (int) y - 1);
    RGBA a4 = img.get((int) x + 2, (int) y - 1);

    // second row from the top, b:
    RGBA b1 = img.get((int) x - 1,(int) y);
    RGBA b2 = img.get((int) x, (int) y);
    RGBA b3 = img.get((int) x + 1, (int) y);
    RGBA b4 = img.get((int) x + 2, (int) y);

    // third row from the top, c:
    RGBA c1 = img.get((int) x - 1,(int) y + 1);
    RGBA c2 = img.get((int) x, (int) y + 1);
    RGBA c3 = img.get((int) x + 1, (int) y + 1);
    RGBA c4 = img.get((int) x + 2, (int) y + 1);

    // bottom row, d:
    RGBA d1 = img.get((int) x - 1,(int) y + 2);
    RGBA d2 = img.get((int) x, (int) y + 2);
    RGBA d3 = img.get((int) x + 1, (int) y + 2);
    RGBA d4 = img.get((int) x + 2, (int) y + 2);

    // calculate percentage, total distance = 3
    // separate decimal numbers first
    float decimalProgress = (float) x - (int) x;
    // calculate percentage
    float percentage = (decimalProgress + 1) / 3;

    // interpolate rows
    RGBA a = interpolateCubically(a1, a2, a3, a4, percentage);
    RGBA b = interpolateCubically(b1, b2, b3, b4, percentage);
    RGBA c = interpolateCubically(c1, c2, c3, c4, percentage);
    RGBA d = interpolateCubically(d1, d2, d3, d4, percentage);

    // calculate y progress, total distance = 3
    decimalProgress = (float) y - (int) y;
    percentage = (decimalProgress + 1) / 3;

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
  private RGBA interpolateCubically(RGBA c0, RGBA c1, RGBA c2, RGBA c3, float x) {

    RGBA res = new RGBA(0.0f, 0.0f, 0.0f);

    //TODO: Blatt 2, Aufgabe 2 c)

    //maybe something wrong here

    // calculate components
    RGBA p = (c3.minus(c2)).minus(c0.minus(c1));
    RGBA q = ((c0.minus(c1)).minus(c3.minus(c2))).times(2);
    RGBA r = c2.minus(c0);
    RGBA b = c1;

    // polynomial equation
    res = (((p.times(Math.pow(x, 3))).plus(q.times(Math.pow(x, 2)))).plus(r.times(x))).plus(b);

    res.clamp();
    return res;
  }
}
