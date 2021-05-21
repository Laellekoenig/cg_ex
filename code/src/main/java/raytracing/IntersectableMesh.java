package raytracing;

import mesh.Mesh;
import utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IntersectableMesh implements Intersectable {

  private final List<Triangle> triangles;
  private final Mesh mesh;

  public IntersectableMesh(Mesh mesh, Vector3 offset) {
    this.mesh = mesh;
    this.triangles = new ArrayList<Triangle>(mesh.tvi.length);

    for (Triplet ti : mesh.tvi) {
      Triangle t = new Triangle(
          mesh.vertices[ti.get(0)].plus(offset),
          mesh.vertices[ti.get(1)].plus(offset),
          mesh.vertices[ti.get(2)].plus(offset));

      triangles.add(t);
    }
  }

  @Override
  public Optional<Intersection> intersect(Ray ray, double near) {
    Optional<Intersection> nearestIntersection = Optional.empty();
    int triangleId = -1;

    for (int i = 0; i < triangles.size(); ++i) {
      Optional<Intersection> intersection = triangles.get(i).intersect(ray, near);

      if (!intersection.isPresent()) {
        continue;
      }

      if (nearestIntersection.isPresent()) {
        if (nearestIntersection.get().t < intersection.get().t) {
          continue;
        }
      }

      nearestIntersection = intersection;
      triangleId = i;
    }

    if (!nearestIntersection.isPresent()) {
      return Optional.empty();
    }

    // Change normal of intersection according to the mesh's actual normal at the intersection
    Triplet tni = mesh.tni[triangleId];
    Vector3 n0 = mesh.normals[tni.get(0)];
    Vector3 n1 = mesh.normals[tni.get(1)];
    Vector3 n2 = mesh.normals[tni.get(2)];

    double t = nearestIntersection.get().t;

    Vector3 intersectionPoint = ray.pointAt(t);
    BarycentricCoordinates bcc = triangles.get(triangleId).barycentricCoords(intersectionPoint);

    Vector3 normal = bcc.interpolate(n0, n1, n2);

    return Optional.of(new Intersection(t, normal.normalize()));
  }


}
