package image.processing.convolution;

import image.Image;

public class GaussConvolution extends Convolution {

  private int size;
  private double sigma;
  private Image<Float> kernel = null;

  public GaussConvolution(int size, double sigma) {
    this.size = size;
    this.sigma = sigma;
  }

  private float gauss(int x, int y) {
    return (float) ((1 / (2 * Math.PI * sigma * sigma)) * Math.exp(-(x * x + y * y) / (2.0 * sigma * sigma)));
  }

  public Image<Float> getKernel() {
    if (this.kernel != null) {
      return this.kernel;
    }

    Image<Float> kernelNew = new Image<Float>(size, size, 0.0f);

    //TODO: Blatt 2, Aufgabe 1 a)

    for (int x = 0; x < size; x++) {
      for (int y = 0; y < size; y++) {
        kernelNew.set(x, y, gauss(x - ((size - 1) / 2), y - ((size - 1) / 2)));
      }
    }

    this.kernel = kernelNew;
    return kernelNew;
  }

}
