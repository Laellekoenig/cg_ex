package exercises;

import image.Image;
import image.ImageUtils;
import image.RGBA;

import java.io.IOException;

import renderer.SimpleRenderer;
import shader.ConstantColorShader;
import shader.InterpolatedColorShader;
import utils.Vector2;

public class Ex1 {

  public static void main(String[] args) {
    new Ex1();
  }

  public Ex1() {

    //Blatt 1, Aufgabe 1 Beispiele
    createAndSaveImage();

    //Blatt 1, Aufgabe 2
    lineRasterExample();

    //Blatt 1, Aufgabe 4 Beispiele
    triangleRasterExample();

    //Blatt 1, Aufgabe 5 Beispiele
    drawInterpolatedTriangles();
  }

  public static Image<RGBA> createAndSaveImage() {

    //TODO: Blatt1, Aufgabe 1

    return null;
  }

  public static Image<RGBA> flipImageUpsideDown(Image<RGBA> img) {

    Image<RGBA> out = img.clone();

    //TODO: Blatt 1, Aufgabe 1

    return out;
  }

  private void lineRasterExample() {
    int width = 500;
    int height = 480;
    SimpleRenderer simpleRenderer = new SimpleRenderer(width, height, new ConstantColorShader());

    //TODO: Blatt 1, Aufgabe 2

    try {
      ImageUtils.write(simpleRenderer.getImg(), "lineRasterExample.png");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void triangleRasterExample() {
    int width = 500;
    int height = 480;
    SimpleRenderer simpleRenderer = new SimpleRenderer(width, height, new ConstantColorShader());

    //TODO: Blatt 1, Aufgabe 4

    try {
      ImageUtils.write(simpleRenderer.getImg(), "triangleRasterExample.png");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void drawInterpolatedTriangles() {
    SimpleRenderer renderer = new SimpleRenderer(500, 480, new InterpolatedColorShader());

    //TODO: Blatt 1, Aufgabe 5

    try {
      ImageUtils.write(renderer.getImg(), "interpolated.png");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
