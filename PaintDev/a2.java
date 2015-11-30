import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
/* A more preffered main method for gui. NEVER RUN A GUI COMPONENTS WITHOUT EVENTQUEUE*/
public class a2 {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PaintAppFrame paintFrame = new PaintAppFrame();
					paintFrame.setTitle("Paint");
					Dimension screenSize = Toolkit.getDefaultToolkit()
							.getScreenSize();
					paintFrame.setBounds(250, 75, 750, 600);
					paintFrame.setMinimumSize(new Dimension(500, 600));
					paintFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
}
