/**
 * HeaderDownloadWorkerIT
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.LinkedBlockingQueue;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.utils.DownloaderImpl;
import uk.co.sleonard.unison.utils.StringUtils;

@Ignore("Requires a live NNTP server and is disabled to avoid external network calls")
public class HeaderDownloadWorkerIT {

	public static LinkedBlockingQueue<NewsArticle> populateQueueWithOneRealMessage()
	        throws IOException, UNISoNException {
		final LinkedBlockingQueue<NewsArticle> queue = new LinkedBlockingQueue<>();
		try (final Reader reader = NewsClientIT.downloadFirstMessage();) {
			final String nntpHost = StringUtils.loadServerList()[0];
			final LinkedBlockingQueue<NewsArticle> queue1 = new LinkedBlockingQueue<>();
			final NewsClient newsClient1 = new NewsClientImpl();
			final HibernateHelper helper2 = null;
			final Session session2 = null;
			final NewsGroupReader groupReader = null;
			final HeaderDownloadWorker worker = new HeaderDownloadWorker(
			        new LinkedBlockingQueue<>(), new DownloaderImpl(nntpHost, queue1, newsClient1,
			                groupReader, helper2, session2));
			worker.setMode(DownloadMode.BASIC);

			final BufferedReader bufReader = new BufferedReader(reader);

			final String line = bufReader.readLine();
			worker.processMessage(queue, line);
			worker.fullstop();
		}
		return queue;
	}

	@Test
	public final void testStoreArticleInfo()
	        throws IOException, UNISoNException, InterruptedException {
		final LinkedBlockingQueue<NewsArticle> queue = HeaderDownloadWorkerIT
		        .populateQueueWithOneRealMessage();
		Assert.assertEquals(1, queue.size());
		final NewsArticle article = queue.take();
		Assert.assertNotNull(article.getSubject());

	}

}
