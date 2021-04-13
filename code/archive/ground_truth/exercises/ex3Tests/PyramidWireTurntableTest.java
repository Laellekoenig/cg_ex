package exercises.ex3Tests;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;

import image.RGBA;
import mesh.Mesh;
import projection.TurnTable;
import renderer.ProjectionTestRenderer;
import shader.ConstantColorShader;
import testSuite.testTemplates.InteractiveTest;
import testSuite.testTemplates.MeshTest;
import utils.Vector3;

public class PyramidWireTurntableTest extends MeshTest implements InteractiveTest {

  protected ProjectionTestRenderer projectedRenderer;

  public PyramidWireTurntableTest(String gsFileName, String title) {
    super(gsFileName, title);

    projectedRenderer = new ProjectionTestRenderer(gs.getWidth(), gs.getHeight(),
        new ConstantColorShader());
    projection = projectedRenderer.getProjection();

    tt = new TurnTable(projectedRenderer, this, w, h);
  }

  @Override
  public Component getWidget() {
    JPanel dummy = new JPanel();
    dummy.setPreferredSize(new Dimension(60, 40));
    return dummy;
  }

  @Override
  public void _draw() {

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

    projectedRenderer.clearImg();

    projectedRenderer.drawPlainLine(l0, c0);
    projectedRenderer.drawPlainLine(l1, c1);
    projectedRenderer.drawPlainLine(l2, c2);
    projectedRenderer.drawPlainLine(l3, c3);
    projectedRenderer.drawPlainLine(l4, c4);
    projectedRenderer.drawPlainLine(l5, c5);
    projectedRenderer.drawPlainLine(l6, c6);
    projectedRenderer.drawPlainLine(l7, c7);

    this.drawn = projectedRenderer.getFinalImg();
    fireDrawEvent();

  }

  @Override
  protected Mesh[] createMeshes() {
    return null;
  }

}
