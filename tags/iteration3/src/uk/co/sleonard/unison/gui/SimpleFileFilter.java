package uk.co.sleonard.unison.gui;

// SimpleFileFilter.java
// A straightforward extension-based example of a file filter. This should be
// replaced by a "first class" Swing class in a later release of Swing.
//
import java.io.File;

import javax.swing.filechooser.FileFilter;

public class SimpleFileFilter extends FileFilter {

	String[] extensions;

	String description;

	public SimpleFileFilter(String ext) {
		this(new String[] { ext }, null);
	}

	public SimpleFileFilter(String[] exts, String descr) {
		// Clone and lowercase the extensions
		extensions = new String[exts.length];
		for (int i = exts.length - 1; i >= 0; i--) {
			extensions[i] = exts[i].toLowerCase();
		}
		// Make sure we have a valid (if simplistic) description
		description = (descr == null ? exts[0] + " files" : descr);
	}

	@Override
	public boolean accept(File f) {
		// We always allow directories, regardless of their extension
		if (f.isDirectory()) {
			return true;
		}

		// Ok, itâ€™s a regular file, so check the extension
		String name = f.getName().toLowerCase();
		for (int i = extensions.length - 1; i >= 0; i--) {
			if (name.endsWith(extensions[i])) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDescription() {
		return description;
	}
}
