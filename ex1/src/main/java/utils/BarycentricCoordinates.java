package utils;

public class BarycentricCoordinates {

  public final double x, y, z;

  public BarycentricCoordinates(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public double interpolate(double a, double b, double c) {
    return x * a + y * b + z * c;
  }

  public <T extends VectorSpace<T>> T interpolate(T a, T b, T c) {
    return a.times(x).plus(b.times(y)).plus(c.times(z));
  }

  public boolean isInside() {
    return x >= 0 && x <= 1.f &&
        y >= 0 && y <= 1.f &&
        z >= 0 && z <= 1.f;
  }

}


