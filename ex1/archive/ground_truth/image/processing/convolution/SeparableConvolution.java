package image.processing.convolution;

import image.Image;
import image.RGBA;
import image.processing.ImageAlgorithm;

public abstract class SeparableConvolution extends Convolution {

  @Override
  public Image<RGBA> perform(Image<RGBA> img) {
    Image<Float> kernel = getKernel();

    if (kernel.cols() != 1 && kernel.rows() != 1) {
      throw new IllegalArgumentException("Kernel cannot be a matrix for separable convolution");
    }

    Image<RGBA> outImg = new Image<RGBA>(img.cols(), img.rows(), new RGBA(0f, 0f, 0f));

    //TODO: Blatt 2, Aufgabe 1 c)

    return outImg;
  }

  @Override
  public void printKernel() {
    Image<Float> kernel = getKernel();
    StringBuilder builder = new StringBuilder("[");
    boolean first = true;

    for (int i = 0; i < kernel.size(); i++) {
      if (!first) {
        builder.append(", ");
      }

      builder.append(kernel.get(i));
      first = false;
    }

    builder.append("]");

    System.out.println(builder.toString());
  }
}
