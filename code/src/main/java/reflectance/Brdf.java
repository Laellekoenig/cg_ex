package reflectance;

import illumination.PointLight;
import image.RGBA;
import utils.Vector3;
import utils.Vector4;

public abstract class Brdf {

  protected abstract RGBA getRadiance(Vector3 toEye, Vector3 toLight, Vector3 n);

  public RGBA getRadiance(Vector4 eye, Vector3 position, PointLight light, Vector3 normal) {
    Vector3 toLight = light.position.minus(position).normalize();
    double ln = toLight.dot(normal);
    Vector3 toEye = new Vector3(eye.x, eye.y, eye.z).minus(position.times(eye.w)).normalize();

    if (ln > 0) {
      RGBA radiance = getRadiance(toEye, toLight, normal.normalize());
      radiance = radiance.times(ln);
      return radiance.multElementWise(light.color);
    }

    return new RGBA(0, 0, 0);
  }

}
