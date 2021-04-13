package exercises.ex3Tests;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import projection.PinholeProjection;
import image.Image;
import image.RGBA;
import testSuite.testTemplates.InteractiveTest;
import testSuite.testTemplates.ProjectionTest;
import utils.Vector3;

public class PyramidWireTest extends ProjectionTest implements InteractiveTest {

  protected Dimension dim = new Dimension(60, 30);

  public PyramidWireTest(String gsFileName, String title) {
    super(gsFileName, title);
  }

  @Override
  public Image<RGBA> renderScene() {

    RGBA c0 = new RGBA(1f, 0f, 0f);
    RGBA c1 = new RGBA(0f, 1f, 0f);
    RGBA c2 = new RGBA(1f, 0f, 1f);
    RGBA c3 = new RGBA(1f, 1f, 0f);
    RGBA c4 = new RGBA(0f, 1f, 1f);
    RGBA c5 = new RGBA(1f, 0f, 1f);
    RGBA c6 = new RGBA(1f, 0.5f, 1f);
    RGBA c7 = new RGBA(1f, 1f, 0.5f);

    Vector3 v0 = new Vector3(-1.0d, -1.0d, 1.0d);
    Vector3 v1 = new Vector3(1.0d, -1.0d, 1.0d);
    Vector3 v2 = new Vector3(1.0d, -1.0d, -1.0d);
    Vector3 v3 = new Vector3(-1.0d, -1.0d, -1.0d);
    Vector3 v4 = new Vector3(0.0d, 1.0d, 0.0d);

    Vector3[] l0 = {v0, v1};
    Vector3[] l1 = {v1, v2};
    Vector3[] l2 = {v2, v3};
    Vector3[] l3 = {v3, v0};
    Vector3[] l4 = {v0, v4};
    Vector3[] l5 = {v1, v4};
    Vector3[] l6 = {v2, v4};
    Vector3[] l7 = {v3, v4};

    renderer.clearImg();

    renderer.drawPlainLine(l0, c0);
    renderer.drawPlainLine(l1, c1);
    renderer.drawPlainLine(l2, c2);
    renderer.drawPlainLine(l3, c3);
    renderer.drawPlainLine(l4, c4);
    renderer.drawPlainLine(l5, c5);
    renderer.drawPlainLine(l6, c6);
    renderer.drawPlainLine(l7, c7);

    return renderer.getFinalImg();
  }

  @Override
  public PinholeProjection getProjection() {
    return renderer.getProjection();
  }

  protected void updateView() {
    this.buildView(); //stores the new viewModel in currentView
    getProjection().setView(currentView);
  }

  @Override
  public Component getWidget() {
    JPanel panel = new JPanel();

    JLabel label = new JLabel("Rotation X: ");
    panel.add(label);

    SpinnerModel model = new SpinnerNumberModel(toDegrees(rotationX), -360d, 360d, 5);
    JSpinner spinner = new JSpinner(model);
    spinner.setPreferredSize(dim);

    spinner.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSpinner mySpinner = (JSpinner) (e.getSource());
        rotationX = toRadians((Double) (mySpinner.getModel().getValue()));
        updateView();
        draw();
      }
    });

    panel.add(spinner);

    label = new JLabel("Rotation Y: ");
    panel.add(label);

    model = new SpinnerNumberModel(toDegrees(rotationY), -360d, 360d, 5);
    spinner = new JSpinner(model);
    spinner.setPreferredSize(dim);

    spinner.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSpinner mySpinner = (JSpinner) (e.getSource());
        rotationY = toRadians((Double) (mySpinner.getModel().getValue()));
        updateView();
        draw();
      }
    });

    panel.add(spinner);

    label = new JLabel("Rotation Z: ");
    panel.add(label);

    model = new SpinnerNumberModel(toDegrees(rotationZ), -360d, 360d, 5);
    spinner = new JSpinner(model);
    spinner.setPreferredSize(dim);

    spinner.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSpinner mySpinner = (JSpinner) (e.getSource());
        rotationZ = toRadians((Double) (mySpinner.getModel().getValue()));
        updateView();
        draw();
      }
    });

    panel.add(spinner);

    return panel;
  }

  private double toDegrees(double v) {
    return (v / Math.PI * 180) % 360;
  }

  private double toRadians(double v) {
    return Math.PI / 180 * v;
  }


}
