package uk.co.sleonard.unison.input;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import uk.co.sleonard.unison.datahandling.DAO.IpAddress;
import uk.co.sleonard.unison.datahandling.DAO.Location;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;

public class LocationFinderImpl implements LocationFinder {

	@Override
	public Location createLocation(final String ipAddress) {
		try {

			/**
			 * {"ip":"213.205.194.135","country_code":"GB","country_name":"United Kingdom"
			 * ,"region_code":"ENG","region_name":"England","city":"London","zip_code":"EC4N",
			 * "time_zone":"Europe/London","latitude":51.5144,"longitude":-0.0941,"metro_code":0}
			 */

			final String sURL = "http://freegeoip.net/json/" + ipAddress; // just a string

			// Connect to the URL using java's native library
			final URL url = new URL(sURL);
			final HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.connect();

			// Convert to a JSON object to print data
			final JsonParser jp = new JsonParser(); // from gson
			final JsonElement root = jp
			        .parse(new InputStreamReader((InputStream) request.getContent())); // Convert
			// the
			// input
			// stream
			// to
			// a
			// json
			// element
			final JsonObject rootobj = root.getAsJsonObject(); // May be an array, may be an object.

			final String city = rootobj.get("city").getAsString();
			final String country = rootobj.get("country_name").getAsString();
			final String countryCode = rootobj.get("country_code").getAsString();
			final boolean guessed = false;

			final Vector<UsenetUser> posters = null;// new Vector<UsenetUser>();
			final Vector<IpAddress> ips = null;// new Vector<IpAddress>();

			return new Location(city, country, countryCode, guessed, posters, ips);

		}
		catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (final IOException e) {
			e.printStackTrace();
		}
		return new Location();

	}
}