package raytracing;

import utils.Vector3;

public interface EnvironmentMap<T> {

  T access(Vector3 direction);

  static <T> EnvironmentMap<T> uniform(T constant) {
    return direction -> constant;
  }
}
