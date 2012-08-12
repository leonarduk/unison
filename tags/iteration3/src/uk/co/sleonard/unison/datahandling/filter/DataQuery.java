package uk.co.sleonard.unison.datahandling.filter;

import java.util.Vector;

import org.apache.log4j.Logger;

import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.DAO.Location;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;

public class DataQuery {
	private static Logger logger = Logger.getLogger("DataQuery");

	private static StringBuffer addWhereClause(StringBuffer sqlBuffer,
			Vector<String> whereClauses) {
		logger.debug("addWhereClause");

		if (whereClauses.size() > 0) {
			sqlBuffer.append(" WHERE " + whereClauses.get(0));
			if (whereClauses.size() > 1) {
				for (int i = 1; i < whereClauses.size(); i++) {
					sqlBuffer.append(" AND " + whereClauses.get(i));
				}
			}
		}
		return sqlBuffer;
	}

	protected static StringBuffer getBaseQuery(Class tableType) {
		return new StringBuffer(" FROM " + tableType.getName());
	}

	public static StringBuffer getLocationIdsString(Vector<Location> locations) {
		logger.debug("getLocationIdsString ");

		StringBuffer buf = new StringBuffer();
		if (locations != null && locations.size() > 0) {
			buf.append(locations.get(0).getId());
			if (locations.size() > 1) {
				for (int i = 1; i < locations.size(); i++) {
					buf.append(", " + locations.get(i).getId() + "");
				}
			}
		}

		return buf;
	}

	public static Vector<Location> getLocations(Vector<String> countries) {
		logger.debug("getLocations : " + countries);

		StringBuffer sqlBuffer = getBaseQuery(Location.class);
		if (null != countries && countries.size() > 0) {
			sqlBuffer.append(" where country in ( ");
			sqlBuffer.append("'" + countries.get(0) + "'");
			if (countries.size() > 1) {
				for (int i = 1; i < countries.size(); i++) {
					sqlBuffer.append(", '" + countries.get(i) + "'");
				}
			}
			sqlBuffer.append(") ");
		}

		return (Vector<Location>) HibernateHelper
				.runQuery(sqlBuffer.toString());
	}

	public static StringBuffer getMessageIdsString(Vector<Message> users) {
		logger.debug("getMessageIdsString");

		StringBuffer buf = new StringBuffer();
		if (users != null && users.size() > 0) {
			buf.append(users.get(0).getId());
			if (users.size() > 1) {
				for (int i = 1; i < users.size(); i++) {
					buf.append(", '" + users.get(i).getId() + "'");
				}
			}
		}
		return buf;
	}

	public static Vector<Message> getMessages(Vector<Message> messages,
			Vector<UsenetUser> users) {
		logger.debug("getMessages");

		StringBuffer sqlBuffer = getBaseQuery(Message.class);
		sqlBuffer.append(" as message ");

		Vector<String> whereClauses = new Vector();
		if (null != users && users.size() > 0) {
			sqlBuffer.append(" left join fetch message.poster as usenetuser ");
			whereClauses.add(" usenetuser.id in ( "
					+ getUsenetUserIdsString(users) + ") ");
		}

		if (null != messages && messages.size() > 0) {
			whereClauses.add(" message.id in ( "
					+ getMessageIdsString(messages) + ") ");
		}

		sqlBuffer = addWhereClause(sqlBuffer, whereClauses);
		return (Vector<Message>) HibernateHelper.runQuery(sqlBuffer.toString());
	}

	public static StringBuffer getNewsgroupIdsString(Vector<NewsGroup> users) {
		logger.debug("getNewsgroupIdsString");

		StringBuffer buf = new StringBuffer();
		if (users != null && users.size() > 0) {
			buf.append(users.get(0).getId());
			if (users.size() > 1) {
				for (int i = 1; i < users.size(); i++) {
					buf.append(", '" + users.get(i).getId() + "'");
				}
			}
		}

		return buf;
	}

	public static Vector<NewsGroup> getNewsGroups(Vector<NewsGroup> newsgroups,
			Vector<Message> messages) {
		logger.debug("getNewsGroups");

		StringBuffer sqlBuffer = getBaseQuery(NewsGroup.class);
		sqlBuffer.append(" as newsgroup ");
		Vector<String> whereClauses = new Vector();
		if (null != messages && messages.size() > 0) {
			sqlBuffer
					.append(" left join  fetch newsgroup.messages as message ");
			whereClauses.add(" message.id in ( "
					+ getMessageIdsString(messages) + ") ");
		}

		if (null != newsgroups && newsgroups.size() > 0) {
			whereClauses.add(" newsgroup.id in ( "
					+ getNewsgroupIdsString(newsgroups) + ") ");
		}

		sqlBuffer = addWhereClause(sqlBuffer, whereClauses);
		return (Vector<NewsGroup>) HibernateHelper.runQuery(sqlBuffer
				.toString());
	}

	public static StringBuffer getTopicIdsString(Vector<Topic> users) {
		logger.debug("getTopicIdsString");

		StringBuffer buf = new StringBuffer();
		if (users != null && users.size() > 0) {
			buf.append(users.get(0).getId());
			if (users.size() > 1) {
				for (int i = 1; i < users.size(); i++) {
					buf.append(", '" + users.get(i).getId() + "'");
				}
			}
		}
		return buf;
	}

	public static Vector<Topic> getTopics(Vector<Topic> topics,
			Vector<Message> messages, Vector<NewsGroup> newsgroups) {
		logger.debug("getTopics");

		StringBuffer sqlBuffer = getBaseQuery(Topic.class);
		sqlBuffer.append(" as topic ");
		Vector<String> whereClauses = new Vector<String>();
		// if (null != messages && messages.size() > 0) {
		// sqlBuffer.append(" left join message ");
		// whereClauses.add(" message.id in ( "
		// + getMessageIdsString(messages) + ") " +
		// " and message.topic = topic");
		// }

		if (null != newsgroups && newsgroups.size() > 0) {
			sqlBuffer.append(" left join fetch topic.newsgroups as newsgroup ");
			whereClauses.add(" newsgroup.id in ( "
					+ getNewsgroupIdsString(newsgroups) + ") ");
		}

		if (null != topics && topics.size() > 0) {
			whereClauses.add(" topic.id in ( " + getTopicIdsString(topics)
					+ ") ");
		}

		sqlBuffer = addWhereClause(sqlBuffer, whereClauses);
		return (Vector<Topic>) HibernateHelper.runQuery(sqlBuffer.toString());
	}

	public static StringBuffer getUsenetUserIdsString(Vector<UsenetUser> users) {
		logger.debug("getUsenetUserIdsString");

		StringBuffer buf = new StringBuffer();
		if (users != null && users.size() > 0) {
			buf.append(users.get(0).getId());
			if (users.size() > 1) {
				for (int i = 1; i < users.size(); i++) {
					buf.append(", '" + users.get(i).getId() + "'");
				}
			}
		}

		return buf;
	}

	public static Vector<UsenetUser> getUsenetUsers(Vector<UsenetUser> users,
			Vector<Location> locations) {
		logger.debug("getUsenetUsers");

		StringBuffer sqlBuffer = getBaseQuery(UsenetUser.class);
		sqlBuffer.append(" as usenetuser ");

		Vector<String> whereClauses = new Vector();
		if (null != locations && locations.size() > 0) {
			sqlBuffer
					.append(" left join fetch usenetuser.location  as location ");
			whereClauses.add(" location.id in ( "
					+ getLocationIdsString(locations) + ") ");
		}

		if (null != users && users.size() > 0) {
			whereClauses.add(" usenetuser.id in ( "
					+ getUsenetUserIdsString(users) + ") ");
		}

		// Query query = HibernateHelper.getHibernateSession();

		sqlBuffer = addWhereClause(sqlBuffer, whereClauses);
		return (Vector<UsenetUser>) HibernateHelper.runQuery(sqlBuffer
				.toString());
	}

	public static void main(String[] args) {

		Vector<Location> locations = testLocations();
		Vector<UsenetUser> users = testUsenetUsers(locations);
		Vector<Message> messages = testMessages(users);
		Vector<NewsGroup> groups = testNewsgroups(messages);
		testTopics(groups);
		logger.warn("DONE");
	}

	/**
	 * @param args
	 * @return
	 */
	public static Vector<Location> testLocations() {
		logger.debug("testLocations");

		logger.warn("ALL COUNTRIES");
		logger.warn(getLocations(null));

		logger.warn("UNITED STATES");
		Vector<String> countries = new Vector<String>();
		countries.add("UNITED STATES");
		Vector<Location> locations = getLocations(countries);
		logger.warn(locations);
		return locations;

	}

	public static Vector<Message> testMessages(Vector<UsenetUser> users) {
		logger.debug("testMessages");

		logger.warn("ALL MESSAGES");
		Vector<Message> messages = null;
		messages = getMessages(messages, users);
		logger.warn(messages);
		return messages;
	}

	private static Vector<NewsGroup> testNewsgroups(Vector<Message> messages) {
		logger.debug("testNewsgroups");

		logger.warn("ALL MESSAGES");
		Vector<NewsGroup> newsgroups = null;
		newsgroups = getNewsGroups(newsgroups, messages);
		logger.warn(newsgroups);
		return newsgroups;
	}

	private static Vector<Topic> testTopics(Vector<NewsGroup> groups) {
		logger.debug("testTopics");

		logger.warn("ALL MESSAGES");
		Vector<Topic> topics = null;
		Vector<Message> messages = null;
		topics = getTopics(topics, messages, groups);
		logger.warn(topics);
		return topics;
	}

	/**
	 * @param locations
	 * @param args
	 * @return
	 */
	public static Vector<UsenetUser> testUsenetUsers(Vector<Location> locations) {
		logger.debug("testUsenetUsers");

		logger.warn("ALL POSTERS");
		Vector<UsenetUser> users = getUsenetUsers(null, locations);
		logger.warn(users);

		// logger.warn("UNITED STATES");
		// logger.warn(getUsenetUsers(new String[] { "UNITED STATES" }));

		return users;
	}

	protected DataQuery() {
	}

}
