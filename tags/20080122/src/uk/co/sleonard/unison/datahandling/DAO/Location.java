package uk.co.sleonard.unison.datahandling.DAO;

// Generated 11-Nov-2007 17:31:30 by Hibernate Tools 3.2.0.b9

import java.util.ArrayList;
import java.util.List;

/**
 * The real world location of an IP address
 * 
 */
public class Location implements java.io.Serializable {
	public int hashCode() {
		int hashCode = 0;
		hashCode = 29 * hashCode + id;

		return hashCode;
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Location)) {
			return false;
		}
		final Location that = (Location) object;
		if (this.getId() != that.getId()
				|| !this.getCity().equals(that.getCity())
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

	@Override
	public String toString() {
		return getCity() + " - " + getCountryCode();
	}

	public String fullString() {
		return getCity() + ", " + getCountry() + " (" + getCountryCode() + ")";
	}

	/**
	 * 
	 */

	private String City;

	private String Country;

	private String CountryCode;

	private boolean Guessed;

	private int id;

	private List<IpAddress> IpAddresses = new ArrayList<IpAddress>(0);

	private List<UsenetUser> posters = new ArrayList<UsenetUser>(0);

	public Location() {
	}

	public Location(final String City, final String Country,
			final String CountryCode, final boolean Guessed,
			final List<UsenetUser> posters, final List<IpAddress> IpAddresses) {
		this.City = City;
		this.Country = Country;
		this.CountryCode = CountryCode;
		this.Guessed = Guessed;
		this.posters = posters;
		this.IpAddresses = IpAddresses;
	}

	public String getCity() {
		return this.City;
	}

	public String getCountry() {
		return this.Country;
	}

	public String getCountryCode() {
		return this.CountryCode;
	}

	public int getId() {
		return this.id;
	}

	public List<IpAddress> getIpAddresses() {
		return this.IpAddresses;
	}

	public List<UsenetUser> getPosters() {
		return this.posters;
	}

	public boolean isGuessed() {
		return this.Guessed;
	}

	public void setCity(final String City) {
		this.City = City;
	}

	public void setCountry(final String Country) {
		this.Country = Country;
	}

	public void setCountryCode(final String CountryCode) {
		this.CountryCode = CountryCode;
	}

	public void setGuessed(final boolean Guessed) {
		this.Guessed = Guessed;
	}

	protected void setId(final int id) {
		this.id = id;
	}

	public void setIpAddresses(final List<IpAddress> IpAddresses) {
		this.IpAddresses = IpAddresses;
	}

	public void setPosters(final List<UsenetUser> posters) {
		this.posters = posters;
	}

}
