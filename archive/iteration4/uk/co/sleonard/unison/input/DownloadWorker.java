package uk.co.sleonard.unison.input;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessageRemovedException;
import javax.mail.MessagingException;

import org.apache.log4j.Logger;

/**
 * Class to create a separate Thread for downloading messages
 * 
 * @author steve
 * 
 */
public class DownloadWorker extends SwingWorker {

	private static final int MAX_DEPTH = 0;

	private NewsGroupReader reader = null;

	private int increment;

	private int messageCount;

	private Folder folder;

	private int startIndex;

	public DownloadWorker(NewsGroupReader reader,

	int increment, int start, Folder folder, int messageCount) {
		super();
		this.startIndex = start;
		this.increment = increment;
		this.reader = reader;
		this.folder = folder;
		this.messageCount = messageCount;

		System.out.println("Creating " + this.getClass() + " "
				+ reader.getNumberOfMessages());
		start();
	}

	private static Logger logger = Logger.getLogger("DownloadWorker");

	@Override
	public Object construct() {

		System.out.println("construct");

		try {
			for (int i = startIndex; i < messageCount; i += increment) {
				if (reader.getQueue().size() > MAX_DEPTH) {
					// wait a second
					Thread.sleep(1000);
				} else {
					if (Thread.interrupted()) {
						throw new InterruptedException();
					}
					boolean ok = false;
					try {
						ok = storeNextMessage(folder, i, ok);
					} catch (javax.mail.MessageRemovedException e) {
						logger.info("Message has been removed - skipping");
					} catch (javax.mail.MessagingException e) {
						// try to reconnect and try one more time to get message
						reader.reconnect();
						ok = storeNextMessage(folder, i, ok);
					}
					if (ok) {
						// reader.incrementMessagesStored();
					} else {
						reader.incrementMessagesSkipped();
					}
					reader.showDownloadStatus();
				}
			} // for
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			return ("Interrupted");
		}
		return ("Completed");
	}

	/**
	 * 
	 * @param folder
	 * @param i
	 * @param ok
	 * @return
	 * @throws MessagingException
	 */
	private boolean storeNextMessage(Folder folder, int i, boolean ok)
			throws MessagingException {
		logger.warn("Store message " + i);

		NewsGroupArticle article;
		Message message = folder.getMessage(i);

		// We want to filter out messages that have been deleted on the server
		if (!message.isExpunged() && null != message.getSubject()) {
			try {
				logger.warn("Save " + message.getMessageNumber() + " to queue");
				article = new NewsGroupArticle(message);
				// write to database
				if (null != article) {
					// && HibernateHelper.hibernateData(article)) {
					reader.getQueue().add(article);

					ok = true;
				}
			} catch (MessageRemovedException e) {

				logger.warn("Message was removed");
			}
		}
		return ok;
	}

	@Override
	public void finished() {
		reader.setDownloaderFinished(this);
	}
}
