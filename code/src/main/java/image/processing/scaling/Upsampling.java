package image.processing.scaling;

import image.Image;
import image.RGBA;
import image.processing.ImageAlgorithm;

public class Upsampling implements ImageAlgorithm {

  private Interpolation interpolation;

  public Upsampling(Interpolation method) {
    this.interpolation = method;
  }

  @Override
  public Image<RGBA> perform(Image<RGBA> img) {
    interpolation.setImage(img);

    Image<RGBA> outImg = new Image<RGBA>(img.cols() * 2, img.rows() * 2,
        new RGBA(0.0f, 0.0f, 0.0f));

    //TODO: Blatt 2, Aufgabe 2 b)
    double stepSize = 0.5;  // factor two

    // go through every pixel
    for (int y = 0; y < outImg.rows(); y++) {
      for (int x = 0; x < outImg.cols(); x++) {

        // get nearest color
        RGBA nearest = interpolation.access(x * stepSize, y * stepSize);
        outImg.set(x, y, nearest);
      }
    }

    return outImg;
  }
}
