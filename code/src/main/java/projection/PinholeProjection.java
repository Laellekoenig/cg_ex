package projection;

import illumination.PointLight;
import utils.Matrix3;
import utils.Vector3;
import utils.Vector4;
import utils.Matrix4;

public class PinholeProjection extends Projection {

  // Transforms an object in world space such that it's located correctly in relation to the camera.
  protected Matrix4 camera;
  // Transforms 3D world-space coordinates into 2D view-space coordinates.
  protected Matrix4 view;
  protected Matrix4 projection;

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
    // The test failIfLightSourceOnXAxisTest still fails, but I don't quite get what that test tests or requires...

    Vector3 l = lightSource.position;

    Vector3 z = l.times(1/l.length());

    Vector3 x0 = new Vector3(1, 0, 0);
    Vector3 y = z.cross(x0).times(1 / z.cross(x0).length());

    Vector3 x = y.cross(z).times(1 / y.cross(z).length());

    Matrix4 M = new Matrix4();

    M.set(0, 0, x.x);
    M.set(0, 1, x.y);
    M.set(0, 2, x.z);
    M.set(1, 0, y.x);
    M.set(1, 1, y.y);
    M.set(1, 2, y.z);
    M.set(2, 0, z.x);
    M.set(2, 1, z.y);
    M.set(2, 2, z.z);

    // The new coordinate system faces the same direction as the light source (due to the rotation) and the z-axis points in that direction.
    // Therefore the required translation involves moving back along the z-axis as far as the distance of the light source.
    M.set(2, 3, - l.length());

    return M;
  }
}
