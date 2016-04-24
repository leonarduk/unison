package uk.co.sleonard.unison.datahandling.DAO;
// Generated 13-Oct-2007 22:29:18 by Hibernate Tools 3.2.0.b9


import java.util.HashSet;
import java.util.Set;

/**
 * 			Represents a news group e.g. soc.senior.issues
 * 		
 */
public class NewsGroup  implements java.io.Serializable {


     private int id;
     private String name;
     private NewsGroup parentNewsGroup;
     /**
      * 				Message Threads in this NewsGroup
 * 			
     */
     private Set topics = new HashSet(0);
     /**
      * 				Messages in this NewsGroup
 * 			
     */
     private Set messages = new HashSet(0);
     private int lastIndexDownloaded;
     private int lastMessageTotal;
     private String fullName;
     private boolean lastNode;

    public NewsGroup() {
    }

	
    public NewsGroup(String name) {
        this.name = name;
    }
    public NewsGroup(String name, NewsGroup parentNewsGroup, Set topics, Set messages, int lastIndexDownloaded, int lastMessageTotal, String fullName, boolean lastNode) {
       this.name = name;
       this.parentNewsGroup = parentNewsGroup;
       this.topics = topics;
       this.messages = messages;
       this.lastIndexDownloaded = lastIndexDownloaded;
       this.lastMessageTotal = lastMessageTotal;
       this.fullName = fullName;
       this.lastNode = lastNode;
    }
   
    public int getId() {
        return this.id;
    }
    
    protected void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public NewsGroup getParentNewsGroup() {
        return this.parentNewsGroup;
    }
    
    public void setParentNewsGroup(NewsGroup parentNewsGroup) {
        this.parentNewsGroup = parentNewsGroup;
    }
    /**       
     *      * 				Message Threads in this NewsGroup
     * 			
     */
    public Set getTopics() {
        return this.topics;
    }
    
    public void setTopics(Set topics) {
        this.topics = topics;
    }
    /**       
     *      * 				Messages in this NewsGroup
     * 			
     */
    public Set getMessages() {
        return this.messages;
    }
    
    public void setMessages(Set messages) {
        this.messages = messages;
    }
    public int getLastIndexDownloaded() {
        return this.lastIndexDownloaded;
    }
    
    public void setLastIndexDownloaded(int lastIndexDownloaded) {
        this.lastIndexDownloaded = lastIndexDownloaded;
    }
    public int getLastMessageTotal() {
        return this.lastMessageTotal;
    }
    
    public void setLastMessageTotal(int lastMessageTotal) {
        this.lastMessageTotal = lastMessageTotal;
    }
    public String getFullName() {
        return this.fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public boolean isLastNode() {
        return this.lastNode;
    }
    
    public void setLastNode(boolean lastNode) {
        this.lastNode = lastNode;
    }

    /**
     * toString
     * @return String
     */
     public String toString() {
	  StringBuffer buffer = new StringBuffer();

      buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
      buffer.append("name").append("='").append(getName()).append("' ");			
      buffer.append("]");
      
      return buffer.toString();
     }



}


