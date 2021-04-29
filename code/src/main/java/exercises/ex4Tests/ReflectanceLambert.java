package exercises.ex4Tests;

import mesh.Mesh;
import mesh.MeshReader;

public class ReflectanceLambert extends exercises.ex4Tests.TeapotReflectanceLambert {

  public ReflectanceLambert(String gsFileName, String title) {
    super(gsFileName, title, false);
  }

  @Override
  protected Mesh[] createMeshes() {
    Mesh mesh = MeshReader.loadObj("data/resources/Teapot.obj").get(0);
    Mesh mesh2 = MeshReader.loadObj("data/resources/TeapotCube.obj").get(1);
    return new Mesh[]{mesh, mesh2};
  }

}