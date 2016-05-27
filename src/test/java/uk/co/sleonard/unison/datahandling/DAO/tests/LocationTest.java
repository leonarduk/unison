package uk.co.sleonard.unison.datahandling.DAO.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import uk.co.sleonard.unison.datahandling.DAO.IpAddress;
import uk.co.sleonard.unison.datahandling.DAO.Location;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;

/**
 * The Class LocationTest.
 * 
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.2.0
 *
 */
public class LocationTest {

	/** The city. */
	private String city = "Boston";

	/** The country. */
	private String country = "United States";

	/** The country code. */
	private String countryCode = "USA";

	/**
	 * Test toString.
	 */
	@Test
	public void testToString() {
		String expected = city + " - " + countryCode;
		Location actual = new Location(city, null, countryCode, true, null, null);
		assertEquals(expected, actual.toString());
	}

	/**
	 * Test fullString.
	 */
	@Test
	public void testFullString() {
		String expected = city + ", " + country + " (" + countryCode + ")";
		Location actual = new Location(city, country, countryCode, true, null, null);
		assertEquals(expected, actual.fullString());
	}

	/**
	 * Test getCity.
	 */
	@Test
	public void testGetCity() {
		String expected = city;
		Location actual = new Location();
		assertNull(actual.getCity());
		actual.setCity(expected);
		assertEquals(expected, actual.getCity());
	}

	/**
	 * Test getCountry.
	 */
	@Test
	public void testGetCountry() {
		String expected = country;
		Location actual = new Location();
		assertNull(actual.getCountry());
		actual.setCountry(expected);
		assertEquals(expected, actual.getCountry());
	}

	/**
	 * Test getCountryCode.
	 */
	@Test
	public void testGetCountryCode() {
		String expected = countryCode;
		Location actual = new Location();
		assertNull(actual.getCountryCode());
		actual.setCountryCode(expected);
		assertEquals(expected, actual.getCountryCode());
	}

	/**
	 * Test getId.
	 */
	@Test
	public void testGetId() {
		assertEquals(0, new Location().getId());
	}

	/**
	 * Test getIpAddresses.
	 */
	@Test
	public void testGetIpAddresses() {
		List<IpAddress> expected = new ArrayList<>();
		Location actual = new Location();
		assertEquals(expected.size(), actual.getIpAddresses().size());
		expected.add(new IpAddress());
		actual.setIpAddresses(expected);
		assertEquals(expected.size(), actual.getIpAddresses().size());
	}

	/**
	 * Test getPosters.
	 */
	@Test
	public void testGetPosters() {
		List<UsenetUser> expected = new ArrayList<>();
		Location actual = new Location();
		assertEquals(expected.size(), actual.getPosters().size());
		expected.add(new UsenetUser());
		actual.setPosters(expected);
		assertEquals(expected.size(), actual.getPosters().size());
	}

	/**
	 * Test isGuessed.
	 */
	@Test
	public void testIsGuessed() {
		Location actual = new Location();
		assertFalse(actual.isGuessed());
		actual.setGuessed(true);
		assertTrue(actual.isGuessed());
	}

	/**
	 * Test hashCode
	 */
	@Test
	public void testHashCode() {
		TestLocation actual1 = new TestLocation();
		TestLocation actual2 = new TestLocation();
		actual1.setIdSuper(5);
		actual2.setIdSuper(59);
		assertTrue(actual1.hashCode() != actual2.hashCode());
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
	void setIdSuper(int id) {
		super.setId(id);
	}
}
