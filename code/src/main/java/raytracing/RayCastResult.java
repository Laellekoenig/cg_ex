package raytracing;

import utils.Intersection;

/**
 * Stores information about the intersection of a ray and a scene object.
 */
public class RayCastResult {

  /**
   * The scene object that is hit by the ray.
   */
  public final SceneObject object;

  /**
   * Geometric information about the actual intersection.
   */
  public final Intersection intersection;

  RayCastResult(SceneObject object, Intersection intersection) {
    this.object = object;
    this.intersection = intersection;
  }
}
