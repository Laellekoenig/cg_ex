package image.processing.scaling;

import image.Image;
import image.RGBA;

public interface Interpolation {

  public RGBA access(double x, double y);

  public void setImage(Image<RGBA> img);

}
