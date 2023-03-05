/**
 * UsenetUser
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.datahandling.DAO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a poster to a news group.
 *
 * @author Hibernate Tools 3.2.0.b9
 * @since Generated 11-Nov-2007 17:31:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UsenetUser implements java.io.Serializable {

    private static final long serialVersionUID = 6240031352036083751L;

    private String name;
    private String email;
    private String ipaddress;
    private Location location;

    private String gender;

    private int id;

    public UsenetUser(final String name, final String email, final String ipaddress,
                      final String gender, final Location location) {
        this.name = name;
        this.email = email;
        this.ipaddress = ipaddress;
        this.gender = gender;
        this.location = location;
    }
    public UsenetUser(final UsenetUser poster) {
        new UsenetUserBuilder()
                .email(poster.getEmail())
                .id(poster.getId())
                .location(poster.getLocation())
                .name(poster.getName())
                .gender(poster.getGender())
                .ipaddress(poster.getIpaddress())
                .build();
    }
    @Override
    public String toString() {
        return this.getName() + "(" + this.getEmail() + ")";
    }

}
