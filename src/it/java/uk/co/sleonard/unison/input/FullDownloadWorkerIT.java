/**
 * FullDownloadWorkerIT
 *
 * @author ${author}
 * @since 23-Jun-2016
 */
package uk.co.sleonard.unison.input;

import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.utils.StringUtils;

public class FullDownloadWorkerIT {

	private FullDownloadWorker					worker;
	private LinkedBlockingQueue<NewsArticle>	outQueue;

	@Before
	public void setUp() throws Exception {
		final String server = StringUtils.loadServerList()[0];
		this.outQueue = HeaderDownloadWorkerIT.populateQueueWithOneRealMessage();

		final NewsClient newsClient = new NewsClientImpl();
		this.worker = new FullDownloadWorker(server, this.outQueue, newsClient);
	}

	@Test
	public final void testDownloadArticleHeaders() throws UNISoNException, InterruptedException {
		final NewsArticle header = this.outQueue.take();
		final String usenetID = header.getArticleId();
		final DownloadRequest request = new DownloadRequest(usenetID, DownloadMode.HEADERS);
		final NewsArticle article = this.worker.downloadArticle(request);
		Assert.assertEquals(header.getArticleId(), article.getArticleId());
	}

	@Test
	public final void testDownloadFullArticle() throws UNISoNException, InterruptedException {
		final NewsArticle header = this.outQueue.take();
		final String usenetID = header.getArticleId();
		final DownloadRequest request = new DownloadRequest(usenetID, DownloadMode.ALL);
		final NewsArticle article = this.worker.downloadArticle(request);
		Assert.assertEquals(header.getArticleId(), article.getArticleId());
	}

}
