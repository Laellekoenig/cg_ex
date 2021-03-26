package image.processing.convolution;

import image.Image;

public class SobelHConvolution extends Convolution {

  private Image<Float> kernel = null;

  @Override
  public Image<Float> getKernel() {
    if (this.kernel != null) {
      return kernel;
    }

    Image<Float> kernelNew = new Image<Float>(3, 3, 0.0f);

    //TODO: Blatt 2, Aufgabe 1 d)
    // first row
    kernelNew.set(0, 1f);
    kernelNew.set(1, 2f);
    kernelNew.set(2, 1f);

    //second row
    kernelNew.set(3, 0f);
    kernelNew.set(4, 0f);
    kernelNew.set(5, 0f);

    //third row
    kernelNew.set(6, -1f);
    kernelNew.set(7, -2f);
    kernelNew.set(8, -1f);

    this.kernel = kernelNew;
    return kernelNew;
  }
}
