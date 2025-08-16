/**
 * LocationFinder
 *
 * @author Stephen <github@leonarduk.com>
 * @since 22-May-2016
 */
package uk.co.sleonard.unison.input;

import uk.co.sleonard.unison.datahandling.DAO.Location;

/**
 * The Interface LocationFinder.
 */
public interface LocationFinder {

    /**
     * Creates the location.
     *
     * @param ipAddress the ip address
     * @return the location
     */
    Location createLocation(String ipAddress);

}
