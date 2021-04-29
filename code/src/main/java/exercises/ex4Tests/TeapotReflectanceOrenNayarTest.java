package exercises.ex4Tests;

import illumination.PointLight;
import image.RGBA;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import mesh.Mesh;
import mesh.MeshReader;
import projection.TurnTable;
import reflectance.OrenNayar;
import renderer.ReflectanceMeshRenderer;
import utils.Vector3;

public class TeapotReflectanceOrenNayarTest extends TeapotLambertTest {

  ReflectanceMeshRenderer renderer;

  private RGBA lightColor = new RGBA(0.7, 0.2, 0.2);
  private RGBA albedo = new RGBA(0.5, 0.5, 0);
  private double sigmaSquarred = 0.1;

  protected Component widget;
  protected Dimension dim = new Dimension(60, 30);

  public TeapotReflectanceOrenNayarTest(String gsFileName, String title) {
    super(gsFileName, title);

    this.rotationX = 0;
    this.rotationY = 0;
    this.rotationZ = 0;
    this.translation = new Vector3(0, 0, -9);
    updateView();

    renderer = new ReflectanceMeshRenderer(gs.getWidth(), gs.getHeight(), meshes);
    addBrdf();
    renderer.lightSources.clear();
    renderer.lightSources.add(new PointLight(new Vector3(-4, 4, 5), lightColor));

    tt = new TurnTable(renderer, this, w, h);
  }

  private void addBrdf() {
    renderer.matBrdf.clear();
    renderer.matBrdf.add(new OrenNayar(albedo, sigmaSquarred));
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

  @Override
  protected void createWidget() {

    JPanel panel = new JPanel();

    addColorWidget("Light Color", lightColor, panel);
    addColorWidget("Light Color", albedo, panel);

    JLabel label = new JLabel("SigmaSquarred: ");
    panel.add(label);
    SpinnerModel model = new SpinnerNumberModel(sigmaSquarred, 0, 30, 0.05);
    JSpinner spinner = new JSpinner(model);
    spinner.setPreferredSize(dim);

    spinner.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSpinner mySpinner = (JSpinner) (e.getSource());
        sigmaSquarred = (Double) (mySpinner.getModel().getValue());
        addBrdf();
        draw();
      }
    });
    panel.add(spinner);

    this.widget = panel;
  }

}
