/**
 * EmailAddressTest
 *
 * @author ${author}
 * @since 29-May-2016
 */
package uk.co.sleonard.unison.datahandling.DAO;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * The Class EmailAddressTest.
 *
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.2.0
 *
 */
public class EmailAddressTest {

    /**
     * Test getEmail.
     */
    @Test
    public void testGetEmail() {
        final String expected = "email";
        final EmailAddress actual = new EmailAddress(null, expected, null);
        Assert.assertEquals(expected, actual.getEmail());
    }

    /**
     * Test getIpAddress.
     */
    @Test
    public void testGetIpAddress() {
        final String expected = "ipAddress";
        final EmailAddress actual = new EmailAddress(null, null, expected);
        Assert.assertEquals(expected, actual.getIpAddress());
    }

    /**
     * Test getName.
     */
    @Test
    public void testGetName() {
        final String expected = "name";
        final EmailAddress actual = new EmailAddress(expected, null, null);
        Assert.assertEquals(expected, actual.getName());
    }

    /**
     * Test HashCode.
     */
    @Test
    public void testHashCode() {
        EmailAddress actual1 = null;
        EmailAddress actual2 = null;
        actual1 = new EmailAddress("user", "email@corporate.com", "127.0.0.1");
        actual2 = new EmailAddress("user2", "email2@corporate.com", "127.0.0.2");
        assertTrue(actual1.hashCode() != actual2.hashCode());
        actual1 = new EmailAddress("user", "email@corporate.com", null);
        actual2 = new EmailAddress("user2", "email2@corporate.com", null);
        assertTrue(actual1.hashCode() != actual2.hashCode());
        actual1 = new EmailAddress("user", null, null);
        actual2 = new EmailAddress("user2", null, null);
        assertTrue(actual1.hashCode() != actual2.hashCode());
        actual1 = new EmailAddress(null, null, null);
        actual2 = new EmailAddress(null, null, null);
        assertTrue(actual1.hashCode() == actual2.hashCode());
    }
}
