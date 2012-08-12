package uk.co.sleonard.unison.datahandling.DAO;

// Generated 11-Nov-2007 17:31:30 by Hibernate Tools 3.2.0.b9

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a message thread
 * 
 */
public class Topic implements java.io.Serializable {
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Topic)) {
			return false;
		}
		final Topic that = (Topic) object;
		if (this.getId() != that.getId()
				|| !this.getSubject().equals(that.getSubject())) {
			return false;
		}
		if (!this.getNewsgroups().containsAll(that.getNewsgroups())
				|| !that.getNewsgroups().containsAll(this.getNewsgroups())) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int hashCode = 0;
		hashCode = 29 * hashCode + id;

		return hashCode;
	}

	/**
	 * 
	 */
	private int id;
	private Set<NewsGroup> newsgroups = new HashSet<NewsGroup>(0);
	private String subject;

	public Topic() {
	}

	@Override
	public String toString() {
		return getSubject();
	}

	public Topic(final String subject) {
		this.subject = subject;
	}

	public Topic(final String subject, final Set<NewsGroup> newsgroups) {
		this.subject = subject;
		this.newsgroups = newsgroups;
	}

	public int getId() {
		return this.id;
	}

	public Set<NewsGroup> getNewsgroups() {
		return this.newsgroups;
	}

	public String getSubject() {
		return this.subject;
	}

	protected void setId(final int id) {
		this.id = id;
	}

	public void setNewsgroups(final Set<NewsGroup> newsgroups) {
		this.newsgroups = newsgroups;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

}
