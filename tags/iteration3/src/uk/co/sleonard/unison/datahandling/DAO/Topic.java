package uk.co.sleonard.unison.datahandling.DAO;

// Generated 13-Oct-2007 22:29:18 by Hibernate Tools 3.2.0.b9

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a message thread
 * 
 */
public class Topic implements java.io.Serializable {

	private int id;

	private String subject;

	private Set newsgroups = new HashSet(0);

	public Topic() {
	}

	public Topic(String subject) {
		this.subject = subject;
	}

	public Topic(String subject, Set newsgroups) {
		this.subject = subject;
		this.newsgroups = newsgroups;
	}

	public int getId() {
		return this.id;
	}

	protected void setId(int id) {
		this.id = id;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Set getNewsgroups() {
		return this.newsgroups;
	}

	public void setNewsgroups(Set newsgroups) {
		this.newsgroups = newsgroups;
	}

}
