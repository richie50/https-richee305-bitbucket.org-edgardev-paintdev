

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

public class a2 {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		PaintAppFrame paintFrame = new PaintAppFrame();
		paintFrame.setTitle("Paint");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		System.out.println(screenSize.getHeight() + " " + screenSize.getWidth());
		paintFrame.setSize(screenSize);
		paintFrame.setVisible(true);
	}
}
