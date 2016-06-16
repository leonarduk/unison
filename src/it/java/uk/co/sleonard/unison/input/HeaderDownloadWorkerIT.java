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

import org.junit.Assert;
import org.junit.Test;

import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;

public class HeaderDownloadWorkerIT {

	public static LinkedBlockingQueue<NewsArticle> populateQueueWithOneRealMessage()
	        throws IOException, UNISoNException {
		final LinkedBlockingQueue<NewsArticle> queue = new LinkedBlockingQueue<>();
		try (final Reader reader = NewsClientIT.downloadFirstMessage();) {
			final HeaderDownloadWorker worker = new HeaderDownloadWorker(
			        new LinkedBlockingQueue<>());
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
