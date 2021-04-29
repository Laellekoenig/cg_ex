package illumination;

import image.RGBA;
import utils.Vector3;

public class PhongPointLight extends PointLight {

  public RGBA ambient;

  public PhongPointLight(final Vector3 lightPosition, final RGBA lightColor, final RGBA ambient) {
    super(lightPosition, lightColor);
    this.ambient = ambient;
  }
}
