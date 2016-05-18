package uk.co.sleonard.unison.datahandling.DAO;

/**
 * The Class EmailAddress.
 */
public class EmailAddress {

	/** The name. */
	private final String name;

	/** The email. */
	private final String email;

	/** The ip address. */
	private final String ipAddress;

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
	public EmailAddress(final String name, final String email, final String ip) {
		this.name = name;
		this.email = email;
		this.ipAddress = ip;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final EmailAddress other = (EmailAddress) obj;
		if (this.email == null) {
			if (other.email != null) {
				return false;
			}
		}
		else if (!this.email.equals(other.email)) {
			return false;
		}
		if (this.ipAddress == null) {
			if (other.ipAddress != null) {
				return false;
			}
		}
		else if (!this.ipAddress.equals(other.ipAddress)) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		}
		else if (!this.name.equals(other.name)) {
			return false;
		}
		return true;
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Gets the ip address.
	 *
	 * @return the ip address
	 */
	public String getIpAddress() {
		return this.ipAddress;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
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
		result = prime * result + (this.email == null ? 0 : this.email.hashCode());
		result = prime * result + (this.ipAddress == null ? 0 : this.ipAddress.hashCode());
		result = prime * result + (this.name == null ? 0 : this.name.hashCode());
		return result;
	}
}
