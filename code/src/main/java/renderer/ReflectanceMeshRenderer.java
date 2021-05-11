package renderer;

import java.util.ArrayList;

import occlusion.Occlusion;
import occlusion.ShadowType;
import illumination.PointLight;
import image.Image;
import image.RGBA;
import rasterization.Correspondence;
import reflectance.Brdf;
import reflectance.LambertBrdf;
import utils.Matrix4;
import utils.Triplet;
import utils.Vector3;
import utils.Vector4;
import mesh.Mesh;

public class ReflectanceMeshRenderer extends MeshRenderer {

  protected Image<RGBA> img;
  public ArrayList<Brdf> matBrdf = new ArrayList<Brdf>();
  public ArrayList<PointLight> lightSources = new ArrayList<PointLight>();

  //Shadows
  private boolean shadows = false;
  private Occlusion shadowSystem;
  private double shadowBias;
  private int pcfMaskSize;
  public ShadowType shadowType = ShadowType.HARD;

  public ReflectanceMeshRenderer(int w, int h, Mesh[] meshes) {
    super(w, h, meshes);
    img = new Image<RGBA>(w, h);
    matBrdf.add(new LambertBrdf(new RGBA(0.7, 0.7, 0.7)));
    lightSources.add(new PointLight(new Vector3(-4, 4, 5), new RGBA(0.7, 0.2, 0.2)));
  }

  @Override
  protected Image<RGBA> colorize(Image<Correspondence> correspondence) {
    if (shadows) {
      shadowSystem = new Occlusion(480, 480, shadowType, shadowBias, pcfMaskSize);
    }

    Vector4 eye = projection.getEye();
    for (PointLight lightSource : lightSources) {
      //TODO: Blatt 4, Aufgabe 6 c)
      if (shadows) {
        shadowSystem.generateShadowMap(projection, lightSource, meshes);
      }

      for (int x = 0; x < correspondence.cols(); x++) {
        for (int y = 0; y < correspondence.rows(); y++) {
          Correspondence c = correspondence.get(x, y);
          if (c != null) {
            shade(x, y, c, eye, lightSource);
          }
        }
      }
    }
    return img;
  }

  protected void shade(int x, int y, Correspondence c, Vector4 eye, PointLight lightSource) {

    //TODO: Blatt 4, Aufgabe 3 b)

    Vector3 vertexOne = c.mesh.vertices[c.mesh.tvi[c.triangle].get(0)];
    Vector3 vertexTwo = c.mesh.vertices[c.mesh.tvi[c.triangle].get(1)];
    Vector3 vertexThree = c.mesh.vertices[c.mesh.tvi[c.triangle].get(2)];

    Vector3 position = c.triCoords.interpolate(vertexOne, vertexTwo, vertexThree);

    Vector3 normalOne = c.mesh.normals[c.mesh.tni[c.triangle].get(0)];
    Vector3 normalTwo = c.mesh.normals[c.mesh.tni[c.triangle].get(1)];
    Vector3 normalThree = c.mesh.normals[c.mesh.tni[c.triangle].get(2)];

    Vector3 normal = c.triCoords.interpolate(normalOne, normalTwo, normalThree);

    RGBA color = new RGBA(0, 0, 0, 1);

    for(Brdf brdf : matBrdf) {
      for(PointLight pointLight : lightSources) {
        color = color.plus(brdf.getRadiance(eye, position, pointLight, normal));
      }
    }

    //TODO: Blatt 4, Aufgabe 6 c)
    if (shadows && shadowSystem.inShadow(position) == 1 || !shadows) {
      img.set(x, y, color);
    }
  }

  public void enableShadow() {
    shadows = true;
  }

  public void setShadowBias(double bias) {
    shadowBias = bias;
  }

  public void setPCFMaskSize(int size) {
    pcfMaskSize = size;
  }

  public void setShadowType(ShadowType sType) {
    shadowType = sType;
  }

  public Occlusion getShadowSystem() {
    return shadowSystem;
  }

  @Override
  public void clearImg() {
    for (int i = 0; i < img.size(); ++i) {
      if (img.get(i) != null) {
        img.get(i).r = 0;
        img.get(i).g = 0;
        img.get(i).b = 0;
        img.get(i).a = 1;
      }
    }
  }

  @Override
  public void rotateLights(Matrix4 rotation) {
    for (PointLight lightSource : lightSources) {
      Vector3 lp = lightSource.position;
      Vector4 t = new Vector4(lp.x, lp.y, lp.z, 1);
      Vector4 rotated = rotation.multiply(t);
      rotated.times(1.0 / rotated.z);
      lightSource.position = new Vector3(rotated.x, rotated.y, rotated.z);
    }
  }

}
