import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.JSlider;

public class AppConfig {
    
    public static enum Mode {
        SELECT, ROTATE,
        LINE, RECTANGLE, ROUNDRECT, OVAL,
        FREEHAND, POLYGON, IMAGE, TEXT
    }

    // Settings

    public static Mode mode = Mode.SELECT;
    public static Color fill = null;
    public static Color line = Color.BLACK;
    public static int stroke = 1; 
    public static boolean isOpaque = true;
    public static boolean isGrid = false;
    public static boolean isRuler = false;
    public static double zoom = 1.0;
    public static Dimension size = new Dimension(600, 600);

    public static File imagePath = null;
    public static AbstractButton fillButton = null;
    public static AbstractButton lineButton = null;
    public static File file = null;
    public static String ext = null;
    public static JSlider zoomSlider = null;
    public static AbstractButton zoomIn = null;
    public static AbstractButton zoomOut = null;

} // AppConfig
