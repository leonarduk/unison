package uk.co.sleonard.unison.datahandling.DAO;

// Generated 11-Nov-2007 17:31:30 by Hibernate Tools 3.2.0.b9

/**
 * Represents a poster to a news group
 * 
 */
public class UsenetUser implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8032895354711821215L;
	private String email;
	private String gender;
	private int id;
	private String ipaddress;
	private Location location;
	private String name;

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
