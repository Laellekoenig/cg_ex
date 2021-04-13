package exercises.ex3Tests;

import image.RGBA;
import mesh.Mesh;
import mesh.MeshReader;
import projection.TurnTable;
import utils.Triplet;
import utils.Vector3;

public class TurnableTeapotTest extends PyramidMeshTest {

  public TurnableTeapotTest(String gsFileName, String title) {
    super(gsFileName, title);
    this.rotationX = Math.PI / 180d * +20;
    this.rotationY = Math.PI / 180d * -35;
    this.translation = new Vector3(-1, 0, -9);
    tt = new TurnTable(renderer, this, w, h);
    updateView();
  }

  @Override
  protected Mesh[] createMeshes() {
    Mesh mesh = MeshReader.loadObj("data/resources/Teapot.obj").get(0);

    //colorize with normals
    mesh.colors = new RGBA[mesh.normals.length];
    for (int i = 0; i < mesh.colors.length; i++) {
      float r = (float) (mesh.normals[i].x / 2.0 + 0.5);
      float g = (float) (mesh.normals[i].y / 2.0 + 0.5);
      float b = (float) (mesh.normals[i].z / 2.0 + 0.5);
      mesh.colors[i] = new RGBA(r, g, b);
    }

    mesh.tci = new Triplet[mesh.tvi.length];
    for (int i = 0; i < mesh.tci.length; i++) {
      mesh.tci[i] = mesh.tvi[i];
    }

    return new Mesh[]{mesh};
  }

}
