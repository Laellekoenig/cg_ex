package exercises.ex4Tests;

import java.awt.Component;

import javax.swing.JPanel;

import illumination.PointLight;
import image.RGBA;
import mesh.Mesh;
import mesh.MeshReader;
import projection.TurnTable;
import reflectance.LambertBrdf;
import renderer.ReflectanceMeshRenderer;
import utils.Vector3;

public class TeapotReflectanceLambert extends TeapotLambertTest {

  protected RGBA lightColor = new RGBA(0.7, 0.2, 0.2);
  private RGBA albedo = new RGBA(0.7, 0.7, 0.7);

  protected ReflectanceMeshRenderer renderer;

  public TeapotReflectanceLambert(String gsFileName, String title, boolean secondLight) {
    super(gsFileName, title);

    this.rotationX = 0;
    this.rotationY = 0;
    this.rotationZ = 0;
    this.translation = new Vector3(0, 0, -9);
    updateView();

    renderer = new ReflectanceMeshRenderer(gs.getWidth(), gs.getHeight(), meshes);
    renderer.matBrdf.clear();
    renderer.matBrdf.add(new LambertBrdf(albedo));
    renderer.lightSources.clear();
    renderer.lightSources.add(new PointLight(new Vector3(-2, 2, 4), lightColor));
    if (secondLight) {
      renderer.lightSources.add(new PointLight(new Vector3(3, 3, 2), lightColor));
    }

    tt = new TurnTable(renderer, this, w, h);
  }

  @Override
  protected void _draw() {
    renderer.clearImg();
    this.drawn = renderer.renderMesh(projection);
    fireDrawEvent();
  }

  @Override
  protected Mesh[] createMeshes() {
    Mesh mesh = MeshReader.loadObj("data/resources/Teapot.obj").get(0);
    return new Mesh[]{mesh};
  }

  @Override
  public Component getWidget() {
    createWidget();
    return widget;
  }

  protected void createWidget() {
    JPanel panel = new JPanel();

    addColorWidget("Light Color", lightColor, panel);
    addColorWidget("Albedo", albedo, panel);

    this.widget = panel;
  }

}
