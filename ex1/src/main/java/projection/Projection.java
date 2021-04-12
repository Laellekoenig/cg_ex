package projection;

import illumination.PointLight;
import utils.Vector3;
import utils.Matrix4;
import utils.Vector4;

public abstract class Projection {

  public static Matrix4 getRotationX(double angle) {

    //TODO: Blatt 3, Aufgabe 1

    double s = Math.cos(angle / 2);
    double x = Math.sin(angle / 2);

    Matrix4 matrix = new Matrix4();
    matrix.set(1, 1, 1 - (2 * Math.pow(x, 2)));
    matrix.set(1, 2, 2 * ((-1) * s * x));
    matrix.set(2, 1, 2 * s * x);
    matrix.set(2, 2, 1 - 2 * Math.pow(x, 2));


    return matrix;
  }

  public static Matrix4 getRotationY(double angle) {

    //TODO: Blatt 3, Aufgabe 1

    Matrix4 matrix = new Matrix4();

    double s = Math.cos(angle / 2);
    double y = Math.sin(angle / 2);

    matrix.set(0, 0, 1 - (2 * Math.pow(y, 2)));
    matrix.set(0, 2, 2 * s * y);
    matrix.set(2, 0, 2 * (-1) * s * y);
    matrix.set(2, 2, 1 - (2 * Math.pow(y, 2)));

    return matrix;
  }

  public static Matrix4 getRotationZ(double angle) {

    //TODO: Blatt 3, Aufgabe 1

    Matrix4 matrix = new Matrix4();

    double s = Math.cos(angle / 2);
    double z = Math.sin(angle / 2);

    matrix.set(0, 0, 1 - (2 * Math.pow(z, 2)));
    matrix.set(0, 1, 2 * (-1) * s * z);
    matrix.set(1, 0, 2 * s * z);
    matrix.set(1, 1, 1 - (2 * Math.pow(z, 2)));

    return matrix;
  }

  public static Matrix4 getTranslation(Vector3 trans) {

    //TODO: Blatt 3, Aufgabe 1

    Matrix4 matrix = new Matrix4();

    matrix.set(0, 3, trans.x);
    matrix.set(1, 3, trans.y);
    matrix.set(2, 3, trans.z);

    return matrix;
  }

  public static Matrix4 getScaling(Vector3 scale) {

    //TODO: Blatt 3, Aufgabe 1

    // Not covered in lecture, this is just a guessed implementation...

    Matrix4 matrix = new Matrix4();

    matrix.set(0, 0, scale.x);
    matrix.set(1, 1, scale.y);
    matrix.set(2, 2, scale.z);

    return matrix;
  }

  protected int width, height;

  public Projection(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public abstract Vector3 project(Vector3 pt);

  public abstract Vector4 getEye();

  public abstract Matrix4 getViewMatrixOfLightSource(PointLight pointLight);

  public abstract Matrix4 getMatrix();
}
