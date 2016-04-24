package uk.co.sleonard.unison.datahandling.DAO;
// Generated 13-Oct-2007 22:29:18 by Hibernate Tools 3.2.0.b9


import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a message
 */
public class Message  implements java.io.Serializable {


     private int id;
     private Date DateCreated;
     private String UsenetMessageID;
     private String Subject;
     private UsenetUser poster;
     private Topic topic;
     private Set newsgroups = new HashSet(0);
     private List referencedMessages = new ArrayList(0);
     private byte[] MessageBody;

    public Message() {
    }

	
    public Message(Date DateCreated, String UsenetMessageID, String Subject, UsenetUser poster, Topic topic, byte[] MessageBody) {
        this.DateCreated = DateCreated;
        this.UsenetMessageID = UsenetMessageID;
        this.Subject = Subject;
        this.poster = poster;
        this.topic = topic;
        this.MessageBody = MessageBody;
    }
    public Message(Date DateCreated, String UsenetMessageID, String Subject, UsenetUser poster, Topic topic, Set newsgroups, List referencedMessages, byte[] MessageBody) {
       this.DateCreated = DateCreated;
       this.UsenetMessageID = UsenetMessageID;
       this.Subject = Subject;
       this.poster = poster;
       this.topic = topic;
       this.newsgroups = newsgroups;
       this.referencedMessages = referencedMessages;
       this.MessageBody = MessageBody;
    }
   
    public int getId() {
        return this.id;
    }
    
    protected void setId(int id) {
        this.id = id;
    }
    public Date getDateCreated() {
        return this.DateCreated;
    }
    
    public void setDateCreated(Date DateCreated) {
        this.DateCreated = DateCreated;
    }
    public String getUsenetMessageID() {
        return this.UsenetMessageID;
    }
    
    public void setUsenetMessageID(String UsenetMessageID) {
        this.UsenetMessageID = UsenetMessageID;
    }
    public String getSubject() {
        return this.Subject;
    }
    
    public void setSubject(String Subject) {
        this.Subject = Subject;
    }
    public UsenetUser getPoster() {
        return this.poster;
    }
    
    public void setPoster(UsenetUser poster) {
        this.poster = poster;
    }
    public Topic getTopic() {
        return this.topic;
    }
    
    public void setTopic(Topic topic) {
        this.topic = topic;
    }
    public Set getNewsgroups() {
        return this.newsgroups;
    }
    
    public void setNewsgroups(Set newsgroups) {
        this.newsgroups = newsgroups;
    }
    public List getReferencedMessages() {
        return this.referencedMessages;
    }
    
    public void setReferencedMessages(List referencedMessages) {
        this.referencedMessages = referencedMessages;
    }
    public byte[] getMessageBody() {
        return this.MessageBody;
    }
    
    public void setMessageBody(byte[] MessageBody) {
        this.MessageBody = MessageBody;
    }




}


