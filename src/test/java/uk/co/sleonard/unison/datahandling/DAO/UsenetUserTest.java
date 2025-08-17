/**
 * UsenetUserTest
 *
 * @author ${author}
 * @since 20-Jun-2016
 */
package uk.co.sleonard.unison.datahandling.DAO;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Extends UsenetUser to change setId(protected)
 *
 * @author Elton <elton_12_nunes@hotmail.com>
 */
@SuppressWarnings("serial")
class TestUsenetUser extends UsenetUser {
}

/**
 * The Class IpAddressTest.
 *
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.2.0
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
        Assertions.assertEquals(expected, actual.getName());
        Assertions.assertEquals(expected2, actual.getEmail());
        Assertions.assertEquals(expected3, actual.getIpaddress());
        Assertions.assertEquals(expected4, actual.getGender());
        Assertions.assertEquals(expected5, actual.getLocation());
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void testCopyConstructor() {
        final Location location = new Location();
        final UsenetUser original = new UsenetUser("John", "john@java.com", "127.0.0.1", "Programming", location);
        original.setId(123);

        final UsenetUser copy = new UsenetUser(original);
        Assertions.assertEquals(original.getName(), copy.getName());
        Assertions.assertEquals(original.getEmail(), copy.getEmail());
        Assertions.assertEquals(original.getIpaddress(), copy.getIpaddress());
        Assertions.assertEquals(original.getGender(), copy.getGender());
        Assertions.assertEquals(original.getLocation(), copy.getLocation());
        Assertions.assertEquals(original.getId(), copy.getId());
    }

    /**
     * Test getEmail.
     */
    @Test
    public void testGetEmail() {
        final String expected = "user@email.com";
        final UsenetUser actual = new UsenetUser();
        Assertions.assertNull(actual.getEmail());
        actual.setEmail(expected);
        Assertions.assertEquals(expected, actual.getEmail());
    }

    /**
     * Test getGender.
     */
    @Test
    public void testGetGender() {
        final String expected = "gender";
        final UsenetUser actual = new UsenetUser();
        Assertions.assertNull(actual.getGender());
        actual.setGender(expected);
        Assertions.assertEquals(expected, actual.getGender());
    }

    /**
     * Test getId.
     */
    @Test
    public void testGetId() {
        Assertions.assertEquals(0, new UsenetUser().getId());
    }

    /**
     * Test getIpAddress.
     */
    @Test
    public void testGetIpaddress() {
        final String expected = "127.0.0.1";
        final UsenetUser actual = new UsenetUser();
        Assertions.assertNull(actual.getIpaddress());
        actual.setIpaddress(expected);
        Assertions.assertEquals(expected, actual.getIpaddress());
    }

    /**
     * Test getLocation.
     */
    @Test
    public void testGetLocation() {
        final Location expected = new Location(null, null, "BR", false, null, null);
        final UsenetUser actual = new UsenetUser();
        Assertions.assertNull(actual.getLocation());
        actual.setLocation(expected);
        Assertions.assertEquals(expected.getCountryCode(), actual.getLocation().getCountryCode());
    }

    /**
     * Test getName.
     */
    @Test
    public void testGetName() {
        final String expected = "name";
        final UsenetUser actual = new UsenetUser();
        Assertions.assertNull(actual.getName());
        actual.setName(expected);
        Assertions.assertEquals(expected, actual.getName());
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
        Assertions.assertTrue(actual1.hashCode() != actual2.hashCode());
    }

    /**
     * Test toString.
     */
    @Test
    public void testToString() {
        final String expected = "elton(elton_12_nunes@hotmail.com)";
        final UsenetUser actual = new UsenetUser("elton", "elton_12_nunes@hotmail.com", null,
                expected, null);
        Assertions.assertEquals(expected, actual.toString());
    }

}
