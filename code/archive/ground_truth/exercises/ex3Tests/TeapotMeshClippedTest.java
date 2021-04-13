package exercises.ex3Tests;

import projection.TurnTable;
import image.RGBA;
import mesh.Mesh;
import mesh.MeshReader;
import utils.Triplet;
import utils.Vector3;

public class TeapotMeshClippedTest extends PyramidMeshClippedTest {


  public TeapotMeshClippedTest(String gsFileName, String title) {
    super(gsFileName, title);
    this.rotationX = 0.2;
    this.rotationY = -1.5;
    this.rotationZ = 0;
    this.translation = new Vector3(0, 0, -4.6);
    updateView();
    tt = new TurnTable(renderer, this, w, h);
  }

  @Override
  protected Mesh[] createMeshes() {

    Mesh mesh = MeshReader.loadObj("data/resources/Teapot.obj").get(0);

    int numTriangles = mesh.tvi.length;

    mesh.colors = new RGBA[numTriangles];
    for (int i = 0; i < mesh.colors.length; i++) {
      float grey = i * 1.0f / mesh.colors.length;
      mesh.colors[i] = new RGBA(grey, grey, grey);
    }

    mesh.tci = new Triplet[numTriangles];
    for (int i = 0; i < mesh.tci.length; i++) {
      mesh.tci[i] = new Triplet(i, i, i);
    }

    return new Mesh[]{mesh};
  }

}
