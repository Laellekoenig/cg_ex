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
    //calculate the vector H
    double absLV = Math.pow(toLight.plus(toEye).length(), -1);
    Vector3 H = toLight.plus(toEye).times(absLV);

    //calculate roughness D
    double scalarNH = n.dot(H);
    double fracture1 = 1 / (4 * Math.pow(m, 2) * Math.pow(scalarNH, 4));
    double fracture2 = (-1) * ((1 - Math.pow(scalarNH, 2)) / (Math.pow(scalarNH, 2) * Math.pow(m, 2)));
    double D = fracture1 * Math.exp(fracture2);

    //calculate geometric attenuation
    double G1 = 1;
    double G2 = 2 * Math.abs(n.dot(H)) * Math.abs(n.dot(toEye)) / Math.abs(toEye.dot(H));
    double G3 = 2 * Math.abs(n.dot(H)) * Math.abs(n.dot(toLight)) / Math.abs(toEye.dot(H));
    double G = Math.min(G1, G2);
    G = Math.min(G, G3);

    //calculate fresnel F
    double F = r0 + (1 - r0) * Math.pow((1 - Math.abs(H.dot(toEye))), 5);

    //and finally calculate the Cook-Torrence BRDF
    RGBA cookTorrence = albedo.times(F * D * G / toEye.dot(n));

    return cookTorrence;
  }
  
}
