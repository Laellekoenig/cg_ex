package testSuite;

import java.util.Arrays;
import java.util.Vector;

import testSuite.testTemplates.VisualTest;

/**
 * A container to capture all tests that can be conducted via the GUI.
 *
 * @author Manuel Kaufmann
 */
public class VisualTestSuite implements Runnable {

  protected Vector<VisualTest> tests;

  public VisualTestSuite(VisualTest... tests) {
    this.tests = new Vector<VisualTest>(Arrays.asList(tests));
    loadGUI();
  }

  private void loadGUI() {
    javax.swing.SwingUtilities.invokeLater(this);
  }

  @Override
  public void run() {
    new MainWindow("TestSuite", this);
  }

  protected void addTest(VisualTest t) {
    tests.add(t);
  }

  public VisualTest getTest(int nr) {
    return tests.get(nr);
  }

  public VisualTest getTest(String title) {
    for (VisualTest t : tests) {
      if (t.getTitle().equals(title)) {
        return t;
      }
    }

    return null;
  }

  public int size() {
    return tests.size();
  }

}
