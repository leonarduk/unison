package uk.co.sleonard.unison.datahandling.DAO;




public class DownloadRequest {
	private String usenetID;
	private DownloadMode mode;

	public enum DownloadMode {
		BASIC,HEADERS, ALL
	}
	public DownloadRequest(String usenetID, DownloadMode mode) {
		this.mode = mode;
		this.usenetID = usenetID;
	}
	public String getUsenetID() {
		return usenetID;
	}
	public DownloadMode getMode() {
		return mode;
	}
}