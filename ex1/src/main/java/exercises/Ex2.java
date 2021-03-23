package exercises;

import image.processing.scaling.BiCubicInterpolation;
import java.io.File;
import java.io.IOException;

import image.Image;
import image.ImageUtils;
import image.RGBA;
import image.processing.ImageAlgorithm;
import image.processing.convolution.GaussConvolution;
import image.processing.convolution.GaussSeparableConvolution;
import image.processing.convolution.LaplaceConvolution;
import image.processing.convolution.MeanConvolution;
import image.processing.convolution.SobelHConvolution;
import image.processing.scaling.BiLinearInterpolation;
import image.processing.scaling.GaussianDownsampling;
import image.processing.scaling.NNDownsampling;
import image.processing.scaling.NNInterpolation;
import image.processing.scaling.Upsampling;
import image.processing.warping.BackwardWarp;
import image.processing.warping.ForwardWarp;
import image.processing.warping.Morphing;

public class Ex2 {

  private static final String outPath = "out/images/ex2/";

  public static void main(String[] args) {
    prepareOutput(outPath);

    //Blatt 2 Aufgabe 1 Beispiele
    blatt2Aufgabe1();

    //Blatt 2 Aufgabe 2 Beispiele
    blatt2Aufgabe2();

    //Blatt 3 Aufgabe 3 Beispiele
    blatt2Aufgabe3();
  }

  public static void prepareOutput(String outPath) {
    File outDir = new File(outPath);
    if (!outDir.exists()) {
      outDir.mkdirs();
    }
  }


  public static void blatt2Aufgabe1() {
    applyKernel(new MeanConvolution(10), outPath + "meanfilter.png", false);

    applyKernel(new GaussConvolution(5, 2.0), outPath + "gaussfilter.png", false);

    applyKernel(new GaussSeparableConvolution(5, 2.0), outPath + "gaussfiltered_separable.png",
        false);

    applyKernel(new LaplaceConvolution(), outPath + "laplacefilter.png", true);

    applyKernel(new SobelHConvolution(), outPath + "sobelfilter.png", true);
  }

  public static void blatt2Aufgabe2() {
    applyKernel(new NNDownsampling(), outPath + "nnDownsampling.png", false);

    applyKernel(new GaussianDownsampling(), outPath + "GaussianDownsampling.png", false);

    applyKernel(new Upsampling(new NNInterpolation()), outPath + "nnUpsampling.png", false);

    applyKernel(new Upsampling(new BiLinearInterpolation()), outPath + "linearUpsampling.png",
        false);

    applyKernel(new Upsampling(new BiCubicInterpolation()), outPath + "cubicUpsampling.png", false);
  }

  public static void blatt2Aufgabe3() {

    backwardWarp();

    forwardWarp();

    morph();
  }


  public static void applyKernel(ImageAlgorithm algorithm, String outName, Boolean normalize) {
    try {
      Image<RGBA> original = ImageUtils.read("data/resources/cat.png");

      Image<RGBA> filtered = algorithm.perform(original);

      if (normalize) {
        ImageUtils.normalize(filtered);
      }

      ImageUtils.write(filtered, outName);
      System.out.println("Image created: " + outName);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void backwardWarp() {
    try {
      Image<RGBA> img = ImageUtils.read("data/resources/thomas.png");

      BackwardWarp warp = new BackwardWarp();
      double strength = 3d;
      int radius = 500;
      warp.createSwirlField(img.cols(), img.rows(), radius, strength);
      Image<RGBA> warped = warp.perform(img);

      String outName = outPath + "backwardwarp.png";
      ImageUtils.write(warped, outName);
      System.out.println("Image created: " + outName);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void forwardWarp() {
    try {
      Image<RGBA> img = ImageUtils.read("data/resources/thomas.png");

      int wavelength = 100;
      ForwardWarp warp = new ForwardWarp();
      warp.createRipples(img.cols(), img.rows(), wavelength);
      Image<RGBA> warped = warp.perform(img);

      String outName = outPath + "forwardwarp.png";
      ImageUtils.write(warped, outName);
      System.out.println("Image created: " + outName);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void morph() {
    try {
      Image<RGBA> anette = ImageUtils.read("data/resources/anette.png");
      Image<RGBA> thomas = ImageUtils.read("data/resources/thomas.png");

      for (float lambda = -0.4f; lambda <= 1.4; lambda += 0.2) {
        Morphing morpher = new Morphing(thomas, anette, "data/resources/t_a.f2image",
            "data/resources/a_t.f2image");
        Image<RGBA> morphed = morpher.morph(lambda);
        String outName = outPath + "thomas_to_anette_" + lambda + ".png";
        ImageUtils.write(morphed, outName);
        System.out.println("Image created: " + outName);

      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

