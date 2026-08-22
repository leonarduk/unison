/**
 * LocationFinderImplIT
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.input;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import uk.co.sleonard.unison.datahandling.DAO.Location;

@Disabled("Requires a live network call to the geolocation service and is disabled to avoid external network calls")
public class LocationFinderImplIT {

    @Test
    public void testCreateLocation() {
        final LocationFinder locationFinder = new LocationFinderImpl();
        final Location actual = locationFinder.createLocation("213.205.194.135");
        Assertions.assertEquals("United Kingdom", actual.getCountry());
        Assertions.assertEquals("London", actual.getCity());
        Assertions.assertEquals("GB", actual.getCountryCode());
    }

}
