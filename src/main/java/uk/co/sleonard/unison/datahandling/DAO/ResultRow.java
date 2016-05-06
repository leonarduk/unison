package uk.co.sleonard.unison.datahandling.DAO;

public class ResultRow implements Comparable<ResultRow> {
	private Object key;
	private Class<?> type;
	private int count;

	/**
	 * 
	 * @param displayText
	 * @param key
	 * @param type
	 */
	public ResultRow(Object key, int count, Class<?> type) {
		this.key = key;
		this.type = type;
		this.count = count;
	}

	@Override
	public String toString() {
		return getCount() + " " + getKey();
	}

	public Object getKey() {
		return key;
	}

	public Class<?> getType() {
		return type;
	}

	public int getCount() {
		return count;
	}

	@Override
	public int compareTo(ResultRow that) {
		// reverse order to get top ones first
		return -(this.count - that.count);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + count;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResultRow other = (ResultRow) obj;
		if (count != other.count)
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
	
}