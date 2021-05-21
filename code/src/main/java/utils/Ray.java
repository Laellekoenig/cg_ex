package utils;

public final class Ray {

  public final Vector3 origin;
  public final Vector3 direction; // Wird im Konstruktor normalisiert!

  public Ray(Vector3 origin, Vector3 direction) {
    this.origin = origin;
    this.direction = direction.normalize();
  }

  public Vector3 pointAt(double t) {
    return origin.plus(direction.times(t));
  }

  public static Ray fromEndPoints(Vector3 start, Vector3 end) {
    return new Ray(start, end.minus(start));
  }
}
