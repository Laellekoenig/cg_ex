package reflectance;

public class Material {

  public Brdf[] brdf;

  public Material(Brdf... brdf) {
    this.brdf = brdf;
  }

}
