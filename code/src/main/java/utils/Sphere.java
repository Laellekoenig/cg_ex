package utils;

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

    return Optional.empty();
  }

}
