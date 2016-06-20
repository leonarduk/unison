/**
 * Topic
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.datahandling.DAO;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a message thread.
 *
 * @author Hibernate Tools 3.2.0.b9
 * @since Generated 11-Nov-2007 17:31:30
 *
 */
public class Topic implements java.io.Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4646650675535168051L;

	/** The id. */
	private int id;

	/** The newsgroups. */
	private Set<NewsGroup> newsgroups = new HashSet<>(0);

	/** The subject. */
	private String subject;

	/**
	 * Instantiates a new topic.
	 */
	public Topic() {
	}

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

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final Topic other = (Topic) obj;
		if (this.id != other.id) {
			return false;
		}
		if (this.newsgroups == null) {
			if (other.newsgroups != null) {
				return false;
			}
		}
		else if (!this.newsgroups.equals(other.newsgroups)) {
			return false;
		}
		if (this.subject == null) {
			if (other.subject != null) {
				return false;
			}
		}
		else if (!this.subject.equals(other.subject)) {
			return false;
		}
		return true;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Gets the newsgroups.
	 *
	 * @return the newsgroups
	 */
	public Set<NewsGroup> getNewsgroups() {
		return this.newsgroups;
	}

	/**
	 * Gets the subject.
	 *
	 * @return the subject
	 */
	public String getSubject() {
		return this.subject;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + this.id;
		result = (prime * result) + ((this.newsgroups == null) ? 0 : this.newsgroups.hashCode());
		result = (prime * result) + ((this.subject == null) ? 0 : this.subject.hashCode());
		return result;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the new id
	 */
	protected void setId(final int id) {
		this.id = id;
	}

	/**
	 * Sets the newsgroups.
	 *
	 * @param newsgroups
	 *            the new newsgroups
	 */
	public void setNewsgroups(final Set<NewsGroup> newsgroups) {
		this.newsgroups = newsgroups;
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
		return this.getSubject();
	}

}
