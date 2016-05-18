package uk.co.sleonard.unison.gui;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import uk.co.sleonard.unison.datahandling.DataQuery;
import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.datahandling.DAO.Location;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.ResultRow;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.gui.generated.UNISoNTabbedFrame;
import uk.co.sleonard.unison.input.DataHibernatorWorker;
import uk.co.sleonard.unison.input.HeaderDownloadWorker;
import uk.co.sleonard.unison.input.NNTPNewsGroup;
import uk.co.sleonard.unison.input.NewsArticle;
import uk.co.sleonard.unison.input.NewsGroupReader;

/**
 * The Class UNISoNController.
 */
public class UNISoNController extends Observable {

	/**
	 * The Enum MatrixType.
	 */
	public enum MatrixType {

		/** The reply to all. */
		REPLY_TO_ALL, /** The reply to first. */
		REPLY_TO_FIRST, /** The reply to last. */
		REPLY_TO_LAST
	}

	/**
	 * The Class NewsGroupComparator.
	 */
	class NewsGroupComparator implements Comparator<NewsGroup> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(final NewsGroup first, final NewsGroup second) {
			return first.getName().compareTo(second.getName());
		}

	}

	/**
	 * The Class TopicComparator.
	 */
	public class TopicComparator implements Comparator<Topic> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(final Topic first, final Topic second) {
			return first.getSubject().compareTo(second.getSubject());
		}

	}

	/** The instance. */
	private static UNISoNController instance;

	// private static UNISoNController instance;

	/** The Constant LOCATION. */
	public static final String LOCATION = "Location";

	/** The logger. */
	private static Logger logger = Logger.getLogger("UNISonController");

	/** The Constant USENETUSER. */
	public static final String USENETUSER = UsenetUser.class.getName();

	/**
	 * Creates the.
	 */
	public static void create() {
		instance = new UNISoNController();
	}

	/**
	 * Creates the.
	 *
	 * @param frame
	 *            the frame
	 * @return the UNI so n controller
	 */
	public static UNISoNController create(JFrame frame) {
		instance = new UNISoNController();
		instance.frame = frame;
		return instance;
	}

	// public static UNISoNController createInstance(final JFrame frame) {
	// return UNISoNController.instance = new UNISoNController(frame);
	// }
	//
	// public static UNISoNController getInstance() {
	// return UNISoNController.instance;
	// }

	/**
	 * Gets the single instance of UNISoNController.
	 *
	 * @return single instance of UNISoNController
	 */
	public static UNISoNController getInstance() {
		return instance;
	}

	/**
	 * Sets the frame.
	 *
	 * @param frame2
	 *            the new frame
	 */
	protected static void setFrame(UNISoNTabbedFrame frame2) {
		// TODO Auto-generated method stub

	}

	/** The filtered. */
	private boolean filtered = false;

	/** The frame. */
	private JFrame frame;

	/** The from date. */
	private Date fromDate;

	/** The header downloader. */
	private HeaderDownloadWorker headerDownloader = new HeaderDownloadWorker();

	/** The helper. */
	private HibernateHelper helper;

	// private Vector<NewsGroup> selectedNewsgroups = null;

	/** The countries filter. */
	private Set<String> countriesFilter = null;

	/** The matrix type. */
	private MatrixType matrixType;

	/** The message. */
	private Message message;

	/** The message queue. */
	private final LinkedBlockingQueue<NewsArticle> messageQueue;

	/** The messages filter. */
	private Vector<Message> messagesFilter = null;

	/** The newsgroup filter. */
	private Set<NewsGroup> newsgroupFilter = null;

	/** The nntp host. */
	private String nntpHost;

	/** The nntp reader. */
	private NewsGroupReader nntpReader;

	/** The selected countries. */
	Set<String> selectedCountries = null;

	/** The selected messages. */
	private final Vector<Message> selectedMessages = null;

	/** The selected newsgroup. */
	private NewsGroup selectedNewsgroup;

	/** The selected newsgroups. */
	private Vector<NewsGroup> selectedNewsgroups;

	/** The selected posters. */
	private Vector<UsenetUser> selectedPosters;

	/** The session. */
	private Session session;

	/** The to date. */
	private Date toDate;

	/** The topics filter. */
	private Set<Topic> topicsFilter = null;

	/** The tops newsgroups. */
	private Set<NewsGroup> topsNewsgroups;

	/** The usenet users filter. */
	private Vector<UsenetUser> usenetUsersFilter = null;

	/** The download panel. */
	private UNISoNLogger downloadPanel;

	/**
	 * Instantiates a new UNI so n controller.
	 */
	private UNISoNController() {
		helper = new HibernateHelper(this);
		this.nntpReader = new NewsGroupReader(this);
		try {
			this.session = helper.getHibernateSession();
		}
		catch (UNISoNException e) {
			showAlert("Error:" + e.getMessage());
		}
		this.messageQueue = new LinkedBlockingQueue<NewsArticle>();

	}

	/**
	 * Ask question.
	 *
	 * @param question
	 *            the question
	 * @param options
	 *            the options
	 * @param title
	 *            the title
	 * @param defaultOption
	 *            the default option
	 * @return the int
	 */
	public int askQuestion(final String question, final String[] options, final String title,
	        final String defaultOption) {
		final int response = JOptionPane.showOptionDialog(this.frame, question, title,
		        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options,
		        defaultOption);
		return response;
	}

	/**
	 * Cancel download.
	 */
	public void cancelDownload() {
		this.stopDownload();
	}

	// /**
	// *
	// * @param groups
	// * @param updateLocation
	// * @throws UNISoNException
	// * @throws IOException
	// */
	// public void downloadMessages(final Set<NNTPNewsGroup> groups)
	// throws UNISoNException {
	// this.nntpReader.startHibernators(1);
	//
	// for (final NNTPNewsGroup group : groups) {
	// try {
	// this.nntpReader.startDownloaders(3);
	// } catch (IOException e) {
	// throw new UNISoNException(e);
	// }
	// }
	// }

	/**
	 * Connect to news group.
	 *
	 * @param newsgroup
	 *            the newsgroup
	 * @deprecated
	 */
	@Deprecated
	public void connectToNewsGroup(final String newsgroup) {
		this.setConnectingState();
		this.showStatus("Connect to " + newsgroup);

		// TODO need to filter by data and allow more than one newsgroup
		this.setSelectedNewsgroup(newsgroup);
		final String host = null;// this.frame.getSelectedHost();
		try {
			this.nntpReader.client.connectToNewsGroup(host, newsgroup);
			this.setConnectedState();

			this.showStatus("MESSAGES:" + this.nntpReader.getNumberOfMessages());
		}
		catch (final java.net.UnknownHostException e) {
			this.showErrorMessage(newsgroup + " not found on " + host);
		}
		catch (final Exception e) {
			this.showErrorMessage("ERROR: " + e);
		}
	}

	/**
	 * Gets the countries filter.
	 *
	 * @return the countries filter
	 */
	// public void downloadMessages(String newsgroup, Date toDate, Date
	// fromDate)
	// throws UNISoNException {
	// this.setDownloadingState(0);
	// logger.info("Start download");
	// if ((null != this.nntpReader) && this.nntpReader.isConnected()) {
	// this.nntpReader.startDownload(newsgroup, fromDate, toDate);
	// } else {
	// System.out.println("Not connected!");
	// }
	// }
	/**
	 * 
	 * @return
	 */
	public DefaultListModel getCountriesFilter() {

		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Gets the header downloader.
	 *
	 * @return the header downloader
	 */
	public HeaderDownloadWorker getHeaderDownloader() {
		return headerDownloader;
	}

	/**
	 * Gets the locations filter.
	 *
	 * @return the locations filter
	 */
	public Set<String> getLocationsFilter() {
		return countriesFilter;
	}

	/**
	 * Gets the matrix type.
	 *
	 * @return the matrix type
	 */
	public MatrixType getMatrixType() {
		return this.matrixType;
	}

	/**
	 * Gets the messages.
	 *
	 * @param topic
	 *            the topic
	 * @param session
	 *            the session
	 * @return the messages
	 */
	@SuppressWarnings("unchecked")
	public Set<Message> getMessages(final Topic topic, Session session) {
		final String query = "from  Message  where topic_id = " + topic.getId();
		final HashSet<Message> returnVal = new HashSet<Message>();
		for (Message message : (List<Message>) helper.runQuery(query, session, Message.class)) {
			if ((null == selectedMessages || selectedMessages.size() == 0
			        || selectedMessages.contains(message))
			        && (null == selectedPosters || selectedPosters.size() == 0
			                || selectedPosters.contains(message.getPoster())))
			    returnVal.add(message);
		}

		return returnVal;
	}

	/**
	 * Gets the messages filter.
	 *
	 * @return the messages filter
	 */
	public Vector<Message> getMessagesFilter() {
		return messagesFilter;
	}

	/**
	 * Gets the newsgroup filter.
	 *
	 * @return the newsgroup filter
	 */
	public Set<NewsGroup> getNewsgroupFilter() {
		return newsgroupFilter;
	}

	/**
	 * Gets the nntp host.
	 *
	 * @return the nntp host
	 */
	public String getNntpHost() {
		return nntpHost;
	}

	/**
	 * Gets the nntp reader.
	 *
	 * @return the nntp reader
	 */
	public NewsGroupReader getNntpReader() {
		return nntpReader;
	}

	/**
	 * Gets the queue.
	 *
	 * @return the queue
	 */
	public LinkedBlockingQueue<NewsArticle> getQueue() {
		DataHibernatorWorker.startHibernators();
		return this.messageQueue;
	}

	/**
	 * Gets the selected countries.
	 *
	 * @return the selected countries
	 */
	private Set<String> getSelectedCountries() {
		return this.selectedCountries;
	}

	/**
	 * Gets the selected message.
	 *
	 * @return the selected message
	 */
	public Message getSelectedMessage() {
		if (this.filtered && !this.messagesFilter.contains(this.message)) {
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
		if (this.filtered && !this.newsgroupFilter.contains(this.selectedNewsgroup)) {
			return null;
		}
		return this.selectedNewsgroup;
	}

	/**
	 * Gets the selected newsgroups.
	 *
	 * @return the selected newsgroups
	 */
	private Vector<NewsGroup> getSelectedNewsgroups() {
		if (!this.filtered) {
			return null;
		}
		return selectedNewsgroups;
	}

	/**
	 * Gets the selected posters.
	 *
	 * @return the selected posters
	 */
	private Vector<UsenetUser> getSelectedPosters() {
		if (!this.filtered) {
			return null;
		}
		return selectedPosters;
	}

	/**
	 * Gets the top countries list.
	 *
	 * @return the top countries list
	 */
	public List<ResultRow> getTopCountriesList() {
		List<ResultRow> results = null;
		HashMap<String, Integer> summaryMap = new HashMap<String, Integer>();

		for (ListIterator<Message> iter = messagesFilter.listIterator(); iter.hasNext();) {
			Message nextMessage = iter.next();

			String nextCountry;
			if (null != nextMessage.getPoster() && null != nextMessage.getPoster().getLocation()
			        && null != nextMessage.getPoster().getLocation().getCountry()) {
				nextCountry = nextMessage.getPoster().getLocation().getCountry();
			}
			else {
				nextCountry = "UNKNOWN";
			}

			Integer count = summaryMap.get(nextCountry);
			if (null == count) {
				count = 0;
			}
			count++;
			summaryMap.put(nextCountry, count);
		}
		results = new Vector<ResultRow>();
		for (Iterator<Entry<String, Integer>> iter = summaryMap.entrySet().iterator(); iter
		        .hasNext();) {
			Entry<String, Integer> entry = iter.next();
			results.add(new ResultRow(entry.getKey(), entry.getValue(), Location.class));
		}
		Collections.sort(results);
		return results;
	}

	/**
	 * Gets the top groups list.
	 *
	 * @return the top groups list
	 */
	public List<ResultRow> getTopGroupsList() {
		List<ResultRow> results = null;
		HashMap<NewsGroup, Integer> summaryMap = new HashMap<NewsGroup, Integer>();

		for (ListIterator<Message> iter = messagesFilter.listIterator(); iter.hasNext();) {
			for (NewsGroup nextGroup : iter.next().getNewsgroups()) {
				if (null == this.getSelectedNewsgroups() || this.getSelectedNewsgroups().size() == 0
				        || this.getSelectedNewsgroups().contains(nextGroup)) {
					Integer count = summaryMap.get(nextGroup);
					if (null == count) {
						count = 0;
					}
					count++;
					summaryMap.put(nextGroup, count);
				}
			}
		}
		results = new Vector<ResultRow>();
		for (Iterator<Entry<NewsGroup, Integer>> iter = summaryMap.entrySet().iterator(); iter
		        .hasNext();) {
			Entry<NewsGroup, Integer> entry = iter.next();
			results.add(new ResultRow(entry.getKey(), entry.getValue(), NewsGroup.class));
		}
		Collections.sort(results);
		return results;
	}

	/**
	 * Gets the top groups vector.
	 *
	 * @return the top groups vector
	 * @throws HibernateException
	 *             the hibernate exception
	 */
	@SuppressWarnings({ "unchecked", "unchecked" })
	public Vector<Vector<Object>> getTopGroupsVector() throws HibernateException {

		final String sql = "SELECT count(*) as posts, newsgroup_id FROM newsgroup_message "
		        + " group by newsgroup_id " + " order by posts desc";

		final SQLQuery query = session.createSQLQuery(sql);

		final List<?> returnVal = query.list();

		final Vector<Vector<Object>> tableData = new Vector<Vector<Object>>();
		final Iterator<?> iter = returnVal.iterator();
		while (iter.hasNext()) {
			final Vector<Object> row = new Vector<Object>();
			final Object[] array = (Object[]) iter.next();
			final int userID = (Integer) array[1];

			final List<NewsGroup> posters = helper.runQuery(
			        "from " + NewsGroup.class.getName() + " where id = " + userID, session,
			        NewsGroup.class);
			if (posters.size() > 0) {
				final NewsGroup usenetUser = posters.get(0);
				row.add(new GUIItem<NewsGroup>(usenetUser.getFullName(), usenetUser));
				row.add(array[0].toString());
			}
			else {
				logger.warn("Poster " + userID + " not found");
			}
			tableData.add(row);
		}
		return tableData;
	}

	/**
	 * Gets the topics filter.
	 *
	 * @return the topics filter
	 */
	public Set<Topic> getTopicsFilter() {
		return topicsFilter;
	}

	/**
	 * Gets the top news groups.
	 *
	 * @return the top news groups
	 */
	public Set<NewsGroup> getTopNewsGroups() {
		return topsNewsgroups;
	}

	/**
	 * Gets the top posters.
	 *
	 * @return the top posters
	 */
	public Vector<ResultRow> getTopPosters() {
		Vector<ResultRow> results = null;
		HashMap<UsenetUser, Integer> summaryMap = new HashMap<UsenetUser, Integer>();

		for (ListIterator<Message> iter = messagesFilter.listIterator(); iter.hasNext();) {
			Message next = iter.next();

			// Want to check if any of the groups are selected
			boolean keep = true;
			if (null != this.getSelectedNewsgroups() && this.getSelectedNewsgroups().size() > 0) {
				Set<NewsGroup> newsgroupsCopy = new HashSet<NewsGroup>();
				newsgroupsCopy.addAll(next.getNewsgroups());
				newsgroupsCopy.removeAll(this.getSelectedNewsgroups());
				if (newsgroupsCopy.size() == next.getNewsgroups().size()) {
					keep = false;
				}
			}

			UsenetUser poster = next.getPoster();
			if (keep && (null == getSelectedPosters() || getSelectedPosters().size() == 0
			        || getSelectedPosters().contains(poster))) {
				Integer count = summaryMap.get(poster);
				if (null == count) {
					count = 0;
				}
				summaryMap.put(poster, ++count);
			}
		}
		results = new Vector<ResultRow>();
		for (Iterator<Entry<UsenetUser, Integer>> iter = summaryMap.entrySet().iterator(); iter
		        .hasNext();) {
			Entry<UsenetUser, Integer> entry = iter.next();
			results.add(new ResultRow(entry.getKey(), entry.getValue(), UsenetUser.class));
		}
		Collections.sort(results);
		return results;
	}

	/**
	 * Gets the usenet users filter.
	 *
	 * @return the usenet users filter
	 */
	public Vector<UsenetUser> getUsenetUsersFilter() {
		return usenetUsersFilter;
	}

	/**
	 * Helper.
	 *
	 * @return the hibernate helper
	 */
	public HibernateHelper helper() {
		return helper;
	}

	/**
	 * This is the main initialisation for the whole application.
	 */
	public void initialiseGUI() {
		this.frame = new JFrame();

		final JSplashScreen splash = new JSplashScreen("loading", frame);

		splash.setProgress(10);

		this.frame.setVisible(true);
		splash.setProgress(50);

		this.refreshDataFromDatabase();

		this.setIdleState();

		splash.setProgress(100);

		splash.close();
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
	 * List newsgroups.
	 *
	 * @param searchString
	 *            the search string
	 * @param host
	 *            the host
	 * @return the sets the
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	public Set<NNTPNewsGroup> listNewsgroups(final String searchString, String host)
	        throws UNISoNException {

		this.nntpHost = host;
		return this.nntpReader.client.listNNTPNewsgroups(searchString, host);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observable#notifyObservers()
	 */
	@Override
	public void notifyObservers() {
		setChanged();
		super.notifyObservers();
	}

	/**
	 * Quick download.
	 *
	 * @param groups
	 *            the groups
	 * @param fromDate
	 *            the from date
	 * @param toDate
	 *            the to date
	 * @param log
	 *            the log
	 * @param mode
	 *            the mode
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	public void quickDownload(Set<NNTPNewsGroup> groups, Date fromDate, Date toDate,
	        UNISoNLogger log, DownloadMode mode) throws UNISoNException {

		for (final NNTPNewsGroup group : groups) {
			try {
				this.nntpReader.client.reconnect();
				this.nntpReader.client.selectNewsgroup(group.getNewsgroup());
				this.nntpReader.setMessageCount(group.getArticleCount());
				headerDownloader.initialise(nntpReader, group.getFirstArticle(),
				        group.getLastArticle(), nntpHost, group.getNewsgroup(), log, mode, fromDate,
				        toDate);
			}
			catch (IOException e) {
				e.printStackTrace();
				throw new UNISoNException(
				        "Error downloading messages. Check your internet connection: ", e);
			}
		}
	}

	/**
	 * Refresh data from database.
	 */
	public void refreshDataFromDatabase() {

		UNISoNController.logger.debug("refreshDataFromDatabase");

		this.messagesFilter = DataQuery.getInstance().getMessages(this.selectedMessages,
		        this.getSelectedPosters(), session, this.fromDate, this.toDate, this.filtered,
		        this.getSelectedNewsgroups(), this.getSelectedCountries());

		this.usenetUsersFilter = new Vector<UsenetUser>();
		this.newsgroupFilter = new HashSet<NewsGroup>();
		this.topsNewsgroups = new HashSet<NewsGroup>();
		this.topicsFilter = new HashSet<Topic>();
		this.countriesFilter = new HashSet<String>();
		for (Message message : this.messagesFilter) {
			UsenetUser poster = null;
			try {
				if (session.contains(message)) {
					session.refresh(message);
				}
			}
			catch (org.hibernate.UnresolvableObjectException e) {
				// ignore
			}
			try {
				poster = message.getPoster();
				if (session.contains(poster)) {
					session.refresh(poster);
				}
			}
			catch (org.hibernate.UnresolvableObjectException e) {
				// ignore
			}

			if (!usenetUsersFilter.contains(poster)) {
				// if (!filtered || null == selectedPosters
				// || selectedPosters.contains(message.getPoster())) {
				usenetUsersFilter.add(poster);
				String country;
				if (null != poster && null != poster.getLocation()
				        && null != poster.getLocation().getCountry()) {
					country = poster.getLocation().getCountry();
				}
				else {
					country = "UNKNOWN";
				}

				countriesFilter.add(country);
			}
			if (!topicsFilter.contains(message.getTopic())) {
				topicsFilter.add(message.getTopic());
			}

			for (NewsGroup group : message.getNewsgroups()) {
				try {
					if (session.contains(group)) {
						session.refresh(group);
					}
				}
				catch (org.hibernate.UnresolvableObjectException e) {
					// ignore
				}

				if (!newsgroupFilter.contains(group)) {
					newsgroupFilter.add(group);
					while (null != group.getParentNewsGroup()) {
						group = group.getParentNewsGroup();
					}
					topsNewsgroups.add(group);
				}
			}
		}
		notifyObservers();

	}

	/**
	 * Sets the button state.
	 *
	 * @param connectButtonState
	 *            the connect button state
	 * @param downloadButtonState
	 *            the download button state
	 * @param pauseButtonState
	 *            the pause button state
	 * @param cancelButtonState
	 *            the cancel button state
	 */
	private void setButtonState(final boolean connectButtonState, final boolean downloadButtonState,
	        final boolean pauseButtonState, final boolean cancelButtonState) {
		// The command line version does not do this
		// if (null != this.frame) {
		// this.frame.setButtonState(connectButtonState, downloadButtonState,
		// pauseButtonState, cancelButtonState);
		// }
	}

	/**
	 * Sets the connected state.
	 */
	public void setConnectedState() {
		setButtonState(false, true, false, true);
	}

	/**
	 * Sets the connecting state.
	 */
	public void setConnectingState() {
		setButtonState(false, false, false, true);
	}

	/**
	 * Sets the dates.
	 *
	 * @param fromDate2
	 *            the from date2
	 * @param toDate2
	 *            the to date2
	 */
	public void setDates(Date fromDate2, Date toDate2) {
		this.toDate = toDate2;
		this.fromDate = fromDate2;
	}

	/**
	 * Sets the downloading state.
	 *
	 * @param progress
	 *            the new downloading state
	 */
	public void setDownloadingState(final int progress) {
		setButtonState(false, false, true, true);
	}

	/**
	 * Once the header download worker completes it will call this. This method will tell the
	 * download panel to update itself.
	 */
	public void setHeaderDownloaderFinished() {
		headerDownloader.notifyObservers();
	}

	/**
	 * Sets the idle state.
	 */
	public void setIdleState() {
		setButtonState(true, false, false, false);
	}

	/**
	 * Sets the matrix type.
	 *
	 * @param type
	 *            the new matrix type
	 */
	public void setMatrixType(final MatrixType type) {
		this.matrixType = type;
	}

	/**
	 * Sets the nntp host.
	 *
	 * @param nntpHost
	 *            the new nntp host
	 */
	public void setNntpHost(String nntpHost) {
		this.nntpHost = nntpHost;
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
		if (!groupName.equals("")) {
			group = helper.getNewsgroupByFullName(groupName, session);
		}
		this.setSelectedNewsgroup(group);
	}

	/**
	 * Sets the selected newsgroups.
	 *
	 * @param groups
	 *            the new selected newsgroups
	 */
	public void setSelectedNewsgroups(Vector<NewsGroup> groups) {
		this.selectedNewsgroups = groups;
	}

	/**
	 * Sets the selected posters.
	 *
	 * @param posters
	 *            the new selected posters
	 */
	public void setSelectedPosters(Vector<UsenetUser> posters) {
		this.selectedPosters = posters;
	}

	/**
	 * Show alert.
	 *
	 * @param message
	 *            the message
	 */
	public void showAlert(String message) {
		JOptionPane.showMessageDialog(this.frame, message);
	}

	/**
	 * Show error message.
	 *
	 * @param message
	 *            the message
	 */
	private void showErrorMessage(final String message) {
		// this.frame.showErrorMessage(message);
	}

	/**
	 * Show status.
	 *
	 * @param message
	 *            the message
	 */
	public void showStatus(final String message) {
		// CLI version does not do this
		if (null != this.frame) {
			showAlert(message);
		}
	}

	/**
	 * Stop download.
	 */
	public void stopDownload() {
		DataHibernatorWorker.stopDownload();
		setIdleState();
	}

	/**
	 * Store newsgroups.
	 *
	 * @param newsgroups
	 *            the newsgroups
	 */
	public void storeNewsgroups(final Set<NNTPNewsGroup> newsgroups) {
		helper.storeNewsgroups(newsgroups, session);
	}

	/**
	 * Switch filtered.
	 *
	 * @param on
	 *            the on
	 */
	public void switchFiltered(boolean on) {
		this.filtered = on;
		this.refreshDataFromDatabase();
	}

	/**
	 * Sets the selected countries.
	 *
	 * @param countries
	 *            the new selected countries
	 */
	public void setSelectedCountries(Set<String> countries) {
		this.selectedCountries = countries;
	}

	/**
	 * Gets the download panel.
	 *
	 * @return the download panel
	 */
	public UNISoNLogger getDownloadPanel() {
		return downloadPanel;
	}

	/**
	 * Sets the download panel.
	 *
	 * @param downloadPanel
	 *            the new download panel
	 */
	public void setDownloadPanel(UNISoNLogger downloadPanel) {
		this.downloadPanel = downloadPanel;
	}
}
