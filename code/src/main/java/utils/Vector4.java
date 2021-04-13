package utils;

//Immutable
public class Vector4 implements VectorSpace<Vector4> {

  final public double x, y, z, w;

  public Vector4(double x, double y, double z, double w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }

  public Vector4(Vector4 o) {
    this(o.x, o.y, o.z, o.w);
  }

  @Override
  public Vector4 plus(Vector4 o) {
    return new Vector4(x + o.x, y + o.y, z + o.z, w + o.w);
  }

  @Override
  public Vector4 minus(Vector4 o) {
    return new Vector4(x - o.x, y - o.y, z - o.z, w - o.w);
  }

  @Override
  public double dot(Vector4 o) {
    return x * o.x + y * o.y + z * o.z + w * o.w;
  }

  @Override
  public Vector4 times(double b) {
    return new Vector4(x * b, y * b, z * b, w * b);
  }

  @Override
  public Vector4 multElementWise(Vector4 o) {
    return new Vector4(x * o.x, y * o.y, z * o.z, w * o.w);
  }

  @Override
  public Vector4 neg() {
    return new Vector4(-x, -y, -z, -w);
  }

  @Override
  public double length() {
    return Math.sqrt(x * x + y * y + z * z + w * w);
  }

  public String toString() {
    return "(" + x + ", " + y + ", " + z + ", " + w + ")";
  }

}
