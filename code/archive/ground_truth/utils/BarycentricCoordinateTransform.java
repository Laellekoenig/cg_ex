package utils;

public class BarycentricCoordinateTransform {

  private double det;
  private Vector2 a, b, c;

  public BarycentricCoordinateTransform(Vector2 a, Vector2 b, Vector2 c) {
    this.a = a;
    this.b = b;
    this.c = c;

    det = a.x * (b.y - c.y) + b.x * (c.y - a.y) + c.x * (a.y - b.y);
  }

  public boolean isDegenerate() {
    return det == 0;
  }

  public BarycentricCoordinates getBarycentricCoordinates(double x, double y) {
    //Blatt 1, Aufgabe 3

    double alpha = 0, beta = 0, gamma = 0;

    assert (!isDegenerate());

    //formula from lecture
    gamma = (-1) * ((a.y - b.y) * x   + (b.x - a.x) * y   + a.x * b.y - b.x * a.y) /
                   ((a.y - c.y) * b.x + (c.x - a.x) * b.y + a.x * c.y - c.x * a.y);

    beta =         ((a.y - c.y) * x   + (c.x - a.x) * y   + a.x * c.y - c.x * a.y) /
                   ((a.y - c.y) * b.x + (c.x - a.x) * b.y + a.x * c.y - c.x * a.y);

    alpha = 1 - beta - gamma;

    return new BarycentricCoordinates(alpha, beta, gamma);
  }
}