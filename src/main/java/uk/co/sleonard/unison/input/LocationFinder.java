package uk.co.sleonard.unison.input;

import uk.co.sleonard.unison.datahandling.DAO.Location;

public interface LocationFinder {

	Location createLocation(String ipAddress);

}
