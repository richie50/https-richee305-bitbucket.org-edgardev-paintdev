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
	static Color LINE_COLOR = new Color(0, 0, 0);
	private Stroke LINE_STROKE = this.THIN_LINE_STROKE;
	private Vector<Line2D.Double> allStrokes;
	
	private Color prevColor;


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
		allStrokes = new Vector<Line2D.Double>();
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
		PaintPanel.LINE_COLOR = this.prevColor;
		this.LINE_STROKE = this.THICK_LINE_STROKE;
	}

	public void setThinBrush() {
		PaintPanel.LINE_COLOR = this.prevColor;
		this.LINE_STROKE = this.THIN_LINE_STROKE;
	}
	
	public void setEraser() {
		prevColor = PaintPanel.LINE_COLOR;
		PaintPanel.LINE_COLOR = Color.WHITE;
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
		PaintAppFrame.paintRect(g);
		PaintAppFrame.paintCircle(g);
		PaintAppFrame.paintRectFill(g);
		PaintAppFrame.paintFillCircle(g);
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
}