package uk.co.sleonard.unison.datahandling.DAO.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import uk.co.sleonard.unison.datahandling.DAO.Location;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;

/**
 * The Class IpAddressTest.
 * 
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.2.0
 *
 */
public class UsenetUserTest {

	/**
	 * Test Constructor
	 */
	@Test
	public void testConstructor() {
		UsenetUser actual = null;
		String expected = "John";
		String expected2 = "john@java.com";
		String expected3 = "127.0.0.1";
		String expected4 = "Programming";
		Location expected5 = new Location();
		actual = new UsenetUser(expected, expected2, expected3, expected4, expected5);
		assertEquals(expected, actual.getName());
		assertEquals(expected2, actual.getEmail());
		assertEquals(expected3, actual.getIpaddress());
		assertEquals(expected4, actual.getGender());
		assertEquals(expected5, actual.getLocation());
	}

	/**
	 * Test hashCode
	 */
	@Test
	public void testHashCode() {
		assertEquals(0, new UsenetUser().hashCode());
	}

	/**
	 * Test toString.
	 */
	@Test
	public void testToString() {
		String expected = "elton(elton_12_nunes@hotmail.com)";
		UsenetUser actual = new UsenetUser("elton", "elton_12_nunes@hotmail.com", null);
		assertEquals(expected, actual.toString());
	}

	/**
	 * Test getEmail.
	 */
	@Test
	public void testGetEmail() {
		String expected = "user@email.com";
		UsenetUser actual = new UsenetUser();
		assertNull(actual.getEmail());
		actual.setEmail(expected);
		assertEquals(expected, actual.getEmail());
	}

	/**
	 * Test getGender.
	 */
	@Test
	public void testGetGender() {
		String expected = "gender";
		UsenetUser actual = new UsenetUser();
		assertNull(actual.getGender());
		actual.setGender(expected);
		assertEquals(expected, actual.getGender());
	}

	/**
	 * Test getId.
	 */
	@Test
	public void testGetId() {
		assertEquals(0, new UsenetUser().getId());
	}

	/**
	 * Test getIpAddress.
	 */
	@Test
	public void testGetIpaddress() {
		String expected = "127.0.0.1";
		UsenetUser actual = new UsenetUser();
		assertNull(actual.getIpaddress());
		actual.setIpaddress(expected);
		assertEquals(expected, actual.getIpaddress());
	}

	/**
	 * Test getLocation.
	 */
	@Test
	public void testGetLocation() {
		Location expected = new Location(null, null, "BR", false, null, null);
		UsenetUser actual = new UsenetUser();
		assertNull(actual.getLocation());
		actual.setLocation(expected);
		assertEquals(expected.getCountryCode(), actual.getLocation().getCountryCode());
	}

	/**
	 * Test getName.
	 */
	@Test
	public void testGetName() {
		String expected = "name";
		UsenetUser actual = new UsenetUser();
		assertNull(actual.getName());
		actual.setName(expected);
		assertEquals(expected, actual.getName());
	}

}
