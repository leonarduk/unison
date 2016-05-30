/**
 * IpAddress
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.datahandling.DAO;

/**
 * The IP address.
 *
 * @author Hibernate Tools 3.2.0.b9
 * @since Generated 11-Nov-2007 17:31:30
 *
 */
public class IpAddress implements java.io.Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -7969874241047620708L;

	/** The id. */
	private int id;

	/** The Ip address. */
	private String IpAddress;

	/** The location. */
	private Location location;

	/**
	 * Instantiates a new ip address.
	 */
	public IpAddress() {
	}

	/**
	 * Instantiates a new ip address.
	 *
	 * @param location
	 *            the location
	 */
	public IpAddress(final Location location) {
		this.location = location;
	}

	/**
	 * Instantiates a new ip address.
	 *
	 * @param IpAddress
	 *            the ip address
	 * @param location
	 *            the location
	 */
	public IpAddress(final String IpAddress, final Location location) {
		this.IpAddress = IpAddress;
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
		if (!(object instanceof IpAddress)) {
			return false;
		}
		final IpAddress that = (IpAddress) object;
		return ((this.getId() == that.getId()) && this.getIpAddress().equals(that.getIpAddress())
		        && this.getLocation().equals(that.getLocation()));
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
	 * Gets the ip address.
	 *
	 * @return the ip address
	 */
	public String getIpAddress() {
		return this.IpAddress;
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
	 * Need to implement this to avoid NonUniqueObjectException //TODO
	 * http://forum.springframework.org/showthread.php?t=22261
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		int hashCode = 0;
		hashCode = (29 * hashCode) + this.id;

		return hashCode;
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
	 * Sets the ip address.
	 *
	 * @param IpAddress
	 *            the new ip address
	 */
	public void setIpAddress(final String IpAddress) {
		this.IpAddress = IpAddress;
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

}
