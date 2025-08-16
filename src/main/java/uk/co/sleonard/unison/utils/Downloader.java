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
    /**
     * Queue a request to download a Usenet message.
     *
     * @param usenetID the unique message identifier to fetch
     * @param mode determines how much of the message to retrieve; one of
     *             {@link DownloadMode#BASIC}, {@link DownloadMode#HEADERS} or {@link DownloadMode#ALL}
     * @throws UNISoNException if the request cannot be queued or the mode is unsupported
     */
    void addDownloadRequest(final String usenetID, final DownloadMode mode) throws UNISoNException;

}
