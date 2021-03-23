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

  public Image<Float> getKernel() {
    if (this.kernel != null) {
      return this.kernel;
    }

    Image<Float> kernelNew = new Image<Float>(size, size, 0.0f);

    //TODO: Blatt 2, Aufgabe 1 a)

    this.kernel = kernelNew;
    return kernelNew;
  }

}
