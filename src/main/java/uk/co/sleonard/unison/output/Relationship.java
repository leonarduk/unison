/**
 * Relationship
 *
 * @author Stephen <github@leonarduk.com>
 * @since 22-May-2016
 */
package uk.co.sleonard.unison.output;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The Class Relationship.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 */
@Getter
@ToString
@EqualsAndHashCode
public class Relationship {

    @Getter
    private static final boolean directed = true;

    private final int owner;

    private final int target;

    private int value = 1;

    public Relationship(final int owner, final int target) {
        this.owner = owner;
        this.target = target;
    }

    /**
     * Increment value.
     *
     * @return the int
     */
    int incrementValue() {
        return ++this.value;
    }
}
