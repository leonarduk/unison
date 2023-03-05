/**
 * Message
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.datahandling.DAO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Message implements java.io.Serializable {
	private static final long serialVersionUID = -4828381724935020136L;
	private Date dateCreated;
	private int id;
	private String subject;
	private UsenetUser poster;
	private Topic topic;
	private String usenetMessageID;
	private Set<NewsGroup> newsgroups = new HashSet<>(0);
	private String referencedMessages;
	private byte[] messageBody;

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
			log.debug("thatOne is null");
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			log.debug("thatOne is a " + obj.getClass().getName());
			return false;
		}
		final Message other = (Message) obj;
		if (this.dateCreated == null) {
			if (other.dateCreated != null) {
				log.debug("thatOne doesn't have null dateCreated");
				return false;
			}
		}
		else if (!this.dateCreated.equals(other.dateCreated)) {
			log.debug("thatOne has a different dateCreated");
			return false;
		}
		if (this.id != other.id) {
			log.debug("thatOne has different Id");

			return false;
		}
		if (!Arrays.equals(this.messageBody, other.messageBody)) {
			log.debug("thatOne has a different body");
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
			log.debug("thatOne has different poster" + this.poster + ":" + other.poster);
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

	@Override
	public String toString() {
		return this.getSubject() + ":" + this.poster;
	}

}
