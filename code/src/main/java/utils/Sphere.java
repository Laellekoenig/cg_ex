package utils;

import javax.swing.text.html.Option;
import java.util.Optional;

public class Sphere implements Intersectable {

  public final Vector3 center;
  public final double radius;
  private final double radius2;

  public Sphere(Vector3 center, double radius) {
    this.center = center;
    this.radius = radius;
    this.radius2 = radius * radius;
  }

  public Optional<Intersection> intersect(Ray ray, double near) {
    //TODO: Blatt 5, Aufgabe 1

    Vector3 Q = ray.origin.minus(center);
    double a = ray.direction.dot(ray.direction);
    double b = 2 * ray.direction.dot(Q);
    double c = Q.dot(Q) - radius * radius;
    double d = b * b - 4 * a * c;

    boolean intersects = d > 0;

    if (intersects) {
      double t1 = (-b + Math.sqrt(d)) / 2 * a;
      double t2 = (-b - Math.sqrt(d)) / 2 * a;

      double t;

      if(t1 <= t2 && t1 >= near) {
        t = t1;
      } else if (t2 < t1 && t2 >= near) {
        t = t2;
      } else {
        return Optional.empty();
      }

      return Optional.of(new Intersection(t, ray.origin.plus(ray.direction.times(1))));
    }
    return Optional.empty();
  }
}
