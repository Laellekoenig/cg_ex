package image.processing.warping;

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

    for (int i = 0; i < img.size(); i += 3) {
      // get three original colors
      RGBA a = img.get(i);
      RGBA b = img.get(i + 1);
      RGBA c = img.get(i + 2);

      // get 2d coordinates
      int x1 = i % img.cols();
      int y1 = i / img.cols();
      int x2 = (i + 1) % img.cols();
      int y2 = (i + 1) / img.cols();
      int x3 = (i + 2) % img.cols();
      int y3 = (i + 2) / img.cols();

      // apply transformation
      Vector2 aVect =  flowField.get(x1, y1);
      Vector2 bVect =  flowField.get(x2, y2);
      Vector2 cVect =  flowField.get(x3, y3);
      x1 += aVect.x;
      y1 += aVect.y;
      x2 += bVect.x;
      y2 += bVect.y;
      x3 += cVect.x;
      y3 += cVect.y;

      Vector2[] triangle = {new Vector2(x1, y1), new Vector2(x2, y2), new Vector2(x3, y3)};
      RGBA[] colors = {a, b, c};
      r.drawTriangle(triangle, colors);
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
