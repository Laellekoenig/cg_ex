package ex4;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import image.Image;
import image.RGBA;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.Before;
import org.junit.Test;
import rasterization.Correspondence;
import renderer.LambertMeshRenderer;
import renderer.MeshRenderer;
import testUtils.UnitCubeTurnableTest;
import utils.BarycentricCoordinates;

public class LambertTest {

  private UnitCubeTurnableTest test;
  private LambertMeshRenderer renderer;

  @Before
  public void setup() {
    int width = 160;
    int height = 90;
    test = new UnitCubeTurnableTest("NoGoldStandardFile", "title",
        new MeshRenderer(width, height, null));
    renderer = new LambertMeshRenderer(width, height, test.meshes);
  }

  @Test
  public void onlyColorTrianglesFacingTheLightSourceTest() {
    try {
      Method m = renderer.getClass()
          .getDeclaredMethod("colorizePixel", int.class, int.class, Correspondence.class);
      m.setAccessible(true);
      Field f = renderer.getClass().getDeclaredField("img");
      f.setAccessible(true);

      for (int i = 0; i < test.meshes[0].tvi.length; ++i) {
        Correspondence c = new Correspondence(test.meshes[0], i,
            new BarycentricCoordinates(.5, .5, .5), 0);
        m.invoke(renderer, 0, i, c);

        f.get(renderer);
        if (i == 0 || i == 1 || i == 2 || i == 3 || i == 10 || i == 11) {
          assertNull(((Image<RGBA>) f.get(renderer)).get(0, i));
        } else {
          assertNotNull(((Image<RGBA>) f.get(renderer)).get(0, i));
        }
      }
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e) {
      fail();
    }
  }
}
