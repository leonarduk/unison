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
	 */
	public Topic(final String subject) {
		this.subject = subject;
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

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Topic)) {
			return false;
		}
		final Topic that = (Topic) object;
		if ((this.getId() != that.getId()) || !this.getSubject().equals(that.getSubject())) {
			return false;
		}
		return (this.getNewsgroups().containsAll(that.getNewsgroups())
		        && that.getNewsgroups().containsAll(this.getNewsgroups()));
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

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hashCode = 0;
		hashCode = (29 * hashCode) + this.id;

		return hashCode;
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
