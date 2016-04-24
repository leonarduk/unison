package uk.co.sleonard.unison.gui;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;

public class GUIItem<T> {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger("GUIItem");

	public static Vector<GUIItem<?>> getGUIList(final List<?> list, HibernateHelper helper) {
		final Vector<GUIItem<?>> returnList = new Vector<GUIItem<?>>();

		for (final ListIterator<?> iter = list.listIterator(); iter.hasNext();) {
			final Object next = iter.next();
			String text = null;
			if (next instanceof NewsGroup) {
				text = ((NewsGroup) next).getFullName();
			} else {
				text = helper.getText(next);
			}
			returnList.add(new GUIItem<Object>(text, next));
		}
		return returnList;
	}

	public static Vector<GUIItem<?>> getGUIList(final Object[] array, HibernateHelper helper) {
		return GUIItem.getGUIList(Arrays.asList(array), helper);
	}

	private final String name;

	private final T object;

	public GUIItem(final String name, final T data) {
		this.name = name;
		this.object = data;
	}

	public T getItem() {
		return this.object;
	}

	@Override
	public String toString() {
		return this.name;
	}
}