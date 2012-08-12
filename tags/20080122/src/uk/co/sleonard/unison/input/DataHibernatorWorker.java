/**
 * DataHibernatorWorker.java
 *
 * Created on 24 October 2007, 18:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package uk.co.sleonard.unison.input;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import uk.co.sleonard.unison.gui.UNISoNController;
import uk.co.sleonard.unison.gui.UNISoNException;
import uk.co.sleonard.unison.gui.UNISoNLogger;

/**
 * 
 * @author steve
 */
public class DataHibernatorWorker extends SwingWorker {

	private static Logger logger = Logger.getLogger("DataHibernatorWorker");

	private static int numberofHibernators = 10;

	private final NewsGroupReader reader;

	private boolean saveToDatabase = true;

	/** Creates a new instance of DataHibernatorWorker */
	public DataHibernatorWorker(final NewsGroupReader reader) {
		super("DataHibernator");
		this.reader = reader;
		DataHibernatorWorker.logger.debug("Creating " + this.getClass() + " "
				+ reader.getNumberOfMessages());
		this.start();
	}

	static UNISoNLogger log;

	public static void setLogger(UNISoNLogger logger) {
		DataHibernatorWorker.log = logger;
	}

	public synchronized static void startHibernators() {
		while (DataHibernatorWorker.workers.size() < numberofHibernators) {
			DataHibernatorWorker.workers.add(new DataHibernatorWorker(
					UNISoNController.getInstance().getNntpReader()));
		}
	}

	public static void stopDownload() {
		for (final ListIterator<DataHibernatorWorker> iter = workers
				.listIterator(); iter.hasNext();) {
			iter.next().interrupt();
		}
	}

	private static ArrayList<DataHibernatorWorker> workers = new ArrayList<DataHibernatorWorker>();

	@Override
	public Object construct() {
		final LinkedBlockingQueue<NewsArticle> queue = UNISoNController
				.getInstance().getQueue();
		DataHibernatorWorker.logger.debug("construct : " + this.saveToDatabase
				+ " queue " + queue.size());

		try {
			// HAve one session per worker rather than per message
			Session session = UNISoNController.getInstance().helper()
					.getHibernateSession();
			while (this.saveToDatabase) {

				while (!queue.isEmpty()) {
					if (Thread.interrupted()) {
						this.stopHibernatingData();
						throw new InterruptedException();
					}

					final NewsArticle article = pollForMessage(queue);
					if (null != article) {
						DataHibernatorWorker.logger.debug("Hibernating "
								+ article.getArticleId() + " " + queue.size());

						if (UNISoNController.getInstance().helper()
								.hibernateData(article, session)) {
							this.reader.incrementMessagesStored();
						} else {
							this.reader.incrementMessagesSkipped();
						}
						this.reader.showDownloadStatus();
					}
				}
				// wait a second
				Thread.sleep(5000);

				// completed save so close down
				if (queue.isEmpty()) {
					this.saveToDatabase = false;
				}
			}
			DataHibernatorWorker.workers.remove(this);
			if (DataHibernatorWorker.workers.size() == 0){
				log.alert("Download complete");
			}
		} catch (final InterruptedException e) {
			return ("Interrupted");
		} catch (UNISoNException e) {
			log.alert("Error: " + e.getMessage());
		}
		return ("Completed");
	}

	private synchronized NewsArticle pollForMessage(final LinkedBlockingQueue<NewsArticle> queue) {
		final NewsArticle article = queue.poll();
		return article;
	}

	public void stopHibernatingData() {
		DataHibernatorWorker.logger.warn("StopHibernatingData");
		this.saveToDatabase = false;
	}
}
