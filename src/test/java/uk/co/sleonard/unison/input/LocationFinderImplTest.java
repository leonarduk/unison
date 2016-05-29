/**
 * LocationFinderImplTest
 *
 * @author ${author}
 * @since 29-May-2016
 */
package uk.co.sleonard.unison.input;

import java.net.URL;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.co.sleonard.unison.datahandling.DAO.Location;

public class LocationFinderImplTest {

	private LocationFinderImpl finder;

	@Before
	public void setUp() throws Exception {
		final URL resource = this.getClass().getClassLoader().getResource("locationFinder/");
		this.finder = new LocationFinderImpl(resource.toString());
	}

	@Test
	public final void testCreateLocation() {
		final Location actual = this.finder.createLocation("213.205.194.135");
		Assert.assertNotNull(actual);
		Assert.assertEquals("United Kingdom", actual.getCountry());
		Assert.assertEquals("London", actual.getCity());
		Assert.assertEquals("GB", actual.getCountryCode());
	}

}
