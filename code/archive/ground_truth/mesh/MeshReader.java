package mesh;

import image.RGBA;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import utils.Vector2;
import utils.Vector3;
import utils.Triplet;

/**
 * Reads .obj files and stores the objects in Meshes. Only the following information per object is
 * considered: - vertices - texture coordinates - vertex normals - faces (are triangulated
 * automatically)
 *
 * @author Manuel Kaufmann
 */
public class MeshReader {

  /**
   * Convenience function to load an .obj file into a mesh. Order: top-down according to appearance
   * in file.
   */
  public static Vector<Mesh> loadObj(String filename) {
    MeshReader mr = new MeshReader(filename);
    mr.read();
    return mr.getMeshes();
  }

  private Vector<MeshLike> tempMeshes; //.obj file can contain several objects
  private MeshLike currentMesh;
  private String filename;

  public MeshReader(String filename) {
    if (!filename.substring(filename.length() - 4).equalsIgnoreCase(".obj")) {
      throw new IllegalArgumentException("Can only read .obj files.");
    }

    tempMeshes = new Vector<MeshLike>();
    this.filename = filename;
  }

  public void read() {
    currentMesh = new MeshLike(); //default in case no "o" key is specified
    tempMeshes.add(currentMesh);
    parseFile(new File(filename));
  }

  private void parseFile(File file) {
    BufferedReader reader = null;

    try {

      reader = new BufferedReader(new FileReader(file));
      String line;

      while ((line = reader.readLine()) != null) {
        String[] args = line.trim()
            .split("\\s++"); //possibly several empty spaces between arguments

        if (!args[0].startsWith("#") && !args[0].isEmpty()) { //ignore comments and empty Lines
          parseLine(args);
        }

      }
    } catch (FileNotFoundException e) {
      System.err.println("File " + file.getPath() + " does not exist.");
    } catch (IOException e) {
      System.err.println("Cannot read file " + file.getPath());
    } finally {
      try {
        reader.close();
      } catch (Exception e) {/* ignored */}
    }

  }

  private void parseLine(String[] args) {
    String key = args[0].toLowerCase();

    if (key.equals("o")) { //a new object is defined

      //only start a new one if current is not empty
      if (!currentMesh.isEmpty()) {
        currentMesh = new MeshLike();
        tempMeshes.add(currentMesh);
      }

    } else if (key.equals("v")) { //vertex coordinates: (x,y,z[,w]) [color1, color2, color2]

      Double[] pt = new Double[4];
      pt[3] = 1.0d; //default

      //we don't want colors
      int end = args.length;
      if (args.length == 8) {
        end = 5; //w and colors specified
      }
      if (args.length == 7) {
        end = 4; //no w, but colors
      }

      for (int i = 1; i < end; i++) {
        pt[i - 1] = Double.parseDouble(args[i]);
      }

      currentMesh.vertices.add(new Vector3(pt[0], pt[1], pt[2])); //ignore w completely

    } else if (key.equals("vn")) { //vertex normal coordinates: (x,y,z), not necessarily unit

      Double[] pt = new Double[3];

      for (int i = 1; i < args.length; i++) {
        pt[i - 1] = Double.parseDouble(args[i]);
      }

      currentMesh.normals.add(new Vector3(pt[0], pt[1], pt[2]));

    } else if (key.equals("vt")) { //texture coordinates: (u,v[,w])

      Double[] pt = new Double[3];
      pt[2] = 0.0d; //default

      for (int i = 1; i < args.length; i++) {
        pt[i - 1] = Double.parseDouble(args[i]);
      }

      currentMesh.texCoords.add(new Vector2(pt[0], pt[1])); //ignore w completely

    } else if (key.equals("f")) { //polygonal face: vertex/texture/normal index starting at 1

      Face f = new Face();

      for (int i = 1; i < args.length; i++) {
        String node = args[i];
        String[] nodeSplitted = node.split("/");
        int size = nodeSplitted.length;

        Vertex v = null;

        if (size == 1) { //vertices only
          v = new Vertex(Integer.parseInt(nodeSplitted[0]) - 1, -1, -1);
        } else if (size == 2) { //vertices and texCoords
          v = new Vertex(Integer.parseInt(nodeSplitted[0]) - 1,
              Integer.parseInt(nodeSplitted[1]) - 1, -1);
          currentMesh.foundTexture = true;
        } else if (size == 3 && nodeSplitted[1].isEmpty()) { //vertices and normals
          v = new Vertex(Integer.parseInt(nodeSplitted[0]) - 1, -1,
              Integer.parseInt(nodeSplitted[2]) - 1);
          currentMesh.foundNormals = true;
        } else if (size == 3 && !nodeSplitted[1].isEmpty()) { //vertices, texture and normals
          v = new Vertex(Integer.parseInt(nodeSplitted[0]) - 1,
              Integer.parseInt(nodeSplitted[1]) - 1, Integer.parseInt(nodeSplitted[2]) - 1);
          currentMesh.foundTexture = true;
          currentMesh.foundNormals = true;
        } else {
          throw new IllegalArgumentException("Bad File Format.");
        }

        f.nodes.add(v);
      }

      currentMesh.faces.add(f);
    }

  }

  public Vector<Mesh> getMeshes() {
    Vector<Mesh> meshes = new Vector<Mesh>(tempMeshes.size());

    calculateOffsets();

    for (MeshLike tempMesh : tempMeshes) {
      meshes.add(convertToMesh(tempMesh));
    }

    return meshes;
  }

  private void calculateOffsets() {

    int vOffset = 0, nOffset = 0, tOffset = 0;

    for (int i = 1; i < tempMeshes.size(); i++) {
      MeshLike m = tempMeshes.get(i);
      MeshLike previous = tempMeshes.get(i - 1);

      vOffset += previous.vertices.size();
      nOffset += previous.normals.size();
      tOffset += previous.texCoords.size();

      m.vOffset = vOffset;
      m.nOffset = nOffset;
      m.tOffset = tOffset;
    }

  }

  private Mesh convertToMesh(MeshLike tempMesh) {
    Mesh mesh = new Mesh();

    mesh.vertices = tempMesh.vertices.toArray(new Vector3[0]);
    if (tempMesh.foundNormals) {
      mesh.normals = tempMesh.normals.toArray(new Vector3[0]);
    }
    if (tempMesh.foundTexture) {
      mesh.texCoords = tempMesh.texCoords.toArray(new Vector2[0]);
    }

    //split up non-triangle faces into triangles
    int numberOfTriangles = tempMesh.faces.size();
    for (Face f : tempMesh.faces) {
      numberOfTriangles += f.nodes.size() - 3;
    }

    mesh.tvi = new Triplet[numberOfTriangles];
    if (tempMesh.foundNormals) {
      mesh.tni = new Triplet[numberOfTriangles];
    }
    if (tempMesh.foundTexture) {
      mesh.tti = new Triplet[numberOfTriangles];
    }

    int t = 0;

    for (Face f : tempMesh.faces) {

      for (int c = 0; c <= f.nodes.size() - 3; c++) {
        mesh.tvi[t] = new Triplet(f.nodes.get(0).vIndex - tempMesh.vOffset,
            f.nodes.get(1 + c).vIndex - tempMesh.vOffset,
            f.nodes.get(2 + c).vIndex - tempMesh.vOffset);

        if (tempMesh.foundNormals) {
          mesh.tni[t] = new Triplet(f.nodes.get(0).nIndex - tempMesh.nOffset,
              f.nodes.get(1 + c).nIndex - tempMesh.nOffset,
              f.nodes.get(2 + c).nIndex - tempMesh.nOffset);
        }

        if (tempMesh.foundTexture) {
          mesh.tti[t] = new Triplet(f.nodes.get(0).tIndex - tempMesh.tOffset,
              f.nodes.get(1 + c).tIndex - tempMesh.tOffset,
              f.nodes.get(2 + c).tIndex - tempMesh.tOffset);
        }

        t++;
      }

    }

    //paint everything gray when texture not used
    mesh.colors = Arrays.asList(new RGBA(160 / 255f, 160 / 255f, 160 / 255f)).toArray(new RGBA[0]);
    mesh.tci = new Triplet[numberOfTriangles];
    Triplet c = new Triplet(0, 0, 0);

    for (int i = 0; i < mesh.tci.length; i++) {
      mesh.tci[i] = c;
    }

    return mesh;
  }

  /******************** Helper Classes ********************/
  private class MeshLike {

    ArrayList<Vector3> vertices, normals;
    ArrayList<Vector2> texCoords;
    ArrayList<Face> faces;
    boolean foundNormals, foundTexture;
    int vOffset, tOffset, nOffset; //needed because face indices are relative

    public MeshLike() {
      this.vertices = new ArrayList<Vector3>();
      this.normals = new ArrayList<Vector3>();
      this.texCoords = new ArrayList<Vector2>();
      this.faces = new ArrayList<Face>();
      this.foundNormals = false;
      this.foundTexture = false;
      this.vOffset = 0;
      this.tOffset = 0;
      this.nOffset = 0;
    }

    public boolean isEmpty() {
      return vertices.isEmpty() && normals.isEmpty() && texCoords.isEmpty();
    }

  }

  private class Face {

    Vector<Vertex> nodes;

    public Face() {
      this.nodes = new Vector<Vertex>();
    }
  }

  private class Vertex {

    int vIndex, tIndex, nIndex;

    public Vertex(int vIndex, int tIndex, int nIndex) {
      this.vIndex = vIndex;
      this.tIndex = tIndex;
      this.nIndex = nIndex;
    }
  }
}
