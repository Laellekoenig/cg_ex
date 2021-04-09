package projection;

import illumination.PointLight;
import utils.Vector3;
import utils.Matrix4;
import utils.Vector4;

public abstract class Projection {

  public static Matrix4 getRotationX(double angle) {

    //TODO: Blatt 3, Aufgabe 1

    return new Matrix4();
  }

  public static Matrix4 getRotationY(double angle) {

    //TODO: Blatt 3, Aufgabe 1

    return new Matrix4();
  }

  public static Matrix4 getRotationZ(double angle) {

    //TODO: Blatt 3, Aufgabe 1

    return new Matrix4();
  }

  public static Matrix4 getTranslation(Vector3 trans) {

    //TODO: Blatt 3, Aufgabe 1

    return new Matrix4();
  }

  public static Matrix4 getScaling(Vector3 scale) {

    //TODO: Blatt 3, Aufgabe 1

    return new Matrix4();
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
