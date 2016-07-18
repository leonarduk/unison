/**
 * FullDownloadWorkerTest
 * 
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.input;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import uk.co.sleonard.unison.UNISoNControllerFX;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.UNISoNLogger;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.utils.StringUtils;

/**
 * The Class FullDownloadWorkerTest.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
public class FullDownloadWorkerTest {

	/** The worker. */
	private FullDownloadWorker worker;

	/**
	 * Setup.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		this.worker = new FullDownloadWorker(StringUtils.loadServerList()[0], null);
	}

	/**
	 * Test AddDownloadRequest.
	 */
	@Ignore
	@Test
	public void testAddDownloadRequest() {
		try {
			FullDownloadWorker.addDownloadRequest("<n9rgdm$g9b$3@news4.open-news-network.org>", DownloadMode.ALL,
					Mockito.mock(UNISoNLogger.class));
			Assert.assertTrue(FullDownloadWorker.queueSize() >= 1);
		} catch (final UNISoNException e) {
			Assert.fail("ERROR: " + e.getMessage());
		}
	}

	/**
	 * Test Contructor.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testConstruct() {
		FullDownloadWorker actual;
		try {
			actual = new FullDownloadWorker(StringUtils.loadServerList()[0], Mockito.mock(LinkedBlockingQueue.class));
			Assert.assertNotNull(actual);
		} catch (final UNISoNException e) {
			Assert.fail("ERROR: " + e.getMessage());
		}
	}

	/**
	 * Test DownloadArticle.
	 */
	@Test
	public void testDownloadArticle() {
		final DownloadRequest request = new DownloadRequest("<n9rgdm$g9b$3@news4.open-news-network.org>",
				DownloadMode.ALL);
		try {
			final NewsArticle actual = this.worker.downloadArticle(request);
			Assert.assertNotNull(actual);
		} catch (final UNISoNException e) {
			Assert.fail("ERROR: " + e.getMessage());
		}

	}

	/**
	 * Test DownloadFullMessage.
	 */
	@Ignore
	@Test
	public void testDownloadFullMessage() {
		Assert.fail("Not yet implemented");
	}

	/**
	 * Test finished.
	 */
	@Ignore
	@Test
	public void testFinished() {
		Assert.fail("Not yet implemented");
	}

	/**
	 * Test fullDownloadWorker.
	 */
	// 5 2006-04-28 <Baf4g.374$Lj1.115@fe10.lga> Re: Novel brain-penetrating
	// antioxidant 4 2 <1146147483.974309.7550@t31g2000cwb.googlegroups.com>
	// <1146149358.240977.254030@t31g2000cwb.googlegroups.com>
	// <1146149630.481616.212070@i39g2000cwa.googlegroups.com>
	// 4 mult-sclerosis 3 -1 -1 -1 -1 uk.people.support.mult-sclerosis true
	@Test
	@Ignore
	public void testFullDownloadWorker() {
		final LinkedBlockingQueue<NewsArticle> queue = new LinkedBlockingQueue<NewsArticle>();
		FullDownloadWorker worker = null;
		try {
			worker = new FullDownloadWorker(UNISoNControllerFX.getInstance().getNntpHost(), queue);
		} catch (final UNISoNException e1) {
			e1.printStackTrace();
		}
		final DownloadRequest request = new DownloadRequest("<Baf4g.374$Lj1.115@fe10.lga>",

				DownloadMode.ALL);
		try {
			final NewsArticle article = worker.downloadFullMessage(request);
			System.out.println("Downloaded: " + article);
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final UNISoNException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test QueueSize.
	 */
	@Ignore
	@Test
	public void testQueueSize() {
		Assert.fail("Not yet implemented");
	}

	/**
	 * Test ReaderToString.
	 */
	@Ignore
	@Test
	public void testReaderToString() {
		Assert.fail("Not yet implemented");
	}

	/**
	 * Test start downloaders.
	 */
	@Ignore
	@Test
	public void testStartDownloaders() {
		Assert.fail("Not yet implemented");
	}

}
