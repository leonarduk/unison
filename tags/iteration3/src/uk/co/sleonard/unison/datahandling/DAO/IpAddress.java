package uk.co.sleonard.unison.datahandling.DAO;
// Generated 13-Oct-2007 22:29:18 by Hibernate Tools 3.2.0.b9



/**
 * 			The  IP address
 * 		
 */
public class IpAddress  implements java.io.Serializable {


     private int id;
     private String IpAddress;
     private Location location;

    public IpAddress() {
    }

	
    public IpAddress(Location location) {
        this.location = location;
    }
    public IpAddress(String IpAddress, Location location) {
       this.IpAddress = IpAddress;
       this.location = location;
    }
   
    public int getId() {
        return this.id;
    }
    
    protected void setId(int id) {
        this.id = id;
    }
    public String getIpAddress() {
        return this.IpAddress;
    }
    
    public void setIpAddress(String IpAddress) {
        this.IpAddress = IpAddress;
    }
    public Location getLocation() {
        return this.location;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }




}


