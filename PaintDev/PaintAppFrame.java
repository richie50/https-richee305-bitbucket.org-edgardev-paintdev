import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PaintAppFrame extends JFrame
    implements MouseListener, MouseMotionListener, ActionListener
{
    static final long serialVersionUID = 2L;

    final int MAX_SAMPLES = 500;
    private PaintPanel paintPanel;
    private JButton clearButton;
    private JButton thickBrush;
    private JButton thinBrush;
    private JButton changeColor;
    private JButton loadImage;

    private Point[] stroke;
    private int strokeCount;
    private int sampleCount;

    PaintAppFrame()
    {
        final int FIELD_WIDTH = 10;

        strokeCount = 0;
        sampleCount = 0;

        stroke = new Point[MAX_SAMPLES];
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        clearButton = new JButton("CLEAR");
        clearButton.setActionCommand("clear");
        clearButton.addActionListener(this);

        thickBrush = new JButton("BRUSH");
        thickBrush.setActionCommand("thick");
        thickBrush.addActionListener(this);

        thinBrush = new JButton("PENCIL");
        thinBrush.setActionCommand("thin");
        thinBrush.addActionListener(this);

        changeColor = new JButton("CHANGE COLOR");
        changeColor.setActionCommand("color");
        changeColor.addActionListener(this);

        loadImage = new JButton("LOAD IMAGE");
        loadImage.setActionCommand("image");
        loadImage.addActionListener(this);

        paintPanel = new PaintPanel();
        paintPanel.addMouseMotionListener(this);
        paintPanel.addMouseListener(this);

        JPanel buttons = new JPanel(new BorderLayout());
        buttons.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        buttons.add(Box.createRigidArea(new Dimension(0, 3)));
        buttons.add(clearButton);
        buttons.add(Box.createRigidArea(new Dimension(0, 3)));
        buttons.add(thickBrush);
        buttons.add(Box.createRigidArea(new Dimension(0, 3)));
        buttons.add(thinBrush);
        buttons.add(Box.createRigidArea(new Dimension(0, 3)));
        buttons.add(changeColor);
        buttons.add(Box.createRigidArea(new Dimension(0, 3)));
        buttons.add(loadImage);

        JPanel p1 = new JPanel(new BorderLayout());
        p1.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        p1.add(paintPanel, "Center");
        p1.setPreferredSize(new Dimension(500, 500));
        p1.setMaximumSize(new Dimension(500, 500));

        JPanel content = new JPanel();
        content.add(buttons);
        content.add(p1);
        this.setContentPane(content);
    }

    public void mouseDragged(MouseEvent me)
    {
        int x = me.getX();
        int y = me.getY();

        if (SwingUtilities.isLeftMouseButton(me))
        {
            stroke[sampleCount] = new Point(x, y);
            int x1 = (int)stroke[sampleCount - 1].getX();
            int y1 = (int)stroke[sampleCount - 1].getY();
            int x2 = (int)stroke[sampleCount].getX();
            int y2 = (int)stroke[sampleCount].getY();
            if (sampleCount < MAX_SAMPLES - 1)
                ++sampleCount;

            // draw ink trail from previous point to current point
            paintPanel.drawInk(x1, y1, x2, y2);
        }
    }

    public void mouseMoved(MouseEvent me)
    {
    }

    public void mouseClicked(MouseEvent me) {}
    public void mouseEntered(MouseEvent me) {}
    public void mouseExited(MouseEvent me) {}

    public void mousePressed(MouseEvent me)
    {
        int x = me.getX();
        int y = me.getY();

        stroke[sampleCount] = new Point(x, y);
        if (sampleCount < MAX_SAMPLES - 1) {
            ++sampleCount;
        }
    }

    public void mouseReleased(MouseEvent me)
    {
        if (SwingUtilities.isLeftMouseButton(me))
        {
            ++strokeCount;
            sampleCount = 0;
        }
    }

    public void actionPerformed(ActionEvent ae)
    {
        String command = ae.getActionCommand();

        switch (command) {
            case "clear":
                paintPanel.clear();
                break;
            case "thick":
                paintPanel.setThickBrush();
                break;
            case "thin":
                paintPanel.setThinBrush();
                break;
            case "color":
                this.setPaintColor();
                break;
            case "image":
                this.loadImage();
                break;

        }


    }

    private void setPaintColor()
    {
        Color color = JColorChooser.showDialog(
                null,
                "Choose Paint Color",
                Color.black
                );
        paintPanel.setColor(color);
    }

    private void loadImage()
    {
        JFileChooser imageSelector = new JFileChooser(
                System.getProperty("user.dir")
                );

        imageSelector.setDialogTitle("Select Image");
        imageSelector.setMultiSelectionEnabled(false);
        imageSelector.addChoosableFileFilter(
                new FileNameExtensionFilter("Images Only", "jpg", "png", "gif")
                );
        imageSelector.setAcceptAllFileFilterUsed(false);
        if (imageSelector.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File imageFile = imageSelector.getSelectedFile();
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Image image = toolkit.getImage(imageFile.getAbsolutePath());
            paintPanel.addImage(image);
        }
    }
}
