/**
 * NewsGroupReader
 *
 * @author Stephen <github@leonarduk.com>
 * @since 22-May-2016
 */
package uk.co.sleonard.unison.input;

import org.apache.log4j.Logger;

import uk.co.sleonard.unison.UNISoNController;
import uk.co.sleonard.unison.UNISoNControllerFX;
import uk.co.sleonard.unison.UNISoNLogger;

/**
 * The Class NewsGroupReader.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
public class NewsGroupReader implements UNISoNLogger {
	/** The logger. */
	private static Logger logger = Logger.getLogger("NewsGroupReader");

	/** The client. */
	public NewsClient client;

	/** The message count. */
	private int messageCount;

	/** The msgs skipped. */
	int msgsSkipped = 0;

	/** The msgs stored. */
	int msgsStored = 0;

	/**
	 * Instantiates a new news group reader.
	 *
	 * @param controller
	 *            the controller
	 */
	public NewsGroupReader(final UNISoNController controller) {
		this.client = new NewsClientImpl();
	}

	/**
	 * Instantiates a new news group reader.
	 *
	 * @param controller
	 *            the controller
	 */
	public NewsGroupReader(final UNISoNControllerFX controller) {
		this.client = new NewsClientImpl();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see uk.co.sleonard.unison.gui.UNISoNLogger#alert(java.lang.String)
	 */
	@Override
	public void alert(final String message) {
		NewsGroupReader.logger.warn(message);
	}

	/**
	 * Gets the message count.
	 *
	 * @return the message count
	 */
	public int getMessageCount() {
		return this.messageCount;
	}

	/**
	 * Gets the messages skipped.
	 *
	 * @return the messages skipped
	 */
	public int getMessagesSkipped() {
		return this.msgsSkipped;
	}

	/**
	 * Gets the messages stored.
	 *
	 * @return the messages stored
	 */
	public int getMessagesStored() {
		return this.msgsStored;
	}

	/**
	 * Gets the number of messages.
	 *
	 * @return the number of messages
	 */
	public int getNumberOfMessages() {
		return this.client.getMessageCount();
	}

	/**
	 * Increment messages skipped.
	 */
	public synchronized void incrementMessagesSkipped() {
		this.msgsSkipped++;
	}

	/**
	 * Increment messages stored.
	 */
	public synchronized void incrementMessagesStored() {
		this.msgsStored++;
	}

	/**
	 * Checks if is connected.
	 *
	 * @return true, if is connected
	 */
	public boolean isConnected() {
		return this.client.isConnected();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see uk.co.sleonard.unison.gui.UNISoNLogger#log(java.lang.String)
	 */
	@Override
	public void log(final String message) {
		NewsGroupReader.logger.info(message);
	}

	/**
	 * Sets the downloader finished.
	 *
	 * @param worker
	 *            the new downloader finished
	 */
	public void setDownloaderFinished(final FullDownloadWorker worker) {
		// throw new RuntimeException("not implemented");
	}

	// public void startDownload(String newsgroup, Date toDate, Date fromDate)
	// throws UNISoNException {
	// Set<NNTPNewsGroup> listNewsgroups = UNISoNController.getInstance()
	// .listNewsgroups(newsgroup);
	// UNISoNController.getInstance().quickDownload(listNewsgroups, fromDate,
	// toDate, this);
	// }

	/**
	 * Sets the message count.
	 *
	 * @param messageCount
	 *            the new message count
	 */
	public void setMessageCount(final int messageCount) {
		this.messageCount = messageCount;
	}

	/**
	 * Show download status.
	 */
	public synchronized void showDownloadStatus() {
		final int i = this.getMessagesSkipped() + this.getMessagesStored();

		if (this.getMessageCount() > 0) {
			final int progress = (i * 100) / this.getMessageCount();
			UNISoNControllerFX.getInstance().setDownloadingState(progress);
		}

	}

}
