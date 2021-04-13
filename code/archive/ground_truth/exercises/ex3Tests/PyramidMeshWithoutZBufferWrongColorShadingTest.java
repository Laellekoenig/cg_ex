package exercises.ex3Tests;

import projection.TurnTable;
import rasterization.MeshRasterizerWithoutZBuffer;
import renderer.MeshRendererWrongColorShading;

public class PyramidMeshWithoutZBufferWrongColorShadingTest extends PyramidMeshTest {

  private MeshRendererWrongColorShading renderer;

  public PyramidMeshWithoutZBufferWrongColorShadingTest(String gsFileName, String title) {
    super(gsFileName, title);
    renderer = new MeshRendererWrongColorShading(gs.getWidth(), gs.getHeight(), meshes);
    tt = new TurnTable(renderer, this, w, h);
  }

  @Override
  protected void _draw() {
    renderer.clearImg();
    this.drawn = renderer.renderMesh(projection, new MeshRasterizerWithoutZBuffer(w, h));
    fireDrawEvent();

  }
}
