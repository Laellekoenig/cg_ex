package image.processing.convolution;

import image.Image;

public class LaplaceConvolution extends Convolution {

  private Image<Float> kernel = null;

  /**
   * A simple Laplace Operator to detect edges.
   */
  @Override
  public Image<Float> getKernel() {
    if (this.kernel != null) {
      return this.kernel;
    }

    Image<Float> kernelNew = new Image<Float>(3, 3, 0.0f);

    //TODO: Blatt 2, Aufgabe 1 d)

    this.kernel = kernelNew;
    return kernelNew;
  }

}
