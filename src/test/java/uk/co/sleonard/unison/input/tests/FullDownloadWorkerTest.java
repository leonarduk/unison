package uk.co.sleonard.unison.input.tests;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.gui.UNISoNController;
import uk.co.sleonard.unison.gui.UNISoNException;
import uk.co.sleonard.unison.input.FullDownloadWorker;
import uk.co.sleonard.unison.input.NewsArticle;
import uk.co.sleonard.unison.utils.StringUtils;

/**
 * The Class FullDownloadWorkerTest.
 */
public class FullDownloadWorkerTest {

	private FullDownloadWorker worker;
	
	@Before
	public void setUp() throws Exception {
		worker = new FullDownloadWorker(StringUtils.loadServerList()[0], null);
	}

	@Ignore
	@Test
	public void testConstruct() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testFinished() {
		fail("Not yet implemented");
	}

	// 5 2006-04-28 <Baf4g.374$Lj1.115@fe10.lga> Re: Novel brain-penetrating
	// antioxidant 4 2 <1146147483.974309.7550@t31g2000cwb.googlegroups.com>
	// <1146149358.240977.254030@t31g2000cwb.googlegroups.com>
	// <1146149630.481616.212070@i39g2000cwa.googlegroups.com>
	// 4 mult-sclerosis 3 -1 -1 -1 -1 uk.people.support.mult-sclerosis true
	@Test
	@Ignore
	public void testFullDownloadWorker() {
		LinkedBlockingQueue<NewsArticle> queue = new LinkedBlockingQueue<NewsArticle>();
		FullDownloadWorker worker = null;
		try {
			worker = new FullDownloadWorker(UNISoNController.getInstance().getNntpHost(), queue);
		} catch (UNISoNException e1) {
			e1.printStackTrace();
		}
		DownloadRequest request = new DownloadRequest("<Baf4g.374$Lj1.115@fe10.lga>",

		DownloadMode.ALL);
		try {
			NewsArticle article = worker.downloadFullMessage(request);
			System.out.println("Downloaded: " + article);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UNISoNException e) {
			e.printStackTrace();
		}
	}

	@Ignore
	@Test
	public void testAddDownloadRequest() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testDownloadArticle() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testDownloadFullMessage() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testReaderToString() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testStartDownloaders() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testQueueSize() {
		fail("Not yet implemented");
	}
	
}
