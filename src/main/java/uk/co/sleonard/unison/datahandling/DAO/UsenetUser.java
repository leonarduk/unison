package uk.co.sleonard.unison.datahandling.DAO;

// Generated 11-Nov-2007 17:31:30 by Hibernate Tools 3.2.0.b9

/**
 * Represents a poster to a news group
 * 
 */
public class UsenetUser implements java.io.Serializable {
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof UsenetUser)) {
			return false;
		}
		final UsenetUser that = (UsenetUser) object;
		if (this.getId() != that.getId()
				|| !this.getEmail().equals(that.getEmail())
				|| !this.getName().equals(that.getName())) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hashCode = 0;
		hashCode = 29 * hashCode + id;

		return hashCode;
	}

	/**
	 * 
	 */
	private String email;
	private String gender;
	private int id;
	private String ipaddress;
	private Location location;
	private String name;

	@Override
	public String toString() {
		return getName() + "(" + getEmail() + ")";
	}

	public UsenetUser() {
	}

	public UsenetUser(final String name, final String email,
			final String ipaddress) {
		this.name = name;
		this.email = email;
		this.ipaddress = ipaddress;
	}

	public UsenetUser(final String name, final String email,
			final String ipaddress, final String gender, final Location location) {
		this.name = name;
		this.email = email;
		this.ipaddress = ipaddress;
		this.gender = gender;
		this.location = location;
	}

	public String getEmail() {
		return this.email;
	}

	public String getGender() {
		return this.gender;
	}

	public int getId() {
		return this.id;
	}

	public String getIpaddress() {
		return this.ipaddress;
	}

	public Location getLocation() {
		return this.location;
	}

	public String getName() {
		return this.name;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setGender(final String gender) {
		this.gender = gender;
	}

	protected void setId(final int id) {
		this.id = id;
	}

	public void setIpaddress(final String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public void setLocation(final Location location) {
		this.location = location;
	}

	public void setName(final String name) {
		this.name = name;
	}

}
