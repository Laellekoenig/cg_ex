package exercises.ex4Tests;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import testSuite.testTemplates.InteractiveTest;


public class PhongShadow extends PhongTest implements InteractiveTest {

  protected double shadowBias = 0.05;

  public PhongShadow(String gsFileName, String title) {
    super(gsFileName, title);

    renderer.enableShadow();
    renderer.setShadowBias(shadowBias);
  }

  @Override
  protected void _draw() {
    renderer.clearImg();
    renderer.setShadowBias(shadowBias);
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
        draw();
      }
    });
    panel.add(spinner);

    label = new JLabel("Shadow Bias: ");
    panel.add(label);

    model = new SpinnerNumberModel(0.0, -1000.0, 100.0, 0.05);
    spinner = new JSpinner(model);
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
