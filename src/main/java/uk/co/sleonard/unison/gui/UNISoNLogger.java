package uk.co.sleonard.unison.gui;

/**
 * The Interface UNISoNLogger.
 * 
 * @author Stephen <github@leonarduk.com>
 * @since
 *
 */
public interface UNISoNLogger {

	/**
	 * Alert.
	 *
	 * @param message
	 *            the message
	 */
	public void alert(String message);

	/**
	 * Log.
	 *
	 * @param message
	 *            the message
	 */
	public void log(String message);
}
