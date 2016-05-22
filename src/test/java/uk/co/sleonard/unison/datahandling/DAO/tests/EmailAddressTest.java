package uk.co.sleonard.unison.datahandling.DAO.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.co.sleonard.unison.datahandling.DAO.EmailAddress;

/**
 * The Class EmailAddressTest.
 * 
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.0.0
 *
 */
public class EmailAddressTest {

	/**
	 * Test getName.
	 */
	@Test
	public void testGetName() {
		String expected = "name";
		EmailAddress actual = new EmailAddress(expected, null, null);
		assertEquals(expected, actual.getName());
	}

	/**
	 * Test getEmail.
	 */
	@Test
	public void testGetEmail() {
		String expected = "email";
		EmailAddress actual = new EmailAddress(null, expected, null);
		assertEquals(expected, actual.getEmail());
	}

	/**
	 * Test getIpAddress.
	 */
	@Test
	public void testGetIpAddress() {
		String expected = "ipAddress";
		EmailAddress actual = new EmailAddress(null, null, expected);
		assertEquals(expected, actual.getIpAddress());
	}

	/**
	 * Test HashCode.
	 */
	@Test
	public void testHashCode() {
		EmailAddress actual = new EmailAddress("user", "email@corporate.com", "127.0.0.1");
		assertEquals(2123816993, actual.hashCode());
		actual = new EmailAddress("user", "email@corporate.com", null);
		assertEquals(-1612454402, actual.hashCode());
		actual = new EmailAddress("user", null, null);
		assertEquals(3629098, actual.hashCode());
		actual = new EmailAddress(null, null, null);
		assertEquals(29791, actual.hashCode());
	}
}
