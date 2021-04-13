package utils;

//Immutable
public class Vector3 implements VectorSpace<Vector3> {

  final public double x, y, z;

  public Vector3(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Vector3(Vector3 o) {
    x = o.x;
    y = o.y;
    z = o.z;
  }

  public double length() {
    return Math.sqrt(x * x + y * y + z * z);
  }

  public double dot(Vector3 d) {
    return x * d.x + y * d.y + z * d.z;
  }

  public Vector3 cross(Vector3 b) {
    return new Vector3(
        y * b.z - z * b.y,
        z * b.x - x * b.z,
        x * b.y - y * b.x
    );
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (!(obj instanceof Vector3)) {
      return false;
    }

    Vector3 d = (Vector3) obj;
    double epsilon = 0.01;
    double xDiff = Math.abs(this.x - d.x);
    double yDiff = Math.abs(this.y - d.y);
    double zDiff = Math.abs(this.z - d.z);
    return xDiff + yDiff + zDiff < epsilon;
  }

  public String toString() {
    return "(" + x + ", " + y + ", " + z + ")";
  }

  @Override
  public Vector3 plus(Vector3 o) {
    return new Vector3(x + o.x, y + o.y, z + o.z);
  }

  @Override
  public Vector3 minus(Vector3 o) {
    return new Vector3(x - o.x, y - o.y, z - o.z);
  }

  @Override
  public Vector3 multElementWise(Vector3 o) {
    return new Vector3(x * o.x, y * o.y, z * o.z);
  }

  @Override
  public Vector3 times(double factor) {
    return new Vector3(x * factor, y * factor, z * factor);
  }

  @Override
  public Vector3 neg() {
    return new Vector3(-x, -y, -z);
  }

  //not inplace
  public Vector3 normalize() {
    return this.times(1 / this.length());
  }

  public static final Vector3 zero = new Vector3(0, 0, 0);
  public static final Vector3 one = new Vector3(1, 1, 1);
}
