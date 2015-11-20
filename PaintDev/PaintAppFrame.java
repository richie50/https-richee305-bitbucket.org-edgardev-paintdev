
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEditSupport;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PaintAppFrame extends JFrame implements MouseListener, MouseMotionListener, ActionListener {
	static final long serialVersionUID = 2L;
	String message;
	private JLabel messageField;
	Font messageFont;
	Color foreColor;
	Color backColor;

	static int flag = 0;
	int FLAG = 0;
	private int textX;
	private int textY;
	// ------------------------------------------------------------------------
	final String TITLE = "DemoFileMenu";
	final int MAX_SAMPLES = 500;
	final String[] BUTTON_ICONS = { "icons/bigger.png", "icons/smaller.png", "icons/wider.png", "icons/narrow.png",
			"icons/taller.png", "icons/shorter.png", "icons/up.png", "icons/down.png", "icons/left.png",
			"icons/right.png", "icons/wordArt.PNG" };

	final String[] BUTTON_NAMES = { "Bigger", "Smaller", "Wider", "Narrower", "Taller", "Shorter", "Up", "Down", "Left",
			"Right", "wordArt" };

	final int[] MNEMONICS = { KeyEvent.VK_B, KeyEvent.VK_S, KeyEvent.VK_W, KeyEvent.VK_N, KeyEvent.VK_T, KeyEvent.VK_H,
			KeyEvent.VK_U, KeyEvent.VK_D, KeyEvent.VK_L, KeyEvent.VK_R, KeyEvent.VK_P };
	private JButton tbButtons[];
	// ---------------------------------------------------------------------------
	private String EXTENSIONS;
	// -------------------------------------------------------------------------
	private Image image;
	private PaintPanel paintPanel;
	private JPanel content; // the main panel
	private JPanel paintCanvas;
	private WordArtCustomDialog cd;
	// -------------------------------------------------------------------------
	private JButton clearButton;
	private JButton thickBrush;
	private JButton thinBrush;
	private JButton changeColor;
	private JButton loadImage;
	private JButton rectButton;
	private JButton rectfillButton;
	private JButton roundRectButton;
	private JButton roundRectFillButton;
	private JButton circFillButton;
	private JButton circleButton;
	private JButton lineButton;
	private JButton eraserButton;
	private JButton undoButton;
	private JButton redoButton;
	private Point[] stroke;
	private int sampleCount;
	static Graphics2D gr;

	// Menu
	File file;
	ImageFileChooser imageChooser;
	private JFileChooser fileChooser;
	final String[] EXT = { ".gif", ".png", ".jpg", ".bmp" };
	JMenuItem newFile;
	JMenuItem open;
	private JMenuItem save;
	private JMenuItem saveAs;
	private JMenuItem exit;
	private JMenuItem Undo;
	private JMenuItem Redo;
	private JMenuItem clear;
	private JMenuItem print;
	private JMenu toolsMenu;
	// toolbar
	private JMenuItem position;
	JPanel popUpToolBar;
	private JMenuItem clearImage;
	private MyFileFilter fileExtensions;
	private String fileExist;

	public static ArrayList<Shape> rectStruct = new ArrayList<Shape>();
	public static ArrayList<Shape> circStruct = new ArrayList<Shape>();
	public static ArrayList<Shape> rectFillStruct = new ArrayList<Shape>();
	public static ArrayList<Shape> circFillStruct = new ArrayList<Shape>();
	public static ArrayList<Shape> roundRectStruct = new ArrayList<Shape>();
	public static ArrayList<Shape> roundRectFillStruct = new ArrayList<Shape>();
	public static ArrayList<Shape> lineStruct = new ArrayList<Shape>();

	private static Point mouseStart;
	private static Point mouseEnd;

	PaintAppFrame() {
		file = new File(System.getProperty("user.dir"));
		this.fileChooser = new JFileChooser(file);
		fileExtensions = new MyFileFilter(EXT);
		// this.fileChooser.addChoosableFileFilter(fileExtensions);
		System.out.println(file.getAbsolutePath());
		System.out.println(file.getName());
		sampleCount = 0;

		stroke = new Point[MAX_SAMPLES];
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		undoButton = new JButton("Undo");
		undoButton.setActionCommand("undo");
		undoButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				paintPanel.undo();
			}
		});

		redoButton = new JButton("Redo");
		redoButton.setActionCommand("Redo");
		redoButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				paintPanel.redo();
			}
		});

		clearButton = new JButton("CLEAR");
		clearButton.setActionCommand("clear");
		clearButton.addActionListener(this);

		thickBrush = new JButton("BRUSH");
		thickBrush.setActionCommand("thick");
		thickBrush.addActionListener(this);

		thinBrush = new JButton("PENCIL");
		thinBrush.setActionCommand("thin");
		thinBrush.addActionListener(this);

		changeColor = new JButton("CHANGE COLOR");
		changeColor.setActionCommand("color");
		changeColor.addActionListener(this);

		loadImage = new JButton("LOAD IMAGE");
		loadImage.setActionCommand("image");
		loadImage.addActionListener(this);

		// added the menu goes here
		image = Toolkit.getDefaultToolkit().getImage(".");
		paintPanel = new PaintPanel();
		paintPanel.addMouseMotionListener(this);
		paintPanel.addMouseListener(this);

		// make this a toolbar and images

		Icon roundRectIcon = new ImageIcon("icons/rectEdge.png");
		Icon roundRectFillIcon = new ImageIcon("icons/fullRectEdge.png");
		Icon lineIcon = new ImageIcon("icons/line.png");
		Icon clearIcon = new ImageIcon("icons/clear.png");
		Icon brushIcon = new ImageIcon("icons/brush.png");
		Icon pencilIcon = new ImageIcon("icons/pencil.png");
		Icon rgbIcon = new ImageIcon("icons/color.png");
		Icon loadIcon = new ImageIcon("icons/open.png");
		Icon eraserIcon = new ImageIcon("icons/erase.png");
		Icon rectIcon = new ImageIcon("icons/rect.png");
		Icon circleIcon = new ImageIcon("icons/circ.png");
		Icon rectfillIcon = new ImageIcon("icons/fullRect.png");
		Icon circFillIcon = new ImageIcon("icons/fullCirc.png");
		Icon undoIcon = new ImageIcon("icons/undo.png");
		Icon redoIcon = new ImageIcon("icons/redo.png");

		undoButton = new JButton(undoIcon);
		redoButton = new JButton(redoIcon);

		clearButton = new JButton(clearIcon);
		clearButton.setActionCommand("clear");
		clearButton.addActionListener(this);

		thickBrush = new JButton(brushIcon);
		thickBrush.setActionCommand("thick");
		thickBrush.addActionListener(this);

		thinBrush = new JButton(pencilIcon);
		thinBrush.setActionCommand("thin");
		thinBrush.addActionListener(this);

		changeColor = new JButton(rgbIcon);
		changeColor.setActionCommand("color");
		changeColor.addActionListener(this);

		loadImage = new JButton(loadIcon);
		loadImage.setActionCommand("image");
		loadImage.addActionListener(this);

		rectButton = new JButton(rectIcon);
		rectButton.setActionCommand("rectangle");
		rectButton.addActionListener(this);

		rectfillButton = new JButton(rectfillIcon);
		rectfillButton.setActionCommand("rectanglefill");
		rectfillButton.addActionListener(this);

		roundRectButton = new JButton(roundRectIcon);
		roundRectButton.setActionCommand("roundrectangle");
		roundRectButton.addActionListener(this);

		roundRectFillButton = new JButton(roundRectFillIcon);
		roundRectFillButton.setActionCommand("roundrectanglefill");
		roundRectFillButton.addActionListener(this);

		circleButton = new JButton(circleIcon);
		circleButton.setActionCommand("circle");
		circleButton.addActionListener(this);

		circFillButton = new JButton(circFillIcon);
		circFillButton.setActionCommand("circlefill");
		circFillButton.addActionListener(this);

		eraserButton = new JButton(eraserIcon);
		eraserButton.setActionCommand("eraser");
		eraserButton.addActionListener(this);

		lineButton = new JButton(lineIcon);
		lineButton.setActionCommand("line");
		lineButton.addActionListener(this);

		JPanel buttons = new JPanel(new GridLayout());
		buttons.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		buttons.setLayout(new GridLayout(16, 2));

		buttons.add(clearButton);
		buttons.add(thickBrush);
		buttons.add(thinBrush);
		buttons.add(changeColor);
		buttons.add(loadImage);
		buttons.add(eraserButton);
		buttons.add(rectButton);
		buttons.add(circleButton);
		buttons.add(rectfillButton);
		buttons.add(circFillButton);
		buttons.add(roundRectButton);
		buttons.add(roundRectFillButton);
		buttons.add(lineButton);
		buttons.add(undoButton);
		buttons.add(redoButton);

		paintCanvas = new JPanel(new BorderLayout());
		paintCanvas.add(paintPanel, "Center");
		paintCanvas.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		paintCanvas.setBackground(new Color(100, 100, 142));
		paintCanvas.setPreferredSize(new Dimension(650, 520));
		paintCanvas.setMaximumSize(new Dimension(2000, 1600));

		content = new JPanel(new BorderLayout());
		content.add(buttons, BorderLayout.WEST);
		content.add(paintCanvas, BorderLayout.CENTER);
		this.setContentPane(content);
		// menu
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar); // makes the menu bar
		// add the File Menu
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);
		// add items that goes under the <file> menu
		ImageIcon newFileImage = new ImageIcon("new_file_image.gif");
		Image img = newFileImage.getImage();
		img = img.getScaledInstance(20, 10, java.awt.Image.SCALE_AREA_AVERAGING);
		newFileImage = new ImageIcon(img);
		newFile = new JMenuItem("New", newFileImage);
		newFile.setMnemonic(KeyEvent.VK_N);
		newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		newFile.addActionListener(this);
		fileMenu.add(newFile);
		// <open>
		newFileImage = new ImageIcon("open_icon.JPG");
		img = newFileImage.getImage();
		img = img.getScaledInstance(30, 10, java.awt.Image.SCALE_AREA_AVERAGING);
		newFileImage = new ImageIcon(img);
		open = new JMenuItem("Open...", newFileImage);
		open.setMnemonic(KeyEvent.VK_O);
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		open.addActionListener(this);
		fileMenu.add(open);
		// <save>
		newFileImage = new ImageIcon("save2_icon.PNG");
		img = newFileImage.getImage();
		img = img.getScaledInstance(30, 10, java.awt.Image.SCALE_AREA_AVERAGING);
		newFileImage = new ImageIcon(img);
		save = new JMenuItem("Save", newFileImage);
		save.setMnemonic(KeyEvent.VK_S);
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		save.addActionListener(this);
		save.setEnabled(false);
		newFileImage = new ImageIcon("save_icon.JPG");
		img = newFileImage.getImage();
		img = img.getScaledInstance(30, 10, java.awt.Image.SCALE_AREA_AVERAGING);
		newFileImage = new ImageIcon(img);
		saveAs = new JMenuItem("Save as...", newFileImage);
		saveAs.setMnemonic(KeyEvent.VK_A);
		saveAs.addActionListener(this);
		saveAs.setEnabled(true);
		fileMenu.add(save);
		fileMenu.add(saveAs);
		// print
		newFileImage = new ImageIcon("save2_icon.PNG");
		img = newFileImage.getImage();
		img = img.getScaledInstance(30, 10, java.awt.Image.SCALE_AREA_AVERAGING);
		newFileImage = new ImageIcon(img);
		print = new JMenuItem("Print...", newFileImage);
		print.setMnemonic(KeyEvent.VK_P);
		print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		print.addActionListener(this);
		print.setEnabled(true);
		fileMenu.add(print);
		// <exit>
		newFileImage = new ImageIcon("exit_icon.PNG");
		img = newFileImage.getImage();
		img = img.getScaledInstance(30, 10, java.awt.Image.SCALE_AREA_AVERAGING);
		newFileImage = new ImageIcon(img);
		exit = new JMenuItem("Exit", newFileImage);
		exit.setMnemonic(KeyEvent.VK_X);
		exit.addActionListener(this);
		exit.setEnabled(true);
		fileMenu.add(exit);
		// <Edit>
		JMenu editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);
		menuBar.add(editMenu);
		toolsMenu = new JMenu("Tools");
		toolsMenu.setMnemonic(KeyEvent.VK_T);
		menuBar.add(toolsMenu);
		position = new JMenuItem("Position");
		toolsMenu.add(position);
		position.addActionListener(this);
		// <red>
		Undo = new JMenuItem("Undo");
		Undo.setMnemonic(KeyEvent.VK_Z);
		Undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		Undo.addActionListener(this);
		//
		Redo = new JMenuItem("Redo");
		Redo.setMnemonic(KeyEvent.VK_X);
		Redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		Redo.addActionListener(this);
		//
		clear = new JMenuItem("Clear");
		clear.setMnemonic(KeyEvent.VK_C);
		clear.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		clear.addActionListener(this);
		//
		clearImage = new JMenuItem("Clear Image");
		clearImage.setMnemonic(KeyEvent.VK_I);
		clearImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
		clearImage.addActionListener(this);
		editMenu.add(Undo);
		editMenu.add(Redo);
		editMenu.add(clear);
		editMenu.add(clearImage);
		editMenu.setMnemonic(KeyEvent.VK_T);
		addDropDownToolBar();

	}

	public void addDropDownToolBar() {
		cd = new WordArtCustomDialog(this);
		int n = BUTTON_ICONS.length;
		int i = 0;
		tbButtons = new JButton[n];
		JToolBar tb = new JToolBar();
		for (i = 0; i < n; ++i) {
			tbButtons[i] = new JButton(new ImageIcon(BUTTON_ICONS[i]));
			tbButtons[i].setMargin(new Insets(0, 0, 0, 0));
			tbButtons[i].setName(BUTTON_NAMES[i]);
			if (i == 10) {
				tbButtons[i].addMouseListener(new PopupListener());
				System.out.println("Word ART");
				message = "";
				messageFont = new Font("Arial", Font.PLAIN, 26);
				foreColor = Color.black;
				backColor = Color.white;
				messageField = new JLabel(message);
				// paintPanel.add(messageField);
				// paintPanel.add(messageField);
			} else {
				System.out.println(i);
				tbButtons[i].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						String source = ((JButton) e.getSource()).getName();

						if (source.equals(BUTTON_NAMES[0])) { // 'bigger'
							System.out.println("bigger");
							paintPanel.makeBigger();
						}

						else if (source.equals(BUTTON_NAMES[1])) { // 'smaller'
							System.out.println("smaller");
							paintPanel.makeSmaller();
						}

						else if (source.equals(BUTTON_NAMES[2])) // 'wider'
							paintPanel.makeWider();

						else if (source.equals(BUTTON_NAMES[3])) // 'narrower'
							paintPanel.makeNarrower();

						else if (source.equals(BUTTON_NAMES[4])) // 'taller'
							paintPanel.makeTaller();

						else if (source.equals(BUTTON_NAMES[5])) // 'shorter'
							paintPanel.makeShorter();

						else if (source.equals(BUTTON_NAMES[6])) // 'up
							paintPanel.moveUp();

						else if (source.equals(BUTTON_NAMES[7])) // 'down'
							paintPanel.moveDown();

						else if (source.equals(BUTTON_NAMES[8])) // 'left'
							paintPanel.moveLeft();

						else if (source.equals(BUTTON_NAMES[9])) // 'right'
							paintPanel.moveRight();
					}
				});
			}
			tbButtons[i].setToolTipText(BUTTON_NAMES[i]);
			tbButtons[i].setMnemonic(MNEMONICS[i]);
			tb.add(tbButtons[i]);
			if (i == 1 || i == 5 || i == 9)
				tb.addSeparator();
		}
		content.add(tb, BorderLayout.NORTH);
		this.setContentPane(content);
	}

	public void updateMessage() {
		messageField.setText(message);
		messageField.setFont(messageFont);
		messageField.setForeground(foreColor);
		messageField.setBackground(backColor);
	}

	public void mouseDragged(MouseEvent me) {

		if (flag == 0) {
			int x = me.getX();
			int y = me.getY();
			// System.out.println(x +"---->"+y);
			if (SwingUtilities.isLeftMouseButton(me)) {
				stroke[sampleCount] = new Point(x, y);
				int x1 = (int) stroke[sampleCount - 1].getX();
				int y1 = (int) stroke[sampleCount - 1].getY();
				int x2 = (int) stroke[sampleCount].getX();
				int y2 = (int) stroke[sampleCount].getY();
				if (sampleCount < MAX_SAMPLES - 1)
					++sampleCount;

				// draw ink trail from previous point to current point
				paintPanel.drawInk(x1, y1, x2, y2);
			}
		} else if (flag == 1) {
			mouseEnd = new Point(me.getX(), me.getY());
			repaint();
			System.out.println("rect dragged");
		}

		else if (flag == 2) {
			mouseEnd = new Point(me.getX(), me.getY());
			repaint();
		}

		else if (flag == 3) {
			mouseEnd = new Point(me.getX(), me.getY());
			repaint();
		}

		else if (flag == 4) {
			mouseEnd = new Point(me.getX(), me.getY());
			repaint();
		}

		else if (flag == 5) {
			mouseEnd = new Point(me.getX(), me.getY());
			repaint();
		}

		else if (flag == 6) {
			mouseEnd = new Point(me.getX(), me.getY());
			repaint();
		} else if (flag == 7) {
			mouseEnd = new Point(me.getX(), me.getY());
			repaint();
		}

		else if (flag == 8) {
			int x = me.getX();
			int y = me.getY();
			if (SwingUtilities.isLeftMouseButton(me)) {
				stroke[sampleCount] = new Point(x, y);
				int x1 = (int) stroke[sampleCount - 1].getX();
				int y1 = (int) stroke[sampleCount - 1].getY();
				int x2 = (int) stroke[sampleCount].getX();
				int y2 = (int) stroke[sampleCount].getY();
				if (sampleCount < MAX_SAMPLES - 1)
					++sampleCount;

				// draw ink trail from previous point to current point
				paintPanel.drawEraser(x1, y1, x2, y2);
			}
		}

	}

	public void mouseMoved(MouseEvent me) {
	}

	public void mouseClicked(MouseEvent me) {
		if (FLAG == 1) {
			textX = me.getX();
			textY = me.getY();
			paintPanel.customePaint(cd.getExampleText(), 0x0, textX, textY, messageFont, backColor, foreColor);
			// FLAG = 0;
		}
	}

	public void mouseEntered(MouseEvent me) {
	}

	public void mouseExited(MouseEvent me) {
	}

	public void mousePressed(MouseEvent me) {
		// textX = me.getX();
		// textY = me.getY();
		// System.out.println("MOUSE PRESSED =>" + textX +"-----"+textY);
		if (flag == 0) {
			int x = me.getX();
			int y = me.getY();
			textX = me.getX();
			textY = me.getY();
			System.out.println("MOUSE PRESSED =>" + textX + "-----" + textY);
			stroke[sampleCount] = new Point(x, y);
			if (sampleCount < MAX_SAMPLES - 1) {
				++sampleCount;
			}
		} else if (flag == 1) {
			mouseStart = new Point(me.getX(), me.getY());
			mouseEnd = mouseStart;
			repaint();
		}

		else if (flag == 2) {
			mouseStart = new Point(me.getX(), me.getY());
			mouseEnd = mouseStart;
			repaint();
		}

		else if (flag == 3) {
			mouseStart = new Point(me.getX(), me.getY());
			mouseEnd = mouseStart;
			repaint();
		}

		else if (flag == 4) {
			mouseStart = new Point(me.getX(), me.getY());
			mouseEnd = mouseStart;
			repaint();
		}

		else if (flag == 5) {
			mouseStart = new Point(me.getX(), me.getY());
			mouseEnd = mouseStart;
			repaint();
		}

		else if (flag == 6) {
			mouseStart = new Point(me.getX(), me.getY());
			mouseEnd = mouseStart;
			repaint();
		}

		else if (flag == 7) {
			mouseStart = new Point(me.getX(), me.getY());
			mouseEnd = mouseStart;
			repaint();
		} else if (flag == 8) {
			int x = me.getX();
			int y = me.getY();

			stroke[sampleCount] = new Point(x, y);
			if (sampleCount < MAX_SAMPLES - 1) {
				++sampleCount;
			}
		}
	}

	public void mouseReleased(MouseEvent me) {
		if (flag == 0) {
			if (SwingUtilities.isLeftMouseButton(me)) {
				sampleCount = 0;
			}
		} else if (flag == 1) {
			Shape r = createRect(mouseStart.x, mouseStart.y, me.getX(), me.getY());
			rectStruct.add(r);
			mouseStart = null;
			mouseEnd = null;
			repaint();
		} else if (flag == 2) {
			Shape r = createCircle(mouseStart.x, mouseStart.y, me.getX(), me.getY());
			circStruct.add(r);
			mouseStart = null;
			mouseEnd = null;
			repaint();
		}

		else if (flag == 3) {
			Shape r = createFillRect(mouseStart.x, mouseStart.y, me.getX(), me.getY());
			rectFillStruct.add(r);
			mouseStart = null;
			mouseEnd = null;
			repaint();
		}

		else if (flag == 4) {
			Shape r = createFillCircle(mouseStart.x, mouseStart.y, me.getX(), me.getY());
			circFillStruct.add(r);
			mouseStart = null;
			mouseEnd = null;
			repaint();
		}

		else if (flag == 5) {
			Shape r = createRoundRect(mouseStart.x, mouseStart.y, me.getX(), me.getY());
			roundRectStruct.add(r);
			mouseStart = null;
			mouseEnd = null;
			repaint();
		}

		else if (flag == 6) {
			Shape r = createRoundRectFill(mouseStart.x, mouseStart.y, me.getX(), me.getY());
			roundRectFillStruct.add(r);
			mouseStart = null;
			mouseEnd = null;
			repaint();
		}

		else if (flag == 7) {
			Shape r = createLine(mouseStart.x, mouseStart.y, me.getX(), me.getY());
			lineStruct.add(r);
			mouseStart = null;
			mouseEnd = null;
			repaint();
		} else if (flag == 8) {
			if (SwingUtilities.isLeftMouseButton(me)) {
				sampleCount = 0;
			}
		}
	}

	@SuppressWarnings("static-access")
	public void actionPerformed(ActionEvent ae) {
		String command = ae.getActionCommand();
		Object source = ae.getSource();
		// instantiate the filechooser
		switch (command) {
		case "clear":
			paintPanel.clear();
			FLAG = 0;
			break;
		case "thick":
			flag = 0;
			FLAG = 0;
			paintPanel.setThickBrush();
			break;
		case "thin":
			flag = 0;
			FLAG = 0;
			paintPanel.setThinBrush();
			break;
		case "color":
			this.setPaintColor();
			FLAG = 0;
			break;
		case "image":
			this.loadImage();
			FLAG = 0;
			break;
		case "rectangle":
			flag = 1;
			FLAG = 0;
			this.paintRect(gr);
			break;
		case "circle":
			flag = 2;
			FLAG = 0;
			this.paintCircle(gr);
			break;

		case "rectanglefill":
			flag = 3;
			FLAG = 0;
			this.paintRectFill(gr);
			break;
		case "circlefill":
			flag = 4;
			FLAG = 0;
			this.paintFillCircle(gr);
			break;

		case "roundrectangle":
			flag = 5;
			FLAG = 0;
			this.paintRoundRectangle(gr);
			break;

		case "roundrectanglefill":
			flag = 6;
			FLAG = 0;
			this.paintRoundRectangleFill(gr);
			break;

		case "line":
			flag = 7;
			FLAG = 0;
			this.paintLine(gr);
			break;

		case "eraser":
			flag = 8;
			FLAG = 0;
			paintPanel.setEraser();
			break;

		}
		if (source == open) {
			System.out.println(fileChooser.getName(file));
			int sh = fileChooser.showOpenDialog(this);
			boolean ok = (sh == JFileChooser.APPROVE_OPTION);
			System.out.println(sh + " " + ok);
			if (ok) {
				file = fileChooser.getSelectedFile();
				System.out.println("OPEN");
				openFile(file);
			}
		} else if (source == newFile) {
			System.out.println("Ask to save current file before destroying");
			paintCanvas.remove(paintPanel);
			paintPanel = new PaintPanel();
			paintPanel.addMouseMotionListener(this);
			paintPanel.addMouseListener(this);
			paintCanvas.add(paintPanel, "Center");
			content.add(paintCanvas, BorderLayout.CENTER);
			this.setContentPane(content);
			saveAs.setEnabled(true);
			save.setEnabled(true);
		} else if (source == save) {
			if (file != null) {
				SaveFile(file, EXTENSIONS);
			} else {
				saveAs.doClick();
			}
		} else if (source == saveAs) {
			imageChooser = new ImageFileChooser(System.getProperty("user.dir"));
			// imageChooser.setCurrentDirectory(file);
			System.out.println("MONITOR: " + imageChooser.getTypeDescription(file));
			// the the list of files in the current directory
			int returnVal = imageChooser.showSaveDialog(this);
			System.out.println("MONITOR2: " + imageChooser);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					/*
					 * Make sure the where ever u wana save the file is
					 * consistent and you are resetting the FILE POINTER every
					 * time you click on this button
					 */
					if (fileExistenceChecker(new File(System.getProperty("user.dir")))) {
						file = imageChooser.getSelectedFileWithExtension();
						EXTENSIONS = ((FileNameExtensionFilter) imageChooser.getFileFilter()).getExtensions()[0];
						fileExist = file.getName();
						System.out.println("WHAT IS THE EXT and file Name:" + EXTENSIONS + "->" + getSaveFileName()
								+ "->" + imageChooser.getSelectedFileWithExtension());
						SaveFile(file, EXTENSIONS);
						save.setEnabled(true);
					} else {
					}
				} catch (FileNotFoundException e) {

					e.printStackTrace();
				}
			}

		} else if (source == position) {
			System.out.println("DropDownToolBar");
		} else if (source == clearImage) {
			paintPanel.clearImage();
		} else if (source == clear) {
			paintPanel.clear();
		} else if (source == print) {
			BufferedImage imageToPrint = new BufferedImage(paintPanel.getWidth(), paintPanel.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			Printer painter = new Printer(imageToPrint);
			boolean observer = painter.imageUpdate(imageToPrint, ImageObserver.ALLBITS, 0, 0, imageToPrint.getWidth(),
					imageToPrint.getHeight());
			PrinterJob job = PrinterJob.getPrinterJob();
			job.pageDialog(job.defaultPage());
			job.setPrintable(painter);
			boolean ok_to_print = job.printDialog();
			if (ok_to_print && observer) {
				try {
					job.print();
				} catch (PrinterException ex) {
					ex.getCause();
				}
			}
		}
	}

	protected void openFile(File file2) {

		this.setTitle(file2.getName() + " - " + TITLE);
		// close.setEnabled(true);
		save.setEnabled(true);
		saveAs.setEnabled(true);
		return;
	}

	protected int SaveFile(File file, String extension) {
		BufferedImage imageToSave = new BufferedImage(paintPanel.getWidth(), paintPanel.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = imageToSave.createGraphics();
		paintPanel.paint(graphics2D);
		try {
			ImageIO.write(imageToSave, extension, file);
			// ImageIO.write(imageToSave, EXT[0], file);
			System.out.println("SAVING . . . ");
		} catch (Exception ex) {
			ex.printStackTrace();
			return JOptionPane.NO_OPTION;
		}
		return JOptionPane.YES_OPTION;
	}

	protected boolean fileExistenceChecker(File file) throws FileNotFoundException {
		Set<String> set = (Set<String>) ImageFileChooser.getAllFiles(file);
		final Object[] options = { "Yes", "No", "Cancel" };
		for (String s : set) {
			System.out.println("OK :" + s.toString() + "===> " + getSaveFileName());
			if (s.toString().equals(getSaveFileName())) {
				return JOptionPane.showOptionDialog(this,
						"The file '" + file.getName() + "' already exists.  " + "Replace existing file?", "Warning",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options,
						options[2]) == JOptionPane.YES_OPTION;
			}
		}
		return rootPaneCheckingEnabled;
	}

	private void setPaintColor() {
		Color color = JColorChooser.showDialog(null, "Choose Paint Color", Color.black);
		paintPanel.setColor(color);
	}

	private void loadImage() {
		JFileChooser imageSelector = new JFileChooser(System.getProperty("user.dir")); // try
																						// using
																						// this
																						// property
																						// for
		// the ImageFileChooser

		imageSelector.setDialogTitle("Select Image");
		imageSelector.setMultiSelectionEnabled(false);
		imageSelector.addChoosableFileFilter(new FileNameExtensionFilter("Images Only", "jpg", "png", "gif"));
		imageSelector.setAcceptAllFileFilterUsed(false);
		if (imageSelector.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File imageFile = imageSelector.getSelectedFile();
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			image = toolkit.getImage(imageFile.getAbsolutePath());
			int h = image.getHeight(this);
			int w = image.getWidth(this);
			setPreferredSize(new Dimension(w, h));
			paintPanel.addImage(image);
			System.out.println(paintPanel);
		}
	}

	public String getSaveFileName() {
		return this.fileExist;
	}

	class PopupListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent me) {
			System.out.println("DIALOG.....");
			int j = cd.showCustomDialog(null, messageFont, foreColor, backColor);
			if (j == WordArtCustomDialog.APPLY_OPTION) {
				messageFont = cd.getFont();
				foreColor = cd.getForeColor();
				backColor = cd.getBackColor();
				FLAG = 1;
			}
		}
	}

	public static void paintRect(Graphics g) {
		gr = (Graphics2D) g;

		for (Shape s : rectStruct) {
			gr.setPaint(PaintPanel.LINE_COLOR);
			gr.setStroke(new BasicStroke(2));

			gr.draw(s);
			// gr.setPaint(this.LINE_COLOR);
			// gr.fill(s);
		}
		if (flag == 1) {
			if (mouseStart != null && mouseEnd != null) {
				gr.setPaint(Color.RED);
				Shape r = createRect(mouseStart.x, mouseStart.y, mouseEnd.x, mouseEnd.y);
				gr.draw(r);
			}
		}
	}

	public static void paintRectFill(Graphics g) {
		gr = (Graphics2D) g;

		for (Shape s : rectFillStruct) {
			gr.setPaint(PaintPanel.LINE_COLOR);
			gr.setStroke(new BasicStroke(2));

			gr.draw(s);
			gr.fill(s);
		}
		if (flag == 3) {

			if (mouseStart != null && mouseEnd != null) {
				gr.setPaint(Color.RED);
				Shape r = createFillRect(mouseStart.x, mouseStart.y, mouseEnd.x, mouseEnd.y);
				gr.draw(r);
			}
		}
	}

	public static void paintCircle(Graphics g) {
		gr = (Graphics2D) g;

		for (Shape s : circStruct) {
			gr.setPaint(PaintPanel.LINE_COLOR);
			gr.setStroke(new BasicStroke(2));

			gr.draw(s);
			// gr.setPaint(PaintPanel.LINE_COLOR);
			// gr.fill(s);
		}

		if (flag == 2) {

			if (mouseStart != null && mouseEnd != null) {
				gr.setPaint(Color.BLUE);
				Shape r = createCircle(mouseStart.x, mouseStart.y, mouseEnd.x, mouseEnd.y);
				gr.draw(r);
			}
		}
	}

	public static void paintFillCircle(Graphics g) {
		gr = (Graphics2D) g;

		for (Shape s : circFillStruct) {
			gr.setPaint(PaintPanel.LINE_COLOR);
			gr.setStroke(new BasicStroke(2));

			gr.draw(s);
			gr.fill(s);
		}
		if (flag == 4) {
			if (mouseStart != null && mouseEnd != null) {
				gr.setPaint(Color.RED);
				Shape r = createFillCircle(mouseStart.x, mouseStart.y, mouseEnd.x, mouseEnd.y);
				gr.draw(r);
			}
		}
	}

	public static void paintRoundRectangle(Graphics g) {
		gr = (Graphics2D) g;

		for (Shape s : roundRectStruct) {
			gr.setPaint(Color.black);
			gr.draw(s);
		}

		if (flag == 5) {

			if (mouseStart != null && mouseEnd != null) {
				gr.setPaint(Color.RED);
				Shape r = createRoundRect(mouseStart.x, mouseStart.y, mouseEnd.x, mouseEnd.y);
				gr.draw(r);
			}
		}
	}

	public static void paintRoundRectangleFill(Graphics g) {
		gr = (Graphics2D) g;

		for (Shape s : roundRectFillStruct) {
			gr.setPaint(Color.black);
			gr.setStroke(new BasicStroke(2));

			gr.draw(s);
			gr.fill(s);

		}

		if (flag == 6) {

			if (mouseStart != null && mouseEnd != null) {
				gr.setPaint(Color.RED);
				Shape r = createRoundRectFill(mouseStart.x, mouseStart.y, mouseEnd.x, mouseEnd.y);
				gr.draw(r);
			}
		}
	}

	public static void paintLine(Graphics g) {
		gr = (Graphics2D) g;

		for (Shape s : lineStruct) {
			gr.setPaint(Color.black);
			gr.setStroke(new BasicStroke(2));

			gr.draw(s);
			gr.fill(s);

		}

		if (flag == 7) {
			if (mouseStart != null && mouseEnd != null) {
				gr.setPaint(Color.GREEN);
				Shape r = createLine(mouseStart.x, mouseStart.y, mouseEnd.x, mouseEnd.y);
				gr.draw(r);
			}
		}
	}

	public static Rectangle2D.Float createRect(int x1, int y1, int x2, int y2) {
		return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
	}

	public static Ellipse2D.Float createCircle(int x1, int y1, int x2, int y2) {
		return new Ellipse2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
	}

	public static Rectangle2D.Float createFillRect(int x1, int y1, int x2, int y2) {
		return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
	}

	public static Ellipse2D.Float createFillCircle(int x1, int y1, int x2, int y2) {
		return new Ellipse2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
	}

	public static RoundRectangle2D.Float createRoundRect(int x1, int y1, int x2, int y2) {
		return new RoundRectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2), 50,
				50);
	}

	public static RoundRectangle2D.Float createRoundRectFill(int x1, int y1, int x2, int y2) {
		return new RoundRectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2), 50,
				50);
	}

	public static Line2D.Float createLine(int x1, int y1, int x2, int y2) {
		return new Line2D.Float(x1, y1, x2, y2);
	}

}
