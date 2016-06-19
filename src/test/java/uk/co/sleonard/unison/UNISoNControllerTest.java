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

	}

	@Test
	public final void testCreate() {

	}

	@Test
	public final void testCreateJFrame() {

	}

	@Test
	public final void testGetAnalysis() {

	}

	@Test
	public final void testGetDatabase() {

	}

	@Test
	public final void testGetDownloadPanel() {

	}

	@Test
	public final void testGetFilter() {

	}

	@Test
	public final void testGetGui() {

	}

	@Test
	public final void testGetHeaderDownloader() {

	}

	@Test
	public final void testGetHelper() {

	}

	@Test
	public final void testGetInstance() {

	}

	@Test
	public final void testGetNntpHost() {

	}

	@Test
	public final void testGetNntpReader() {

	}

	@Test
	public final void testGetQueue() {

	}

	@Test
	public final void testGetSession() {

	}

	@Test
	public final void testHelper() {

	}

	@Test
	public final void testListNewsgroups() {

	}

	public final void testMatrixType() {
		this.controller.setMatrixType(MatrixType.REPLY_TO_ALL);
		Assert.assertEquals(MatrixType.REPLY_TO_ALL, this.controller.getMatrixType());
	}

	@Test
	public final void testQuickDownload() {

	}

	@Test
	public final void testSetConnectedState() {

	}

	@Test
	public final void testSetConnectingState() {

	}

	@Test
	public final void testSetDownloadingState() {

	}

	@Test
	public final void testSetDownloadPanel() {

	}

	@Test
	public final void testSetFrame() {

	}

	@Test
	public final void testSetGui() {

	}

	@Test
	public final void testSetHeaderDownloaderFinished() {

	}

	@Test
	public final void testSetIdleState() {

	}

	@Test
	public final void testSetNntpHost() {

	}

	@Test
	public final void testSetSession() {

	}

	@Test
	public final void testStopDownload() {

	}

	@Test
	public final void testStoreNewsgroups() {

	}

	@Test
	public final void testSwitchFiltered() {

	}

}
