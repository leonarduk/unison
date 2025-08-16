package uk.co.sleonard.unison.datahandling.DAO;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The Class EmailAddress.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 */
@Data
@AllArgsConstructor
public class EmailAddress {
    private final String name;
    private final String email;
    private final String ipAddress;
}
