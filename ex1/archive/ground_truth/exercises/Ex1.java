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
      ImageUtils.write(img, "a1.png");
    } catch (IOException e) {
      e.printStackTrace();
    }

    //flip image
    flipImageUpsideDown(img);

    return img;
  }

  public static Image<RGBA> flipImageUpsideDown(Image<RGBA> img) {

    Image<RGBA> out = img.clone();
    int w = out.cols();
    int h = out.rows();

    //TODO: Blatt 1, Aufgabe 1
    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        RGBA color = img.get(j, i);
        out.set(j, h - 1 - i, color);
      }
    }

    //save image
    try {
      ImageUtils.write(out, "mirror.png");
    } catch (IOException e) {
      e.printStackTrace();
    }

    return out;
  }

  private void lineRasterExample() {
    int width = 500;
    int height = 480;
    SimpleRenderer simpleRenderer = new SimpleRenderer(width, height, new ConstantColorShader());

    //TODO: Blatt 1, Aufgabe 2
    //draw lines, starting from center, going to the edge every 22,5°
    //-> check all possible positive and negative slopes above and below m = 1
    Vector2 start = new Vector2(250, 240);
    Vector2 end = new Vector2(499, 479);
    Vector2 end2 = new Vector2(499, 240);
    Vector2 end3 = new Vector2(499, 0);
    Vector2 end4 = new Vector2(250, 0);
    Vector2 end5 = new Vector2(0, 0);
    Vector2 end6 = new Vector2(0, 240);
    Vector2 end7 = new Vector2(0, 479);
    Vector2 end8 = new Vector2(250, 479);
    Vector2 end9 = new Vector2(499, 360);
    Vector2 end10 = new Vector2(375, 479);
    Vector2 end11 = new Vector2(499, 120);
    Vector2 end12 = new Vector2(0, 120);
    Vector2 end13 = new Vector2(0, 360);
    Vector2 end14 = new Vector2(375, 0);
    Vector2 end15 = new Vector2(125, 0);
    Vector2 end16 = new Vector2(125, 479);

    Vector2[] line = {start, end};
    Vector2[] line2 = {start, end2};
    Vector2[] line3 = {start, end3};
    Vector2[] line4 = {start, end4};
    Vector2[] line5 = {start, end5};
    Vector2[] line6 = {start, end6};
    Vector2[] line7 = {start, end7};
    Vector2[] line8 = {start, end8};
    Vector2[] line9 = {start, end9};
    Vector2[] line10 = {start, end10};
    Vector2[] line11 = {start, end11};
    Vector2[] line12 = {start, end12};
    Vector2[] line13 = {start, end13};
    Vector2[] line14 = {start, end14};
    Vector2[] line15 = {start, end15};
    Vector2[] line16 = {start, end16};

    simpleRenderer.drawPlainLine(line, RGBA.red);
    simpleRenderer.drawPlainLine(line2, RGBA.red);
    simpleRenderer.drawPlainLine(line3, RGBA.red);
    simpleRenderer.drawPlainLine(line4, RGBA.red);
    simpleRenderer.drawPlainLine(line5, RGBA.red);
    simpleRenderer.drawPlainLine(line6, RGBA.red);
    simpleRenderer.drawPlainLine(line7, RGBA.red);
    simpleRenderer.drawPlainLine(line8, RGBA.red);
    simpleRenderer.drawPlainLine(line9, RGBA.red);
    simpleRenderer.drawPlainLine(line10, RGBA.red);
    simpleRenderer.drawPlainLine(line11, RGBA.red);
    simpleRenderer.drawPlainLine(line12, RGBA.red);
    simpleRenderer.drawPlainLine(line13, RGBA.red);
    simpleRenderer.drawPlainLine(line14, RGBA.red);
    simpleRenderer.drawPlainLine(line15, RGBA.red);
    simpleRenderer.drawPlainLine(line16, RGBA.red);

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

    simpleRenderer.drawPlainTriangle(new Vector2[]{new Vector2(30, 30), new Vector2(30, 150), new Vector2(150, 300)}, RGBA.red);

    try {
      ImageUtils.write(simpleRenderer.getImg(), "triangleRasterExample.png");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void drawInterpolatedTriangles() {
    SimpleRenderer renderer = new SimpleRenderer(500, 480, new InterpolatedColorShader());

    //TODO: Blatt 1, Aufgabe 5

    renderer.drawTriangle(new Vector2[]{new Vector2(250, 120), new Vector2(125, 360)
            , new Vector2(375, 361)}, new RGBA[]{RGBA.red, RGBA.green, RGBA.blue});

    renderer.drawLine(new Vector2[]{new Vector2(250, 50), new Vector2(250, 400)}
            , new RGBA[]{RGBA.red, RGBA.blue});

    renderer.drawLine(new Vector2[]{new Vector2(50, 300), new Vector2(400, 300)}
            , new RGBA[]{RGBA.red, RGBA.blue});

    renderer.drawLine(new Vector2[]{new Vector2(1, 1), new Vector2(10, 1)}
            , new RGBA[]{RGBA.red, RGBA.blue});
    try {
      ImageUtils.write(renderer.getImg(), "interpolated.png");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
