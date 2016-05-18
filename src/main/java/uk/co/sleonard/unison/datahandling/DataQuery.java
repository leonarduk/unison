/*
 * 
 */
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

/**
 * The Class DataQuery.
 */
public class DataQuery {

	/** The logger. */
	private static Logger logger = Logger.getLogger("DataQuery");

	/** The helper. */
	private HibernateHelper helper;

	/**
	 * Instantiates a new data query.
	 */
	protected DataQuery() {
		this(UNISoNController.getInstance().helper());
	}

	/**
	 * Instantiates a new data query.
	 *
	 * @param helper
	 *            the helper
	 */
	public DataQuery(HibernateHelper helper) {
		this.helper = helper;
	}

	/**
	 * Gets the single instance of DataQuery.
	 *
	 * @return single instance of DataQuery
	 */
	public static DataQuery getInstance() {
		return DataQueryHelper.getInstance();
	}

	/**
	 * The Class DataQueryHelper.
	 */
	static class DataQueryHelper {

		/** The instance. */
		static DataQuery instance = new DataQuery();

		/**
		 * Gets the single instance of DataQueryHelper.
		 *
		 * @return single instance of DataQueryHelper
		 */
		static DataQuery getInstance() {
			return instance;
		}
	}

	/**
	 * Adds the where clause.
	 *
	 * @param sqlBuffer
	 *            the sql buffer
	 * @param whereClauses
	 *            the where clauses
	 * @return the string buffer
	 */
	private StringBuffer addWhereClause(final StringBuffer sqlBuffer,
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

	/**
	 * Gets the base query.
	 *
	 * @param tableType
	 *            the table type
	 * @return the base query
	 */
	protected StringBuffer getBaseQuery(final Class<?> tableType) {
		return new StringBuffer(" FROM " + tableType.getName());
	}

	/**
	 * Gets the locations.
	 *
	 * @param countries
	 *            the countries
	 * @param session
	 *            the session
	 * @param filtered
	 *            the filtered
	 * @return the locations
	 */
	@SuppressWarnings("unchecked")
	public Vector<Location> getLocations(final Vector<String> countries, Session session,
	        boolean filtered) {
		DataQuery.logger.debug("getLocations : " + countries);
		if (filtered && null != countries && countries.size() > 0) {
			final StringBuffer sqlBuffer = getLocationsSQL(countries);

			return helper.runQuery(sqlBuffer.toString(), session, Location.class);
		}
		return null;
	}

	/**
	 * Gets the locations sql.
	 *
	 * @param countries
	 *            the countries
	 * @return the locations sql
	 */
	public StringBuffer getLocationsSQL(final Vector<String> countries) {
		final StringBuffer sqlBuffer = getBaseQuery(Location.class);
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
		return sqlBuffer;
	}

	/**
	 * Gets the message ids string.
	 *
	 * @param users
	 *            the users
	 * @return the message ids string
	 */
	public StringBuffer getMessageIdsString(final Vector<Message> users) {
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

	/**
	 * Gets the messages.
	 *
	 * @param messages
	 *            the messages
	 * @param users
	 *            the users
	 * @param session
	 *            the session
	 * @param fromDate
	 *            the from date
	 * @param toDate
	 *            the to date
	 * @param filtered
	 *            the filtered
	 * @param newsgroups
	 *            the newsgroups
	 * @param countries
	 *            the countries
	 * @return the messages
	 */
	@SuppressWarnings("unchecked")
	public Vector<Message> getMessages(final Vector<Message> messages,
	        final Vector<UsenetUser> users, Session session, Date fromDate, Date toDate,
	        boolean filtered, List<NewsGroup> newsgroups, Set<String> countries) {
		DataQuery.logger.debug("getMessages");

		StringBuffer sqlBuffer = getBaseQuery(Message.class);
		sqlBuffer.append(" as message ");

		if (filtered) {
			final Vector<String> whereClauses = new Vector<String>();
			if ((null != users && users.size() > 0)) {
				sqlBuffer.append(" left join fetch message.poster as usenetuser ");
				whereClauses.add(" usenetuser.id in ( " + getUsenetUserIdsString(users) + ") ");
			}

			if ((null != countries && countries.size() > 0)) {
				sqlBuffer.append(" left join fetch message.poster.location as location ");
				whereClauses.add(" location.Country in ('" + join(countries, "','") + "') ");
			}
			if ((null != newsgroups) && (newsgroups.size() > 0)) {
				sqlBuffer.append(" left join fetch message.newsgroups as newsgroup ");
				whereClauses.add(" newsgroup.id in ( " + getNewsGroupIdsString(newsgroups) + ") ");
			}

			if ((null != messages) && (messages.size() > 0)) {
				whereClauses.add(" message.id in ( " + getMessageIdsString(messages) + ") ");
			}

			if (null != fromDate) {
				whereClauses.add(
				        " message.DateCreated >= '" + yyyyMMDDFormatter.format(fromDate) + "'");
			}

			if (null != toDate) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(toDate);
				cal.add(Calendar.DAY_OF_MONTH, 1);

				whereClauses.add(
				        " message.DateCreated < '" + yyyyMMDDFormatter.format(cal.getTime()) + "'");
			}

			if (whereClauses.size() > 0) {
				sqlBuffer = addWhereClause(sqlBuffer, whereClauses);
			}
		}
		return helper.runQuery(sqlBuffer.toString(), session, Message.class);
	}

	/**
	 * Join.
	 *
	 * @param s
	 *            the s
	 * @param delimiter
	 *            the delimiter
	 * @return the string
	 */
	public String join(Collection s, String delimiter) {
		StringBuffer buffer = new StringBuffer();
		Iterator<String> iter = s.iterator();
		while (iter.hasNext()) {
			buffer.append(iter.next());
			if (iter.hasNext()) {
				buffer.append(delimiter);
			}
		}
		return buffer.toString();
	}

	/**
	 * Gets the news group ids string.
	 *
	 * @param newsgroups
	 *            the newsgroups
	 * @return the news group ids string
	 */
	private String getNewsGroupIdsString(List<NewsGroup> newsgroups) {
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

	/** The yyyy mmdd formatter. */
	SimpleDateFormat yyyyMMDDFormatter = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * Gets the usenet user ids string.
	 *
	 * @param users
	 *            the users
	 * @return the usenet user ids string
	 */
	public StringBuffer getUsenetUserIdsString(final Vector<UsenetUser> users) {
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

	/**
	 * Test locations.
	 *
	 * @param session
	 *            the session
	 * @return the vector
	 */
	public Vector<Location> testLocations(Session session) {
		DataQuery.logger.debug("testLocations");

		DataQuery.logger.warn("ALL COUNTRIES");
		DataQuery dataQuery = getInstance();
		DataQuery.logger.warn(dataQuery.getLocations(null, session, true));

		DataQuery.logger.warn("UNITED STATES");
		final Vector<String> countries = new Vector<String>();
		countries.add("UNITED STATES");
		final Vector<Location> locations = dataQuery.getLocations(countries, session, true);
		DataQuery.logger.warn(locations);
		return locations;

	}

}
