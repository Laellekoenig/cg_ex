package image.processing.warping;

import image.Image;
import image.ImageUtils;
import image.RGBA;
import utils.Vector2;

public class Morphing {

  private Image<RGBA> imgA, imgB;
  private Image<Vector2> a2b, b2a;

  public Morphing(Image<RGBA> imgA, Image<RGBA> imgB, String flowFieldA2B, String flowFieldB2A) {
    this(imgA, imgB, ImageUtils.readFlowField(flowFieldA2B),
        ImageUtils.readFlowField(flowFieldB2A));
  }

  public Morphing(Image<RGBA> imgA, Image<RGBA> imgB, Image<Vector2> a2b, Image<Vector2> b2a) {
    this.imgA = imgA;
    this.imgB = imgB;

    this.a2b = a2b;
    this.b2a = b2a;
  }

  public Image<RGBA> morph(float lambda) {
    Image<RGBA> outImage = new Image<RGBA>(imgA.cols(), imgA.rows(), new RGBA(0f, 0f, 0f));

    //TODO: Blatt 2, Aufgabe 3 c)

    return outImage;
  }
}
