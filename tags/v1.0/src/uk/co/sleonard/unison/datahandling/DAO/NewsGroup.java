package uk.co.sleonard.unison.datahandling.DAO;

// Generated 11-Nov-2007 17:31:30 by Hibernate Tools 3.2.0.b9

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a news group e.g. soc.senior.issues
 * 
 */
public class NewsGroup implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 302478787125055799L;

	private int firstMessage;

	private String fullName;

	private int id;

	private int lastIndexDownloaded;

	private int lastMessage;

	private int lastMessageTotal;

	private boolean lastNode;

	/**
	 * Messages in this NewsGroup
	 * 
	 */
	private Set<Message> messages = new HashSet<Message>(0);

	private String name;

	private NewsGroup parentNewsGroup;

	/**
	 * Message Threads in this NewsGroup
	 * 
	 */
	private Set<Topic> topics = new HashSet<Topic>(0);

	public NewsGroup() {
	}

	public NewsGroup(final String name) {
		this.name = name;
	}

	public NewsGroup(final String name, final NewsGroup parentNewsGroup,
			final Set<Topic> topics, final Set<Message> messages,
			final int lastIndexDownloaded, final int lastMessageTotal,
			final int firstMessage, final int lastMessage,
			final String fullName, final boolean lastNode) {
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

	public int getFirstMessage() {
		return this.firstMessage;
	}

	public String getFullName() {
		return this.fullName;
	}

	public int getId() {
		return this.id;
	}

	public int getLastIndexDownloaded() {
		return this.lastIndexDownloaded;
	}

	public int getLastMessage() {
		return this.lastMessage;
	}

	public int getLastMessageTotal() {
		return this.lastMessageTotal;
	}

	/**
	 * * Messages in this NewsGroup
	 * 
	 */
	public Set<Message> getMessages() {
		return this.messages;
	}

	public String getName() {
		return this.name;
	}

	public NewsGroup getParentNewsGroup() {
		return this.parentNewsGroup;
	}

	/**
	 * * Message Threads in this NewsGroup
	 * 
	 */
	public Set<Topic> getTopics() {
		return this.topics;
	}

	public boolean isLastNode() {
		return this.lastNode;
	}

	public void setFirstMessage(final int firstMessage) {
		this.firstMessage = firstMessage;
	}

	public void setFullName(final String fullName) {
		this.fullName = fullName;
	}

	protected void setId(final int id) {
		this.id = id;
	}

	public void setLastIndexDownloaded(final int lastIndexDownloaded) {
		this.lastIndexDownloaded = lastIndexDownloaded;
	}

	public void setLastMessage(final int lastMessage) {
		this.lastMessage = lastMessage;
	}

	public void setLastMessageTotal(final int lastMessageTotal) {
		this.lastMessageTotal = lastMessageTotal;
	}

	public void setLastNode(final boolean lastNode) {
		this.lastNode = lastNode;
	}

	public void setMessages(final Set<Message> messages) {
		this.messages = messages;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setParentNewsGroup(final NewsGroup parentNewsGroup) {
		this.parentNewsGroup = parentNewsGroup;
	}

	public void setTopics(final Set<Topic> topics) {
		this.topics = topics;
	}

	/**
	 * toString
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		return getName();
	}

}
