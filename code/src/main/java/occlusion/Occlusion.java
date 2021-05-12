package occlusion;

import com.sun.tools.javac.util.Assert;
import illumination.PointLight;
import image.Image;
import mesh.Mesh;
import projection.PinholeProjection;
import projection.Projection;
import rasterization.Correspondence;
import rasterization.MeshRasterizer;
import rasterization.TriangleRasterizer;
import renderer.MeshRenderer;
import utils.*;

public class Occlusion {

  public Image<Correspondence> shadowMap;
  private PinholeProjection shadowProjection;

  private int height, width;
  private double shadowBias;
  private int pcfMask;
  private ShadowType shadowType;

  public Occlusion(int height, int width, ShadowType shadowType, double shadowBias, int pcfMask) {
    this.height = height;
    this.width = width;
    this.shadowType = shadowType;
    this.pcfMask = pcfMask;
    this.shadowBias = shadowBias;
  }

  public void generateShadowMap(Projection projection, PointLight lightSource, Mesh[] meshes) {

    //TODO: Blatt 4, Aufgabe 6 b)
    // The map in the suite looks a bit off, the colours are too bright.

    shadowProjection = new PinholeProjection(width, height);
    shadowProjection.setView(projection.getViewMatrixOfLightSource(lightSource));

    MeshRasterizer rasterizer = new MeshRasterizer(width, height);

    shadowMap = rasterizer.rasterize(shadowProjection, meshes);
  }

  /**
   * Returns if the position is in shadow or not:
   * 1 = completely visible
   * 0 = completely in the shadow(s)
   */
  public double inShadow(Vector3 position) {

    //TODO: Blatt 4, Aufgabe 7
    // Has some artifacts, so it doesn't quite match the gold standard... maybe due to wonky shadow map?

    // Project the position from world space into view space.
    Vector3 tPosition = shadowProjection.project(position);

    int offset = pcfMask / 2;
    double shadowIntensity = 0;

    if (shadowType.equals(ShadowType.HARD)) {
      // point of position in shadow map
      Correspondence pointInSm = shadowMap.get((int) tPosition.x, (int) tPosition.y);

      // if the shadow map has the point and the depth of the analyzed position is greater than that point, the position
      // isn't lighted up.
      if (pointInSm != null && pointInSm.depth + shadowBias <= (-1) * tPosition.z) {
        return 0;
      }

      return 1.0;
    }

    // If the shadows are soft, then iterate over the filter and add 1 to the intensity for every pixel which is visible.
    for (int xPos = (int) tPosition.x - offset; xPos <= tPosition.x + offset; xPos++) {
      for (int yPos = (int) tPosition.y - offset; yPos <= tPosition.y + offset; yPos++) {
        Correspondence pointInSm = shadowMap.get(xPos, yPos);
        if (pointInSm != null && pointInSm.depth + shadowBias <= (-1) * tPosition.z) {
          shadowIntensity += 0;
        } else {
          shadowIntensity += 1;
        }
      }
    }

    // Calculate and return the intensity of the light in regard to the filter.
    if (pcfMask != 0) {
      shadowIntensity = shadowIntensity / (pcfMask * pcfMask);
    }

    return shadowIntensity;
  }
}
