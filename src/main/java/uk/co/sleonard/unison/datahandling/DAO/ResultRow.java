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
}