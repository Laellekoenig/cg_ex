package rasterization;

import mesh.Mesh;
import utils.BarycentricCoordinates;

/**
 * Connects a Pixel to a point of a triangle
 */
public class Correspondence {

  /**
   * The mesh the triangle is from
   */
  public Mesh mesh;

  /**
   * Index of the triangle
   */
  public int triangle;

  /**
   * Barycentric coordinates of the point
   */
  public BarycentricCoordinates triCoords;

  /**
   * Depth of the point, used for z-buffer
   */
  public double depth;


  public Correspondence(Mesh mesh, int triangle, BarycentricCoordinates triCoords, double depth) {
    this.mesh = mesh;
    this.triangle = triangle;
    this.triCoords = triCoords;
    this.depth = depth;
  }

  public Correspondence() {
    this(null, 0, null, Double.MAX_VALUE);
  }
}
