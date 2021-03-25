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

    RGBA value = new RGBA(0, 0, 0, 1);

    for (int x = 0; x < img.cols(); x++) {
      for (int y = 0; y < img.rows(); y++) {
        for (int s = (-1) * (kernel.cols() - 1) / 2; s <= (kernel.cols() - 1) / 2; s++) {
          for (int t = (-1) * (kernel.rows() - 1) / 2; t <= (kernel.rows() - 1) / 2; t++) {
            value = value.plus((img.get(x - s, y - t)).times(kernel.get(s + ((kernel.cols() - 1) / 2)
                    , t + ((kernel.rows() - 1) / 2))));
          }
        }
        //System.out.println(value.toString());
        outImg.set(x, y, value);
        value = new RGBA(0, 0, 0, 1);
      }
    }

    /*try {
      ImageUtils.write(outImg, "test.png");
    } catch (IOException e) {
      e.printStackTrace();
    }*/

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
