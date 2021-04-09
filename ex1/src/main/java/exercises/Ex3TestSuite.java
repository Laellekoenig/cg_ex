package exercises;

import testSuite.VisualTestSuite;
import exercises.ex3Tests.PyramidMeshClippedTest;
import exercises.ex3Tests.PyramidWireTurntableTest;
import exercises.ex3Tests.TeapotMeshClippedTest;
import exercises.ex3Tests.PyramidMeshTest;
import exercises.ex3Tests.PyramidMeshWrongColorizer;
import exercises.ex3Tests.PyramidMeshWithoutZBufferWrongColorShadingTest;
import exercises.ex3Tests.PyramidWireTest;
import exercises.ex3Tests.TurnableTeapotTest;

public class Ex3TestSuite {

  public static void main(String args[]) {

    String dir = "data/resources/ex3";

    new VisualTestSuite(
        new PyramidWireTest(dir + "/pyramid-w.png", "Pyramid Wire Frame"),
        new PyramidWireTurntableTest(dir + "/pyramid-w.png", "Turntable: Pyramid Wire Frame"),
        new PyramidMeshWithoutZBufferWrongColorShadingTest(dir + "/meshRasterisation.png",
            "Rasterization: Pyramid Mesh"),
        new PyramidMeshWrongColorizer(dir + "/meshRasterisationZBuffer.png",
            "Z-Buffer: Pyramid Mesh"),
        new PyramidMeshTest(dir + "/pyramid-colorized.png", "Colorization: Pyramid Mesh"),
        //Test colorization
        new PyramidMeshClippedTest(dir + "/pyramid-clipped.png", "Clipping: Pyramid Mesh"),
        new TeapotMeshClippedTest(dir + "/teapot-clipped.png", "Clipping: Teapot Mesh"),
        new TurnableTeapotTest(dir + "/teapot.png", "Turntable: Teapot")
    );
  }
}
