package ex1;

import exercises.Ex1;
import image.Image;
import image.ImageUtils;
import image.RGBA;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ImageFlipTest {

  private Image<RGBA> img;
  private Image<RGBA> flipped;
  private RGBA color = new RGBA(1, 0, 0);

  @Before
  public void setup() {
    img = new Image<>(160, 90);
    int radius = 20;
    for (int x = 0; x <= 2 * radius; ++x) {
      for (int y = 0; y <= 2 * radius; ++y) {
        if (Math.pow(x - radius, 2) + Math.pow(y - radius, 2) <= Math.pow(radius, 2)) {
          img.set(x, y, color);
        }
      }
    }
    for (int x = 0; x < img.cols(); ++x) {
      img.set(x, img.rows() - 1, color);
    }
    flipped = Ex1.flipImageUpsideDown(img);

    // This writes the files into out/test/ for visual comparison.
    try {
      ImageUtils.write(img, "testImg.png");
      ImageUtils.write(flipped, "flippedTestImg.png");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testImageDoubleFlip() {
    Image<RGBA> unflipped = Ex1.flipImageUpsideDown(flipped);
    for (int x = 0; x < img.cols(); x++) {
      for (int y = 0; y < img.rows(); y++) {
        assertEquals("Image should be identical to original if flipped twice, but is not.",
            img.get(x, y), unflipped.get(x, y));
      }
    }
  }

  @Test
  public void testImageSize() {
    assertEquals("Flipped image has not the same size as the original.", img.size(),
        flipped.size());
    assertEquals("Flipped image has not the same height as the original.", img.rows(),
        flipped.rows());
    assertEquals("Flipped image has not the same width as the original.", img.cols(),
        flipped.cols());
  }

  @Test
  public void testImageBottomRowCorrectlyFlippedToTop() {
    for (int x = 0; x < img.cols(); ++x) {
      assertEquals("Image is displaced vertically by at least 1.", color, flipped.get(x, 0));
    }
  }
}
