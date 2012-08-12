package uk.co.sleonard.unison.datahandling.DAO;
// Generated 13-Oct-2007 22:29:18 by Hibernate Tools 3.2.0.b9


import java.util.ArrayList;
import java.util.List;

/**
 * 			The real world location of an IP address
 * 		
 */
public class Location  implements java.io.Serializable {


     private int id;
     private String City;
     private String Country;
     private String CountryCode;
     private boolean Guessed;
     private List posters = new ArrayList(0);
     private List IpAddresses = new ArrayList(0);

    public Location() {
    }

    public Location(String City, String Country, String CountryCode, boolean Guessed, List posters, List IpAddresses) {
       this.City = City;
       this.Country = Country;
       this.CountryCode = CountryCode;
       this.Guessed = Guessed;
       this.posters = posters;
       this.IpAddresses = IpAddresses;
    }
   
    public int getId() {
        return this.id;
    }
    
    protected void setId(int id) {
        this.id = id;
    }
    public String getCity() {
        return this.City;
    }
    
    public void setCity(String City) {
        this.City = City;
    }
    public String getCountry() {
        return this.Country;
    }
    
    public void setCountry(String Country) {
        this.Country = Country;
    }
    public String getCountryCode() {
        return this.CountryCode;
    }
    
    public void setCountryCode(String CountryCode) {
        this.CountryCode = CountryCode;
    }
    public boolean isGuessed() {
        return this.Guessed;
    }
    
    public void setGuessed(boolean Guessed) {
        this.Guessed = Guessed;
    }
    public List getPosters() {
        return this.posters;
    }
    
    public void setPosters(List posters) {
        this.posters = posters;
    }
    public List getIpAddresses() {
        return this.IpAddresses;
    }
    
    public void setIpAddresses(List IpAddresses) {
        this.IpAddresses = IpAddresses;
    }




}


