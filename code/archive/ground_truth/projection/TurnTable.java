package projection;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;

import testSuite.Scaling;
import utils.Matrix3;
import utils.Matrix4;
import utils.Vector3;

public class TurnTable implements MouseListener, MouseMotionListener {

  private TurnableRenderer renderer;
  private TurnableTest test;
  private int w, h, mouseX, mouseY;
  private Vector3 translation;
  public double elevation, azimuth, zoomStep;

  private Matrix4 currentView;

  public TurnTable(TurnableRenderer renderer, TurnableTest test, int w, int h) {
    this.w = w;
    this.h = h;
    this.renderer = renderer;
    this.test = test;
    this.elevation = test.getRotationX();
    this.azimuth = test.getRotationY();
    this.currentView = test.getView();
    this.translation = test.getTranslation();
    zoomStep = 0.2;
    updateView(azimuth, elevation);
  }

  private void updateView(double azimuth, double elevation) {
    currentView = buildViewMatrix(azimuth, elevation, translation);
    setRendererViewMatrix(currentView);
  }

  private Matrix4 buildViewMatrix(double azimuth, double elevation, Vector3 translation) {
    Matrix4 currentView;

    //TODO: Blatt 3, Aufgabe 2

    // It's good to know that there's always a complicated alternative: Manual multiplication of the two quaternions
    // each representing the rotation around one axis... needed to implement it this way in order to understand that
    // one gets exactly the same result when multiplying the two pure rotation matrices, as done below.
    /*
    double s = Math.cos(azimuth / 2) * Math.cos(elevation / 2);
    Vector3 v = new Vector3(0, Math.cos(azimuth / 2) * Math.sin(elevation / 2), 0).plus(new Vector3(0, 0
            , Math.cos(elevation / 2) * Math.sin(azimuth / 2)))
            .plus(new Vector3(-1 * Math.sin((azimuth / 2)) * Math.sin((elevation / 2)), 0, 0));

    Matrix4 matrix = new Matrix4();

    matrix.set(0, 0, 1 - (2 * Math.pow(v.y, 2) + 2 * Math.pow(v.z, 2)));
    matrix.set(0, 1, 2 * v.x * v.y - 2 * s * v.z);
    matrix.set(0, 2, 2 * v.x * v.z + 2 * s * v.y);

    matrix.set(1, 0, 2 * v.x * v.y + 2 * s * v.z);
    matrix.set(1, 1, 1 - (2 * Math.pow(v.x, 2) + 2 * Math.pow(v.z, 2)));
    matrix.set(1, 2, 2 * v.y * v.z - 2 * s * v.x);

    matrix.set(2, 0, 2 * v.x * v.z - 2 * s * v.y);
    matrix.set(2, 1, 2 * v.y * v.z + 2 * s * v.x);
    matrix.set(2, 2, 1 - (2 * Math.pow(v.x, 2) + 2 * Math.pow(v.y, 2)));

    matrix.set(0, 3, translation.x);
    matrix.set(1, 3, translation.y);
    matrix.set(2, 3, translation.z);
    */

    Matrix4 azimuthMatrix = Projection.getRotationZ(azimuth);
    Matrix4 elevationMatrix = Projection.getRotationY(elevation);

    currentView = Matrix4.multiply(azimuthMatrix, elevationMatrix);

    currentView.set(0, 3, translation.x);
    currentView.set(1, 3, translation.y);
    currentView.set(2, 3, translation.z);

    //System.out.printf("ViewMatrix by multiplication: \n %s \n", currentView);
    //System.out.printf("ViewMatrix by quaternion: \n %s \n", matrix);

    return currentView;
  }

  private void setRendererViewMatrix(Matrix4 currentView) {
    renderer.setProjectionView(currentView);
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    if (e.isShiftDown()) {
      zoom(e.getY() < mouseY);
      mouseY = e.getY();
    } else {
      if (SwingUtilities.isRightMouseButton(e)) {
        double lazimuth = handleAzimuth((int) (e.getX() / Scaling.getFactor()), 0);
        double lelevation = handleElevation((int) (e.getY() / Scaling.getFactor()), 0);

        Matrix4 lrot = buildViewMatrix(lazimuth, lelevation, new Vector3(0, 0, 0));
        renderer.rotateLights(lrot);
      } else {
        azimuth = handleAzimuth((int) (e.getX() / Scaling.getFactor()), azimuth);
        elevation = handleElevation((int) (e.getY() / Scaling.getFactor()), elevation);

        updateView(azimuth, elevation);
      }
    }
    test.draw();
  }

  @Override
  public void mousePressed(MouseEvent e) {
    mouseX = (int) (e.getX() / Scaling.getFactor());
    mouseY = (int) (e.getY() / Scaling.getFactor());
  }

  @Override
  public void mouseMoved(MouseEvent e) {
  }

  @Override
  public void mouseClicked(MouseEvent e) {
  }

  @Override
  public void mouseReleased(MouseEvent e) {
  }

  @Override
  public void mouseEntered(MouseEvent e) {
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }

  private void zoom(boolean in) {

    //TODO: Blatt 3, Aufgabe 2

    if (in)   translation = translation.plus(new Vector3(0, 0, zoomStep));
    else      translation = translation.minus(new Vector3(0, 0, zoomStep));

    updateView(azimuth, elevation);
  }

  private double handleAzimuth(int newMouseX, double azimuth) {

    //TODO: Blatt 3, Aufgabe 2

    int difference = Math.abs(newMouseX - mouseX);

    double percentage = (float) difference / w;

    if (percentage > 1) {
      System.out.println("Error with percentage!");
    }

    if (mouseX < newMouseX)   azimuth -= Math.PI * percentage;
    else                      azimuth += Math.PI * percentage;

    return azimuth;
  }

  private double handleElevation(int newMouseY, double elevation) {

    //TODO: Blatt 3, Aufgabe 2

    int difference = Math.abs(newMouseY - mouseY);

    double percentage = (float) difference / h;

    if (percentage > 1) {
      System.out.println("Error with percentage!");
    }

    if (mouseY < newMouseY)   elevation -= Math.PI * percentage;
    else                      elevation += Math.PI * percentage;

    return elevation;
  }

  public void reset() {
    this.elevation = test.getRotationX();
    this.azimuth = test.getRotationY();
    this.currentView = test.getView();
    this.translation = test.getTranslation();
    this.zoomStep = 0.2;
    updateView(azimuth, elevation);
  }

  public String toString() {
    return "rotationX: " + elevation +
            " rotationY: " + azimuth +
            " translation: (" + translation.x + ", " + translation.y + ", " + translation.z + ")";
  }
}