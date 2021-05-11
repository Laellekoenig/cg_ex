package renderer;

import illumination.PhongPointLight;
import image.Image;
import image.RGBA;
import mesh.Mesh;
import occlusion.Occlusion;
import occlusion.ShadowType;
import rasterization.Correspondence;
import reflectance.PhongMaterial;
import utils.Matrix4;
import utils.Triplet;
import utils.Vector3;
import utils.Vector4;

public class PhongMeshRenderer extends MeshRenderer {

  protected Image<RGBA> img;
  protected PhongPointLight lightSource;
  protected PhongMaterial material;

  //Shadows
  private boolean shadows = false;
  private Occlusion shadowSystem;
  private double shadowBias;
  private int pcfMaskSize;
  private ShadowType shadowType = ShadowType.HARD;

  public PhongMeshRenderer(int w, int h, Mesh[] meshes, RGBA lightColor, double matShininess) {
    super(w, h, meshes);
    img = new Image<RGBA>(w, h);

    Vector3 lightPosition = new Vector3(-4, 4, 5);
    RGBA ambientLight = new RGBA(0.2, 0.2, 0.4);
    lightSource = new PhongPointLight(lightPosition, lightColor, ambientLight);

    RGBA matAmbient = new RGBA(0.4, 0.4, 0.4);
    RGBA matDiffuse = new RGBA(0.6, 0.2, 0.2);
    RGBA matSpecular = new RGBA(0.7, 0.7, 0.7);
    material = new PhongMaterial(matAmbient, matDiffuse, matSpecular, matShininess);
  }

  public RGBA getLightColor() {
    return lightSource.color;
  }

  public PhongPointLight getLightSource() {
    return lightSource;
  }

  public PhongMaterial getMaterial() {
    return material;
  }

  public void setMaterial(PhongMaterial mat) {
    material = mat;
  }

  @Override
  protected Image<RGBA> colorize(Image<Correspondence> correspondence) {
    if (shadows) {
      shadowSystem = new Occlusion(480, 480, shadowType, shadowBias, pcfMaskSize);
      //TODO: Blatt 4, Aufgabe 6 c)
      shadowSystem.generateShadowMap(projection, lightSource, meshes);
    }
    
    Vector4 eye = projection.getEye();
    for (int xx = 0; xx < correspondence.cols(); xx++) {
      for (int yy = 0; yy < correspondence.rows(); yy++) {
        Correspondence c = correspondence.get(xx, yy);
        if (c != null) {
          shadePhong(xx, yy, c, eye);
        }
      }
    }

    return img;
  }

  protected void shadePhong(int x, int y, Correspondence c, Vector4 eye) {

    //TODO: Blatt 4, Aufgabe 2

    RGBA I_A = lightSource.ambient;
    RGBA I_C = lightSource.color;
    RGBA r_A = material.ambient;
    RGBA r_D = material.diffuseReflectance;
    RGBA r_S = material.specularReflectance;
    double m = material.shininess;

    Vector3 vertexOne = c.mesh.vertices[c.mesh.tvi[c.triangle].get(0)];
    Vector3 vertexTwo = c.mesh.vertices[c.mesh.tvi[c.triangle].get(1)];
    Vector3 vertexThree = c.mesh.vertices[c.mesh.tvi[c.triangle].get(2)];

    Vector3 point = c.triCoords.interpolate(vertexOne, vertexTwo, vertexThree);

    Vector3 L = lightSource.position.minus(point);

    Vector3 normalOne = c.mesh.normals[c.mesh.tni[c.triangle].get(0)];
    Vector3 normalTwo = c.mesh.normals[c.mesh.tni[c.triangle].get(1)];
    Vector3 normalThree = c.mesh.normals[c.mesh.tni[c.triangle].get(2)];

    Vector3 N = c.triCoords.interpolate(normalOne, normalTwo, normalThree);

    L = L.normalize();
    N = N.normalize();

    // this formula for calculating the reflection vector was found on slide 42 of the slideset 9.
    Vector3 R = L.minus(N.times(L.dot(N) * 2));
    //Vector3 R = N.times(2 * N.dot(L)).minus(L);
    R = R.normalize();

    eye = eye.times(1/eye.w);
    Vector3 V = new Vector3(eye.x, eye.y, eye.z).minus(point);
    V = V.normalize();

    double LN = L.dot(N);
    double RV = R.dot(V);

    if (LN < 0) {
      //img.set(x, y, lightSource.ambient.multElementWise(material.ambient));
      LN = 0;
    }

    /*if (Math.abs(Math.acos(V.dot(R))) >= 0 && Math.abs(Math.acos(V.dot(R))) <= 1) {
      img.set(x, y, lightSource.ambient.multElementWise(material.ambient));
      return;
    }*/


    RGBA I_Phong = r_A.multElementWise(I_A).plus(r_D.multElementWise(I_C).times(LN)).plus(r_S.multElementWise(I_C).times(Math.pow(RV, m)));


    //TODO: Blatt 4, Aufgabe 6 c)
    if (shadowSystem.inShadow(point) == 1.0) {
      img.set(x, y, I_Phong);
    }

  }

  public void enableShadow() {
    shadows = true;
  }

  public void setShadowBias(double shadowBias) {
    this.shadowBias = shadowBias;
  }

  //For testing purposes
  public Occlusion getShadowSystem() {
    return shadowSystem;
  }

  public void setShadowType(ShadowType shadowType) {
    this.shadowType = shadowType;
  }

  public void setPCFMaskSize(int pcfMaskSize) {
    this.pcfMaskSize = pcfMaskSize;
  }


  @Override
  public void clearImg() {
    for (int i = 0; i < img.size(); i++) {
      if (img.get(i) != null) {
        RGBA reset = img.get(i);
        reset.a = 1f;
        reset.r = 0;
        reset.g = 0;
        reset.b = 0;
      }
    }
  }

  @Override
  public void rotateLights(Matrix4 rotation) {
    Vector3 lp = lightSource.position;
    Vector4 t = new Vector4(lp.x, lp.y, lp.z, 1);
    Vector4 rotated = rotation.multiply(t);
    rotated.times(1.0 / rotated.z);
    lightSource.position = new Vector3(rotated.x, rotated.y, rotated.z);
  }


}
