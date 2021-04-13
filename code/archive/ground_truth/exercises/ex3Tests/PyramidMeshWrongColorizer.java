package exercises.ex3Tests;

import projection.TurnTable;
import rasterization.MeshRasterizer;
import renderer.MeshRendererWrongColorShading;

public class PyramidMeshWrongColorizer extends PyramidMeshTest {

  private MeshRendererWrongColorShading renderer;

  public PyramidMeshWrongColorizer(String gsFileName, String title) {
    super(gsFileName, title);
    renderer = new MeshRendererWrongColorShading(gs.getWidth(), gs.getHeight(), meshes);
    tt = new TurnTable(renderer, this, w, h);
  }

  @Override
  protected void _draw() {
    renderer.clearImg();
    this.drawn = renderer.renderMesh(projection, new MeshRasterizer(w, h));
    fireDrawEvent();
  }
}
