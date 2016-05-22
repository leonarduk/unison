package uk.co.sleonard.unison.datahandling.DAO.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import uk.co.sleonard.unison.datahandling.DAO.IpAddress;
import uk.co.sleonard.unison.datahandling.DAO.Location;

/**
 * The Class IpAddressTest.
 * 
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.2.0
 *
 */
public class IpAddressTest {

	/**
	 * Test getId.
	 */
	@Test
	public void testGetId() {
		assertEquals(0, new IpAddress().getId());
	}

	/**
	 * Test getIpAddress.
	 */
	@Test
	public void testGetIpAddress() {
		String expected = "127.0.0.1";
		IpAddress actual = new IpAddress();
		assertNull(actual.getIpAddress());
		actual.setIpAddress(expected);
		assertEquals(expected, actual.getIpAddress());
		actual = new IpAddress(expected, null);
		assertEquals(expected, actual.getIpAddress()); // Constructor Test
	}

	/**
	 * Test getLocation.
	 */
	@Test
	public void testGetLocation() {
		Location expected = new Location();
		expected.setCity("Madrid");
		IpAddress actual = new IpAddress();
		assertNull(actual.getLocation());
		actual.setLocation(expected);
		assertEquals(expected.getCity(), actual.getLocation().getCity());
		actual = new IpAddress(expected);
		assertEquals(expected.getCity(), actual.getLocation().getCity()); // Constructor
		                                                                  // Test
	}

}
