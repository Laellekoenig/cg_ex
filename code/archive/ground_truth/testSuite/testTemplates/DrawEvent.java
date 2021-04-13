package testSuite.testTemplates;

import java.util.EventObject;

/**
 * Signals that a test was (re-)drawn (either because it was chosen via the menu or because a
 * parameter in the widget changed.
 *
 * @author Manuel Kaufmann
 */
public class DrawEvent extends EventObject {

  private static final long serialVersionUID = 1L;//default, silences compiler warning
  public VisualTest test;

  public DrawEvent(Object source) {
    super(source);
    this.test = (VisualTest) source;
  }

}
