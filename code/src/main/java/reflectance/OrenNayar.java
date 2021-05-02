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
    //start by calculating A and B
    double A = 1 - 0.5 * (sigmaSquarred / (sigmaSquarred + 0.33));
    double B =    0.45 * (sigmaSquarred / (sigmaSquarred + 0.09));

    //calculate angles phiIn and phiOut next
    //cos
    double phiIn  = Math.acos(toLight.normalize().dot(n.normalize()));
    double phiOut = Math.acos(toEye.normalize().dot(n.normalize()));

    //determine alpha and beta
    double alpha = Math.max(phiIn, phiOut);
    double beta   = Math.max(phiIn, phiOut);
    double cosDelta = Math.cos(phiIn - phiOut);

    //calculate final result
    RGBA orenNayar = albedo.times(A + (B * Math.max(0, cosDelta)) * Math.sin(alpha) * Math.tan(beta));
    return orenNayar;
  }
}
