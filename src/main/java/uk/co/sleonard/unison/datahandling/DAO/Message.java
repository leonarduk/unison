/**
 * Message
 * 
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.datahandling.DAO;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a message.
 *
 * @author Hibernate Tools 3.2.0.b9
 * @since Generated 11-Nov-2007 17:31:30
 *
 */
public class Message implements java.io.Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4828381724935020136L;

	/** The Date created. */
	private Date dateCreated;

	/** The id. */
	private int id;

	/** The Message body. */
	private byte[] messageBody;

	/** The newsgroups. */
	private Set<NewsGroup> newsgroups = new HashSet<>(0);

	/** The poster. */
	private UsenetUser poster;

	/** The referenced messages. */
	private String referencedMessages;

	/** The Subject. */
	private String subject;

	/** The topic. */
	private Topic topic;

	/** The Usenet message id. */
	private String usenetMessageID;

	/**
	 * Instantiates a new message.
	 */
	public Message() {
	}

	/**
	 * Instantiates a new message.
	 *
	 * @param dateCreated
	 *            the date created
	 * @param usenetMessageID
	 *            the usenet message id
	 * @param subject
	 *            the subject
	 * @param poster
	 *            the poster
	 * @param topic
	 *            the topic
	 * @param messageBody
	 *            the message body
	 */
	public Message(final Date dateCreated, final String usenetMessageID, final String subject,
	        final UsenetUser poster, final Topic topic, final byte[] messageBody) {
		this.dateCreated = dateCreated;
		this.usenetMessageID = usenetMessageID;
		this.subject = subject;
		this.poster = poster;
		this.topic = topic;
		this.messageBody = messageBody;
	}

	/**
	 * Instantiates a new message.
	 *
	 * @param DateCreated
	 *            the date created
	 * @param UsenetMessageID
	 *            the usenet message id
	 * @param Subject
	 *            the subject
	 * @param poster
	 *            the poster
	 * @param topic
	 *            the topic
	 * @param newsgroups
	 *            the newsgroups
	 * @param referencedMessages
	 *            the referenced messages
	 * @param MessageBody
	 *            the message body
	 */
	public Message(final Date DateCreated, final String UsenetMessageID, final String Subject,
	        final UsenetUser poster, final Topic topic, final Set<NewsGroup> newsgroups,
	        final String referencedMessages, final byte[] MessageBody) {
		this.dateCreated = DateCreated;
		this.usenetMessageID = UsenetMessageID;
		this.subject = Subject;
		this.poster = poster;
		this.topic = topic;
		this.newsgroups = newsgroups;
		this.referencedMessages = referencedMessages;
		this.messageBody = MessageBody;
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
		if (!(object instanceof Message)) {
			return false;
		}
		final Message that = (Message) object;
		if ((this.getId() != that.getId())
		        || ((null != this.getMessageBody())
		                && !this.getMessageBody().equals(that.getMessageBody()))
		        || !this.getTopic().equals(that.getTopic())
		        || !this.getUsenetMessageID().equals(that.getUsenetMessageID())
		        || !this.getDateCreated().equals(that.getDateCreated())
		        || !this.getReferencedMessages().equals(that.getReferencedMessages())
		        || !this.getPoster().equals(that.getPoster())
		        || !this.getSubject().equals(this.getSubject())) {
			return false;
		}
		return (this.getNewsgroups().containsAll(that.getNewsgroups())
		        && that.getNewsgroups().containsAll(this.getNewsgroups()));
	}

	/**
	 * Gets the date created.
	 *
	 * @return the date created
	 */
	public Date getDateCreated() {
		return this.dateCreated;
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
	 * Gets the message body.
	 *
	 * @return the message body
	 */
	public byte[] getMessageBody() {
		return this.messageBody;
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
	 * Gets the poster.
	 *
	 * @return the poster
	 */
	public UsenetUser getPoster() {
		return this.poster;
	}

	/**
	 * Gets the referenced messages.
	 *
	 * @return the referenced messages
	 */
	public String getReferencedMessages() {
		return this.referencedMessages;
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
	 * Gets the topic.
	 *
	 * @return the topic
	 */
	public Topic getTopic() {
		return this.topic;
	}

	/**
	 * Gets the usenet message id.
	 *
	 * @return the usenet message id
	 */
	public String getUsenetMessageID() {
		return this.usenetMessageID;
	}

	/**
	 * Need to implement this to avoid NonUniqueObjectException //TODO
	 * http://forum.springframework.org/showthread.php?t=22261
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		int hashCode = 0;
		hashCode = (29 * hashCode) + this.id;

		return hashCode;
	}

	/**
	 * Sets the date created.
	 *
	 * @param DateCreated
	 *            the new date created
	 */
	public void setDateCreated(final Date DateCreated) {
		this.dateCreated = DateCreated;
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
	 * Sets the message body.
	 *
	 * @param MessageBody
	 *            the new message body
	 */
	public void setMessageBody(final byte[] MessageBody) {
		this.messageBody = MessageBody;
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
	 * Sets the poster.
	 *
	 * @param poster
	 *            the new poster
	 */
	public void setPoster(final UsenetUser poster) {
		this.poster = poster;
	}

	/**
	 * Sets the referenced messages.
	 *
	 * @param referencedMessages
	 *            the new referenced messages
	 */
	public void setReferencedMessages(final String referencedMessages) {
		this.referencedMessages = referencedMessages;
	}

	/**
	 * Sets the subject.
	 *
	 * @param Subject
	 *            the new subject
	 */
	public void setSubject(final String Subject) {
		this.subject = Subject;
	}

	/**
	 * Sets the topic.
	 *
	 * @param topic
	 *            the new topic
	 */
	public void setTopic(final Topic topic) {
		this.topic = topic;
	}

	/**
	 * Sets the usenet message id.
	 *
	 * @param UsenetMessageID
	 *            the new usenet message id
	 */
	public void setUsenetMessageID(final String UsenetMessageID) {
		this.usenetMessageID = UsenetMessageID;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getSubject() + ":" + this.poster;
	}

}
