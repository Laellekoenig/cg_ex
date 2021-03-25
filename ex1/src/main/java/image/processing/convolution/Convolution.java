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

    printKernel();

    //TODO: Blatt 2, Aufgabe 1 b)

    // get width and height of image
    int w = img.cols();
    int h = img.rows();

    // determine the center of the kernel
    int kernelSize = kernel.cols();       // kernel must be a square
    // check if kernel even
    boolean even = kernelSize % 2 == 0;
    int kernelWidth = kernelSize / 2;    // center at (kernelWidth/kernelWidth)

    printKernel();

    // go through original image
    for (int x = 0; x < w; x++) {
      for (int y = 0; y < h; y++) {

        RGBA color = new RGBA(0, 0, 0); // final color for new image
        int i = 0;  // for going through kernel

        // calculate bottom and right bounds of kernel
        // different if kernel is even (not in exact center)
        int yBound = y + kernelWidth;
        int xBound = x + kernelWidth;
        if (!even) {
          // go one step further if kernel has a perfect center
          yBound++;
          xBound++;
        }

        // apply kernel to portion of image
        // this will not work for even kernel sizes (index out of bounds)
        for (int ky = y - kernelWidth; ky < yBound; ky++) {
          for (int kx = x - kernelWidth; kx < xBound; kx++) {

            // get current color
            RGBA current = img.get(kx, ky);
            // get kernel value of same pixel and advance i for next
            float mult = kernel.get(i++);
            // apply multiplication to current pixel
            current = current.times(mult);
            // add result to the sum of all results
            color = color.plus(current);
          }
        }

        //set the calculated color in new image
        outImg.set(x, y, color);
      }
    }

    try {
      ImageUtils.write(outImg, "test.png");
    } catch (IOException e) {
      e.printStackTrace();
    }

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
