package shader;

import utils.BarycentricCoordinates;
import utils.Vector2;
import image.RGBA;

public class InterpolatedColorShader extends PixelShader {


  /**
   * Colors a pixel (x,y) of a line between startPoint and endPoint with the interpolated colors of
   * if RGBA[] lineColors
   */
  @Override
  public void handleLinePixel(int x, int y, Vector2 startPoint, Vector2 endPoint) {
    //TODO: Blatt 1, Aufgabe 5


  }

  /**
   * Colors a pixel (x,y) interpolating the color from the barycentric coordinates and the colors of
   * RGBA[] triangleColors
   */
  @Override
  public void handleTrianglePixel(int x, int y,
      BarycentricCoordinates triCoords) {
    //TODO: Blatt 1, Aufgabe 5


    //triCoords.interpolate();
  }

  @Override
  public void setLineColors(RGBA[] colors) {
    lineColors = colors;
  }

  @Override
  public void setTriangleColors(RGBA[] colors) {
    triangleColors = colors;
  }

}
