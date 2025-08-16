/**
 * StatusMonitor
 *
 * @author ${author}
 * @since 22-Jun-2016
 */
package uk.co.sleonard.unison;

import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;

import java.util.Set;

public interface StatusMonitor {
    void downloadEnabled(final boolean enabled);

    public void updateAvailableGroups(final Set<NewsGroup> availableGroups);

}
