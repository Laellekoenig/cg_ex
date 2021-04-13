package image.processing.scaling;

import image.Image;
import image.RGBA;

public class NNInterpolation implements Interpolation {

  private Image<RGBA> img;

  public void setImage(Image<RGBA> image) {
    this.img = image;
  }

  @Override
  public RGBA access(double x, double y) {
    return img.get((int) Math.round(x), (int) Math.round(y));
  }
}
