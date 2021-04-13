package image.processing;

import image.Image;
import image.RGBA;

public interface ImageAlgorithm {

  public Image<RGBA> perform(Image<RGBA> img);

}
