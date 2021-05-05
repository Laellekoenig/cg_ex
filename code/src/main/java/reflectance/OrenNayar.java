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
    // start by calculating A and B
    double A = 1 - 0.5 * (sigmaSquarred / (sigmaSquarred + 0.33));
    double B =    0.45 * (sigmaSquarred / (sigmaSquarred + 0.09));

    // calculate angles phiIn and phiOut next
    // cos
    double phiInCos  = toLight.normalize().dot(n.normalize());
    double phiOutCos = toEye.normalize().dot(n.normalize());
    // sin = sqrt(1 - cos^2)
    double phiInSin   = Math.sqrt(1 - Math.pow(phiInCos, 2));
    double phiOutSin  = Math.sqrt(1 - Math.pow(phiOutCos, 2));
    // tan = sin / cos + 0.0001
    double phiInTan   = phiInSin / (phiInCos + 0.0001);
    double phiOutTan  = phiOutSin / (phiOutCos + 0.0001);
    // cos(a - b) = cos a * cos b + sin a + sin b
    double cosDelta = (phiInCos * phiOutCos) + (phiInSin * phiOutSin);
    // determine alpha and beta
    double alpha  = Math.max(phiInSin, phiOutSin);
    double beta   = Math.min(phiInTan, phiOutTan);

    // calculate final result
    RGBA orenNayar = albedo.times(A + (B * Math.max(0, cosDelta)) * alpha * beta);
    return orenNayar;
  }
}
