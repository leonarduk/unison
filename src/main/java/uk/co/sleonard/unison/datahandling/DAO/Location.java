/**
 * Location
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.datahandling.DAO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * The real world location of an IP address.
 *
 * @author Hibernate Tools 3.2.0.b9
 * @since Generated 11-Nov-2007 17:31:30
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location implements java.io.Serializable {

	private static final long serialVersionUID = -4937544540164453405L;
	private String City;
	private String Country;
	private String CountryCode;
	private boolean Guessed;
	private int id;
	private List<IpAddress> IpAddresses = new ArrayList<>(0);
	private List<UsenetUser> posters = new ArrayList<>(0);

	public Location(final String city, final String country, final String countryCode,
					final boolean guessed, final List<UsenetUser> posters,
					final List<IpAddress> ipAddresses) {
		this.City = city;
		this.Country = country;
		this.CountryCode = countryCode;
		this.Guessed = guessed;
		this.posters = posters;
		this.IpAddresses = ipAddresses;
	}

	/**
	 * Full string.
	 *
	 * @return the string
	 */
	public String fullString() {
		return this.getCity() + ", " + this.getCountry() + " (" + this.getCountryCode() + ")";
	}

	@Override
	public String toString() {
		return this.getCity() + " - " + this.getCountryCode();
	}

}
