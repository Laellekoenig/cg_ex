package exercises.ex4Tests;

import mesh.Mesh;
import mesh.MeshReader;
import exercises.ex4Tests.TeapotPhongShadingTest;

public class PhongTest extends TeapotPhongShadingTest {

  public PhongTest(String gsFileName, String title) {
    super(gsFileName, title);
  }

  @Override
  protected Mesh[] createMeshes() {
    Mesh mesh = MeshReader.loadObj("data/resources/Teapot.obj").get(0);
    Mesh mesh2 = MeshReader.loadObj("data/resources/TeapotCube.obj").get(1);
    return new Mesh[]{mesh, mesh2};
  }

}
