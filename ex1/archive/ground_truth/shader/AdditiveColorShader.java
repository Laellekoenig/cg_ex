package shader;

import image.RGBA;
import utils.BarycentricCoordinates;
import utils.Vector2;

public class AdditiveColorShader extends PixelShader {

  private RGBA constantColor = new RGBA(0.25f, 0.25f, 0.25f);

  @Override
  public void handleLinePixel(int x, int y, Vector2 va, Vector2 vb) {
    outImg.set(x, y, outImg.get(x, y).plus(constantColor));
  }

  @Override
  public void handleTrianglePixel(int x, int y,
      BarycentricCoordinates triCoords) {

    if (outImg.get(x, y) == null) {
      outImg.set(x, y, constantColor);
    } else {
      outImg.set(x, y, outImg.get(x, y).plus(constantColor));
    }
  }

  @Override
  public void setLineColors(RGBA[] colors) {
    lineColors = colors;
  }

  @Override
  public void setTriangleColors(RGBA[] colors) {
    this.triangleColors = colors;
  }

}
