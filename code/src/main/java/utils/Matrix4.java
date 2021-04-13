package utils;

public class Matrix4 {

  public static Matrix4 multiply(Matrix4 a, Matrix4 b) {
    if (a == null || b == null) {
      return null;
    }

    return new Matrix4(
        a.m[0] * b.m[0] + a.m[4] * b.m[1] + a.m[8] * b.m[2] + a.m[12] * b.m[3],      // ROW 1
        a.m[0] * b.m[4] + a.m[4] * b.m[5] + a.m[8] * b.m[6] + a.m[12] * b.m[7],
        a.m[0] * b.m[8] + a.m[4] * b.m[9] + a.m[8] * b.m[10] + a.m[12] * b.m[11],
        a.m[0] * b.m[12] + a.m[4] * b.m[13] + a.m[8] * b.m[14] + a.m[12] * b.m[15],

        a.m[1] * b.m[0] + a.m[5] * b.m[1] + a.m[9] * b.m[2] + a.m[13] * b.m[3],      // ROW 2
        a.m[1] * b.m[4] + a.m[5] * b.m[5] + a.m[9] * b.m[6] + a.m[13] * b.m[7],
        a.m[1] * b.m[8] + a.m[5] * b.m[9] + a.m[9] * b.m[10] + a.m[13] * b.m[11],
        a.m[1] * b.m[12] + a.m[5] * b.m[13] + a.m[9] * b.m[14] + a.m[13] * b.m[15],

        a.m[2] * b.m[0] + a.m[6] * b.m[1] + a.m[10] * b.m[2] + a.m[14] * b.m[3],     // ROW 3
        a.m[2] * b.m[4] + a.m[6] * b.m[5] + a.m[10] * b.m[6] + a.m[14] * b.m[7],
        a.m[2] * b.m[8] + a.m[6] * b.m[9] + a.m[10] * b.m[10] + a.m[14] * b.m[11],
        a.m[2] * b.m[12] + a.m[6] * b.m[13] + a.m[10] * b.m[14] + a.m[14] * b.m[15],

        a.m[3] * b.m[0] + a.m[7] * b.m[1] + a.m[11] * b.m[2] + a.m[15] * b.m[3],     // ROW 4
        a.m[3] * b.m[4] + a.m[7] * b.m[5] + a.m[11] * b.m[6] + a.m[15] * b.m[7],
        a.m[3] * b.m[8] + a.m[7] * b.m[9] + a.m[11] * b.m[10] + a.m[15] * b.m[11],
        a.m[3] * b.m[12] + a.m[7] * b.m[13] + a.m[11] * b.m[14] + a.m[15] * b.m[15]);
  }

  public Vector4 multiply(Vector4 v) {
    return new Vector4(
        m[0] * v.x + m[4] * v.y + m[8] * v.z + m[12] * v.w,
        m[1] * v.x + m[5] * v.y + m[9] * v.z + m[13] * v.w,
        m[2] * v.x + m[6] * v.y + m[10] * v.z + m[14] * v.w,
        m[3] * v.x + m[7] * v.y + m[11] * v.z + m[15] * v.w);
  }


  protected double m[];


  public Matrix4() {
    m = new double[16];
    loadIdentity();
  }

  public Matrix4(Matrix4 d) {
    m = new double[16];
    for (int i = 0; i < 16; i++) {
      m[i] = d.m[i];
    }
  }

  public Matrix4(double m0, double m4, double m8, double m12,
      double m1, double m5, double m9, double m13,
      double m2, double m6, double m10, double m14,
      double m3, double m7, double m11, double m15) {
    m = new double[16];
    m[0] = m0;
    m[1] = m1;
    m[2] = m2;
    m[3] = m3;
    m[4] = m4;
    m[5] = m5;
    m[6] = m6;
    m[7] = m7;
    m[8] = m8;
    m[9] = m9;
    m[10] = m10;
    m[11] = m11;
    m[12] = m12;
    m[13] = m13;
    m[14] = m14;
    m[15] = m15;
  }


  private Matrix4 loadIdentity() {
    m[0] = 1;
    m[1] = 0;
    m[2] = 0;
    m[3] = 0;
    m[4] = 0;
    m[5] = 1;
    m[6] = 0;
    m[7] = 0;
    m[8] = 0;
    m[9] = 0;
    m[10] = 1;
    m[11] = 0;
    m[12] = 0;
    m[13] = 0;
    m[14] = 0;
    m[15] = 1;
    return this;
  }

  public Vector4 times(Vector4 v) {
    return new Vector4(
        m[0] * v.x + m[4] * v.y + m[8] * v.z + m[12] * v.w,
        m[1] * v.x + m[5] * v.y + m[9] * v.z + m[13] * v.w,
        m[2] * v.x + m[6] * v.y + m[10] * v.z + m[14] * v.w,
        m[3] * v.x + m[7] * v.y + m[11] * v.z + m[15] * v.w);
  }

  private Matrix4 multiply(double d) {
    for (int i = 0; i < 16; i++) {
      m[i] *= d;
    }
    return this;
  }

  public double get(int row, int col) {
    return m[col * 4 + row];
  }

  public void set(int row, int col, double val) {
    m[col * 4 + row] = val;
  }

  public Matrix3 extract3x3() {
    return new Matrix3(m[0], m[4], m[8],
        m[1], m[5], m[9],
        m[2], m[6], m[10]);
  }

  public Matrix4 inverted() {
    Matrix4 mat = new Matrix4(this);
    mat.invert();
    return mat;
  }

  private void invert() {
    double[][] A = new double[4][4];
    for (int i = 0; i < 16; i++) {
      A[i % 4][i / 4] = m[i];
    }

    set(0, 0, Determinants
        .det3x3(A[1][1], A[2][1], A[3][1], A[1][2], A[2][2], A[3][2], A[1][3], A[2][3], A[3][3]));
    set(1, 0, -Determinants
        .det3x3(A[1][0], A[2][0], A[3][0], A[1][2], A[2][2], A[3][2], A[1][3], A[2][3], A[3][3]));
    set(2, 0, Determinants
        .det3x3(A[1][0], A[2][0], A[3][0], A[1][1], A[2][1], A[3][1], A[1][3], A[2][3], A[3][3]));
    set(3, 0, -Determinants
        .det3x3(A[1][0], A[2][0], A[3][0], A[1][1], A[2][1], A[3][1], A[1][2], A[2][2], A[3][2]));

    set(0, 1, -Determinants
        .det3x3(A[0][1], A[2][1], A[3][1], A[0][2], A[2][2], A[3][2], A[0][3], A[2][3], A[3][3]));
    set(1, 1, Determinants
        .det3x3(A[0][0], A[2][0], A[3][0], A[0][2], A[2][2], A[3][2], A[0][3], A[2][3], A[3][3]));
    set(2, 1, -Determinants
        .det3x3(A[0][0], A[2][0], A[3][0], A[0][1], A[2][1], A[3][1], A[0][3], A[2][3], A[3][3]));
    set(3, 1, Determinants
        .det3x3(A[0][0], A[2][0], A[3][0], A[0][1], A[2][1], A[3][1], A[0][2], A[2][2], A[3][2]));

    set(0, 2, Determinants
        .det3x3(A[0][1], A[1][1], A[3][1], A[0][2], A[1][2], A[3][2], A[0][3], A[1][3], A[3][3]));
    set(1, 2, -Determinants
        .det3x3(A[0][0], A[1][0], A[3][0], A[0][2], A[1][2], A[3][2], A[0][3], A[1][3], A[3][3]));
    set(2, 2, Determinants
        .det3x3(A[0][0], A[1][0], A[3][0], A[0][1], A[1][1], A[3][1], A[0][3], A[1][3], A[3][3]));
    set(3, 2, -Determinants
        .det3x3(A[0][0], A[1][0], A[3][0], A[0][1], A[1][1], A[3][1], A[0][2], A[1][2], A[3][2]));

    set(0, 3, -Determinants
        .det3x3(A[0][1], A[1][1], A[2][1], A[0][2], A[1][2], A[2][2], A[0][3], A[1][3], A[2][3]));
    set(1, 3, Determinants
        .det3x3(A[0][0], A[1][0], A[2][0], A[0][2], A[1][2], A[2][2], A[0][3], A[1][3], A[2][3]));
    set(2, 3, -Determinants
        .det3x3(A[0][0], A[1][0], A[2][0], A[0][1], A[1][1], A[2][1], A[0][3], A[1][3], A[2][3]));
    set(3, 3, Determinants
        .det3x3(A[0][0], A[1][0], A[2][0], A[0][1], A[1][1], A[2][1], A[0][2], A[1][2], A[2][2]));

    double det = (A[0][0] * get(0, 0)) + (A[0][1] * get(1, 0)) +
        (A[0][2] * get(2, 0)) + (A[0][3] * get(3, 0));

    double oodet = 1 / det;

    multiply(oodet);
  }

  public String toString() {
    StringBuilder builder = new StringBuilder("[");
    boolean first = true;

    for (int r = 0; r < 4; r++) {

      StringBuilder row = new StringBuilder();
      if (first) {
        first = false;
      } else {
        row.append(" ");
      }

      for (int c = 0; c < 4; c++) {
        row.append(get(r, c));
        row.append(" ");
      }

      builder.append(row);
      builder.append("\n");
    }

    builder.deleteCharAt(builder.length() - 1);
    builder.delete(builder.length() - 1, builder.length());
    builder.append("]");

    return builder.toString();
  }
}
