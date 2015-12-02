
/**
 * @author Edgar Zaganjori, Daniyal Javed, Richmond Frimpong
 * @course EECS3461 
 * @title Printer
 */

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

public class Printer implements Printable, ImageObserver {
	private Graphics2D g2d;
	private Image image;

	/**
	 * 
	 * @param TempImage
	 */
	public Printer(Image TempImage) {
		this.image = TempImage;
	}

	/**
	 * Print painting
	 */
	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		/* if the page wants to print more than one page */
		g2d = (Graphics2D) graphics;
		if (pageIndex > 0) {
			return NO_SUCH_PAGE;
		}
		g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

		graphics.drawImage(image, 50, 50, 50, 50, 50, 50, 50, 50, null);
		return 0;
	}

	/**
	 * 
	 * @return image
	 */
	public Image getImage() {
		return this.image;
	}

	/**
	 * 
	 * @param g
	 */
	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, this);
	}

	/**
	 * Update the image
	 */
	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
		if ((infoflags & ALLBITS) != 0) {
			return false;
		}
		return true;
	}

}
