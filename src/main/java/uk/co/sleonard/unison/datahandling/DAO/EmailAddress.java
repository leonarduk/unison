package uk.co.sleonard.unison.datahandling.DAO;

/**
 * The Class EmailAddress.
 */
public class EmailAddress {

	/** The name. */
	private String name;

	/** The email. */
	private String email;

	/** The ip address. */
	private String ipAddress;

	/**
	 * Instantiates a new email address.
	 *
	 * @param name
	 *            the name
	 * @param email
	 *            the email
	 * @param ip
	 *            the ip
	 */
	public EmailAddress(String name, String email, String ip) {
		this.name = name;
		this.email = email;
		this.ipAddress = ip;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Gets the ip address.
	 *
	 * @return the ip address
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		EmailAddress other = (EmailAddress) obj;
		if (email == null) {
			if (other.email != null) return false;
		}
		else if (!email.equals(other.email)) return false;
		if (ipAddress == null) {
			if (other.ipAddress != null) return false;
		}
		else if (!ipAddress.equals(other.ipAddress)) return false;
		if (name == null) {
			if (other.name != null) return false;
		}
		else if (!name.equals(other.name)) return false;
		return true;
	}
}
