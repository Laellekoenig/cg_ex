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
    camera.set(0, 0, width);
    camera.set(1, 1, height);
    camera.set(0, 2, width / 2.0);    //center of screen
    camera.set(1, 2, height / 2.0);
  }

  // Die Rotationsmatrix als Identitätsmatrix bedeutet, dass nicht rotiert wird, da dabei der Winkel Theta = 0 ist und
  // dass das Auge auf dem Koordinatenursprung bleibt, da der Translationsvektor somit ein Nullvektor ist.
  private void initializeView() {
    view = new Matrix4();
  }

  @Override
  public Vector3 project(Vector3 pt) {
    //TODO: Blatt 3, Aufgabe 1

    // Dieser vierdimensionale Vektor muss auf drei Dimensionen reduziert werden
    // (als Nebenprodukt davon, dass die Multiplikation
    // mit einer 4x4 anstatt einer 3x4 Matrix durchgeführt werden muss.
    // Es gilt x4 = 1, wobei dieser Wert entfernt wird.
    Vector4 homogPt = new Vector4(pt.x, pt.y, pt.z, 1);

    Vector4 extProjection = projection.multiply(homogPt);
    Vector3 result = new Vector3(extProjection.x, extProjection.y, extProjection.z);

    //"inhomogenize" x and y only, bodge to get z buffer working properly and not mess up pyramid wireframe
    //we tried skipping inhomogenizing but the pyramid wireframe would always not render
    return new Vector3((result.x / result.z), (result.y / result.z), result.z);
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
