package uk.co.sleonard.unison.input.tests;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import uk.co.sleonard.unison.input.HeaderDownloadWorker;

@Ignore
public class HeaderDownloadWorkerTest {

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testConstructor() {
		HeaderDownloadWorker worker = new HeaderDownloadWorker();
	}

	@Ignore
	@Test
	public void testFullStop() {
		HeaderDownloadWorker worker = new HeaderDownloadWorker();
		System.out.println("Wait 2 secs and stop");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Stop");
		worker.fullstop();
		// fail("Not yet implemented");
	}
}
