package image.processing.scaling;

import image.Image;
import image.RGBA;
import image.processing.ImageAlgorithm;
import image.processing.convolution.GaussSeparableConvolution;

public class GaussianDownsampling implements ImageAlgorithm {

  /**
   * For sake of simplicity, scale factor is 2
   */
  @Override
  public Image<RGBA> perform(Image<RGBA> img) {

    Image<RGBA> outImg = new Image<RGBA>(img.cols() / 2, img.rows() / 2);

    //TODO: Blatt 2, Aufgabe 2 a)

    return outImg;
  }
}
