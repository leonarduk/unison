package uk.co.sleonard.unison.datahandling;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import javax.naming.NamingException;
import javax.swing.JOptionPane;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.NonUniqueResultException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hsqldb.util.DatabaseManagerSwing;

import uk.co.sleonard.unison.datahandling.DAO.EmailAddress;
import uk.co.sleonard.unison.datahandling.DAO.IpAddress;
import uk.co.sleonard.unison.datahandling.DAO.Location;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.ResultRow;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.gui.UNISoNController;
import uk.co.sleonard.unison.gui.UNISoNException;
import uk.co.sleonard.unison.input.NNTPNewsGroup;
import uk.co.sleonard.unison.input.NewsArticle;
import uk.co.sleonard.unison.utils.StringUtils;

/**
 * This is one of the the most important classes as it helps persist the data to
 * the HSQL database
 * 
 * @author steve
 * 
 */
public class HibernateHelper {

	//private final static String DB_URL = "jdbc:hsqldb:file:DB/projectDB";
	private final static String DB_URL = "jdbc:hsqldb:file:src/main/resources/DB/projectDB";

	private final static String dbDriver = "org.hsqldb.jdbcDriver";

	private final static String dbUser = "sa";

	private static boolean firstConnect = true;

	public static final String GUI_ARGS[] = { "-driver", dbDriver, "-url",
			DB_URL, "-user", dbUser };

	private static final String HIBERNATE_CONNECTION_URL = "hibernate.connection.url";

	private static Logger logger = Logger.getLogger("HibernateHelper");

	private static SessionFactory sessionFactory = null;

	public static void main(final String[] args) {
		HibernateHelper helper = new HibernateHelper(null);
		helper.generateSchema();
		DatabaseManagerSwing.main(GUI_ARGS);
	}

	private UNISoNController controller;

	public HibernateHelper(UNISoNController controller) {
		this.controller = controller;
	}

	public void closeHibernateSessionFactory() throws HibernateException {
		sessionFactory.close();
	}

	private synchronized Location createLocation(final NewsArticle article,
			Session session) {
		Location location;
		IpAddress ip = findOrCreateIpAddress(article, session);
		location = ip.getLocation();
		if (null == location) {
			// create location to find city (does web lookup)
			location = findOrCreateLocation(session, ip);
		}
		return location;
	}

	@SuppressWarnings("unchecked")
	public Location createLocation(final String ipAddress) {

		Location location = null;
		final String locationLookupUrl = "http://api.hostip.info/rough.php?ip="
				+ ipAddress;

		final HashMap<String, String> fieldMap = new HashMap<String, String>();

		try {
			// Create a URL for the desired page
			final URL url = new URL(locationLookupUrl);

			// Read all the text returned by the server
			final BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String str;
			// Example output:
			// Country: UNITED STATES
			// Country Code: US
			// City: Beltsville, MD <-- this becomes location field
			// Guessed: true
			while ((str = in.readLine()) != null) {
				// str is one line of text; readLine() strips the newline
				// character(s)
				final String[] fields = str.split(": ");
				fieldMap.put(fields[0], fields[1]);
			}
			in.close();

			final String city = fieldMap.get("City");
			final String country = fieldMap.get("Country");
			final String countryCode = fieldMap.get("Country Code");
			final boolean guessed = Boolean.getBoolean(fieldMap.get("Guessed"));

			Vector<UsenetUser> posters = null;// new Vector<UsenetUser>();
			Vector<IpAddress> ips = null;// new Vector<IpAddress>();

			location = new Location(city, country, countryCode, guessed,
					posters, ips);

		} catch (final MalformedURLException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return location;
	}

	private Message createMessage(final NewsArticle article, Topic topic,
			UsenetUser poster) throws UNISoNException {
		Message message;
		byte[] body = null;
		if (null != article.getContent()) {
			// As messages can be quite large, we compress them
			try {
				body = StringUtils.compress(article.getContent().toString());
			} catch (IOException e) {
				throw new UNISoNException("Failed to compress message", e);
			}
		}

		message = new Message(article.getDate(), article.getArticleId(),
				article.getSubject(), poster, topic, null, article
						.getReferences(), body);
		return message;
	}

	private synchronized UsenetUser createUsenetUser(final NewsArticle article,
			Session session, Location location, String gender) {
		// Add location details if got all details
		if (article.isFullHeader()) {
			location = createLocation(article, session);
		}
		UsenetUser poster = findOrCreateUsenetUser(article, session, gender);
		if (null == poster.getLocation()
				|| !poster.getLocation().equals(location)) {
			poster.setLocation(location);
			poster.setLocation(location);
			session.saveOrUpdate(poster);
		}
		return poster;
	}

	public List<?> fetchAll(final Class<?> classtype, Session session)
			throws GenericJDBCException {
		final String query = "from " + classtype.getName();
		final List<?> returnVal = runQuery(query, session);
		return returnVal;
	}

	@SuppressWarnings("unchecked")
	public List<NewsGroup> fetchBaseNewsGroups(Session session)
			throws GenericJDBCException {
		final String query = "from " + NewsGroup.class.getName()
				+ " where lastnode is true";
		final List<NewsGroup> returnVal = (List<NewsGroup>) runQuery(query,
				session);
		return returnVal;
	}

	public Serializable fetchOrInsert(final Session session,
			Serializable storable, final List<?> dbList)
			throws HibernateException {
		if (dbList.size() > 0) {
			storable = (Serializable) dbList.get(0);
		} else {
			session.saveOrUpdate(storable);
		}
		return storable;
	}

	/**
	 * 
	 * @param key
	 * @param session
	 * @param objclass
	 * @return
	 * @throws HibernateException
	 */
	public Object findByKey(final int key, final Session session,
			final Class<?> objclass) throws HibernateException {
		final String queryText = objclass.getName() + ".findByKey";
		final Query query = session.getNamedQuery(queryText);
		query.setInteger("key", key);
		Object uniqueResult = null;
		try {
			uniqueResult = query.uniqueResult();
		} catch (NonUniqueResultException e) {
			throw new RuntimeException("Got non-unique result for " + key
					+ " on " + objclass.getName() + " " + e);
		}
		return uniqueResult;
	}

	/**
	 * 
	 * @param key
	 * @param session
	 * @param objclass
	 * @return
	 * @throws HibernateException
	 */
	public Object findByKey(final String key, final Session session,
			final Class<?> objclass) throws HibernateException {

		final String queryText = objclass.getName() + ".findByKey";
		final Query query = session.getNamedQuery(queryText);
		query.setString("key", key);
		Object uniqueResult = null;
		try {
			uniqueResult = query.uniqueResult();

		} catch (NonUniqueResultException e) {
			throw new RuntimeException("Got non-unique result for " + key
					+ " on " + objclass.getName() + " " + e);
		}
		return uniqueResult;
	}

	/**
	 * 
	 * @param article
	 * @param session
	 * @return
	 */
	private synchronized IpAddress findOrCreateIpAddress(
			final NewsArticle article, Session session) {
		// If user is new, then location might be too
		IpAddress ip = (IpAddress) findByKey(article.getPostingHost(), session,
				IpAddress.class);

		if (null == ip) {
			ip = new IpAddress(article.getPostingHost(), null);
			session.saveOrUpdate(ip);
		}
		return ip;
	}

	/**
	 * 
	 * @param session
	 * @param ipAddress
	 * @return
	 */
	private synchronized Location findOrCreateLocation(Session session,
			IpAddress ipAddress) {
		Location location;
		location = createLocation(ipAddress.getIpAddress());

		// find if location was already created for another
		// IP
		final Location dbLocation = (Location) findByKey(location.getCity(),
				session, Location.class);
		if (null != dbLocation) {
			location = dbLocation;
			List<IpAddress> ipAddresses = location.getIpAddresses();
			if (null == ipAddresses) {
				ipAddresses = new ArrayList<IpAddress>();
			}
			ipAddresses.add(ipAddress);
		}
		session.saveOrUpdate(location);

		return location;
	}

	/**
	 * 
	 * @param article
	 * @param session
	 * @param topic
	 * @param poster
	 * @param msgId
	 * @return
	 */
	private synchronized Message findOrCreateMessage(final Message aMessage,
			Session session) {
		String key = aMessage.getUsenetMessageID();
		// if (messages.containsKey(key)) {
		// return messages.get(key);
		// }
		Message message = null;
		try {
			message = (Message) findByKey(key, session, Message.class);
			if (null == message) {
				session.saveOrUpdate(aMessage.getPoster());
				message = aMessage;
				session.saveOrUpdate(aMessage);
				// messagesCache.put(key, message);
			}
		} catch (ObjectNotFoundException e) {
			logger.warn(message.getPoster(), e);
		}
		return message;

	}

	public synchronized NewsGroup findOrCreateNewsGroup(final Session session,
			final String groupName) {

		// if (groupsCache.containsKey(groupName)) {
		// return groupsCache.get(groupName);
		// }

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
			group = (NewsGroup) findByKey(fullName, session, NewsGroup.class);
			if (null == group) {
				HashSet<Topic> topics = null; // new HashSet<Topic>();
				// session.saveOrUpdate(topics);
				HashSet<Message> messages = null;// new HashSet<Message>();
				// session.saveOrUpdate(messages);
				group = new NewsGroup(namePart, lastGroup, topics, messages,
						-1, -1, -1, -1, fullName, false);
				session.saveOrUpdate(group);
			}
			lastGroup = group;
		}
		// Mark the last one as final
		group.setLastNode(true);
		session.saveOrUpdate(group);

		return lastGroup;
	}

	private synchronized Topic findOrCreateTopic(final Session session,
			String subject) {
		Topic topic = null;
		subject = subject.replaceAll("Re: ", "").trim();
		topic = (Topic) findByKey(subject, session, Topic.class);
		if (null == topic) {
			HashSet<NewsGroup> groups = null; // new HashSet<NewsGroup>();
			topic = new Topic(subject, groups);
			session.saveOrUpdate(topic);
		}
		return topic;
	}

	private synchronized UsenetUser findOrCreateUsenetUser(
			final NewsArticle article, Session session, String gender) {
		// Need to create a user to get the correctly formatted email
		// for the key
		EmailAddress emailAddress = UsenetUserHelper.parseFromField(article
				.getFrom(), article.getPostingHost());
		UsenetUser poster = null;
		poster = (UsenetUser) findByKey(emailAddress.getEmail(), session,
				UsenetUser.class);

		if (null == poster) {
			poster = new UsenetUser(emailAddress.getName(), emailAddress
					.getEmail(), emailAddress.getIpAddress(), gender, null);
			session.saveOrUpdate(poster);
		}
		return poster;
	}

	public void generateSchema() {
		Configuration config;
		try {
			config = getHibernateConfig();
			Session session = getHibernateSession();
			Transaction tx = session.beginTransaction();
			final SchemaExport sch = new SchemaExport(config);
			sch.create(true, true);
			tx.commit();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public String getGoogleMapURL(final String city) {
		String googleUrl;
		if (!city.equals("(Private Address)")) {
			googleUrl = "http://maps.google.com/maps?f=q&hl=en&geocode=&q="
					+ city + "&ie=UTF8&z=12&om=1";
		} else {
			googleUrl = "UNKNOWN LOCATION";
		}
		return googleUrl;
	}

	private Configuration getHibernateConfig() throws HibernateException,
			MappingException, NamingException {
		// Hashtable<String, String> env = new Hashtable<String, String>();
		// env.put(Context.INITIAL_CONTEXT_FACTORY,
		// "com.sun.jndi.fscontext.RefFSContextFactory");
		// env.put(Context.PROVIDER_URL, "file:/");
		// Context initialContext = new InitialContext(env);
		Configuration config = new Configuration().configure();

		// show what config we have just read in
		final Properties props = config.getProperties();

		if (firstConnect) {
			// will be like jdbc:hsqldb:file:<filelocation>
			final String connUrl = props.getProperty(HIBERNATE_CONNECTION_URL);
			final String dbLocation = connUrl.replaceFirst("jdbc:hsqldb:file:",
					"");
			logger.warn("DB: " + dbLocation);

			final String dbLockFileName = dbLocation + ".lck";
			final File dbLock = new File(dbLockFileName);

			// Try to delete it once anyway
			if (dbLock.exists()) {
				dbLock.delete();
			}
			// If lock exists warn user and ask what to do
			while (dbLock.exists()) {

				final String question = "Found database lockfile "
						+ dbLockFileName
						+ "\n if there is another UNISoN running, then quit,\n"
						+ " otherwise click OK";
				final String defaultOption = "OK";

				final String[] options = { defaultOption, "Quit" };
				final String title = "Database locked";
				if (null != controller) {
					final int response = controller.askQuestion(question,
							options, title, defaultOption);
					switch (response) {
					case 0: // delete
						dbLock.delete();
						break;
					case 1:
						System.exit(0);
					case JOptionPane.CLOSED_OPTION: // quit
						break;
					}
				} else {
					dbLock.delete();
				}
			}
			firstConnect = false;
			final String dbSchemaFile = dbLocation + ".script";
			final File dbSchema = new File(dbSchemaFile);

			if (!dbSchema.exists()) {
				logger.warn("No database - creating now");
				generateSchema();
			}
		}
		logger.debug("getHibernateConfig");
		return config;
	}

	@SuppressWarnings("deprecation")
	public synchronized Session getHibernateSession() throws UNISoNException {
		logger.debug("getHibernateSession");
		if (null == sessionFactory) {
			try {
				final Configuration config = getHibernateConfig();

				// FIXME - couldn't stop the NoInitialContext warning so this
				// hack will stop it being displayed
				Level level = Logger.getRoot().getLevel();
				Logger.getRoot().setLevel(Level.FATAL);
				sessionFactory = config.buildSessionFactory();
				Logger.getRoot().setLevel(level);

			} catch (Throwable e) {
				e.printStackTrace();
				throw new UNISoNException("Failed to connect to DB", e);
			}
		}
		logger.debug("getHibernateSession");

		return sessionFactory.openSession();

	}

	public Vector<ResultRow> getListResults(String query, Class<?> type,
			Session session) {
		List<?> resultsRows = runSQLQuery(query, session);
		Vector<ResultRow> resultsVector = new Vector<ResultRow>();
		for (Object next : resultsRows) {
			Object[] results = (Object[]) next;
			String key = (String) results[0];
			int count = Integer.parseInt(results[1].toString());
			resultsVector.add(new ResultRow(key, count, type));
		}
		return resultsVector;
	}

	public NewsGroup getNewsgroupByFullName(final String groupName,
			Session session) {
		final Query query = session.createQuery("from "
				+ NewsGroup.class.getName() + " where fullname=?");

		final NewsGroup group = (NewsGroup) query.setString(0, groupName)
				.uniqueResult();
		return group;
	}

	public String getText(final Object object) {
		String text = "";
		if (object instanceof Message) {
			text = "From:" + ((Message) object).getPoster().toString();
		} else if (object instanceof NewsGroup) {
			final NewsGroup group = (NewsGroup) object;
			text = group.getName();
			if (group.getLastMessageTotal() > 0) {
				text += " (" + group.getLastMessageTotal() + ")";
			}
		} else if (object instanceof Location) {
			final Location location = (Location) object;
			text = "Location : " + location.getCity() + ","
					+ location.getCountryCode();
		} else if (object instanceof UsenetUser) {
			text = "Poster : " + ((UsenetUser) object).getName();
		} else if (object instanceof Topic) {
			// text = ((Topic) object).getSubject();
		}
		return text;
	}

	public synchronized boolean hibernateData(final NewsArticle article,
			Session session) {
		Transaction tx = null;

		logger.debug("hibernateData: " + article.getArticleId());

		Location location = null;
		String gender = null;
		try {
			tx = session.beginTransaction();
			tx.begin();

			UsenetUser poster = createUsenetUser(article, session, location,
					gender);

			Message message = createMessage(article, null, poster);

			storeNewsgroups(article.getNewsgroupsList(), message, session);

			tx.commit();

		} catch (Exception e) {
			if (e instanceof GenericJDBCException) {
				e = (Exception) e.getCause();
			}
			if (e instanceof BatchUpdateException) {
				BatchUpdateException buex = (BatchUpdateException) e;
				System.err.println("Contents of BatchUpdateException:");
				System.err.println(" Update counts: ");
				int[] updateCounts = buex.getUpdateCounts();
				for (int i = 0; i < updateCounts.length; i++) {
					System.err.println("  Statement " + i + ":"
							+ updateCounts[i]);
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
			logger.error("Failed to store message", e);
			e.printStackTrace();
			if (tx != null) {
				try {
					tx.rollback();
				} catch (final HibernateException e1) {
					e1.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public Vector<?> runQuery(final Query query) {
		logger.debug("runSQL: " + query);
		// TODO create prepared statements
		Vector<?> returnVal = null;
		try {
			returnVal = new Vector<Object>(query.list());
		} catch (final GenericJDBCException dbe) {
			throw dbe;
		} catch (final HibernateException e) {
			logger.error("Error fetching " + NewsGroup.class.getName(), e);
		}
		if (null == returnVal) {
			returnVal = new Vector<Object>();
		}
		return returnVal;
	}

	public Vector<?> runQuery(final String query, Session hibernateSession) {
		logger.debug("runSQL: " + query);
		final Query createQuery = hibernateSession.createQuery(query);
		return runQuery(createQuery);
	}

	public List<?> runSQLQuery(final String query, Session session) {
		logger.debug("runSQL: " + query);
		return runQuery(session.createSQLQuery(query));
	}

	public synchronized void storeNewsgroups(final List<String> newsgroupsList,
			Message message, final Session session) throws HibernateException {
		message = findOrCreateMessage(message, session);

		final ListIterator<String> iter = newsgroupsList.listIterator();
		while (iter.hasNext()) {
			final String groupName = iter.next().trim();

			if (groupName.trim().length() > 0) {
				final NewsGroup group = findOrCreateNewsGroup(session,
						groupName);
				if (null != message) {
					Set<NewsGroup> newsgroups = message.getNewsgroups();
					if (null == newsgroups) {
						newsgroups = new HashSet<NewsGroup>();
						message.setNewsgroups(newsgroups);
					}
					newsgroups.add(group);
					session.saveOrUpdate(message);

					Topic topic = findOrCreateTopic(session, message
							.getSubject());

					Set<Message> groupMessages = group.getMessages();
					if (null == groupMessages) {
						groupMessages = new HashSet<Message>();
						group.setMessages(groupMessages);
					}
					groupMessages.add(message);

					Set<Topic> groupTopics = group.getTopics();
					if (null == groupTopics) {
						groupTopics = new HashSet<Topic>();
						group.setTopics(groupTopics);
					}
					groupTopics.add(topic);
					session.saveOrUpdate(group);

					Set<NewsGroup> topicsGroups = topic.getNewsgroups();
					if (null == topicsGroups) {
						topicsGroups = new HashSet<NewsGroup>();
						topic.setNewsgroups(topicsGroups);
					}
					topicsGroups.add(group);
					session.saveOrUpdate(topic);
					message.setTopic(topic);
					session.saveOrUpdate(message);

				}
				// adding this just in case
				logger.debug("Stored " + group + " " + group.getId());
				// groups.add(group);
			}
		}
	}

	public List<NewsGroup> storeNewsgroups(
			final Set<NNTPNewsGroup> newsgroupsList, Session session)
			throws HibernateException {
		final Iterator<NNTPNewsGroup> iter = newsgroupsList.iterator();
		final List<NewsGroup> groups = new Vector<NewsGroup>();

		while (iter.hasNext()) {
			final NNTPNewsGroup sourceGroup = iter.next();
			final String groupName = sourceGroup.getNewsgroup().trim();

			if (groupName.trim().length() > 0) {
				final NewsGroup group = findOrCreateNewsGroup(session,
						groupName);

				// Add some stats to the news group
				group.setLastMessageTotal(sourceGroup.getArticleCount());
				group.setFirstMessage(sourceGroup.getFirstArticle());
				group.setLastMessage(sourceGroup.getLastArticle());

				session.saveOrUpdate(group);
				session.flush();

				logger.debug("Stored " + group.getId() + " "
						+ group.getLastMessageTotal() + " "
						+ group.getFirstMessage() + " "
						+ group.getLastMessage());
				// groups.add(group);
			}
		}
		return groups;
	}
}