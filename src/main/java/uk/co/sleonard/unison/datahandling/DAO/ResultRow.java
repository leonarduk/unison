/**
 * ResultRow
 * 
 * @author ${author}
 * @since 20-Jun-2016
 */
package uk.co.sleonard.unison.datahandling.DAO;

/**
 * The Class ResultRow.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
public class ResultRow implements Comparable<ResultRow> {

	/** The key. */
	private final Object key;

	/** The type. */
	private final Class<?> type;

	/** The count. */
	private final int count;

	/**
	 * Instantiates a new result row.
	 *
	 * @param key
	 *            the key
	 * @param count
	 *            the count
	 * @param type
	 *            the type
	 */
	public ResultRow(final Object key, final int count, final Class<?> type) {
		this.key = key;
		this.type = type;
		this.count = count;
	}

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

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final ResultRow other = (ResultRow) obj;
		if (this.count != other.count) {
			return false;
		}
		if (this.key == null) {
			if (other.key != null) {
				return false;
			}
		}
		else if (!this.key.equals(other.key)) {
			return false;
		}
		return true;
	}

	/**
	 * Gets the count.
	 *
	 * @return the count
	 */
	public int getCount() {
		return this.count;
	}

	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	public Object getKey() {
		return this.key;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public Class<?> getType() {
		return this.type;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + this.count;
		result = (prime * result) + (this.key == null ? 0 : this.key.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getCount() + " " + this.getKey();
	}

}
