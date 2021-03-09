package image;

import utils.VectorSpace;

public class RGBA implements VectorSpace<RGBA> {

  //r, g, b, a in range [0, 1]
  public float r, g, b, a;

  public RGBA(double r, double g, double b, double a) {
    this.r = (float) r;
    this.g = (float) g;
    this.b = (float) b;
    this.a = (float) a;
  }

  public RGBA(double r, double g, double b) {
    this(r, g, b, 1f); //not transparent per default
  }

  public RGBA(RGBA o) {
    this(o.r, o.g, o.b, o.a);
  }

  /**
   * Unpack an integer that codes RGBA values the way Java's BufferedImages does it.
   */
  public RGBA(int rgb) {
    int red = (rgb >> 16) & 0xFF;
    int green = (rgb >> 8) & 0xFF;
    int blue = (rgb) & 0xFF;
    int alpha = (rgb >> 24) & 0xFF;

    r = red / 255f;
    g = green / 255f;
    b = blue / 255f;
    a = alpha / 255f;
  }

  public static RGBA min(RGBA c0, RGBA c1) {
    float r = Math.min(c0.r, c1.r);
    float g = Math.min(c0.g, c1.g);
    float b = Math.min(c0.b, c1.b);
    return new RGBA(r, g, b);
  }

  public static RGBA max(RGBA c0, RGBA c1) {
    float r = Math.max(c0.r, c1.r);
    float g = Math.max(c0.g, c1.g);
    float b = Math.max(c0.b, c1.b);
    return new RGBA(r, g, b);

  }

  @Override
  public RGBA times(double factor) {
    return new RGBA(r * factor, g * factor, b * factor, 1f);
  }

  @Override
  public double dot(RGBA o) {
    return r * o.r + g * o.g + b * o.b; // ignore alpha?
  }

  @Override
  public RGBA plus(RGBA o) {
    return new RGBA(r + o.r, g + o.g, b + o.b, a);
  }

  @Override
  public RGBA minus(RGBA o) {
    return new RGBA(r - o.r, g - o.g, b - o.b, a);
  }

  // in place!
  public void minusAbs(RGBA o) {
    r = Math.abs(r - o.r);
    g = Math.abs(g - o.g);
    b = Math.abs(b - o.b);
  }

  @Override
  public RGBA multElementWise(RGBA o) {
    return new RGBA(r * o.r, g * o.g, b * o.b, a);
  }

  public RGBA divide(RGBA o) {
    return new RGBA(r / o.r, g / o.g, b / o.b, a);
  }

  /**
   * Encodes the RGBA values into a single integer (32 bit) the way a BufferedImage can use it.
   * Coding: 00000000 00000000 00000000 11111111 ^Alpha   ^Red     ^Green   ^Blue
   */
  public int pack() {
    int rgb = 0;
    rgb = rgb | (int) (b * 255);
    rgb = rgb | ((int) (g * 255) << 8);
    rgb = rgb | ((int) (r * 255) << 16);
    rgb = rgb | ((int) (a * 255) << 24);

    return rgb;

  }

  public void clamp() {
    r = (r < 0) ? 0 : ((r > 1) ? 1 : r);
    g = (g < 0) ? 0 : ((g > 1) ? 1 : g);
    b = (b < 0) ? 0 : ((b > 1) ? 1 : b);
    a = (a < 0) ? 0 : ((a > 1) ? 1 : a);
  }

  public RGBA clone() {
    return new RGBA(this);
  }

  public String toString() {
    return "(" + r + ", " + g + ", " + b + ")";
  }

  @Override
  public RGBA neg() {
    return new RGBA(-r, -g, -b, -a);
  }

  @Override
  public double length() {
    return Math.abs(r * r + g * g + b * b);
  }

  // Some Shortcuts
  public static final RGBA white = new RGBA(1, 1, 1, 1);
  public static final RGBA black = new RGBA(0, 0, 0, 1);
  public static final RGBA grey = new RGBA(.5, .5, .5, 1);
  public static final RGBA red = new RGBA(1, 0, 0, 1);
  public static final RGBA green = new RGBA(0, 1, 0, 1);
  public static final RGBA blue = new RGBA(0, 0, 1, 1);
  public static final RGBA cyan = new RGBA(0, 1, 1, 1);
  public static final RGBA yellow = new RGBA(1, 1, 0, 1);
  public static final RGBA magenta = new RGBA(1, 0, 1, 1);

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (!(obj instanceof RGBA)) {
      return false;
    }

    RGBA that = (RGBA) obj;
    double epsilon = 0.02;
    double rDiff = Math.abs(this.r - that.r);
    double gDiff = Math.abs(this.g - that.g);
    double bDiff = Math.abs(this.b - that.b);
    return rDiff + gDiff + bDiff < epsilon;
  }
}
