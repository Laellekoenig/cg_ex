package image.processing.convolution;

import image.Image;
import image.RGBA;
import image.processing.ImageAlgorithm;

public abstract class Convolution implements ImageAlgorithm, Kernel {

  @Override
  public Image<RGBA> perform(Image<RGBA> img) {
    Image<RGBA> outImg = new Image<RGBA>(img.cols(), img.rows());
    Image<Float> kernel = getKernel();

    //TODO: Blatt 2, Aufgabe 1 b)

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
