package ex3;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import projection.PinholeProjection;
import projection.Projection;
import utils.Matrix4;
import utils.Vector3;
import utils.Vector4;

public class PinholeProjectionTest {

  private final double delta = .01;

  @Test
  public void invertedRotationTest() {
    final Matrix4 identity = new Matrix4();
    final double angle = Math.PI / 2;
    final Matrix4 rotX = Projection.getRotationX(angle);
    final Matrix4 rotY = Projection.getRotationY(angle);
    final Matrix4 rotZ = Projection.getRotationZ(angle);

    Matrix4 m1 = Matrix4.multiply(rotX, rotX.inverted());
    testMatrix(null, identity, m1);

    Matrix4 m2 = Matrix4.multiply(rotY, rotY.inverted());
    testMatrix(null, identity, m2);

    Matrix4 m3 = Matrix4.multiply(rotZ, rotZ.inverted());
    testMatrix(null, identity, m3);
  }

  @Test
  public void fullRotationTest() {
    final Matrix4 identity = new Matrix4();
    final double angle = Math.PI / 2;
    final Matrix4 rotX = Projection.getRotationX(angle);
    final Matrix4 rotY = Projection.getRotationY(angle);
    final Matrix4 rotZ = Projection.getRotationZ(angle);

    Matrix4 m4 = new Matrix4(identity);
    Matrix4 m5 = new Matrix4(identity);
    Matrix4 m6 = new Matrix4(identity);
    for (int i = 0; i < 4; ++i) {
      m4 = Matrix4.multiply(m4, rotX);
      m5 = Matrix4.multiply(m5, rotY);
      m6 = Matrix4.multiply(m6, rotZ);
    }
    testMatrix("Rotating 90° in X-direction 4 times should return the identity matrix.", identity,
        m4);
    testMatrix("Rotating 90° in Y-direction 4 times should return the identity matrix.", identity,
        m5);
    testMatrix("Rotating 90° in Z-direction 4 times should return the identity matrix.", identity,
        m6);
  }

  private void testMatrix(String message, Matrix4 expected, Matrix4 actual) {
    for (int x = 0; x < 4; ++x) {
      for (int y = 0; y < 4; ++y) {
        if (message == null) {
          assertEquals(expected.get(x, y), actual.get(x, y), delta);
        } else {
          assertEquals(message + " (at row=" + x + ", col=" + y + ")", expected.get(x, y),
              actual.get(x, y), delta);
        }
      }
    }
  }
}
