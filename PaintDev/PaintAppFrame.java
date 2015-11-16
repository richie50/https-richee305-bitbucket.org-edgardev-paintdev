import java.io.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PaintAppFrame extends JFrame implements MouseListener, MouseMotionListener, ActionListener {
	static final long serialVersionUID = 2L;
	final String TITLE = "DemoFileMenu";
	final int MAX_SAMPLES = 500;
	final String[] BUTTON_ICONS = { "icons/bigger.png", "icons/smaller.png", "icons/wider.png", "icons/narrow.png", 
			"icons/taller.png", "icons/shorter.png", "icons/up.png", "icons/down.png", 
			"icons/left.png", "icons/right.png" };

	final String[] BUTTON_NAMES = { "Bigger", "Smaller", "Wider", "Narrower", "Taller", "Shorter", "Up", "Down", "Left",
			"Right" };

	final int[] MNEMONICS = { KeyEvent.VK_B, KeyEvent.VK_S, KeyEvent.VK_W, KeyEvent.VK_N, KeyEvent.VK_T, KeyEvent.VK_H,
			KeyEvent.VK_U, KeyEvent.VK_D, KeyEvent.VK_L, KeyEvent.VK_R };
	private JButton tbButtons[];
	// -----------------------------------------
	private Image image;
	private PaintPanel paintPanel;
	private PaintPanel paintPanel2;
	private JPanel content; // the main panel
	private JPanel paintCanvas;
	// -----------------------------------------
	private JButton clearButton;
	private JButton thickBrush;
	private JButton thinBrush;
	private JButton changeColor;
	private JButton loadImage;
	private JButton squareButton;
	private JButton circleButton;
	private JButton eraserButton;
	private Point[] stroke;
	private int strokeCount;
	private int sampleCount;
	// Menu
	File file;
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
	private JMenu toolsMenu;
	// toolbar
	private JToolBar toolBar;
	private JMenuItem position;
	JPanel popUpToolBar;
	private JMenuItem clearImage;

	PaintAppFrame() {
		file = new File(".");
		this.fileChooser = new JFileChooser(file);
		// this.fileChooser.addChoosableFileFilter(new MyFileFilter(EXT));
		System.out.println(file.getAbsolutePath());
		System.out.println(file.getName());
		final int FIELD_WIDTH = 10;

		strokeCount = 0;
		sampleCount = 0;

		stroke = new Point[MAX_SAMPLES];
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		Icon clearIcon = new ImageIcon("icons/clear.png");
		Icon brushIcon = new ImageIcon("icons/brush.png");
		Icon pencilIcon = new ImageIcon("icons/pencil.png");
		Icon rgbIcon = new ImageIcon("icons/color.png");
		Icon loadIcon = new ImageIcon("icons/open.png");
		Icon squareIcon = new ImageIcon("icons/square.png");
		Icon circleIcon = new ImageIcon("icons/circle.png");
		Icon eraserIcon = new ImageIcon("icons/erase.png");

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

		squareButton = new JButton(squareIcon);
		squareButton.setActionCommand("square");
		squareButton.addActionListener(this);

		circleButton = new JButton(circleIcon);
		circleButton.setActionCommand("circle");
		circleButton.addActionListener(this);

		eraserButton = new JButton(eraserIcon);
		eraserButton.setActionCommand("eraser");
		eraserButton.addActionListener(this);

		// added the menu goes here
		image = Toolkit.getDefaultToolkit().getImage(".");
		paintPanel = new PaintPanel();
		paintPanel.addMouseMotionListener(this);
		paintPanel.addMouseListener(this);
		// make this a toolbar
		JPanel buttons = new JPanel(new GridLayout());
		buttons.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		buttons.setLayout(new GridLayout(10, 2));

		buttons.add(clearButton);
		buttons.add(thickBrush);
		buttons.add(thinBrush);
		buttons.add(changeColor);
		buttons.add(loadImage);
		buttons.add(squareButton);
		buttons.add(circleButton);
		buttons.add(eraserButton);

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
		newFileImage = new ImageIcon("open_icon.JPG");
		img = newFileImage.getImage();
		img = img.getScaledInstance(30, 10, java.awt.Image.SCALE_AREA_AVERAGING);
		newFileImage = new ImageIcon(img);
		save = new JMenuItem("Save", newFileImage);
		save.setMnemonic(KeyEvent.VK_S);
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		save.addActionListener(this);
		save.setEnabled(false);
		newFileImage = new ImageIcon("open_icon.JPG");
		img = newFileImage.getImage();
		img = img.getScaledInstance(30, 10, java.awt.Image.SCALE_AREA_AVERAGING);
		newFileImage = new ImageIcon(img);
		saveAs = new JMenuItem("Save as...", newFileImage);
		saveAs.setMnemonic(KeyEvent.VK_A);
		saveAs.addActionListener(this);
		saveAs.setEnabled(true);
		fileMenu.add(save);
		fileMenu.add(saveAs);
		// <exit>
		exit = new JMenuItem("Exit");
		exit.setMnemonic(KeyEvent.VK_X);
		exit.addActionListener(this);
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
		int n = BUTTON_ICONS.length;
		tbButtons = new JButton[n];
		JToolBar tb = new JToolBar();
		for (int i = 0; i < n; ++i) {
			tbButtons[i] = new JButton(new ImageIcon(BUTTON_ICONS[i]));
			tbButtons[i].setMargin(new Insets(0, 0, 0, 0));
			tbButtons[i].setName(BUTTON_NAMES[i]);
			tbButtons[i].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
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
			tbButtons[i].setToolTipText(BUTTON_NAMES[i]);
			tbButtons[i].setMnemonic(MNEMONICS[i]);
			tb.add(tbButtons[i]);
			if (i == 1 || i == 5)
				tb.addSeparator();
		}
		content.add(tb, BorderLayout.NORTH);
		this.setContentPane(content);
	}

	public void mouseDragged(MouseEvent me) {
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
	}

	public void mouseMoved(MouseEvent me) {
	}

	public void mouseClicked(MouseEvent me) {
	}

	public void mouseEntered(MouseEvent me) {
	}

	public void mouseExited(MouseEvent me) {
	}

	public void mousePressed(MouseEvent me) {
		int x = me.getX();
		int y = me.getY();

		stroke[sampleCount] = new Point(x, y);
		if (sampleCount < MAX_SAMPLES - 1) {
			++sampleCount;
		}
	}

	public void mouseReleased(MouseEvent me) {
		if (SwingUtilities.isLeftMouseButton(me)) {
			++strokeCount;
			sampleCount = 0;
		}
	}

	public void actionPerformed(ActionEvent ae) {
		String command = ae.getActionCommand();
		Object source = ae.getSource();
		// instantiate the filechooser
		switch (command) {
		case "clear":
			paintPanel.clear();
			break;
		case "thick":
			paintPanel.setThickBrush();
			break;
		case "thin":
			paintPanel.setThinBrush();
			break;
		case "color":
			this.setPaintColor();
			break;
		case "image":
			this.loadImage();
			break;
		case "eraser":
			paintPanel.setEraser();
			break;

		}
		if (source == open) {
			// fc.accept(file);
			// fc.getDescription(file);
			int sh = fileChooser.showOpenDialog(null);
			boolean ok = (sh == JFileChooser.APPROVE_OPTION);
			System.out.println(sh + " " + ok);
			if (ok) {
				file = fileChooser.getSelectedFile();
				System.out.println("&&&");
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
				SaveFile(file);
			} else {
				saveAs.doClick();
			}
		} else if (source == saveAs) {
			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				file = fileChooser.getSelectedFile();
				if (!file.exists() || fileExistenceChecker(file)) {
					SaveFile(file);
				}
			}
		} else if (source == position) {
			System.out.println("DropDownToolBar");
		} else if (source == clearImage) {
			paintPanel.clearImage();
		}
	}

	protected void openFile(File file2) {
		// TODO Auto-generated method stub
		this.setTitle(file2.getName() + " - " + TITLE);
		// close.setEnabled(true);
		save.setEnabled(true);
		saveAs.setEnabled(true);
		return;
	}

	protected void SaveFile(File file) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		System.out.println("Figure how to write that currant panel in a image form");
		writer.print(paintPanel.getImage());
		writer.close();
		this.setTitle(file.getName() + "1" + "-" + TITLE);
	}

	protected boolean fileExistenceChecker(File file) {
		final Object[] options = { "Yes", "No", "Cancel" };
		return JOptionPane.showOptionDialog(this,
				"The file '" + file.getName() + "' already exists.  " + "Replace existing file?", "Warning",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options,
				options[2]) == JOptionPane.YES_OPTION;
	}

	private void setPaintColor() {
		Color color = JColorChooser.showDialog(null, "Choose Paint Color", Color.black);
		paintPanel.setColor(color);
	}

	private void loadImage() {
		JFileChooser imageSelector = new JFileChooser(System.getProperty("user.dir"));

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
}
