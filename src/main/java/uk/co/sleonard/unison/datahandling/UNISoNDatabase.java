/**
 * UNISoNDatabase
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.datahandling;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import uk.co.sleonard.unison.NewsGroupFilter;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;

import java.beans.PropertyChangeSupport;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.co.sleonard.unison.DataChangeListener;

@Slf4j
public class UNISoNDatabase {
    private final Session session;
    private final NewsGroupFilter filter;
    private final HibernateHelper helper;
    private final DataQuery dataQuery;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public UNISoNDatabase(final NewsGroupFilter filter2, final Session session2,
                          final HibernateHelper helper2, final DataQuery dataQuery) {
        this.session = session2;
        this.filter = filter2;
        this.helper = helper2;
        this.dataQuery = dataQuery;
    }

    /**
     * Gets the messages.
     *
     * @param topic    the topic
     * @param session1 the session
     * @return the messages
     */
    public Set<Message> getMessages(final Topic topic, final Session session1) {
        final String query = "from  Message  where topic_id = " + topic.getId();
        final HashSet<Message> returnVal = new HashSet<>();
        final Session effective = (session1 != null) ? session1 : this.session;
        List<Message> results = null;
        try {
            results = this.helper.runQuery(query, effective, Message.class);
        } catch (final Exception e) {
            log.warn("Failed to run query {}", query, e);
            return returnVal;
        }

        if ((results == null) || results.isEmpty()) {
            return returnVal;
        }

        for (final Message message1 : results) {
            if (((null == this.filter.getSelectedMessages())
                    || (this.filter.getSelectedMessages().size() == 0)
                    || this.filter.getSelectedMessages().contains(message1))
                    && ((null == this.filter.getSelectedPosters())
                    || (this.filter.getSelectedPosters().size() == 0)
                    || this.filter.getSelectedPosters().contains(message1.getPoster()))) {
                returnVal.add(message1);
            }
        }

        return returnVal;
    }

    /**
     * Register a listener to be notified when the database data changes.
     *
     * @param listener the listener to register
     */
    public void addDataChangeListener(final DataChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    /**
     * Remove a previously registered listener.
     *
     * @param listener the listener to remove
     */
    public void removeDataChangeListener(final DataChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    /**
     * Notify all registered listeners that the data has changed.
     */
    public void notifyListeners() {
        this.pcs.firePropertyChange("database", null, null);
    }

    /**
     * Refresh data from database.
     */
    public void refreshDataFromDatabase() {
        log.debug("refreshDataFromDatabase");

        this.filter.setMessagesFilter(this.dataQuery.getMessages(this.filter.getSelectedMessages(),
                this.filter.getSelectedPosters(), this.session, this.filter.getFromDate(),
                this.filter.getToDate(), this.filter.isFiltered(),
                this.filter.getSelectedNewsgroups(), this.filter.getSelectedCountries()));

        this.filter.clear();

        for (final Message message1 : this.filter.getMessagesFilter()) {
            UsenetUser poster = null;
            try {
                if (this.session.contains(message1)) {
                    this.session.refresh(message1);
                }
            } catch (final org.hibernate.UnresolvableObjectException e) {
                log.warn("", e);
            }
            try {
                poster = message1.getPoster();
                if (this.session.contains(poster)) {
                    this.session.refresh(poster);
                }
            } catch (final org.hibernate.UnresolvableObjectException e) {
                log.warn("", e);
            }

            if (!this.filter.getUsenetUsersFilter().contains(poster)) {
                // if (!filtered || null == selectedPosters
                // || selectedPosters.contains(message.getPoster())) {
                this.filter.getUsenetUsersFilter().add(poster);
                String country;
                if ((null != poster) && (null != poster.getLocation())
                        && (null != poster.getLocation().getCountry())) {
                    country = poster.getLocation().getCountry();
                } else {
                    country = "UNKNOWN";
                }

                this.filter.getCountriesFilter().add(country);
            }
            if (!this.filter.getTopicsFilter().contains(message1.getTopic())) {
                this.filter.getTopicsFilter().add(message1.getTopic());
            }

            for (NewsGroup group : message1.getNewsgroups()) {
                try {
                    if (this.session.contains(group)) {
                        this.session.refresh(group);
                    }
                } catch (final org.hibernate.UnresolvableObjectException e) {
                    log.warn("", e);
                }

                if (!this.filter.getNewsgroupFilter().contains(group)) {
                    this.filter.getNewsgroupFilter().add(group);
                    while (null != group.getParentNewsGroup()) {
                        group = group.getParentNewsGroup();
                    }
                    this.filter.getTopsNewsgroups().add(group);
                }
            }
        }
        this.notifyListeners();
    }

    @Override
    public String toString() {
        return "UNISoNDatabase [session=" + this.session + ", filter=" + this.filter + ", helper="
                + this.helper + "]";
    }

}
