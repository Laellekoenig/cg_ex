package ex3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.Before;
import org.junit.Test;
import projection.TurnTable;
import renderer.MeshRenderer;
import testUtils.UnitCubeTurnableTest;
import utils.Vector3;

public class TurntableTest {

  private double delta = .01;
  private int width = 160;
  private int height = 90;
  private TurnTable turntable = null;

  @Before
  public void setup() {
    MeshRenderer renderer = new MeshRenderer(width, height, null);
    turntable = new TurnTable(renderer,
        new UnitCubeTurnableTest("NoGoldStandardFile", "title", renderer), width, height);
  }

  @Test
  public void handleAzimuthTest() {
    try {
      Method m = turntable.getClass().getDeclaredMethod("handleAzimuth", int.class, double.class);
      m.setAccessible(true);

      double azimuth = 0;
      azimuth = (Double) m.invoke(turntable, -width, azimuth);
      assertEquals("Not turning by -180° when moving the mouse from right to left image border.",
          Math.PI, azimuth, delta);
      azimuth = (Double) m.invoke(turntable, width, azimuth);
      assertEquals("Not turning by 180° when moving the mouse from left to right image border.",
          -Math.PI, azimuth, delta);
      assertEquals("Azimuth changes when keeping the mouse still.", azimuth,
          (Double) m.invoke(turntable, width, azimuth), 0);

      int[] vals = {0, 10, 0, -10, 0};
      double[] expected = {0, -.1963, 0, .1963, 0};
      for (int i = 0; i < vals.length; ++i) {
        azimuth = (Double) m.invoke(turntable, vals[i], azimuth);
        assertEquals(expected[i], azimuth, delta);
      }
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      fail();
    }
  }

  @Test
  public void handleElevationTest() {
    try {
      Method m = turntable.getClass().getDeclaredMethod("handleElevation", int.class, double.class);
      m.setAccessible(true);

      double elevation = 0;
      elevation = (Double) m.invoke(turntable, -height, elevation);
      assertEquals("Not rotating by 180° when moving the mouse from right to left image border.",
          -Math.PI, elevation, delta);
      elevation = (Double) m.invoke(turntable, height, elevation);
      assertEquals("Not rotating by -180° when moving the mouse from left to right image border.",
          Math.PI, elevation, delta);
      assertEquals("Elevation changes when keeping the mouse still.", elevation,
          (Double) m.invoke(turntable, height, elevation), 0);

      int[] vals = {0, 10, 0, -10, 0};
      double[] expected = {0, .3491, 0, -.3491, 0};
      for (int i = 0; i < vals.length; ++i) {
        elevation = (Double) m.invoke(turntable, vals[i], elevation);
        assertEquals(expected[i], elevation, delta);
      }
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      fail();
    }
  }

  @Test
  public void zoomTest() {
    try {
      Field f = turntable.getClass().getDeclaredField("translation");
      f.setAccessible(true);
      Vector3 translation = (Vector3) f.get(turntable);

      Method m = turntable.getClass().getDeclaredMethod("zoom", boolean.class);
      m.setAccessible(true);

      int steps = 10;
      Vector3 zoom = new Vector3(0, 0, steps * turntable.zoomStep);
      for (int i = 0; i < steps; ++i) {
        m.invoke(turntable, true);
      }
      assertEquals("Probably zoom direction is wrong.", translation.plus(zoom), f.get(turntable));

      for (int i = 0; i < steps; ++i) {
        m.invoke(turntable, false);
      }
      assertEquals("Zoom does not return back to origin when zoomed in and out equally often.",
          translation, f.get(turntable));

      for (int i = 0; i < steps; ++i) {
        m.invoke(turntable, false);
      }
      assertEquals("Probably zoom direction is wrong.", translation.minus(zoom), f.get(turntable));
    } catch (IllegalAccessException | InvocationTargetException | NoSuchFieldException | NoSuchMethodException e) {
      fail();
    }
  }
}
