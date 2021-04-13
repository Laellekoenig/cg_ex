package exercises.ex3Tests;

import image.RGBA;
import mesh.Mesh;
import projection.TurnTable;
import utils.Triplet;
import utils.Vector3;

public class PyramidMeshClippedTest extends PyramidMeshTest {

  public PyramidMeshClippedTest(String gsFileName, String title) {
    super(gsFileName, title);
    this.rotationY = -0.6108652;
    this.rotationX = 0.009075;
    this.translation = new Vector3(0, 0, -2.3);
    updateView();

    tt = new TurnTable(renderer, this, w, h);
  }

  @Override
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

}
