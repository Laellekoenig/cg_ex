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

    //TODO: Blatt 1, Aufgabe 2

  }
}
