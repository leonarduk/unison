/**
 * IpAddress
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.datahandling.DAO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The IP address.
 *
 * @author Hibernate Tools 3.2.0.b9
 * @since Generated 11-Nov-2007 17:31:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IpAddress implements java.io.Serializable {

  private static final long serialVersionUID = -7969874241047620708L;
  private String IpAddress;
  private Location location;
  private int id;

  public IpAddress(final String IpAddress, final Location location) {
    this.IpAddress = IpAddress;
    this.location = location;
  }
}
