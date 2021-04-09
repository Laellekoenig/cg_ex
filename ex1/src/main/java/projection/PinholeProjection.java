package projection;

import illumination.PointLight;

import utils.Matrix3;
import utils.Vector3;
import utils.Vector4;
import utils.Matrix4;

public class PinholeProjection extends Projection {

  protected Matrix4 camera, view, projection;

  public PinholeProjection(int width, int height) {
    super(width, height);
    initializeCamera();
    initializeView();
    projection = Matrix4.multiply(camera, view);
  }

  private void initializeCamera() {
    camera = new Matrix4();

    //TODO: Blatt 3, Aufgabe 1
  }

  private void initializeView() {
    view = new Matrix4();
  }

  @Override
  public Vector3 project(Vector3 pt) {

    //TODO: Blatt 3, Aufgabe 1

    return new Vector3(0, 0, 0);
  }

  @Override
  public Matrix4 getMatrix() {
    return projection;
  }

  public Matrix4 getView() {
    return view;
  }

  public void setView(Matrix4 m) {
    view = m;
    projection = Matrix4.multiply(camera, view);
  }

  public void setCamera(Matrix4 m) {
    camera = m;
    projection = Matrix4.multiply(camera, view);
  }

  public Vector4 getEye() {
    Matrix4 ti = new Matrix4(projection);
    ti = ti.inverted();

    Vector4 e = ti.multiply(new Vector4(0, 0, 0, 1));
    e = e.times(1.0 / e.w);

    return e;
  }

  public Matrix4 getViewMatrixOfLightSource(PointLight lightSource) {

    //TODO: Blatt 4, Aufgabe 6 a)
    return new Matrix4();
  }
}
