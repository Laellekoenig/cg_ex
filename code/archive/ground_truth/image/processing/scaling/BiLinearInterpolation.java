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

    //TODO: Blatt 2, Aufgabe 2 c)

    // interpolate x first
    int leftPixel = (int) x;            // round down
    int rightPixel = leftPixel + 1;     // next neighbor
    int topPixel = (int) y;             // y value of x-Pixels
    double percentage = x - leftPixel;  // get digits after comma -> percentage

    RGBA topLineInterpolated = interpolate(img.get(leftPixel, topPixel), img.get(rightPixel, topPixel), percentage);

    //interpolate bottom Line
    int bottomPixel = topPixel + 1;     // next y neighbor

    RGBA bottomLineInterpolated = interpolate(img.get(leftPixel, bottomPixel), img.get(rightPixel, bottomPixel), percentage);

    // interpolate between two lines
    percentage = y - topPixel;          // get digits after comma -> percentage

    return interpolate(topLineInterpolated, bottomLineInterpolated, percentage);
  }

  private RGBA interpolate(RGBA a, RGBA b, double percentage) {
    double counter = 1 - percentage;
    return new RGBA(a.r * counter + b.r * percentage,
                    a.g * counter + b.g * percentage,
                    a.b * counter + b.b * percentage);
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
