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
			return DataQueryHelper.instance;
		}
	}

	/** The logger. */
	private static Logger logger = Logger.getLogger("DataQuery");

	/**
	 * Gets the single instance of DataQuery.
	 *
	 * @return single instance of DataQuery
	 */
	public static DataQuery getInstance() {
		return DataQueryHelper.getInstance();
	}

	/** The helper. */
	private final HibernateHelper helper;

	/** The yyyy mmdd formatter. */
	SimpleDateFormat yyyyMMDDFormatter = new SimpleDateFormat("yyyy-MM-dd");

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
	public DataQuery(final HibernateHelper helper) {
		this.helper = helper;
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
	public Vector<Location> getLocations(final Vector<String> countries, final Session session,
	        final boolean filtered) {
		DataQuery.logger.debug("getLocations : " + countries);
		if (filtered && null != countries && countries.size() > 0) {
			final StringBuffer sqlBuffer = this.getLocationsSQL(countries);

			return this.helper.runQuery(sqlBuffer.toString(), session, Location.class);
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
		final StringBuffer sqlBuffer = this.getBaseQuery(Location.class);
		if (null != countries) {
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
		if (users != null && users.size() > 0) {
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
	        final Vector<UsenetUser> users, final Session session, final Date fromDate,
	        final Date toDate, final boolean filtered, final List<NewsGroup> newsgroups,
	        final Set<String> countries) {
		DataQuery.logger.debug("getMessages");

		StringBuffer sqlBuffer = this.getBaseQuery(Message.class);
		sqlBuffer.append(" as message ");

		if (filtered) {
			final Vector<String> whereClauses = new Vector<String>();
			if (null != users && users.size() > 0) {
				sqlBuffer.append(" left join fetch message.poster as usenetuser ");
				whereClauses
				        .add(" usenetuser.id in ( " + this.getUsenetUserIdsString(users) + ") ");
			}

			if (null != countries && countries.size() > 0) {
				sqlBuffer.append(" left join fetch message.poster.location as location ");
				whereClauses.add(" location.Country in ('" + this.join(countries, "','") + "') ");
			}
			if (null != newsgroups && newsgroups.size() > 0) {
				sqlBuffer.append(" left join fetch message.newsgroups as newsgroup ");
				whereClauses
				        .add(" newsgroup.id in ( " + this.getNewsGroupIdsString(newsgroups) + ") ");
			}

			if (null != messages && messages.size() > 0) {
				whereClauses.add(" message.id in ( " + this.getMessageIdsString(messages) + ") ");
			}

			if (null != fromDate) {
				whereClauses.add(" message.DateCreated >= '"
				        + this.yyyyMMDDFormatter.format(fromDate) + "'");
			}

			if (null != toDate) {
				final Calendar cal = Calendar.getInstance();
				cal.setTime(toDate);
				cal.add(Calendar.DAY_OF_MONTH, 1);

				whereClauses.add(" message.DateCreated < '"
				        + this.yyyyMMDDFormatter.format(cal.getTime()) + "'");
			}

			if (whereClauses.size() > 0) {
				sqlBuffer = this.addWhereClause(sqlBuffer, whereClauses);
			}
		}
		return this.helper.runQuery(sqlBuffer.toString(), session, Message.class);
	}

	/**
	 * Gets the news group ids string.
	 *
	 * @param newsgroups
	 *            the newsgroups
	 * @return the news group ids string
	 */
	private String getNewsGroupIdsString(final List<NewsGroup> newsgroups) {
		final StringBuffer buf = new StringBuffer();
		if (newsgroups != null && newsgroups.size() > 0) {
			buf.append("'" + newsgroups.get(0).getId() + "'");
			if (newsgroups.size() > 1) {
				for (int i = 1; i < newsgroups.size(); i++) {
					buf.append(", '" + newsgroups.get(i).getId() + "'");
				}
			}
		}
		return buf.toString();
	}

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
		if (users != null && users.size() > 0) {
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
	 * Join.
	 *
	 * @param s
	 *            the s
	 * @param delimiter
	 *            the delimiter
	 * @return the string
	 */
	public String join(final Collection s, final String delimiter) {
		final StringBuffer buffer = new StringBuffer();
		final Iterator<String> iter = s.iterator();
		while (iter.hasNext()) {
			buffer.append(iter.next());
			if (iter.hasNext()) {
				buffer.append(delimiter);
			}
		}
		return buffer.toString();
	}

	/**
	 * Test locations.
	 *
	 * @param session
	 *            the session
	 * @return the vector
	 */
	public Vector<Location> testLocations(final Session session) {
		DataQuery.logger.debug("testLocations");

		DataQuery.logger.warn("ALL COUNTRIES");
		final DataQuery dataQuery = DataQuery.getInstance();
		DataQuery.logger.warn(dataQuery.getLocations(null, session, true));

		DataQuery.logger.warn("UNITED STATES");
		final Vector<String> countries = new Vector<String>();
		countries.add("UNITED STATES");
		final Vector<Location> locations = dataQuery.getLocations(countries, session, true);
		DataQuery.logger.warn(locations);
		return locations;

	}

}
