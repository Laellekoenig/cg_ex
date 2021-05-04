package reflectance;

import image.RGBA;
import renderer.LambertMeshRenderer;
import utils.Vector3;

public class LambertBrdf extends Brdf {

  public RGBA albedo;

  public LambertBrdf(final RGBA albedo) {
    super();
    this.albedo = albedo;
  }

  protected RGBA getRadiance(Vector3 toEye, Vector3 toLight, Vector3 n) {

    // Because Lambert's reflector is constant the radiance is not dependant on any of the three vectors, as long
    // as the point has a light source illuminating it and the point is visible. So if the angle between the light vector
    // and n or the eye vector and n is larger than 90 degrees, then it doesn't get illuminated. But if that isn't the
    // case and the point is therefore visible and illuminated, then the radiance is always equal to the albedo.

    if(toEye.dot(n) < 0 || toLight.dot(n) < 0) {
      return new RGBA(0, 0, 0, 1);
    }
    return albedo;
  }
}
