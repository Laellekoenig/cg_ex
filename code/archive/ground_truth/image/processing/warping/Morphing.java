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

    // apply lambda to vector field a2b
    for (int i = 0; i < a2b.size(); i++) {
      Vector2 vector = a2b.get(i);
      vector = vector.times(lambda);
      a2b.set(i, vector);
    }

    // apply lambda to vector field b2a
    for (int i = 0; i < b2a.size(); i++) {
      Vector2 vector = b2a.get(i);
      vector = vector.times(1 - lambda);
      b2a.set(i, vector);
    }

    // apply new vector fields to images A and B, "morph into same position"
    BackwardWarp backwardWarp = new BackwardWarp(a2b);
    imgA = backwardWarp.perform(imgA);
    backwardWarp = new BackwardWarp(b2a);
    imgB = backwardWarp.perform(imgB);

    // go through output image
    for (int i = 0; i < outImage.size(); i++) {
      // get corresponding warped pixels from images A and B
      RGBA colorA = imgA.get(i);
      RGBA colorB = imgB.get(i);

      // morph colors -> determine output pixel color
      colorA = colorA.times(lambda);
      colorB = colorB.times(1 - lambda);

      outImage.set(i, colorA.plus(colorB));
    }

    return outImage;
  }
}
