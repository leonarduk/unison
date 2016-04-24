/**
 * DataHibernatorWorker.java
 *
 * Created on 24 October 2007, 18:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package uk.co.sleonard.unison.input;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import uk.co.sleonard.unison.gui.UNISoNController;

/**
 * 
 * @author steve
 */
public class DataHibernatorWorker extends SwingWorker {

	private static Logger logger = Logger.getLogger("DataHibernatorWorker");

	private final NewsGroupReader reader;

	private boolean saveToDatabase = true;

	/** Creates a new instance of DataHibernatorWorker */
	public DataHibernatorWorker(final NewsGroupReader reader) {
		super();
		this.reader = reader;
		DataHibernatorWorker.logger.debug("Creating " + this.getClass() + " "
				+ reader.getNumberOfMessages());
		this.start();
	}

	@Override
	public Object construct() {
		final LinkedBlockingQueue<NewsArticle> queue = this.reader.getQueue();
		DataHibernatorWorker.logger.debug("construct : " + this.saveToDatabase
				+ " queue " + queue.size());

		try {

			while (this.saveToDatabase) {

				while (this.saveToDatabase && !queue.isEmpty()) {
					if (Thread.interrupted()) {
						this.stopHibernatingData();
						throw new InterruptedException();
					}

					final NewsArticle article = queue.poll();
					if (null != article) {
						DataHibernatorWorker.logger.debug("Hibernating "
								+ article.getArticleId() + " " + queue.size());
						if (UNISoNController.getInstance().helper().hibernateData(article)) {
							this.reader.incrementMessagesStored();
						} else {
							this.reader.incrementMessagesSkipped();
						}
						this.reader.showDownloadStatus();
					}
				}
				// wait a second
				Thread.sleep(1000);
			}
		} catch (final InterruptedException e) {
			return ("Interrupted");
		}
		return ("Completed");
	}

	public void stopHibernatingData() {
		DataHibernatorWorker.logger.warn("StopHibernatingData");
		this.saveToDatabase = false;
	}
}
