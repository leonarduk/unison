/**
 * IpAddressTest
 * 
 * @author ${author}
 * @since 20-Jun-2016
 */
package uk.co.sleonard.unison.datahandling.DAO;

import org.junit.Assert;
import org.junit.Test;

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
		Assert.assertEquals(0, new IpAddress().getId());
	}

	/**
	 * Test getIpAddress.
	 */
	@Test
	public void testGetIpAddress() {
		final String expected = "127.0.0.1";
		IpAddress actual = new IpAddress();
		Assert.assertNull(actual.getIpAddress());
		actual.setIpAddress(expected);
		Assert.assertEquals(expected, actual.getIpAddress());
		actual = new IpAddress(expected, null);
		Assert.assertEquals(expected, actual.getIpAddress()); // Constructor Test
	}

	/**
	 * Test getLocation.
	 */
	@Test
	public void testGetLocation() {
		final Location expected = new Location();
		expected.setCity("Madrid");
		IpAddress actual = new IpAddress();
		Assert.assertNull(actual.getLocation());
		actual.setLocation(expected);
		Assert.assertEquals(expected.getCity(), actual.getLocation().getCity());
		actual = new IpAddress(null, expected);
		Assert.assertEquals(expected.getCity(), actual.getLocation().getCity()); // Constructor
		// Test
	}

	/**
	 * Test hashCode
	 */
	@Test
	public void testHashCode() {
		final TestIpAddress actual1 = new TestIpAddress();
		final TestIpAddress actual2 = new TestIpAddress();
		actual1.setIpAddress("124");
		actual2.setIpAddress("556");
		Assert.assertTrue(actual1.hashCode() != actual2.hashCode());
	}

}

/**
 * Extends IpAddress class to change setId(protected)
 *
 * @author Elton <elton_12_nunes@hotmail.com>
 *
 */
@SuppressWarnings("serial")
class TestIpAddress extends IpAddress {
	@Override
	public void setIpAddress(final String IpAddress) {
		super.setIpAddress(IpAddress);
	}
}
