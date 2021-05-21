package utils;

public class Intersection {

  /**
   * The parameter of the ray at the point of intersection. I.e. the parameter t for which
   * ray.pointAt(t) describes the intersection point.
   */
  public final double t;

  /**
   * The normal at the intersection point.
   */
  public final Vector3 normal;

  public Intersection(double t, Vector3 normal) {
    this.t = t;
    this.normal = normal;
  }
}
