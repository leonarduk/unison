package uk.co.sleonard.unison.datahandling.DAO;

/**
 * The Class ResultRow.
 */
public class ResultRow implements Comparable<ResultRow> {

	/** The key. */
	private Object key;

	/** The type. */
	private Class<?> type;

	/** The count. */
	private int count;

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
	public ResultRow(Object key, int count, Class<?> type) {
		this.key = key;
		this.type = type;
		this.count = count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getCount() + " " + getKey();
	}

	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	public Object getKey() {
		return key;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public Class<?> getType() {
		return type;
	}

	/**
	 * Gets the count.
	 *
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ResultRow that) {
		// reverse order to get top ones first
		return -(this.count - that.count);
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
		result = prime * result + count;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ResultRow other = (ResultRow) obj;
		if (count != other.count) return false;
		if (key == null) {
			if (other.key != null) return false;
		}
		else if (!key.equals(other.key)) return false;
		return true;
	}

}
