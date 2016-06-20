/**
 * LocationTest
 * 
 * @author ${author}
 * @since 20-Jun-2016
 */
package uk.co.sleonard.unison.datahandling.DAO;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * The Class LocationTest.
 *
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.2.0
 *
 */
public class LocationTest {

	/** The city. */
	private final String city = "Boston";

	/** The country. */
	private final String country = "United States";

	/** The country code. */
	private final String countryCode = "USA";

	/**
	 * Test fullString.
	 */
	@Test
	public void testFullString() {
		final String expected = this.city + ", " + this.country + " (" + this.countryCode + ")";
		final Location actual = new Location(this.city, this.country, this.countryCode, true, null,
		        null);
		Assert.assertEquals(expected, actual.fullString());
	}

	/**
	 * Test getCity.
	 */
	@Test
	public void testGetCity() {
		final String expected = this.city;
		final Location actual = new Location();
		Assert.assertNull(actual.getCity());
		actual.setCity(expected);
		Assert.assertEquals(expected, actual.getCity());
	}

	/**
	 * Test getCountry.
	 */
	@Test
	public void testGetCountry() {
		final String expected = this.country;
		final Location actual = new Location();
		Assert.assertNull(actual.getCountry());
		actual.setCountry(expected);
		Assert.assertEquals(expected, actual.getCountry());
	}

	/**
	 * Test getCountryCode.
	 */
	@Test
	public void testGetCountryCode() {
		final String expected = this.countryCode;
		final Location actual = new Location();
		Assert.assertNull(actual.getCountryCode());
		actual.setCountryCode(expected);
		Assert.assertEquals(expected, actual.getCountryCode());
	}

	/**
	 * Test getId.
	 */
	@Test
	public void testGetId() {
		Assert.assertEquals(0, new Location().getId());
	}

	/**
	 * Test getIpAddresses.
	 */
	@Test
	public void testGetIpAddresses() {
		final List<IpAddress> expected = new ArrayList<>();
		final Location actual = new Location();
		Assert.assertEquals(expected.size(), actual.getIpAddresses().size());
		expected.add(new IpAddress());
		actual.setIpAddresses(expected);
		Assert.assertEquals(expected.size(), actual.getIpAddresses().size());
	}

	/**
	 * Test getPosters.
	 */
	@Test
	public void testGetPosters() {
		final List<UsenetUser> expected = new ArrayList<>();
		final Location actual = new Location();
		Assert.assertEquals(expected.size(), actual.getPosters().size());
		expected.add(new UsenetUser());
		actual.setPosters(expected);
		Assert.assertEquals(expected.size(), actual.getPosters().size());
	}

	/**
	 * Test hashCode
	 */
	@Test
	public void testHashCode() {
		final TestLocation actual1 = new TestLocation();
		final TestLocation actual2 = new TestLocation();
		actual1.setCity("testa");
		actual2.setCity("test3");
		Assert.assertTrue(actual1.hashCode() != actual2.hashCode());
	}

	/**
	 * Test isGuessed.
	 */
	@Test
	public void testIsGuessed() {
		final Location actual = new Location();
		Assert.assertFalse(actual.isGuessed());
		actual.setGuessed(true);
		Assert.assertTrue(actual.isGuessed());
	}

	/**
	 * Test toString.
	 */
	@Test
	public void testToString() {
		final String expected = this.city + " - " + this.countryCode;
		final Location actual = new Location(this.city, null, this.countryCode, true, null, null);
		Assert.assertEquals(expected, actual.toString());
	}

}

/**
 * Extends Location to change setId(protected)
 *
 * @author Elton <elton_12_nunes@hotmail.com>
 *
 */
@SuppressWarnings("serial")
class TestLocation extends Location {
	@Override
	public void setCity(final String City) {
		super.setCity(City);
	}
}
