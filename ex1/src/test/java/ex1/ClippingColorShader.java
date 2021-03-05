package ex1;

import image.RGBA;
import shader.PixelShader;
import utils.BarycentricCoordinates;
import utils.Vector2;

public class ClippingColorShader extends PixelShader {

  private int lineCounter = 0;
  private int triangleCounter = 0;

  @Override
  public void setLineColors(RGBA[] colors) {
  }

  @Override
  public void setTriangleColors(RGBA[] colors) {
  }

  @Override
  public void handleLinePixel(int x, int y, Vector2 va, Vector2 vb) {
    ++lineCounter;
  }

  @Override
  public void handleTrianglePixel(int x, int y, BarycentricCoordinates triCoords) {
    ++triangleCounter;
  }

  public int getLineCount() {
    return lineCounter;
  }

  public int getTriangleCount() {
    return triangleCounter;
  }
}
