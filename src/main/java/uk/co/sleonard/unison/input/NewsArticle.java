package uk.co.sleonard.unison.input;

// Import Commons/NET clases
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.net.nntp.Article;
import org.apache.log4j.Logger;

import uk.co.sleonard.unison.gui.UNISoNException;
import uk.co.sleonard.unison.utils.HttpDateObject;
import uk.co.sleonard.unison.utils.StringUtils;

/**
 * The Class NewsArticle.
 *
 * @author Stephen <github@leonarduk.com>
 * @since
 *
 */
public class NewsArticle implements Comparable<Object> {

	/** The logger. */
	private static Logger logger = Logger.getLogger("NewsArtiole");

	/** The article id. */
	private String articleID;

	/** The article number. */
	private int articleNumber;

	/** The content. */
	// yyyyMMdd HHmmss 'GMT'
	private String content;

	/** The date. */
	private Date date;

	/** The from. */
	private String from;

	/** The newsgroups. */
	private String newsgroups;

	/** The posting host. */
	private String postingHost;

	/** The references. */
	private String references;

	/** The subject. */
	private String subject;

	/**
	 * Instantiates a new news article.
	 */
	public NewsArticle() {
	}

	/**
	 * Instantiates a new news article.
	 *
	 * @param article
	 *            the article
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	@SuppressWarnings("deprecation")
	public NewsArticle(final Article article) throws UNISoNException {
		this.setArticleId(article.getArticleId());
		this.setArticleNumber(article.getArticleNumber());
		try {
			this.setDate(article.getDate());
		}
		catch (final Exception e) {
			NewsArticle.logger.warn("Failed to parse date: " + this.date);
			throw new UNISoNException(e);
		}

		this.setFrom(article.getFrom());
		this.setSubject(article.getSubject());
	}

	/**
	 * Instantiates a new news article.
	 *
	 * @param articleID
	 *            the article id
	 * @param articleNumber
	 *            the article number
	 * @param date
	 *            the date
	 * @param from
	 *            the from
	 * @param subject
	 *            the subject
	 * @param references
	 *            the references
	 * @param content
	 *            the content
	 * @param newsgroups
	 *            the newsgroups
	 * @param postingHost
	 *            the posting host
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	public NewsArticle(final String articleID, final int articleNumber, final Date date,
	        final String from, final String subject, final String references, final String content,
	        final String newsgroups, final String postingHost) throws UNISoNException {
		this.setArticleId(articleID);
		this.setArticleNumber(articleNumber);
		this.setDate(date);
		this.setFrom(from);
		this.setSubject(subject);
		this.setReferences(references);
		this.setContent(content);
		this.setNewsgroups(newsgroups);
		this.setPostingHost(postingHost);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final Object o) {
		final int myNumber = this.getArticleNumber();
		final int otherNumber = ((NewsArticle) o).getArticleNumber();
		if (myNumber < otherNumber) {
			return -1;
		}
		else if (myNumber > otherNumber) {
			return 1;
		}
		else {
			return 0;
		}
	}

	/**
	 * Gets the article id.
	 *
	 * @return the article id
	 */
	public String getArticleId() {
		return this.articleID;
	}

	/**
	 * Gets the article number.
	 *
	 * @return the article number
	 */
	int getArticleNumber() {
		return this.articleNumber;
	}

	/**
	 * Gets the content.
	 *
	 * @return the content
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * Gets the from.
	 *
	 * @return the from
	 */
	public String getFrom() {
		return this.from;
	}

	/**
	 * Gets the newsgroups.
	 *
	 * @return the newsgroups
	 */
	private String getNewsgroups() {
		return this.newsgroups;
	}

	/**
	 * Gets the newsgroups list.
	 *
	 * @return the newsgroups list
	 */
	public List<String> getNewsgroupsList() {
		return StringUtils.convertStringToList(this.getNewsgroups(), ",");

		// final StringTokenizer fields = new StringTokenizer(
		// this.getNewsgroups(), " ");
		// final Vector<String> crosspostGroups = new Vector<String>();
		//
		// while (fields.hasMoreElements()) {
		// String nextToken = fields.nextToken();
		//
		// // in case group is listed with its message number
		// // e.g. de.soc.senioren:56345
		// if (nextToken.contains(":")) {
		// nextToken = nextToken.substring(0, nextToken.indexOf(":"));
		// }
		//
		// crosspostGroups.add(nextToken);
		// // System.out.println("Adding " + nextToken);
		//
		// }
		// return crosspostGroups;
	}

	/**
	 * Gets the posting host.
	 *
	 * @return the posting host
	 */
	public String getPostingHost() {
		return this.postingHost;
	}

	/**
	 * Gets the references.
	 *
	 * @return the references
	 */
	public String getReferences() {
		return this.references;
	}

	/**
	 * Gets the references list.
	 *
	 * @return the references list
	 */
	public List<String> getReferencesList() {
		return StringUtils.convertStringToList(this.getReferences(), " ");
	}

	/**
	 * Gets the subject.
	 *
	 * @return the subject
	 */
	public String getSubject() {
		return this.subject;
	}

	/**
	 * If downloaded using XOVER, then crosspost details, posting host (ussed for location) and
	 * content will be empty.
	 *
	 * @return true, if is full header
	 */
	public boolean isFullHeader() {
		return (null != this.postingHost);
	}

	/**
	 * Sets the article id.
	 *
	 * @param articleId
	 *            the new article id
	 */
	public void setArticleId(final String articleId) {
		this.articleID = articleId;
		if (null == articleId) {
			throw new NullPointerException("Article ID cannot be null");
		}
	}

	/**
	 * Sets the article number.
	 *
	 * @param articleNumber
	 *            the new article number
	 */
	public void setArticleNumber(final int articleNumber) {
		this.articleNumber = articleNumber;
	}

	/**
	 * Sets the content.
	 *
	 * @param content
	 *            the new content
	 */
	public void setContent(final String content) {
		this.content = content;
	}

	/**
	 * Sets the date.
	 *
	 * @param date
	 *            the new date
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	void setDate(final Date date) throws UNISoNException {
		this.date = date;
		if (null == date) {
			throw new UNISoNException("Cannot create article without date field");
		}
	}

	/**
	 * Sets the date.
	 *
	 * @param date
	 *            the new date
	 * @throws ParseException
	 *             the parse exception
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	public void setDate(final String date) throws ParseException, UNISoNException {
		this.date = HttpDateObject.getParser().parse(date);
		if (null == date) {
			throw new UNISoNException("Cannot create article without date field");
		}

	}

	/**
	 * Sets the from.
	 *
	 * @param from
	 *            the new from
	 */
	public void setFrom(final String from) {
		this.from = from;
		if (null == from) {
			throw new NullPointerException("Cannot create article without from field");
		}
	}

	/**
	 * Sets the newsgroups.
	 *
	 * @param newsgroups
	 *            the new newsgroups
	 */
	private void setNewsgroups(final String newsgroups) {
		this.newsgroups = newsgroups;
	}

	/**
	 * Sets the posting host.
	 *
	 * @param postingHost
	 *            the new posting host
	 */
	public void setPostingHost(final String postingHost) {
		this.postingHost = postingHost;
	}

	/**
	 * Sets the references.
	 *
	 * @param references
	 *            the new references
	 */
	public void setReferences(final String references) {
		this.references = references;
	}

	/**
	 * Sets the subject.
	 *
	 * @param subject
	 *            the new subject
	 */
	public void setSubject(final String subject) {
		this.subject = subject;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();

		buf.append("ID: " + this.getArticleId());
		buf.append("\nPostingHost: " + this.getPostingHost());
		buf.append("\nNumber: " + this.getArticleNumber());
		buf.append("\nFrom: " + this.getFrom());
		buf.append("\nSent: " + this.getDate());
		buf.append("\nSubject: " + this.getSubject());
		buf.append("\nCrossposts: " + this.getNewsgroupsList());
		buf.append("\nReferences: " + this.getReferencesList());

		if (null != this.getContent()) {
			buf.append(this.getContent());
		}
		return buf.toString();
	}

}
