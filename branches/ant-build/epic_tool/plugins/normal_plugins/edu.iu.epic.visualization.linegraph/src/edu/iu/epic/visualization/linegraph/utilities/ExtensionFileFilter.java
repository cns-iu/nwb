package edu.iu.epic.visualization.linegraph.utilities;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/*
 * From
 *  http://www.java2s.com/Code/JavaAPI/javax.swing/JFileChoosersetFileFilterFileFilterfilter.htm
 * TODO: Make this a utility class?
 */
public class ExtensionFileFilter extends FileFilter {
	private String description;
	private String[] extensions;

	public ExtensionFileFilter(String description, String extension) {
		this(description, new String[] { extension });
	}

	public ExtensionFileFilter(String description, String[] extensions) {
		if (description == null) {
			this.description = extensions[0];
		} else {
			this.description = description;
		}

		this.extensions = (String[]) extensions.clone();
		toLower(this.extensions);
	}

	public String getDescription() {
		return description;
	}

	public boolean accept(File file) {
		if (file.isDirectory()) {
			return true;
		} else {
			String path = file.getAbsolutePath().toLowerCase();

			for (int i = 0, n = extensions.length; i < n; i++) {
				String extension = extensions[i];

				return fileExtensionIsAcceptable(path, extension);
			}
		}

		return false;
	}
	
	private void toLower(String[] array) {
		for (int i = 0, n = array.length; i < n; i++) {
			array[i] = array[i].toLowerCase();
		}
	}

	private boolean fileExtensionIsAcceptable(String path, String extension) {
		int targetDotStringPosition = path.length() - extension.length() - 1;

		return
			path.endsWith(extension) 
			&& (path.charAt(targetDotStringPosition) == '.');
	}
}
