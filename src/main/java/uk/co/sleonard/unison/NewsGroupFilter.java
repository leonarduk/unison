/**
 * NewsGroupFilter
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;

public class NewsGroupFilter {
	/** The selected messages. */
	private final static Vector<Message>	selectedMessages	= null;
	/** The topics filter. */
	private Set<Topic>						topicsFilter		= null;

	/** The messages filter. */
	private Vector<Message> messagesFilter = null;

	/** The newsgroup filter. */
	private Set<NewsGroup>		newsgroupFilter		= null;
	/** The usenet users filter. */
	private Vector<UsenetUser>	usenetUsersFilter	= null;
	/** The filtered. */
	private boolean				filtered				= false;

	/** The message. */
	private Message message;

	/** The from date. */
	private Date				fromDate;
	/** The to date. */
	private Date				toDate;
	/** The selected newsgroup. */
	private NewsGroup			selectedNewsgroup;
	/** The selected newsgroups. */
	private Vector<NewsGroup>	selectedNewsgroups;
	/** The selected posters. */
	private Vector<UsenetUser>	selectedPosters;

	/** The countries filter. */
	private Set<String> countriesFilter = null;

	/** The tops newsgroups. */
	private Set<NewsGroup> topsNewsgroups;

	/** The selected countries. */
	Set<String>				selectedCountries	= null;
	private final Session	session;

	private final HibernateHelper helper;

	public NewsGroupFilter(final Session session, final HibernateHelper helper) {
		super();
		this.clear();
		this.session = session;
		this.helper = helper;
	}

	public void clear() {
		this.usenetUsersFilter = new Vector<>();
		this.newsgroupFilter = new HashSet<>();
		this.setTopsNewsgroups(new HashSet<>());
		this.topicsFilter = new HashSet<>();
		this.countriesFilter = new HashSet<>();
	}

	public Set<String> getCountriesFilter() {
		return this.countriesFilter;
	}

	public Date getFromDate() {
		return this.fromDate;
	}

	/**
	 * Gets the locations filter.
	 *
	 * @return the locations filter
	 */
	public Set<String> getLocationsFilter() {
		return this.getCountriesFilter();
	}

	/**
	 * Gets the messages filter.
	 *
	 * @return the messages filter
	 */
	public Vector<Message> getMessagesFilter() {
		return this.messagesFilter;
	}

	/**
	 * Gets the newsgroup filter.
	 *
	 * @return the newsgroup filter
	 */
	public Set<NewsGroup> getNewsgroupFilter() {
		return this.newsgroupFilter;
	}

	/**
	 * Gets the selected countries.
	 *
	 * @return the selected countries
	 */
	public Set<String> getSelectedCountries() {
		return this.selectedCountries;
	}

	/**
	 * Gets the selected message.
	 *
	 * @return the selected message
	 */
	public Message getSelectedMessage() {
		if (this.isFiltered() && !this.getMessagesFilter().contains(this.message)) {
			return null;
		}
		return this.message;
	}

	public Vector<Message> getSelectedMessages() {
		return NewsGroupFilter.selectedMessages;
	}

	/**
	 * Gets the selected newsgroup.
	 *
	 * @return the selected newsgroup
	 */
	public NewsGroup getSelectedNewsgroup() {
		if (this.isFiltered() && !this.newsgroupFilter.contains(this.selectedNewsgroup)) {
			return null;
		}
		return this.selectedNewsgroup;
	}

	/**
	 * Gets the selected newsgroups.
	 *
	 * @return the selected newsgroups
	 */
	public Vector<NewsGroup> getSelectedNewsgroups() {
		if (!this.isFiltered()) {
			return null;
		}
		return this.selectedNewsgroups;
	}

	/**
	 * Gets the selected posters.
	 *
	 * @return the selected posters
	 */
	public Vector<UsenetUser> getSelectedPosters() {
		if (!this.isFiltered()) {
			return null;
		}
		return this.selectedPosters;
	}

	public Date getToDate() {
		return this.toDate;
	}

	/**
	 * Gets the topics filter.
	 *
	 * @return the topics filter
	 */
	public Set<Topic> getTopicsFilter() {
		return this.topicsFilter;
	}

	/**
	 * Gets the top news groups.
	 *
	 * @return the top news groups
	 */
	public Set<NewsGroup> getTopNewsGroups() {
		return this.getTopsNewsgroups();
	}

	public Set<NewsGroup> getTopsNewsgroups() {
		return this.topsNewsgroups;
	}

	/**
	 * Gets the usenet users filter.
	 *
	 * @return the usenet users filter
	 */
	public Vector<UsenetUser> getUsenetUsersFilter() {
		return this.usenetUsersFilter;
	}

	/**
	 * Checks if is filtered.
	 *
	 * @return true, if is filtered
	 */
	public boolean isFiltered() {
		return this.filtered;
	}

	/**
	 * Sets the dates.
	 *
	 * @param fromDate2
	 *            the from date2
	 * @param toDate2
	 *            the to date2
	 */
	public void setDates(final Date fromDate2, final Date toDate2) {
		this.setToDate(toDate2);
		this.setFromDate(fromDate2);
	}

	public void setFiltered(final boolean on) {
		this.filtered = on;
	}

	public void setFromDate(final Date fromDate) {
		this.fromDate = fromDate;
	}

	public void setMessagesFilter(final Vector<Message> messagesFilter) {
		this.messagesFilter = messagesFilter;
	}

	/**
	 * Sets the selected countries.
	 *
	 * @param countries
	 *            the new selected countries
	 */
	public void setSelectedCountries(final Set<String> countries) {
		this.selectedCountries = countries;
	}

	/**
	 * Sets the selected message.
	 *
	 * @param message
	 *            the new selected message
	 */
	public void setSelectedMessage(final Message message) {
		this.message = message;
	}

	/**
	 * Sets the selected newsgroup.
	 *
	 * @param group
	 *            the new selected newsgroup
	 */
	public void setSelectedNewsgroup(final NewsGroup group) {
		this.selectedNewsgroup = group;
		// this.frame.setSelectedNewsgroup(group);
	}

	/**
	 * Sets the selected newsgroup.
	 *
	 * @param groupName
	 *            the new selected newsgroup
	 */
	public void setSelectedNewsgroup(final String groupName) {
		NewsGroup group = null;
		if (!StringUtils.isEmpty(groupName)) {
			group = this.helper.getNewsgroupByFullName(groupName, this.session);
		}
		this.setSelectedNewsgroup(group);
	}

	/**
	 * Sets the selected newsgroups.
	 *
	 * @param groups
	 *            the new selected newsgroups
	 */
	public void setSelectedNewsgroups(final Vector<NewsGroup> groups) {
		this.selectedNewsgroups = groups;
	}

	/**
	 * Sets the selected posters.
	 *
	 * @param posters
	 *            the new selected posters
	 */
	public void setSelectedPosters(final Vector<UsenetUser> posters) {
		this.selectedPosters = posters;
	}

	public void setToDate(final Date toDate) {
		this.toDate = toDate;
	}

	public void setTopsNewsgroups(final Set<NewsGroup> topsNewsgroups) {
		this.topsNewsgroups = topsNewsgroups;
	}
}
