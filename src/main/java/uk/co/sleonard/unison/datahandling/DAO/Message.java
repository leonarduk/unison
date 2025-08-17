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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents a message.
 *
 * @author Hibernate Tools 3.2.0.b9
 * @since Generated 11-Nov-2007 17:31:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
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

  public Message(
      final Date dateCreated,
      final String usenetMessageID,
      final String subject,
      final UsenetUser poster,
      final Topic topic,
      final Set<NewsGroup> newsgroups,
      final String referencedMessages,
      final byte[] messageBody) {
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
  public String toString() {
    return this.getSubject() + ":" + this.poster;
  }
}
