/**
 * UsenetUser
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.datahandling.DAO;

/**
 * Represents a poster to a news group.
 *
 * @author Hibernate Tools 3.2.0.b9
 * @since Generated 11-Nov-2007 17:31:30
 *
 */
public class UsenetUser implements java.io.Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 6240031352036083751L;

	/** The email. */
	private String email;

	/** The gender. */
	private String gender;

	/** The id. */
	private int id;

	/** The ipaddress. */
	private String ipaddress;

	/** The location. */
	private Location location;

	/** The name. */
	private String name;

	/**
	 * Instantiates a new usenet user.
	 */
	public UsenetUser() {
	}

	/**
	 * Instantiates a new usenet user.
	 *
	 * @param name
	 *            the name
	 * @param email
	 *            the email
	 * @param ipaddress
	 *            the ipaddress
	 * @param gender
	 *            the gender
	 * @param location
	 *            the location
	 */
	public UsenetUser(final String name, final String email, final String ipaddress,
	        final String gender, final Location location) {
		this.name = name;
		this.email = email;
		this.ipaddress = ipaddress;
		this.gender = gender;
		this.location = location;
	}

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
		final UsenetUser other = (UsenetUser) obj;
		if (this.email == null) {
			if (other.email != null) {
				return false;
			}
		}
		else if (!this.email.equals(other.email)) {
			return false;
		}
		if (this.gender == null) {
			if (other.gender != null) {
				return false;
			}
		}
		else if (!this.gender.equals(other.gender)) {
			return false;
		}
		if (this.id != other.id) {
			return false;
		}
		if (this.ipaddress == null) {
			if (other.ipaddress != null) {
				return false;
			}
		}
		else if (!this.ipaddress.equals(other.ipaddress)) {
			return false;
		}
		if (this.location == null) {
			if (other.location != null) {
				return false;
			}
		}
		else if (!this.location.equals(other.location)) {
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
	 * Gets the gender.
	 *
	 * @return the gender
	 */
	public String getGender() {
		return this.gender;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Gets the ipaddress.
	 *
	 * @return the ipaddress
	 */
	public String getIpaddress() {
		return this.ipaddress;
	}

	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public Location getLocation() {
		return this.location;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.email == null) ? 0 : this.email.hashCode());
		result = (prime * result) + ((this.gender == null) ? 0 : this.gender.hashCode());
		result = (prime * result) + this.id;
		result = (prime * result) + ((this.ipaddress == null) ? 0 : this.ipaddress.hashCode());
		result = (prime * result) + ((this.location == null) ? 0 : this.location.hashCode());
		result = (prime * result) + ((this.name == null) ? 0 : this.name.hashCode());
		return result;
	}

	/**
	 * Sets the email.
	 *
	 * @param email
	 *            the new email
	 */
	public void setEmail(final String email) {
		this.email = email;
	}

	/**
	 * Sets the gender.
	 *
	 * @param gender
	 *            the new gender
	 */
	public void setGender(final String gender) {
		this.gender = gender;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the new id
	 */
	protected void setId(final int id) {
		this.id = id;
	}

	/**
	 * Sets the ipaddress.
	 *
	 * @param ipaddress
	 *            the new ipaddress
	 */
	public void setIpaddress(final String ipaddress) {
		this.ipaddress = ipaddress;
	}

	/**
	 * Sets the location.
	 *
	 * @param location
	 *            the new location
	 */
	public void setLocation(final Location location) {
		this.location = location;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getName() + "(" + this.getEmail() + ")";
	}

}
