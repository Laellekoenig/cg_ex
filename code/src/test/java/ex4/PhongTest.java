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
import renderer.PhongMeshRenderer;
import testUtils.UnitCubeTurnableTest;
import utils.BarycentricCoordinates;
import utils.Vector4;

public class PhongTest {

  private UnitCubeTurnableTest test;
  private PhongMeshRenderer renderer;

  @Before
  public void setup() {
    int width = 160;
    int height = 90;
    RGBA lightColor = new RGBA(.3, .3, .3);
    double matShininess = 2.0;
    test = new UnitCubeTurnableTest("NoGoldStandardFile", "title",
        new MeshRenderer(width, height, null));
    renderer = new PhongMeshRenderer(width, height, test.meshes, lightColor, matShininess);
  }

  @Test
  public void colorNotNullForAllTrianglesTest() {
    try {
      Method m = renderer.getClass()
          .getDeclaredMethod("shadePhong", int.class, int.class, Correspondence.class,
              Vector4.class);
      m.setAccessible(true);
      Field f = renderer.getClass().getDeclaredField("img");
      f.setAccessible(true);

      for (int i = 0; i < test.meshes[0].tvi.length; ++i) {
        Correspondence c = new Correspondence(test.meshes[0], i,
            new BarycentricCoordinates(.5, .5, .5), 0);
        m.invoke(renderer, 0, i, c, new Vector4(0, 0, 0, 0));

        f.get(renderer);
        assertNotNull(((Image<RGBA>) f.get(renderer)).get(0, i));
      }
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e) {
      fail();
    }
  }
}
