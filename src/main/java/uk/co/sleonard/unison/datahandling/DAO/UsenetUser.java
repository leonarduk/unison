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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
@Entity
@Table(name = "USENETUSER")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedQuery(name = "uk.co.sleonard.unison.datahandling.DAO.UsenetUser.findByKey",
        query = "from uk.co.sleonard.unison.datahandling.DAO.UsenetUser as g where g.email = :key")
public class UsenetUser implements java.io.Serializable {

    private static final long serialVersionUID = 6240031352036083751L;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "ipaddress", nullable = false)
    private String ipaddress;

    @ManyToOne
    @JoinColumn(name = "LOCATION_ID")
    private Location location;

    @Column(name = "gender")
    private String gender;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USENETUSER_ID")
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
