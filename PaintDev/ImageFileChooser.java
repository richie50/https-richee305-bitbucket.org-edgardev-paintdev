

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageFileChooser extends JFileChooser {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String ext[] = { "gif", "jpg", "bmp", "png" };
	private File file;
	private String path;

	public ImageFileChooser(String temp) {
		super(temp);
		setAcceptAllFileFilterUsed(false);
		configExtensions();
	}

	private void configExtensions() {
		this.addChoosableFileFilter(
				new FileNameExtensionFilter("JPEG (*.jpg;*.jpeg;*.jpe;*.jfif)", "jpg", "jpeg", "jpe", "jfif"));
		this.addChoosableFileFilter(new FileNameExtensionFilter("PNG (*.png)", "png"));
		this.addChoosableFileFilter(new FileNameExtensionFilter("Bitmap (*.bmp;*.dib)", "bmp", "dib"));
		this.addChoosableFileFilter(new FileNameExtensionFilter("GIF (*.gif)", "gif"));
	}

	/**
	 * Works around a JFileChooser limitation, that the selected file when
	 * saving is returned exactly as typed and doesn't take into account the
	 * selected file filter.
	 */
	public File getSelectedFileWithExtension() {
		File file = super.getSelectedFile();
		if (getFileFilter() instanceof FileNameExtensionFilter) {
			String[] exts = ((FileNameExtensionFilter) getFileFilter()).getExtensions();
			String nameLower = file.getName().toLowerCase();
			for (String ext : exts) { // check if it already has a valid
										// extension
				if (nameLower.endsWith('.' + ext.toLowerCase())) {
					return file; // if yes, return as-is
				}
			}
			// if not, append the first one from the selected filter
			file = new File(file.toString() + '.' + exts[0]);
		}
		return file;
	}

	/*
	 * An extended implementation of file I/O operation to check for existing
	 * files
	 */
	public static Set<?> getAllFiles(File file) throws FileNotFoundException {
		File[] filesList = file.listFiles(); // find a way to list all directory
												// files to avoid straining
												// resources
		// System.out.println(filesList.length);
		Set<String> set = new HashSet<String>();
		for (File f : filesList) {
			// if (f.isDirectory())
			// getAllFiles(f);
			if (f.isFile()) {
				for (int i = 0; i < ext.length; i++) {
					if (f.isFile() && f.getName().endsWith(ext[i])) {
						set.add(f.getName());
					}
				}
			}
		}
		return set;

	}
}