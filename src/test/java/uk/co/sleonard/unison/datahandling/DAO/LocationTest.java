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
import org.mockito.Mockito;

/**
 * The Class LocationTest.
 *
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.2.0
 */
public class LocationTest {

  /** The city. */
  private static final String city = "Boston";

  /** The country. */
  private static final String country = "United States";

  /** The country code. */
  private static final String countryCode = "USA";

  @Test
  public void testEquals() throws Exception {
    final boolean guessed = true;
    final List<UsenetUser> posters = new ArrayList<>();
    final List<IpAddress> ipAddresses = new ArrayList<>();
    final Location l1 =
        new Location(
            LocationTest.city,
            LocationTest.country,
            LocationTest.countryCode,
            guessed,
            posters,
            ipAddresses);
    Location l2 =
        new Location(
            LocationTest.city,
            LocationTest.country,
            LocationTest.countryCode,
            !guessed,
            posters,
            ipAddresses);
    Assert.assertTrue(l1.equals(l1));
    Assert.assertEquals(l1, l1);
    Assert.assertFalse(l1.equals(l2));
    Assert.assertNotEquals(l1, l2);
    l2 =
        new Location(
            LocationTest.city + "Wrong",
            LocationTest.country,
            LocationTest.countryCode,
            guessed,
            posters,
            ipAddresses);
    Assert.assertNotEquals(l1, l2);
    l2 =
        new Location(
            LocationTest.city,
            LocationTest.country + "Wrong",
            LocationTest.countryCode,
            guessed,
            posters,
            ipAddresses);
    Assert.assertNotEquals(l1, l2);
    l2 =
        new Location(
            LocationTest.city,
            LocationTest.country,
            LocationTest.countryCode + "Wrong",
            guessed,
            posters,
            ipAddresses);
    Assert.assertNotEquals(l1, l2);
    final List<UsenetUser> posters2 = new ArrayList<>();
    posters2.add(Mockito.mock(UsenetUser.class));
    l2 =
        new Location(
            LocationTest.city,
            LocationTest.country,
            LocationTest.countryCode,
            guessed,
            posters2,
            ipAddresses);
    Assert.assertNotEquals(l1, l2);
    final List<IpAddress> ipAdddresses2 = new ArrayList<>();
    ipAdddresses2.add(Mockito.mock(IpAddress.class));
    l2 =
        new Location(
            LocationTest.city,
            LocationTest.country,
            LocationTest.countryCode,
            guessed,
            posters,
            ipAdddresses2);
    Assert.assertNotEquals(l1, l2);
  }

  /** Test fullString. */
  @Test
  public void testFullString() {
    final String expected =
        LocationTest.city + ", " + LocationTest.country + " (" + LocationTest.countryCode + ")";
    final Location actual =
        new Location(
            LocationTest.city, LocationTest.country, LocationTest.countryCode, true, null, null);
    Assert.assertEquals(expected, actual.fullString());
  }

  /** Test getCity. */
  @Test
  public void testGetCity() {
    final String expected = LocationTest.city;
    final Location actual = new Location();
    Assert.assertNull(actual.getCity());
    actual.setCity(expected);
    Assert.assertEquals(expected, actual.getCity());
  }

  /** Test getCountry. */
  @Test
  public void testGetCountry() {
    final String expected = LocationTest.country;
    final Location actual = new Location();
    Assert.assertNull(actual.getCountry());
    actual.setCountry(expected);
    Assert.assertEquals(expected, actual.getCountry());
  }

  /** Test getCountryCode. */
  @Test
  public void testGetCountryCode() {
    final String expected = LocationTest.countryCode;
    final Location actual = new Location();
    Assert.assertNull(actual.getCountryCode());
    actual.setCountryCode(expected);
    Assert.assertEquals(expected, actual.getCountryCode());
  }

  /** Test getId. */
  @Test
  public void testGetId() {
    Assert.assertEquals(0, new Location().getId());
  }

  /** Test getIpAddresses. */
  @Test
  public void testGetIpAddresses() {
    final List<IpAddress> expected = new ArrayList<>();
    final Location actual = new Location();
    Assert.assertEquals(expected.size(), actual.getIpAddresses().size());
    expected.add(new IpAddress());
    actual.setIpAddresses(expected);
    Assert.assertEquals(expected.size(), actual.getIpAddresses().size());
  }

  /** Test getPosters. */
  @Test
  public void testGetPosters() {
    final List<UsenetUser> expected = new ArrayList<>();
    final Location actual = new Location();
    Assert.assertEquals(expected.size(), actual.getPosters().size());
    expected.add(new UsenetUser());
    actual.setPosters(expected);
    Assert.assertEquals(expected.size(), actual.getPosters().size());
  }

  /** Test hashCode */
  @Test
  public void testHashCode() {
    final TestLocation actual1 = new TestLocation();
    final TestLocation actual2 = new TestLocation();
    actual1.setCity("testa");
    actual2.setCity("test3");
    Assert.assertTrue(actual1.hashCode() != actual2.hashCode());
  }

  /** Test isGuessed. */
  @Test
  public void testIsGuessed() {
    final Location actual = new Location();
    Assert.assertFalse(actual.isGuessed());
    actual.setGuessed(true);
    Assert.assertTrue(actual.isGuessed());
  }

  /** Test toString. */
  @Test
  public void testToString() {
    final String expected = LocationTest.city + " - " + LocationTest.countryCode;
    final Location actual =
        new Location(LocationTest.city, null, LocationTest.countryCode, true, null, null);
    Assert.assertEquals(expected, actual.toString());
  }
}

/**
 * Extends Location to change setId(protected)
 *
 * @author Elton <elton_12_nunes@hotmail.com>
 */
@SuppressWarnings("serial")
class TestLocation extends Location {
  @Override
  public void setCity(final String City) {
    super.setCity(City);
  }
}
