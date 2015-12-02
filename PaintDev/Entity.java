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

	Entity(Object ent, int typeArg, int xArg, int yArg) {
		entity = ent;
		type = typeArg;
		x = xArg;
		y = yArg;
	}
	Entity(Object ent, int typeArg) {
		entity = ent;
		type = typeArg;
	}
	Entity(Object ent, int typeArg, int xArg, int yArg ,Font tempf, Color back , Color fore) {
		entity = ent;
		type = typeArg;
		x = xArg;
		y = yArg;
		font = tempf;
		backColor = back;
		foreColor = fore;
	}

	public Object Entity() {
		return entity;
	}

	public int getType() {
		return type;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	public Font getFont(){
		return font;
	}
	public Color getForeColor(){
		return foreColor;
	}
	public Color getBackColor(){
		return backColor;
	}
	public String toString(){
		return "Object type " + entity.getClass() +"-->"+ font.getName();
	}
}

