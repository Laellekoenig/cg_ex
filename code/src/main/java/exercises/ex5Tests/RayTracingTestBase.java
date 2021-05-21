package exercises.ex5Tests;

import image.*;
import image.Image;
import projection.PinholeProjection;
import raytracing.RayTracer;
import raytracing.RayTracingMaterial;
import raytracing.Scene;
import testSuite.testTemplates.InteractiveTest;
import testSuite.testTemplates.Visual3DTest;
import utils.Vector3;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class RayTracingTestBase extends Visual3DTest implements InteractiveTest {

  protected PinholeProjection projection;
  protected RayTracer rayTracer;

  private boolean setup = false;

  public RayTracingTestBase(String gsFileName, String title) {
    super(gsFileName, title);

    projection = new PinholeProjection(w, h);
    rayTracer = new RayTracer(w, h, projection);

    setupView();
    updateView();

    rayTracer.setScene(new Scene());
  }

  protected void setupView() {
    rotationX = toRadians(45);
    rotationY = toRadians(45);
    rotationZ = 0;
    translation = new Vector3(0, 0, -8);
  }

  private void setupTestIfNotSetup() {
    if (setup) {
      return;
    }

    setup = true;

    setupRayTracer();
    setupScene(new TestSceneBuilder(rayTracer.getScene()));

    updateView();
  }

  protected void setupRayTracer() {

  }

  protected void setupScene(TestSceneBuilder sceneBuilder) {
    sceneBuilder.addSphere(new Vector3(-2.8, 0, 0), 1.5,
        RayTracingMaterial.DefaultColored(RGBA.red.times(0.7)));
    sceneBuilder.addSphere(new Vector3(2.8, 0, 0), 1.5,
        RayTracingMaterial.DefaultColored(RGBA.yellow.times(0.7)));
    sceneBuilder.addCube(Vector3.zero, Vector3.one, TestSceneBuilder.COLORS);
  }

  public Image<RGBA> renderImage() {
    return rayTracer.render();
  }

  protected void _draw() {
    setupTestIfNotSetup();
    drawn = renderImage();
    fireDrawEvent();
  }

  @Override
  public PinholeProjection getProjection() {
    return projection;
  }

  protected JSpinner createSpinner(double initial, double min, double max, double step,
      Consumer<Double> onUpdate) {
    SpinnerModel model = new SpinnerNumberModel(initial, min, max, step);
    JSpinner spinner = new JSpinner(model);

    spinner.setPreferredSize(new Dimension(60, 30));
    spinner.addChangeListener(e -> {
      double value = (double) ((JSpinner) e.getSource()).getModel().getValue();
      onUpdate.accept(value);
      draw();
    });

    return spinner;
  }

  @Override
  public Component getWidget() {
    JPanel panel = new JPanel();
    buildWidget(panel);
    return panel;
  }

  protected void buildWidget(JPanel panel) {
    addRotationSpinners(panel);
  }

  protected void addRotationSpinners(JPanel panel) {
    panel.add(new JLabel("Rotation X: "));
    panel.add(createSpinner(toDegrees(rotationX), -360, 360, 5, r -> {
      rotationX = toRadians(r);
      updateView();
    }));

    panel.add(new JLabel("Rotation Y: "));
    panel.add(createSpinner(toDegrees(rotationY), -360, 360, 5, r -> {
      rotationY = toRadians(r);
      updateView();
    }));

    panel.add(new JLabel("Rotation Z: "));
    panel.add(createSpinner(toDegrees(rotationZ), -360, 360, 5, r -> {
      rotationZ = toRadians(r);
      updateView();
    }));
  }

  protected void addZSpinner(JPanel panel) {
    addZSpinner(panel, -10, 0);
  }

  protected void addZSpinner(JPanel panel, int min, int max) {
    panel.add(new JLabel("Camera Z-Offset: "));
    panel.add(createSpinner(translation.z, min, max, 0.5, r -> {
      translation = new Vector3(0, 0, 1).times(r);
      updateView();
    }));
  }

  protected void addRecursionDepthSpinner(JPanel panel) {
    panel.add(new Label("Recursion Depth"));
    panel.add(createSpinner(rayTracer.getRayTraceDepth(), 0, 10, 1,
        d -> rayTracer.setRayTraceDepth((int) (double) d)));
  }

  protected void updateView() {
    buildView();
    projection.setView(currentView);
  }

  private double toDegrees(double v) {
    return v / Math.PI * 180;
  }

  private double toRadians(double v) {
    return Math.PI / 180 * v;
  }
}
