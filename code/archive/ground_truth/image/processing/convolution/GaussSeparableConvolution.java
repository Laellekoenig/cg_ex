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
    // components of equation
    double fraction = 1 / Math.sqrt(2 * Math.PI * Math.pow(sigma, 2));
    double exponent = (-1) * Math.pow(x, 2) / 2 * Math.pow(sigma, 2);

    // return value from full equation
    return (float) (fraction * Math.exp(exponent));
  }

  @Override
  public Image<Float> getKernel() {

    if (this.kernel != null) {
      return this.kernel; //for faster access
    }

    Image<Float> kernel = new Image<Float>(size, 1, 0.0f);

    //TODO: Blatt 2, Aufgabe 1 c)

    // array for calculated values, norm afterwards
    float[] gaussianVals = new float[size];
    float sum = 0;  // for norming
    // calculate offset for placing gaussian bell in the middle of kernel
    int gaussStart = -(size / 2);

    for (int i = 0; i < size; i++) {
      // calculate gauss with offset and add to array
      float g = gauss(gaussStart + i);
      gaussianVals[i] = g;
      // add to sum
      sum += g;
    }

    //norm values and set to kernel
    for (int i = 0; i < size; i++) {
      kernel.set(i, gaussianVals[i] / sum);
    }

    this.kernel = kernel;
    return kernel;
  }
}
