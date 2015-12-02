
/**
 * @author Edgar Zaganjori, Daniyal Javed, Richmond Frimpong
 * @course EECS3461 
 * @title ColoredShape
 */

import java.awt.*;

/*
 * Colored shape class
 */
public class ColoredShape {
	private Shape shape;
	private Color color;

	/*
	 * @param Shape, color set the shape and the color
	 * 
	 * @return shape, color
	 */
	public ColoredShape(Shape shape, Color color) {
		this.shape = shape;
		this.color = color;
	}

	/*
	 * @return the shape passed
	 */
	public Shape getShape() {
		return shape;
	}

	/*
	 * @return the color of the shape
	 */
	public Color getColor() {
		return color;
	}
}