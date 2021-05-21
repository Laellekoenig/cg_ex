package utils;

import java.util.Random;

public final class RandomHelper {

  private static final int seed = 42;
  private static Random R = new Random(seed);

  public static void resetRandomSeed() {
    R = new Random(seed);
  }

  /**
   * Returns a uniformly distributed random point on the surface of the unit sphere.
   */
  public static Vector3 samplePointOnUnitSphere() {
    while (true) {
      Vector3 p = samplePointInUnitCube();
      double l = p.length();
      if (0.00000001 < l && l <= 1.0) {
        return p.normalize();
      }
    }
  }

  /**
   * Returns a uniformly distributed random point inside the unit sphere.
   */
  public static Vector3 samplePointInUnitSphere() {
    while (true) {
      Vector3 p = samplePointInUnitCube();
      if (p.length() <= 1.0) {
        return p;
      }
    }
  }

  /**
   * Returns a uniformly distributed random point inside the unit cube.
   */
  public static Vector3 samplePointInUnitCube() {
    double x = R.nextDouble() * 2 - 1;
    double y = R.nextDouble() * 2 - 1;
    double z = R.nextDouble() * 2 - 1;

    return new Vector3(x, y, z);
  }

  /**
   * Returns a uniformly distributed random float in the range [0,1[.
   */
  public static double sampleUniform1D() {
    return R.nextDouble();
  }

  /**
   * Samples the 1D standard normal distribution.
   */
  public static double sampleStandardNormal1D() {
    return R.nextGaussian();
  }

  /**
   * Returns a random 3D-Vector distributed along a spherical standard normal distribution.
   */
  public static Vector3 sampleStandardNormal3D() {
    return new Vector3(
        sampleStandardNormal1D(),
        sampleStandardNormal1D(),
        sampleStandardNormal1D());
  }
}
