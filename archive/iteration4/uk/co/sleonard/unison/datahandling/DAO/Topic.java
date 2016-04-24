package uk.co.sleonard.unison.datahandling.DAO;

// Generated 11-Nov-2007 17:31:30 by Hibernate Tools 3.2.0.b9

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a message thread
 * 
 */
public class Topic implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5658289999765458770L;
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
