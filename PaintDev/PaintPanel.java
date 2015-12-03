
/**
 * @author Edgar Zaganjori, Daniyal Javed, Richmond Frimpong
 * @course EECS3461 
 * @title PaintPanel
 */

import javax.swing.*;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.*;

public class PaintPanel extends JPanel {
	static final long serialVersionUID = 1L;
	Graphics2D g2;
	/* initthe strokes */
	private Stroke THIN_LINE_STROKE = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	private Stroke THICK_LINE_STROKE = new BasicStroke(4.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	private Stroke THICK_LINE_STROKE3 = new BasicStroke(7.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	private Stroke THICK_LINE_STROKE4 = new BasicStroke(9.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	private Stroke ERASER_STROKE = new BasicStroke(10.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);
	/* default black line color */
	static Color LINE_COLOR = new Color(0, 0, 0);
	private Stroke LINE_STROKE = this.THIN_LINE_STROKE;
	/* Data structures for the strokes */
	private Vector<Entity> vectorForString;
	private Vector<Entity> vectorForImages;
	private Vector<Entity> redoString;
	private Vector<LineSegment> allStrokes;
	private Vector<LineSegment> eraserStrokes;
	private Vector<LineSegment> redoAllStrokes;
	private static ArrayList<ColoredShape> redoStruct = new ArrayList<ColoredShape>();

	Graphics2D gr;
	Image img;
	private int x;
	private int y;
	private int width;
	private int height;
	private int xOffset;
	private int yOffset;
	final int INC = 10;

	public PaintPanel() {
		// init strokes & background
		redoAllStrokes = new Vector<LineSegment>();
		vectorForString = new Vector<Entity>();
		vectorForImages = new Vector<Entity>();
		allStrokes = new Vector<LineSegment>();
		eraserStrokes = new Vector<LineSegment>();
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		width = 250;
		height = 250;
		xOffset = 0;
		yOffset = 0;
	}

	/**
	 * @return the color
	 */
	public static Color getColor() {
		return LINE_COLOR;
	}

	/**
	 * set the color
	 * 
	 * @param Color
	 */
	public void setColor(Color color) {
		PaintPanel.LINE_COLOR = color;
	}

	/**
	 * set the brush to the thick brush
	 */
	public void setThickBrush() {
		this.LINE_STROKE = this.THICK_LINE_STROKE;
	}

	/**
	 * set the brush to level 3 thickness
	 */
	public void setThickBrush3() {
		this.LINE_STROKE = this.THICK_LINE_STROKE3;
	}

	/**
	 * set the brush to level 4 thickness
	 */
	public void setThickBrush4() {
		this.LINE_STROKE = this.THICK_LINE_STROKE4;
	}

	/**
	 * use the pencil/thin brush
	 */
	public void setThinBrush() {
		this.LINE_STROKE = this.THIN_LINE_STROKE;
	}

	/**
	 * set the eraser
	 */
	public void setEraser() {
		this.LINE_STROKE = this.ERASER_STROKE;
	}

	/**
	 * Paint all the line segments stored in the vector
	 */
	private void paintInkStrokes(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(LINE_COLOR);
		Stroke s = g2.getStroke(); // save current stroke
		g2.setStroke(LINE_STROKE); // set desired stroke

		for (LineSegment s1 : allStrokes) {
			s1.draw(g2);
		}
		g2.setStroke(s); // restore stroke
	}

	/**
	 * Prepares eraser
	 * 
	 * @param g
	 */
	private void paintEraser(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		// set the inking color
		g2.setColor(Color.WHITE);
		// set the stroke thickness, and cap and join attributes ('round')
		Stroke s = g2.getStroke(); // save current stroke
		g2.setStroke(LINE_STROKE); // set desired stroke
		// retrive each line segment and draw it
		for (LineSegment s1 : allStrokes) {
			s1.draw(g2);
		}
		g2.setStroke(s); // restore stroke
	}

	/**
	 * Paints the eraser
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void drawEraser(int x1, int y1, int x2, int y2) {
		// get graphics context
		g2 = (Graphics2D) this.getGraphics();
		// create the line
		Line2D.Double inkSegment = new Line2D.Double(x1, y1, x2, y2);
		g2.setColor(Color.WHITE); // set the inking color
		Stroke s = g2.getStroke(); // save current stroke
		g2.setStroke(LINE_STROKE); // set desired stroke
		g2.draw(inkSegment); // draw it!
		g2.setStroke(s); // restore stroke
		allStrokes.add(new LineSegment(inkSegment, Color.white, s)); // add to
		eraserStrokes.add(new LineSegment(inkSegment, Color.white, s));
	}

	/**
	 * Draw ink for free hand
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void drawInk(int x1, int y1, int x2, int y2) {
		// get graphics context
		g2 = (Graphics2D) this.getGraphics();
		// create the line
		Line2D.Double inkSegment = new Line2D.Double(x1, y1, x2, y2);
		g2.setColor(LINE_COLOR); // set the inking color
		Stroke s = g2.getStroke(); // save current stroke
		g2.setStroke(LINE_STROKE); // set desired stroke
		g2.draw(inkSegment); // draw it!
		g2.setStroke(s); // restore stroke
		allStrokes.add(new LineSegment(inkSegment, LINE_COLOR, LINE_STROKE));
	}

	/**
	 * 
	 * @return Shape
	 */
	public Shape getImage() {
		Shape paint = g2.getClip();
		return paint.getBounds2D();
	}

	/**
	 * clear method removes everything
	 */
	public void clear() {
		allStrokes.clear();
		this.setBackground(Color.white);
		if (!PaintAppFrame.circFillStruct.isEmpty()) {
			PaintAppFrame.circFillStruct.clear();
		}
		if (!PaintAppFrame.circStruct.isEmpty()) {
			PaintAppFrame.circStruct.clear();
		}
		if (!PaintAppFrame.circTransStruct.isEmpty()) {
			PaintAppFrame.circTransStruct.clear();
		}
		if (!PaintAppFrame.lineStruct.isEmpty()) {
			PaintAppFrame.lineStruct.clear();
		}
		if (!PaintAppFrame.lineStruct2.isEmpty()) {
			PaintAppFrame.lineStruct2.clear();
		}
		if (!PaintAppFrame.lineStruct3.isEmpty()) {
			PaintAppFrame.lineStruct3.clear();
		}
		if (!PaintAppFrame.rectTransStruct.isEmpty()) {
			PaintAppFrame.rectTransStruct.clear();
		}
		if (!PaintAppFrame.rectStruct.isEmpty()) {
			PaintAppFrame.rectStruct.clear();
		}
		if (!PaintAppFrame.rectFillStruct.isEmpty()) {
			PaintAppFrame.rectFillStruct.clear();
		}
		if (!PaintAppFrame.roundRectStruct.isEmpty()) {
			PaintAppFrame.roundRectStruct.clear();
		}
		if (!PaintAppFrame.roundRectFillStruct.isEmpty()) {
			PaintAppFrame.roundRectFillStruct.clear();
		}
		if (!PaintAppFrame.roundRectTransStruct.isEmpty()) {
			PaintAppFrame.roundRectTransStruct.clear();
		}
		if (!eraserStrokes.isEmpty()) {
			eraserStrokes.clear();
		}
		clearImage();
		this.repaint();
	}

	/**
	 * remove image loaded
	 */
	public void clearImage() {
		Graphics2D graphics = (Graphics2D) this.getGraphics();
		graphics.clearRect(x, y, width, height);
		graphics.dispose();
	}

	/**
	 * Overriden paintComponent which throws everything to the panel
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // paint background
		paintInkStrokes(g);
		paintEraser(g);
		PaintAppFrame.paintRect(g);
		PaintAppFrame.paintCircle(g);
		PaintAppFrame.paintRectFill(g);
		PaintAppFrame.paintFillCircle(g);
		PaintAppFrame.paintRoundRectangle(g);
		PaintAppFrame.paintRoundRectangleFill(g);
		PaintAppFrame.paintLine(g);
		PaintAppFrame.paintLine2(g);
		PaintAppFrame.paintLine3(g);
		PaintAppFrame.paintTransCircle(g);
		PaintAppFrame.paintTransRectangle(g);
		PaintAppFrame.paintTransRoundRectangle(g);
		paintEntities(g);
		//
		x = this.getWidth() / 2 - width / 2 + xOffset / 2;
		y = this.getHeight() / 2 - height / 2 + yOffset / 2;
		g.drawImage(img, x, y, width, height, this);
		//
	}

	/**
	 * Toolbox method
	 */
	public void makeBigger() {
		width = width + INC;
		height = height + INC;
		setPreferredSize(new Dimension(width, height));
		this.repaint();
	}

	/**
	 * Toolbox method
	 */
	public void makeSmaller() {
		width = width - INC;
		height = height - INC;
		setPreferredSize(new Dimension(width, height));
		this.repaint();
	}

	/**
	 * Toolbox method
	 */
	public void makeWider() {
		width = width + INC;
		setPreferredSize(new Dimension(width, height));
		this.repaint();
	}

	/**
	 * Toolbox method
	 */
	public void makeNarrower() {
		width = width - INC;
		setPreferredSize(new Dimension(width, height));
		this.repaint();
	}

	/**
	 * Toolbox method
	 */
	public void makeTaller() {
		height = height + 10;
		setPreferredSize(new Dimension(width, height));
		this.repaint();
	}

	/**
	 * Toolbox method
	 */
	public void makeShorter() {
		height = height - 10;
		setPreferredSize(new Dimension(width, height));
		this.repaint();
	}

	/**
	 * Toolbox method
	 */
	public void moveUp() {
		yOffset = yOffset - INC;
		this.repaint();
	}

	/**
	 * Toolbox method
	 */
	public void moveDown() {
		yOffset = yOffset + INC;
		this.repaint();
	}

	/**
	 * Toolbox method
	 */
	public void moveLeft() {
		xOffset = xOffset - INC;
		this.repaint();
	}

	/**
	 * Toolbox method
	 */
	public void moveRight() {
		xOffset = xOffset + INC;
		this.repaint();
	}

	/**
	 * reset image to original size
	 */
	public void resetImage() {
		Component frame = null;
		if (img != null) {
			try {
				width = img.getWidth(this);
				height = img.getHeight(this);
				setPreferredSize(new Dimension(width, height));
				xOffset = 0;
				yOffset = 0;
				this.repaint();
			} catch (NoSuchElementException ex) {
				JOptionPane.showMessageDialog(frame, "Image is damaged");
			}
		} else if (img == null) {
			JOptionPane.showMessageDialog(frame, " No Image to Resize! Please load an image first.");
		}
	}

	/**
	 * method to rotate images
	 */
	public void rotateImage(double degrees, ImageObserver obs) {
		Component frame = null;
		if (this.img == null) {
			JOptionPane.showMessageDialog(frame, " Unsupported operation! Please load an image first.");
		} else {
			ImageIcon icon = new ImageIcon(this.img);
			BufferedImage blankCanvas = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(),
					BufferedImage.TYPE_INT_ARGB);
			/// now rotate graphics
			//System.out.println("HERE");
			Graphics2D g2 = (Graphics2D) blankCanvas.getGraphics();
			g2.rotate(Math.toRadians(degrees), icon.getIconWidth() / 2, icon.getIconHeight() / 2);
			g2.drawImage(this.img, 0, 0, obs);
			this.img = blankCanvas;
			this.repaint();
		}
	}

	/**
	 * undo method, usually by removing last element from data structures
	 */
	public void undo() {
		if (!allStrokes.isEmpty()) {
			for (int i = 0; i < allStrokes.size(); i++) {
				redoAllStrokes.addElement(allStrokes.elementAt(i));
			}
			int size = allStrokes.size() - 1;
			System.out.println(allStrokes.size());
			allStrokes.remove(size);
			repaint();
		}
		if (!PaintAppFrame.rectStruct.isEmpty()) {
			for (int i = 0; i < PaintAppFrame.rectStruct.size(); i++) {
				redoStruct.add(PaintAppFrame.rectStruct.get(i));
			}
			int size = PaintAppFrame.rectStruct.size() - 1;
			PaintAppFrame.rectStruct.remove(size);
			repaint();
		} else if (!PaintAppFrame.circStruct.isEmpty()) {
			for (int i = 0; i < PaintAppFrame.circStruct.size(); i++) {
				redoStruct.add(PaintAppFrame.circStruct.get(i));
			}
			int size = PaintAppFrame.circStruct.size() - 1;
			PaintAppFrame.circStruct.remove(size);
			repaint();
		} else if (!PaintAppFrame.rectFillStruct.isEmpty()) {
			for (int i = 0; i < PaintAppFrame.rectFillStruct.size(); i++) {
				redoStruct.add(PaintAppFrame.rectFillStruct.get(i));
			}
			int size = PaintAppFrame.rectFillStruct.size() - 1;
			PaintAppFrame.rectFillStruct.remove(size);
			repaint();
		} else if (!PaintAppFrame.rectTransStruct.isEmpty()) {
			for (int i = 0; i < PaintAppFrame.rectTransStruct.size(); i++) {
				redoStruct.add(PaintAppFrame.rectTransStruct.get(i));
			}
			int size = PaintAppFrame.rectTransStruct.size() - 1;
			PaintAppFrame.rectTransStruct.remove(size);
			repaint();
		} else if (!PaintAppFrame.circFillStruct.isEmpty()) {
			for (int i = 0; i < PaintAppFrame.circFillStruct.size(); i++) {
				redoStruct.add(PaintAppFrame.circFillStruct.get(i));
			}
			int size = PaintAppFrame.circFillStruct.size() - 1;
			PaintAppFrame.circFillStruct.remove(size);
			repaint();
		} else if (!PaintAppFrame.circTransStruct.isEmpty()) {
			for (int i = 0; i < PaintAppFrame.circTransStruct.size(); i++) {
				redoStruct.add(PaintAppFrame.circTransStruct.get(i));
			}
			int size = PaintAppFrame.circTransStruct.size() - 1;
			PaintAppFrame.circTransStruct.remove(size);
			repaint();
		} else if (!PaintAppFrame.roundRectStruct.isEmpty()) {
			for (int i = 0; i < PaintAppFrame.roundRectStruct.size(); i++) {
				redoStruct.add(PaintAppFrame.roundRectStruct.get(i));
			}
			int size = PaintAppFrame.roundRectStruct.size() - 1;
			PaintAppFrame.roundRectStruct.remove(size);
			repaint();
		} else if (!PaintAppFrame.roundRectFillStruct.isEmpty()) {
			for (int i = 0; i < PaintAppFrame.roundRectFillStruct.size(); i++) {
				redoStruct.add(PaintAppFrame.roundRectFillStruct.get(i));
			}
			int size = PaintAppFrame.roundRectFillStruct.size() - 1;
			PaintAppFrame.roundRectFillStruct.remove(size);
			repaint();
		} else if (!PaintAppFrame.roundRectTransStruct.isEmpty()) {
			for (int i = 0; i < PaintAppFrame.roundRectTransStruct.size(); i++) {
				redoStruct.add(PaintAppFrame.roundRectTransStruct.get(i));
			}
			int size = PaintAppFrame.roundRectTransStruct.size() - 1;
			PaintAppFrame.roundRectTransStruct.remove(size);
			repaint();
		} else if (!PaintAppFrame.lineStruct.isEmpty()) {
			for (int i = 0; i < PaintAppFrame.lineStruct.size(); i++) {
				redoStruct.add(PaintAppFrame.lineStruct.get(i));
			}
			int size = PaintAppFrame.lineStruct.size() - 1;
			PaintAppFrame.lineStruct.remove(size);
			repaint();
		} else if (!PaintAppFrame.lineStruct2.isEmpty()) {
			for (int i = 0; i < PaintAppFrame.lineStruct2.size(); i++) {
				redoStruct.add(PaintAppFrame.lineStruct2.get(i));
			}
			int size = PaintAppFrame.lineStruct2.size() - 1;
			PaintAppFrame.lineStruct2.remove(size);
			repaint();
		} else if (!PaintAppFrame.lineStruct3.isEmpty()) {
			for (int i = 0; i < PaintAppFrame.lineStruct3.size(); i++) {
				redoStruct.add(PaintAppFrame.lineStruct3.get(i));
			}
			int size = PaintAppFrame.lineStruct3.size() - 1;
			PaintAppFrame.lineStruct3.remove(size);
			repaint();
		} else if (!eraserStrokes.isEmpty()) {
			for (int i = 0; i < eraserStrokes.size(); i++) {
				redoAllStrokes.addElement(eraserStrokes.elementAt(i));
			}
			int size = eraserStrokes.size() - 1;
			eraserStrokes.remove(size);
			repaint();
		}
		while (!vectorForString.isEmpty()) {
			int size = vectorForString.size() - 1;
			vectorForString.remove(size);
			repaint();
		}
	}

	/**
	 * redo method, if a move is undone it is stored into a redo data structure,
	 * and then it is restored when pressed redo button
	 */
	public void redo() {
		if (!PaintAppFrame.rectStruct.isEmpty()) {
			for (int i = 0; i < redoStruct.size(); i++) {
				PaintAppFrame.rectStruct.add(redoStruct.get(i));
			}
			redoStruct.clear();
			repaint();
		} else if (!PaintAppFrame.circStruct.isEmpty()) {
			for (int i = 0; i < redoStruct.size(); i++) {
				PaintAppFrame.circStruct.add(redoStruct.get(i));
			}
			redoStruct.clear();
			repaint();
		} else if (!PaintAppFrame.rectFillStruct.isEmpty()) {
			for (int i = 0; i < redoStruct.size(); i++) {
				PaintAppFrame.rectFillStruct.add(redoStruct.get(i));
			}
			redoStruct.clear();
			repaint();
		} else if (!PaintAppFrame.circFillStruct.isEmpty()) {
			for (int i = 0; i < redoStruct.size(); i++) {
				PaintAppFrame.circFillStruct.add(redoStruct.get(i));
			}
			redoStruct.clear();
			repaint();
		} else if (!PaintAppFrame.roundRectStruct.isEmpty()) {
			for (int i = 0; i < redoStruct.size(); i++) {
				PaintAppFrame.roundRectStruct.add(redoStruct.get(i));
			}
			redoStruct.clear();
			repaint();
		} else if (!PaintAppFrame.roundRectFillStruct.isEmpty()) {
			for (int i = 0; i < redoStruct.size(); i++) {
				PaintAppFrame.roundRectFillStruct.add(redoStruct.get(i));
			}
			redoStruct.clear();
			repaint();
		} else if (!PaintAppFrame.lineStruct.isEmpty()) {
			for (int i = 0; i < redoStruct.size(); i++) {
				PaintAppFrame.lineStruct.add(redoStruct.get(i));
			}
			redoStruct.clear();
			repaint();
		} else if (!PaintAppFrame.lineStruct2.isEmpty()) {
			for (int i = 0; i < redoStruct.size(); i++) {
				PaintAppFrame.lineStruct2.add(redoStruct.get(i));
			}
			redoStruct.clear();
			repaint();
		} else if (!PaintAppFrame.lineStruct3.isEmpty()) {
			for (int i = 0; i < redoStruct.size(); i++) {
				PaintAppFrame.lineStruct3.add(redoStruct.get(i));
			}
			redoStruct.clear();
			repaint();
		} else if (!allStrokes.isEmpty() && !redoAllStrokes.isEmpty()) {
			for (int i = 0; i < redoAllStrokes.size(); i++) {
				allStrokes.addElement(redoAllStrokes.elementAt(i));
			}
			repaint();
		} else if (!eraserStrokes.isEmpty() && !redoAllStrokes.isEmpty()) {
			for (int i = 0; i < redoAllStrokes.size(); i++) {
				eraserStrokes.addElement(redoAllStrokes.elementAt(i));
			}
			repaint();
		} else if (!PaintAppFrame.roundRectTransStruct.isEmpty() && !redoStruct.isEmpty()) {
			for (int i = 0; i < redoStruct.size(); i++) {
				PaintAppFrame.roundRectTransStruct.add(redoStruct.get(i));
			}
			redoStruct.clear();
			repaint();
		} else if (!PaintAppFrame.rectTransStruct.isEmpty() && !redoStruct.isEmpty()) {
			for (int i = 0; i < redoStruct.size(); i++) {
				PaintAppFrame.rectTransStruct.add(redoStruct.get(i));
			}
			redoStruct.clear();
			repaint();
		} else if (!PaintAppFrame.circTransStruct.isEmpty() && !redoStruct.isEmpty()) {
			for (int i = 0; i < redoStruct.size(); i++) {
				PaintAppFrame.circTransStruct.add(redoStruct.get(i));
			}
			redoStruct.clear();
			repaint();
		}
	}

	/**
	 * insert text
	 */
	public void addWordArt(String s) {
		Graphics2D g = (Graphics2D) this.getGraphics();
		g.drawString(s, x, y);
	}

	public void customePaint(Object obj, int type, int x, int y, Font f, Color b, Color c) {
		vectorForString.addElement(new Entity(obj, type, x, y, f, b, c));
		this.repaint();
	}

	/**
	 * load an image
	 */
	public Image addImage(Image image) {
		img = image;
		MediaTracker mt = new MediaTracker(this);
		try {
			mt.waitForAll();
		} catch (InterruptedException e) {
			System.out.printf("Failed to print image: %s\n", image.toString());
		}
		Graphics2D g2 = (Graphics2D) this.getGraphics();
		g2.drawImage(image, x , y, width, height, this);
		this.repaint();
		return img;
	}

	/**
	 * draw over images
	 */
	public void customDrawImage(Object obj, int type, int width, int height) {
		vectorForImages.add(new Entity(obj, type));
		this.repaint();
	}

	/**
	 * Paint the entities
	 * 
	 * @param g
	 */
	private void paintEntities(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		for (int i = 0; i < vectorForString.size(); i++) {
			Entity e = vectorForString.elementAt(i);
			int x = e.getX();
			int y = e.getY();
			switch (e.getType()) {
			case Entity.STRING:
				g2.setColor(e.getForeColor());
				g2.setBackground(e.getBackColor());
				g2.setFont(e.getFont());
				g2.drawString((String) e.Entity(), x, y);
				break;
			}
		}
		// draw images
		for (int j = 0; j < vectorForImages.size(); j++) {
			System.out.println("SIZE OF IMAGE VECTOR ->" + vectorForImages.size());
			Entity img = vectorForImages.elementAt(j);
			this.img = (Image) img.Entity();
			int w = this.img.getWidth(this);
			int h = this.img.getHeight(this);
			switch (img.getType()) {
			case Entity.IMAGE:
				x = this.getWidth() / 2 - width / 2 + xOffset / 2;
				y = this.getHeight() / 2 - height / 2 + yOffset / 2;
				System.out.println("In Images x => y " + w + " " + h);
				g2.drawImage(this.img, x, y, w, h, new ImageObserver() {
					@Override
					public boolean imageUpdate(Image img, int flags, int x, int y, int width, int height) {
						if ((flags & HEIGHT) != 0)
							System.out.println("Image height = " + height);
						if ((flags & WIDTH) != 0)
							System.out.println("Image width = " + width);
						if ((flags & FRAMEBITS) != 0)
							System.out.println("Another frame finished.");
						if ((flags & SOMEBITS) != 0)
							System.out.println("Image section :" + new Rectangle(x, y, width, height));
						if ((flags & ALLBITS) != 0) {
							System.out.println("Image finished!");
							return false;
						}
						if ((flags & ABORT) != 0) {
							System.out.println("Image load aborted...");
							return false;
						}
						return true;
					}
				});
				repaint();
				break;
			}
		}
	}

	/**
	 * empty word art
	 */
	public void clearWordArt() {
		vectorForString.clear();
		this.repaint();
	}
}