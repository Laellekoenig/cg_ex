package testSuite.testTemplates;

import image.Image;
import image.ImageUtils;
import image.RGBA;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public abstract class VisualTest {

  protected String gsFileName, title;
  protected BufferedImage gs;
  protected Image<RGBA> drawn;
  protected int w, h;

  private ArrayList<DrawEventListener> listeners;
  private float elapsedTime = 0.0f;

  public VisualTest(String gsFileName, String title) {
    this.gsFileName = gsFileName;
    this.title = title;
    this.listeners = new ArrayList<DrawEventListener>();

    try {

      System.out.println("Reading " + gsFileName);
      this.gs = ImageUtils.toBufferedImage(ImageUtils.read(gsFileName));
      this.w = gs.getWidth();
      this.h = gs.getHeight();

    } catch (IOException e) {
      System.err.println("Could not read gold standard image " + gsFileName);

      // Set these to something reasonable even though GS is not available
      this.w = 500;
      this.h = 480;
      this.gs = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    }
  }

  protected abstract void _draw();

  public void draw() {
    long toc = System.currentTimeMillis();
    _draw();
    elapsedTime = (System.currentTimeMillis() - toc) / 1000f;
  }

  public BufferedImage getGoldStandard() {
    return gs;
  }

  public Image<RGBA> getDrawnImage() {
    return drawn;
  }

  public String getTitle() {
    return title;
  }

  public String getgsName() {
    return gsFileName;
  }

  public float getElapsedTime() {
    return elapsedTime;
  }

  public synchronized void addListener(DrawEventListener listener) {
    listeners.add(listener);
  }

  public synchronized void removeListener(DrawEventListener listener) {
    listeners.remove(listener);
  }

  protected synchronized void fireDrawEvent() {
    DrawEvent e = new DrawEvent(this);
    for (DrawEventListener l : listeners) {
      l.repaint(e);
    }
  }

}
