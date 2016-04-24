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
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hsqldb.util.DatabaseManagerSwing;

import uk.co.sleonard.unison.datahandling.DAO.IpAddress;
import uk.co.sleonard.unison.datahandling.DAO.Location;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.gui.UNISoNController;
import uk.co.sleonard.unison.input.NewsGroupArticle;

/**
 * This is one of the the most important classes as it helps persist the data to
 * the HSQL database
 * 
 * @author steve
 * 
 */
public class HibernateHelper {

	private static final String HIBERNATE_CONNECTION_URL = "hibernate.connection.url";

	private static Logger logger = Logger.getLogger("HibernateHelper");

	private static SessionFactory sessionFactory = null;

	// TODO move to a string helper class
	public static String getText(Object object) {
		String text = "";
		if (object instanceof Message) {
			text = "From:" + ((Message) object).getPoster().getEmail();
		} else if (object instanceof NewsGroup) {
			NewsGroup group = (NewsGroup) object;
			text = group.getName();
		} else if (object instanceof Location) {
			Location location = (Location) object;
			text = "Location : " + location.getCity() + ","
					+ location.getCountryCode();
		} else if (object instanceof UsenetUser) {
			text = "Poster : " + ((UsenetUser) object).getName();
		} else if (object instanceof Topic) {
			// text = ((Topic) object).getSubject();
		}
		return text;
	}

	@SuppressWarnings("unchecked")
	public static Location createLocation(String ipAddress) {

		Location location = null;
		String locationLookupUrl = "http://api.hostip.info/rough.php?ip="
				+ ipAddress;

		HashMap<String, String> fieldMap = new HashMap<String, String>();

		try {
			// Create a URL for the desired page
			URL url = new URL(locationLookupUrl);

			// Read all the text returned by the server
			BufferedReader in = new BufferedReader(new InputStreamReader(url
					.openStream()));
			String str;
			// Example output:
			// Country: UNITED STATES
			// Country Code: US
			// City: Beltsville, MD <-- this becomes location field
			// Guessed: true
			while ((str = in.readLine()) != null) {
				// str is one line of text; readLine() strips the newline
				// character(s)
				String[] fields = str.split(": ");
				fieldMap.put(fields[0], fields[1]);
			}
			in.close();

			String city = fieldMap.get("City");
			String country = fieldMap.get("Country");
			String countryCode = fieldMap.get("Country Code");
			boolean guessed = Boolean.getBoolean(fieldMap.get("Guessed"));

			location = new Location(city, country, countryCode, guessed,
					new Vector<UsenetUser>(), new Vector<IpAddress>());

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return location;
	}

	public static List<?> fetchAll(Class classtype) throws GenericJDBCException {
		String query = "from " + classtype.getName();
		List<?> returnVal = runQuery(query);
		return returnVal;
	}

	public static List<NewsGroup> fetchBaseNewsGroups()
			throws GenericJDBCException {
		String query = "from " + NewsGroup.class.getName()
				+ " where lastnode is true";
		List<NewsGroup> returnVal = (List<NewsGroup>) runQuery(query);
		return returnVal;
	}

	public static Serializable fetchOrInsert(Session session,
			Serializable storable, List dbList) throws HibernateException {
		if (dbList.size() > 0) {
			storable = (Serializable) dbList.get(0);
		} else {
			session.saveOrUpdate(storable);
		}
		return storable;
	}

	@SuppressWarnings("unchecked")
	public static List<Topic> fetchTopics(NewsGroup group)
			throws GenericJDBCException {

		List<Topic> returnVal = null;

		// + " on newsgroup_id " +

		if (null != group) {
			SQLQuery query = getHibernateSession().createSQLQuery(
					"SELECT topic.* from topic"
							+ " inner join newsgroup_topic "
							+ " on newsgroup_topic.topic_id= topic.topic_id "
							+ " where newsgroup_topic.newsgroup_id = :groupID");
			query.addEntity(Topic.class);
			query.setInteger("groupID", group.getId());
			returnVal = query.list();
		} else {
			String query = "from Topic ";
			returnVal = (List<Topic>) runQuery(query);
		}

		logger.debug("There are " + returnVal.size() + " topics");
		return returnVal;
	}

	public static List<NewsGroup> fetchTopNewsGroups()
			throws GenericJDBCException {
		Session session = getHibernateSession();
		List<NewsGroup> list = fetchTopNewsGroups(session);
		session.close();
		return list;
	}

	public static List<NewsGroup> fetchTopNewsGroups(Session session)
			throws GenericJDBCException {
		String query = "from " + NewsGroup.class.getName()
				+ " where parentnewsgroup is null";
		List<NewsGroup> returnVal = (List<NewsGroup>) runQuery(query);
		return returnVal;
	}

	public static List findByIntegerKey(int key, Session session, Class objclass)
			throws HibernateException {
		String queryText = objclass.getName() + ".findByKey";
		Query query = session.getNamedQuery(queryText);
		query.setInteger("key", key);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public static List<Serializable> findByStringKey(String key,
			Session session, Class objclass) throws HibernateException {

		String queryText = objclass.getName() + ".findByKey";
		Query query = session.getNamedQuery(queryText);
		query.setString("key", key);
		return query.list();
	}

	public static Serializable findFirstByKey(int key, Session session,
			Class objclass) throws HibernateException {
		List<Serializable> list = findByIntegerKey(key, session, objclass);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public static Serializable findFirstByKey(String key, Session session,
			Class objclass) throws HibernateException {
		logger.warn("findFirstByKey : " + key);

		List<Serializable> list = findByStringKey(key, session, objclass);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public static NewsGroup findOrCreateNewsGroup(Session session,
			String groupName) {
		NewsGroup lastGroup = null;

		if (groupName.contains(".")) {
			String[] nameparts = groupName.split("\\.");
			// last part of name will be processed later
			NewsGroup group = null;
			for (int i = 0; i < nameparts.length; i++) {
				group = findOrCreateNewsGroup(session, nameparts[i]);
				String name = nameparts[i];
				if (null != lastGroup) {
					group.setParentNewsGroup(lastGroup);
					name = lastGroup.getFullName() + "." + name;
				}
				group.setFullName(name);
				group.setLastNode(false);
				lastGroup = group;
				logger.info("create: " + groupName + " " + lastGroup + " "
						+ nameparts[1] + " " + group);
			}
			// Mark the last one as final
			group.setLastNode(true);

		} else {
			lastGroup = (NewsGroup) findFirstByKey(groupName, session,
					NewsGroup.class);
			if (null == lastGroup) {
				lastGroup = new NewsGroup(groupName, null,
						new HashSet<Topic>(), new HashSet<Message>(), -1, -1,
						groupName, false);

				session.saveOrUpdate(lastGroup);
			}
		}
		return lastGroup;
	}

	private static Topic findOrCreateTopic(Session session, String subject) {
		Topic topic = null;
		subject = subject.replaceAll("Re: ", "");
		topic = (Topic) findFirstByKey(subject, session, Topic.class);
		if (null == topic) {
			topic = new Topic(subject, new HashSet<NewsGroup>());
			session.saveOrUpdate(topic);
		}
		return topic;
	}

	public static void generateSchema() {
		Configuration config;
		try {
			config = getHibernateConfig();
			SchemaExport sch = new SchemaExport(config);
			sch.create(true, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<NewsGroup> getChildNewsGroups(NewsGroup group) {
		String query = "from " + NewsGroup.class.getName()
				+ " where parentnewsgroup = " + group.getId();
		List<NewsGroup> returnVal = (List<NewsGroup>) runQuery(query);
		return returnVal;
	}

	public static String getGoogleMapURL(String city) {
		String googleUrl;
		if (!city.equals("(Private Address)")) {
			googleUrl = "http://maps.google.com/maps?f=q&hl=en&geocode=&q="
					+ city + "&ie=UTF8&z=12&om=1";
		} else {
			googleUrl = "UNKNOWN LOCATION";
		}
		return googleUrl;
	}

	private static boolean firstConnect = true;

	private static Configuration getHibernateConfig()
			throws HibernateException, MappingException {
		logger.debug("getHibernateConfig");

		Configuration config;
		logger.info("hibernateData");

		config = new Configuration().configure();

		logger.info("hibernateData");

		// show what config we have just read in
		Properties props = config.getProperties();

		// for (Iterator iter = props.entrySet().iterator(); iter.hasNext();) {
		// Entry next = (Entry) iter.next();
		//
		// logger.warn("CONFIG: " + next.getKey() + "=" + next.getValue());
		// }

		if (firstConnect) {
			logger.info("hibernateData");

			// will be like jdbc:hsqldb:file:<filelocation>
			String connUrl = props.getProperty(HIBERNATE_CONNECTION_URL);
			String dbLocation = connUrl.replaceFirst("jdbc:hsqldb:file:", "");
			logger.warn("DB: " + dbLocation);

			String dbLockFileName = dbLocation + ".lck";
			File dbLock = new File(dbLockFileName);

			// If lock exists warn user and ask what to do
			while (dbLock.exists()) {
				String question = "Found database lockfile " + dbLockFileName
						+ "\n if there is another UNISoN running, then quit,\n"
						+ " otherwise you can delete the file";
				String defaultOption = "Delete File";

				String[] options = { defaultOption, "Quit" };
				String title = "Database locked";
				if (null != UNISoNController.getInstance()) {
					int response = UNISoNController.getInstance().askQuestion(
							question, options, title, defaultOption);
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
			String dbSchemaFile = dbLocation + ".script";
			File dbSchema = new File(dbSchemaFile);

			if (!dbSchema.exists()) {
				logger.warn("No database - creating now");
				generateSchema();
			}
		}
		logger.debug("getHibernateConfig");

		config.addClass(NewsGroup.class);
		config.addClass(Message.class);
		config.addClass(Location.class);
		config.addClass(UsenetUser.class);
		config.addClass(Topic.class);
		config.addClass(IpAddress.class);

		return config;
	}

	public static Session getHibernateSession() throws HibernateException {
		logger.debug("getHibernateSession");

		if (null == sessionFactory) {
			Configuration config = getHibernateConfig();
			// TODO handle NoIntialContext exeption
			sessionFactory = config.buildSessionFactory();
		}
		logger.debug("getHibernateSession");
		Session session = sessionFactory.openSession();

		return session;
	}

	public static List<Message> getMessages(Topic topic) {
		String query = "from  Message  where topic_id = " + topic.getId();
		List<Message> returnVal = new ArrayList<Message>();
		returnVal.addAll((List<Message>) runQuery(query));
		return returnVal;
	}

	@SuppressWarnings("unchecked")
	public static boolean hibernateData(NewsGroupArticle article) {
		Transaction tx = null;
		Session session = null;
		logger.info("hibernateData: " + article.getMessageID());

		try {

			session = getHibernateSession();
			logger.info("hibernateData");

			String msgId = article.getMessageID();

			Message message = (Message) findFirstByKey(msgId, session,
					Message.class);

			String usenetMessageID = null;
			String email = "";

			if (null != message) {
				email = message.getPoster().getEmail();
				usenetMessageID = message.getUsenetMessageID();

				logger.debug(email + " " + usenetMessageID);
			}
			// see if message already exists. Only continue if new message
			// check if no message exists or if has been created as a dummy
			if (null == message || email.equals(usenetMessageID)) {
				tx = session.beginTransaction();
				// search for all messages to find the source topic
				Topic topic = null;

				topic = findOrCreateTopic(session, article.getSubject());

				// Need to create a user to get the correctly formatted email
				// for the key
				UsenetUser poster = UsenetUserHelper.createUsenetUser(article
						.getFromString(), article.getPostingHost());

				Serializable dbVersionOfPoster = findFirstByKey(poster
						.getEmail(), session, UsenetUser.class);
				// Look up database using the email. If its missing, save to
				// database
				if (null == dbVersionOfPoster) {
					// If user is new, then location might be too
					IpAddress ip = (IpAddress) findFirstByKey(article
							.getPostingHost(), session, IpAddress.class);
					Location location = null;
					if (null == ip) {
						// create location to find city (does web lookup)
						location = createLocation(article.getPostingHost());

						// find if location was already created for another IP
						Location dbLocation = (Location) findFirstByKey(
								location.getCity(), session, Location.class);
						if (null != dbLocation) {
							location = dbLocation;
						}
						ip = new IpAddress(article.getPostingHost(), location);
						session.saveOrUpdate(location);
						location.getIpAddresses().add(ip);
						session.saveOrUpdate(ip);

					} else {
						location = ip.getLocation();
					}
					poster.setLocation(location);
					session.saveOrUpdate(poster);
				} else {
					poster = (UsenetUser) dbVersionOfPoster;
				}

				// add all the messages this one refers to
				List<Message> references = new Vector<Message>();

				ListIterator<String> iter = article.getReferencesList()
						.listIterator();
				while (iter.hasNext()) {
					String refID = iter.next();

					// Look up database to see if we have stored
					// this message's predecessor
					Message msg = (Message) findFirstByKey(refID, session,
							Message.class);

					if (null == msg) {
						// we don't know who this is, so create a user based on
						// message ID
						UsenetUser origPosterProxy = UsenetUserHelper
								.createUsenetUser("GENERATED <GEN:" + refID
										+ ">", null);
						origPosterProxy.setLocation(null);
						session.saveOrUpdate(origPosterProxy);

						// Give generated message the same references as the one
						// that refers to it
						Vector<Message> newrefs = new Vector<Message>(
								references);
						newrefs.remove(refID);

						// TODO - add newsgroups ?
						msg = new Message(article.getDate(), refID, topic
								.getSubject(), origPosterProxy, topic,
								new HashSet<NewsGroup>(), newrefs,
								new byte[] {});

						session.saveOrUpdate(msg);
						session.saveOrUpdate(topic);

					}
					references.add(msg);

				}
				if (null == message) {
					message = new Message();
				}

				message.setDateCreated(article.getDate());
				message.setUsenetMessageID(article.getMessageID());
				message.setSubject(article.getSubject());
				message.setPoster(poster);
				message.setNewsgroups(new HashSet<NewsGroup>());
				message.setTopic(topic);

				message.setReferencedMessages(references);

				// As messages can be quite large, we compress them
				byte[] messageBody = StringUtils.compress(article.getContent()
						.toString());
				message.setMessageBody(messageBody);

				session.saveOrUpdate(message);

				session.saveOrUpdate(topic);

				storeNewsgroups(article.getNewsgroupsList(), message, session);

				// commit all changes
				tx.commit();
			} else {
				return false;
			}

		} catch (Exception e) {
			logger.error("Failed to store message", e);
			e.printStackTrace();
			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException e1) {
					e1.printStackTrace();
					return false;
				}
			}
			return false;

		} finally {
			try {
				session.close();
			} catch (HibernateException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		generateSchema();
		DatabaseManagerSwing.main(DatabaseConnection.GUI_ARGS);
	}

	public static List<?> runSQLQuery(String query) {
		logger.debug("runSQL: " + query);
		return runQuery(getHibernateSession().createSQLQuery(query));
	}

	public static Vector<?> runQuery(String query) {
		logger.debug("runSQL: " + query);
		Session hibernateSession = getHibernateSession();
		Query createQuery = hibernateSession.createQuery(query);
		return runQuery(createQuery);
	}

	public static Vector<?> runQuery(Query query) {
		logger.debug("runSQL: " + query);
		// TODO create prepared statements
		Vector<?> returnVal = null;
		try {
			returnVal = new Vector(query.list());
		} catch (GenericJDBCException dbe) {
			throw dbe;
		} catch (HibernateException e) {
			logger.error("Error fetching " + NewsGroup.class.getName(), e);
		}
		if (null == returnVal) {
			returnVal = new Vector<Object>();
		}
		return returnVal;
	}

	public static List<NewsGroup> storeNewsgroups(List<String> newsgroupsList,
			Message message, Session session) throws HibernateException {
		ListIterator<String> iter = newsgroupsList.listIterator();
		List<NewsGroup> groups = new Vector<NewsGroup>();
		while (iter.hasNext()) {
			String groupName = iter.next().trim();

			if (groupName.trim().length() > 0) {
				NewsGroup group = findOrCreateNewsGroup(session, groupName);

				/*
				 * NewsGroup
				 */
				group.getMessages().add(message);
				message.getNewsgroups().add(group);

				logger.debug("Num Msgs:" + group.getMessages());

				Topic topic = message.getTopic();
				group.getTopics().add(topic);
				topic.getNewsgroups().add(group);
				session.saveOrUpdate(topic);
				session.saveOrUpdate(message);

				logger.debug("Added " + message.getId() + " to "
						+ group.getName() + group.getId()
						+ group.getMessages().contains(message));

				session.saveOrUpdate(group);
				session.flush();

				logger.error("Stored " + group + " " + group.getId());
				// groups.add(group);
			}
		}
		return groups;
	}

	public static NewsGroup getNewsgroupByFullName(String groupName) {
		Query query = HibernateHelper.getHibernateSession().createQuery(
				"from " + NewsGroup.class.getName() + " where fullname=?");

		NewsGroup group = (NewsGroup) query.setString(0, groupName)
				.uniqueResult();
		return group;
	}

	public static void closeHibernateSessionFactory() throws HibernateException {
		sessionFactory.close();
	}
}
