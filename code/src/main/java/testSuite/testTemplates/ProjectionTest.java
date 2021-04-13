package testSuite.testTemplates;


import image.Image;
import image.RGBA;
import renderer.ProjectionTestRenderer;
import shader.ConstantColorShader;

/**
 * Allows to test the projection implementation even though the concepts of Meshes is not yet
 * introduced.
 */
public abstract class ProjectionTest extends Visual3DTest {

  protected ProjectionTestRenderer renderer;

  public ProjectionTest(String gsFileName, String title) {
    super(gsFileName, title);
    this.renderer = new ProjectionTestRenderer(gs.getWidth(), gs.getHeight(),
        new ConstantColorShader());
//		renderer.projection.setView(currentView);
    renderer.setProjectionView(currentView);
  }

  @Override
  public void _draw() {
    this.drawn = renderScene();
    fireDrawEvent();
  }

  public abstract Image<RGBA> renderScene();
}
