package uk.co.sleonard.unison.input.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Date;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.gui.UNISoNException;
import uk.co.sleonard.unison.gui.UNISoNLogger;
import uk.co.sleonard.unison.input.HeaderDownloadWorker;
import uk.co.sleonard.unison.input.NewsClient;
import uk.co.sleonard.unison.input.NewsGroupReader;
import uk.co.sleonard.unison.utils.StringUtils;

import com.ibm.icu.util.Calendar;

/**
 * The Class HeaderDownloadWorker.
 */
public class HeaderDownloadWorkerTest {

	private HeaderDownloadWorker worker;

	/**
	 * Setup.
	 */
	@Before
	public void setUp() throws Exception {
		worker = new HeaderDownloadWorker();
	}

	/**
	 * Test Class Constructor.
	 */
	@Test
	public void testConstructor() {
		HeaderDownloadWorker worker = new HeaderDownloadWorker();
		assertNotNull(worker);
	}

	/**
	 * Test NotifyObservers.
	 */
	@Test
	public void testNotifyObservers() {
		this.worker.notifyObservers();
	}

	/**
	 * Test construct
	 */
	@Ignore
	@Test
	public void testConstruct() {
	}

	/**
	 * Test if download then finished.
	 */
	@Test
	public void testFinished() {
		this.worker.resume();
		assertTrue(this.worker.isDownloading());
		this.worker.finished();
		assertFalse(this.worker.isDownloading());
	}

	/**
	 * Test if in download
	 */
	@Test
	public void testIsDownloading() {
		assertFalse(this.worker.isDownloading());
		this.worker.resume();
		assertTrue(this.worker.isDownloading());
	}

	/**
	 * Test sToreArticleInfo
	 * @throws UNISoNException Signals that an exception has occurred.
	 */
	@Test
	public void testStoreArticleInfo() throws UNISoNException {
		boolean actual = this.worker.storeArticleInfo();
		assertTrue(actual);
	}

	/**
	 * Test fullStop
	 */
	@Test
	@Ignore
	public void testFullstop() {
		HeaderDownloadWorker worker = new HeaderDownloadWorker();
		System.out.println("Wait 2 secs and stop");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Stop");
		worker.fullstop();
	}

	/**
	 * Test Initialize
	 * @throws UNISoNException Signals that an exception has occurred.
	 */
	@Test
	@Ignore
	public void testInitialise() throws UNISoNException {

		NewsGroupReader ngr = mock(NewsGroupReader.class);
		NewsClient nc = PowerMockito.mock(NewsClient.class);
		ngr.client = nc;
		UNISoNLogger uniLog = mock(UNISoNLogger.class);
		Date fromAndTo = Calendar.getInstance().getTime();
		assertFalse(this.worker.isDownloading());
		
		this.worker.initialise(ngr, 0, 1, StringUtils.loadServerList()[0],
				"newsgroup", uniLog, DownloadMode.ALL, fromAndTo, fromAndTo);;
				
		assertTrue(this.worker.isDownloading());
	}

	/**
	 * Test Resume
	 */
	@Test
	public void testResume() {
		assertFalse(this.worker.isDownloading());
		this.worker.resume();
		assertTrue(this.worker.isDownloading());
	}

	/**
	 * Test Pause
	 */
	@Test
	public void testPause() {
		this.worker.resume();
		assertTrue(this.worker.isDownloading());
		this.worker.pause();
		assertFalse(this.worker.isDownloading());
	}
}
