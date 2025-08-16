/**
 * NewsGroup
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.datahandling.DAO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.net.nntp.NewsgroupInfo;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a news group e.g. soc.senior.issues
 *
 * @author Hibernate Tools 3.2.0.b9
 * @since Generated 11-Nov-2007 17:31:30
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsGroup implements java.io.Serializable, Comparable<NewsGroup> {
	private static final long	serialVersionUID	= -8526704874026184061L;
	private int					firstMessage;
private String fullName;
	private int id;
	private int lastIndexDownloaded;
	private int lastMessage;
	private int lastMessageTotal;
	private boolean lastNode;
	private Set<Message> messages = new HashSet<>(0);
	private String name;
	private NewsGroup parentNewsGroup;
	private Set<Topic> topics = new HashSet<>(0);
	private int estimatedArticleCount;

	@SuppressWarnings("deprecation")
	public NewsGroup(final NewsgroupInfo original) {
		this.estimatedArticleCount = original.getArticleCount();
		this.firstMessage = original.getFirstArticle();
		this.lastMessage = original.getLastArticle();
		this.name = original.getNewsgroup();
		this.fullName = this.name;
	}

	public NewsGroup(final String name, final NewsGroup parentNewsGroup, final Set<Topic> topics,
	        final Set<Message> messages, final int lastIndexDownloaded, final int lastMessageTotal,
	        final int firstMessage, final int lastMessage, final String fullName,
	        final boolean lastNode) {
		this.name = name;
		this.parentNewsGroup = parentNewsGroup;
		this.topics = topics;
		this.messages = messages;
		this.lastIndexDownloaded = lastIndexDownloaded;
		this.lastMessageTotal = lastMessageTotal;
		this.lastMessage = lastMessage;
		this.firstMessage = firstMessage;

		this.fullName = fullName;
		this.lastNode = lastNode;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final NewsGroup that) {
		return this.getName().compareTo(that.getName());
	}


	public int getArticleCount() {
		final int total = this.lastMessage - this.firstMessage;
		if (total < this.estimatedArticleCount) {
			return total;
		}
		return this.estimatedArticleCount;
	}

	/**
	 * Checks if is last node.
	 *
	 * @return true, if is last node
	 */
        public boolean isLastNode() {
                return this.lastNode;
        }

        @Override
        public boolean equals(final Object obj) {
                if (this == obj) {
                        return true;
                }
                if (!(obj instanceof NewsGroup)) {
                        return false;
                }
                final NewsGroup other = (NewsGroup) obj;
                return this.id == other.id && Objects.equals(this.name, other.name)
                                && Objects.equals(this.fullName, other.fullName);
        }

        @Override
        public int hashCode() {
                return Objects.hash(this.id, this.name, this.fullName);
        }

        @Override
        public String toString() {
                return this.getFullName() + "(" + this.getArticleCount() + ")";
        }

}
