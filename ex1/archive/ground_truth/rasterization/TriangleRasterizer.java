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

    //TODO: Blatt 1, Aufgabe 4
    Vector2 va = trianglePoints[0];
    Vector2 vb = trianglePoints[1];
    Vector2 vc = trianglePoints[2];

    //round coordinates
    /*
    va = new Vector2(Math.round(va.x), Math.round(va.y));
    vb = new Vector2(Math.round(vb.x), Math.round(vb.y));
    vc = new Vector2(Math.round(vc.x), Math.round(vc.y));
    */

    //check if valid triangle
    BarycentricCoordinateTransform bct = new BarycentricCoordinateTransform(va, vb, vc);
    if (bct.isDegenerate()) {
      return;
    }

    //determine min and max X and Y values
    int xMin = (int) va.x;  //assumption
    if (vb.x < xMin) xMin = (int) vb.x;
    if (vc.x < xMin) xMin = (int) vc.x;
    if (xMin < 0)    xMin = 0;

    int xMax = (int) va.x;  //assumption
    if (vb.x > xMax) xMax = (int) vb.x;
    if (vc.x > xMax) xMax = (int) vc.x;
    if (xMax >= w)   xMax = w - 1;

    int yMin = (int) va.y;  //assumption
    if (vb.y < yMin) yMin = (int) vb.y;
    if (vc.y < yMin) yMin = (int) vc.y;
    if (yMin < 0)    yMin = 0;

    int yMax = (int) va.y;  //assumption
    if (vb.y > yMax) yMax = (int) vb.y;
    if (vc.y > yMax) yMax = (int) vc.y;
    if (yMax >= h)   yMax = h - 1;

    BarycentricCoordinates bc;

    for (int i = yMin; i <= yMax; i++) {
      for (int j = xMin; j <= xMax; j++) {
        bc = bct.getBarycentricCoordinates(j, i);
        if (bc.isInside()) {
          handler.handleTrianglePixel(j, i, bc);
        }
      }
    }
  }
}
