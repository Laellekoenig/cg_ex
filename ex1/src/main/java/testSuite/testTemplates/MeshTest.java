package testSuite.testTemplates;

import mesh.Mesh;
import projection.PinholeProjection;
import projection.TurnTable;
import projection.TurnableTest;
import renderer.MeshRenderer;

/**
 * Test template used when Meshes are involved (most of the time).
 */
public abstract class MeshTest extends Visual3DTest implements TurnableTest {

  protected PinholeProjection projection;
  protected Mesh[] meshes;
  protected MeshRenderer renderer;

  protected TurnTable tt; //optional, instantiate this if you want a turntable in a test

  public MeshTest(String gsFileName, String title, MeshRenderer renderer) {
    super(gsFileName, title);

    projection = new PinholeProjection(gs.getWidth(), gs.getHeight());
    updateView();
    meshes = createMeshes();

    this.renderer = renderer;
  }

  public MeshTest(String gsFileName, String title) {
    this(gsFileName, title, null);
    renderer = new MeshRenderer(gs.getWidth(), gs.getHeight(), meshes);
  }


  @Override
  protected void _draw() {
    renderer.clearImg();
    this.drawn = renderer.renderMesh(projection);
    fireDrawEvent();
  }

  @Override
  public PinholeProjection getProjection() {
    return projection;
  }

  /**
   * Rebuilds the view matrix according to the current values of rotationX, rotationY, rotationZ and
   * zTranslation of the super class and updates the projection respectively.
   */
  protected void updateView() {
    buildView();
    projection.setView(currentView);
  }

  public boolean hasTurnTable() {
    return tt != null;
  }

  public TurnTable getTurnTable() {
    return tt;
  }

  protected abstract Mesh[] createMeshes();


}
