package mesh;

import image.Image;
import image.ImageUtils;
import image.RGBA;

import java.io.IOException;

import rasterization.Correspondence;

import utils.Vector2;
import utils.Triplet;

public class TextureMap {

  private Image<RGBA> texture;
  boolean perspectiveCorrect;

  public TextureMap(String filename) {
    try {
      this.texture = ImageUtils.read(filename);
      this.perspectiveCorrect = false;
    } catch (IOException e) {
      System.err.println("Cannot read file " + filename);
    }
  }

  public RGBA access(Correspondence c) {
    //TODO: Blatt 5
    return new RGBA(0f, 0f, 0f);
  }


}
