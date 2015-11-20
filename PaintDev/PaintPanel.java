
import javax.swing.*;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class PaintPanel extends JPanel {
	static final long serialVersionUID = 1L;
	Graphics2D g2; // only possible way i could get a reference to the image we
					// draw
	private Stroke THIN_LINE_STROKE = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

	private Stroke THICK_LINE_STROKE = new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	private Stroke ERASER_STROKE = new BasicStroke(10.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);

	static Color LINE_COLOR = new Color(0, 0, 0);
	private Stroke LINE_STROKE = this.THIN_LINE_STROKE;
	private Vector<Entity> vectorForString;
	private Vector<Line2D.Double> allStrokes;
	private Vector<Line2D.Double> eraserStrokes;
	private Vector<Line2D.Double> redoAllStrokes;
	private static ArrayList<Shape> redoStruct = new ArrayList<Shape>();

	Rectangle rect;

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
		redoAllStrokes = new Vector<Line2D.Double>();
		vectorForString = new Vector<Entity>();
		allStrokes = new Vector<Line2D.Double>();
		eraserStrokes = new Vector<Line2D.Double>();
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		// width = img.getWidth(this);
		// height = img.getHeight(this);
		width = 250;
		height = 250;
		xOffset = 0;
		yOffset = 0;
	}

	public static Color getColor() {
		return LINE_COLOR;
	}

	public void setColor(Color color) {
		PaintPanel.LINE_COLOR = color;
	}

	public void setThickBrush() {
		this.LINE_STROKE = this.THICK_LINE_STROKE;
	}

	public void setThinBrush() {
		this.LINE_STROKE = this.THIN_LINE_STROKE;
	}

	public void setEraser() {
		this.LINE_STROKE = this.ERASER_STROKE;
	}

	/*
	 * Paint all the line segments stored in the vector
	 */
	private void paintInkStrokes(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		// set the inking color
		g2.setColor(LINE_COLOR);

		// set the stroke thickness, and cap and join attributes ('round')
		Stroke s = g2.getStroke(); // save current stroke
		g2.setStroke(LINE_STROKE); // set desired stroke

		// retrive each line segment and draw it
		for (int i = 0; i < allStrokes.size(); ++i) {
			g2.draw(allStrokes.elementAt(i));
		}

		g2.setStroke(s); // restore stroke
	}

	private void paintEraser(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		// set the inking color
		g2.setColor(Color.WHITE);

		// set the stroke thickness, and cap and join attributes ('round')
		Stroke s = g2.getStroke(); // save current stroke
		g2.setStroke(LINE_STROKE); // set desired stroke

		// retrive each line segment and draw it
		for (int i = 0; i < eraserStrokes.size(); ++i) {
			g2.draw(eraserStrokes.elementAt(i));
		}

		g2.setStroke(s); // restore stroke
	}

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

		allStrokes.add(inkSegment); // add to vector
		eraserStrokes.add(inkSegment); // add to vector
	}

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
		allStrokes.add(inkSegment); // add to vector
	}

	public void addImage(Image image) {
		img = image;
		MediaTracker mt = new MediaTracker(this);
		try {
			mt.waitForAll();
		} catch (InterruptedException e) {
			System.out.printf("Failed to print image: %s\n", image.toString());
		}
		Graphics2D g2 = (Graphics2D) this.getGraphics();
		g2.drawImage(image, 0, 0, width, height, this);
		// g2.drawImage(image ,0 , 0 , this);
	}

	public Shape getImage() {
		Shape paint = g2.getClip();
		return paint.getBounds2D();

	}

	public void clear() {
		allStrokes.clear();
		this.repaint();
	}

	public void clearImage() {
		Graphics2D graphics = (Graphics2D) this.getGraphics();
		System.out.println(graphics.toString());
		graphics.clearRect(x, y, width, height);
	}

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
		paintEntities(g);

		x = this.getWidth() / 2 - width / 2 + xOffset / 2;
		y = this.getHeight() / 2 - height / 2 + yOffset / 2;
		g.drawImage(img, x, y, width, height, this);
	}

	public void makeBigger() {
		width = width + INC;
		height = height + INC;
		setPreferredSize(new Dimension(width, height));
		this.repaint();
	}

	public void makeSmaller() {
		width = width - INC;
		height = height - INC;
		setPreferredSize(new Dimension(width, height));
		this.repaint();
	}

	public void makeWider() {
		width = width + INC;
		setPreferredSize(new Dimension(width, height));
		this.repaint();
	}

	public void makeNarrower() {
		width = width - INC;
		setPreferredSize(new Dimension(width, height));
		this.repaint();
	}

	public void makeTaller() {
		height = height + 10;
		setPreferredSize(new Dimension(width, height));
		this.repaint();
	}

	public void makeShorter() {
		height = height - 10;
		setPreferredSize(new Dimension(width, height));
		this.repaint();
	}

	public void moveUp() {
		yOffset = yOffset - INC;
		this.repaint();
	}

	public void moveDown() {
		yOffset = yOffset + INC;
		this.repaint();
	}

	public void moveLeft() {
		xOffset = xOffset - INC;
		this.repaint();
	}

	public void moveRight() {
		xOffset = xOffset + INC;
		this.repaint();
	}

	public void resetImage() {
		width = img.getWidth(this);
		height = img.getHeight(this);
		setPreferredSize(new Dimension(width, height));
		xOffset = 0;
		yOffset = 0;
		this.repaint();
	}

	public void undo() {
		/* undo method for rectStruct */
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
		} else if (!PaintAppFrame.circFillStruct.isEmpty()) {
			for (int i = 0; i < PaintAppFrame.circFillStruct.size(); i++) {
				redoStruct.add(PaintAppFrame.circFillStruct.get(i));
			}
			int size = PaintAppFrame.circFillStruct.size() - 1;
			PaintAppFrame.circFillStruct.remove(size);
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
		} else if (!PaintAppFrame.lineStruct.isEmpty()) {
			for (int i = 0; i < PaintAppFrame.lineStruct.size(); i++) {
				redoStruct.add(PaintAppFrame.lineStruct.get(i));
			}
			int size = PaintAppFrame.lineStruct.size() - 1;
			PaintAppFrame.lineStruct.remove(size);
			repaint();
		}

	}

	public void redo() {
		if (!PaintAppFrame.rectStruct.isEmpty() && !redoStruct.isEmpty()) {
			for (int i = 0; i < redoStruct.size(); i++) {
				PaintAppFrame.rectStruct.add(redoStruct.get(i));
			}
			repaint();
		}
		if (!PaintAppFrame.circStruct.isEmpty() && !redoStruct.isEmpty()) {
			for (int i = 0; i < redoStruct.size(); i++) {
				PaintAppFrame.circStruct.add(redoStruct.get(i));
			}
			repaint();
		}
		if (!PaintAppFrame.rectFillStruct.isEmpty() && !redoStruct.isEmpty()) {
			for (int i = 0; i < redoStruct.size(); i++) {
				PaintAppFrame.rectFillStruct.add(redoStruct.get(i));
			}
			repaint();
		}
		if (!PaintAppFrame.circFillStruct.isEmpty() && !redoStruct.isEmpty()) {
			for (int i = 0; i < redoStruct.size(); i++) {
				PaintAppFrame.circFillStruct.add(redoStruct.get(i));
			}
			repaint();
		}
		if (!PaintAppFrame.roundRectStruct.isEmpty() && !redoStruct.isEmpty()) {
			for (int i = 0; i < redoStruct.size(); i++) {
				PaintAppFrame.roundRectStruct.add(redoStruct.get(i));
			}
			repaint();
		}
		if (!PaintAppFrame.roundRectFillStruct.isEmpty() && !redoStruct.isEmpty()) {
			for (int i = 0; i < redoStruct.size(); i++) {
				PaintAppFrame.roundRectFillStruct.add(redoStruct.get(i));
			}
			repaint();
		}
		if (!PaintAppFrame.lineStruct.isEmpty() && !redoStruct.isEmpty()) {
			for (int i = 0; i < redoStruct.size(); i++) {
				PaintAppFrame.lineStruct.add(redoStruct.get(i));
			}
			repaint();
		}
		if (!allStrokes.isEmpty() && !redoAllStrokes.isEmpty()) {
			for (int i = 0; i < redoAllStrokes.size(); i++) {
				allStrokes.addElement(redoAllStrokes.elementAt(i));
			}
			repaint();
		}
	}

	// RIchmond changes dont touch
	public void addWordArt(String s) {
		Graphics2D g = (Graphics2D) this.getGraphics();
		g.drawString(s, x, y);
	}

	public void customePaint(Object obj, int type, int x, int y, Font f, Color b, Color c) {
		vectorForString.addElement(new Entity(obj, type, x, y, f, b, c));
		this.repaint();
	}

	private void paintEntities(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		for (int i = 0; i < vectorForString.size(); i++) {
			Entity e = (Entity) vectorForString.elementAt(i);
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
	}
}