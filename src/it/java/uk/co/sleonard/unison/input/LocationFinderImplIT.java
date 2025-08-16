/**
 * LocationFinderImplIT
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.input;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import uk.co.sleonard.unison.datahandling.DAO.Location;

public class LocationFinderImplIT {

	@Ignore  // times out in Travis build
	@Test
	public void testCreateLocation() {
		final LocationFinder locationFinder = new LocationFinderImpl();
		final Location actual = locationFinder.createLocation("213.205.194.135");
		Assert.assertEquals("United Kingdom", actual.getCountry());
		Assert.assertEquals("London", actual.getCity());
		Assert.assertEquals("GB", actual.getCountryCode());
	}

}
