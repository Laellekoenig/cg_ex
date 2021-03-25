package image.processing.convolution;

import image.Image;

public class MeanConvolution extends Convolution {

  private int size;
  private Image<Float> kernel = null;

  public MeanConvolution(int size) {
    this.size = size;
  }

  public Image<Float> getKernel() {
    if (this.kernel != null) {
      return this.kernel;
    }

    Image<Float> kernelNew = new Image<Float>(size, size, 0.0f);

    //Blatt 2, Aufgabe 1 a)

    for (int i = 0; i < kernelNew.size(); i++) {
      // average: 1 / numPixels
      kernelNew.set(i, (float) 1 / (size * size));
    }

    this.kernel = kernelNew;
    return kernelNew;
  }
}
