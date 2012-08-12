package uk.co.sleonard.unison.datahandling.DAO;

public class ResultRow {
	private String key;
	private Class<?> type;
	private int count;

	/**
	 * 
	 * @param displayText
	 * @param key
	 * @param type
	 */
	public ResultRow(String key, int count, Class<?> type) {
		this.key = key;
		this.type = type;
		this.count = count;
	}

	@Override
	public String toString() {
		return getCount() + " " + getKey();
	}

	public String getKey() {
		return key;
	}

	public Class<?> getType() {
		return type;
	}

	public int getCount() {
		return count;
	}
}