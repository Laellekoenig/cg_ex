package exercises.ex3Tests;

import java.awt.Dimension;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import image.RGBA;
import mesh.Mesh;
import projection.TurnTable;
import testSuite.testTemplates.InteractiveTest;
import testSuite.testTemplates.MeshTest;
import utils.Triplet;
import utils.Vector3;

public class PyramidMeshTest extends MeshTest implements InteractiveTest {

  private Dimension dim = new Dimension(60, 30);

  /*** Members of MeshTest
   protected PinholeProjection projection;
   protected TurnTable tt; //optional, instantiate this if you want a turntable in a test
   protected Mesh[] meshes;

   protected MeshRenderer renderer;
   */

  public PyramidMeshTest(String gsFileName, String title) {
    super(gsFileName, title);

    tt = new TurnTable(renderer, this, w, h);
  }

  protected Mesh[] createMeshes() {

    Mesh mesh = new Mesh();
    mesh.vertices = new Vector3[5];
    mesh.vertices[0] = new Vector3(-1.0d, -1.0d, 1.0d);
    mesh.vertices[1] = new Vector3(1.0d, -1.0d, 1.0d);
    mesh.vertices[2] = new Vector3(1.0d, -1.0d, -1.0d);
    mesh.vertices[3] = new Vector3(-1.0d, -1.0d, -1.0d);
    mesh.vertices[4] = new Vector3(0.0d, 1.0d, 0.0d);

    mesh.colors = new RGBA[6];
    mesh.colors[0] = new RGBA(1.0f, 0.0f, 0.0f);
    mesh.colors[1] = new RGBA(0.0f, 1.0f, 0.0f);
    mesh.colors[2] = new RGBA(0.0f, 0.0f, 1.0f);
    mesh.colors[3] = new RGBA(1.0f, 1.0f, 0.0f);
    mesh.colors[4] = new RGBA(0.0f, 1.0f, 1.0f);
    mesh.colors[5] = new RGBA(1.0f, 0.0f, 1.0f);

    mesh.tvi = new Triplet[6];
    mesh.tvi[0] = new Triplet(0, 1, 4);
    mesh.tvi[1] = new Triplet(1, 2, 4);
    mesh.tvi[2] = new Triplet(2, 3, 4);
    mesh.tvi[3] = new Triplet(3, 0, 4);
    mesh.tvi[4] = new Triplet(0, 1, 2);
    mesh.tvi[5] = new Triplet(0, 2, 3);

    mesh.tci = new Triplet[6];

    mesh.tci[0] = new Triplet(0, 1, 4);
    mesh.tci[1] = new Triplet(1, 2, 4);
    mesh.tci[2] = new Triplet(2, 3, 4);
    mesh.tci[3] = new Triplet(3, 0, 4);
    mesh.tci[4] = new Triplet(0, 1, 2);
    mesh.tci[5] = new Triplet(0, 2, 3);

    return new Mesh[]{mesh};
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
    return v / Math.PI * 180;
  }

  private double toRadians(double v) {
    return Math.PI / 180 * v;
  }

}
