/**
 * UsenetUserTest
 *
 * @author ${author}
 * @since 20-Jun-2016
 */
package uk.co.sleonard.unison.datahandling.DAO;

import org.junit.Assert;
import org.junit.Test;

/**
 * Extends UsenetUser to change setId(protected)
 *
 * @author Elton <elton_12_nunes@hotmail.com>
 *
 */
@SuppressWarnings("serial")
class TestUsenetUser extends UsenetUser {
	@Override
	public UsenetUser setEmail(final String email) {
		return super.setEmail(email);
	}
}

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
		final String expected = "John";
		final String expected2 = "john@java.com";
		final String expected3 = "127.0.0.1";
		final String expected4 = "Programming";
		final Location expected5 = new Location();
		actual = new UsenetUser(expected, expected2, expected3, expected4, expected5);
		Assert.assertEquals(expected, actual.getName());
		Assert.assertEquals(expected2, actual.getEmail());
		Assert.assertEquals(expected3, actual.getIpaddress());
		Assert.assertEquals(expected4, actual.getGender());
		Assert.assertEquals(expected5, actual.getLocation());
	}

	/**
	 * Test getEmail.
	 */
	@Test
	public void testGetEmail() {
		final String expected = "user@email.com";
		final UsenetUser actual = new UsenetUser();
		Assert.assertNull(actual.getEmail());
		actual.setEmail(expected);
		Assert.assertEquals(expected, actual.getEmail());
	}

	/**
	 * Test getGender.
	 */
	@Test
	public void testGetGender() {
		final String expected = "gender";
		final UsenetUser actual = new UsenetUser();
		Assert.assertNull(actual.getGender());
		actual.setGender(expected);
		Assert.assertEquals(expected, actual.getGender());
	}

	/**
	 * Test getId.
	 */
	@Test
	public void testGetId() {
		Assert.assertEquals(0, new UsenetUser().getId());
	}

	/**
	 * Test getIpAddress.
	 */
	@Test
	public void testGetIpaddress() {
		final String expected = "127.0.0.1";
		final UsenetUser actual = new UsenetUser();
		Assert.assertNull(actual.getIpaddress());
		actual.setIpaddress(expected);
		Assert.assertEquals(expected, actual.getIpaddress());
	}

	/**
	 * Test getLocation.
	 */
	@Test
	public void testGetLocation() {
		final Location expected = new Location(null, null, "BR", false, null, null);
		final UsenetUser actual = new UsenetUser();
		Assert.assertNull(actual.getLocation());
		actual.setLocation(expected);
		Assert.assertEquals(expected.getCountryCode(), actual.getLocation().getCountryCode());
	}

	/**
	 * Test getName.
	 */
	@Test
	public void testGetName() {
		final String expected = "name";
		final UsenetUser actual = new UsenetUser();
		Assert.assertNull(actual.getName());
		actual.setName(expected);
		Assert.assertEquals(expected, actual.getName());
	}

	/**
	 * Test hashCode
	 */
	@Test
	public void testHashCode() {
		final TestUsenetUser actual1 = new TestUsenetUser();
		final TestUsenetUser actual2 = new TestUsenetUser();
		actual1.setEmail("a@2");
		actual2.setEmail("a@22");
		Assert.assertTrue(actual1.hashCode() != actual2.hashCode());
	}

	/**
	 * Test toString.
	 */
	@Test
	public void testToString() {
		final String expected = "elton(elton_12_nunes@hotmail.com)";
		final UsenetUser actual = new UsenetUser("elton", "elton_12_nunes@hotmail.com", null,
		        expected, null);
		Assert.assertEquals(expected, actual.toString());
	}

}
