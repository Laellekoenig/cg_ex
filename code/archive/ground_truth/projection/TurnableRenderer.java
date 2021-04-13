package projection;

import utils.Matrix4;

public interface TurnableRenderer {

  void setProjectionView(Matrix4 currentView);

  void rotateLights(Matrix4 rotation);

}
