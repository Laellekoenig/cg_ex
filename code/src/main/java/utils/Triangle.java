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

    PinholeProjection projection = new PinholeProjection(10, 10);

    projection.setView(projection.getViewMatrixOfLightSource(new PointLight(normal, null)));

    Vector3 aProjected = projection.project(a);
    Vector3 bProjected = projection.project(b);
    Vector3 cProjected = projection.project(c);
    Vector3 pProjected = projection.project(p);

    aProjected = aProjected.times(aProjected.z);
    bProjected = bProjected.times(bProjected.z);
    cProjected = cProjected.times(cProjected.z);
    pProjected = pProjected.times(pProjected.z);

    BarycentricCoordinateTransform bcTransform = new BarycentricCoordinateTransform(new Vector2(aProjected.x, aProjected.y)
            , new Vector2(bProjected.x, bProjected.y), new Vector2(cProjected.x, cProjected.y));

    return bcTransform.getBarycentricCoordinates(pProjected.x, pProjected.y);
  }


  public Optional<Intersection> intersect(Ray ray, double near) {
    //TODO: Blatt 5, Aufgabe 1



    return Optional.empty();
  }

}
