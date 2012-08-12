package uk.co.sleonard.unison.gui;

import java.util.Vector;

import org.apache.log4j.Logger;

import uk.co.sleonard.unison.datahandling.DAO.Location;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;

public class DataQuery {
	private static Logger logger = Logger.getLogger("DataQuery");

	private static StringBuffer addWhereClause(final StringBuffer sqlBuffer,
			final Vector<String> whereClauses) {
		DataQuery.logger.debug("addWhereClause");

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

	protected static StringBuffer getBaseQuery(final Class<?> tableType) {
		return new StringBuffer(" FROM " + tableType.getName());
	}

	public static StringBuffer getLocationIdsString(
			final Vector<Location> locations) {
		DataQuery.logger.debug("getLocationIdsString ");

		final StringBuffer buf = new StringBuffer();
		if ((locations != null) && (locations.size() > 0)) {
			buf.append(locations.get(0).getId());
			if (locations.size() > 1) {
				for (int i = 1; i < locations.size(); i++) {
					buf.append(", " + locations.get(i).getId() + "");
				}
			}
		}

		return buf;
	}

	@SuppressWarnings("unchecked")
	public static Vector<Location> getLocations(final Vector<String> countries) {
		DataQuery.logger.debug("getLocations : " + countries);

		final StringBuffer sqlBuffer = DataQuery.getBaseQuery(Location.class);
		if ((null != countries) && (countries.size() > 0)) {
			sqlBuffer.append(" where country in ( ");
			sqlBuffer.append("'" + countries.get(0) + "'");
			if (countries.size() > 1) {
				for (int i = 1; i < countries.size(); i++) {
					sqlBuffer.append(", '" + countries.get(i) + "'");
				}
			}
			sqlBuffer.append(") ");
		}

		return (Vector<Location>) UNISoNController.getInstance().helper()
				.runQuery(sqlBuffer.toString());
	}

	public static StringBuffer getMessageIdsString(final Vector<Message> users) {
		DataQuery.logger.debug("getMessageIdsString");

		final StringBuffer buf = new StringBuffer();
		if ((users != null) && (users.size() > 0)) {
			buf.append(users.get(0).getId());
			if (users.size() > 1) {
				for (int i = 1; i < users.size(); i++) {
					buf.append(", '" + users.get(i).getId() + "'");
				}
			}
		}
		return buf;
	}

	@SuppressWarnings("unchecked")
	public static Vector<Message> getMessages(final Vector<Message> messages,
			final Vector<UsenetUser> users) {
		DataQuery.logger.debug("getMessages");

		StringBuffer sqlBuffer = DataQuery.getBaseQuery(Message.class);
		sqlBuffer.append(" as message ");

		final Vector<String> whereClauses = new Vector<String>();
		if ((null != users) && (users.size() > 0)) {
			sqlBuffer.append(" left join fetch message.poster as usenetuser ");
			whereClauses.add(" usenetuser.id in ( "
					+ DataQuery.getUsenetUserIdsString(users) + ") ");
		}

		if ((null != messages) && (messages.size() > 0)) {
			whereClauses.add(" message.id in ( "
					+ DataQuery.getMessageIdsString(messages) + ") ");
		}

		sqlBuffer = DataQuery.addWhereClause(sqlBuffer, whereClauses);
		return (Vector<Message>) UNISoNController.getInstance().helper()
				.runQuery(sqlBuffer.toString());
	}

	public static StringBuffer getNewsgroupIdsString(
			final Vector<NewsGroup> groups) {
		DataQuery.logger.debug("getNewsgroupIdsString");

		final StringBuffer buf = new StringBuffer();
		if ((groups != null) && (groups.size() > 0)) {
			buf.append(groups.get(0).getId());
			if (groups.size() > 1) {
				for (int i = 1; i < groups.size(); i++) {
					buf.append(", '" + groups.get(i).getId() + "'");
				}
			}
		}

		return buf;
	}

	@SuppressWarnings("unchecked")
	public static Vector<NewsGroup> getNewsGroups(
			final Vector<NewsGroup> newsgroups, final Vector<Message> messages) {
		DataQuery.logger.debug("getNewsGroups");

		StringBuffer sqlBuffer = DataQuery.getBaseQuery(NewsGroup.class);
		sqlBuffer.append(" as newsgroup ");
		final Vector<String> whereClauses = new Vector<String>();
		if ((null != messages) && (messages.size() > 0)) {
			sqlBuffer
					.append(" left join  fetch newsgroup.messages as message ");
			whereClauses.add(" message.id in ( "
					+ DataQuery.getMessageIdsString(messages) + ") ");
		}

		if ((null != newsgroups) && (newsgroups.size() > 0)) {
			whereClauses.add(" newsgroup.id in ( "
					+ DataQuery.getNewsgroupIdsString(newsgroups) + ") ");
		}

		sqlBuffer = DataQuery.addWhereClause(sqlBuffer, whereClauses);
		return (Vector<NewsGroup>) UNISoNController.getInstance().helper()
				.runQuery(sqlBuffer.toString());
	}

	public static StringBuffer getTopicIdsString(final Vector<Topic> users) {
		DataQuery.logger.debug("getTopicIdsString");

		final StringBuffer buf = new StringBuffer();
		if ((users != null) && (users.size() > 0)) {
			buf.append(users.get(0).getId());
			if (users.size() > 1) {
				for (int i = 1; i < users.size(); i++) {
					buf.append(", '" + users.get(i).getId() + "'");
				}
			}
		}
		return buf;
	}

	@SuppressWarnings("unchecked")
	public static Vector<Topic> getTopics(final Vector<Topic> topics,
			final Vector<Message> messages, final Vector<NewsGroup> newsgroups) {
		DataQuery.logger.debug("getTopics");

		StringBuffer sqlBuffer = DataQuery.getBaseQuery(Topic.class);
		sqlBuffer.append(" as topic ");
		final Vector<String> whereClauses = new Vector<String>();
		// if (null != messages && messages.size() > 0) {
		// sqlBuffer.append(" left join message ");
		// whereClauses.add(" message.id in ( "
		// + getMessageIdsString(messages) + ") " +
		// " and message.topic = topic");
		// }

		if ((null != newsgroups) && (newsgroups.size() > 0)) {
			sqlBuffer.append(" left join fetch topic.newsgroups as newsgroup ");
			whereClauses.add(" newsgroup.id in ( "
					+ DataQuery.getNewsgroupIdsString(newsgroups) + ") ");
		}

		if ((null != topics) && (topics.size() > 0)) {
			whereClauses.add(" topic.id in ( "
					+ DataQuery.getTopicIdsString(topics) + ") ");
		}

		sqlBuffer = DataQuery.addWhereClause(sqlBuffer, whereClauses);
		return (Vector<Topic>) UNISoNController.getInstance().helper()
				.runQuery(sqlBuffer.toString());
	}

	public static StringBuffer getUsenetUserIdsString(
			final Vector<UsenetUser> users) {
		DataQuery.logger.debug("getUsenetUserIdsString");

		final StringBuffer buf = new StringBuffer();
		if ((users != null) && (users.size() > 0)) {
			buf.append(users.get(0).getId());
			if (users.size() > 1) {
				for (int i = 1; i < users.size(); i++) {
					buf.append(", '" + users.get(i).getId() + "'");
				}
			}
		}

		return buf;
	}

	@SuppressWarnings("unchecked")
	public static Vector<UsenetUser> getUsenetUsers(
			final Vector<UsenetUser> users, final Vector<Location> locations) {
		DataQuery.logger.debug("getUsenetUsers");

		StringBuffer sqlBuffer = DataQuery.getBaseQuery(UsenetUser.class);
		sqlBuffer.append(" as usenetuser ");

		final Vector<String> whereClauses = new Vector<String>();
		if ((null != locations) && (locations.size() > 0)) {
			sqlBuffer
					.append(" left join fetch usenetuser.location  as location ");
			whereClauses.add(" location.id in ( "
					+ DataQuery.getLocationIdsString(locations) + ") ");
		}

		if ((null != users) && (users.size() > 0)) {
			whereClauses.add(" usenetuser.id in ( "
					+ DataQuery.getUsenetUserIdsString(users) + ") ");
		}

		// Query query = helper.getHibernateSession();

		sqlBuffer = DataQuery.addWhereClause(sqlBuffer, whereClauses);
		return (Vector<UsenetUser>) UNISoNController.getInstance().helper()
				.runQuery(sqlBuffer.toString());
	}

	public static void main(final String[] args) {

		final Vector<Location> locations = DataQuery.testLocations();
		final Vector<UsenetUser> users = DataQuery.testUsenetUsers(locations);
		final Vector<Message> messages = DataQuery.testMessages(users);
		final Vector<NewsGroup> groups = DataQuery.testNewsgroups(messages);
		DataQuery.testTopics(groups);
		DataQuery.logger.warn("DONE");
	}

	/**
	 * @param args
	 * @return
	 */
	public static Vector<Location> testLocations() {
		DataQuery.logger.debug("testLocations");

		DataQuery.logger.warn("ALL COUNTRIES");
		DataQuery.logger.warn(DataQuery.getLocations(null));

		DataQuery.logger.warn("UNITED STATES");
		final Vector<String> countries = new Vector<String>();
		countries.add("UNITED STATES");
		final Vector<Location> locations = DataQuery.getLocations(countries);
		DataQuery.logger.warn(locations);
		return locations;

	}

	public static Vector<Message> testMessages(final Vector<UsenetUser> users) {
		DataQuery.logger.debug("testMessages");

		DataQuery.logger.warn("ALL MESSAGES");
		Vector<Message> messages = null;
		messages = DataQuery.getMessages(messages, users);
		DataQuery.logger.warn(messages);
		return messages;
	}

	private static Vector<NewsGroup> testNewsgroups(
			final Vector<Message> messages) {
		DataQuery.logger.debug("testNewsgroups");

		DataQuery.logger.warn("ALL MESSAGES");
		Vector<NewsGroup> newsgroups = null;
		newsgroups = DataQuery.getNewsGroups(newsgroups, messages);
		DataQuery.logger.warn(newsgroups);
		return newsgroups;
	}

	private static Vector<Topic> testTopics(final Vector<NewsGroup> groups) {
		DataQuery.logger.debug("testTopics");

		DataQuery.logger.warn("ALL MESSAGES");
		Vector<Topic> topics = null;
		final Vector<Message> messages = null;
		topics = DataQuery.getTopics(topics, messages, groups);
		DataQuery.logger.warn(topics);
		return topics;
	}

	/**
	 * @param locations
	 * @param args
	 * @return
	 */
	public static Vector<UsenetUser> testUsenetUsers(
			final Vector<Location> locations) {
		DataQuery.logger.debug("testUsenetUsers");

		DataQuery.logger.warn("ALL POSTERS");
		final Vector<UsenetUser> users = DataQuery.getUsenetUsers(null,
				locations);
		DataQuery.logger.warn(users);

		// logger.warn("UNITED STATES");
		// logger.warn(getUsenetUsers(new String[] { "UNITED STATES" }));

		return users;
	}

	protected DataQuery() {
	}

}
