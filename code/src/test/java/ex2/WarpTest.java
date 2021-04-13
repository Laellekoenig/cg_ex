package ex2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import image.Image;
import image.ImageUtils;
import image.RGBA;
import image.processing.warping.BackwardWarp;
import image.processing.warping.ForwardWarp;
import image.processing.warping.Morphing;
import java.io.IOException;
import org.junit.Test;
import utils.Vector2;

public class WarpTest {

  private RGBA color = RGBA.red;
  private String pathToResources = "data/resources/";

  @Test
  public void backwardWarpIdentityTest() {
    try {
      Image<RGBA> img = ImageUtils.read(pathToResources + "cat.png");

      BackwardWarp warp = new BackwardWarp(new Image<>(img.cols(), img.rows(), new Vector2(0, 0)));
      Image<RGBA> out = warp.perform(img);

      for (int x = 0; x < img.cols(); ++x) {
        for (int y = 0; y < img.rows(); ++y) {
          assertEquals(
              "Warped image is not identical to original when warped with static flow field.",
              img.get(x, y), out.get(x, y));
        }
      }
    } catch (IOException e) {
      fail("Failed loading resources.");
    }
  }

  @Test
  public void backwardWarpFromSinglePixelTest() {
    int size = 9;
    int center = size / 2;

    Image<RGBA> img = new Image<>(size, size, RGBA.black);
    img.set(center, center, color);

    Image<Vector2> flow = new Image<>(size, size);
    for (int x = 0; x < size; ++x) {
      for (int y = 0; y < size; ++y) {
        flow.set(x, y, new Vector2(center - x, center - y));
      }
    }

    BackwardWarp warp = new BackwardWarp(flow);
    Image<RGBA> out = warp.perform(img);

    for (int x = 0; x < size; ++x) {
      for (int y = 0; y < size; ++y) {
        assertEquals(color, out.get(x, y));
      }
    }
  }

  @Test
  public void forwardWarpIdentityTest() {
    try {
      Image<RGBA> img = ImageUtils.read(pathToResources + "cat.png");

      ForwardWarp warp = new ForwardWarp(new Image<>(img.cols(), img.rows(), new Vector2(0, 0)));
      Image<RGBA> out = warp.perform(img);

      for (int x = 0; x < img.cols(); ++x) {
        for (int y = 0; y < img.rows(); ++y) {
          assertEquals(
              "Warped image is not identical to original when warped with static flow field.",
              img.get(x, y), out.get(x, y));
        }
      }
    } catch (IOException e) {
      fail("Failed loading resources.");
    }
  }

  @Test
  public void forwardWarpToSinglePixelTest() {
    int size = 9;
    int center = size / 2;
    Image<RGBA> img = new Image<>(size, size, color);

    Image<Vector2> flow = new Image<>(size, size);
    for (int x = 0; x < size; ++x) {
      for (int y = 0; y < size; ++y) {
        flow.set(x, y, new Vector2(center - x, center - y));
      }
    }

    ForwardWarp warp = new ForwardWarp(flow);
    Image<RGBA> out = warp.perform(img);

    for (int x = 0; x < size; ++x) {
      for (int y = 0; y < size; ++y) {
        // All triangles are degenerate -> nothing should be drawn.
        assertNull("Make sure that only triangles completely inside the original image are warped.",
            out.get(x, y));
      }
    }
  }

  @Test
  public void morphIdentityColorInterpolationTest() {
    int size = 10;
    Image<RGBA> srcRed = new Image<>(size, size, RGBA.red);
    Image<RGBA> srcBlue = new Image<>(size, size, RGBA.blue);
    Image<Vector2> flow = new Image<>(size, size, new Vector2(0, 0));

    Morphing morpher = new Morphing(srcRed, srcBlue, flow, flow);
    for (float lambda = 0; lambda <= 1; lambda += 0.1) {
      Image<RGBA> morphed = morpher.morph(lambda);
      RGBA expected = new RGBA(lambda, 0, 1 - lambda);

      for (int x = 0; x < size; ++x) {
        for (int y = 0; y < size; ++y) {
          assertEquals(expected, morphed.get(x, y));
        }
      }
    }
  }
}
