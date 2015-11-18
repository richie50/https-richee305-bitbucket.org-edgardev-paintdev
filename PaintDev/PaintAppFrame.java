import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.*;

import javax.swing.border.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.html.HTMLDocument.Iterator;

public class PaintAppFrame extends JFrame implements MouseListener,
		MouseMotionListener, ActionListener {
	static final long serialVersionUID = 2L;
	String message;
	private JLabel messageField;
	Font messageFont;
	Color foreColor;
	Color backColor;

	// ------------------------------------------------------------------------
	final String TITLE = "DemoFileMenu";
	final int MAX_SAMPLES = 500;
	final String[] BUTTON_ICONS = { "tbBigger.gif", "tbSmaller.gif",
			"tbWider.gif", "tbNarrower.gif", "tbTaller.gif", "tbShorter.gif",
			"tbUp.gif", "tbDown.gif", "tbLeft.gif", "tbRight.gif",
			"word_ar.PNG" };

	final String[] BUTTON_NAMES = { "Bigger", "Smaller", "Wider", "Narrower",
			"Taller", "Shorter", "Up", "Down", "Left", "Right", "wordArt" };

	final int[] MNEMONICS = { KeyEvent.VK_B, KeyEvent.VK_S, KeyEvent.VK_W,
			KeyEvent.VK_N, KeyEvent.VK_T, KeyEvent.VK_H, KeyEvent.VK_U,
			KeyEvent.VK_D, KeyEvent.VK_L, KeyEvent.VK_R, KeyEvent.VK_P };
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
	private JButton circleButton;
	private JButton eraserButton;
	private Point[] stroke;
	private int strokeCount;
	private int sampleCount;
	
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
	

	PaintAppFrame() {
		file = new File(System.getProperty("user.dir"));
		this.fileChooser = new JFileChooser(file);
		fileExtensions = new MyFileFilter(EXT);
		// this.fileChooser.addChoosableFileFilter(fileExtensions);
		System.out.println(file.getAbsolutePath());
		System.out.println(file.getName());
		final int FIELD_WIDTH = 10;

		strokeCount = 0;
		sampleCount = 0;

		stroke = new Point[MAX_SAMPLES];
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

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
		Icon clearIcon = new ImageIcon("clear.png");
		Icon brushIcon = new ImageIcon("brush.png");
		Icon pencilIcon = new ImageIcon("pencil.png");
		Icon rgbIcon = new ImageIcon("color.png");
		Icon loadIcon = new ImageIcon("open.png");
		Icon rectIcon = new ImageIcon("rectangle.png");
		Icon circleIcon = new ImageIcon("circle.png");
		Icon eraserIcon = new ImageIcon("erase.png");
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
		
		circleButton = new JButton(circleIcon);
		circleButton.setActionCommand("circle");
		circleButton.addActionListener(this);
		
		eraserButton = new JButton(eraserIcon);
		eraserButton.setActionCommand("eraser");
		eraserButton.addActionListener(this);
		JPanel buttons = new JPanel(new GridLayout());
		buttons.setBorder(BorderFactory.createRaisedSoftBevelBorder());
	    buttons.setLayout(new GridLayout(6, 2));

	    buttons.add(clearButton);
		buttons.add(thickBrush);
		buttons.add(thinBrush);
		buttons.add(changeColor);
		buttons.add(loadImage);
		buttons.add(rectButton);
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
		img = img
				.getScaledInstance(20, 10, java.awt.Image.SCALE_AREA_AVERAGING);
		newFileImage = new ImageIcon(img);
		newFile = new JMenuItem("New", newFileImage);
		newFile.setMnemonic(KeyEvent.VK_N);
		newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.CTRL_MASK));
		newFile.addActionListener(this);
		fileMenu.add(newFile);
		// <open>
		newFileImage = new ImageIcon("open_icon.JPG");
		img = newFileImage.getImage();
		img = img
				.getScaledInstance(30, 10, java.awt.Image.SCALE_AREA_AVERAGING);
		newFileImage = new ImageIcon(img);
		open = new JMenuItem("Open...", newFileImage);
		open.setMnemonic(KeyEvent.VK_O);
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				ActionEvent.CTRL_MASK));
		open.addActionListener(this);
		fileMenu.add(open);
		// <save>
		newFileImage = new ImageIcon("save2_icon.PNG");
		img = newFileImage.getImage();
		img = img
				.getScaledInstance(30, 10, java.awt.Image.SCALE_AREA_AVERAGING);
		newFileImage = new ImageIcon(img);
		save = new JMenuItem("Save", newFileImage);
		save.setMnemonic(KeyEvent.VK_S);
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.CTRL_MASK));
		save.addActionListener(this);
		save.setEnabled(false);
		newFileImage = new ImageIcon("save_icon.JPG");
		img = newFileImage.getImage();
		img = img
				.getScaledInstance(30, 10, java.awt.Image.SCALE_AREA_AVERAGING);
		newFileImage = new ImageIcon(img);
		saveAs = new JMenuItem("Save as...", newFileImage);
		saveAs.setMnemonic(KeyEvent.VK_A);
		saveAs.addActionListener(this);
		saveAs.setEnabled(true);
		fileMenu.add(save);
		fileMenu.add(saveAs);
		//print
		newFileImage = new ImageIcon("save2_icon.PNG");
		img = newFileImage.getImage();
		img = img
				.getScaledInstance(30, 10, java.awt.Image.SCALE_AREA_AVERAGING);
		newFileImage = new ImageIcon(img);
		print = new JMenuItem("Print...", newFileImage);
		print.setMnemonic(KeyEvent.VK_P);
		print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				ActionEvent.CTRL_MASK));
		print.addActionListener(this);
		print.setEnabled(true);
		fileMenu.add(print);
		// <exit>
		newFileImage = new ImageIcon("exit_icon.PNG");
		img = newFileImage.getImage();
		img = img
				.getScaledInstance(30, 10, java.awt.Image.SCALE_AREA_AVERAGING);
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
		Undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				ActionEvent.CTRL_MASK));
		Undo.addActionListener(this);
		//
		Redo = new JMenuItem("Redo");
		Redo.setMnemonic(KeyEvent.VK_X);
		Redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				ActionEvent.CTRL_MASK));
		Redo.addActionListener(this);
		//
		clear = new JMenuItem("Clear");
		clear.setMnemonic(KeyEvent.VK_C);
		clear.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				ActionEvent.CTRL_MASK));
		clear.addActionListener(this);
		//
		clearImage = new JMenuItem("Clear Image");
		clearImage.setMnemonic(KeyEvent.VK_I);
		clearImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
				ActionEvent.CTRL_MASK));
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
			if(i == 10){
				tbButtons[i].addMouseListener(new PopupListener());
				System.out.println("Word ART");
				message = "";
				messageFont = new Font("Arial", Font.PLAIN, 26);
				foreColor = Color.black;
				backColor = Color.white;
				messageField = new JLabel(message);
				paintPanel.add(messageField);
				MouseListener fireCustomDialoag = new MouseListener() {
					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub
					}
					@Override
					public void mousePressed(MouseEvent e) {
						// TODO Auto-generated method stub
					}
					@Override
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub
					}
					@Override
					public void mouseEntered(MouseEvent e) {
						// TODO Auto-generated method stub
					}
					@Override
					public void mouseClicked(MouseEvent e) {
						// TODO Auto-generated method stub
						Object source = (JLabel) e.getSource();
						System.out.println("DIALOG.....");
						int j = cd.showCustomDialog(null, messageFont,
								foreColor, backColor);
						if (source == messageField) {
							if (j == WordArtCustomDialog.APPLY_OPTION) {
								messageFont = cd.getFont();
								foreColor = cd.getForeColor();
								backColor = cd.getBackColor();

								messageField
										.setPreferredSize(messageField
												.getPreferredSize());
								// messageField.setEditable(true);
								messageField
										.setHorizontalAlignment(SwingConstants.CENTER);
								// messageField
								// .setAlignmentX(Component.CENTER_ALIGNMENT);
								messageField
										.setToolTipText("Right click to change message");
								messageField.setText(message);
								messageField.setFont(messageFont);
								messageField.setForeground(foreColor);
								messageField.setBackground(backColor);
								paintPanel.add(messageField);
							}
						}
					}
				};
				messageField.addMouseListener(fireCustomDialoag);
				paintPanel.add(messageField);
			}
			else {
				System.out.println(i);
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
		case "rectangle":
			paintPanel.drawRect();
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
			System.out.println("MONITOR: "
					+ imageChooser.getTypeDescription(file));
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
						EXTENSIONS = ((FileNameExtensionFilter) imageChooser
								.getFileFilter()).getExtensions()[0];
						fileExist = file.getName();
						System.out.println("WHAT IS THE EXT and file Name:"
								+ EXTENSIONS + "->" + getSaveFileName() + "->"
								+ imageChooser.getSelectedFileWithExtension());
						SaveFile(file, EXTENSIONS);
						save.setEnabled(true);
					} else {
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} else if (source == position) {
			System.out.println("DropDownToolBar");
		} else if (source == clearImage) {
			paintPanel.clearImage();
		} else if (source == clear) {
			paintPanel.clear();
		}else if(source == print){
			BufferedImage imageToPrint = new BufferedImage(paintPanel.getWidth(),paintPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
			Printer painter = new Printer(imageToPrint);
			boolean observer = painter.imageUpdate(imageToPrint, ImageObserver.ALLBITS, 0, 0, imageToPrint.getWidth(), imageToPrint.getHeight());
			PrinterJob job = PrinterJob.getPrinterJob();
			PageFormat format = job.pageDialog(job.defaultPage());
			job.setPrintable(painter);
			boolean ok_to_print = job.printDialog();
			if(ok_to_print && observer){
				try{
					job.print();
				}catch(PrinterException ex){
					ex.getCause();
				}
			}
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

	protected int SaveFile(File file, String extension) {
		BufferedImage imageToSave = new BufferedImage(paintPanel.getWidth(),
				paintPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
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

	protected boolean fileExistenceChecker(File file)
			throws FileNotFoundException {
		Set<String> set = (Set<String>) ImageFileChooser.getAllFiles(file);
		final Object[] options = { "Yes", "No", "Cancel" };
		for (String s : set) {
			System.out.println("OK :" + s.toString() + "===> "
					+ getSaveFileName());
			if (s.toString().equals(getSaveFileName())) {
				return JOptionPane.showOptionDialog(this,
						"The file '" + file.getName() + "' already exists.  "
								+ "Replace existing file?", "Warning",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE, null, options, options[2]) == JOptionPane.YES_OPTION;
			}
		}
		return rootPaneCheckingEnabled;
	}

	private void setPaintColor() {
		Color color = JColorChooser.showDialog(null, "Choose Paint Color",
				Color.black);
		paintPanel.setColor(color);
	}

	private void loadImage() {
		JFileChooser imageSelector = new JFileChooser(
				System.getProperty("user.dir")); // try using this property for
		// the ImageFileChooser

		imageSelector.setDialogTitle("Select Image");
		imageSelector.setMultiSelectionEnabled(false);
		imageSelector.addChoosableFileFilter(new FileNameExtensionFilter(
				"Images Only", "jpg", "png", "gif"));
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
			JLabel s = new JLabel("Enter new message");
			Font font =  new Font(Font.MONOSPACED , Font.BOLD | Font.ITALIC , 20);
			s.setFont(font);
			String tmp = JOptionPane.showInputDialog(message, s.getText());
			//String tmp = (String) JOptionPane.showInputDialog(messageField, s.getText(), "Your Text goes here" ,1 , new ImageIcon(), null , 0);
			if (tmp != null && tmp.length() > 0) {
				message = tmp;
				updateMessage();
				paintPanel.add(messageField);
			}
		}
	}

}
