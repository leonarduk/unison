package uk.co.sleonard.unison.gui;

// SimpleFileFilter.java
// A straightforward extension-based example of a file filter. This should be
// replaced by a "first class" Swing class in a later release of Swing.
//
import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * The Class SimpleFileFilter.
 * 
 * @author
 * @since
 *
 */
public class SimpleFileFilter extends FileFilter {

	/** The description. */
	String		description;

	/** The extensions. */
	String[]	extensions;

	/**
	 * Instantiates a new simple file filter.
	 *
	 * @param ext
	 *            the ext
	 */
	public SimpleFileFilter(final String ext) {
		this(new String[] { ext }, null);
	}

	/**
	 * Instantiates a new simple file filter.
	 *
	 * @param exts
	 *            the exts
	 * @param descr
	 *            the descr
	 */
	public SimpleFileFilter(final String[] exts, final String descr) {
		// Clone and lowercase the extensions
		this.extensions = new String[exts.length];
		for (int i = exts.length - 1; i >= 0; i--) {
			this.extensions[i] = exts[i].toLowerCase();
		}
		// Make sure we have a valid (if simplistic) description
		this.description = descr == null ? exts[0] + " files" : descr;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	@Override
	public boolean accept(final File f) {
		// We always allow directories, regardless of their extension
		if (f.isDirectory()) {
			return true;
		}

		// Ok, itâ€™s a regular file, so check the extension
		final String name = f.getName().toLowerCase();
		for (int i = this.extensions.length - 1; i >= 0; i--) {
			if (name.endsWith(this.extensions[i])) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	@Override
	public String getDescription() {
		return this.description;
	}
}
