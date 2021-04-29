package exercises.ex4Tests;

import utils.Matrix4;

public class PhongFromLight extends PhongTest {

  public PhongFromLight(String gsFileName, String title) {
    super(gsFileName, title);
    tt = null;
  }

  @Override
  protected void _draw() {

    Matrix4 viewMatrix = projection.getViewMatrixOfLightSource(renderer.getLightSource());
    projection.setView(viewMatrix);

    renderer.clearImg();
    this.drawn = renderer.renderMesh(projection);
    fireDrawEvent();
  }

}
