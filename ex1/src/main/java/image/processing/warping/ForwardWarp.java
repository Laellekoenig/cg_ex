package image.processing.warping;

import java.beans.VetoableChangeListener;

import image.Image;
import image.RGBA;
import image.processing.ImageAlgorithm;
import renderer.SimpleRenderer;
import shader.InterpolatedColorShader;
import utils.Vector2;

public class ForwardWarp implements ImageAlgorithm {

  private Image<Vector2> flowField;

  public ForwardWarp(Image<Vector2> flowField) {
    this.flowField = flowField;
  }

  public ForwardWarp() {
    super();
  }

  @Override
  public Image<RGBA> perform(Image<RGBA> img) {
    //render the targets as triangles
    SimpleRenderer r = new SimpleRenderer(img.cols(), img.rows(), new InterpolatedColorShader());

    //TODO: Blatt 2, Aufgabe 3 b)

    for (int y = 0; y < img.rows(); y += 3) {
      for (int x = 0; x < img.cols(); x++) {
        // get colors
        RGBA colorA = img.get(x, y);
        RGBA colorB = img.get(x, y + 1);
        RGBA colorC = img.get(x, y + 2);

        // create vectors from coordinates
        Vector2 pixelA = new Vector2(x, y);
        Vector2 pixelB = new Vector2(x, y + 1);
        Vector2 pixelC = new Vector2(x, y + 2);

        // apply warp
        pixelA = pixelA.plus(flowField.get(x, y));
        pixelB = pixelB.plus(flowField.get(x, y + 1));
        pixelC = pixelC.plus(flowField.get(x, y + 2));

        // draw resulting triangle
        RGBA[] colors = {colorA, colorB, colorC};
        Vector2[] triangle = {pixelA, pixelB, pixelC};
        r.drawTriangle(triangle, colors);
      }
    }

    return r.getImg();
  }

  public void createRipples(int w, int h, double wavelength) {
    Image<Vector2> ripples = new Image<>(w, h);

    for (int x = 0; x < w; x++) {
      for (int y = 0; y < h; y++) {
        Vector2 val = new Vector2(10.f * Math.cos(2.f * 3.1415 * (float) y / wavelength),
            5.f * Math.sin(2.f * 3.1415 * (float) x / wavelength));
        ripples.set(x, y, val);
      }
    }

    this.flowField = ripples;
  }
}
