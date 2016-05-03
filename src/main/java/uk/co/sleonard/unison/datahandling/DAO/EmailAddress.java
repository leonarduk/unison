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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((ipAddress == null) ? 0 : ipAddress.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmailAddress other = (EmailAddress) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (ipAddress == null) {
			if (other.ipAddress != null)
				return false;
		} else if (!ipAddress.equals(other.ipAddress))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
