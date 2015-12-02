
/**
 * @author Edgar Zaganjori, Daniyal Javed, Richmond Frimpong
 * @course EECS3461 
 * @title LineSegment
 */
import java.awt.*;
import java.awt.geom.Line2D;

public class LineSegment {
	private Line2D.Double coordinates;
	private Color color;
	private Stroke stroke;

	/**
	 * Class to store color stroke and place of free hand drawing
	 * 
	 * @param coordinates
	 * @param color
	 * @param stroke
	 */
	public LineSegment(Line2D.Double coordinates, Color color, Stroke stroke) {
		this.coordinates = coordinates;
		this.color = color;
		this.stroke = stroke;
	}

	/**
	 * draw method for free hand drawing
	 * 
	 * @param graphics2D
	 */
	public void draw(Graphics2D graphics2D) {
		graphics2D.setColor(color);
		graphics2D.setStroke(stroke);
		graphics2D.draw(coordinates);
	}
}