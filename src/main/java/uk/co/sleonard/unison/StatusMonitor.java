/**
 * StatusMonitor
 *
 * @author ${author}
 * @since 22-Jun-2016
 */
package uk.co.sleonard.unison;

import java.util.Set;

import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;

public interface StatusMonitor {
	void downloadEnabled(final boolean enabled);

	public void updateAvailableGroups(final Set<NewsGroup> availableGroups);

}
