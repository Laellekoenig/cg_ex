package illumination;

import image.RGBA;
import utils.Vector3;

public class DirectionalLight {

  public Vector3 direction;
  public RGBA color;

  public DirectionalLight(final Vector3 direction, final RGBA color) {
    this.direction = direction.normalize();
    this.color = color;
  }

}
