package exercises.ex4Tests;

import javax.swing.JPanel;

import reflectance.LambertBrdf;
import renderer.ReflectanceMeshRenderer;
import utils.Matrix4;

public class ReflectanceLambertFromLight extends ReflectanceLambert {

  public ReflectanceLambertFromLight(String gsFileName, String title) {
    super(gsFileName, title);
    renderer = new ReflectanceMeshRenderer(gs.getWidth(), gs.getHeight(), meshes);
  }

  @Override
  protected void _draw() {

    Matrix4 viewMatrix = projection.getViewMatrixOfLightSource(renderer.lightSources.get(0));
    projection.setView(viewMatrix);

    renderer.clearImg();
    this.drawn = renderer.renderMesh(projection);
    fireDrawEvent();
  }

  @Override
  protected void createWidget() {
    JPanel panel = new JPanel();

    addColorWidget("Light Color", renderer.lightSources.get(0).color, panel);
    LambertBrdf b = (LambertBrdf) renderer.matBrdf.get(0);
    addColorWidget("Albedo", b.albedo, panel);

    this.widget = panel;
  }

}
