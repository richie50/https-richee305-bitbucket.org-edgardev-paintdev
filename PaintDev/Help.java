
/**
 * @author Edgar Zaganjori, Daniyal Javed, Richmond Frimpong
 * @course EECS3461 
 * @title Help
 */

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

public class Help extends JDialog {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates the help window in the toolbar
	 * 
	 * @param parent
	 * @param title
	 * @param message
	 */

	public Help(JFrame parent, String title, String message) {
		super(parent, title);
		System.out.println("creating the window..");
		// set the position of the window
		Point p = new Point(400, 400);
		setLocation(p.x, p.y);

		// Create a message
		JPanel messagePane = new JPanel();
		JLabel acc = new JLabel(new ImageIcon("icons/helpPAINT.png"));
		acc.setBounds(180, 30, 40, 40);
		messagePane.add(new JLabel(
				"<html> <font color=\"red\"> <center> EECS 3461 Paint App </center> <br> <br> <br> <br> The team developed this:"
						+ "<center>1. Edgar Zaganjori ------ cse23106 <br>2. Daniyal Javed Khan ---- cse31034 <br>3. Richmond Frimpong --- cse23004<br> </center>"
						+ "<font size=\"2\"> We hope you enjoy this! <br> Any suggestions please contact us through our github <br> <center><a href=\"https://github.com/ezaganjoridev\">@ezaganjoridev</a> <center></font> </font></html>"));
		// get content pane, which is usually the
		// Container of all the dialog's components.
		getContentPane().add(acc);
		getContentPane().add(messagePane);

		// Create a button
		JPanel buttonPane = new JPanel();
		JButton button = new JButton("Close");
		buttonPane.add(button);
		// set action listener on the button
		button.addActionListener(new MyActionListener());
		getContentPane().add(buttonPane, BorderLayout.PAGE_END);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
	}

	/**
	 * @return rootPane override the createRootPane inherited by the JDialog, to
	 *         create the rootPane. create functionality to close the window
	 *         when "Escape" button is pressed
	 */
	public JRootPane createRootPane() {
		JRootPane rootPane = new JRootPane();
		KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
		Action action = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				System.out.println("escaping..");
				setVisible(false);
				dispose();
			}
		};
		InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(stroke, "ESCAPE");
		rootPane.getActionMap().put("ESCAPE", action);
		return rootPane;
	}

	/**
	 * an action listener to be used when an action is performed (e.g. button is
	 * pressed)
	 * 
	 *
	 */
	class MyActionListener implements ActionListener {

		// close and dispose of the window.
		public void actionPerformed(ActionEvent e) {
			System.out.println("disposing the window..");
			setVisible(false);
			dispose();
		}
	}
}