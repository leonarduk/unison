/**
 * NewsGroupReader
 *
 * @author Stephen <github@leonarduk.com>
 * @since 22-May-2016
 */
package uk.co.sleonard.unison.input;

import lombok.extern.slf4j.Slf4j;
import uk.co.sleonard.unison.UNISoNController;

/**
 * The Class NewsGroupReader.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 */
@Slf4j
public class NewsGroupReader {

    /**
     * The client.
     */
    private final NewsClient client;

    private final UNISoNController controller;

    /**
     * The message count.
     */
    private int messageCount;

    /**
     * The msgs skipped.
     */
    private int msgsSkipped = 0;

    /**
     * The msgs stored.
     */
    private int msgsStored = 0;

    /**
     * Instantiates a new news group reader.
     *
     * @param client the {@link NewsClient} to use
     */
    public NewsGroupReader(final NewsClient client, final UNISoNController controller) {
        this.client = client;
        this.controller = controller;
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


    // public void startDownload(String newsgroup, Date toDate, Date fromDate)
    // throws UNISoNException {
    // Set<NNTPNewsGroup> listNewsgroups = UNISoNController.getInstance()
    // .listNewsgroups(newsgroup);
    // UNISoNController.getInstance().quickDownload(listNewsgroups, fromDate,
    // toDate, this);
    // }

    /**
     * Sets the downloader finished.
     *
     * @param worker the new downloader finished
     */
    public void setDownloaderFinished(final FullDownloadWorker worker) {
        // throw new RuntimeException("not implemented");
    }

    /**
     * Sets the message count.
     *
     * @param messageCount the new message count
     */
    public void setMessageCount(final int messageCount) {
        this.messageCount = messageCount;
    }

    /**
     * Show download status.
     */
    synchronized void showDownloadStatus() {
        final int i = this.getMessagesSkipped() + this.getMessagesStored();

        if (this.getMessageCount() > 0 && this.controller != null) {
            final int progress = (i * 100) / this.getMessageCount();
            this.controller.setDownloadingState(progress);
        }

    }

}
