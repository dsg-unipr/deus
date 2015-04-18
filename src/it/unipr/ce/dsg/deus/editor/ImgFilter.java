package it.unipr.ce.dsg.deus.editor;

import java.io.File;
import javax.swing.filechooser.*;


/**
 * A class to filter extension file (jpg)
 * 
 * @author Fabrizio Caramia (caramia@ce.unipr.it)
 * @author Mario Sabbatelli (smario@ce.unipr.it)
 */
public class ImgFilter extends FileFilter {

	public final String jpeg = "jpeg";
	public final String jpg = "jpg";

	// Accept all directories and jpg,
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String extension = getExtension(f);
		if (extension != null) {
			if (extension.equals(jpeg) || extension.equals(jpg)) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}

	public String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	// The description of this filter
	public String getDescription() {
		return "Jpeg";
	}
}
