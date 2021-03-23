package image.processing.convolution;

import image.Image;

public class GaussSeparableConvolution extends SeparableConvolution {

  private int size;
  private double sigma;
  private Image<Float> kernel = null;

  public GaussSeparableConvolution(int size, double sigma) {
    this.size = size;
    this.sigma = sigma;
  }

  @Override
  public Image<Float> getKernel() {
    if (this.kernel != null) {
      return this.kernel; //for faster access
    }

    Image<Float> kernel = new Image<Float>(size, 1, 0.0f);

    //TODO: Blatt 2, Aufgabe 1 c)

    this.kernel = kernel;
    return kernel;
  }
}
