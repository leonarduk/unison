/**
 * UNISoNDatabase
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.datahandling;

import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import uk.co.sleonard.unison.NewsGroupFilter;
import uk.co.sleonard.unison.UNISoNController;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;

public class UNISoNDatabase extends Observable {
	private static Logger			logger	= Logger.getLogger(UNISoNDatabase.class);
	private final Session			session;
	private final NewsGroupFilter	filter;
	private final HibernateHelper	helper;

	public UNISoNDatabase(final NewsGroupFilter filter2, final Session session2,
	        final HibernateHelper helper2) {
		this.session = session2;
		this.filter = filter2;
		this.helper = helper2;
	}

	public UNISoNDatabase(final UNISoNController controller) {
		this.session = controller.getSession();
		this.filter = controller.getFilter();
		this.helper = controller.getHelper();
	}

	/**
	 * Gets the messages.
	 *
	 * @param topic
	 *            the topic
	 * @param session1
	 *            the session
	 * @return the messages
	 */
	public Set<Message> getMessages(final Topic topic, final Session session1) {
		final String query = "from  Message  where topic_id = " + topic.getId();
		final HashSet<Message> returnVal = new HashSet<>();
		for (final Message message1 : (List<Message>) this.helper.runQuery(query, session1,
		        Message.class)) {
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

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Observable#notifyObservers()
	 */
	@Override
	public void notifyObservers() {
		this.setChanged();
		super.notifyObservers();
	}

	/**
	 * Refresh data from database.
	 */
	public void refreshDataFromDatabase() {

		UNISoNDatabase.logger.debug("refreshDataFromDatabase");

		this.filter.setMessagesFilter(DataQuery.getInstance().getMessages(
		        this.filter.getSelectedMessages(), this.filter.getSelectedPosters(), this.session,
		        this.filter.getFromDate(), this.filter.getToDate(), this.filter.isFiltered(),
		        this.filter.getSelectedNewsgroups(), this.filter.getSelectedCountries()));

		this.filter.clear();

		for (final Message message1 : this.filter.getMessagesFilter()) {
			UsenetUser poster = null;
			try {
				if (this.session.contains(message1)) {
					this.session.refresh(message1);
				}
			}
			catch (final org.hibernate.UnresolvableObjectException e) {
				UNISoNDatabase.logger.warn(e);
			}
			try {
				poster = message1.getPoster();
				if (this.session.contains(poster)) {
					this.session.refresh(poster);
				}
			}
			catch (final org.hibernate.UnresolvableObjectException e) {
				UNISoNDatabase.logger.warn(e);
			}

			if (!this.filter.getUsenetUsersFilter().contains(poster)) {
				// if (!filtered || null == selectedPosters
				// || selectedPosters.contains(message.getPoster())) {
				this.filter.getUsenetUsersFilter().add(poster);
				String country;
				if ((null != poster) && (null != poster.getLocation())
				        && (null != poster.getLocation().getCountry())) {
					country = poster.getLocation().getCountry();
				}
				else {
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
				}
				catch (final org.hibernate.UnresolvableObjectException e) {
					UNISoNDatabase.logger.warn(e);
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
		this.notifyObservers();
	}

}
