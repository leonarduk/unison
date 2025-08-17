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
  /**
   * Called when the ability to download articles changes.
   *
   * @param enabled {@code true} if downloading is currently permitted; {@code false} otherwise
   */
  void downloadEnabled(final boolean enabled);

  /**
   * Informs the monitor that the set of downloadable groups has been refreshed.
   *
   * @param availableGroups the groups that are now available for download
   */
  void updateAvailableGroups(final Set<NewsGroup> availableGroups);
}
