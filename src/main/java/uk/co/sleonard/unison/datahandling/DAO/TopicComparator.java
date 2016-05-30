package uk.co.sleonard.unison.datahandling.DAO;

import java.util.Comparator;

/**
 * The Class TopicComparator.
 */
public class TopicComparator implements Comparator<Topic> {

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final Topic first, final Topic second) {
		return first.getSubject().compareTo(second.getSubject());
	}

}