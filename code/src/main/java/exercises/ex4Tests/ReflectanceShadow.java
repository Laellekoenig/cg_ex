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
import projection.TurnTable;
import reflectance.LambertBrdf;
import renderer.ReflectanceMeshRenderer;
import utils.Vector3;

public class ReflectanceShadow extends PhongShadow {

  protected ReflectanceMeshRenderer renderer;

  private RGBA albedo = new RGBA(0.7, 0.7, 0.7);
  private RGBA lightColor = new RGBA(0.7, 0.2, 0.2);

  public ReflectanceShadow(String gsFileName, String title) {
    super(gsFileName, title);

    renderer = new ReflectanceMeshRenderer(gs.getWidth(), gs.getHeight(), meshes);
    renderer.enableShadow();

    setBias();

    addBrdf();

    renderer.lightSources.clear();
    renderer.lightSources.add(new PointLight(new Vector3(-4, 4, 5), lightColor));

    tt = new TurnTable(renderer, this, w, h);
  }

  private void setBias() {
    renderer.setShadowBias(shadowBias);
  }

  private void addBrdf() {
    renderer.matBrdf.clear();
    renderer.matBrdf.add(new LambertBrdf(albedo));
  }

  @Override
  protected void _draw() {
    addBrdf();
    setBias();
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

    JLabel label = new JLabel("Shadow Bias: ");
    panel.add(label);

    SpinnerModel model = new SpinnerNumberModel(0.0, -1000.0, 100.0, 0.05);
    JSpinner spinner = new JSpinner(model);
    spinner.setPreferredSize(dim);
    spinner.getModel().setValue(shadowBias);

    spinner.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSpinner mySpinner = (JSpinner) (e.getSource());
        shadowBias = ((Double) (mySpinner.getModel().getValue()));
        draw();
      }
    });
    panel.add(spinner);

    this.widget = panel;
  }

}
