package image;

import java.util.Arrays;

public class Image<T> {

  protected int w, h;
  private Object[] data; //generic array not possible because unchecked type parameter

  public Image(int w, int h) {
    this.w = w;
    this.h = h;
    this.data = new Object[w * h];
  }

  /**
   * Creates an image and fills it with defaultVal.
   */
  public Image(int w, int h, T defaultVal) {
    this.w = w;
    this.h = h;
    this.data = new Object[w * h];
    Arrays.fill(data, defaultVal);
  }

  public Image<T> clone() {
    Image<T> out = new Image<T>(w, h);
    out.data = data.clone();
    return out;
  }

  public T get(int x, int y) {
    return access(x, y);
  }

  public T get(int i) {
    return access(i);
  }

  public void set(int x, int y, T val) {
    data[y * w + x] = val;
  }

  public void set(int i, T val) {
    data[i] = val;
  }

  public int cols() {
    return w;
  }

  public int rows() {
    return h;
  }

  public int size() {
    return w * h;
  }

  /**
   * Clamped access: If the image is accessed outside its borders, the nearest border value is
   * returned.
   */
  @SuppressWarnings("unchecked")//unfortunately no other way
  private T access(int x, int y) {

    x = (x < 0) ? 0 : ((x >= w) ? w - 1 : x);
    y = (y < 0) ? 0 : ((y >= h) ? h - 1 : y);

    return (T) data[y * w + x];
  }

  /**
   * Clamped access
   */
  @SuppressWarnings("unchecked")//unfortunately no other way
  private T access(int i) {

    i = (i < 0) ? 0 : ((i >= w * h) ? w * h : i);

    return (T) data[i];
  }
}
