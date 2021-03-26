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

  private float gauss(int x) {
    double fraction = 1 / Math.sqrt(2 * Math.PI * Math.pow(sigma, 2));
    double exponent = -1 * Math.pow(x, 2) / 2 * Math.pow(sigma, 2);
    return (float) (fraction * Math.exp(exponent));
  }

  @Override
  public Image<Float> getKernel() {

    if (this.kernel != null) {
      return this.kernel; //for faster access
    }

    Image<Float> kernel = new Image<Float>(size, 1, 0.0f);

    //TODO: Blatt 2, Aufgabe 1 c)
    float[] vals = new float[size];
    float sum = 0;
    int gaussStart = -(size / 2);

    for (int i = 0; i < size; i++) {
      float g = gauss(gaussStart + i);
      vals[i] = g;
      sum += g;
    }

    for (int i = 0; i < size; i++) {
      kernel.set(i, vals[i] / sum);
    }

    this.kernel = kernel;
    return kernel;
  }
}
