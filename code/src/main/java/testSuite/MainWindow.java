package testSuite;

import image.ImageUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;

import testSuite.testTemplates.DrawEvent;
import testSuite.testTemplates.DrawEventListener;
import testSuite.testTemplates.InteractiveTest;
import testSuite.testTemplates.MeshTest;
import testSuite.testTemplates.VisualTest;

public class MainWindow extends JFrame implements DrawEventListener {

  private enum displayMode {GS, DIFF}

  ;

  private static final long serialVersionUID = 1L; //default, silences compiler warning

  private VisualTestSuite testSuite;
  private JPanel contentPane, imagePane, widget;
  private JScrollPane ownImage, gsImage;
  private JLabel statusLabel;

  private BufferedImage own, gs = null;
  private displayMode currentMode = null;
  private VisualTest currentTest = null;

  public MainWindow(String title, VisualTestSuite testSuite) {
    super(title);
    currentMode = displayMode.GS;
    this.testSuite = testSuite;

    loadGUI();
    selectTest(testSuite.getTest(0));
  }

  private void loadGUI() {
    setLookAndFeel();
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    setContentPane();
    populateContentPane();

    createMenuBar();

    //set GUI visible
    this.pack();
    this.setVisible(true);
  }

  private void setLookAndFeel() {

    try {
      UIManager.setLookAndFeel(
          UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException e) {
      System.err.println("Cannot find SystemLookAndFeel Class.");
    } catch (InstantiationException e) {
      System.err.println("Cannot instantiate SystemLookAndFeel Class.");
    } catch (IllegalAccessException e) {
      System.err.println("No right to instantiate SystemLookAndFeel Class.");
    } catch (UnsupportedLookAndFeelException e) {
      System.err.println("SystemLookAndFeel not supported.");
    }
  }

  /**
   * Replaces the default contentPane of the main window frame.
   */
  private void setContentPane() {
    contentPane = new JPanel(new BorderLayout());
    contentPane
        .setPreferredSize(
            new Dimension((int) (1080 * Scaling.getFactor()), (int) (650 * Scaling.getFactor())));
    contentPane.setLayout(new BorderLayout());
    //contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

    this.setContentPane(contentPane);
  }

  /**
   * ContentPane consists of the image pane that holds the homebrewn and the gold standard images
   * and a interactive pane that contains a possible widget and a status bar.
   */
  private void populateContentPane() {

    createImagePane();
    createInteractivePane();
  }

  private void createImagePane() {
    imagePane = new JPanel();
    imagePane.setPreferredSize(new Dimension(this.getWidth(), this.getHeight() - 90));

    imagePane.setLayout(new BoxLayout(imagePane, BoxLayout.X_AXIS));
    imagePane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    this.add(imagePane, BorderLayout.CENTER);

    populateImagePane();
  }

  private void populateImagePane() {

    JPanel ownPane = createHomebrewnPane();

    imagePane.add(ownPane);
    imagePane.add(Box.createRigidArea(new Dimension(20, 0)));

    JPanel gsPane = createGoldStandardPane();

    imagePane.add(gsPane);

  }

  /**
   * HomebrewnPane is part of the ImagePane, located on the left. It contains a title and the Panel
   * that actually holds the image.
   *
   * @return
   */
  private JPanel createHomebrewnPane() {
    JPanel ownPane = new JPanel(new BorderLayout());
    ownPane.setPreferredSize(
        new Dimension((int) (520 * Scaling.getFactor()), (int) (550 * Scaling.getFactor())));
    ownPane.setMaximumSize(
        new Dimension((int) (520 * Scaling.getFactor()), (int) (550 * Scaling.getFactor())));
    ownPane.setMinimumSize(
        new Dimension((int) (520 * Scaling.getFactor()), (int) (550 * Scaling.getFactor())));

    JPanel title = new JPanel();
    title.setPreferredSize(
        new Dimension((int) (ownPane.getWidth() * Scaling.getFactor()),
            (int) (40 * Scaling.getFactor())));
    title.setLayout(new BoxLayout(title, BoxLayout.X_AXIS));
    JLabel titleLabel = new JLabel("Homebrew");
    titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
    title.add(titleLabel);
    ownPane.add(title, BorderLayout.NORTH);

    ownImage = makeImageContainer();

    ownPane.add(ownImage, BorderLayout.CENTER);
    return ownPane;
  }

  /**
   * The container to hold an image as a JScrollPane.
   */
  private JScrollPane makeImageContainer() {
    JScrollPane scrollPane = new JScrollPane();
    scrollPane
        .setPreferredSize(
            new Dimension((int) (520 * Scaling.getFactor()), (int) (500 * Scaling.getFactor())));
    scrollPane.getViewport().setBackground(Color.BLACK);
    scrollPane.addMouseWheelListener(new MouseWheelListener() {

      @Override
      public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.isControlDown() && e.getWheelRotation() < 0) {
          zoomIn();
        }
      }
    });
    return scrollPane;
  }

  private void zoomIn() {
    System.out.println("Zoom in");
    gs = ImageUtils.enlarge(gs);
    own = ImageUtils.enlarge(own);

    displayGoldStandard(gs);
    displayHomebrewnImage(own);
  }


  /**
   * GoldStandardPane is part of the ImagePane, located on the right. It contains two buttons in the
   * title area, to switch between gold standard and difference-image, and the actual panel holding
   * the image.
   *
   * @return
   */
  private JPanel createGoldStandardPane() {
    JPanel gsPane = new JPanel(new BorderLayout());
    gsPane.setPreferredSize(
        new Dimension((int) (520 * Scaling.getFactor()), (int) (550 * Scaling.getFactor())));
    gsPane.setMaximumSize(
        new Dimension((int) (520 * Scaling.getFactor()), (int) (550 * Scaling.getFactor())));
    gsPane.setMinimumSize(
        new Dimension((int) (520 * Scaling.getFactor()), (int) (550 * Scaling.getFactor())));

    JPanel title = new JPanel();
    title.setPreferredSize(
        new Dimension((int) (500 * Scaling.getFactor()), (int) (40 * Scaling.getFactor())));
    title.setLayout(new BoxLayout(title, BoxLayout.X_AXIS));

    JButton gsButton = new JButton("Gold Standard");
    gsButton.setPreferredSize(
        new Dimension((int) (100 * Scaling.getFactor()),
            (int) (title.getHeight() * Scaling.getFactor())));
    gsButton.setFocusPainted(false);
    gsButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        changeDisplayMode(displayMode.GS);
      }

    });

    JButton diffButton = new JButton("Difference");
    diffButton.setPreferredSize(new Dimension((int) (gsButton.getWidth() * Scaling.getFactor()),
        (int) (gsButton.getHeight() * Scaling.getFactor())));
    diffButton.setFocusPainted(false);
    diffButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        changeDisplayMode(displayMode.DIFF);
      }

    });

    title.add(gsButton);
    title.add(Box.createRigidArea(new Dimension(5, 0)));
    title.add(diffButton);
    gsPane.add(title, BorderLayout.NORTH);

    gsImage = makeImageContainer();
    gsPane.add(gsImage, BorderLayout.CENTER);
    return gsPane;
  }

  private void createInteractivePane() {
    JPanel pane = new JPanel();
    contentPane.add(pane, BorderLayout.SOUTH);

    pane.setPreferredSize(new Dimension(this.getWidth(), 90));
    pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

    //widget
    widget = new JPanel(new FlowLayout(FlowLayout.LEFT));
    widget.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
    widget.setPreferredSize(new Dimension(this.getWidth(), 60));
    pane.add(widget);

    pane.add(Box.createVerticalGlue());

    //statusbar
    JPanel statusBar = new JPanel();
    statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
    pane.add(statusBar);

    statusBar.setPreferredSize(new Dimension(this.getWidth(), 30));
    statusBar.setLayout(new BorderLayout());

    statusLabel = new JLabel("status");
    statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
    statusBar.add(statusLabel);
  }

  private void createMenuBar() {
    JMenuBar menuBar = new JMenuBar();

    JMenu menu = new JMenu("File");
    menuBar.add(menu);

    createFileMenu(menu);

    menu = new JMenu("Tests");
    menuBar.add(menu);

    loadTestMenu(menu);

    menu = new JMenu("View");
    menuBar.add(menu);

    createViewMenu(menu);

    menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
    this.setJMenuBar(menuBar);
  }

  private void createFileMenu(JMenu menu) {
    JMenuItem menuItem = new JMenuItem("Save Image");
    menuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File(currentTest.getTitle() + ".png"));
        int returnVal = fc.showSaveDialog(MainWindow.this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
          try {
            ImageUtils.write(currentTest.getDrawnImage(), fc.getSelectedFile().getAbsolutePath());
            writeStatus("Saved homebrew image to " + fc.getSelectedFile().getPath());
          } catch (IOException e1) {
            System.err.println("Could not store file " + fc.getSelectedFile().getAbsolutePath());
            writeStatus("ERROR: could not save file " + fc.getSelectedFile().getAbsolutePath());
          }
          ;
        }
      }
    });

    menu.add(menuItem);
  }

  private void loadTestMenu(JMenu menu) {
    JMenuItem menuItem;

    for (VisualTest test : testSuite.tests) {
      menuItem = new JMenuItem(test.getTitle());
      menuItem.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          selectTest(testSuite.getTest(e.getActionCommand()));
        }
      });

      menu.add(menuItem);
      test.addListener(this); //used for draw events
    }
  }

  private void createViewMenu(JMenu menu) {
    JMenuItem menuItem = new JMenuItem("Zoom In");
    menuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        zoomIn();
      }
    });

    menu.add(menuItem);

    menuItem = new JMenuItem("Reset");
    menuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        gs = currentTest.getGoldStandard();
        own = ImageUtils.toBufferedImage(currentTest.getDrawnImage());

        if (currentTest instanceof MeshTest) {
          ((MeshTest) currentTest).getTurnTable().reset();
          currentTest.draw();
          System.out.println("Reset");
        }

        displayHomebrewnImage(own);
        displayGoldStandard(gs);
      }
    });

    menu.add(menuItem);
  }


  private void changeDisplayMode(displayMode m) {
    if (currentMode != m) {
      if (m == displayMode.GS) {
        displayGoldStandard(gs);
      } else if (m == displayMode.DIFF) {
        displayDiffImage();
      }

      currentMode = m;
    }
  }

  private void updateTime(float elapsedTime) {
    writeStatus("Test took " + elapsedTime + " seconds");
  }

  private void writeStatus(String text) {
    statusLabel.setText(" " + text);
  }

  public void selectTest(VisualTest test) {
    System.out.println("Selected test: " + test.getTitle());

    removeTurnTable();
    addNewTurnTable(test);
    handleWidget(test);

    currentMode = displayMode.GS;
    currentTest = test;
    test.draw();  //fires draw event, which then calls repaint()
  }

  private void removeTurnTable() {
    if (currentTest != null && currentTest instanceof MeshTest) {
      MeshTest t = (MeshTest) currentTest;
      if (t.hasTurnTable()) {
        ownImage.removeMouseListener(t.getTurnTable());
        ownImage.removeMouseMotionListener(t.getTurnTable());
      }
    }
  }

  private void addNewTurnTable(VisualTest test) {
    if (test instanceof MeshTest) {
      MeshTest t = (MeshTest) test;
      if (t.hasTurnTable()) {
        ownImage.addMouseMotionListener(t.getTurnTable());
        ownImage.addMouseListener(t.getTurnTable());
      }
    }
  }

  private void handleWidget(VisualTest test) {
    if (test instanceof InteractiveTest) {
      InteractiveTest ivt = (InteractiveTest) test;
      setWidget(ivt.getWidget());
    } else {
      clearWidget();
    }
  }


  public void displayHomebrewnImage(BufferedImage img) {
    ScrollablePicture picture = new ScrollablePicture(new ImageIcon(scale(img)), 1);
    ownImage.setViewportView(picture);
  }

  public void displayGoldStandard(BufferedImage img) {
    ScrollablePicture picture = new ScrollablePicture(new ImageIcon(scale(img)), 1);
    gsImage.setViewportView(picture);
  }

  public void displayDiffImage() {
    if (gs != null && own != null) {
      BufferedImage diff = ImageUtils.differenceBetween(own, gs);
      displayGoldStandard(diff);
    }
  }

  public void clearWidget() {
    for (Component c : widget.getComponents()) {
      widget.remove(c);
    }
    pack();
  }

  public void setWidget(Component c) {
    clearWidget();
    if (c != null) {
      widget.add(c);
    }
    pack();
  }

  @Override
  public void repaint(DrawEvent e) {
    gs = e.test.getGoldStandard(); /* pre-load in case difference is needed */
    own = ImageUtils.toBufferedImage(e.test.getDrawnImage());

    displayHomebrewnImage(own);
    displayGoldStandard(gs);
    updateTime(e.test.getElapsedTime());
  }

  private BufferedImage scale(BufferedImage img) {
    if (Scaling.getFactor() == 1) {
      return img;
    }

    BufferedImage scaled = new BufferedImage((int) (img.getWidth() * Scaling.getFactor()),
        (int) (img.getHeight() * Scaling.getFactor()), img.getType());
    Graphics2D g = scaled.createGraphics();
    AffineTransform at = AffineTransform.getScaleInstance(Scaling.getFactor(), Scaling.getFactor());
    g.drawRenderedImage(img, at);

    return scaled;
  }
}
