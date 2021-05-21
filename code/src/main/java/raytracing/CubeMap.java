package raytracing;

import image.Image;
import image.ImageUtils;
import image.RGBA;

import utils.Vector3;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class CubeMap<T> implements EnvironmentMap<T> {

  private final List<Image<T>> faces;

  public CubeMap(List<Image<T>> faces) {
    assert faces.size() == 6;
    this.faces = new ArrayList<Image<T>>(faces);
  }

  public T access(Vector3 direction) {
    int faceIndex = getFace(direction);

    double u;
    double v;

    switch (faceIndex) {
      case 0: // X-
        u = -direction.z / direction.x;
        v = direction.y / direction.x;
        break;
      case 1: // Y-
        u = -direction.x / direction.y;
        v = direction.z / direction.y;
        break;
      case 2: // Z-
        u = direction.x / direction.z;
        v = direction.y / direction.z;
        break;
      case 3: // X+
        u = -direction.z / direction.x;
        v = -direction.y / direction.x;
        break;
      case 4: // Y+
        u = direction.x / direction.y;
        v = direction.z / direction.y;
        break;
      case 5: // Z+
        u = direction.x / direction.z;
        v = -direction.y / direction.z;
        break;
      default:
        throw new IllegalStateException();
    }

    u = 0.5 * u + 0.5;
    v = 0.5 * v + 0.5;

    return accessFaceUV(faceIndex, u, v);
  }

  private T accessFaceUV(int faceIndex, double u, double v) {
    if (faceIndex < 0 || faceIndex > 5) {
      throw new IllegalArgumentException("Illegal face index.");
    }

    Image<T> face = faces.get(faceIndex);

    int i = (int) ((face.cols() - 1) * u);
    int j = (int) ((face.rows() - 1) * v);

    return face.get(i, j);
  }

  private static int getFace(Vector3 direction) {
    double maxVal = abs(direction.x);
    int maxDir = 0;

    if (maxVal < abs(direction.y)) {
      maxVal = abs(direction.y);
      maxDir = 1;
    }

    if (maxVal < abs(direction.z)) {
      maxVal = abs(direction.z);
      maxDir = 2;
    }

    if (getElement(direction, maxDir) > 0) {
      maxDir += 3;
    }

    return maxDir;
  }

  private static double getElement(Vector3 v, int index) {
    switch (index) {
      case 0:
        return v.x;
      case 1:
        return v.y;
      case 2:
        return v.z;
      default:
        throw new IllegalArgumentException("Index must be in the range [0, 2]");
    }
  }

  /**
   * Loads six cubemap face images and returns an instance of CubeMap&lt;RGBA&gt; The parameter
   * namePattern compactly specifies the path to the face images. Please not that the string must
   * contain the formatting instruction "%s", which will be replaced by (negx, negy, negz, etc.) to
   * load the individual images. E.g. "paht/to/cubemap-faces/face%s.png"
   *
   * @param namePattern Pattern of the file name of the form "paht/to/cubemap-faces/face%s.ext"
   * @return An instance of CubeMap&lt;RGBA&gt;.
   * @throws IOException Can be thrown by internal call to ImageUtils.read()
   */
  public static CubeMap<RGBA> load(String namePattern) throws IOException {
    return load(
        String.format(namePattern, "negx"),
        String.format(namePattern, "negy"),
        String.format(namePattern, "negz"),
        String.format(namePattern, "posx"),
        String.format(namePattern, "posy"),
        String.format(namePattern, "posz")
    );
  }

  /**
   * Loads the six cubemap face images specified in parameters (pos|neg)[xyz] and returns an
   * instance of CubeMap&lt;RGBA&gt;
   *
   * @return An instance of CubeMap&lt;RGBA&gt;.
   * @throws IOException Can be thrown by internal call to ImageUtils.read()
   */
  public static CubeMap<RGBA> load(String negX,
      String negY,
      String negZ,
      String posX,
      String posY,
      String posZ) throws IOException {
    return new CubeMap<>(loadFaces(negX, negY, negZ, posX, posY, posZ));
  }

  private static List<Image<RGBA>> loadFaces(String... paths) throws IOException {
    ArrayList<Image<RGBA>> faces = new ArrayList<>(6);

    for (String path : paths) {
      System.out.println("[INFO] Loading cube-map face " + path + " ...");
      faces.add(ImageUtils.read(path));
    }

    return faces;
  }
}
