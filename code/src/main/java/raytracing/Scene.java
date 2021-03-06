package raytracing;

import utils.Intersectable;
import utils.Intersection;
import utils.Ray;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

public class Scene {

  private final List<SceneObject> objects = new ArrayList<SceneObject>();

  public Scene(SceneObject... sceneObjects) {
    for (SceneObject object : sceneObjects) {
      addObject(object);
    }
  }

  /**
   * Casts a ray against all objects in the scene (i.e. objects list), and returns an Optional of
   * RayCastResult correspondin to the closest intersection of the ray with a scene object. If there
   * is no such intersection, Optional.empty is returned.
   *
   * @param ray The ray.
   * @param eps The minimum distance of an intersection to the ray's origin. Intersections that are
   *            closer to the origin than eps units shall be ignored.
   * @return Optional of RayCastResult representing the closest intersection; Optional.empty if
   * there is no intersection
   */
  public Optional<RayCastResult> rayCastScene(Ray ray, double eps) {
    //TODO: Blatt 5, Aufgabe 1

    // Copied from the slides

    double minT = Double.POSITIVE_INFINITY;
    Optional<SceneObject> closestObject = Optional.empty();

    SceneObject prevObject = objects.get(0);

    for (SceneObject object : objects) {
      Optional<Intersection> optIntersection = object.geometry.intersect(ray, eps);

      if (optIntersection.isPresent() && optIntersection.get().t < minT) {
        minT = optIntersection.get().t;
        closestObject = Optional.of(object);
      }
    }

    if (!closestObject.isPresent()) {
      //System.out.println("empty");
      return Optional.empty();
    }

    RayCastResult result = new RayCastResult(closestObject.get(), closestObject.get().geometry.intersect(ray, eps).get());

    return Optional.of(result);
  }

  /**
   * Similar to rayCastScene, but without guarantees regarding the distance of an intersection. I.e.
   * this method might return the first valid intersection instead of the one closest to the ray's
   * origin.
   * <p>
   * Use this method for shadowing.
   */
  public Optional<RayCastResult> rayCastSceneAny(Ray ray, double eps) {
    //TODO: Blatt 5, Aufgabe 5

    // intersect witrh any object
    for (SceneObject object : objects) {
      Optional<Intersection> optIntersection = object.geometry.intersect(ray, eps);

      if (optIntersection.isPresent()) {
        // first intersection found
        return Optional.of(new RayCastResult(object, object.geometry.intersect(ray, eps).get()));
      }
    }

    return Optional.empty();
  }

  public SceneObject addObject(Intersectable geometry, RayTracingMaterial material) {
    SceneObject newObject = new SceneObject(geometry, material);
    addObject(newObject);

    return newObject;
  }

  public void addObject(SceneObject sceneObject) {
    objects.add(sceneObject);
  }

  public void removeSceneObject(SceneObject sceneObject) {
    objects.remove(sceneObject);
  }

  public void clearSceneObjects() {
    objects.clear();
  }

  public static Scene empty() {
    return new Scene();
  }
}
