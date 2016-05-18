package uk.co.sleonard.unison.datahandling.DAO;

/**
 * The Class DownloadRequest.
 */
public class DownloadRequest {

	/** The usenet id. */
	private String usenetID;

	/** The mode. */
	private DownloadMode mode;

	/**
	 * The Enum DownloadMode.
	 */
	public enum DownloadMode {

		/** The basic. */
		BASIC, /** The headers. */
		HEADERS, /** The all. */
		ALL
	}

	/**
	 * Instantiates a new download request.
	 *
	 * @param usenetID
	 *            the usenet id
	 * @param mode
	 *            the mode
	 */
	public DownloadRequest(String usenetID, DownloadMode mode) {
		this.mode = mode;
		this.usenetID = usenetID;
	}

	/**
	 * Gets the usenet id.
	 *
	 * @return the usenet id
	 */
	public String getUsenetID() {
		return usenetID;
	}

	/**
	 * Gets the mode.
	 *
	 * @return the mode
	 */
	public DownloadMode getMode() {
		return mode;
	}
}
