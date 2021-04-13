package shader;

import rasterization.LinePixelHandler;
import rasterization.TrianglePixelHandler;
import image.Image;
import image.RGBA;

public abstract class PixelShader implements LinePixelHandler, TrianglePixelHandler {

  protected Image<RGBA> outImg;
  protected RGBA[] triangleColors, lineColors;
  protected RGBA constantColor;

  public void setImgSize(int w, int h) {
    outImg = new Image<RGBA>(w, h);
  }

  public void setImage(Image<RGBA> img) {
    this.outImg = img;
  }

  public Image<RGBA> getImage() {
    return outImg;
  }

  public void clearImage() {
    for (int i = 0; i < outImg.size(); i++) {
      if (outImg.get(i) != null) {
        RGBA reset = outImg.get(i);
        reset.a = 1f;
        reset.r = 0;
        reset.g = 0;
        reset.b = 0;
      }
    }
  }

  public abstract void setLineColors(RGBA[] colors);

  public abstract void setTriangleColors(RGBA[] colors);

}
