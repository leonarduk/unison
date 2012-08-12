package uk.co.sleonard.unison.datahandling.DAO;
// Generated 13-Oct-2007 22:29:18 by Hibernate Tools 3.2.0.b9



/**
 * 			Represents a poster to a news group
 * 		
 */
public class UsenetUser  implements java.io.Serializable {


     private int id;
     private String name;
     private String email;
     private String ipaddress;
     private String gender;
     private Location location;

    public UsenetUser() {
    }

	
    public UsenetUser(String name, String email, String ipaddress) {
        this.name = name;
        this.email = email;
        this.ipaddress = ipaddress;
    }
    public UsenetUser(String name, String email, String ipaddress, String gender, Location location) {
       this.name = name;
       this.email = email;
       this.ipaddress = ipaddress;
       this.gender = gender;
       this.location = location;
    }
   
    public int getId() {
        return this.id;
    }
    
    protected void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    public String getIpaddress() {
        return this.ipaddress;
    }
    
    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }
    public String getGender() {
        return this.gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    public Location getLocation() {
        return this.location;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }




}


