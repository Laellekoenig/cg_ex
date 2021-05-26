package raytracing;

import illumination.DirectionalLight;
import image.Image;
import image.RGBA;
import projection.PinholeProjection;

import projection.TurnableRenderer;
import utils.*;

import java.util.Optional;
import java.util.function.BiPredicate;

public class RayTracer implements TurnableRenderer {

  private final int width;
  private final int height;

  private final PinholeProjection projection;

  // Beleuchtung und Environment Map
  private Optional<EnvironmentMap<RGBA>> environmentMap = Optional.empty();
  private Optional<DirectionalLight> lightSource = Optional.empty();

  private double ambientLight = 0.28;

  // Ray Tracing und Rekursionstiefe
  private boolean rayTracingEnabled = false;
  private int rayTraceDepth = 5;

  // Reflexionen
  private boolean roughReflectionsEnabled = false;
  private double roughReflectionRoughness = 0.05;
  private int roughReflectionSamples = 10;

  // Schatten
  private boolean shadowsEnabled = false;
  private boolean softShadowsEnabled = false;
  private int softShadowSamples = 20;
  private double shadowSoftness = 0.05;

  // Tiefenschärfe
  private boolean depthOfFieldEnabled = false;
  private int depthOfFieldSamples = 20;
  private double depthOfFieldFocalLength = 5.0;
  private double depthOfField = 0.05;

  // Adaptives Supersampling
  private boolean adaptiveSupersamplingEnabled = false;
  private double adaptiveSupersamplingThreshold = 0.3;
  private int adaptiveSupersamplingSamples = 3;

  private Scene scene = Scene.empty();


  public RayTracer(int width, int height, PinholeProjection projection) {
    this.width = width;
    this.height = height;

    this.projection = projection;
  }

  public Image<RGBA> render() {
    RandomHelper.resetRandomSeed();
    Image<RGBA> framebuffer = new Image<RGBA>(width, height);

    //TODO: Blatt 5, Aufgabe 2

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        framebuffer.set(x, y, followRay(x, y));
      }
    }

    if (adaptiveSupersamplingEnabled) {
      //TODO: Blatt 5, Aufgabe 7 c)
    }

    return framebuffer;
  }

  private RGBA followRay(double x, double y) {
    Matrix4 invertedProjection = projection.getMatrix().inverted();

    //TODO: Blatt 5, Aufgabe 2

    // arbitrary depth of near-clipping plane in direction of negative z-axis
    double z = -1;

    // transform the 2d pixel space into the near clipping plane in 3d world space
    Vector4 homogPixelLocation = new Vector4(-x, -y,  z, 1);
    Vector4 homogPoint = invertedProjection.times(homogPixelLocation);
    Vector3 point = new Vector3(homogPoint.x, homogPoint.y, homogPoint.z);

    // transform the origin into world space
    Vector4 screenHomogOrigin = new Vector4(0, 0, 0, 1);
    Vector4 worldHomogOrigin = invertedProjection.times(screenHomogOrigin);
    Vector3 origin = new Vector3(worldHomogOrigin.x, worldHomogOrigin.y, worldHomogOrigin.z);

    // create a ray from the origin to the pixel (x, y) represented on the near clipping plane in world space
    Ray ray = Ray.fromEndPoints(origin, point);

    RGBA color = followRay(rayTraceDepth, ray);

    if (depthOfFieldEnabled) {
      //TODO: Blatt 5, Aufgabe 7 b)
    } else {
      color = followRay(rayTraceDepth, ray);
    }

    return color;
  }

  private RGBA followRay(int depth, Ray ray) {
    return followRay(depth, ray, 0.001);
  }

  private RGBA followRay(int depth, Ray ray, double eps) {
    //TODO: Blatt 5, Aufgabe 2

    Optional<RayCastResult> optResult = scene.rayCastScene(ray, eps);

    RGBA color;

    if (optResult.isPresent()) {
      RayCastResult result = optResult.get();

      Intersection intersection = result.intersection;

      SceneObject object = result.object;

      RayTracingMaterial material = object.getMaterial();

      if (lightSource.isPresent()) {
        RGBA c = material.getColor();
        Vector3 n = intersection.normal;
        RGBA a = material.getAmbientColor();
        double I_a = ambientLight;
        double I_l = getLightContribution(ray.direction, n, eps);
        color = c.times(I_l).plus(a.times(I_a));
      } else {
        color = material.getColor();
      }

      if (depth > 0 && rayTracingEnabled) {
        Vector3 point = ray.origin.plus(ray.direction);
        color.plus(getReflectionTerm(ray, point, intersection.normal, material, depth, eps));
      }

    } else {
      color = RGBA.grey;
    }

    //TODO: Blatt 5, Aufgabe 3
    //TODO: Blatt 5, Aufgabe 4
    //TODO: Blatt 5, Aufgabe 6
    //TODO: Blatt 5, Aufgabe 7 a)

    return color;
  }

  private RGBA getReflectionTerm(Ray ray, Vector3 point, Vector3 normal,
      RayTracingMaterial material, int depth, double eps) {
    //TODO: Blatt 5, Aufgabe 3

    // breaking conditions
    if (!rayTracingEnabled) return new RGBA(0, 0, 0);;
    if (depth == 0) return new RGBA(0, 0, 0);;

    // get outgoing vector
    //http://particle.uni-wuppertal.de/vorkurse/Physik06/optik1.pdf S. 4
    double en = ray.direction.dot(normal);
    double n2 = normal.dot(normal);
    Vector3 pn = normal.times(en / n2);
    //vector after perfect reflexion
    Vector3 r = ray.direction.minus(pn.times(2));

    // create new ray and cast it
    Ray nextRay = Ray.fromEndPoints(point, r);
    Optional<RayCastResult> optResult = scene.rayCastScene(nextRay, eps);

    if (optResult.isPresent()) {
      RayCastResult result = optResult.get();
      Intersection intersection = result.intersection;
      SceneObject object = result.object;
      RayTracingMaterial nextMaterial = object.getMaterial();

      RGBA reflectanceColor = nextMaterial.getColor().multElementWise(nextMaterial.getReflectance());

      Vector3 nextPoint = nextRay.origin.plus(nextRay.direction);
      return reflectanceColor.plus(getReflectionTerm(nextRay, nextPoint, intersection.normal, nextMaterial, depth - 1, eps));
    }

    return new RGBA(0, 0, 0);
  }

  private RGBA getRefractionTerm(Ray ray, Vector3 point, Vector3 normal,
      RayTracingMaterial material, int depth, double eps) {
    if (!material.isTransparent()) {
      return RGBA.black;
    }

    //TODO: Blatt 5, Aufgabe 6
    return null;
  }

  private double getLightContribution(Vector3 point, Vector3 normal, double eps) {
    if (!lightSource.isPresent()) {
      return 1;
    }

    //TODO: Blatt 5, Aufgabe 2

    double I_l = Math.max(- normal.dot(lightSource.get().direction), 0);

    //TODO: Blatt 5, Aufgabe 5a)
    //TODO: Blatt 5, Aufgabe 5b)
    return I_l;
  }


  // TurnableRenderer Interface methods
  @Override
  public void setProjectionView(Matrix4 currentView) {
    if (projection != null) {
      projection.setView(currentView);
    }
  }

  @Override
  public void rotateLights(Matrix4 rotation) {

  }

  // region Getters & Setters...
  public PinholeProjection getProjection() {
    return projection;
  }

  public Optional<EnvironmentMap<RGBA>> getEnvironmentMap() {
    return environmentMap;
  }

  public void setEnvironmentMap(Optional<EnvironmentMap<RGBA>> environmentMap) {
    this.environmentMap = environmentMap;
  }

  public Optional<DirectionalLight> getLightSource() {
    return lightSource;
  }

  public void setLightSource(Optional<DirectionalLight> lightSource) {
    this.lightSource = lightSource;
  }

  public double getAmbientLight() {
    return ambientLight;
  }

  public void setAmbientLight(double ambientLight) {
    this.ambientLight = ambientLight;
  }

  public boolean isRayTracingEnabled() {
    return rayTracingEnabled;
  }

  public void setRayTracingEnabled(boolean rayTracingEnabled) {
    this.rayTracingEnabled = rayTracingEnabled;
  }

  public int getRayTraceDepth() {
    return rayTraceDepth;
  }

  public void setRayTraceDepth(int rayTraceDepth) {
    this.rayTraceDepth = rayTraceDepth;
  }

  public boolean isRoughReflectionsEnabled() {
    return roughReflectionsEnabled;
  }

  public void setRoughReflectionsEnabled(boolean roughReflectionsEnabled) {
    this.roughReflectionsEnabled = roughReflectionsEnabled;
  }

  public double getRoughReflectionRoughness() {
    return roughReflectionRoughness;
  }

  public void setRoughReflectionRoughness(double roughReflectionRoughness) {
    this.roughReflectionRoughness = roughReflectionRoughness;
  }

  public int getRoughReflectionSamples() {
    return roughReflectionSamples;
  }

  public void setRoughReflectionSamples(int roughReflectionSamples) {
    this.roughReflectionSamples = roughReflectionSamples;
  }

  public boolean isShadowsEnabled() {
    return shadowsEnabled;
  }

  public void setShadowsEnabled(boolean shadowsEnabled) {
    this.shadowsEnabled = shadowsEnabled;
  }

  public boolean isSoftShadowsEnabled() {
    return softShadowsEnabled;
  }

  public void setSoftShadowsEnabled(boolean softShadowsEnabled) {
    this.softShadowsEnabled = softShadowsEnabled;
  }

  public int getSoftShadowSamples() {
    return softShadowSamples;
  }

  public void setSoftShadowSamples(int softShadowSamples) {
    this.softShadowSamples = softShadowSamples;
  }

  public double getShadowSoftness() {
    return shadowSoftness;
  }

  public void setShadowSoftness(double shadowSoftness) {
    this.shadowSoftness = shadowSoftness;
  }

  public boolean isDepthOfFieldEnabled() {
    return depthOfFieldEnabled;
  }

  public void setDepthOfFieldEnabled(boolean depthOfFieldEnabled) {
    this.depthOfFieldEnabled = depthOfFieldEnabled;
  }

  public int getDepthOfFieldSamples() {
    return depthOfFieldSamples;
  }

  public void setDepthOfFieldSamples(int depthOfFieldSamples) {
    this.depthOfFieldSamples = depthOfFieldSamples;
  }

  public double getDepthOfFieldFocalLength() {
    return depthOfFieldFocalLength;
  }

  public void setDepthOfFieldFocalLength(double depthOfFieldFocalLength) {
    this.depthOfFieldFocalLength = depthOfFieldFocalLength;
  }

  public double getDepthOfField() {
    return depthOfField;
  }

  public void setDepthOfField(double depthOfField) {
    this.depthOfField = depthOfField;
  }

  public boolean isAdaptiveSupersamplingEnabled() {
    return adaptiveSupersamplingEnabled;
  }

  public void setAdaptiveSupersamplingEnabled(boolean adaptiveSupersamplingEnabled) {
    this.adaptiveSupersamplingEnabled = adaptiveSupersamplingEnabled;
  }

  public double getAdaptiveSupersamplingThreshold() {
    return adaptiveSupersamplingThreshold;
  }

  public void setAdaptiveSupersamplingThreshold(double adaptiveSupersamplingThreshold) {
    this.adaptiveSupersamplingThreshold = adaptiveSupersamplingThreshold;
  }

  public int getAdaptiveSupersamplingSamples() {
    return adaptiveSupersamplingSamples;
  }

  public void setAdaptiveSupersamplingSamples(int adaptiveSupersamplingSamples) {
    this.adaptiveSupersamplingSamples = adaptiveSupersamplingSamples;
  }

  public Scene getScene() {
    return scene;
  }

  public void setScene(Scene scene) {
    this.scene = scene;
  }

  // endregion
}
