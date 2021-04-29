package ex4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import image.RGBA;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.Test;
import reflectance.OrenNayar;
import utils.Vector3;

public class OrenNayarTest {

  @Test
  public void blackForAlbedoBlack() {
    OrenNayar orenNayar = new OrenNayar(RGBA.black, 20);
    try {
      Method m = orenNayar.getClass()
          .getDeclaredMethod("getRadiance", Vector3.class, Vector3.class, Vector3.class);
      m.setAccessible(true);

      Vector3 v1 = new Vector3(1, 0, 0);
      Vector3 v2 = new Vector3(0, 1, 0);
      Vector3 v3 = new Vector3(0, 0, 1);

      RGBA color = (RGBA) m.invoke(orenNayar, v1, v2, v3);
      color.clamp();
      assertEquals(RGBA.black, color);

      color = (RGBA) m.invoke(orenNayar, v1, v1, v1);
      color.clamp();
      assertEquals(RGBA.black, color);

      color = (RGBA) m.invoke(orenNayar, v1, v1, v2);
      color.clamp();
      assertEquals(RGBA.black, color);

      color = (RGBA) m.invoke(orenNayar, v1, v2, v1);
      color.clamp();
      assertEquals(RGBA.black, color);

      color = (RGBA) m.invoke(orenNayar, v1, v2, v2);
      color.clamp();
      assertEquals(RGBA.black, color);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      fail();
    }
  }

  @Test
  public void perfectReflectionWithZeroRoughnessTest() {
    RGBA materialColor = RGBA.red;
    OrenNayar orenNayar = new OrenNayar(materialColor, 0);
    try {
      Method m = orenNayar.getClass()
          .getDeclaredMethod("getRadiance", Vector3.class, Vector3.class, Vector3.class);
      m.setAccessible(true);

      Vector3 v1 = new Vector3(1, 0, 0);
      Vector3 v2 = new Vector3(0, 1, 0);
      Vector3 v3 = new Vector3(0, 0, 1);

      RGBA color = (RGBA) m.invoke(orenNayar, v1, v2, v3);
      color.clamp();
      assertEquals(materialColor, color);

      color = (RGBA) m.invoke(orenNayar, v1, v1, v1);
      color.clamp();
      assertEquals(materialColor, color);

      color = (RGBA) m.invoke(orenNayar, v1, v1, v2);
      color.clamp();
      assertEquals(materialColor, color);

      color = (RGBA) m.invoke(orenNayar, v1, v2, v1);
      color.clamp();
      assertEquals(materialColor, color);

      color = (RGBA) m.invoke(orenNayar, v1, v2, v2);
      color.clamp();
      assertEquals(materialColor, color);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      fail();
    }
  }
}
