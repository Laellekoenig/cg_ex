package image.processing.scaling;

import image.Image;
import image.RGBA;

public class BiLinearInterpolation implements Interpolation {

  private Image<RGBA> img;

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
   * Returns c0*(1-dx) + c1*dx where dx is the distance from c0 to the point to be interpolated.
   */
  private RGBA linearlyInterpolate(RGBA c0, RGBA c1, double dx) {

    RGBA res = new RGBA(0.0f, 0.0f, 0.0f);

    //TODO: Blatt 2, Aufgabe 2 c)

    return res;
  }
}
