package image.processing.warping;

import image.Image;
import image.RGBA;
import image.processing.ImageAlgorithm;
import image.processing.scaling.BiLinearInterpolation;
import image.processing.scaling.Interpolation;
import utils.Vector2;
import utils.VectorOperations;

public class BackwardWarp implements ImageAlgorithm {

  private Image<Vector2> flowField;

  public BackwardWarp(Image<Vector2> flowField) {
    this.flowField = flowField;
  }

  public BackwardWarp() {
    super();
  }

  @Override
  public Image<RGBA> perform(Image<RGBA> img) {
    Image<RGBA> outImg = new Image<>(img.cols(), img.rows());
    Interpolation method = new BiLinearInterpolation();
    method.setImage(img);

    //TODO: Blatt 2, Aufgabe 3 a)

    return outImg;
  }

  public void createSwirlField(int w, int h, double radius, double strength) {
    Vector2 center = new Vector2(w / 2f, h / 2f);
    flowField = new Image<>(w, h);

    for (int x = 0; x < w; x++) {
      for (int y = 0; y < h; y++) {

        Vector2 dest = new Vector2(x, y);

        double theta = Math.atan2(y - center.y, x - center.x);
        double roh = VectorOperations.distance(dest, center);

        double r = Math.log(2) * radius / 5;

        double thetaPrime = strength * Math.exp(-roh / r) + theta;
        Vector2 source = new Vector2(center.x + roh * Math.cos(thetaPrime) - x,
            center.y + roh * Math.sin(thetaPrime) - y);

        flowField.set(x, y, source);
      }
    }
  }

  public void createRotationField(int w, int h, double theta) {
    Vector2 center = new Vector2(w / 2f, h / 2f);
    flowField = new Image<>(w, h);

    for (int x = 0; x < w; x++) {
      for (int y = 0; y < h; y++) {

        Vector2 source = invRotate(new Vector2(x - center.x, y - center.y), theta);
        flowField.set(x, y, new Vector2(source.x + center.x - x, source.y + center.y - y));
      }
    }
  }

  private Vector2 invRotate(Vector2 v, double theta) {
    double xRotated = v.x * Math.cos(-theta) - v.y * Math.sin(-theta);
    double yRotated = v.x * Math.sin(-theta) + v.y * Math.cos(-theta);
    return new Vector2(xRotated, yRotated);
  }
}
