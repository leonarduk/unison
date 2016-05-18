package uk.co.sleonard.unison.datahandling.DAO;

// Generated 11-Nov-2007 17:31:30 by Hibernate Tools 3.2.0.b9

import java.util.ArrayList;
import java.util.List;

/**
 * The real world location of an IP address.
 */
public class Location implements java.io.Serializable {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hashCode = 0;
		hashCode = 29 * hashCode + id;

		return hashCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Location)) {
			return false;
		}
		final Location that = (Location) object;
		if (this.getId() != that.getId() || !this.getCity().equals(that.getCity())
		        || !this.getCountry().equals(that.getCountry())
		        || !this.getCountryCode().equals(this.getCountryCode())) {
			return false;
		}
		if (!this.getIpAddresses().containsAll(that.getIpAddresses())
		        || !that.getIpAddresses().containsAll(this.getIpAddresses())) {
			return false;
		}
		if (!this.getPosters().containsAll(that.getPosters())
		        || !that.getPosters().containsAll(this.getPosters())) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getCity() + " - " + getCountryCode();
	}

	/**
	 * Full string.
	 *
	 * @return the string
	 */
	public String fullString() {
		return getCity() + ", " + getCountry() + " (" + getCountryCode() + ")";
	}

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
	private List<IpAddress> IpAddresses = new ArrayList<IpAddress>(0);

	/** The posters. */
	private List<UsenetUser> posters = new ArrayList<UsenetUser>(0);

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

}
