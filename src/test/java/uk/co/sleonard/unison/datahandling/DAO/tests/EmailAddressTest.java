package uk.co.sleonard.unison.datahandling.DAO.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import uk.co.sleonard.unison.datahandling.DAO.EmailAddress;

/**
 * The Class EmailAddressTest.
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

}
