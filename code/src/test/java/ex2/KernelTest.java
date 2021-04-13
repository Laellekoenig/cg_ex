package ex2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import image.Image;
import image.ImageUtils;
import image.RGBA;
import image.processing.convolution.Convolution;
import image.processing.convolution.GaussConvolution;
import image.processing.convolution.GaussSeparableConvolution;
import image.processing.convolution.LaplaceConvolution;
import image.processing.convolution.MeanConvolution;
import image.processing.convolution.SobelHConvolution;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

public class KernelTest {

  private Image<RGBA> cat;
  private Image<RGBA> chess = new Image<>(20, 20);
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
  public void testMeanKernelOfSize1() {
    Image<RGBA> img = new MeanConvolution(1).perform(chess);
    System.out.println("arrived");
    for (int x = 0; x < chess.cols(); ++x) {
      for (int y = 0; y < chess.rows(); ++y) {
        assertEquals(chess.get(x, y), img.get(x, y));
      }
    }
  }

  @Test
  public void testMeanKernel() {
    int[] sizes = {3, 5, 7, 9};
    for (int size : sizes) {
      Image<RGBA> img = new MeanConvolution(size).perform(chess);
      for (int x = size - 1; x < chess.cols() - size + 1; ++x) {
        for (int y = size - 1; y < chess.rows() - size + 1; ++y) {
          double factor = Math.floor(Math.pow(size, 2) / 2) / Math.pow(size, 2);
          if ((x + y) % 2 == 0) {
            assertEquals(RGBA.white.times(factor).plus(RGBA.black.times(1 - factor)),
                img.get(x, y));
          } else {
            assertEquals(RGBA.white.times(1 - factor).plus(RGBA.black.times(factor)),
                img.get(x, y));
          }
        }
      }
    }
  }

  @Test
  public void testMeanFilter() {
    testKernel("meanfilter.png", new MeanConvolution(10), false, "");
  }

  @Test
  public void testGaussKernel() {
    testKernel("gaussfilter.png", new GaussConvolution(5, 2), false, "");
  }

  @Test
  public void testSeparableGaussKernel() {
    testKernel("gaussfiltered_separable.png", new GaussSeparableConvolution(5, 2), false,
        "Make sure that the output image was filtered both in horizontal and vertical direction");
  }

  @Test
  public void testLaplaceConvolution() {
    testKernel("laplacefilter.png", new LaplaceConvolution(), true, "");
  }

  @Test
  public void testSobelHConvolution() {
    testKernel("sobelfilter.png", new SobelHConvolution(), true, "");
  }

  private void testKernel(String filename, Convolution kernel, boolean normalize, String msg) {
    Image<RGBA> groundTruth = null;
    try {
      groundTruth = ImageUtils.read(pathToResources + "ex2/" + filename);
    } catch (IOException e) {
      fail("Failed loading ground truth.");
    }
    Image<RGBA> filtered = kernel.perform(cat);
    if (normalize) {
      ImageUtils.normalize(filtered);
    }

    assertEquals(groundTruth.size(), filtered.size());
    assertEquals(groundTruth.cols(), filtered.cols());
    assertEquals(groundTruth.rows(), filtered.rows());
    for (int x = 0; x < filtered.cols(); ++x) {
      for (int y = 0; y < filtered.rows(); ++y) {
        assertEquals("Failed at " + x + "/" + y + "\n" + msg, groundTruth.get(x, y),
            filtered.get(x, y));
      }
    }
  }

}
