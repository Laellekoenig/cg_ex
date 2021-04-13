package utils;

public interface VectorSpace<T> {

//	// inplace operations
//	public void add(T other);
//	public void subtract(T other);
//	public void mult(float factor);

  public T plus(T other);

  public T minus(T other);

  public double dot(T other);

  public T multElementWise(T other);

  public T times(double factor);

  public T neg();

  public double length();


}
