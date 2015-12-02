
/**
 * @author Edgar Zaganjori, Daniyal Javed, Richmond Frimpong
 * @course EECS3461 
 * @title Entity
 */

import java.awt.Color;
import java.awt.Font;

public class Entity {
	public static final int STRING = 0;
	public static final int IMAGE = 1;
	private Object entity;
	private Font font;
	private Color backColor;
	private Color foreColor;
	private int type;
	private int x;
	private int y;
	private int width;
	private int height;

	/**
	 * 
	 * @param ent
	 * @param typeArg
	 * @param xArg
	 * @param yArg
	 *            default method for Entity
	 */

	Entity(Object ent, int typeArg, int xArg, int yArg) {
		entity = ent;
		type = typeArg;
		x = xArg;
		y = yArg;
	}

	/**
	 * 
	 * @param ent
	 * @param typeArg
	 */
	Entity(Object ent, int typeArg) {
		entity = ent;
		type = typeArg;
	}

	/**
	 * 
	 * @param ent
	 * @param typeArg
	 * @param xArg
	 * @param yArg
	 * @param tempf
	 * @param back
	 * @param fore
	 */
	Entity(Object ent, int typeArg, int xArg, int yArg, Font tempf, Color back, Color fore) {
		entity = ent;
		type = typeArg;
		x = xArg;
		y = yArg;
		font = tempf;
		backColor = back;
		foreColor = fore;
	}

	/**
	 * Default constructor
	 * 
	 * @return entity
	 */
	public Object Entity() {
		return entity;
	}

	/**
	 * 
	 * @return type
	 */

	public int getType() {
		return type;
	}

	/**
	 * 
	 * @return x
	 */
	public int getX() {
		return x;
	}

	/**
	 * 
	 * @return y
	 */
	public int getY() {
		return y;
	}

	/**
	 * 
	 * @return width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * 
	 * @return height
	 */

	public int getHeight() {
		return height;
	}

	/**
	 * 
	 * @return font
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * 
	 * @return foreColor
	 */
	public Color getForeColor() {
		return foreColor;
	}

	/**
	 * 
	 * @return backColor
	 */
	public Color getBackColor() {
		return backColor;
	}

	/**
	 * toString method return String
	 */
	public String toString() {
		return "Object type " + entity.getClass() + "-->" + font.getName();
	}
}
