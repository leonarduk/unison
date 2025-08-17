package uk.co.sleonard.unison.datahandling.DAO;

/**
 * The Class ResultRow.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
public record ResultRow(Object key, int count, Class<?> type) implements Comparable<ResultRow> {
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
        return this.count + " " + this.key;
    }
}
