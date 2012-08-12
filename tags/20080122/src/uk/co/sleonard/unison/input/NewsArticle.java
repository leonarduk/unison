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
 * 
 * @author steve
 * 
 */
public class NewsArticle implements Comparable<Object> {
	private static Logger logger = Logger.getLogger("NewsArtiole");

	private String articleID;

	private int articleNumber;

	// yyyyMMdd HHmmss 'GMT'
	private String content;

	private Date date;

	private String from;

	private String newsgroups;

	private String postingHost;

	private String references;

	private String subject;

	public NewsArticle(final Article article) throws UNISoNException {
		this.setArticleId(article.getArticleId());
		this.setArticleNumber(article.getArticleNumber());
		try {
			this.setDate(article.getDate());
		} catch (final Exception e) {
			NewsArticle.logger.warn("Failed to parse date: " + this.date);
			throw new UNISoNException(e);
		}

		this.setFrom(article.getFrom());
		this.setSubject(article.getSubject());
	}

	/**
	 * If downloaded using XOVER, then crosspost details, posting host (ussed
	 * for location) and content will be empty
	 * 
	 * @return
	 */
	public boolean isFullHeader() {
		if (null == postingHost) {
			return false;
		}
		return true;
	}

	public NewsArticle(final String articleID, final int articleNumber,
			final Date date, final String from, final String subject,
			final String references, final String content,
			final String newsgroups, final String postingHost)
			throws UNISoNException {
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

	public NewsArticle() {
		// TODO Auto-generated constructor stub
	}

	public int compareTo(final Object o) {
		final int myNumber = this.getArticleNumber();
		final int otherNumber = ((NewsArticle) o).getArticleNumber();
		if (myNumber < otherNumber) {
			return -1;
		} else if (myNumber > otherNumber) {
			return 1;
		} else {
			return 0;
		}
	}

	public String getArticleId() {
		return this.articleID;
	}

	int getArticleNumber() {
		return this.articleNumber;
	}

	public String getContent() {
		return this.content;
	}

	public Date getDate() {
		return this.date;
	}

	public String getFrom() {
		return this.from;
	}

	private String getNewsgroups() {
		return this.newsgroups;
	}

	public List<String> getNewsgroupsList() {
		return StringUtils.convertStringToList(this.getNewsgroups(), ",");

//		final StringTokenizer fields = new StringTokenizer(
//				this.getNewsgroups(), " ");
//		final Vector<String> crosspostGroups = new Vector<String>();
//
//		while (fields.hasMoreElements()) {
//			String nextToken = fields.nextToken();
//
//			// in case group is listed with its message number
//			// e.g. de.soc.senioren:56345
//			if (nextToken.contains(":")) {
//				nextToken = nextToken.substring(0, nextToken.indexOf(":"));
//			}
//
//			crosspostGroups.add(nextToken);
//			// System.out.println("Adding " + nextToken);
//
//		}
//		return crosspostGroups;
	}

	public String getPostingHost() {
		return this.postingHost;
	}

	public String getReferences() {
		return this.references;
	}

	public List<String> getReferencesList() {
		return StringUtils.convertStringToList(this.getReferences(), " ");
	}

	public String getSubject() {
		return this.subject;
	}

	public void setArticleId(final String articleId) {
		this.articleID = articleId;
		if (null == articleId) {
			throw new NullPointerException("Article ID cannot be null");
		}
	}

	public void setArticleNumber(final int articleNumber) {
		this.articleNumber = articleNumber;
	}

	public void setContent(final String content) {
		this.content = content;
	}

	void setDate(final Date date) throws UNISoNException {
		this.date = date;
		if (null == date) {
			throw new UNISoNException(
					"Cannot create article without date field");
		}
	}

	public void setDate(final String date) throws ParseException,
			UNISoNException {
		this.date = HttpDateObject.getParser().parse(date);
		if (null == date) {
			throw new UNISoNException(
					"Cannot create article without date field");
		}

	}

	public void setFrom(final String from) {
		this.from = from;
		if (null == from) {
			throw new NullPointerException(
					"Cannot create article without from field");
		}
	}

	private void setNewsgroups(final String newsgroups) {
		this.newsgroups = newsgroups;
	}

	public void setPostingHost(final String postingHost) {
		this.postingHost = postingHost;
	}

	public void setReferences(final String references) {
		this.references = references;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

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

		if (null != getContent()) {
			buf.append(getContent());
		}
		return buf.toString();
	}

}
