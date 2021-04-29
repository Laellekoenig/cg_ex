package exercises.ex4Tests;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import occlusion.ShadowType;

public class SoftShadowReflectanceRenderer extends ReflectanceShadow {

  protected int pcfMask = 3;

  public SoftShadowReflectanceRenderer(String gsFileName, String title) {
    super(gsFileName, title);

    renderer.enableShadow();
    renderer.setShadowBias(shadowBias);
    renderer.setPCFMaskSize(pcfMask);
    renderer.setShadowType(ShadowType.SOFT);
  }

  @Override
  protected void _draw() {
    renderer.setShadowBias(shadowBias);
    renderer.setPCFMaskSize(pcfMask);

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

    JLabel label = new JLabel("Shadow Bias: ");
    panel.add(label);

    SpinnerModel model = new SpinnerNumberModel(shadowBias, 0, 20.0, 0.05);
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
