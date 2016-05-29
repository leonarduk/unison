/**
 * EmailAddressTest
 * 
 * @author ${author}
 * @since 29-May-2016
 */
package uk.co.sleonard.unison.datahandling.DAO;

import org.junit.Assert;
import org.junit.Test;

import uk.co.sleonard.unison.datahandling.DAO.EmailAddress;

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
		EmailAddress actual = new EmailAddress("user", "email@corporate.com", "127.0.0.1");
		Assert.assertEquals(2123816993, actual.hashCode());
		actual = new EmailAddress("user", "email@corporate.com", null);
		Assert.assertEquals(-1612454402, actual.hashCode());
		actual = new EmailAddress("user", null, null);
		Assert.assertEquals(3629098, actual.hashCode());
		actual = new EmailAddress(null, null, null);
		Assert.assertEquals(29791, actual.hashCode());
	}
}
