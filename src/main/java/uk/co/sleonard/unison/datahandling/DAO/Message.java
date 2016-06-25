/**
 * Message
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.datahandling.DAO;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

	Log logger = LogFactory.getLog(Message.class);

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
	 * @param newsgroups
	 *            the newsgroups
	 * @param referencedMessages
	 *            the referenced messages
	 * @param messageBody
	 *            the message body
	 */
	public Message(final Date dateCreated, final String usenetMessageID, final String subject,
	        final UsenetUser poster, final Topic topic, final Set<NewsGroup> newsgroups,
	        final String referencedMessages, final byte[] messageBody) {
		this.dateCreated = dateCreated;
		this.usenetMessageID = usenetMessageID;
		this.subject = subject;
		this.poster = poster;
		this.topic = topic;
		this.newsgroups = newsgroups;
		this.referencedMessages = referencedMessages;
		this.messageBody = messageBody;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			this.logger.debug("thatOne is null");
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			this.logger.debug("thatOne is a " + obj.getClass().getName());
			return false;
		}
		final Message other = (Message) obj;
		if (this.dateCreated == null) {
			if (other.dateCreated != null) {
				this.logger.debug("thatOne doesn't have null dateCreated");
				return false;
			}
		}
		else if (!this.dateCreated.equals(other.dateCreated)) {
			this.logger.debug("thatOne has a different dateCreated");
			return false;
		}
		if (this.id != other.id) {
			this.logger.debug("thatOne has different Id");

			return false;
		}
		if (!Arrays.equals(this.messageBody, other.messageBody)) {
			this.logger.debug("thatOne has a different body");
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
		if (this.poster == null) {
			if (other.poster != null) {
				return false;
			}
		}
		else if (!this.poster.equals(other.poster)) {
			this.logger.debug("thatOne has different poster" + this.poster + ":" + other.poster);
			return false;
		}
		if (this.referencedMessages == null) {
			if (other.referencedMessages != null) {
				return false;
			}
		}
		else if (!this.referencedMessages.equals(other.referencedMessages)) {
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
		if (this.topic == null) {
			if (other.topic != null) {
				return false;
			}
		}
		else if (!this.topic.equals(other.topic)) {
			return false;
		}
		if (this.usenetMessageID == null) {
			if (other.usenetMessageID != null) {
				return false;
			}
		}
		else if (!this.usenetMessageID.equals(other.usenetMessageID)) {
			return false;
		}
		return true;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.dateCreated == null) ? 0 : this.dateCreated.hashCode());
		result = (prime * result) + this.id;
		result = (prime * result) + Arrays.hashCode(this.messageBody);
		result = (prime * result) + ((this.newsgroups == null) ? 0 : this.newsgroups.hashCode());
		result = (prime * result) + ((this.poster == null) ? 0 : this.poster.hashCode());
		result = (prime * result)
		        + ((this.referencedMessages == null) ? 0 : this.referencedMessages.hashCode());
		result = (prime * result) + ((this.subject == null) ? 0 : this.subject.hashCode());
		result = (prime * result) + ((this.topic == null) ? 0 : this.topic.hashCode());
		result = (prime * result)
		        + ((this.usenetMessageID == null) ? 0 : this.usenetMessageID.hashCode());
		return result;
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
