package illumination;

import image.RGBA;
import utils.Vector3;

public class PointLight {

  public Vector3 position;
  public RGBA color;

  public PointLight(final Vector3 lightPosition, final RGBA lightColor) {
    this.position = lightPosition;
    this.color = lightColor;
  }
}
