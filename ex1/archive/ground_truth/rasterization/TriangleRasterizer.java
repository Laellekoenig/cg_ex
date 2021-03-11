package rasterization;


import utils.BarycentricCoordinateTransform;
import utils.BarycentricCoordinates;
import utils.Vector2;

public class TriangleRasterizer {

  private TrianglePixelHandler handler;
  private int w, h;


  public TriangleRasterizer(TrianglePixelHandler handler, int w, int h) {
    this.handler = handler;
    this.w = w;
    this.h = h;
  }

  public void rasterTriangle(Vector2[] trianglePoints) {
    Vector2 va = trianglePoints[0];
    Vector2 vb = trianglePoints[1];
    Vector2 vc = trianglePoints[2];

    BarycentricCoordinateTransform bct = new BarycentricCoordinateTransform(va, vb, vc);
    if (bct.isDegenerate()) {
      return;
    }

    int xMin = (int) Math.round(va.x), xMax = (int) Math.round(va.x), yMin = (int) Math.round(va.y)
            , yMax = (int) Math.round(va.y);

    for(Vector2 v : new Vector2[]{vb, vc}) {
      if(v.x < xMin) {
        xMin = (int) Math.round(v.x);
      }

      if(v.x > xMax) {
        xMax = (int) Math.round(v.x);
      }

      if(v.y < yMin) {
        yMin = (int) Math.round(v.y);
      }

      if(v.y > yMax) {
        yMax = (int) Math.round(v.y);
      }
    }

    handler.handleTrianglePixel(xMin, yMin, bct.getBarycentricCoordinates(xMin, yMin));

    //System.out.printf("yMin: %d, yMax: %d, \n", yMin, yMax);

    BarycentricCoordinates bc;

    for(int y = yMin; y <= yMax; y++) {
      for(int x = xMin; x <= xMax; x++) {
        bc = bct.getBarycentricCoordinates(x, y);
        if(bc.isInside()) {
          handler.handleTrianglePixel(x, y, bc);
        } else {
          //System.out.printf("(%d, %d) \n", x, y);
        }
      }
    }

    //(0 < bc.x && bc.x < 1) && (0 < bc.y && bc.y < 1) && (0 < bc.z && bc.z < 1)

    //TODO: Blatt 1, Aufgabe 4
  }
}
