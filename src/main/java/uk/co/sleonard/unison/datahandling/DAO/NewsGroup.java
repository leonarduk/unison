package uk.co.sleonard.unison.datahandling.DAO;

// Generated 11-Nov-2007 17:31:30 by Hibernate Tools 3.2.0.b9

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a news group e.g. soc.senior.issues
 *
 */
public class NewsGroup implements java.io.Serializable, Comparable<NewsGroup> {

	/**
	 *
	 */
	private static final long serialVersionUID = -8526704874026184061L;

	/** The first message. */

	private int firstMessage;

	/** The full name. */
	private String fullName;

	/** The id. */
	private int id;

	/** The last index downloaded. */
	private int lastIndexDownloaded;

	/** The last message. */
	private int lastMessage;

	/** The last message total. */
	private int lastMessageTotal;

	/** The last node. */
	private boolean lastNode;

	/** Messages in this NewsGroup. */
	private Set<Message> messages = new HashSet<Message>(0);

	/** The name. */
	private String name;

	/** The parent news group. */
	private NewsGroup parentNewsGroup;

	/** Message Threads in this NewsGroup. */
	private Set<Topic> topics = new HashSet<Topic>(0);

	/**
	 * Instantiates a new news group.
	 */
	public NewsGroup() {
	}

	/**
	 * Instantiates a new news group.
	 *
	 * @param name
	 *            the name
	 */
	public NewsGroup(final String name) {
		this.name = name;
	}

	/**
	 * Instantiates a new news group.
	 *
	 * @param name
	 *            the name
	 * @param parentNewsGroup
	 *            the parent news group
	 * @param topics
	 *            the topics
	 * @param messages
	 *            the messages
	 * @param lastIndexDownloaded
	 *            the last index downloaded
	 * @param lastMessageTotal
	 *            the last message total
	 * @param firstMessage
	 *            the first message
	 * @param lastMessage
	 *            the last message
	 * @param fullName
	 *            the full name
	 * @param lastNode
	 *            the last node
	 */
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
		return this.getFullName().compareTo(that.getFullName());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object object) {
		if (!(object instanceof NewsGroup)) {
			return false;
		}
		final NewsGroup that = (NewsGroup) object;
		if (!this.getFullName().equals(that.getFullName())) {
			return false;
		}

		return true;
	}

	/**
	 * Gets the first message.
	 *
	 * @return the first message
	 */
	public int getFirstMessage() {
		return this.firstMessage;
	}

	/**
	 * Gets the full name.
	 *
	 * @return the full name
	 */
	public String getFullName() {
		return this.fullName;
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
	 * Gets the last index downloaded.
	 *
	 * @return the last index downloaded
	 */
	public int getLastIndexDownloaded() {
		return this.lastIndexDownloaded;
	}

	/**
	 * Gets the last message.
	 *
	 * @return the last message
	 */
	public int getLastMessage() {
		return this.lastMessage;
	}

	/**
	 * Gets the last message total.
	 *
	 * @return the last message total
	 */
	public int getLastMessageTotal() {
		return this.lastMessageTotal;
	}

	/**
	 * * Messages in this NewsGroup.
	 *
	 * @return the messages
	 */
	public Set<Message> getMessages() {
		return this.messages;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the parent news group.
	 *
	 * @return the parent news group
	 */
	public NewsGroup getParentNewsGroup() {
		return this.parentNewsGroup;
	}

	/**
	 * * Message Threads in this NewsGroup.
	 *
	 * @return the topics
	 */
	public Set<Topic> getTopics() {
		return this.topics;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hashCode = 0;
		hashCode = 29 * hashCode + this.id;

		return hashCode;
	}

	/**
	 * Checks if is last node.
	 *
	 * @return true, if is last node
	 */
	public boolean isLastNode() {
		return this.lastNode;
	}

	/**
	 * Sets the first message.
	 *
	 * @param firstMessage
	 *            the new first message
	 */
	public void setFirstMessage(final int firstMessage) {
		this.firstMessage = firstMessage;
	}

	/**
	 * Sets the full name.
	 *
	 * @param fullName
	 *            the new full name
	 */
	public void setFullName(final String fullName) {
		this.fullName = fullName;
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
	 * Sets the last index downloaded.
	 *
	 * @param lastIndexDownloaded
	 *            the new last index downloaded
	 */
	public void setLastIndexDownloaded(final int lastIndexDownloaded) {
		this.lastIndexDownloaded = lastIndexDownloaded;
	}

	/**
	 * Sets the last message.
	 *
	 * @param lastMessage
	 *            the new last message
	 */
	public void setLastMessage(final int lastMessage) {
		this.lastMessage = lastMessage;
	}

	/**
	 * Sets the last message total.
	 *
	 * @param lastMessageTotal
	 *            the new last message total
	 */
	public void setLastMessageTotal(final int lastMessageTotal) {
		this.lastMessageTotal = lastMessageTotal;
	}

	/**
	 * Sets the last node.
	 *
	 * @param lastNode
	 *            the new last node
	 */
	public void setLastNode(final boolean lastNode) {
		this.lastNode = lastNode;
	}

	/**
	 * Sets the messages.
	 *
	 * @param messages
	 *            the new messages
	 */
	public void setMessages(final Set<Message> messages) {
		this.messages = messages;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Sets the parent news group.
	 *
	 * @param parentNewsGroup
	 *            the new parent news group
	 */
	public void setParentNewsGroup(final NewsGroup parentNewsGroup) {
		this.parentNewsGroup = parentNewsGroup;
	}

	/**
	 * Sets the topics.
	 *
	 * @param topics
	 *            the new topics
	 */
	public void setTopics(final Set<Topic> topics) {
		this.topics = topics;
	}

	/**
	 * toString.
	 *
	 * @return String
	 */
	@Override
	public String toString() {
		return this.getFullName();
	}

}
