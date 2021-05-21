package raytracing;

import utils.*;

import java.util.Optional;

/**
 * This class brings the geometric properties of an object and its material together. It implements
 * the Intersectable interface by delegating class to intersect to its geometry object.
 */
public class SceneObject implements Intersectable {

  protected RayTracingMaterial material;

  protected Intersectable geometry;

  public SceneObject(Intersectable geometry, RayTracingMaterial material) {
    setGeometry(geometry);
    setMaterial(material);
  }

  @Override
  public Optional<Intersection> intersect(Ray ray, double near) {
    return getGeometry().intersect(ray, near);
  }

  // Getters & Setters...
  public RayTracingMaterial getMaterial() {
    return material;
  }

  public void setMaterial(RayTracingMaterial material) {
    this.material = material;
  }

  public Intersectable getGeometry() {
    return geometry;
  }

  public void setGeometry(Intersectable geometry) {
    this.geometry = geometry;
  }

}
