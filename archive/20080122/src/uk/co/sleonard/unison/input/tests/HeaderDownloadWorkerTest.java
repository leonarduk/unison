package uk.co.sleonard.unison.input.tests;

import uk.co.sleonard.unison.input.HeaderDownloadWorker;
import junit.framework.TestCase;

public class HeaderDownloadWorkerTest extends TestCase {

	public HeaderDownloadWorkerTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();

	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testConstruct() {
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

	public void testFinished() {
		// fail("Not yet implemented");
	}

	public void testStoreArticleInfo() {
		// fail("Not yet implemented");
	}

	public void testHeaderDownloadWorker() {
		// fail("Not yet implemented");
	}

	public void testInitialise() {
		// fail("Not yet implemented");
	}

}
