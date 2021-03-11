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

  int clippingBinaryCode(Vector2 vector) {
    int result = 0;

    if (vector.x < 0) {
      result += 1;
    }

    if (vector.x > w) {
      result += 2;
    }

    if (vector.y < 0) {
      result += 4;
    }

    if (vector.y > h) {
      result += 8;
    }

    return result;
  }

  private void bresenham(Vector2 startPoint, Vector2 endPoint) {

    //TODO: Blatt 1, Aufgabe 2

    if (startPoint.x > endPoint.x) {
      Vector2 temp = startPoint;
      startPoint = endPoint;
      endPoint = temp;
    }

    double deltaX = endPoint.x - startPoint.x;
    double deltaY = endPoint.y - startPoint.y;

    double m;
    if (deltaX == 0) {
      if (deltaY < 0) {
        m = -999999999; //-inf
      } else {
        m = 999999999; //inf
      }
    } else {
      m = deltaY / deltaX;
    }

    boolean rotated = false;
    double rad = 0;
    if (m > 1) {
      rotated = true;
      rad = Math.PI / 2;
      startPoint = rotateClock(startPoint, rad);
      endPoint = rotateClock(endPoint, rad);
      deltaX = endPoint.x - startPoint.x;
      deltaY = endPoint.y - startPoint.y;
      m = deltaY / deltaX;
    }

    if (m < -1) {
      rotated = true;
      rad = -1 * Math.PI / 2;
      startPoint = rotateClock(startPoint, rad);
      endPoint = rotateClock(endPoint, rad);
      deltaX = endPoint.x - startPoint.x;
      deltaY = endPoint.y - startPoint.y;
      m = deltaY / deltaX;
    }

    boolean mirrored = false;
    if (m < 0) {
      mirrored = true;
      startPoint = new Vector2(startPoint.x, -1 * startPoint.y);
      endPoint = new Vector2(endPoint.x, -1 * endPoint.y);
      deltaX = endPoint.x - startPoint.x;
      deltaY = endPoint.y - startPoint.y;
    }

    System.out.println(m);

    //how many iterations?
    int iter = (int) Math.abs(deltaX);
    if (Math.abs(deltaX) < Math.abs(deltaY)) iter = (int) Math.abs(deltaY);

    int currentX = (int) startPoint.x;
    int currentY = (int) startPoint.y;
    double e = deltaY - (deltaX / 2);

    for (int i = 0; i <= iter; i++) {
      //check if in bounds
      int printX = currentX;
      int printY = currentY;

      if (mirrored) {
        printY *= -1;
      }

      if (rotated) {
        Vector2 v = new Vector2(printX, printY);
        v = rotateClock(v, -1 * rad); //revert rotation
        printX = (int) Math.round(v.x);
        printY = (int) Math.round(v.y);
      }

      if (printX >= 0 && printX < w && printY >= 0 && printY < h) {
        handler.handleLinePixel(printX, printY, startPoint, endPoint);
      }

      if (e <= 0) {
        currentX += 1;
        e += deltaY;
      } else {
        currentX += 1;
        currentY += 1;
        e += deltaY - deltaX;
      }
    }

    /**
     *  The following commented-out code should work, yet because my implementation can't draw vertical lines yet,
     *  the code enters an infinite loop when tested in that regard. Hence it being commented-out.
     */
    /*int startPointBin = clippingBinaryCode(startPoint);
    int endPointBin = clippingBinaryCode(endPoint);

    while (clippingBinaryCode(startPoint) != 0 || clippingBinaryCode(endPoint) != 0) {
      if ((startPointBin & endPointBin) != 0) {
        return;
      }

      if (startPointBin >= 8 || endPointBin >= 8) {
        Vector2 cutYmin = new Vector2((-1) * b / m, 0);
        startPoint = cutYmin;
        System.out.println("cutymin");
      } else if (startPointBin >= 4 || endPointBin >= 4) {
        Vector2 cutYmax = new Vector2((h - b) / m, h);
        endPoint = cutYmax;
        System.out.println("cutymax");
      } else if (startPointBin >= 2 || endPointBin >= 2) {
        Vector2 cutXmax = new Vector2(w, (m * w) + b);
        endPoint = cutXmax;
        System.out.println("cutxmax,  (" + w + ", " + ((m * w) + b) + ")");
      } else {
        Vector2 cutXmin = new Vector2(0, b);
        startPoint = cutXmin;
        System.out.println("cutxmin");
      }

      startPointBin = clippingBinaryCode(startPoint);
      endPointBin = clippingBinaryCode(endPoint);

      System.out.println("it");
    }*/

    /*
    int x = (int) startPoint.x;
    int y = (int) startPoint.y;

    handler.handleLinePixel(x, y, startPoint, endPoint);

    if (deltaX >= deltaY) {
      double E = 2 * deltaY - deltaX;
      for (int i = 0; i < deltaX; i++) {
        if (E < 0) {
          x++;
          E = E + (2 * deltaY);
        } else {
          x++;
          if (endPoint.y > startPoint.y) {
            y++;
          } {
            y--;
          }
          E = E + (2 * deltaY) - (2 * deltaX);
        }
        handler.handleLinePixel(x, y, startPoint, endPoint);
      }
    }
     */
  }

  private Vector2 rotateClock(Vector2 v, double rad) {
    //x' = x * cos(rad) + y * sin(rad)
    double xPrime = v.x * Math.cos(rad) + v.y * Math.sin(rad);
    //y' = -x * sin(rad) + y * cos(rad)
    double yPrime = -1 * v.x * Math.sin(rad) + v.y * Math.cos(rad);
    xPrime = Math.round(xPrime);
    yPrime = Math.round(yPrime);
    return new Vector2(xPrime, yPrime);
  }
}
