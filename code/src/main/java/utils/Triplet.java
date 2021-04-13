package utils;

public class Triplet {

  private int[] vals;

  public Triplet() {
    vals = new int[3];
  }

  public Triplet(int a, int b, int c) {
    vals = new int[]{a, b, c};
  }

  public int get(int idx) {
    return vals[idx];
  }

  public String toString() {
    return "(" + vals[0] + ", " + vals[1] + ", " + vals[2] + ")";
  }

}
