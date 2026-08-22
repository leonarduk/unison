/**
 * LocationFinderImpl
 *
 * @author ${author}
 * @since 29-May-2016
 */
package uk.co.sleonard.unison.input;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import uk.co.sleonard.unison.datahandling.DAO.IpAddress;
import uk.co.sleonard.unison.datahandling.DAO.Location;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLConnection;
import java.util.Vector;
import java.util.regex.Pattern;

/**
 * The Class LocationFinderImpl.
 */
@Slf4j
public class LocationFinderImpl implements LocationFinder {

    /**
     * The web url.
     */
    private final String webUrl;
    private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    /**
     * Instantiates a new location finder impl.
     */
    public LocationFinderImpl() {
        this("https://ip-api.com/json/");
    }

    /**
     * Instantiates a new location finder impl.
     *
     * @param url the url
     */
    LocationFinderImpl(final String url) {
        this.webUrl = url;
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.co.sleonard.unison.input.LocationFinder#createLocation(java.lang.String)
     */
    @Override
    public Location createLocation(final String ipAddress) {
        try {

            // Assure that IP Address

            Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
            if (!pattern.matcher(ipAddress).matches()) {
                return new Location();
            }

            /**
             * {"status":"success","country":"United Kingdom","countryCode":"GB",
             * "region":"ENG","regionName":"England","city":"London","zip":"EC4N",
             * "lat":51.5144,"lon":-0.0941,"timezone":"Europe/London","query":"213.205.194.135"}
             */

            final String sURL = this.webUrl + ipAddress; // just a string

            // Connect to the URL using java's native library
            final URLConnection request = URI.create(sURL).toURL().openConnection();
            request.connect();

            // Convert to a JSON object to print data
            final JsonElement root = JsonParser
                    .parseReader(new InputStreamReader((InputStream) request.getContent())); // Convert
            // the
            // input
            // stream
            // to
            // a
            // json
            // element
            final JsonObject rootobj = root.getAsJsonObject(); // May be an array, may be an object.

            final String city = rootobj.get("city").getAsString();
            final String country = rootobj.get("country").getAsString();
            final String countryCode = rootobj.get("countryCode").getAsString();
            final boolean guessed = false;

            final Vector<UsenetUser> posters = null;// new Vector<UsenetUser>();
            final Vector<IpAddress> ips = null;// new Vector<IpAddress>();

            return new Location(city, country, countryCode, guessed, posters, ips);

        } catch (final IOException e) {
            log.warn("Failed to look up location for IP address {}: {}", ipAddress, e.getMessage());
        }
        return new Location();

    }
}
