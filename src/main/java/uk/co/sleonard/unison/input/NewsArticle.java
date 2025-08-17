/**
 * NewsArticle
 *
 * @author Stephen <github@leonarduk.com>
 * @since 22-May-2016
 */
package uk.co.sleonard.unison.input;

// Import Commons/NET clases

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.utils.StringUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * The Class NewsArticle.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsArticle {

    /**
     * The article id.
     */
    private String articleID;

    /**
     * The article number.
     */
    private int articleNumber;

    /**
     * The content.
     */
    // yyyyMMdd HHmmss 'GMT'
    private String content;

    /**
     * The date.
     */
    private Date date;

    /**
     * The from.
     */
    private String from;

    /**
     * The newsgroups.
     */
    private String newsgroups;

    /**
     * The posting host.
     */
    private String postingHost;

    /**
     * The references.
     */
    private String references;

    /**
     * The subject.
     */
    private String subject;


    NewsArticle(final String articleId2, final int articleNumber2, final Date date2,
                final String from2, final String subject2, final String references2,
                final String newsgroup) throws UNISoNException {
        this(articleId2, articleNumber2, date2, from2, subject2, references2, null, newsgroup,
                null);
    }

    /**
     * Instantiates a new news article.
     *
     * @param articleID     the article id
     * @param articleNumber the article number
     * @param date          the date
     * @param from          the from
     * @param subject       the subject
     * @param references    the references
     * @param content       the content
     * @param newsgroups    the newsgroups
     * @param postingHost   the posting host
     * @throws UNISoNException the UNI so n exception
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


    /**
     * Gets the newsgroups list.
     *
     * @return the newsgroups list
     */
    public List<String> getNewsgroupsList() {
        return StringUtils.convertStringToList(this.getNewsgroups(), ",");
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
     * @param articleId the new article id
     */
    public void setArticleId(final String articleId) {
        this.articleID = articleId;
        if (null == articleId) {
            throw new NullPointerException("Article ID cannot be null");
        }
    }


    /**
     * Sets the date.
     *
     * @param date the new date
     * @throws UNISoNException the UNI so n exception
     */
    private void setDate(final Date date) throws UNISoNException {
        this.date = date;
        if (null == date) {
            throw new UNISoNException("Cannot create article without date field");
        }
    }

    /**
     * Sets the date.
     *
     * @param date the new date
     * @throws ParseException  the parse exception
     * @throws UNISoNException the UNI so n exception
     */
    public void setDate(final String date) throws ParseException, UNISoNException {
        this.date = StringUtils.stringToDate(date);
        if (null == date) {
            throw new UNISoNException("Cannot create article without date field");
        }

    }

    /**
     * Sets the from.
     *
     * @param from the new from
     */
    public void setFrom(final String from) {
        this.from = from;
        if (null == from) {
            throw new NullPointerException("Cannot create article without from field");
        }
    }

    @Override
    public String toString() {
        final StringBuffer buf = new StringBuffer();

        buf.append("ID: " + this.getArticleID());
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
