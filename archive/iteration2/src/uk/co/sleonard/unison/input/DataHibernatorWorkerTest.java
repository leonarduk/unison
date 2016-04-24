package uk.co.sleonard.unison.input;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

public class DataHibernatorWorkerTest extends TestCase {

	LinkedBlockingQueue<NewsGroupArticle> queue;

	private NewsGroupReader reader;

	private static Logger logger = Logger.getLogger("DataHibernatorWorkerTest");

	protected void setUp() throws Exception {
		super.setUp();
		queue = new LinkedBlockingQueue<NewsGroupArticle>();
		for (int i = 1; i < 100; i++) {
			HashMap<String, Object> fields = new HashMap<String, Object>();
			fields.put(NewsGroupArticle.MESSAGE_ID, "TEST" + i);
			fields.put(NewsGroupArticle.SUBJECT, "TEST SUBJECT");
			fields.put(NewsGroupArticle.NNTP_POSTING_HOST, "127.0.0.1");
			fields.put(NewsGroupArticle.FROM, "test@unisonprog");

			fields.put(NewsGroupArticle.SENT, new Date());
			fields.put(NewsGroupArticle.NEWSGROUPS, "alt.test, alt.test" + i);

			NewsGroupArticle article = new NewsGroupArticle(fields);
			logger.warn("Put " + article.getMessageID() + " on queue"
					+ queue.size());
			queue.add(article);
		}
		reader = new NewsGroupReader(null) {
			@Override
			public LinkedBlockingQueue<NewsGroupArticle> getQueue() {
				return queue;
			}

			@Override
			public synchronized void incrementMessagesStored() {
				logger.warn("Saved message. Remaining: " + queue.size());
			}

			@Override
			public int getNumberOfMessages() {
				return queue.size();
			}

		};
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testname() throws Exception {
		logger.warn("testname");

		ArrayList<DataHibernatorWorker> workers = new ArrayList<DataHibernatorWorker>();

		int numberofHibernators = 1;
		for (int i = 0; i < numberofHibernators; i++) {
			workers.add(new DataHibernatorWorker(reader));
		}
	}
}
