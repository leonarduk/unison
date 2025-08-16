/**
 * ResultRow
 *
 * @author ${author}
 * @since 20-Jun-2016
 */
package uk.co.sleonard.unison.datahandling.DAO;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The Class ResultRow.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
@Data
@AllArgsConstructor
public class ResultRow implements Comparable<ResultRow> {
    private final Object key;
    private final int count;
    private final Class<?> type;

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final ResultRow that) {
        // reverse order to get top ones first
        return -(this.count - that.count);
    }

    @Override
    public String toString() {
        return this.getCount() + " " + this.getKey();
    }
}
