package testSuite.testTemplates;

import java.awt.Component;

public interface InteractiveTest {

  /**
   * Creates the actual widget dependent on the design of the test.
   */
  public abstract Component getWidget();


}
