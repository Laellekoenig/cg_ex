package rasterization;

import utils.Vector2;

public interface LinePixelHandler {

  public void handleLinePixel(int x, int y, Vector2 va, Vector2 vb);

}
