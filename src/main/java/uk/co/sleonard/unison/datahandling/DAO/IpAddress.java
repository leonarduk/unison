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
	 * @param IpAddress
	 *            the ip address
	 * @param location
	 *            the location
	 */
	public IpAddress(final String IpAddress, final Location location) {
		this.IpAddress = IpAddress;
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
		final IpAddress other = (IpAddress) obj;
		if (this.IpAddress == null) {
			if (other.IpAddress != null) {
				return false;
			}
		}
		else if (!this.IpAddress.equals(other.IpAddress)) {
			return false;
		}
		if (this.id != other.id) {
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
		return true;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.IpAddress == null) ? 0 : this.IpAddress.hashCode());
		result = (prime * result) + this.id;
		result = (prime * result) + ((this.location == null) ? 0 : this.location.hashCode());
		return result;
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
