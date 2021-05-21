package ex5;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Optional;
import org.junit.Test;
import utils.Intersection;
import utils.Ray;
import utils.Sphere;
import utils.Triangle;
import utils.Vector3;

public class IntersectionTest {

  private double near = 0;
  private Vector3 xDir = new Vector3(1, 0, 0);
  double delta = .01;

  @Test
  public void sphereIntersectionTest() {
    Vector3 center = new Vector3(3, 0, 0);
    double radius = 2;
    Sphere sphere = new Sphere(center, radius);

    Vector3 origin = new Vector3(0, 0, 0);
    Ray ray = new Ray(origin, xDir);
    Optional<Intersection> intersection = sphere.intersect(ray, near);
    assertTrue("Ray should intersect with sphere when heading straight at it.",
        intersection.isPresent());
    assertEquals("Maybe wrong intersection point chosen.", 1, intersection.get().t, delta);

    ray = new Ray(origin, new Vector3(3, 2, 0));
    intersection = sphere.intersect(ray, near);
    assertTrue("Ray should intersect", intersection.isPresent());
    assertEquals("Maybe wrong intersection point chosen.", 1.387, intersection.get().t, delta);

    ray = new Ray(new Vector3(0, 3, 0), xDir);
    intersection = sphere.intersect(ray, near);
    assertFalse("Ray misses sphere and should thus not intersect.", intersection.isPresent());

    // tangent
    ray = new Ray(new Vector3(0, 2, 0), xDir);
    intersection = sphere.intersect(ray, near);
    assertFalse("Ray is tangential to sphere and should not intersect.", intersection.isPresent());
  }

  @Test
  public void triangleIntersectionTest() {
    Triangle triangle = new Triangle(new Vector3(3, 0, 0), new Vector3(3, 0, 3),
        new Vector3(3, 3, 3));

    Ray rayHit = new Ray(new Vector3(0, 1, 2), xDir);
    Optional<Intersection> hit = triangle.intersect(rayHit, near);
    assertTrue("Ray should intersect with triangle.", hit.isPresent());
    assertEquals("Distance to intersection is wrong.", 3, hit.get().t, delta);

    Ray rayMiss = new Ray(new Vector3(0, 2, 1), xDir);
    Optional<Intersection> miss = triangle.intersect(rayMiss, near);
    assertFalse("Ray misses triangle and should thus not intersect.", miss.isPresent());
  }
}
