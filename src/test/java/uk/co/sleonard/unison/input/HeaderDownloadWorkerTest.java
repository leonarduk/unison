/**
 * HeaderDownloadWorkerTest
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.input;

import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import com.ibm.icu.util.Calendar;

import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.UNISoNLogger;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.utils.StringUtils;

/**
 * The Class HeaderDownloadWorker.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
public class HeaderDownloadWorkerTest {

	private static Logger			lo1gger	= Logger.getLogger(HeaderDownloadWorkerTest.class);
	private HeaderDownloadWorker	worker;

	/**
	 * Setup.
	 */
	@Before
	public void setUp() throws Exception {
		this.worker = new HeaderDownloadWorker(new LinkedBlockingQueue<>());
	}

	/**
	 * Test construct
	 */

	@Test
	public void testConstruct() {
	}

	/**
	 * Test if download then finished.
	 */
	@Test
	public void testFinished() {
		this.worker.resume();
		Assert.assertTrue(this.worker.isDownloading());
		this.worker.finished();
		Assert.assertFalse(this.worker.isDownloading());
	}

	/**
	 * Test fullStop
	 */
	@Test
	public void testFullstop() {
		System.out.println("Wait 2 secs and stop");
		try {
			Thread.sleep(2000);
		}
		catch (final InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Stop");
		this.worker.fullstop();
	}

	/**
	 * Test Initialize
	 *
	 * @throws UNISoNException
	 *             Signals that an exception has occurred.
	 */
	@Test

	public void testInitialise() throws UNISoNException {

		final NewsGroupReader ngr = Mockito.mock(NewsGroupReader.class);
		final NewsClient nc = PowerMockito.mock(NewsClient.class);
		ngr.client = nc;
		final UNISoNLogger uniLog = Mockito.mock(UNISoNLogger.class);
		final Date fromAndTo = Calendar.getInstance().getTime();
		Assert.assertFalse(this.worker.isDownloading());

		final String server = StringUtils.loadServerList()[0];

		this.worker.initialise(ngr, 0, 1, server, "newsgroup", uniLog, DownloadMode.ALL, fromAndTo,
		        fromAndTo);
		;

		Assert.assertTrue(this.worker.isDownloading());
	}

	/**
	 * Test if in download
	 */
	@Test
	public void testIsDownloading() {
		Assert.assertFalse(this.worker.isDownloading());
		this.worker.resume();
		Assert.assertTrue(this.worker.isDownloading());
	}

	/**
	 * Test NotifyObservers.
	 */
	@Test
	public void testNotifyObservers() {
		this.worker.notifyObservers();
	}

	/**
	 * Test Pause
	 */
	@Test
	public void testPause() {
		this.worker.resume();
		Assert.assertTrue(this.worker.isDownloading());
		this.worker.pause();
		Assert.assertFalse(this.worker.isDownloading());
	}

	/**
	 * Test Resume
	 */
	@Test
	public void testResume() {
		Assert.assertFalse(this.worker.isDownloading());
		this.worker.resume();
		Assert.assertTrue(this.worker.isDownloading());
	}

	/**
	 * Test sToreArticleInfo
	 *
	 * @throws UNISoNException
	 *             Signals that an exception has occurred.
	 */
	@Test
	public void testStoreArticleInfo() throws UNISoNException {
		final LinkedBlockingQueue<NewsArticle> queue = new LinkedBlockingQueue<>();
		final boolean actual = this.worker.storeArticleInfo(queue);
		Assert.assertTrue(actual);
	}
}
