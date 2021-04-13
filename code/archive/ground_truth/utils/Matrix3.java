package utils;

public class Matrix3 {

  protected double m[];

  public Matrix3() {
    m = new double[9];
    loadIdentity();
  }

  public Matrix3(Matrix3 o) {
    m = o.m.clone();
  }

  protected void loadIdentity() {
    m[0] = 1;
    m[1] = 0;
    m[2] = 0;
    m[3] = 0;
    m[4] = 1;
    m[5] = 0;
    m[6] = 0;
    m[7] = 0;
    m[8] = 1;
  }

  public Matrix3(double m0, double m3, double m6,
      double m1, double m4, double m7,
      double m2, double m5, double m8) {

    m = new double[9];

    m[0] = m0;
    m[1] = m1;
    m[2] = m2;
    m[3] = m3;
    m[4] = m4;
    m[5] = m5;
    m[6] = m6;
    m[7] = m7;
    m[8] = m8;
  }


  public Matrix3 inverted() {
    double a = m[0], b = m[3], c = m[6],
        d = m[1], e = m[4], f = m[7],
        g = m[2], h = m[5], i = m[8];

    Matrix3 inv = new Matrix3(e * i - f * h, c * h - b * i, b * f - c * e,
        f * g - d * i, a * i - c * g, c * d - a * f,
        d * h - e * g, b * g - a * h, a * e - b * d);

    double det = Determinants.det3x3(a, b, c, d, e, f, g, h, i);
    return inv.times(1 / det);
  }

//	public void invert(){
//		Matrix3 inv = inverted();
//		
//		for(int k = 0; k < 9; k++){
//			m[k] *= inv.m[k];
//		}
//	}

  public Matrix3 times(double d) {
    Matrix3 m = new Matrix3(this);
    m.multiply(d);
    return m;
  }

  private void multiply(double d) {
    for (int i = 0; i < 9; i++) {
      m[i] *= d;
    }
  }

  public Matrix3 transposed() {
    return new Matrix3(m[0], m[1], m[2],
        m[3], m[4], m[5],
        m[6], m[7], m[8]);
  }

//	private void transpose(){
//		Matrix3 t = transposed();
//		
//		for(int k = 0; k < 9; k++){
//			m[k] *= t.m[k];
//		}
//	}

//	public double get(int row, int col){
//		return m[col*3 + row];
//	}
//	
//	public void set(int row, int col, double val){
//		m[col*3 + row] = val;
//	}

  public Vector3 multiply(Vector3 v) {
    return new Vector3(
        m[0] * v.x + m[3] * v.y + m[6] * v.z,
        m[1] * v.x + m[4] * v.y + m[7] * v.z,
        m[2] * v.x + m[5] * v.y + m[8] * v.z);
  }

  public String toString() {
    return "(" + m[0] + ", " + m[3] + ", " + m[6] + ")\n" +
        "(" + m[1] + ", " + m[4] + ", " + m[7] + ")\n" +
        "(" + m[2] + ", " + m[5] + ", " + m[8] + ")";

  }
}
