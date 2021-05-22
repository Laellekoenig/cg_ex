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

    // Here we're using a formula found on https://math.stackexchange.com/questions/1939423/calculate-if-vector-intersects-sphere (22.05.21)
    // in order to calculate if the vector representing the ray intersects the sphere.
    Vector3 Q = ray.origin.minus(center);
    double a = ray.direction.dot(ray.direction);
    double b = 2 * ray.direction.dot(Q);
    double c = Q.dot(Q) - radius * radius;
    double d = b * b - 4 * a * c;

    // If d < 0 then the intersection points are complex and thereby the ray doesn't intersect the sphere. If d == 0
    // then the ray is a tangent to the sphere.
    boolean intersects = d > 0;


    // If there is an intersection, then the two intersection distances are saved in t1 and t2.
    if (intersects) {
      double t1 = (-b + Math.sqrt(d)) / 2 * a;
      double t2 = (-b - Math.sqrt(d)) / 2 * a;

      // The intersection distances need to be larger or equal to the minimal distance, otherwise the intersection is
      // behind the camera and thereby irrelevant, so we test the two intersection points for that condition. Additionally
      // we find the closer of the two distances and save that into the variable t, which is used for the return value.
      double t;

      if(t1 <= t2 && t1 >= near) {
        t = t1;
      } else if (t2 < t1 && t2 >= near) {
        t = t2;
      } else {
        return Optional.empty();
      }

      // The parametric equation of the ray (ray(t) = ray.origin + t * ray.direction) is used to return the length of the
      // ray by setting t to the calculated value. This only happens if an intersection was detected, otherwise an empty
      // Optional object is returned.
      return Optional.of(new Intersection(t, ray.origin.plus(ray.direction.times(1))));
    }
    return Optional.empty();
  }
}
