/**
 * FullDownloadWorkerTest
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.input;

import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.UNISoNLogger;
import uk.co.sleonard.unison.datahandling.HibernateHelper;
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
	private FullDownloadWorker	worker;
	private NewsClient			newsClient;
	private LinkedBlockingQueue	outQueue;

	/**
	 * Setup.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		this.newsClient = Mockito.mock(NewsClient.class);
		this.outQueue = new LinkedBlockingQueue<>();
		this.worker = new FullDownloadWorker(StringUtils.loadServerList()[0], this.outQueue,
		        this.newsClient);
	}

	/**
	 * Test AddDownloadRequest.
	 */
	@Test
	public void testAddDownloadRequest() {
		try {
			final String nntpHost = "testserver";
			final LinkedBlockingQueue<NewsArticle> queue = new LinkedBlockingQueue<>();
			final NewsGroupReader reader = Mockito.mock(NewsGroupReader.class);
			final HibernateHelper helper = Mockito.mock(HibernateHelper.class);
			final Session session = Mockito.mock(Session.class);
			FullDownloadWorker.addDownloadRequest("<n9rgdm$g9b$3@news4.open-news-network.org>",
			        DownloadMode.ALL, Mockito.mock(UNISoNLogger.class), nntpHost, queue,
			        this.newsClient, reader, helper, session);
			queue.add(new NewsArticle("123", 1, new Date(), "eg@mail.com", "Lets talk", "", "alt"));
			FullDownloadWorker.addDownloadRequest("<n9rgdm$g9b$3@news4.open-news-network.org>",
			        DownloadMode.ALL, Mockito.mock(UNISoNLogger.class), nntpHost, queue,
			        this.newsClient, reader, helper, session);

			// Assert.assertTrue(FullDownloadWorker.queueSize() >= 1);
		}
		catch (final UNISoNException e) {
			e.printStackTrace();
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
			actual = new FullDownloadWorker(StringUtils.loadServerList()[0],
			        new LinkedBlockingQueue(), this.newsClient);
			Assert.assertNotNull(actual);
		}
		catch (final UNISoNException e) {
			Assert.fail("ERROR: " + e.getMessage());
		}
	}

	@Ignore
	@Test
	public void testConvertHeaderStringToArticle() throws Exception {
		final String theInfo = "";
		this.worker.convertHeaderStringToArticle(theInfo);
	}

	/**
	 * Test DownloadArticle.
	 *
	 * @throws IOException
	 */
	@Ignore        // hangs
	@Test
	public void testDownloadArticle() throws IOException {
		final Reader value = Mockito.mock(Reader.class);
		Mockito.when(this.newsClient.retrieveArticle(Matchers.anyString())).thenReturn(value);
		final DownloadRequest request = new DownloadRequest(
		        "<n9rgdm$g9b$3@news4.open-news-network.org>", DownloadMode.ALL);
		try {
			final NewsArticle actual = this.worker.downloadArticle(request);
			Assert.assertNotNull(actual);
		}
		catch (final UNISoNException e) {
			Assert.fail("ERROR: " + e.getMessage());
		}

	}

	@Test
	public void testFailToConnect() throws Exception {
		Mockito.doThrow(new IOException("Failed to connect")).when(this.newsClient)
		        .connect(Matchers.anyString());
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
	public void testFullDownloadWorker() {
		final LinkedBlockingQueue<NewsArticle> queue = new LinkedBlockingQueue<>();
		FullDownloadWorker worker = null;
		try {
			final String nntpHost = "testserver";
			worker = new FullDownloadWorker(nntpHost, queue, this.newsClient);
		}
		catch (final UNISoNException e1) {
			e1.printStackTrace();
		}
		final DownloadRequest request = new DownloadRequest("<Baf4g.374$Lj1.115@fe10.lga>",

		DownloadMode.ALL);
		try {
			final NewsArticle article = worker.downloadFullMessage(request);
			System.out.println("Downloaded: " + article);
		}
		catch (final IOException e) {
			e.printStackTrace();
		}
		catch (final UNISoNException e) {
			e.printStackTrace();
		}
	}
}
