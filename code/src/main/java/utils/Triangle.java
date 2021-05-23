package utils;

import illumination.PointLight;
import projection.PinholeProjection;

import java.util.Optional;

public class Triangle implements Intersectable {

  // Points of the Triangle
  public final Vector3 a;
  public final Vector3 b;
  public final Vector3 c;

  private Vector3 normal;


  public Triangle(Vector3 a, Vector3 b, Vector3 c) {
    this.a = a;
    this.b = b;
    this.c = c;

    //TODO: Blatt 5, Aufgabe 1

    // Two sides of the triangle.
    Vector3 A = b.minus(a);
    Vector3 B = c.minus(a);

    // The cross product of two sides of the triangle is perpendicular to the plane spanned by the vectors representing
    // those two sides.
    // => The vector perpendicular to that plane is also perpendicular to the triangle and is thereby its normal.
    normal = A.cross(B).normalize();
  }

  /**
   * Calculates the barycentric coordinates of a point p when projected onto the plane of the
   * triangle.
   *
   * @param p A point in R^3
   * @return The barycentric coordinates of p projected onto the triangle's plane.
   */
  public BarycentricCoordinates barycentricCoords(Vector3 p) {
    //TODO: Blatt 5, Aufgabe 1

    // In order to work with barycentric coordinates we need to drop an axis in order to transform the triangle into
    // 2D. The axis to be dropped is determined by the dominant direction of the normal, in order to maximise the area
    // of the 2D triangle.

    if (Math.abs(normal.x) >= Math.abs(normal.y) && Math.abs(normal.x) >= Math.abs(normal.z)) {
      // The x-axis is the dominant axis of the normal => we drop the x-axis.
      BarycentricCoordinateTransform bcTransform = new BarycentricCoordinateTransform(new Vector2(a.y, a.z)
              , new Vector2(b.y, b.z), new Vector2(c.y, c.z));

      return bcTransform.getBarycentricCoordinates(p.y, p.z);
    } else if (Math.abs(normal.y) >= Math.abs(normal.z)) {
      // => We drop the y-axis
      BarycentricCoordinateTransform bcTransform = new BarycentricCoordinateTransform(new Vector2(a.x, a.z)
              , new Vector2(b.x, b.z), new Vector2(c.x, c.z));

      return bcTransform.getBarycentricCoordinates(p.x, p.z);
    } else {
      // The only remaining option is that the z-axis is the dominant direction of the normal => we drop the z-axis
      BarycentricCoordinateTransform bcTransform = new BarycentricCoordinateTransform(new Vector2(a.x, a.y)
              , new Vector2(b.x, b.y), new Vector2(c.x, c.y));

      return bcTransform.getBarycentricCoordinates(p.x, p.y);
    }
  }


  public Optional<Intersection> intersect(Ray ray, double near) {

    // The dot product of the ray's origin in relation to a point in the triangle and the triangle's normal divided by
    // the dot product of the ray's direction vector and the normal give us negative t. As with the sphere, we can use
    // the ray's parametric form (ray(t) = ray.origin + ray.direction * t) to find the point of intersection.
    Vector3 pointInTriangleToRayOrigin = ray.origin.minus(a);
    double interDot1 = pointInTriangleToRayOrigin.dot(normal);
    double interDot2 = ray.direction.dot(normal);
    double t = (-1) * interDot1 / interDot2;
    Vector3 intersection = ray.origin.plus(ray.direction.times(t));

    boolean intersects = barycentricCoords(intersection).isInside();

    // Same as with the sphere: If the point intersecting the plane spanned by two side vectors intersects the triangle
    // and the length of the vector t is larger or equal to the minimum length, then the intersection is returned, otherwise
    // simply an empty container.
    if (intersects && t >= near) {
      return Optional.of(new Intersection(t, normal));
    }

    return Optional.empty();
    //return Optional.of(new Intersection(t, normal));
  }

}
