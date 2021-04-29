package exercises.ex4Tests;


import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import illumination.PointLight;
import image.RGBA;
import mesh.Mesh;
import mesh.MeshReader;
import projection.TurnTable;
import reflectance.CookTorrance;
import reflectance.OrenNayar;
import renderer.ReflectanceMeshRenderer;
import utils.Vector3;

public class TeapotCookTorranceOrenNayar extends TeapotLambertTest {

  private ReflectanceMeshRenderer renderer;

  protected RGBA lightColor = new RGBA(0.7, 0.2, 0.2);
  protected RGBA albedo = new RGBA(0.5, 0.5, 0);

  private double m = 0.4;
  private double r0 = 0.2;

  private double sigmaSquarred = 0.1;

  public TeapotCookTorranceOrenNayar(String gsFileName, String title) {
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
    renderer.matBrdf.add(new CookTorrance(albedo, m, r0));
  }

  @Override
  protected void _draw() {
    renderer.clearImg();
    this.drawn = renderer.renderMesh(projection);
    fireDrawEvent();
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
    addColorWidget("Albedo", albedo, panel);

    JLabel label = new JLabel("Rauheit m: ");
    panel.add(label);
    SpinnerModel model = new SpinnerNumberModel(m, 0.05, 30, 0.05);
    JSpinner spinner = new JSpinner(model);
    spinner.setPreferredSize(dim);

    spinner.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSpinner mySpinner = (JSpinner) (e.getSource());
        m = (Double) (mySpinner.getModel().getValue());
        addBrdf();
        draw();
      }
    });
    panel.add(spinner);

    label = new JLabel("Fresnel r0: ");
    panel.add(label);
    model = new SpinnerNumberModel(r0, 0, 30, 0.1);
    spinner = new JSpinner(model);
    spinner.setPreferredSize(dim);

    spinner.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSpinner mySpinner = (JSpinner) (e.getSource());
        r0 = (Double) (mySpinner.getModel().getValue());
        addBrdf();
        draw();
      }
    });
    panel.add(spinner);

    label = new JLabel("SigmaSquarred: ");
    panel.add(label);
    model = new SpinnerNumberModel(sigmaSquarred, 0, 30, 0.05);
    spinner = new JSpinner(model);
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

  @Override
  protected Mesh[] createMeshes() {
    Mesh mesh = MeshReader.loadObj("data/resources/Teapot.obj").get(0);
    return new Mesh[]{mesh};
  }
}
