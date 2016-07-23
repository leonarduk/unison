/**
 * DataHibernatorWorker.java
 *
 * Created on 24 October 2007, 18:10
 *
 * To change this template, choose Tools | Template Manager and open the template in the editor.
 */

package uk.co.sleonard.unison.input;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import uk.co.sleonard.unison.UNISoNControllerFX;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.UNISoNLogger;
import uk.co.sleonard.unison.datahandling.HibernateHelper;

/**
 * The Class DataHibernatorWorker.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
public class DataHibernatorWorker extends SwingWorker {

	/** The logger. */
	private static Logger logger = Logger.getLogger("DataHibernatorWorker");

	/** The number of hibernators. */
	private static int numberofHibernators = 20;

	/** The log. */
	static UNISoNLogger log;

	/** The workers. */
	private static ArrayList<DataHibernatorWorker> workers = new ArrayList<>();

	/** The reader. */
	private final NewsGroupReader reader;

	/** The save to database. */
	private boolean saveToDatabase = true;

	private final HibernateHelper helper;

	/**
	 * Sets the logger.
	 *
	 * @param logger
	 *            the new logger
	 */
	public static void setLogger(final UNISoNLogger logger) {
		DataHibernatorWorker.log = logger;
	}

	/**
	 * Start hibernators.
	 */
	public synchronized static void startHibernators() {
		while (DataHibernatorWorker.workers.size() < DataHibernatorWorker.numberofHibernators) {
			DataHibernatorWorker.workers.add(
			        new DataHibernatorWorker(UNISoNControllerFX.getInstance().getNntpReader()));
		}
	}

	/**
	 * Stop download.
	 */
	public static void stopDownload() {
		for (final ListIterator<DataHibernatorWorker> iter = DataHibernatorWorker.workers
		        .listIterator(); iter.hasNext();) {
			iter.next().interrupt();
		}
	}

	public DataHibernatorWorker(final HibernateHelper helper) {
		super("DataHibernator");
		this.reader = null;
		this.helper = helper;
	}

	/**
	 * Creates a new instance of DataHibernatorWorker.
	 *
	 * @param reader
	 *            the reader
	 */
	public DataHibernatorWorker(final NewsGroupReader reader) {
		super("DataHibernatorWorker");
		this.helper = UNISoNControllerFX.getInstance().getHelper();

		this.reader = reader;
		DataHibernatorWorker.logger
		        .debug("Creating " + this.getClass() + " " + reader.getNumberOfMessages());
		this.start();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see uk.co.sleonard.unison.input.SwingWorker#construct()
	 */
	@Override
	public Object construct() {
		final LinkedBlockingQueue<NewsArticle> queue = UNISoNControllerFX.getInstance().getQueue();
		DataHibernatorWorker.logger
		        .debug("construct : " + this.saveToDatabase + " queue " + queue.size());

		try {
			// HAve one session per worker rather than per message
			final Session session = UNISoNControllerFX.getInstance().getHelper()
			        .getHibernateSession();
			while (this.saveToDatabase) {
				this.pollQueue(queue, session);
				// wait a second
				Thread.sleep(5000);
				// completed save so close down
				if (queue.isEmpty()) {
					this.saveToDatabase = false;
				}
			}
			DataHibernatorWorker.workers.remove(this);
			if (DataHibernatorWorker.workers.size() == 0) {
				DataHibernatorWorker.log.alert("Download complete");
			}
		}
		catch (final InterruptedException e) {
			return "Interrupted";
		}
		catch (final UNISoNException e) {
			DataHibernatorWorker.log.alert("Error: " + e.getMessage());
		}
		return "Completed";
	}

	/**
	 * Poll for message.
	 *
	 * @param queue
	 *            the queue
	 * @return the news article
	 */
	private synchronized NewsArticle pollForMessage(final LinkedBlockingQueue<NewsArticle> queue) {
		final NewsArticle article = queue.poll();
		return article;
	}

	public void pollQueue(final LinkedBlockingQueue<NewsArticle> queue, final Session session)
	        throws InterruptedException {
		while (!queue.isEmpty()) {
			if (Thread.interrupted()) {
				this.stopHibernatingData();
				throw new InterruptedException();
			}

			final NewsArticle article = this.pollForMessage(queue);
			if (null != article) {
				DataHibernatorWorker.logger
				        .debug("Hibernating " + article.getArticleId() + " " + queue.size());

				if (this.helper.hibernateData(article, session)) {
					this.reader.incrementMessagesStored();
				}
				else {
					this.reader.incrementMessagesSkipped();
				}
				this.reader.showDownloadStatus();
			}
		}
	}

	/**
	 * Stop hibernating data.
	 */
	public void stopHibernatingData() {
		DataHibernatorWorker.logger.warn("StopHibernatingData");
		this.saveToDatabase = false;
	}
}
