package rasterization;

import utils.Vector2;

public class LineRasterizer {

  private LinePixelHandler handler;
  private int w, h;

  public LineRasterizer(LinePixelHandler linePixelHandler, int w, int h) {
    this.handler = linePixelHandler;
    this.w = w;
    this.h = h;
  }

  public void rasterLine(Vector2[] line) {
    assert (line.length > 1);
    for (int i = 0; i < line.length - 1; ++i) {
      bresenham(line[i], line[i + 1]);
    }
  }

  private void bresenham(Vector2 startPoint, Vector2 endPoint) {
    //Blatt 1, Aufgabe 2
    //used for printing later
    Vector2 originalStart = startPoint;
    Vector2 originalEnd = endPoint;

    boolean switched = false;

    //only go from left to right
    if (startPoint.x > endPoint.x) {
      Vector2 temp = startPoint;
      startPoint = endPoint;
      endPoint = temp;
      switched = true;
    }

    double deltaX = endPoint.x - startPoint.x;
    double deltaY = endPoint.y - startPoint.y;
    double m;

    //if the line is vertical
    if (deltaX == 0) {
      if (deltaY < 0) {
        m = -999999999; //-inf
      } else {
        m = 999999999; //inf
      }
    } else {
      //if not vertical, calculate slope
      m = deltaY / deltaX;
    }

    boolean rotated = false;
    double rad = 0; //radiants of rotation

    //if the slope is above 1, rotate clockwise -> back into bresenham domain
    if (m > 1) {
      rotated = true;
      rad = Math.PI / 2;
      startPoint = rotateClock(startPoint, rad);
      endPoint = rotateClock(endPoint, rad);
      deltaX = endPoint.x - startPoint.x;
      deltaY = endPoint.y - startPoint.y;
      m = deltaY / deltaX;
    }

    //if slope is below -1, rotate counter-clockwise -> back into bresenham domain
    if (m < -1) {
      rotated = true;
      rad = -1 * Math.PI / 2;
      startPoint = rotateClock(startPoint, rad);
      endPoint = rotateClock(endPoint, rad);
      deltaX = endPoint.x - startPoint.x;
      deltaY = endPoint.y - startPoint.y;
      m = deltaY / deltaX;
    }

    //if the slope is negative, mirror line on x-axis -> positive slope again
    boolean mirrored = false;
    if (m < 0) {
      mirrored = true;
      startPoint = new Vector2(startPoint.x, -1 * startPoint.y);
      endPoint = new Vector2(endPoint.x, -1 * endPoint.y);
      deltaX = endPoint.x - startPoint.x;
      deltaY = endPoint.y - startPoint.y;
    }

    //how many iterations?
    int iter = (int) Math.abs(deltaX);
    if (Math.abs(deltaX) < Math.abs(deltaY)) iter = (int) Math.abs(deltaY);

    //calculate starting points
    int currentX = (int) startPoint.x;
    int currentY = (int) startPoint.y;
    //calculate starting error, from slide
    double e = deltaY - (deltaX / 2);

    //go through points
    for (int i = 0; i <= iter; i++) {
      //printing variables, necessary in case of transformation -> don't change current variables
      int printX = currentX;
      int printY = currentY;

      //if line was mirrored, reverse -> mirror again via x-axis
      if (mirrored) {
        printY *= -1;
      }

      //if line was rotated, reverse -> -1 * rad reverses rotation
      if (rotated) {
        Vector2 v = new Vector2(printX, printY);
        v = rotateClock(v, -1 * rad); //revert rotation
        printX = (int) Math.round(v.x);
        printY = (int) Math.round(v.y);
      }

      //check if in bounds of image, then print
      if (printX >= 0 && printX < w && printY >= 0 && printY < h) {
        handler.handleLinePixel(printX, printY, originalStart, originalEnd);
      }

      //advance e, currentX and currentY
      if (e <= 0) {
        currentX += 1;
        e += deltaY;
      } else {
        currentX += 1;
        currentY += 1;
        e += deltaY - deltaX;
      }
    }
  }

  /**
   * used to transform coordinates of a vector by rotation
   * @param v: the vector that is to be rotated
   * @param rad: the ammount of rotation, in radiants
   * @return the rotated input vector
   */
  private Vector2 rotateClock(Vector2 v, double rad) {
    //x' = x * cos(rad) + y * sin(rad)
    double xPrime = v.x * Math.cos(rad) + v.y * Math.sin(rad);
    //y' = -x * sin(rad) + y * cos(rad)
    double yPrime = -1 * v.x * Math.sin(rad) + v.y * Math.cos(rad);

    //round to avoid (int) cast rounding mistakes:
    // (int) 3.999999 = 3
    // (int) Math.round(3.99999) = 4
    xPrime = Math.round(xPrime);
    yPrime = Math.round(yPrime);
    return new Vector2(xPrime, yPrime);
  }
}
