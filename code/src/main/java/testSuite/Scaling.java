package testSuite;

import java.awt.Toolkit;

public class Scaling {

  private static double scalingFactor =
      Toolkit.getDefaultToolkit().getScreenSize().getWidth() > 2000 ? 1.5 : 1;

  public static double getFactor() {
    return scalingFactor;
  }
}
