/**
 * Downloader
 *
 * @author ${author}
 * @since 16-Jun-2016
 */
package uk.co.sleonard.unison.utils;

import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;

public interface Downloader {
    void addDownloadRequest(final String usenetID, final DownloadMode mode) throws UNISoNException;

}
