
/**
 * @author Edgar Zaganjori, Daniyal Javed, Richmond Frimpong
 * @course EECS3461 
 * @title a2
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 * 
 * Main Method
 *
 */

public class a2 {
	private UIManager.LookAndFeelInfo[] laf;

	public static void main(String[] args) {

		try {
			String theme = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
			UIManager.setLookAndFeel(theme);

		} catch (Exception e) {
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					// UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
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
