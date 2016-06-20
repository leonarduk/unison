/**
 * GUIItem
 *
 * @author ${author}
 * @since 20-Jun-2016
 */
package uk.co.sleonard.unison.datahandling.DAO;

import org.apache.log4j.Logger;

/**
 * The Class GUIItem.
 *
 * @param <T>
 *            the generic type
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 */
public class GUIItem<T> {

	/** The Constant logger. */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger("GUIItem");

	/** The name. */
	private final String name;

	/** The object. */
	private final T object;

	/**
	 * Instantiates a new GUI item.
	 *
	 * @param name
	 *            the name
	 * @param data
	 *            the data
	 */
	public GUIItem(final String name, final T data) {
		this.name = name;
		this.object = data;
	}

	/**
	 * Gets the item.
	 *
	 * @return the item
	 */
	public T getItem() {
		return this.object;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.name;
	}
}
