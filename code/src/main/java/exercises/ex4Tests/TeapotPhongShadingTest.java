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
import renderer.PhongMeshRenderer;
import utils.Vector3;

public class TeapotPhongShadingTest extends TeapotLambertTest {

  protected RGBA lightColor = new RGBA(0.3, 0.3, 0.3);
  protected double matShininess = 2.0;

  protected Component widget;
  protected Dimension dim = new Dimension(60, 30);

  protected PhongMeshRenderer renderer;

  public TeapotPhongShadingTest(String gsFileName, String title) {
    super(gsFileName, title);

    this.rotationX = 0;
    this.rotationY = 0;
    this.rotationZ = 0;
    this.translation = new Vector3(0, 0, -9);
    updateView();

    renderer = new PhongMeshRenderer(gs.getWidth(), gs.getHeight(), meshes, lightColor,
        matShininess);
    tt = new TurnTable(renderer, this, w, h);
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

    JLabel label = new JLabel("Shininess: ");
    panel.add(label);

    SpinnerModel model = new SpinnerNumberModel((int) (matShininess), 1, 30, 1);
    JSpinner spinner = new JSpinner(model);
    spinner.setPreferredSize(dim);

    spinner.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSpinner mySpinner = (JSpinner) (e.getSource());
        matShininess = (Integer) (mySpinner.getModel().getValue());
        renderer.getMaterial().shininess = matShininess;
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
