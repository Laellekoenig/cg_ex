package utils;

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

    return new BarycentricCoordinates(0.0, 0.0, 1.0);
  }


  public Optional<Intersection> intersect(Ray ray, double near) {
    //TODO: Blatt 5, Aufgabe 1

    return Optional.empty();
  }

}
