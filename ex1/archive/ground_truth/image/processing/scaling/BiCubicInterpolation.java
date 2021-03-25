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

    res.clamp();
    return res;
  }
}
