
/**
 * @author Edgar Zaganjori, Daniyal Javed, Richmond Frimpong
 * @course EECS3461 
 * @title MyFileFilter
 */

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class MyFileFilter extends FileFilter {
	/**
	 * to ignore java warnings
	 */
	private static final long serialVersionUID = 1L;
	private String array[];

	/**
	 * 
	 * @param extensions
	 */
	public MyFileFilter(String[] extensions) {
		// TODO Auto-generated constructor stub
		array = extensions;
	}

	/**
	 * @param f
	 */
	@Override
	public boolean accept(File f) {
		// TODO Auto-generated method stub
		if (f.isDirectory()) {
			return true;
		}
		// show the extensions
		for (int j = 0; j < array.length; j++) {
			if (f.getName().toLowerCase().indexOf(array[j].toLowerCase()) > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return tmp
	 */
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		String tmp = "";
		for (int i = 0; i < array.length; ++i) {
			tmp += "*" + array[i] + " ";
			System.out.println(tmp);
		}
		return tmp;
	}

}
