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
    //Blatt 1, Aufgabe 5

    //calculate the euclidean distance between start end end
    //sqrt((x_1 - x_2)^2 + (y_1 - y_2)^2)
    double totalDistance = (Math.sqrt(Math.pow((endPoint.x - startPoint.x), 2) + Math.pow((endPoint.y - startPoint.y), 2)));

    //calculate progress already made
    double progress = (Math.sqrt(Math.pow((x - startPoint.x), 2) + Math.pow((y - startPoint.y), 2)));

    //calculate progress in %
    double percentage = progress / totalDistance;

    //adjust mix of colors according to progress
    RGBA color = new RGBA(lineColors[0].r * (1 - percentage) + lineColors[1].r * percentage,
                          lineColors[0].g * (1 - percentage) + lineColors[1].g * percentage,
                          lineColors[0].b * (1 - percentage) + lineColors[1].b * percentage);

    outImg.set(x, y, color);
  }

  /**
   * Colors a pixel (x,y) interpolating the color from the barycentric coordinates and the colors of
   * RGBA[] triangleColors
   */
  @Override
  public void handleTrianglePixel(int x, int y, BarycentricCoordinates triCoords) {
    //Blatt 1, Aufgabe 5
    //use provided interpolation method
    outImg.set(x, y, triCoords.interpolate(triangleColors[0], triangleColors[1], triangleColors[2]));
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
