/**
 * UNISoNGUI
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.gui;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class UNISoNGUI {
	/** The frame. */
	private final JFrame frame;

	public UNISoNGUI(final JFrame frame2) {
		this.frame = frame2;
	}

	/**
	 * Ask question.
	 *
	 * @param question
	 *            the question
	 * @param options
	 *            the options
	 * @param title
	 *            the title
	 * @param defaultOption
	 *            the default option
	 * @return the int
	 */
	public int askQuestion(final String question, final String[] options, final String title,
	        final String defaultOption) {
		final int response = JOptionPane.showOptionDialog(this.frame, question, title,
		        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options,
		        defaultOption);
		return response;
	}

	/**
	 *
	 * @return
	 */
	public DefaultListModel<?> getCountriesFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Show alert.
	 *
	 * @param message
	 *            the message
	 */
	public void showAlert(final String messageText) {
		JOptionPane.showMessageDialog(this.frame, messageText);
	}

	/**
	 * Show status.
	 *
	 * @param message1
	 *            the message
	 */
	public void showStatus(final String message1) {
		// CLI version does not do this
		if (null != this.frame) {
			this.showAlert(message1);
		}
	}

}
