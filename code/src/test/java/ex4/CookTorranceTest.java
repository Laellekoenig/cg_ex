package ex4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import image.RGBA;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.Before;
import org.junit.Test;
import reflectance.CookTorrance;
import utils.Vector3;

public class CookTorranceTest {

  private CookTorrance cookTorrance;

  @Before
  public void setup() {
    cookTorrance = new CookTorrance(RGBA.red, 1, 1);
  }

  @Test
  public void blackIfViewpoint90degreesToLightSourceTest() {
    try {
      Method m = cookTorrance.getClass()
          .getDeclaredMethod("getRadiance", Vector3.class, Vector3.class, Vector3.class);
      m.setAccessible(true);

      Vector3 v1 = new Vector3(1, 0, 0);
      Vector3 v2 = new Vector3(0, 1, 0);
      RGBA color = (RGBA) m.invoke(cookTorrance, v1, v2, v1);
      color.clamp();

      assertEquals("Color should be black for angle=90° between normal+lightsource and view.",
          RGBA.black, color);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      fail();
    }
  }

  @Test
  public void failIfViewpoint180degreesToLightSourceTest() {
    try {
      Method m = cookTorrance.getClass()
          .getDeclaredMethod("getRadiance", Vector3.class, Vector3.class, Vector3.class);
      m.setAccessible(true);

      Vector3 v1 = new Vector3(1, 0, 0);
      Vector3 v2 = new Vector3(-1, 0, 0);
      RGBA color = (RGBA) m.invoke(cookTorrance, v1, v2, v1);
      color.clamp();

      assertEquals("Division by 0 expected du to cos(90°).", "(NaN, NaN, NaN)", color.toString());
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      fail();
    }
  }
}
