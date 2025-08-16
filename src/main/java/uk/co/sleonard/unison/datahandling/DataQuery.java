/*
 *
 */
package uk.co.sleonard.unison.datahandling;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import uk.co.sleonard.unison.datahandling.DAO.Location;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The Class DataQuery.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
@Slf4j
public class DataQuery {

	/** The helper. */
	private final HibernateHelper helper;

	/** The yyyy mmdd formatter. */
	private final SimpleDateFormat yyyyMMDDFormatter = new SimpleDateFormat("yyyy-MM-dd");

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
		log.debug("addWhereClause");

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
	private StringBuffer getBaseQuery(final Class<?> tableType) {
		return new StringBuffer(" FROM " + tableType.getName());
	}

	/**
	 * Gets the locations sql.
	 *
	 * @param countries
	 *            the countries
	 * @return the locations sql
	 */
	StringBuffer getLocationsSQL(final Vector<String> countries) {
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
	StringBuffer getMessageIdsString(final Vector<Message> users) {
		log.debug("getMessageIdsString");

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
	public Vector<Message> getMessages(final Vector<Message> messages,
	        final Vector<UsenetUser> users, final Session session, final Date fromDate,
	        final Date toDate, final boolean filtered, final List<NewsGroup> newsgroups,
	        final Set<String> countries) {
		log.debug("getMessages");

		StringBuffer sqlBuffer = this.getBaseQuery(Message.class);
		sqlBuffer.append(" as message ");

		if (filtered) {
			final Vector<String> whereClauses = new Vector<>();
			if ((null != users) && (users.size() > 0)) {
				sqlBuffer.append(" left join fetch message.poster as usenetuser ");
				whereClauses
				        .add(" usenetuser.id in ( " + this.getUsenetUserIdsString(users) + ") ");
			}

			if ((null != countries) && (countries.size() > 0)) {
				sqlBuffer.append(" left join fetch message.poster.location as location ");
				whereClauses.add(" location.Country in ('" + this.join(countries, "','") + "') ");
			}
			if ((null != newsgroups) && (newsgroups.size() > 0)) {
				sqlBuffer.append(" left join fetch message.newsgroups as newsgroup ");
				whereClauses
				        .add(" newsgroup.id in ( " + this.getNewsGroupIdsString(newsgroups) + ") ");
			}

			if ((null != messages) && (messages.size() > 0)) {
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

	/**
	 * Gets the usenet user ids string.
	 *
	 * @param users
	 *            the users
	 * @return the usenet user ids string
	 */
	StringBuffer getUsenetUserIdsString(final Vector<UsenetUser> users) {
		log.debug("getUsenetUserIdsString");

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
	 * Join.
	 *
	 * @param s
	 *            the s
	 * @param delimiter
	 *            the delimiter
	 * @return the string
	 */
	String join(final Collection<String> s, final String delimiter) {
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
}
