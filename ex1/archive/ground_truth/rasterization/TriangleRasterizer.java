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

    //TODO: Blatt 1, Aufgabe 4
  }
}
