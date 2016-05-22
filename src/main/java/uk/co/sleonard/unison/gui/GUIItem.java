package uk.co.sleonard.unison.gui;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;

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
	private static final Logger	logger	= Logger.getLogger("GUIItem");

	/** The name. */
	private final String		name;

	/** The object. */
	private final T				object;

	/**
	 * Gets the GUI list.
	 *
	 * @param list
	 *            the list
	 * @param helper
	 *            the helper
	 * @return the GUI list
	 */
	public static Vector<GUIItem<?>> getGUIList(final List<?> list, final HibernateHelper helper) {
		final Vector<GUIItem<?>> returnList = new Vector<>();

		for (final ListIterator<?> iter = list.listIterator(); iter.hasNext();) {
			final Object next = iter.next();
			String text = null;
			if (next instanceof NewsGroup) {
				text = ((NewsGroup) next).getFullName();
			}
			else {
				text = helper.getText(next);
			}
			returnList.add(new GUIItem<>(text, next));
		}
		return returnList;
	}

	/**
	 * Gets the GUI list.
	 *
	 * @param array
	 *            the array
	 * @param helper
	 *            the helper
	 * @return the GUI list
	 */
	public static Vector<GUIItem<?>> getGUIList(final Object[] array,
	        final HibernateHelper helper) {
		return GUIItem.getGUIList(Arrays.asList(array), helper);
	}

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
