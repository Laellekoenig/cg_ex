package ex2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import image.Image;
import image.ImageUtils;
import image.RGBA;
import image.processing.convolution.GaussConvolution;
import image.processing.scaling.BiCubicInterpolation;
import image.processing.scaling.BiLinearInterpolation;
import image.processing.scaling.GaussianDownsampling;
import image.processing.scaling.NNDownsampling;
import image.processing.scaling.NNInterpolation;
import image.processing.scaling.Upsampling;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

public class ScalingTest {

  private Image<RGBA> chess = new Image<>(20, 20);
  private Image<RGBA> cat;
  private String pathToResources = "data/resources/";

  @Before
  public void setup() {
    try {
      cat = ImageUtils.read(pathToResources + "cat.png");
    } catch (IOException e) {
      fail("Failed loading resources.");
    }

    for (int x = 0; x < chess.cols(); ++x) {
      for (int y = 0; y < chess.rows(); ++y) {
        if ((x + y) % 2 == 0) {
          chess.set(x, y, RGBA.black);
        } else {
          chess.set(x, y, RGBA.white);
        }
      }
    }
  }

  @Test
  public void nnDownsamplingChessTest() {
    Image<RGBA> img = new NNDownsampling().perform(chess);
    for (int x = 0; x < img.cols(); ++x) {
      for (int y = 0; y < img.cols(); ++y) {
        assertEquals(RGBA.black, img.get(x, y));
      }
    }
  }

  @Test
  public void gaussianDownsamplingChessTest() {
    Image<RGBA> img = new GaussianDownsampling().perform(chess);
    for (int x = 1; x < img.cols() - 1; ++x) {
      for (int y = 1; y < img.rows() - 1; ++y) {
        assertEquals(RGBA.grey, img.get(x, y));
      }
    }
  }

  @Test
  public void nnUpsamplingChessTest() {
    Image<RGBA> img = new Upsampling(new NNInterpolation()).perform(chess);
    for (int x = 0; x < img.cols() - 1; ++x) {
      for (int y = 0; y < img.rows() - 1; ++y) {
        assertEquals(((x + 1) / 2 + (y + 1) / 2) % 2 == 0 ? RGBA.black : RGBA.white, img.get(x, y));
      }
    }
  }

  @Test
  public void bilinearUpsamplingChessTest() {
    Image<RGBA> img = new Upsampling(new BiLinearInterpolation()).perform(chess);
    for (int x = 0; x < img.cols() - 1; ++x) {
      for (int y = 0; y < img.rows() - 1; ++y) {
        if (x % 2 == 0 && y % 2 == 0) {
          assertEquals((x / 2 + y / 2) % 2 == 0 ? RGBA.black : RGBA.white, img.get(x, y));
        } else {
          assertEquals(RGBA.grey, img.get(x, y));
        }
      }
    }
  }

  @Test
  public void bicubicUpsamplingChessTest() {
    Image<RGBA> img = new Upsampling(new BiCubicInterpolation()).perform(chess);
    for (int x = 2; x < img.cols() - 3; ++x) {
      for (int y = 2; y < img.rows() - 3; ++y) {
        if (x % 2 == 0 && y % 2 == 0) {
          assertEquals((x / 2 + y / 2) % 2 == 0 ? RGBA.black : RGBA.white, img.get(x, y));
        } else {
          assertEquals(RGBA.grey, img.get(x, y));
        }
      }
    }
  }
}
