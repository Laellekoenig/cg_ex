package testSuite.testTemplates;

import java.io.IOException;

import image.Image;
import image.ImageUtils;
import image.RGBA;

public abstract class ImageTest extends VisualTest {

  protected Image<RGBA> baseImage;
  protected Image<RGBA> smallImage;
  protected Image<RGBA> bigImage;

  public ImageTest(String gsFileName, String title) {
    super(gsFileName, title);

    try {
      baseImage = ImageUtils.read("data/resources/cat.png");
      smallImage = ImageUtils.read("data/resources/cat-small.png");
      bigImage = ImageUtils.read("data/resources/cat-big.png");
    } catch (IOException e) {
      System.err.println("Cannot read resource images.");
    }
  }


}
