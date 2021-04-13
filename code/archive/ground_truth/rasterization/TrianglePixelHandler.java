package rasterization;

import utils.BarycentricCoordinates;

public interface TrianglePixelHandler {

  public void handleTrianglePixel(int x, int y, BarycentricCoordinates triCoords);

}
