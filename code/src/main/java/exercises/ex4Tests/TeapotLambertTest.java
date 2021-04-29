package exercises.ex4Tests;

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
import renderer.LambertMeshRenderer;
import testSuite.testTemplates.InteractiveTest;
import testSuite.testTemplates.MeshTest;
import utils.Vector3;

public class TeapotLambertTest extends MeshTest implements InteractiveTest {

  protected RGBA lightColor = new RGBA(0.7, 0.2, 0.2);
  protected double albedo = 0.7;

  protected Component widget;
  protected Dimension dim = new Dimension(60, 30);

  private LambertMeshRenderer renderer;

  public TeapotLambertTest(String gsFileName, String title) {
    super(gsFileName, title);
    this.rotationX = 0;
    this.rotationY = 0;
    this.rotationZ = 0;
    this.translation = new Vector3(0, 0, -9);

    renderer = new LambertMeshRenderer(gs.getWidth(), gs.getHeight(), meshes);
    renderer.setLightColor(lightColor);
    renderer.setAlbedo(albedo);

    updateView();

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
    if (widget == null) {
      createWidget();
    }

    return widget;
  }


  protected void createWidget() {

    JPanel panel = new JPanel();

    addColorWidget("Light Color", lightColor, panel);

    JLabel label = new JLabel("Albedo: ");
    panel.add(label);
    SpinnerModel model = new SpinnerNumberModel(albedo, 0, 30, 0.1);
    JSpinner spinner = new JSpinner(model);
    spinner.setPreferredSize(dim);

    spinner.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSpinner mySpinner = (JSpinner) (e.getSource());
        albedo = (Double) (mySpinner.getModel().getValue());
        renderer.setAlbedo(albedo);
        draw();
      }
    });
    panel.add(spinner);

    this.widget = panel;
  }

  protected void addColorWidget(String title, final RGBA color, JPanel panel) {
    JLabel label = new JLabel(title + " R: ");
    panel.add(label);

    SpinnerModel model = new SpinnerNumberModel(scaleRGB(color.r), 0, 255, 5);
    JSpinner spinner = new JSpinner(model);
    spinner.setPreferredSize(dim);

    spinner.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSpinner mySpinner = (JSpinner) (e.getSource());
        color.r = scaleBack((Integer) (mySpinner.getModel().getValue()));
        draw();
      }
    });
    panel.add(spinner);

    label = new JLabel("G: ");
    panel.add(label);

    model = new SpinnerNumberModel(scaleRGB(color.g), 0, 255, 5);
    spinner = new JSpinner(model);
    spinner.setPreferredSize(dim);

    spinner.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSpinner mySpinner = (JSpinner) (e.getSource());
        color.g = scaleBack((Integer) (mySpinner.getModel().getValue()));
        draw();
      }
    });
    panel.add(spinner);

    label = new JLabel("B: ");
    panel.add(label);

    model = new SpinnerNumberModel(scaleRGB(color.b), 0, 255, 5);
    spinner = new JSpinner(model);
    spinner.setPreferredSize(dim);

    spinner.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSpinner mySpinner = (JSpinner) (e.getSource());
        color.b = scaleBack((Integer) (mySpinner.getModel().getValue()));
        draw();
      }
    });
    panel.add(spinner);

  }

  protected int scaleRGB(double rgbValue) {
    return (int) (rgbValue * 255);
  }

  protected float scaleBack(int rgbValue) {
    return (float) (rgbValue / 255.0);
  }

}
