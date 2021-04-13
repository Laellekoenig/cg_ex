package projection;

import utils.Matrix4;
import utils.Vector3;

public interface TurnableTest {

  void draw();

  Matrix4 getView();

  double getRotationX();

  void setRotationX(double x);

  double getRotationY();

  void setRotationY(double y);

  double getRotationZ();

  void setRotationZ(double z);

  Vector3 getTranslation();

  PinholeProjection getProjection();
}
