package reflectance;

import image.RGBA;

public class PhongMaterial {

  public RGBA ambient, diffuseReflectance, specularReflectance;
  public double shininess;

  public PhongMaterial(RGBA ambient, RGBA diffuse, RGBA specular, double shininess) {
    this.ambient = ambient;
    this.diffuseReflectance = diffuse;
    this.specularReflectance = specular;
    this.shininess = shininess;
  }

}
