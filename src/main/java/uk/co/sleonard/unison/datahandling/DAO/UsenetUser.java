package uk.co.sleonard.unison.datahandling.DAO;

// Generated 11-Nov-2007 17:31:30 by Hibernate Tools 3.2.0.b9

/**
 * Represents a poster to a news group.
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
	 */
	public UsenetUser(final String name, final String email, final String ipaddress) {
		this.name = name;
		this.email = email;
		this.ipaddress = ipaddress;
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

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof UsenetUser)) {
			return false;
		}
		final UsenetUser that = (UsenetUser) object;
		if (this.getId() != that.getId() || !this.getEmail().equals(that.getEmail())
		        || !this.getName().equals(that.getName())) {
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

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hashCode = 0;
		hashCode = 29 * hashCode + this.id;

		return hashCode;
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
