package testSuite.testTemplates;

import projection.Projection;
import projection.TurnableTest;
import utils.Matrix4;
import utils.Vector3;

/**
 * Test Template used for all tests that project stuff. Defines a default view matrix.
 *
 * @author Manuel Kaufmann
 */
public abstract class Visual3DTest extends VisualTest implements TurnableTest {

  public Matrix4 currentView;
  public double rotationX, rotationY, rotationZ;
  public Vector3 translation;

  public Visual3DTest(String gsFileName, String title) {
    super(gsFileName, title);
    this.rotationX = Math.PI / 180d * -20;
    this.rotationY = Math.PI / 180d * -35;
    this.rotationZ = 0;
    this.translation = new Vector3(0, 0, -5);
    buildView();
  }


  /**
   * Creates a view matrix taking into account the specified rotationY, rotationX, rotationZ and a
   * zTranslation.
   */
  protected void buildView() {
    Matrix4 rotY = Projection.getRotationY(rotationY);
    Matrix4 rotX = Projection.getRotationX(rotationX);
    Matrix4 rotZ = Projection.getRotationZ(rotationZ);

    currentView = Matrix4.multiply(Matrix4.multiply(rotX, rotY), rotZ);
    currentView.set(0, 3, translation.x);
    currentView.set(1, 3, translation.y);
    currentView.set(2, 3, translation.z);
  }

  @Override
  public Matrix4 getView() {
    return currentView;
  }

  @Override
  public double getRotationX() {
    return rotationX;
  }

  @Override
  public void setRotationX(double d) {
    this.rotationX = d;
  }

  @Override
  public double getRotationY() {
    return rotationY;
  }

  @Override
  public void setRotationY(double d) {
    this.rotationY = d;
  }

  @Override
  public double getRotationZ() {
    return rotationZ;
  }

  @Override
  public void setRotationZ(double d) {
    this.rotationZ = d;
  }

  @Override
  public Vector3 getTranslation() {
    return translation;
  }

}
