/**
 * Location
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.datahandling.DAO;

import java.util.ArrayList;
import java.util.List;

/**
 * The real world location of an IP address.
 *
 * @author Hibernate Tools 3.2.0.b9
 * @since Generated 11-Nov-2007 17:31:30
 *
 */
public class Location implements java.io.Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4937544540164453405L;

	/** The City. */

	private String City;

	/** The Country. */
	private String Country;

	/** The Country code. */
	private String CountryCode;

	/** The Guessed. */
	private boolean Guessed;

	/** The id. */
	private int id;

	/** The Ip addresses. */
	private List<IpAddress> IpAddresses = new ArrayList<>(0);

	/** The posters. */
	private List<UsenetUser> posters = new ArrayList<>(0);

	/**
	 * Instantiates a new location.
	 */
	public Location() {
	}

	/**
	 * Instantiates a new location.
	 *
	 * @param City
	 *            the city
	 * @param Country
	 *            the country
	 * @param CountryCode
	 *            the country code
	 * @param Guessed
	 *            the guessed
	 * @param posters
	 *            the posters
	 * @param IpAddresses
	 *            the ip addresses
	 */
	public Location(final String City, final String Country, final String CountryCode,
	        final boolean Guessed, final List<UsenetUser> posters,
	        final List<IpAddress> IpAddresses) {
		this.City = City;
		this.Country = Country;
		this.CountryCode = CountryCode;
		this.Guessed = Guessed;
		this.posters = posters;
		this.IpAddresses = IpAddresses;
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
		final Location other = (Location) obj;
		if (this.City == null) {
			if (other.City != null) {
				return false;
			}
		}
		else if (!this.City.equals(other.City)) {
			return false;
		}
		if (this.Country == null) {
			if (other.Country != null) {
				return false;
			}
		}
		else if (!this.Country.equals(other.Country)) {
			return false;
		}
		if (this.CountryCode == null) {
			if (other.CountryCode != null) {
				return false;
			}
		}
		else if (!this.CountryCode.equals(other.CountryCode)) {
			return false;
		}
		if (this.Guessed != other.Guessed) {
			return false;
		}
		if (this.IpAddresses == null) {
			if (other.IpAddresses != null) {
				return false;
			}
		}
		else if (!this.IpAddresses.equals(other.IpAddresses)) {
			return false;
		}
		if (this.id != other.id) {
			return false;
		}
		if (this.posters == null) {
			if (other.posters != null) {
				return false;
			}
		}
		else if (!this.posters.equals(other.posters)) {
			return false;
		}
		return true;
	}

	/**
	 * Full string.
	 *
	 * @return the string
	 */
	public String fullString() {
		return this.getCity() + ", " + this.getCountry() + " (" + this.getCountryCode() + ")";
	}

	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	public String getCity() {
		return this.City;
	}

	/**
	 * Gets the country.
	 *
	 * @return the country
	 */
	public String getCountry() {
		return this.Country;
	}

	/**
	 * Gets the country code.
	 *
	 * @return the country code
	 */
	public String getCountryCode() {
		return this.CountryCode;
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
	 * Gets the ip addresses.
	 *
	 * @return the ip addresses
	 */
	public List<IpAddress> getIpAddresses() {
		return this.IpAddresses;
	}

	/**
	 * Gets the posters.
	 *
	 * @return the posters
	 */
	public List<UsenetUser> getPosters() {
		return this.posters;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.City == null) ? 0 : this.City.hashCode());
		result = (prime * result) + ((this.Country == null) ? 0 : this.Country.hashCode());
		result = (prime * result) + ((this.CountryCode == null) ? 0 : this.CountryCode.hashCode());
		result = (prime * result) + (this.Guessed ? 1231 : 1237);
		result = (prime * result) + ((this.IpAddresses == null) ? 0 : this.IpAddresses.hashCode());
		result = (prime * result) + this.id;
		result = (prime * result) + ((this.posters == null) ? 0 : this.posters.hashCode());
		return result;
	}

	/**
	 * Checks if is guessed.
	 *
	 * @return true, if is guessed
	 */
	public boolean isGuessed() {
		return this.Guessed;
	}

	/**
	 * Sets the city.
	 *
	 * @param City
	 *            the new city
	 */
	public void setCity(final String City) {
		this.City = City;
	}

	/**
	 * Sets the country.
	 *
	 * @param Country
	 *            the new country
	 */
	public void setCountry(final String Country) {
		this.Country = Country;
	}

	/**
	 * Sets the country code.
	 *
	 * @param CountryCode
	 *            the new country code
	 */
	public void setCountryCode(final String CountryCode) {
		this.CountryCode = CountryCode;
	}

	/**
	 * Sets the guessed.
	 *
	 * @param Guessed
	 *            the new guessed
	 */
	public void setGuessed(final boolean Guessed) {
		this.Guessed = Guessed;
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
	 * Sets the ip addresses.
	 *
	 * @param IpAddresses
	 *            the new ip addresses
	 */
	public void setIpAddresses(final List<IpAddress> IpAddresses) {
		this.IpAddresses = IpAddresses;
	}

	/**
	 * Sets the posters.
	 *
	 * @param posters
	 *            the new posters
	 */
	public void setPosters(final List<UsenetUser> posters) {
		this.posters = posters;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getCity() + " - " + this.getCountryCode();
	}

}
