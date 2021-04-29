package ex4;

import static org.junit.Assert.assertEquals;

import illumination.PointLight;
import image.RGBA;
import org.junit.Before;
import org.junit.Test;
import projection.PinholeProjection;
import utils.Matrix4;
import utils.Vector3;

public class ShadowViewMatrixTest {

  private final double delta = .01;
  private PinholeProjection projection;
  private RGBA lightColor;

  @Before
  public void setup() {
    projection = new PinholeProjection(160, 90);
    lightColor = RGBA.white;
  }

  @Test
  public void failIfLightSourceOnXAxisTest() {
    for (int i = 0; i < 5; ++i) {
      PointLight lightSource = new PointLight(new Vector3(i, 0, 0), lightColor);

      if (i == 0) {
        assertEquals("[NaN NaN NaN NaN \n NaN NaN NaN NaN \n NaN NaN NaN NaN \n 0.0 0.0 0.0 1.0]",
            projection.getViewMatrixOfLightSource(lightSource).toString());
      } else {
        assertEquals(
            "[NaN NaN NaN NaN \n NaN NaN NaN NaN \n 1.0 0.0 0.0 -" + i + ".0 \n 0.0 0.0 0.0 1.0]",
            projection.getViewMatrixOfLightSource(lightSource).toString());
      }
    }
  }

  @Test
  public void viewMatrixOfLightSourceTest() {
    PointLight lightSource = new PointLight(new Vector3(0, 1, 0), lightColor);
    Matrix4 p = projection.getViewMatrixOfLightSource(lightSource);
    for (int x = 0; x < 4; ++x) {
      for (int y = 0; y < 4; ++y) {
        if (x == 0 && y == 0) {
          assertEquals(1, p.get(x, y), delta);
        } else if (x == 1 && y == 2) {
          assertEquals(-1, p.get(x, y), delta);
        } else if (x == 2 && y == 1) {
          assertEquals(1, p.get(x, y), delta);
        } else if (x == 2 && y == 3) {
          assertEquals(-1, p.get(x, y), delta);
        } else if (x == 3 && y == 3) {
          assertEquals(1, p.get(x, y), delta);
        } else {
          assertEquals(0, p.get(x, y), delta);
        }
      }
    }

    lightSource = new PointLight(new Vector3(0, 0, 1), lightColor);
    p = projection.getViewMatrixOfLightSource(lightSource);
    for (int x = 0; x < 4; ++x) {
      for (int y = 0; y < 4; ++y) {
        if (x == 0 && y == 0) {
          assertEquals(1, p.get(x, y), delta);
        } else if (x == 1 && y == 1) {
          assertEquals(1, p.get(x, y), delta);
        } else if (x == 2 && y == 2) {
          assertEquals(1, p.get(x, y), delta);
        } else if (x == 2 && y == 3) {
          assertEquals(-1, p.get(x, y), delta);
        } else if (x == 3 && y == 3) {
          assertEquals(1, p.get(x, y), delta);
        } else {
          assertEquals(0, p.get(x, y), delta);
        }
      }
    }

    lightSource = new PointLight(new Vector3(1, 1, 1), lightColor);
    p = projection.getViewMatrixOfLightSource(lightSource);
    for (int x = 0; x < 4; ++x) {
      for (int y = 0; y < 4; ++y) {
        if (x == 0 && y == 0) {
          assertEquals(0.82, p.get(x, y), delta);
        } else if (x == 0 && (y == 1 || y == 2)) {
          assertEquals(-0.41, p.get(x, y), delta);
        } else if (x == 1 && y == 1) {
          assertEquals(0.71, p.get(x, y), delta);
        } else if (x == 1 && y == 2) {
          assertEquals(-0.71, p.get(x, y), delta);
        } else if (x == 2) {
          if (y == 3) {
            assertEquals(-1.73, p.get(x, y), delta);
          } else {
            assertEquals(0.58, p.get(x, y), delta);
          }
        } else if (x == 3 && y == 3) {
          assertEquals(1, p.get(x, y), delta);
        } else {
          assertEquals(0, p.get(x, y), delta);
        }
      }
    }
  }
}
