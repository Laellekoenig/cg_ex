package image.processing.convolution;

import image.Image;

public interface Kernel {

  public Image<Float> getKernel();

  public void printKernel();

}
