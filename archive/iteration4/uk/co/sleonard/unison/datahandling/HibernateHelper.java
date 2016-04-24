package uk.co.sleonard.unison.datahandling;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import javax.swing.JOptionPane;

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

	private static boolean firstConnect = true;

	private static final String HIBERNATE_CONNECTION_URL = "hibernate.connection.url";

	private static Logger logger = Logger.getLogger("HibernateHelper");

	private static SessionFactory sessionFactory = null;

	public void closeHibernateSessionFactory() throws HibernateException {
		sessionFactory.close();
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

	public List<?> fetchAll(final Class<?> classtype)
			throws GenericJDBCException {
		final String query = "from " + classtype.getName();
		final List<?> returnVal = runQuery(query);
		return returnVal;
	}

	@SuppressWarnings("unchecked")
	public List<NewsGroup> fetchBaseNewsGroups() throws GenericJDBCException {
		final String query = "from " + NewsGroup.class.getName()
				+ " where lastnode is true";
		final List<NewsGroup> returnVal = (List<NewsGroup>) runQuery(query);
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

	@SuppressWarnings("unchecked")
	public List<Topic> fetchTopics(final NewsGroup group,
			UNISoNController controller) throws GenericJDBCException {

		List<Topic> returnVal = null;

		// + " on newsgroup_id " +

		if (null != group) {
			final SQLQuery query = getHibernateSession().createSQLQuery(
					"SELECT topic.* from topic"
							+ " inner join newsgroup_topic "
							+ " on newsgroup_topic.topic_id= topic.topic_id "
							+ " where newsgroup_topic.newsgroup_id = :groupID");
			query.addEntity(Topic.class);
			query.setInteger("groupID", group.getId());
			returnVal = query.list();
		} else {
			final String query = "from Topic ";
			returnVal = (List<Topic>) runQuery(query);
		}

		logger.debug("There are " + returnVal.size() + " topics");
		return returnVal;
	}

	public List<NewsGroup> fetchTopNewsGroups() throws GenericJDBCException {
		final Session session = getHibernateSession();
		final List<NewsGroup> list = fetchTopNewsGroups(session);
		// session.close();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<NewsGroup> fetchTopNewsGroups(final Session session)
			throws GenericJDBCException {
		final String query = "from " + NewsGroup.class.getName()
				+ " where parentnewsgroup is null";
		final List<NewsGroup> returnVal = (List<NewsGroup>) runQuery(query);
		return returnVal;
	}

	private UNISoNController controller;

	public HibernateHelper(UNISoNController controller) {
		this.controller = controller;
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

	private static HashMap<String, NewsGroup> groupsCache = new HashMap<String, NewsGroup>();

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
				logger.debug("create: " + groupName + " " + lastGroup + " "
						+ nameparts[1] + " " + group);
			}
			lastGroup = group;
		}
		// Mark the last one as final
		group.setLastNode(true);
		session.saveOrUpdate(group);
		groupsCache.put(groupName, group);

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

	public void generateSchema() {
		Configuration config;
		try {
			config = getHibernateConfig();
			final SchemaExport sch = new SchemaExport(config);
			sch.create(true, true);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public List<NewsGroup> getChildNewsGroups(final NewsGroup group) {
		final String query = "from " + NewsGroup.class.getName()
				+ " where parentnewsgroup = " + group.getId();
		final List<NewsGroup> returnVal = (List<NewsGroup>) runQuery(query);
		return returnVal;
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
			MappingException {
		Configuration config = new Configuration().configure();

//		try {
//			Context ctx = new InitialContext();
//		} catch (NamingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
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

	public synchronized Session getHibernateSession() throws HibernateException {
		logger.debug("getHibernateSession");
		if (null == sessionFactory) {
			final Configuration config = getHibernateConfig();
			// TODO handle NoIntialContext exception
			try {
				sessionFactory = config.buildSessionFactory();
			} catch (Throwable e) {
				// TODO: handle exception
			}
		}
		logger.debug("getHibernateSession");

		return sessionFactory.openSession();

	}

	@SuppressWarnings("unchecked")
	public List<Message> getMessages(final Topic topic) {
		final String query = "from  Message  where topic_id = " + topic.getId();
		final List<Message> returnVal = new ArrayList<Message>();
		returnVal.addAll((List<Message>) runQuery(query));
		return returnVal;
	}

	public NewsGroup getNewsgroupByFullName(final String groupName) {
		final Query query = getHibernateSession().createQuery(
				"from " + NewsGroup.class.getName() + " where fullname=?");

		final NewsGroup group = (NewsGroup) query.setString(0, groupName)
				.uniqueResult();
		return group;
	}

	// TODO move to a string helper class
	public String getText(final Object object) {
		String text = "";
		if (object instanceof Message) {
			text = "From:" + ((Message) object).getPoster().getEmail();
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

	public boolean hibernateData(final NewsArticle article) {
		Session session = null;
		Transaction tx = null;

		logger.debug("hibernateData: " + article.getArticleId());

		Location location = null;
		String gender = null;
		try {
			session = getHibernateSession();
			tx = session.beginTransaction();
			Topic topic = findOrCreateTopic(session, article.getSubject());

			// Add location details if got all details
			if (article.isFullHeader()) {
				IpAddress ip = findOrCreateIpAddress(article, session);
				location = ip.getLocation();
				if (null == location) {
					// create location to find city (does web lookup)
					location = findOrCreateLocation(session, ip);
				}
			}
			UsenetUser poster = findOrCreateUsenetUser(article, session,
					gender, location);

			Message message = createMessage(article, topic, poster);
			message = findOrCreateMessage(message, session);

			storeNewsgroups(article.getNewsgroupsList(), message, session);

			tx.commit();
		} catch (final Exception e) {
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
			location.getIpAddresses().add(ipAddress);
			session.saveOrUpdate(location);
		}
	
		
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

	private Message createMessage(final NewsArticle article, Topic topic,
			UsenetUser poster) {
		Message message;
		byte[] body = null;
		if (null != article.getContent()) {
			// As messages can be quite large, we compress them
			try {
				body = StringUtils.compress(article.getContent().toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		message = new Message(article.getDate(), article.getArticleId(),
				article.getSubject(), poster, topic, null, article
						.getReferences(), body);
		return message;
	}

	private synchronized UsenetUser findOrCreateUsenetUser(
			final NewsArticle article, Session session, String gender,
			Location location) {
		// Need to create a user to get the correctly formatted email
		// for the key
		EmailAddress emailAddress = UsenetUserHelper.parseFromField(article
				.getFrom(), article.getPostingHost());
		UsenetUser poster = null;
		poster = (UsenetUser) findByKey(emailAddress.getEmail(), session,
				UsenetUser.class);

		if (null == poster) {
			poster = new UsenetUser(emailAddress.getName(), emailAddress
					.getEmail(), emailAddress.getIpAddress(), gender, location);
			session.saveOrUpdate(poster);
		}
		return poster;
	}

	private final static String dbDriver = "org.hsqldb.jdbcDriver";

	private final static String dbUser = "sa";
	private final static String DB_URL = "jdbc:hsqldb:file:DB/projectDB";

	public static final String GUI_ARGS[] = { "-driver", dbDriver, "-url",
			DB_URL, "-user", dbUser };

	public static void main(final String[] args) {
		HibernateHelper helper = new HibernateHelper(null);
		helper.generateSchema();
		DatabaseManagerSwing.main(GUI_ARGS);
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

	public Vector<?> runQuery(final String query) {
		logger.debug("runSQL: " + query);
		final Session hibernateSession = getHibernateSession();
		final Query createQuery = hibernateSession.createQuery(query);
		return runQuery(createQuery);
	}

	public List<?> runSQLQuery(final String query) {
		logger.debug("runSQL: " + query);
		return runQuery(getHibernateSession().createSQLQuery(query));
	}

	public void storeNewsgroups(final List<String> newsgroupsList,
			final Message message, final Session session)
			throws HibernateException {
		final ListIterator<String> iter = newsgroupsList.listIterator();
		while (iter.hasNext()) {
			final String groupName = iter.next().trim();

			if (groupName.trim().length() > 0) {
				final NewsGroup group = findOrCreateNewsGroup(session,
						groupName);

				/*
				 * NewsGroup
				 */
				if (null != message) {
					Set<NewsGroup> newsgroups = message.getNewsgroups();
					if (null == newsgroups) {
						newsgroups = new HashSet<NewsGroup>();
					}
					newsgroups.add(group);
					session.saveOrUpdate(message);

					// logger.debug("Num Msgs:"
					// + group.getMessages());

					final Topic topic = message.getTopic();
					Set<Topic> topics = group.getTopics();
					if (null == topics) {
						topics = new HashSet<Topic>();
					}
					topics.add(topic);
					Set<NewsGroup> topicsGroups = topic.getNewsgroups();
					if (null == topicsGroups) {
						topicsGroups = new HashSet<NewsGroup>();
					}
					topicsGroups.add(group);
					session.saveOrUpdate(topic);
					// session.saveOrUpdate(message);
					Set<Message> groupMessages = group.getMessages();
					if (null == groupMessages) {
						groupMessages = new HashSet<Message>();
					}
					groupMessages.add(message);
					session.saveOrUpdate(group);
				}

				session.saveOrUpdate(group);
				// session.flush();

				// adding this just in case
				session.saveOrUpdate(message);
				logger.debug("Stored " + group + " " + group.getId());
				// groups.add(group);
			}
		}
	}

	public List<NewsGroup> storeNewsgroups(
			final Set<NNTPNewsGroup> newsgroupsList) throws HibernateException {
		final Iterator<NNTPNewsGroup> iter = newsgroupsList.iterator();
		final List<NewsGroup> groups = new Vector<NewsGroup>();
		final Session session = getHibernateSession();

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

	public Vector<ResultRow> getListResults(String query, Class<?> type) {
		List<?> resultsRows = runSQLQuery(query);
		Vector<ResultRow> resultsVector = new Vector<ResultRow>();
		for (Object next : resultsRows) {
			Object[] results = (Object[]) next;
			String key = (String) results[0];
			int count = Integer.parseInt(results[1].toString());
			resultsVector.add(new ResultRow(key, count, type));
		}
		return resultsVector;
	}
}
