
/**
 * @author Edgar Zaganjori, Daniyal Javed, Richmond Frimpong
 * @course EECS3461 
 * @title a2
 */

import java.awt.*;
import javax.swing.*;

/**
 * 
 * Main Method
 *
 */

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
					Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
					paintFrame.setSize(screenSize);
					paintFrame.setMinimumSize(new Dimension(600, 600));
					paintFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
}
