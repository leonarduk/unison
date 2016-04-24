package uk.co.sleonard.unison.input;

/**
 * NewsGroupArticle - NNTP news article
 *
 * @author Steve Leonard Copyright (C) 2007
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessageRemovedException;
import javax.mail.MessagingException;

import uk.co.sleonard.unison.datahandling.DatabaseConnection;
import uk.co.sleonard.unison.datahandling.StringUtils;

/**
 * This class is used to hold the data found in the NNTP message that we
 * download.
 * 
 * @author steve
 * 
 */
public class NewsGroupArticle {
	public static final String FOLLOWUP_TO = "Followup-To";

	public static final String FROM = "From";

	public static final String NNTP_POSTING_HOST = "NNTP-Posting-Host";

	public static final String MESSAGE_ID = "Message-ID";

	public static final String fieldApproved = "Approved";

	public static final String fieldComplaints = "Complaints";

	public static final String fieldControl = "Control";

	public static final String fieldDateSent = "Sent";

	public static final String fieldDistribution = "Distribution";

	public static final String fieldExpires = "Expires";

	public static final String fieldFollowUpTo = "FollowUpTo";

	public static final String fieldFrom = "From_";

	public static final String fieldKeywords = "KeyWords";

	public static final String fieldLines = "Lines";

	public static final String fieldNewsGroups = "NewsGroups";

	public static final String fieldUserID = "UserID";

	public static final String fieldMessageID = "MessageID";

	public static final String fieldOrganization = "Organization";

	public static final String fieldPath = "Path";

	public static final String fieldPostingHost = "PostingHost";

	public static final String fieldReferences = "References";

	public static final String fieldReplyTo = "ReplyTo";

	public static final String fieldSender = "Sender";

	public static final String fieldSubject = "Subject";

	public static final String fieldSummary = "Summary";

	public static final String fieldText = "Text";

	public static final String fieldTrace = "Trace";

	public static final String fieldUserAgent = "UserAgent";

	public static final String fieldXref = "Xref";

	public static final String tableName = "NewsArticle";

	public static final String SUBJECT = "Subject";

	public static final String SENT = "Sent";

	public static final String NEWSGROUPS = "NewsGroups";

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.city.abbh224.persistence.Storable#createTable()
	 */
	public static void createTable(DatabaseConnection conn) {

		conn.runUpdate("DROP TABLE " + tableName);

		// NB - creating CACHED table - this saves DB to file not
		// just holding in memory.
		String ddl = "CREATE CACHED TABLE " + tableName + "( " + fieldMessageID
				+ " varchar(255), " + fieldSubject + " varchar(255), "
				+ fieldNewsGroups + " varchar(255), " + fieldFrom
				+ " varchar(50), " + fieldDateSent + " datetime, " + fieldPath
				+ " varchar(255), " + fieldReplyTo + " varchar(255), "
				+ fieldSender + " varchar(255), " + fieldFollowUpTo
				+ " varchar(255), " + fieldExpires + " varchar(255), "
				+ fieldReferences + " varchar(255), " + fieldControl
				+ " varchar(255), " + fieldDistribution + " varchar(255), "
				+ fieldOrganization + " varchar(255), " + fieldKeywords
				+ " varchar(255), " + fieldSummary + " varchar(255), "
				+ fieldApproved + " varchar(255), " + fieldLines + " int, "
				+ fieldXref + " varchar(255), " + fieldPostingHost
				+ " varchar(255), " + fieldUserAgent + " varchar(255), "
				+ fieldTrace + " varchar(255), " + fieldComplaints
				+ " varchar(255), " + fieldText + " OTHER, " + fieldUserID
				+ " VARCHAR(20), " + " CONSTRAINT uniqID UNIQUE ("
				+ fieldMessageID + " )) ";
		conn.runUpdate(ddl);
	}

	private Object content;

	private String group;

	private HashMap<String, Object> headerFields;

	private int lineCount;

	private Date sentDate;

	private String subject;

	public NewsGroupArticle(HashMap<String, Object> fieldsMap) {
		this.headerFields = fieldsMap;
		subject = (String) headerFields.get(SUBJECT);
		sentDate = (Date) headerFields.get(SENT);
		String newsgroupText = getFieldValue(NEWSGROUPS).toString();

		newsGroups = StringUtils.convertCommasToList(newsgroupText
				.toLowerCase());

	}

	/**
	 * 
	 * @param message
	 * @throws MessageRemovedException
	 */
	public NewsGroupArticle(Message message) throws MessageRemovedException {

		try {
			// read all the header fields to extract the ones
			// we need
			Enumeration headers = message.getAllHeaders();

			headerFields = new HashMap<String, Object>();
			while (headers.hasMoreElements()) {
				Header header = (Header) headers.nextElement();

				StringBuffer valueBuf = new StringBuffer();

				// In case the field appears more than once
				// then append each one
				if (headerFields.containsKey(header.getName())) {
					valueBuf.append(headerFields.get(header.getName()) + "\n");
				}
				String value = header.getValue().replaceAll("<", "");
				value = value.replaceAll(">", "");

				valueBuf.append(value);

				headerFields.put(header.getName(), valueBuf.toString());
				// System.out.println(header.getName() + "=" +
				// header.getValue());
			}

			// mandatory fields
			// not using m.getFrom() as it insists on validating email addresses
			// and some are invalid for some reason (have spaces etc)

			sentDate = message.getSentDate(); // getSent()
			// Some ranting posts have enormous subject lines which
			// cause problems when inserting into database
			subject = message.getSubject(); // getSubject()
			if (subject.length() > 255) {
				subject = subject.substring(0, 254);
			}
			String newsgroupText = getFieldValue(NEWSGROUPS).toString();

			newsGroups = StringUtils.convertCommasToList(newsgroupText
					.toLowerCase());
			// since some messages only show the cross posting
			newsGroups.add(message.getFolder().getName());

			// Message-ID - getMessageID()
			// Path - getPath()

			/*
			 * optional fields
			 */
			// Reply-To - getReplyTo()
			// Sender - original poster of message - getSender()
			// Followup-To - groups to reply to - getFollowupTo()
			// Expires - getExpires()
			// References - getReferences()
			// Control - not needed by us - getControl()
			// Distribution - getDistribution()
			// Organization - where sender is from (Usually ISP) -
			// getOrganization()
			// Keywords - not used - getKeywords()
			// Summary - not used - getSummary()
			// Approved - getApproved()
			// Lines - getLineCount()
			lineCount = message.getLineCount();

			// Xref - getXref()
			// NNTP-Posting-Host - getPostingHost()
			// User-Agent (or) X-Newsreader - getUserAgent()
			// X-Trace: - getTrace()
			// X-Complaints-To: - getComplaintsTo()

			// Message text - getContent()
			content = message.getContent();

			group = message.getFolder().getFullName();

		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getApproved() {
		return getFieldValue("Approved").toString();
	}

	public String getComplaintsTo() {
		return getFieldValue("X-Complaints-To").toString();
	}

	public Object getContent() {
		if (null != content)
			return content;
		return "";
	}

	public String getControl() {
		return getFieldValue("Control").toString();
	}

	public Date getDate() {
		return sentDate;
	}

	public String getDistribution() {
		return getFieldValue("Distribution").toString();
	}

	public String getExpires() {
		return getFieldValue("Expires").toString();
	}

	public String getFieldValue(String key) {
		String value = "";
		if (null == headerFields) {
			throw new NullPointerException("HEADERFIELDS NULL - FIX THIS BUG");
		}
		if (null != key && headerFields.containsKey(key)) {
			value = headerFields.get(key).toString();
		}
		return value;
	}

	public String getFollowupTo() {
		return getFieldValue(FOLLOWUP_TO).toString();
	}

	// public Address[] getFrom() {
	// return from;
	// }

	public String getFromString() {
		return getFieldValue(FROM).toString();
	}

	public String getHeaderString() {
		// Mandatory fields
		StringBuffer buf = new StringBuffer("From : " + getFromString() + "\n");
		buf.append("Sent date: " + getDate() + "\n");
		buf.append("Groups: " + getNewsgroupsList() + "\n");
		buf.append("Subject: " + getSubject() + "\n");
		buf.append("Message ID: " + getMessageID() + "\n");
		buf.append("Path " + getPath() + "\n");

		// Optional fields
		buf.append("ReplyTo:" + getReplyTo() + "\n");
		buf.append("Sender:" + getSender() + "\n");
		buf.append("FollowUpTo:" + getFollowupTo() + "\n");
		buf.append("Expires:" + getExpires() + "\n");

		buf.append("References " + getReferences() + "\n");
		buf.append("Control " + getControl() + "\n");

		buf.append("Distribution " + getDistribution() + "\n");

		buf.append("Organization " + getOrganization() + "\n");

		buf.append("Keywords " + getKeywords() + "\n");

		buf.append("Summary " + getSummary() + "\n");
		buf.append("Approved " + getApproved() + "\n");
		buf.append("LineCount " + getLineCount() + "\n");
		buf.append("Xref " + getXref() + "\n");

		buf.append("Posting Host: " + getPostingHost() + "\n");
		buf.append("User Agent: " + getUserAgent() + "\n");

		buf.append("X-Trace: " + getTrace() + "\n");

		buf.append("ComplaintsTo " + getComplaintsTo() + "\n");
		return buf.toString();

	}

	public String getKeywords() {
		return getFieldValue("Keywords").toString();
	}

	public int getLineCount() {
		return lineCount;
	}

	public String getMessageID() {
		return getFieldValue(MESSAGE_ID).toString();
	}

	List<String> newsGroups;

	public List<String> getNewsgroupsList() {
		return newsGroups;
	}

	public String getOrganization() {
		return getFieldValue("Organization").toString();
	}

	public List getPathList() {
		return StringUtils.convertCommasToList(getPath());
	}

	public String getPath() {
		return getFieldValue("Path").toString();
	}

	public String getPostingHost() {
		return getFieldValue(NNTP_POSTING_HOST).toString();

	}

	public String getReferences() {
		return getFieldValue("References").toString();
	}

	public List getReferencesList() {
		// getReferences returns String like <ssf..> <sfsdf..>
		Vector<String> groups = new Vector<String>();

		StringTokenizer tok = new StringTokenizer(getReferences(), " ");
		while (tok.hasMoreTokens()) {
			String next = tok.nextToken();
			groups.add(next);
		}

		return groups;
	}

	public String getReplyTo() {
		return getFieldValue("Reply-To").toString();
	}

	public String getSender() {
		return getFieldValue("Sender").toString();
	}

	public String getSourceGroup() {
		return group;
	}

	public String getSubject() {
		return subject;
	}

	public String getSummary() {
		return getFieldValue("Summary").toString();
	}

	public String getTrace() {
		return getFieldValue("X-Trace").toString();
	}

	public String getUserAgent() {
		return getFieldValue("User-Agent").toString();
	}

	public String getXref() {
		return getFieldValue("Xref").toString();
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(getHeaderString());
		buf.append("Message: " + getContent() + "\n");
		return buf.toString();
	}
}
