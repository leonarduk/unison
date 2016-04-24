/*
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

import uk.co.sleonard.unison.datahandling.HibernateHelper;

/**
 * 
 * @author steve
 */
public class DataHibernatorWorker extends SwingWorker {

	private NewsGroupReader reader;

	private static Logger logger = Logger.getLogger("DataHibernatorWorker");

	private boolean saveToDatabase = true;

	/** Creates a new instance of DataHibernatorWorker */
	public DataHibernatorWorker(NewsGroupReader reader) {
		super();
		this.reader = reader;
		logger.warn("Creating " + this.getClass() + " "
				+ reader.getNumberOfMessages());
		start();
	}

	public void stopHibernatingData() {
		logger.warn("StopHibernatingData");
		saveToDatabase = false;
	}

	public Object construct() {
		LinkedBlockingQueue<NewsGroupArticle> queue = reader.getQueue();
		logger.warn("construct : " + saveToDatabase + " queue " + queue.size());

		try {
			while (saveToDatabase) {

				while (saveToDatabase && !queue.isEmpty()) {
					if (Thread.interrupted()) {
						stopHibernatingData();
						throw new InterruptedException();
					}

					NewsGroupArticle article = queue.poll();
					if (null != article) {
						logger.warn("Hibernating " + article.getMessageID()
								+ " " + queue.size());
						if (HibernateHelper.hibernateData(article)) {
							reader.incrementMessagesStored();
						} else {
							reader.incrementMessagesSkipped();
						}
						reader.showDownloadStatus();
					}
				}
				// wait a second
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			return ("Interrupted");
		}
		return ("Completed");
	}
}
