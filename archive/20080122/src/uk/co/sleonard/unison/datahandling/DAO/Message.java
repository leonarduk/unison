package uk.co.sleonard.unison.datahandling.DAO;

// Generated 11-Nov-2007 17:31:30 by Hibernate Tools 3.2.0.b9

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a message
 */
public class Message implements java.io.Serializable {

	/**
	 * Need to implement this to avoid NonUniqueObjectException //TODO
	 * http://forum.springframework.org/showthread.php?t=22261
	 */
	public int hashCode() {
		int hashCode = 0;
		hashCode = 29 * hashCode + id;

		return hashCode;
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Message)) {
			return false;
		}
		final Message that = (Message) object;
		if (this.getId() != that.getId()
				|| (null != this.getMessageBody() && !this.getMessageBody()
						.equals(that.getMessageBody()))
				|| !this.getTopic().equals(that.getTopic())
				|| !this.getUsenetMessageID().equals(that.getUsenetMessageID())
				|| !this.getDateCreated().equals(that.getDateCreated())
				|| !this.getReferencedMessages().equals(
						that.getReferencedMessages())
				|| !this.getPoster().equals(that.getPoster())
				|| !this.getSubject().equals(this.getSubject())) {
			return false;
		}
		if (!this.getNewsgroups().containsAll(that.getNewsgroups())
				|| !that.getNewsgroups().containsAll(this.getNewsgroups())) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return getSubject() + ":" + poster;
	}

	/**
	 * 
	 */
	private Date DateCreated;
	private int id;
	private byte[] MessageBody;
	private Set<NewsGroup> newsgroups = new HashSet<NewsGroup>(0);
	private UsenetUser poster;
	private String referencedMessages;
	private String Subject;
	private Topic topic;
	private String UsenetMessageID;

	public Message() {
	}

	public Message(final Date DateCreated, final String UsenetMessageID,
			final String Subject, final UsenetUser poster, final Topic topic,
			final byte[] MessageBody) {
		this.DateCreated = DateCreated;
		this.UsenetMessageID = UsenetMessageID;
		this.Subject = Subject;
		this.poster = poster;
		this.topic = topic;
		this.MessageBody = MessageBody;
	}

	public Message(final Date DateCreated, final String UsenetMessageID,
			final String Subject, final UsenetUser poster, final Topic topic,
			final Set<NewsGroup> newsgroups, final String referencedMessages,
			final byte[] MessageBody) {
		this.DateCreated = DateCreated;
		this.UsenetMessageID = UsenetMessageID;
		this.Subject = Subject;
		this.poster = poster;
		this.topic = topic;
		this.newsgroups = newsgroups;
		this.referencedMessages = referencedMessages;
		this.MessageBody = MessageBody;
	}

	public Date getDateCreated() {
		return this.DateCreated;
	}

	public int getId() {
		return this.id;
	}

	public byte[] getMessageBody() {
		return this.MessageBody;
	}

	public Set<NewsGroup> getNewsgroups() {
		return this.newsgroups;
	}

	public UsenetUser getPoster() {
		return this.poster;
	}

	public String getReferencedMessages() {
		return this.referencedMessages;
	}

	public String getSubject() {
		return this.Subject;
	}

	public Topic getTopic() {
		return this.topic;
	}

	public String getUsenetMessageID() {
		return this.UsenetMessageID;
	}

	public void setDateCreated(final Date DateCreated) {
		this.DateCreated = DateCreated;
	}

	protected void setId(final int id) {
		this.id = id;
	}

	public void setMessageBody(final byte[] MessageBody) {
		this.MessageBody = MessageBody;
	}

	public void setNewsgroups(final Set<NewsGroup> newsgroups) {
		this.newsgroups = newsgroups;
	}

	public void setPoster(final UsenetUser poster) {
		this.poster = poster;
	}

	public void setReferencedMessages(final String referencedMessages) {
		this.referencedMessages = referencedMessages;
	}

	public void setSubject(final String Subject) {
		this.Subject = Subject;
	}

	public void setTopic(final Topic topic) {
		this.topic = topic;
	}

	public void setUsenetMessageID(final String UsenetMessageID) {
		this.UsenetMessageID = UsenetMessageID;
	}

}
