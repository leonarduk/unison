package uk.co.sleonard.unison.datahandling.DAO;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The Class DownloadRequest.
 * 
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
@Data
@AllArgsConstructor
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

	private final String		usenetID;
	private final DownloadMode	mode;


}
