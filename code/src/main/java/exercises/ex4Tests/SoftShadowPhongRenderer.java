package exercises.ex4Tests;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import image.RGBA;
import occlusion.ShadowType;

public class SoftShadowPhongRenderer extends PhongShadow {


  protected int pcfMask = 3;

  public SoftShadowPhongRenderer(String gsFileName, String title) {
    super(gsFileName, title);

    matShininess = 4;
    lightColor = new RGBA(0.5, 0.5, 0.5);

    renderer.getLightSource().color = lightColor;
    renderer.getMaterial().shininess = matShininess;
    renderer.enableShadow();
    renderer.setShadowBias(shadowBias);
    renderer.setShadowType(ShadowType.SOFT);
    renderer.setPCFMaskSize(pcfMask);

  }

  @Override
  protected void _draw() {
    renderer.getMaterial().shininess = matShininess;
    renderer.setShadowBias(shadowBias);
    renderer.setPCFMaskSize(pcfMask);
    renderer.clearImg();
    this.drawn = renderer.renderMesh(projection);
    fireDrawEvent();
  }

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

    label = new JLabel("Mask Size: ");
    panel.add(label);

    model = new SpinnerNumberModel(pcfMask, 0, 20, 1);
    spinner = new JSpinner(model);
    spinner.setPreferredSize(dim);

    spinner.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSpinner mySpinner = (JSpinner) (e.getSource());
        pcfMask = ((Integer) (mySpinner.getModel().getValue()));
        draw();
      }
    });
    panel.add(spinner);

    this.widget = panel;
  }


}
