package uk.co.sleonard.unison.datahandling.DAO;

import org.junit.jupiter.api.Test;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The Class DownloadRequestTest.
 *
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.2.0
 */
public class DownloadRequestTest {

    /**
     * Test getUsenetId.
     */
    @Test
    public void testGetUsenetID() {
        var expected = new String("usenet");
        var actual = new DownloadRequest(expected, DownloadMode.ALL);
        assertEquals(expected, actual.usenetID());
    }

    /**
     * Test getMode.
     */
    @Test
    public void testGetMode() {
        var expected = DownloadMode.ALL;
        var actual = new DownloadRequest("usenet", expected);
        assertEquals(expected, actual.mode());
    }

}
