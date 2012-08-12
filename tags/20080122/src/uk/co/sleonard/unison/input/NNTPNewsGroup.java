package uk.co.sleonard.unison.input;

import org.apache.commons.net.nntp.NewsgroupInfo;

public final class NNTPNewsGroup implements Comparable<NNTPNewsGroup> {
	/** * A constant indicating that a newsgroup is moderated. ** */
	public static final int MODERATED_POSTING_PERMISSION = 1;

	/** * A constant indicating that a newsgroup is public and unmoderated. ** */
	public static final int PERMITTED_POSTING_PERMISSION = 2;

	/***************************************************************************
	 * A constant indicating that a newsgroup is closed for general posting.
	 **************************************************************************/
	public static final int PROHIBITED_POSTING_PERMISSION = 3;

	/***************************************************************************
	 * A constant indicating that the posting permission of a newsgroup is
	 * unknown. For example, the NNTP GROUP command does not return posting
	 * information, so NewsgroupInfo instances obtained from that command
	 * willhave an UNKNOWN_POSTING_PERMISSION.
	 **************************************************************************/
	public static final int UNKNOWN_POSTING_PERMISSION = 0;

	@Override
	public String toString() {
		return getNewsgroup() + " (" + getArticleCount() + ")";
	}

	private int __estimatedArticleCount;

	private int __firstArticle, __lastArticle;

	private String __newsgroup;

	private int __postingPermission;

	public NNTPNewsGroup(final NewsgroupInfo original) {
		this.__estimatedArticleCount = original.getArticleCount();
		this.__firstArticle = original.getFirstArticle();
		this.__lastArticle = original.getLastArticle();
		this.__newsgroup = original.getNewsgroup();
		this.__postingPermission = original.getPostingPermission();
	}

	void _setArticleCount(final int count) {
		this.__estimatedArticleCount = count;
	}

	void _setFirstArticle(final int first) {
		this.__firstArticle = first;
	}

	void _setLastArticle(final int last) {
		this.__lastArticle = last;
	}

	void _setNewsgroup(final String newsgroup) {
		this.__newsgroup = newsgroup;
	}

	void _setPostingPermission(final int permission) {
		this.__postingPermission = permission;
	}

	public int compareTo(final NNTPNewsGroup that) {
		return this.getNewsgroup().compareTo(that.getNewsgroup());
	}

	/***************************************************************************
	 * Get the estimated number of articles in the newsgroup. The accuracy of
	 * this value will depend on the server implementation.
	 * <p>
	 * 
	 * @return The estimated number of articles in the newsgroup.
	 **************************************************************************/
	public int getArticleCount() {
		int total = __lastArticle - __firstArticle;
		if (total < this.__estimatedArticleCount)
			return total;
		return this.__estimatedArticleCount;
	}

	/***************************************************************************
	 * Get the number of the first article in the newsgroup.
	 * <p>
	 * 
	 * @return The number of the first article in the newsgroup.
	 **************************************************************************/
	public int getFirstArticle() {
		return this.__firstArticle;
	}

	/***************************************************************************
	 * Get the number of the last article in the newsgroup.
	 * <p>
	 * 
	 * @return The number of the last article in the newsgroup.
	 **************************************************************************/
	public int getLastArticle() {
		return this.__lastArticle;
	}

	/***************************************************************************
	 * Get the newsgroup name.
	 * <p>
	 * 
	 * @return The name of the newsgroup.
	 **************************************************************************/
	public String getNewsgroup() {
		return this.__newsgroup;
	}

	/***************************************************************************
	 * Get the posting permission of the newsgroup. This will be one of the
	 * <code> POSTING_PERMISSION </code> constants.
	 * <p>
	 * 
	 * @return The posting permission status of the newsgroup.
	 **************************************************************************/
	public int getPostingPermission() {
		return this.__postingPermission;
	}

	/*
	 * public String toString() { StringBuffer buffer = new StringBuffer();
	 * buffer.append(__newsgroup); buffer.append(' ');
	 * buffer.append(__lastArticle); buffer.append(' ');
	 * buffer.append(__firstArticle); buffer.append(' ');
	 * switch(__postingPermission) { case 1: buffer.append('m'); break; case 2:
	 * buffer.append('y'); break; case 3: buffer.append('n'); break; } return
	 * buffer.toString(); }
	 */
}
