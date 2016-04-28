package uk.co.sleonard.unison.datahandling.DAO;

public class EmailAddress {
	private String name;
	private String email;
	private String ipAddress;

	public EmailAddress(String name, String email, String ip) {
		this.name = name;
		this.email = email;
		this.ipAddress = ip;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getIpAddress() {
		return ipAddress;
	}
}
