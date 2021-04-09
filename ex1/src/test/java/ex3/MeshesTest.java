package ex3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import image.Image;
import java.lang.reflect.Field;
import mesh.Mesh;
import org.junit.Before;
import org.junit.Test;
import projection.PinholeProjection;
import rasterization.Correspondence;
import rasterization.MeshRasterizer;
import rasterization.TrianglePixelHandler;
import rasterization.TriangleRasterizer;
import renderer.MeshRenderer;
import testUtils.UnitCubeTurnableTest;
import utils.Triplet;
import utils.Vector2;
import utils.Vector3;

public class MeshesTest {

  private int width, height;
  private MeshRasterizer rasterizer;
  private UnitCubeTurnableTest test;

  @Before
  public void setup() {
    width = 160;
    height = 90;
    rasterizer = new MeshRasterizer(width, height);
    test = new UnitCubeTurnableTest("NoGoldStandardFile", "title",
        new MeshRenderer(width, height, null));
  }

  @Test
  public void countRasterizedTrianglesTest() {
    CountTriangleRasterizer triangleRasterizer = new CountTriangleRasterizer(null, width, height);
    rasterizer.rasterize(test.getProjection(), triangleRasterizer, test.meshes);
    int expected = 12;
    int count = triangleRasterizer.getCount();
    assertEquals("The mesh rasterizer is called " + (count > expected ? "too often."
        : "less than expected."), expected, count);
  }

  private class CountTriangleRasterizer extends TriangleRasterizer {

    private int counter;

    CountTriangleRasterizer(TrianglePixelHandler handler, int w, int h) {
      super(handler, w, h);
    }

    @Override
    public void rasterTriangle(Vector2[] trianglePoints) {
      ++counter;
    }

    int getCount() {
      return counter;
    }
  }

}

