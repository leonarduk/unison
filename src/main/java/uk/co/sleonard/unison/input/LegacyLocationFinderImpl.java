package uk.co.sleonard.unison.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Vector;

import uk.co.sleonard.unison.datahandling.DAO.IpAddress;
import uk.co.sleonard.unison.datahandling.DAO.Location;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;

public class LegacyLocationFinderImpl implements LocationFinder {

	/**
	 * Creates the location.
	 *
	 * @param ipAddress
	 *            the ip address
	 * @return the location
	 */
	@Override
	public Location createLocation(final String ipAddress) {

		Location location = null;
		final String locationLookupUrl = "http://api.hostip.info/rough.php?ip=" + ipAddress;

		final HashMap<String, String> fieldMap = new HashMap<>();

		try {
			// Create a URL for the desired page
			final URL url = new URL(locationLookupUrl);

			// Read all the text returned by the server
			try (final BufferedReader in = new BufferedReader(
			        new InputStreamReader(url.openStream()));) {
				String str;
				// Example output:
				// Country: UNITED STATES
				// Country Code: US
				// City: Beltsville, MD <-- this becomes location field
				// Guessed: true
				while ((str = in.readLine()) != null) {
					// str is one line of text; readLine() strips the newline
					// character(s)
					final String[] fields = str.split(": ");
					fieldMap.put(fields[0], fields[1]);
				}
			}
			final String city = fieldMap.get("City");
			final String country = fieldMap.get("Country");
			final String countryCode = fieldMap.get("Country Code");
			final boolean guessed = Boolean.getBoolean(fieldMap.get("Guessed"));

			final Vector<UsenetUser> posters = null;// new Vector<UsenetUser>();
			final Vector<IpAddress> ips = null;// new Vector<IpAddress>();

			location = new Location(city, country, countryCode, guessed, posters, ips);

		}
		catch (final MalformedURLException e) {
			e.printStackTrace();
		}
		catch (final IOException e) {
			e.printStackTrace();
		}
		return location;
	}

}
