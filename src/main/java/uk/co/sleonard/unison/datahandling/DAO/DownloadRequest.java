package uk.co.sleonard.unison.datahandling.DAO;

/**
 * The Class DownloadRequest.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 */
public record DownloadRequest(String usenetID, DownloadMode mode) {

    /**
     * The Enum DownloadMode.
     */
    public enum DownloadMode {
        /**
         * The basic.
         */
        BASIC,
        /**
         * The headers.
         */
        HEADERS,
        /**
         * The all.
         */
        ALL
    }
}
