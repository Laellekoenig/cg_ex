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

  private double gauss(int x, int y) {
    // components of equation
    double mainFraction = 1 / (2 * Math.PI * Math.pow(sigma, 2));
    double exponent = (-1) * (Math.pow(x, 2) + Math.pow(y, 2)) / (2 * Math.pow(sigma, 2));

    // return value from full equation
    return mainFraction * Math.exp(exponent);
  }

  public Image<Float> getKernel() {
    if (this.kernel != null) {
      return this.kernel;
    }

    Image<Float> kernelNew = new Image<Float>(size, size, 0.0f);

    //TODO: Blatt 2, Aufgabe 1 a)

    // array for calculated values, norm afterwards
    double[] gaussianVals = new double[size * size];
    double sum = 0;  // for norming later
    int i = 0;

    for (int x = 0; x < size; x++) {
      for (int y = 0; y < size; y++) {
        // calculate gauss and add to array
        double g = gauss(x, y);
        gaussianVals[i++] = g;
        // add to sum
        sum += g;
      }
    }

    //norm values and set to image
    for (int j = 0; j < gaussianVals.length; j++) {
      double norm = gaussianVals[j] / sum;
      kernelNew.set(j, (float) norm);
    }

    this.kernel = kernelNew;
    return kernelNew;
  }
}
