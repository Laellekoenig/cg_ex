package shader;

import image.RGBA;
import utils.BarycentricCoordinates;
import utils.Vector2;

public class ConstantColorShader extends PixelShader {


  public void setConstantColor(RGBA color) {
    constantColor = color;
  }

  @Override
  public void handleLinePixel(int x, int y, Vector2 startPoint, Vector2 endPoint) {
    outImg.set(x, y, constantColor);
  }

  @Override
  public void handleTrianglePixel(int x, int y,
      BarycentricCoordinates triCoords) {
    outImg.set(x, y, constantColor);
  }

  @Override
  public void setLineColors(RGBA[] colors) {
    constantColor = colors[0];
  }

  @Override
  public void setTriangleColors(RGBA[] colors) {
    constantColor = colors[0];
  }

}