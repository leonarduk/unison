package uk.co.sleonard.unison.datahandling.DAO;

// Generated 11-Nov-2007 17:31:30 by Hibernate Tools 3.2.0.b9

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a message thread.
 */
public class Topic implements java.io.Serializable {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Topic)) {
			return false;
		}
		final Topic that = (Topic) object;
		if (this.getId() != that.getId() || !this.getSubject().equals(that.getSubject())) {
			return false;
		}
		if (!this.getNewsgroups().containsAll(that.getNewsgroups())
		        || !that.getNewsgroups().containsAll(this.getNewsgroups())) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hashCode = 0;
		hashCode = 29 * hashCode + id;

		return hashCode;
	}

	/** The id. */
	private int id;

	/** The newsgroups. */
	private Set<NewsGroup> newsgroups = new HashSet<NewsGroup>(0);

	/** The subject. */
	private String subject;

	/**
	 * Instantiates a new topic.
	 */
	public Topic() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getSubject();
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

}
