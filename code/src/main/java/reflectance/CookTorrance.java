package reflectance;

import image.RGBA;
import utils.Vector3;

public class CookTorrance extends Brdf {

  private final double eps = 1e-4;

  private double m;
  private double r0;
  private RGBA albedo;

  public CookTorrance(RGBA albedo, double m, double r0) {
    this.albedo = albedo;
    this.m = m;
    this.r0 = r0;
  }

  @Override
  protected RGBA getRadiance(Vector3 toEye, Vector3 toLight, Vector3 n) {

    //TODO: Blatt 4, Aufgabe 4

    return new RGBA(0,0,0);
  }
  
}
