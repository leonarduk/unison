/**
 * UNISoNControllerTest
 *
 * @author ${author}
 * @since 17-Jun-2016
 */
package uk.co.sleonard.unison;

import javax.swing.JFrame;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class UNISoNControllerTest {

	private UNISoNController controller;

	@Before
	public void setUp() throws Exception {
		final JFrame frame = Mockito.mock(JFrame.class);
		this.controller = UNISoNController.create(frame);
	}

	@Test
	public final void testCancelDownload() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testCreate() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testCreateJFrame() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testGetAnalysis() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testGetDatabase() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testGetDownloadPanel() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testGetFilter() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testGetGui() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testGetHeaderDownloader() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testGetHelper() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testGetInstance() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testGetNntpHost() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testGetNntpReader() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testGetQueue() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testGetSession() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testHelper() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testListNewsgroups() {
		Assert.fail("Not yet implemented");
	}

	public final void testMatrixType() {
		this.controller.setMatrixType(MatrixType.REPLY_TO_ALL);
		Assert.assertEquals(MatrixType.REPLY_TO_ALL, this.controller.getMatrixType());
	}

	@Test
	public final void testQuickDownload() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testSetConnectedState() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testSetConnectingState() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testSetDownloadingState() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testSetDownloadPanel() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testSetFrame() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testSetGui() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testSetHeaderDownloaderFinished() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testSetIdleState() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testSetNntpHost() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testSetSession() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testStopDownload() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testStoreNewsgroups() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public final void testSwitchFiltered() {
		Assert.fail("Not yet implemented");
	}

}
