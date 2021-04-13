package utils;

public class VectorOperations {

  public static <T extends VectorSpace<T>> T add(T a, T b) {
    return a.plus(b);
  }

  public static <T extends VectorSpace<T>> T subtract(T a, T b) {
    return a.minus(b);
  }

  public static <T extends VectorSpace<T>> T multiply(T a, double b) {
    return a.times(b);
  }


  // specialization
  public static Vector3 cross(Vector3 a, Vector3 b) {
    return new Vector3(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x);
  }


  public static <T extends VectorSpace<T>> double distance(T a, T b) {
    return a.minus(b).length();
  }
}
