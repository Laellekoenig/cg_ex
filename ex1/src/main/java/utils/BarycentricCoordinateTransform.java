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
    double lambdaX = 0, lambdaY = 0, lambdaZ = 0;

    assert (!isDegenerate());

    //TODO: Blatt 1, Aufgabe 3

    return new BarycentricCoordinates(lambdaX, lambdaY, lambdaZ);
  }
}