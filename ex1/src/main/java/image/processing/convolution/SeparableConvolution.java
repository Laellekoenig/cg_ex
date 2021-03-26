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

    Image<RGBA> halfBaked = new Image<RGBA>(img.cols(), img.rows(), new RGBA(0f, 0f, 0f));  // for first convolution
    Image<RGBA> outImg = new Image<RGBA>(img.cols(), img.rows(), new RGBA(0f, 0f, 0f));     // for second convolution

    //TODO: Blatt 2, Aufgabe 1 c)

    // calculate offset for centering kernel on pixel
    int offset = kernel.cols() / 2;

    // apply to rows
    // go through every pixel of original image
    for (int y = 0; y < img.rows(); y++) {
      for (int x = 0; x < img.cols(); x++) {

        // color to be filled in
        RGBA newColor = new RGBA(0, 0, 0);
        // apply offset
        int xStart = x - offset;

        // go through kernel
        for (int i = 0; i < kernel.cols(); i++) {
          // get value from kernel
          float multiplier = kernel.get(i);
          // get current color from image
          RGBA currentColor = img.get(xStart + i, y);
          // apply filter
          currentColor = currentColor.times(multiplier);
          newColor = newColor.plus(currentColor);
        }

        halfBaked.set(x, y, newColor);
      }
    }

    // apply to cols
    // go through half-baked image
    for (int x = 0; x < halfBaked.cols(); x++) {
      for (int y = 0; y < halfBaked.rows(); y++) {

        // color to be set
        RGBA newColor = new RGBA(0, 0, 0);
        // apply offset
        int yStart = y - offset;

        // go through kernel
        for (int i = 0; i < kernel.cols(); i++) {
          // get value from kernel
          float multiplier = kernel.get(i);
          // get current color from halfBaked
          RGBA currentColor = halfBaked.get(x, yStart + i);
          // apply filter
          currentColor = currentColor.times(multiplier);
          newColor = newColor.plus(currentColor);
        }

        outImg.set(x, y, newColor);
      }
    }

    printKernel();

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
