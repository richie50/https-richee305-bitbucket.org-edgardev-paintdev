import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.border.*;

public class PaintPanel extends JPanel
{
    static final long serialVersionUID = 1L;

    private Stroke THIN_LINE_STROKE = new BasicStroke(2.0f,
                BasicStroke.CAP_ROUND, BasicStroke. JOIN_ROUND);

    private Stroke THICK_LINE_STROKE = new BasicStroke(5.0f,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    private Color LINE_COLOR = new Color(0, 0, 0);
    private Stroke LINE_STROKE = this.THIN_LINE_STROKE;

    private Vector<Line2D.Double> allStrokes;

    public PaintPanel()
    {
        allStrokes = new Vector<Line2D.Double>();
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        paintInkStrokes(g);
    }

    public Color getColor() {
        return this.LINE_COLOR;
    }

    public void setColor(Color color) {
        this.LINE_COLOR = color;
    }

    public void setThickBrush() {
        this.LINE_STROKE = this.THICK_LINE_STROKE;
    }

    public void setThinBrush() {
        this.LINE_STROKE = this.THIN_LINE_STROKE;
    }

    /*
     * Paint all the line segments stored in the vector
    */
    private void paintInkStrokes(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;

        // set the inking color
        g2.setColor(LINE_COLOR);

        // set the stroke thickness, and cap and join attributes ('round')
        Stroke s = g2.getStroke(); // save current stroke
        g2.setStroke(LINE_STROKE);  // set desired stroke

        // retrive each line segment and draw it
        for (int i = 0; i < allStrokes.size(); ++i) {
            g2.draw(allStrokes.elementAt(i));
        }

        g2.setStroke(s); // restore stroke
    }

    public void drawInk(int x1, int y1, int x2, int y2)
    {
        // get graphics context
        Graphics2D g2 = (Graphics2D)this.getGraphics();

        // create the line
        Line2D.Double inkSegment = new Line2D.Double(x1, y1, x2, y2);

        g2.setColor(LINE_COLOR);    // set the inking color
        Stroke s = g2.getStroke(); // save current stroke
        g2.setStroke(LINE_STROKE);  // set desired stroke 
        g2.draw(inkSegment);       // draw it!   
        g2.setStroke(s);           // restore stroke 
        allStrokes.add(inkSegment);         // add to vector
    }

    public void addImage(Image image)
    {
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(image, 0);

        try {
            mt.waitForAll();
        } catch (InterruptedException e) {
            System.out.printf("Failed to print image: %s\n", image.toString());
        }
        Graphics2D g2 = (Graphics2D)this.getGraphics();
        g2.drawImage(image, 0, 0, this);
    }

    public void clear()
    {
        allStrokes.clear();
        this.repaint();
    }
}
