package uk.co.sleonard.unison.datahandling.DAO.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;

/**
 * The Class DownloadRequestTest.
 * 
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.2.0
 *
 */
public class DownloadRequestTest {

	/**
	 * Test getUsenetId.
	 */
	@Test
	public void testGetUsenetID() {
		String expected = new String("usenet");
		DownloadRequest actual = new DownloadRequest(expected, DownloadMode.ALL);
		assertEquals(expected, actual.getUsenetID());
	}

	/**
	 * Test getMode.
	 */
	@Test
	public void testGetMode() {
		DownloadMode expected = DownloadMode.ALL;
		DownloadRequest actual = new DownloadRequest("usenet", expected);
		assertEquals(expected, actual.getMode());
	}

}
