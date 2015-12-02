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

    public LineSegment(Line2D.Double coordinates, Color color, Stroke stroke) {
        this.coordinates = coordinates;
        this.color = color;
        this.stroke = stroke;
    }

    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(color);
        graphics2D.setStroke(stroke);
        graphics2D.draw(coordinates);
    }
}