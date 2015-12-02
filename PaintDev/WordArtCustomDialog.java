
/**
 * @author Edgar Zaganjori, Daniyal Javed, Richmond Frimpong
 * @course EECS3461 
 * @title WordArtCustomDialog
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class WordArtCustomDialog extends JDialog implements ActionListener, ItemListener {
	static final long serialVersionUID = 42L;

	public static final int APPLY_OPTION = 0;
	public static final int CANCEL_OPTION = 1;
	int userResponse;
	final String[] SZ = { "10", "14", "18", "22", "26", "32", "38", "48" };

	JCheckBox italic;
	JCheckBox bold;
	JComboBox<String> sizeCombo;
	JComboBox<String> fontCombo;
	JTextField example;
	JButton ok;
	JButton cancel;
	JButton foreground;

	/**
	 * 
	 * @param owner
	 */
	WordArtCustomDialog(Frame owner) {
		super(owner, "Add Text", true);
		this.setResizable(false);
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontList = ge.getAvailableFontFamilyNames();
		fontCombo = new JComboBox<String>(fontList);

		italic = new JCheckBox("Italic");
		bold = new JCheckBox("Bold");

		sizeCombo = new JComboBox<String>(SZ);
		((JLabel) sizeCombo.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		sizeCombo.setSelectedIndex(4);
		sizeCombo.setPreferredSize(new Dimension(45, 21)); // tweek size

		example = new JTextField(" Example ");
		example.setHorizontalAlignment(SwingConstants.CENTER);
		example.setFont(new Font("sanserif", Font.PLAIN, 28));
		example.setEditable(false);

		ok = new JButton("Apply");
		cancel = new JButton("Cancel");
		ok.setPreferredSize(cancel.getPreferredSize());

		foreground = new JButton("Color");

		fontCombo.addActionListener(this);
		italic.addItemListener(this);
		bold.addItemListener(this);
		sizeCombo.addActionListener(this);
		ok.addActionListener(this);
		cancel.addActionListener(this);
		foreground.addActionListener(this);
		// custom dialog set up
		JPanel p0 = new JPanel();
		p0.add(fontCombo);
		p0.setBorder(new TitledBorder(new EtchedBorder(), "Font family"));

		JPanel p1a = new JPanel();
		p1a.add(italic);
		p1a.add(bold);
		p1a.setBorder(new TitledBorder(new EtchedBorder(), "Font style"));

		JPanel p1b = new JPanel();
		p1b.add(sizeCombo);
		p1b.add(new JLabel("pt."));
		p1b.setBorder(new TitledBorder(new EtchedBorder(), "Font size"));

		JPanel p1 = new JPanel();
		p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
		p1.add(p1a);
		p1.add(p1b);
		p1.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel p2 = new JPanel(); // use FlowLayout
		p2.add(foreground);
		p2.setBorder(new TitledBorder(new EtchedBorder(), "Message color"));
		p2.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel p3 = new JPanel();
		p3.setLayout(new BoxLayout(p3, BoxLayout.Y_AXIS));
		p3.add(example);
		p3.setPreferredSize(new Dimension(250, 60));
		p3.setMaximumSize(new Dimension(250, 60));
		p3.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel p4 = new JPanel();
		p4.add(ok);
		p4.add(cancel);
		p4.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(p0);
		p.add(Box.createRigidArea(new Dimension(0, 10)));
		p.add(p1);
		p.add(Box.createRigidArea(new Dimension(0, 10)));
		p.add(p2);
		p.add(Box.createRigidArea(new Dimension(0, 10)));
		p.add(p3);
		p.add(Box.createRigidArea(new Dimension(0, 10)));
		p.add(p4);
		p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// tweek sizes of panels to make the dialog look nice

		Dimension d1 = p3.getPreferredSize();
		Dimension d2 = p1.getPreferredSize();
		p1.setPreferredSize(new Dimension(d1.width, d2.height));
		p1.setMaximumSize(new Dimension(d1.width, d2.height));
		d2 = p2.getPreferredSize();
		p2.setPreferredSize(new Dimension(d1.width, d2.height));
		p2.setMaximumSize(new Dimension(d1.width, d2.height));

		this.setContentPane(p);
		this.pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();

		if (source == ok) {
			userResponse = APPLY_OPTION;
			this.setVisible(false);
		}

		else if (source == cancel) {
			userResponse = CANCEL_OPTION;
			this.setVisible(false);
		}

		else if (source == fontCombo) {
			@SuppressWarnings("unchecked")
			JComboBox<String> cb = (JComboBox<String>) source;
			String s = (String) cb.getSelectedItem();
			Font tmp = example.getFont();
			example.setFont(new Font(s, tmp.getStyle(), tmp.getSize()));
		}

		else if (source == sizeCombo) {
			@SuppressWarnings("unchecked")
			JComboBox<String> cb = (JComboBox<String>) source;
			String s = (String) cb.getSelectedItem();
			int newSize = Integer.parseInt(s);
			Font tmp = example.getFont();
			example.setFont(new Font(tmp.getFamily(), tmp.getStyle(), newSize));
		}

		else if (source == foreground) {
			Color tmp = JColorChooser.showDialog(this, "Choose text color", example.getForeground());
			if (tmp != null)
				example.setForeground(tmp);
		}

	}

	/**
	 * Change the state of the text
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		Font tmp = example.getFont();
		int style = tmp.getStyle();

		if (source == italic)
			if (italic.isSelected())
				style = style | Font.ITALIC; // turn italic on
			else
				style = style & ~Font.ITALIC; // turn italic off
		else if (source == bold)
			if (bold.isSelected())
				style = style | Font.BOLD; // turn bold on
			else
				style = style & ~Font.BOLD; // turn bold off

		example.setFont(new Font(tmp.getFamily(), style, tmp.getSize()));
	}

	/**
	 * @return font
	 */
	public Font getFont() {
		return example.getFont();
	}

	/**
	 * 
	 * @return Forecolor
	 */
	public Color getForeColor() {
		return example.getForeground();
	}

	/**
	 * 
	 * @return backGround
	 */
	public Color getBackColor() {
		return example.getBackground();
	}

	/**
	 * Create the dialogue
	 * 
	 * @param f
	 * @param fontArg
	 * @param foreColorArg
	 * @param backColorArg
	 * @return
	 */
	public int showCustomDialog(Frame f, Font fontArg, Color foreColorArg, Color backColorArg) {
		this.setLocationRelativeTo(f);

		// set the font combobox to the current font family name

		String s = fontArg.getName();
		fontCombo.setSelectedItem((Object) s);

		// set the style checkboxes to the current font style

		int style = fontArg.getStyle();
		if ((style & Font.ITALIC) == 0)
			italic.setSelected(false);
		else
			italic.setSelected(true);
		if ((style & Font.BOLD) == 0)
			bold.setSelected(false);
		else
			bold.setSelected(true);

		// set the size combobox to the current font size

		int size = fontArg.getSize();
		sizeCombo.setSelectedItem((Object) ("" + size));

		// set the example text field to the current message properties

		example.setFont(fontArg);
		example.setForeground(foreColorArg);
		example.setBackground(backColorArg);
		example.setEditable(true);

		// show the dialog

		this.setVisible(true);

		// When the user closes the dialog by clicking "Apply" or "Cancel",
		// return a pre-defined integer indicating which button was
		// pressed.

		return userResponse;
	}

	/**
	 * 
	 * @return text
	 */
	public String getExampleText() {
		return example.getText();
	}
}
