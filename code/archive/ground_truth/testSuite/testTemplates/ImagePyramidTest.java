package testSuite.testTemplates;

import image.Image;
import image.RGBA;
import image.processing.ImageAlgorithm;

import java.util.Vector;

public abstract class ImagePyramidTest extends ImageTest {

  private int levels;

  public ImagePyramidTest(String gsFileName, String title, int levels) {
    super(gsFileName, title);
    this.levels = levels;
  }

  protected Vector<Image<RGBA>> buildPyramidOf(Image<RGBA> img, ImageAlgorithm algo) {
    Vector<Image<RGBA>> pyramid = new Vector<Image<RGBA>>(levels);
    pyramid.add(img);

    for (int i = 1; i < levels; i++) {
      pyramid.add(algo.perform(pyramid.get(i - 1)));
    }

    return pyramid;
  }

}
