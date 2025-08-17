/**
 * LocationFinderImplTest
 *
 * @author ${author}
 * @since 29-May-2016
 */
package uk.co.sleonard.unison.input;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.sleonard.unison.datahandling.DAO.Location;

import java.net.URL;

public class LocationFinderImplTest {

    private LocationFinderImpl finder;

    @BeforeEach
    public void setUp() throws Exception {
        final URL resource = this.getClass().getClassLoader().getResource("locationFinder/");
        this.finder = new LocationFinderImpl(resource.toString());
    }

    @Test
    public final void testCreateLocation() {
        final Location actual = this.finder.createLocation("213.205.194.135");
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("United Kingdom", actual.getCountry());
        Assertions.assertEquals("London", actual.getCity());
        Assertions.assertEquals("GB", actual.getCountryCode());

        Location actual2 = this.finder.createLocation("wrongipaddress");
        Assertions.assertNull(actual2.getCountry());
        Assertions.assertNull(actual2.getCity());
        Assertions.assertNull(actual2.getCountryCode());
    }
}
