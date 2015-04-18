package it.unipr.ce.dsg.deus.editor;

import java.io.File;

import javax.swing.filechooser.FileFilter;


/**
 * A class to filter extension file (xml e bup)
 * 
 * @author Fabrizio Caramia (caramia@ce.unipr.it)
 * @author Mario Sabbatelli (smario@ce.unipr.it)
 */
public class XmlFilter extends FileFilter {

	private String fileType;

	public XmlFilter(String type) {
		this.fileType = type;
	}

	@Override
	public boolean accept(File f) {

		if (f.isDirectory()) {
			return true;
		}

		String extension = getExtension(f);
		if (extension != null) {
			if (fileType.equals("xml")) {
				if (extension.equals("xml")) {
					return true;
				} else {
					return false;
				}
			} else if (fileType.equals("bup")) {
				if (extension.equals("bup")) {
					return true;
				} else {
					return false;
				}
			} else {
				if (extension.equals("xml") || extension.equals("bup")) {
					return true;
				} else {
					return false;
				}
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

	@Override
	public String getDescription() {
		return fileType;
	}

}
