package mesh;

import image.RGBA;
import utils.Vector2;
import utils.Vector3;
import utils.Triplet;

public class Mesh {

  public Vector3[] vertices, normals;
  public RGBA[] colors;
  public Vector2[] texCoords;
  public Triplet[] tvi, tti, tni, tci; //triangle vertices, texture, normal and color indices
  public TextureMap texture;

  public boolean hasTexture() {
    return texture != null;
  }

  public void setTexture(String filename) {
    texture = new TextureMap(filename);
  }


}