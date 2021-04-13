package utils;

// Immutable
public class Vector2 implements VectorSpace<Vector2> {


  final public double x, y;

  public Vector2(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public Vector2(Vector2 d) {
    this(d.x, d.y);
  }

  @Override
  public Vector2 plus(Vector2 o) {
    return new Vector2(x + o.x, y + o.y);
  }

  @Override
  public Vector2 minus(Vector2 o) {
    return new Vector2(x - o.x, y - o.y);
  }

  @Override
  public double dot(Vector2 o) {
    return x * o.x + y * o.y;
  }

  @Override
  public Vector2 multElementWise(Vector2 o) {
    return new Vector2(x * o.x, y * o.y);
  }

  @Override
  public Vector2 times(double factor) {
    return new Vector2(x * factor, y * factor);
  }

  @Override
  public Vector2 neg() {
    return new Vector2(-x, -y);
  }

  /**
   * Norm L_Inf: max{ |v_i| für alle i}
   *
   * @return
   */
  public double normLInf() {
    return Math.max(Math.abs(x), Math.abs(y));
  }

  public double length() {
    return Math.sqrt(x * x + y * y);
  }

  public String toString() {
    return "(" + x + ", " + y + ")";
  }

}
