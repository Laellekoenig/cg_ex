package projection;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;

import testSuite.Scaling;
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
    Matrix4 currentView = null;

    //TODO: Blatt 3, Aufgabe 2

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

    updateView(azimuth, elevation);
  }

  private double handleAzimuth(int newMouseX, double azimuth) {

    //TODO: Blatt 3, Aufgabe 2

    return azimuth;
  }

  private double handleElevation(int newMouseY, double elevation) {

    //TODO: Blatt 3, Aufgabe 2

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
