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
    //create new image, 300x300 pixels, white background
    int width = 300;
    int height = 300;
    Image<RGBA> img = new Image<RGBA>(width, height, RGBA.white);

    //draw pixels near origin
    img.set(0, 0, RGBA.black);
    img.set(0, 1, RGBA.black);
    img.set(1, 0, RGBA.black);
    img.set(1, 1, RGBA.black);

    //draw horizontal line across image
    for (int i = 0; i < width; i++) {
      img.set(i, 150, RGBA.green);
    }

    //draw vertical line across image
    for (int i = 0; i < height; i++) {
      img.set(150, i, RGBA.magenta);
    }

    //draw circle
    int r = 30;
    int coordX = 225;
    int coordY = 225;

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int deltaX = j - coordX;
        int deltaY = i - coordY;
        double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        if (distance <= r) {
          img.set(j, i, RGBA.cyan);
        }
      }
    }

    //save image
    try {
      ImageUtils.write(img, "aufgabe1.png");
    } catch (IOException e) {
      e.printStackTrace();
    }

    return img;
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
