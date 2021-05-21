package utils;

import java.util.Optional;

public interface Intersectable {

  /**
   * Casts a ray against the object implementing this interface.
   *
   * @param ray  The ray.
   * @param near Intersections are ignored, if they are less than 'near' units away from the ray's
   *             origin.
   * @return Optional.empty if there is no intersection; Optional.of(new Intersection(...))
   * otherwise.
   */
  Optional<Intersection> intersect(final Ray ray, final double near);

}
