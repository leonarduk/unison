package uk.co.sleonard.unison.datahandling.DAO;

// Generated 11-Nov-2007 17:31:30 by Hibernate Tools 3.2.0.b9

/**
 * The IP address
 * 
 */
public class IpAddress implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -298847784176005488L;
	private int id;
	private String IpAddress;
	private Location location;

	public IpAddress() {
	}

	public IpAddress(final Location location) {
		this.location = location;
	}

	public IpAddress(final String IpAddress, final Location location) {
		this.IpAddress = IpAddress;
		this.location = location;
	}

	public int getId() {
		return this.id;
	}

	public String getIpAddress() {
		return this.IpAddress;
	}

	public Location getLocation() {
		return this.location;
	}

	protected void setId(final int id) {
		this.id = id;
	}

	public void setIpAddress(final String IpAddress) {
		this.IpAddress = IpAddress;
	}

	public void setLocation(final Location location) {
		this.location = location;
	}

}
