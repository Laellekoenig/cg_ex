package image.processing.convolution;

import image.Image;
import image.ImageUtils;
import image.RGBA;
import image.processing.ImageAlgorithm;

import java.io.IOException;

public abstract class Convolution implements ImageAlgorithm, Kernel {

  @Override
  public Image<RGBA> perform(Image<RGBA> img) {
    Image<RGBA> outImg = new Image<RGBA>(img.cols(), img.rows());
    Image<Float> kernel = getKernel();
    int iterationCounter = 0;

    //TODO: Blatt 2, Aufgabe 1 b)

    // get width and height of image
    int w = img.cols();
    int h = img.rows();
    int kernelHalf = kernel.cols() / 2;    // center at (kernelHalf/kernelHalf)

    // go through original image
    for (int y = 0; y < h; y++) {
      for (int x = 0; x < w; x++) {

        // get the starting location relative to the kernel
        int xStart = x - kernelHalf;
        int yStart = y - kernelHalf;

        // color to be filled in
        RGBA finalColor = new RGBA(0, 0, 0);

        // go through kernel
        for (int i = 0; i < kernel.size(); i++) {

          // get entry in kernel
          float multiplier = kernel.get(i);

          // calculate relative offset for original image
          int xOffset = i % kernel.cols();
          int yOffset = i / kernel.cols();

          // apply offset
          int xCurrent = xStart + xOffset;
          int yCurrent = yStart + yOffset;

          // get color from original image
          RGBA currentColor = img.get(xCurrent, yCurrent);

          // apply kernel to it and add it to the final color
          currentColor = currentColor.times(multiplier);
          finalColor = finalColor.plus(currentColor);
          iterationCounter++;
        }

        // set pixel
        outImg.set(x, y, finalColor);
      }
    }

    System.out.println(iterationCounter + " iterations for input size n = " + img.size());

    return outImg;
  }

  public abstract Image<Float> getKernel();

  @Override
  public void printKernel() {
    Image<Float> kernel = getKernel();
    StringBuilder builder = new StringBuilder("[");
    boolean first = true;

    for (int y = 0; y < kernel.rows(); y++) {

      StringBuilder row = new StringBuilder();
      if (first) {
        first = false;
      } else {
        row.append(" ");
      }

      for (int x = 0; x < kernel.cols(); x++) {
        row.append(kernel.get(x, y));
        row.append(" ");
      }

      builder.append(row);
      builder.append("\n");
    }

    builder.deleteCharAt(builder.length() - 1);
    builder.delete(builder.length() - 1, builder.length());
    builder.append("]");

    System.out.println(builder.toString());
  }
}
