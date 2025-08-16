/**
 * NewsGroupFilter
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.datahandling.HibernateHelper;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import lombok.Data;

@Data
public class NewsGroupFilter {
    /**
     * The selected messages.
     */
    private Vector<Message> selectedMessages = null;
    /**
     * The topics filter.
     */
    private Set<Topic> topicsFilter = null;

    /**
     * The messages filter.
     */
    private Vector<Message> messagesFilter = null;

    /**
     * The newsgroup filter.
     */
    private Set<NewsGroup> newsgroupFilter = null;
    /**
     * The usenet users filter.
     */
    private Vector<UsenetUser> usenetUsersFilter = null;
    /**
     * The filtered.
     */
    private boolean filtered = false;

    /**
     * The message.
     */
    private Message message;

    /**
     * The from date.
     */
    private Date fromDate;
    /**
     * The to date.
     */
    private Date toDate;
    /**
     * The selected newsgroup.
     */
    private NewsGroup selectedNewsgroup;
    /**
     * The selected newsgroups.
     */
    private Vector<NewsGroup> selectedNewsgroups;
    /**
     * The selected posters.
     */
    private Vector<UsenetUser> selectedPosters;

    /**
     * The countries filter.
     */
    private Set<String> countriesFilter = null;

    /**
     * The tops newsgroups.
     */
    private Set<NewsGroup> topsNewsgroups;

    /**
     * The selected countries.
     */
    private Set<String> selectedCountries = null;
    private final Session session;

    private final HibernateHelper helper;

    NewsGroupFilter(final Session session, final HibernateHelper helper) {
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
        this.messagesFilter = new Vector<>();
        this.selectedPosters = new Vector<>();
        this.selectedCountries = new HashSet<>();
        this.selectedNewsgroups = new Vector<>();
        this.selectedMessages = new Vector<>();
    }
    public Message getSelectedMessage() {
        if (this.isFiltered() && !this.getMessagesFilter().contains(this.message)) {
            return null;
        }
        return this.message;
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

    /**
     * Sets the dates.
     *
     * @param fromDate2 the from date2
     * @param toDate2   the to date2
     */
    public void setDates(final Date fromDate2, final Date toDate2) {
        this.setToDate(toDate2);
        this.setFromDate(fromDate2);
    }

    /**
     * Sets the selected newsgroup.
     *
     * @param groupName the new selected newsgroup
     */
    public void setSelectedNewsgroup(final String groupName) {
        if (!StringUtils.isEmpty(groupName)) {
            NewsGroup group = this.helper.getNewsgroupByFullName(groupName, this.session);
            this.setSelectedNewsgroup(group.getName());
        }
    }
}
