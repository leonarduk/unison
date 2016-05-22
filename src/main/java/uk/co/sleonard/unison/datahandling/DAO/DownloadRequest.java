package uk.co.sleonard.unison.datahandling.DAO;

/**
 * The Class DownloadRequest.
 * 
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
public class DownloadRequest {

	/**
	 * The Enum DownloadMode.
	 */
	public enum DownloadMode {

		/** The basic. */
		BASIC,
		/** The headers. */
		HEADERS,
		/** The all. */
		ALL
	}

	/** The usenet id. */
	private final String		usenetID;

	/** The mode. */
	private final DownloadMode	mode;

	/**
	 * Instantiates a new download request.
	 *
	 * @param usenetID
	 *            the usenet id
	 * @param mode
	 *            the mode
	 */
	public DownloadRequest(final String usenetID, final DownloadMode mode) {
		this.mode = mode;
		this.usenetID = usenetID;
	}

	/**
	 * Gets the mode.
	 *
	 * @return the mode
	 */
	public DownloadMode getMode() {
		return this.mode;
	}

	/**
	 * Gets the usenet id.
	 *
	 * @return the usenet id
	 */
	public String getUsenetID() {
		return this.usenetID;
	}
}
