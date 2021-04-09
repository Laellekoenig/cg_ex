package testUtils;

import mesh.Mesh;
import renderer.MeshRenderer;
import testSuite.testTemplates.MeshTest;
import utils.Triplet;
import utils.Vector3;

public class UnitCubeTurnableTest extends MeshTest {

  public UnitCubeTurnableTest(String gsFileName, String title, MeshRenderer renderer) {
    super(gsFileName, title, renderer);
  }

  public Mesh[] meshes;

  @Override
  protected Mesh[] createMeshes() {
    Mesh cube = new Mesh();

    cube.vertices = new Vector3[8];
    cube.vertices[0] = new Vector3(0, 0, 0);
    cube.vertices[1] = new Vector3(1, 0, 0);
    cube.vertices[2] = new Vector3(0, 1, 0);
    cube.vertices[3] = new Vector3(0, 0, 1);
    cube.vertices[4] = new Vector3(1, 1, 0);
    cube.vertices[5] = new Vector3(1, 0, 1);
    cube.vertices[6] = new Vector3(0, 1, 1);
    cube.vertices[7] = new Vector3(1, 1, 1);

    double l = Math.sqrt(3);
    cube.normals = new Vector3[8];
    cube.normals[0] = new Vector3(-l, -l, -l);
    cube.normals[1] = new Vector3(l, -l, -l);
    cube.normals[2] = new Vector3(-l, l, -l);
    cube.normals[3] = new Vector3(-l, -l, l);
    cube.normals[4] = new Vector3(l, l, -l);
    cube.normals[5] = new Vector3(l, -l, l);
    cube.normals[6] = new Vector3(-l, l, l);
    cube.normals[7] = new Vector3(l, l, l);

    cube.tvi = new Triplet[12];
    cube.tvi[0] = new Triplet(0, 1, 2);
    cube.tvi[1] = new Triplet(1, 2, 4);
    cube.tvi[2] = new Triplet(0, 1, 3);
    cube.tvi[3] = new Triplet(1, 3, 5);
    cube.tvi[4] = new Triplet(0, 2, 3);
    cube.tvi[5] = new Triplet(2, 3, 6);
    cube.tvi[6] = new Triplet(3, 5, 6);
    cube.tvi[7] = new Triplet(5, 6, 7);
    cube.tvi[8] = new Triplet(2, 4, 6);
    cube.tvi[9] = new Triplet(4, 6, 7);
    cube.tvi[10] = new Triplet(1, 4, 5);
    cube.tvi[11] = new Triplet(4, 5, 7);

    cube.tni = new Triplet[12];
    cube.tni[0] = new Triplet(0, 1, 2);
    cube.tni[1] = new Triplet(1, 2, 4);
    cube.tni[2] = new Triplet(0, 1, 3);
    cube.tni[3] = new Triplet(1, 3, 5);
    cube.tni[4] = new Triplet(0, 2, 3);
    cube.tni[5] = new Triplet(2, 3, 6);
    cube.tni[6] = new Triplet(3, 5, 6);
    cube.tni[7] = new Triplet(5, 6, 7);
    cube.tni[8] = new Triplet(2, 4, 6);
    cube.tni[9] = new Triplet(4, 6, 7);
    cube.tni[10] = new Triplet(1, 4, 5);
    cube.tni[11] = new Triplet(4, 5, 7);

    meshes = new Mesh[]{cube};

    return meshes;
  }
}
