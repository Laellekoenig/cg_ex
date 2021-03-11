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

    lambdaZ = (-1) * ((a.y - b.y) * x + (b.x - a.x) * y + a.x * b.y - b.x * a.y) / ((a.y - c.y) * b.x + (c.x - a.x)
            * b.y + a.x * c.y - c.x * a.y);

    lambdaY = ((a.y - c.y) * x + (c.x - a.x) * y + a.x * c.y - c.x * a.y) / ((a.y - c.y) * b.x + (c.x - a.x)
            * b.y + a.x * c.y - c.x * a.y);

    lambdaX = 1 - lambdaY - lambdaZ;

    assert(lambdaX + lambdaY + lambdaZ == 0);

    System.out.printf("Should be: (%f, %f), is actually (%f, %f) \n", x, y, lambdaX * a.x + lambdaY * b.x + lambdaZ * c.x
            , lambdaX * a.y + lambdaY * b.y + lambdaZ * c.y);

    return new BarycentricCoordinates(lambdaX, lambdaY, lambdaZ);
  }
}