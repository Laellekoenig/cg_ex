package image.processing.scaling;

import image.Image;
import image.RGBA;
import image.processing.ImageAlgorithm;
import image.processing.convolution.GaussConvolution;
import image.processing.convolution.GaussSeparableConvolution;

public class GaussianDownsampling implements ImageAlgorithm {

  /**
   * For sake of simplicity, scale factor is 2
   */
  @Override
  public Image<RGBA> perform(Image<RGBA> img) {

    Image<RGBA> outImg = new Image<RGBA>(img.cols() / 2, img.rows() / 2);

    //TODO: Blatt 2, Aufgabe 2 a)

    // apply gauss filter first
    img = new GaussConvolution(5, 1).perform(img);

    int i = 0;  // keep track of progress in outImg
    // skip every second x and y value
    for (int x = 0; x < img.cols(); x += 2) {
      for (int y = 0; y < img.rows(); y += 2) {
        outImg.set(i++, img.get(x, y));
      }
    }

    return outImg;
  }
}
