/**
 * NewsGroupReader
 *
 * @author Stephen <github@leonarduk.com>
 * @since 22-May-2016
 */
package uk.co.sleonard.unison.input;

import lombok.extern.slf4j.Slf4j;
import uk.co.sleonard.unison.UNISoNController;
import uk.co.sleonard.unison.UNISoNLogger;

/**
 * The Class NewsGroupReader.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
@Slf4j
public class NewsGroupReader implements UNISoNLogger {

	/** The client. */
	NewsClient client;

	/** The message count. */
	private int messageCount;

	/** The msgs skipped. */
	private int msgsSkipped = 0;

	/** The msgs stored. */
	private int msgsStored = 0;

	/**
	 * Instantiates a new news group reader.
	 *
	 * @param controller
	 *            the controller
	 */
	public NewsGroupReader(final UNISoNController controller) {
		this.client = new NewsClientImpl();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see uk.co.sleonard.unison.gui.UNISoNLogger#alert(java.lang.String)
	 */
	@Override
	public void alert(final String message) {
                log.warn(message);
	}

	public NewsClient getClient() {
		return this.client;
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
	synchronized void incrementMessagesSkipped() {
		this.msgsSkipped++;
	}

	/**
	 * Increment messages stored.
	 */
	synchronized void incrementMessagesStored() {
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
            log.info(message);
	}

	// public void startDownload(String newsgroup, Date toDate, Date fromDate)
	// throws UNISoNException {
	// Set<NNTPNewsGroup> listNewsgroups = UNISoNController.getInstance()
	// .listNewsgroups(newsgroup);
	// UNISoNController.getInstance().quickDownload(listNewsgroups, fromDate,
	// toDate, this);
	// }

	public void setClient(final NewsClient client) {
		this.client = client;
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
	synchronized void showDownloadStatus() {
		final int i = this.getMessagesSkipped() + this.getMessagesStored();

		if (this.getMessageCount() > 0) {
			final int progress = (i * 100) / this.getMessageCount();
			UNISoNController.getInstance().setDownloadingState(progress);
		}

	}

}
