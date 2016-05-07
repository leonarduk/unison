package uk.co.sleonard.unison.datahandling.DAO;

// Generated 11-Nov-2007 17:31:30 by Hibernate Tools 3.2.0.b9

/**
 * The IP address
 * 
 */
public class IpAddress implements java.io.Serializable {
	/**
	 * Need to implement this to avoid NonUniqueObjectException //TODO
	 * http://forum.springframework.org/showthread.php?t=22261
	 */
	@Override
	public int hashCode() {
		int hashCode = 0;
		hashCode = 29 * hashCode + id;

		return hashCode;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof IpAddress)) {
			return false;
		}
		final IpAddress that = (IpAddress) object;
		if (this.getId() != that.getId()
				|| !this.getIpAddress().equals(that.getIpAddress())
				|| !this.getLocation().equals(that.getLocation())) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 */
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
