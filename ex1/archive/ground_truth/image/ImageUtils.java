package image;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.imageio.ImageIO;

import utils.Vector2;

public class ImageUtils {

  public static Image<RGBA> read(String filename) throws IOException {
    BufferedImage img = ImageIO.read(new File(filename));

    final int w = img.getWidth();
    final int h = img.getHeight();
    Image<RGBA> out = new Image<RGBA>(w, h);

    for (int x = 0; x < w; x++) {
      for (int y = 0; y < h; y++) {
        out.set(x, y, new RGBA(img.getRGB(x, y)));
      }
    }

    return out;
  }

  /**
   * File type determined by file extension.
   */
  public static void write(Image<RGBA> img, String filename) throws IOException {
    BufferedImage outImg = toBufferedImage(img);
    ImageIO.write(outImg, filename.substring(filename.length() - 3),
        new File(filename));//this works for bmp,png,jpg and gif
  }

  /**
   * Converts an RGBA Image into a BufferedImage to be able to display it in a GUI.
   */
  public static BufferedImage toBufferedImage(Image<RGBA> img) {
    int w = img.cols();
    int h = img.rows();
    BufferedImage outImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

    for (int x = 0; x < w; x++) {
      for (int y = 0; y < h; y++) {
        RGBA val = img.get(x, y);
        if (val == null) {
          outImg.setRGB(x, y, new RGBA(0f, 0f, 0f).pack()); /*paint it black*/
        } else {
          outImg.setRGB(x, y, val.pack());
        }
      }
    }
    return outImg;
  }

  public static BufferedImage differenceBetween(BufferedImage img1, BufferedImage img2) {
    int w = img1.getWidth();
    int h = img1.getHeight();

    if (w != img2.getWidth() || h != img2.getHeight()) {
      throw new IllegalArgumentException(
          "Images sizes differ: " + img1.getWidth() + "x" + img1.getHeight() +
              " vs " + img2.getWidth() + "x" + img2.getHeight());
    }

    BufferedImage outImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

    for (int x = 0; x < w; x++) {
      for (int y = 0; y < h; y++) {
        RGBA rgb1 = new RGBA(img1.getRGB(x, y));
        RGBA rgb2 = new RGBA(img2.getRGB(x, y));
        rgb1.minusAbs(rgb2);
        outImg.setRGB(x, y, rgb1.pack());
      }
    }

    return outImg;
  }

  /**
   * Upscales an image by a factor of two
   */
  public static BufferedImage enlarge(BufferedImage img) {
    int w = img.getWidth();
    int h = img.getHeight();

    BufferedImage outImg = new BufferedImage(w * 2, h * 2, img.getType());

    for (int x = 0; x < w; x++) {
      for (int y = 0; y < h; y++) {
        int rgb = img.getRGB(x, y);
        outImg.setRGB(2 * x, 2 * y, rgb);
        outImg.setRGB(2 * x + 1, 2 * y, rgb);
        outImg.setRGB(2 * x + 1, 2 * y + 1, rgb);
        outImg.setRGB(2 * x, 2 * y + 1, rgb);
      }
    }

    return outImg;
  }

  /**
   * Normalizes an image, such that the range from min to max (over all channels) lies in the
   * interval [0, 1] again.
   */
  public static void normalize(Image<RGBA> img) {

    RGBA min = new RGBA(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
    RGBA max = new RGBA(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

    for (int i = 0; i < img.size(); i++) {
      if (img.get(i) != null) {
        min = RGBA.min(min, img.get(i));
        max = RGBA.max(max, img.get(i));
      }
    }

    RGBA denom = max.minus(min);

    for (int i = 0; i < img.size(); i++) {
      RGBA imgVal = img.get(i);
      if (imgVal != null) {
        imgVal = imgVal.minus(min);
        imgVal = imgVal.divide(denom);
      }
      img.set(i, imgVal);
    }
  }

  /**
   * Puts img2 into img1 at position (x, y). Protruding parts of img2 are clipped.
   */
  public static void inset(Image<RGBA> img1, Image<RGBA> img2, int c, int r) {
    int startW = Math.max(0, c);
    int startH = Math.max(0, r);

    int endW = Math.min(img1.w, img2.w + c);
    int endH = Math.min(img1.h, img2.h + r);

    if (endW <= startW || endH <= startH) {
      return;
    }

    for (int x = startW; x < endW; x++) {
      for (int y = startH; y < endH; y++) {
        RGBA val = img2.get(x - c, y - r);
        val = (val == null) ? new RGBA(0f, 0f, 0f) : val;
        img1.set(x, y, val);
      }
    }
  }

  /**
   * Reads files of type *.f2image that encode fixed warp fields of size 256 x 256 and hence do NOT
   * contain any header information. The files were dumped using C++'s std::ofstream.write()
   * function. The bytes were written in little endian order.
   * <p>
   * f2image files store 256*256 two-dimensional float-vectors. Hence their capacity should always
   * amount to exactly: 256 * 256 * 2 dimensions * 4 bytes = 524'288 bytes
   */
  public static Image<Vector2> readFlowField(String fileName) {
    int w = 256;
    int h = 256; /* size is fixed */

    Image<Vector2> temp = new Image<Vector2>(w, h);

    DataInputStream dis = null;
    byte[] data = null;
    File file = null;

    try {

      file = new File(fileName);
      dis = new DataInputStream(new FileInputStream(file));

      //Read complete byte stream and convert it to ByteBuffer
      data = new byte[(int) file.length()];
      dis.readFully(data);
      ByteBuffer buffer = ByteBuffer.wrap(data);
      buffer.order(ByteOrder.LITTLE_ENDIAN);

      if (buffer.capacity() != 256 * 256 * 2 * 4) {
        throw new IllegalArgumentException("The file " + file.getName()
            + " does not encode the type of flowfields this method can load.");
      }

      //Retrieve floats from ByteBuffer one by one
      for (int i = 0; i < w * h; i++) {
        temp.set(i, new Vector2(buffer.getFloat(), buffer.getFloat()));
      }

    } catch (FileNotFoundException e) {
      System.err.println("File " + file.getPath() + " does not exist.");
    } catch (IOException e) {
      System.err.println("Cannot read file " + file.getPath());
    } catch (BufferUnderflowException e) {
      System.err.println("Cannot decode flowfield (the size is expected to be 256x256.");
    } finally {
      try {
        dis.close();
      } catch (Exception e) {/*ignored*/}
    }

    Image<Vector2> outImg = new Image<Vector2>(w, h);

    //Apparently this conversion is necessary
    for (int x = 0; x < w; x++) {
      for (int y = 0; y < h; y++) {
        Vector2 val = temp.get(x, h - 1 - y);
        outImg.set(x, y, new Vector2(val.x, -val.y));
      }
    }

    return outImg;
  }
}
