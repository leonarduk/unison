package uk.co.sleonard.unison.datahandling;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import uk.co.sleonard.unison.datahandling.DAO.Location;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.gui.UNISoNController;

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

	@SuppressWarnings("unchecked")
	public static Vector<Location> getLocations(final Vector<String> countries,
			Session session, boolean filtered) {
		DataQuery.logger.debug("getLocations : " + countries);
		if (filtered && null != countries && countries.size() > 0) {
			final StringBuffer sqlBuffer = DataQuery
					.getBaseQuery(Location.class);
			if ((null != countries)) {
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
					.runQuery(sqlBuffer.toString(), session);
		}
		return null;
	}

	public static StringBuffer getMessageIdsString(final Vector<Message> users) {
		DataQuery.logger.debug("getMessageIdsString");

		final StringBuffer buf = new StringBuffer();
		if ((users != null) && (users.size() > 0)) {
			buf.append("'" + users.get(0).getId() + "'");
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
			final Vector<UsenetUser> users, Session session, Date fromDate,
			Date toDate, boolean filtered, List<NewsGroup> newsgroups,
			Set<String> countries) {
		DataQuery.logger.debug("getMessages");

		StringBuffer sqlBuffer = DataQuery.getBaseQuery(Message.class);
		sqlBuffer.append(" as message ");

		if (filtered) {
			final Vector<String> whereClauses = new Vector<String>();
			if ((null != users && users.size() > 0)) {
				sqlBuffer
						.append(" left join fetch message.poster as usenetuser ");
				whereClauses.add(" usenetuser.id in ( "
						+ DataQuery.getUsenetUserIdsString(users) + ") ");
			}

			if ((null != countries && countries.size() > 0)) {
				sqlBuffer
						.append(" left join fetch message.poster.location as location ");
				whereClauses.add(" location.Country in ('"
						+ join(countries, "','") + "') ");
			}
			if ((null != newsgroups) && (newsgroups.size() > 0)) {
				sqlBuffer
						.append(" left join fetch message.newsgroups as newsgroup ");
				whereClauses.add(" newsgroup.id in ( "
						+ DataQuery.getNewsGroupIdsString(newsgroups) + ") ");
			}

			if ((null != messages) && (messages.size() > 0)) {
				whereClauses.add(" message.id in ( "
						+ DataQuery.getMessageIdsString(messages) + ") ");
			}

			if (null != fromDate) {
				whereClauses.add(" message.DateCreated >= '"
						+ yyyyMMDDFormatter.format(fromDate) + "'");
			}

			if (null != toDate) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(toDate);
				cal.add(Calendar.DAY_OF_MONTH, 1);

				whereClauses.add(" message.DateCreated < '"
						+ yyyyMMDDFormatter.format(cal.getTime()) + "'");
			}

			if (whereClauses.size() > 0) {
				sqlBuffer = DataQuery.addWhereClause(sqlBuffer, whereClauses);
			}
		}
		return (Vector<Message>) UNISoNController.getInstance().helper()
				.runQuery(sqlBuffer.toString(), session);
	}

	public static String join(Collection s, String delimiter) {
		StringBuffer buffer = new StringBuffer();
		Iterator iter = s.iterator();
		while (iter.hasNext()) {
			buffer.append(iter.next());
			if (iter.hasNext()) {
				buffer.append(delimiter);
			}
		}
		return buffer.toString();
	}

	private static String getNewsGroupIdsString(List<NewsGroup> newsgroups) {
		final StringBuffer buf = new StringBuffer();
		if ((newsgroups != null) && (newsgroups.size() > 0)) {
			buf.append("'" + newsgroups.get(0).getId() + "'");
			if (newsgroups.size() > 1) {
				for (int i = 1; i < newsgroups.size(); i++) {
					buf.append(", '" + newsgroups.get(i).getId() + "'");
				}
			}
		}
		return buf.toString();
	}

	static SimpleDateFormat yyyyMMDDFormatter = new SimpleDateFormat(
			"yyyy-MM-dd");

	public static StringBuffer getUsenetUserIdsString(
			final Vector<UsenetUser> users) {
		DataQuery.logger.debug("getUsenetUserIdsString");

		final StringBuffer buf = new StringBuffer();
		if ((users != null) && (users.size() > 0)) {
			buf.append("'" + users.get(0).getId() + "'");
			if (users.size() > 1) {
				for (int i = 1; i < users.size(); i++) {
					buf.append(", '" + users.get(i).getId() + "'");
				}
			}
		}

		return buf;
	}

	public static void main(final String[] args) {

		// Session session = null;
		// final Vector<Location> locations = DataQuery.testLocations(session);
		// final Vector<UsenetUser> users = DataQuery.testUsenetUsers(locations,
		// session);
		// final Vector<Message> messages = DataQuery.testMessages(users,
		// session);
		// final Vector<NewsGroup> groups = DataQuery.testNewsgroups(messages,
		// session);
		// DataQuery.testTopics(groups, session);
		DataQuery.logger.warn("DONE");
	}

	/**
	 * @param session
	 * @param args
	 * @return
	 */
	public static Vector<Location> testLocations(Session session) {
		DataQuery.logger.debug("testLocations");

		DataQuery.logger.warn("ALL COUNTRIES");
		DataQuery.logger.warn(DataQuery.getLocations(null, session, true));

		DataQuery.logger.warn("UNITED STATES");
		final Vector<String> countries = new Vector<String>();
		countries.add("UNITED STATES");
		final Vector<Location> locations = DataQuery.getLocations(countries,
				session, true);
		DataQuery.logger.warn(locations);
		return locations;

	}

	protected DataQuery() {
	}

}
