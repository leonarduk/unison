package uk.co.sleonard.unison.datahandling.DAO;

import java.util.Comparator;

/**
 * The Class NewsGroupComparator.
 */
class NewsGroupComparator implements Comparator<NewsGroup> {

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final NewsGroup first, final NewsGroup second) {
		return first.getName().compareTo(second.getName());
	}

}