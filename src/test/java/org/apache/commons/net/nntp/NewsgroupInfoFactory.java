/**
 * NewsgroupInfoFactory
 *
 * @author Stephen Leonard
 *
 *         Created this so we can create test NewsgroupInfo objects
 * @since 17-Jun-2016
 */
package org.apache.commons.net.nntp;

/**
 * A factory for creating NewsgroupInfo objects.
 */
public class NewsgroupInfoFactory {

	/**
	 * Newsgroup info.
	 *
	 * @param count
	 *            the count
	 * @param first
	 *            the first
	 * @param last
	 *            the last
	 * @param newsgroup
	 *            the newsgroup
	 * @return the newsgroup info
	 */
	public static NewsgroupInfo newsgroupInfo(final long count, final long first, final long last,
	        final String newsgroup) {
		return NewsgroupInfoFactory.newsgroupInfo(count, first, last, newsgroup, 0);
	}

	/**
	 * Newsgroup info.
	 *
	 * @param count
	 *            the count
	 * @param first
	 *            the first
	 * @param last
	 *            the last
	 * @param newsgroup
	 *            the newsgroup
	 * @param permission
	 *            the permission
	 * @return the newsgroup info
	 */
	public static NewsgroupInfo newsgroupInfo(final long count, final long first, final long last,
	        final String newsgroup, final int permission) {
		final NewsgroupInfo newsgroupInfo = new NewsgroupInfo();
		newsgroupInfo._setArticleCount(count);
		newsgroupInfo._setFirstArticle(first);
		newsgroupInfo._setLastArticle(last);
		newsgroupInfo._setNewsgroup(newsgroup);
		newsgroupInfo._setPostingPermission(permission);
		return newsgroupInfo;
	}
}
