package reflectance;

import image.RGBA;
import utils.Vector3;

public class OrenNayar extends Brdf {

  public RGBA albedo;
  public double sigmaSquarred;
  private double eps = 0.0001;

  public OrenNayar(RGBA albedo, double roughness) {
    this.albedo = albedo;
    this.sigmaSquarred = roughness;
  }

  @Override
  protected RGBA getRadiance(Vector3 toEye, Vector3 toLight, Vector3 n) {

    //TODO: Blatt 4, Aufgabe 5

    return new RGBA(0,0,0);
  }
}
