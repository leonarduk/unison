/**
 * HibernateHelper
 *
 * @author Stephen <github@leonarduk.com>
 * @since 22-May-2016
 */
package uk.co.sleonard.unison.datahandling;

import java.io.File;
import java.io.IOException;
import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import javax.naming.NamingException;
import javax.swing.JOptionPane;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.NonUniqueResultException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hsqldb.util.DatabaseManagerSwing;

import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DAO.EmailAddress;
import uk.co.sleonard.unison.datahandling.DAO.IpAddress;
import uk.co.sleonard.unison.datahandling.DAO.Location;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.ResultRow;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.gui.UNISoNGUI;
import uk.co.sleonard.unison.input.LocationFinder;
import uk.co.sleonard.unison.input.LocationFinderImpl;
import uk.co.sleonard.unison.input.NewsArticle;
import uk.co.sleonard.unison.utils.StringUtils;

/**
 * This is one of the the most important classes as it helps persist the data to the HSQL database.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
public class HibernateHelper {

	/** The Constant DB_URL. */
	// private final static String DB_URL = "jdbc:hsqldb:file:DB/projectDB";
	private final static String DB_URL = "jdbc:hsqldb:file:src/main/resources/DB/projectDB";

	/** The Constant dbDriver. */
	private final static String dbDriver = "org.hsqldb.jdbcDriver";

	/** The Constant dbUser. */
	private final static String dbUser = "sa";

	/** The first connect. */
	private static boolean firstConnect = true;

	/** The Constant GUI_ARGS. */
	public static final String GUI_ARGS[] = { "-driver", HibernateHelper.dbDriver, "-url",
	        HibernateHelper.DB_URL, "-user", HibernateHelper.dbUser };

	/** The Constant HIBERNATE_CONNECTION_URL. */
	private static final String HIBERNATE_CONNECTION_URL = "hibernate.connection.url";

	/** The logger. */
	private static Logger logger = Logger.getLogger("HibernateHelper");

	/** The session factory. */
	private static SessionFactory sessionFactory = null;

	private final LocationFinder locationFinder;

	private final UNISoNGUI gui;

	/**
	 * This opens up a SQL client to access the database directly.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {
		final HibernateHelper helper = new HibernateHelper(null);
		helper.generateSchema();
		DatabaseManagerSwing.main(HibernateHelper.GUI_ARGS);
	}

	/**
	 * Instantiates a new hibernate helper.
	 *
	 * @param controller
	 *            the controller
	 */
	public HibernateHelper(final UNISoNGUI gui) {
		this.gui = gui;
		this.locationFinder = new LocationFinderImpl();
	}

	/**
	 * Creates the location.
	 *
	 * @param article
	 *            the article
	 * @param session
	 *            the session
	 * @return the location
	 */
	private synchronized Location createLocation(final NewsArticle article, final Session session) {
		Location location;
		final IpAddress ip = this.findOrCreateIpAddress(article, session);
		location = ip.getLocation();
		if (null == location) {
			// create location to find city (does web lookup)
			location = this.findOrCreateLocation(session, ip);
		}
		return location;
	}

	/**
	 * Creates the message.
	 *
	 * @param article
	 *            the article
	 * @param topic
	 *            the topic
	 * @param poster
	 *            the poster
	 * @return the message
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	Message createMessage(final NewsArticle article, final Topic topic, final UsenetUser poster)
	        throws UNISoNException {
		Message message;
		byte[] body = null;
		if (null != article.getContent()) {
			// As messages can be quite large, we compress them
			try {
				body = StringUtils.compress(article.getContent().toString());
			}
			catch (final IOException e) {
				throw new UNISoNException("Failed to compress message", e);
			}
		}

		message = new Message(article.getDate(), article.getArticleId(), article.getSubject(),
		        poster, topic, null, article.getReferences(), body);
		return message;
	}

	/**
	 * Creates the usenet user.
	 *
	 * @param article
	 *            the article
	 * @param session
	 *            the session
	 * @param location
	 *            the location
	 * @param gender
	 *            the gender
	 * @return the usenet user
	 */
	private synchronized UsenetUser createUsenetUser(final NewsArticle article,
	        final Session session, final Location locationInput, final String gender) {
		Location location = locationInput;
		// Add location details if got all details
		if (article.isFullHeader()) {
			location = this.createLocation(article, session);
		}
		final UsenetUser poster = this.findOrCreateUsenetUser(article, session, gender);
		if ((null == poster.getLocation()) || !poster.getLocation().equals(location)) {
			poster.setLocation(location);
			poster.setLocation(location);
			session.saveOrUpdate(poster);
		}
		return poster;
	}

	/**
	 * Find by key.
	 *
	 * @param key
	 *            the key
	 * @param session
	 *            the session
	 * @param objclass
	 *            the objclass
	 * @return the object
	 * @throws HibernateException
	 *             the hibernate exception @+ aram objclass
	 */
	public Object findByKey(final String key, final Session session, final Class<?> objclass)
	        throws HibernateException {

		final String queryText = objclass.getName() + ".findByKey";
		final Query query = session.getNamedQuery(queryText);
		query.setString("key", key);
		Object uniqueResult = null;
		try {
			uniqueResult = query.uniqueResult();

		}
		catch (final NonUniqueResultException e) {
			throw new RuntimeException(
			        "Got non-unique result for " + key + " on " + objclass.getName() + " " + e);
		}
		return uniqueResult;
	}

	Message findMessage(final Message aMessage, final Session session) {
		Message message;
		message = (Message) this.findByKey(aMessage.getUsenetMessageID(), session, Message.class);
		return message;
	}

	/**
	 * Find or create ip address.
	 *
	 * @param article
	 *            the article
	 * @param session
	 *            the session
	 * @return the ip address
	 */
	private synchronized IpAddress findOrCreateIpAddress(final NewsArticle article,
	        final Session session) {
		// If user is new, then location might be too
		IpAddress ip = (IpAddress) this.findByKey(article.getPostingHost(), session,
		        IpAddress.class);

		if (null == ip) {
			ip = new IpAddress(article.getPostingHost(), null);
			session.saveOrUpdate(ip);
		}
		return ip;
	}

	/**
	 * Find or create location.
	 *
	 * @param session
	 *            the session
	 * @param ipAddress
	 *            the ip address
	 * @return the location
	 */
	synchronized Location findOrCreateLocation(final Session session, final IpAddress ipAddress) {
		Location location;
		location = this.locationFinder.createLocation(ipAddress.getIpAddress());

		// find if location was already created for another
		// IP
		final Location dbLocation = (Location) this.findByKey(location.getCity(), session,
		        Location.class);
		if (null != dbLocation) {
			location = dbLocation;
			List<IpAddress> ipAddresses = location.getIpAddresses();
			if (null == ipAddresses) {
				ipAddresses = new ArrayList<>();
			}
			ipAddresses.add(ipAddress);
		}
		session.saveOrUpdate(location);

		return location;
	}

	/**
	 * Find or create message.
	 *
	 * @param aMessage
	 *            the a message
	 * @param session
	 *            the session
	 * @return the message
	 */
	private synchronized Message findOrCreateMessage(final Message aMessage,
	        final Session session) {
		Message message = null;
		try {
			message = this.findMessage(aMessage, session);
			if (null == message) {
				session.saveOrUpdate(aMessage.getPoster());
				message = aMessage;
				session.saveOrUpdate(aMessage);
				// messagesCache.put(key, message);
			}
		}
		catch (final ObjectNotFoundException e) {
			HibernateHelper.logger.warn(message.getPoster(), e);
		}
		return message;

	}

	/**
	 * Find or create news group.
	 *
	 * @param session
	 *            the session
	 * @param groupName
	 *            the group name
	 * @return the news group
	 */
	synchronized NewsGroup findOrCreateNewsGroup(final Session session, final String groupName) {

		NewsGroup lastGroup = null;

		final String[] nameparts = groupName.split("\\.");
		// last part of name will be processed later
		NewsGroup group = null;
		String fullName = "";
		for (final String namePart : nameparts) {
			if (!fullName.equals("")) {
				fullName += ".";
			}
			fullName += namePart;
			group = (NewsGroup) this.findByKey(fullName, session, NewsGroup.class);
			if (null == group) {
				final HashSet<Topic> topics = null; // new HashSet<Topic>();
				// session.saveOrUpdate(topics);
				final HashSet<Message> messages = null;// new HashSet<Message>();
				// session.saveOrUpdate(messages);
				group = new NewsGroup(namePart, lastGroup, topics, messages, -1, -1, -1, -1,
				        fullName, false);
				session.saveOrUpdate(group);
			}
			lastGroup = group;
		}
		// Mark the last one as final
		group.setLastNode(true);
		session.saveOrUpdate(group);

		return lastGroup;
	}

	/**
	 * Find or create topic.
	 *
	 * @param session
	 *            the session
	 * @param subject
	 *            the subject
	 * @return the topic
	 */
	private synchronized Topic findOrCreateTopic(final Session session, final String subjectInput) {
		Topic topic = null;
		String subject = subjectInput;
		subject = subject.replaceAll("Re: ", "").trim();
		topic = (Topic) this.findByKey(subject, session, Topic.class);
		if (null == topic) {
			final HashSet<NewsGroup> groups = null; // new HashSet<NewsGroup>();
			topic = new Topic(subject, groups);
			session.saveOrUpdate(topic);
		}
		return topic;
	}

	/**
	 * Find or create usenet user.
	 *
	 * @param article
	 *            the article
	 * @param session
	 *            the session
	 * @param gender
	 *            the gender
	 * @return the usenet user
	 */
	private synchronized UsenetUser findOrCreateUsenetUser(final NewsArticle article,
	        final Session session, final String gender) {
		// Need to create a user to get the correctly formatted email
		// for the key
		final EmailAddress emailAddress = UsenetUserHelper.parseFromField(article);
		UsenetUser poster = this.findUsenetUser(article, session);

		if (null == poster) {
			poster = new UsenetUser(emailAddress.getName(), emailAddress.getEmail(),
			        emailAddress.getIpAddress(), gender, null);
			session.saveOrUpdate(poster);
		}
		return poster;
	}

	synchronized UsenetUser findUsenetUser(final NewsArticle article, final Session session) {
		final EmailAddress emailAddress = UsenetUserHelper.parseFromField(article);
		return (UsenetUser) this.findByKey(emailAddress.getEmail(), session, UsenetUser.class);
	}

	/**
	 * Generate schema.
	 */
	public void generateSchema() {
		Configuration config;
		try {
			config = this.getHibernateConfig();
			final Session session = this.getHibernateSession();
			final Transaction tx = session.beginTransaction();
			final SchemaExport sch = new SchemaExport(config);
			sch.create(true, true);
			tx.commit();
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the hibernate config.
	 *
	 * @return the hibernate config
	 * @throws HibernateException
	 *             the hibernate exception
	 * @throws MappingException
	 *             the mapping exception
	 * @throws NamingException
	 *             the naming exception
	 */
	private Configuration getHibernateConfig()
	        throws HibernateException, MappingException, NamingException {
		final Configuration config = new Configuration().configure();

		// show what config we have just read in
		final Properties props = config.getProperties();

		if (HibernateHelper.firstConnect) {
			// will be like jdbc:hsqldb:file:<filelocation>
			final String connUrl = props.getProperty(HibernateHelper.HIBERNATE_CONNECTION_URL);
			final String dbLocation = connUrl.replaceFirst("jdbc:hsqldb:file:", "");
			HibernateHelper.logger.warn("DB: " + dbLocation);

			final String dbLockFileName = dbLocation + ".lck";
			final File dbLock = new File(dbLockFileName);

			// Try to delete it once anyway
			if (dbLock.exists()) {
				dbLock.delete();
			}
			// If lock exists warn user and ask what to do
			while (dbLock.exists()) {

				final String question = "Found database lockfile " + dbLockFileName
				        + "\n if there is another UNISoN running, then quit,\n"
				        + " otherwise click OK";
				final String defaultOption = "OK";

				final String[] options = { defaultOption, "Quit" };
				final String title = "Database locked";
				if (null != this.gui) {
					final int response = this.gui.askQuestion(question, options, title,
					        defaultOption);
					switch (response) {
						case 0: // delete
							dbLock.delete();
							break;
						case 1:
							System.exit(0);
							break;
						case JOptionPane.CLOSED_OPTION: // quit
							break;
						default:
							break;
					}
				}
				else {
					dbLock.delete();
				}
			}
			HibernateHelper.firstConnect = false;
			final String dbSchemaFile = dbLocation + ".script";
			final File dbSchema = new File(dbSchemaFile);

			if (!dbSchema.exists()) {
				HibernateHelper.logger.warn("No database - creating now");
				this.generateSchema();
			}
		}
		HibernateHelper.logger.debug("getHibernateConfig");
		return config;
	}

	/**
	 * Gets the hibernate session.
	 *
	 * @return the hibernate session
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	@SuppressWarnings("deprecation")
	public synchronized Session getHibernateSession() throws UNISoNException {
		HibernateHelper.logger.debug("getHibernateSession");
		if (null == HibernateHelper.sessionFactory) {
			try {
				final Configuration config = this.getHibernateConfig();

				// FIXME - couldn't stop the NoInitialContext warning so this
				// hack will stop it being displayed
				final Level level = Category.getRoot().getLevel();
				Category.getRoot().setLevel(Level.FATAL);
				HibernateHelper.sessionFactory = config.buildSessionFactory();
				Category.getRoot().setLevel(level);

			}
			catch (final Throwable e) {
				e.printStackTrace();
				throw new UNISoNException("Failed to connect to DB", e);
			}
		}
		HibernateHelper.logger.debug("getHibernateSession");

		return HibernateHelper.sessionFactory.openSession();

	}

	/**
	 * Gets the list results.
	 *
	 * @param <T>
	 *            the generic type
	 * @param query
	 *            the query
	 * @param type
	 *            the type
	 * @param session
	 *            the session
	 * @return the list results
	 */
	<T> Vector<ResultRow> getListResults(final String query, final Class<T> type,
	        final Session session) {
		final List<T> resultsRows = this.runSQLQuery(query, session, type);
		final Vector<ResultRow> resultsVector = new Vector<>();
		for (final Object next : resultsRows) {
			final Object[] results = (Object[]) next;
			final String key = (String) results[0];
			final int count = Integer.parseInt(results[1].toString());
			resultsVector.add(new ResultRow(key, count, type));
		}
		return resultsVector;
	}

	/**
	 * Gets the newsgroup by full name.
	 *
	 * @param groupName
	 *            the group name
	 * @param session
	 *            the session
	 * @return the newsgroup by full name
	 */
	public NewsGroup getNewsgroupByFullName(final String groupName, final Session session) {
		final Query query = session
		        .createQuery("from " + NewsGroup.class.getName() + " where fullname=?");

		final NewsGroup group = (NewsGroup) query.setString(0, groupName).uniqueResult();
		return group;
	}

	/**
	 * Gets the text.
	 *
	 * @param object
	 *            the object
	 * @return the text
	 */
	public String getText(final Object object) {
		String text = "";
		if (object instanceof Message) {
			text = "From:" + ((Message) object).getPoster().toString();
		}
		else if (object instanceof NewsGroup) {
			final NewsGroup group = (NewsGroup) object;
			text = group.getName();
			if (group.getLastMessageTotal() > 0) {
				text += " (" + group.getLastMessageTotal() + ")";
			}
		}
		else if (object instanceof Location) {
			final Location location = (Location) object;
			text = "Location : " + location.getCity() + "," + location.getCountryCode();
		}
		else if (object instanceof UsenetUser) {
			text = "Poster : " + ((UsenetUser) object).getName();
		}
		else if (object instanceof Topic) {
			// text = ((Topic) object).getSubject();
		}
		return text;
	}

	/**
	 * Hibernate data.
	 *
	 * @param article
	 *            the article
	 * @param session
	 *            the session
	 * @return true, if successful
	 */
	public synchronized boolean hibernateData(final NewsArticle article, final Session session) {
		Transaction tx = null;

		HibernateHelper.logger.debug("hibernateData: " + article.getArticleId());

		final Location location = null;
		final String gender = null;
		try {
			tx = session.beginTransaction();
			tx.begin();

			final UsenetUser poster = this.createUsenetUser(article, session, location, gender);

			final Message message = this.createMessage(article, null, poster);

			this.storeNewsgroups(article.getNewsgroupsList(), message, session);

			tx.commit();

		}
		catch (Exception e) {
			if (e instanceof GenericJDBCException) {
				e = (Exception) e.getCause();
			}
			if (e instanceof BatchUpdateException) {
				final BatchUpdateException buex = (BatchUpdateException) e;
				System.err.println("Contents of BatchUpdateException:");
				System.err.println(" Update counts: ");
				final int[] updateCounts = buex.getUpdateCounts();
				for (int i = 0; i < updateCounts.length; i++) {
					System.err.println("  Statement " + i + ":" + updateCounts[i]);
				}
				System.err.println(" Message: " + buex.getMessage());
				System.err.println(" SQLSTATE: " + buex.getSQLState());
				System.err.println(" Error code: " + buex.getErrorCode());
				System.err.println(" Article: " + article);

				SQLException ex = buex.getNextException();
				while (ex != null) {
					System.err.println("SQL exception:");
					System.err.println(" Message: " + ex.getMessage());
					System.err.println(" SQLSTATE: " + ex.getSQLState());
					System.err.println(" Error code: " + ex.getErrorCode());
					System.err.println(" Error code: " + ex.getErrorCode());

					ex = ex.getNextException();
				}

			}
			HibernateHelper.logger.error("Failed to store message", e);
			e.printStackTrace();
			if (tx != null) {
				try {
					tx.rollback();
				}
				catch (final HibernateException e1) {
					e1.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Run query.
	 *
	 * @param <T>
	 *            the generic type
	 * @param query
	 *            the query
	 * @param type
	 *            the type
	 * @return the vector
	 */
	@SuppressWarnings("unchecked")
	<T> Vector<T> runQuery(final Query query, final Class<T> type) {
		HibernateHelper.logger.debug("runSQL: " + query);
		// TODO create prepared statements
		Vector<?> returnVal = null;
		try {
			returnVal = new Vector<T>(query.list());
		}
		catch (final GenericJDBCException dbe) {
			throw dbe;
		}
		catch (final HibernateException e) {
			HibernateHelper.logger.error("Error fetching " + NewsGroup.class.getName(), e);
		}
		if (null == returnVal) {
			returnVal = new Vector<>();
		}
		return (Vector<T>) returnVal;
	}

	/**
	 * Run query.
	 *
	 * @param <T>
	 *            the generic type
	 * @param query
	 *            the query
	 * @param hibernateSession
	 *            the hibernate session
	 * @param type
	 *            the type
	 * @return the vector
	 */
	public <T> Vector<T> runQuery(final String query, final Session hibernateSession,
	        final Class<T> type) {
		HibernateHelper.logger.debug("runSQL: " + query);
		final Query createQuery = hibernateSession.createQuery(query);
		return this.runQuery(createQuery, type);
	}

	/**
	 * Run sql query.
	 *
	 * @param <T>
	 *            the generic type
	 * @param query
	 *            the query
	 * @param session
	 *            the session
	 * @param type
	 *            the type
	 * @return the list
	 */
	private <T> List<T> runSQLQuery(final String query, final Session session,
	        final Class<T> type) {
		HibernateHelper.logger.debug("runSQL: " + query);
		return this.runQuery(session.createSQLQuery(query), type);
	}

	/**
	 * Store newsgroups.
	 *
	 * @param newsgroupsList
	 *            the newsgroups list
	 * @param data.message
	 *            the message
	 * @param session
	 *            the session
	 * @throws HibernateException
	 *             the hibernate exception
	 */
	private synchronized void storeNewsgroups(final List<String> newsgroupsList,
	        final Message messageInput, final Session session) throws HibernateException {
		Message message = messageInput;
		message = this.findOrCreateMessage(message, session);

		final ListIterator<String> iter = newsgroupsList.listIterator();
		while (iter.hasNext()) {
			final String groupName = iter.next().trim();

			if (groupName.trim().length() > 0) {
				final NewsGroup group = this.findOrCreateNewsGroup(session, groupName);
				if (null != message) {
					Set<NewsGroup> newsgroups = message.getNewsgroups();
					if (null == newsgroups) {
						newsgroups = new HashSet<>();
						message.setNewsgroups(newsgroups);
					}
					newsgroups.add(group);
					session.saveOrUpdate(message);

					final Topic topic = this.findOrCreateTopic(session, message.getSubject());

					Set<Message> groupMessages = group.getMessages();
					if (null == groupMessages) {
						groupMessages = new HashSet<>();
						group.setMessages(groupMessages);
					}
					groupMessages.add(message);

					Set<Topic> groupTopics = group.getTopics();
					if (null == groupTopics) {
						groupTopics = new HashSet<>();
						group.setTopics(groupTopics);
					}
					groupTopics.add(topic);
					session.saveOrUpdate(group);

					Set<NewsGroup> topicsGroups = topic.getNewsgroups();
					if (null == topicsGroups) {
						topicsGroups = new HashSet<>();
						topic.setNewsgroups(topicsGroups);
					}
					topicsGroups.add(group);
					session.saveOrUpdate(topic);
					message.setTopic(topic);
					session.saveOrUpdate(message);

				}
				// adding this just in case
				HibernateHelper.logger.debug("Stored " + group + " " + group.getId());
				// groups.add(group);
			}
		}
	}

	/**
	 * Store newsgroups.
	 *
	 * @param newsgroupsList
	 *            the newsgroups list
	 * @param session
	 *            the session
	 * @return the list
	 * @throws HibernateException
	 *             the hibernate exception
	 */
	public List<NewsGroup> storeNewsgroups(final Set<NewsGroup> newsgroupsList,
	        final Session session) throws HibernateException {
		final Iterator<NewsGroup> iter = newsgroupsList.iterator();
		final List<NewsGroup> groups = new Vector<>();

		while (iter.hasNext()) {
			final NewsGroup sourceGroup = iter.next();
			final String groupName = sourceGroup.getName().trim();

			if (groupName.trim().length() > 0) {
				final NewsGroup group = this.findOrCreateNewsGroup(session, groupName);

				// Add some stats to the news group
				group.setLastMessageTotal(sourceGroup.getArticleCount());
				group.setFirstMessage(sourceGroup.getFirstMessage());
				group.setLastMessage(sourceGroup.getLastMessage());

				session.saveOrUpdate(group);
				session.flush();

				HibernateHelper.logger
				        .debug("Stored " + group.getId() + " " + group.getLastMessageTotal() + " "
				                + group.getFirstMessage() + " " + group.getLastMessage());
				// groups.add(group);
			}
		}
		return groups;
	}
}
