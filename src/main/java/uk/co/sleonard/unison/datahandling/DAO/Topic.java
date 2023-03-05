/**
 * Topic
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.datahandling.DAO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a message thread.
 *
 * @author Hibernate Tools 3.2.0.b9
 * @since Generated 11-Nov-2007 17:31:30
 *
 */
@Data
@NoArgsConstructor
public class Topic implements java.io.Serializable {
	private static final long serialVersionUID = -4646650675535168051L;
	private int id;
	private Set<NewsGroup> newsgroups = new HashSet<>(0);
	private String subject;

	/**
	 * Instantiates a new topic.
	 *
	 * @param subject
	 *            the subject
	 * @param newsgroups
	 *            the newsgroups
	 */
	public Topic(final String subject, final Set<NewsGroup> newsgroups) {
		this.subject = subject;
		this.newsgroups = newsgroups;
	}

	public Topic(final Topic topic) {
		this(topic.subject, topic.newsgroups);
		this.id = topic.id;
	}

	@Override
	public String toString() {
		return this.getSubject();
	}

}
