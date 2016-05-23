/**
 * UNISoNController
 *
 * @author Stephen <github@leonarduk.com>
 * @since 22-May-2016
 */
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
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
public class UNISoNController extends Observable {

	/** The instance. */
	private static UNISoNController instance;

	/** The logger. */
	private static Logger logger = Logger.getLogger("UNISonController");

	/** The Constant LOCATION. */
	public static final String LOCATION = "Location";

	/** The Constant USENETUSER. */
	public static final String USENETUSER = UsenetUser.class.getName();

	/** The countries filter. */
	private Set<String> countriesFilter = null;

	/** The message queue. */
	private final LinkedBlockingQueue<NewsArticle> messageQueue;

	/** The nntp reader. */
	private final NewsGroupReader nntpReader;

	/** The topics filter. */
	private Set<Topic> topicsFilter = null;

	/** The filtered. */
	private boolean filtered = false;

	// private Vector<NewsGroup> selectedNewsgroups = null;

	/** The frame. */
	private JFrame frame;

	/** The from date. */
	private Date fromDate;

	/** The header downloader. */
	private final HeaderDownloadWorker headerDownloader = new HeaderDownloadWorker();

	/** The helper. */
	private final HibernateHelper helper;

	/** The matrix type. */
	private MatrixType matrixType;

	/** The message. */
	private Message message;

	/** The messages filter. */
	private Vector<Message> messagesFilter = null;

	/** The newsgroup filter. */
	private Set<NewsGroup> newsgroupFilter = null;

	/** The nntp host. */
	private String nntpHost;

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

	/** The tops newsgroups. */
	private Set<NewsGroup> topsNewsgroups;

	// private static UNISoNController instance;

	/** The usenet users filter. */
	private Vector<UsenetUser> usenetUsersFilter = null;

	/** The download panel. */
	private UNISoNLogger downloadPanel;

	/**
	 * Creates the.
	 */
	public static void create() {
		UNISoNController.instance = new UNISoNController();
	}

	/**
	 * Creates the.
	 *
	 * @param frame
	 *            the frame
	 * @return the UNI so n controller
	 */
	public static UNISoNController create(final JFrame frame) {
		UNISoNController.instance = new UNISoNController();
		UNISoNController.instance.frame = frame;
		return UNISoNController.instance;
	}

	/**
	 * Gets the single instance of UNISoNController.
	 *
	 * @return single instance of UNISoNController
	 */
	public static UNISoNController getInstance() {
		return UNISoNController.instance;
	}

	// private static UNISoNController instance;

	/**
	 * Sets the frame.
	 *
	 * @param frame2
	 *            the new frame
	 */
	protected static void setFrame(final UNISoNTabbedFrame frame2) {
		// TODO Auto-generated method stub

	}

	/**
	 * Instantiates a new UNI so n controller.
	 */
	private UNISoNController() {
		this.helper = new HibernateHelper(this);
		this.nntpReader = new NewsGroupReader(this);
		try {
			this.session = this.helper.getHibernateSession();
		}
		catch (final UNISoNException e) {
			this.showAlert("Error:" + e.getMessage());
		}
		this.messageQueue = new LinkedBlockingQueue<>();

	}

	// public static UNISoNController createInstance(final JFrame frame) {
	// return UNISoNController.instance = new UNISoNController(frame);
	// }
	//
	// public static UNISoNController getInstance() {
	// return UNISoNController.instance;
	// }

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

	// private Vector<NewsGroup> selectedNewsgroups = null;

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
		catch (@SuppressWarnings("unused") final java.net.UnknownHostException e) {
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
	public DefaultListModel<?> getCountriesFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Gets the download panel.
	 *
	 * @return the download panel
	 */
	public UNISoNLogger getDownloadPanel() {
		return this.downloadPanel;
	}

	/**
	 * Gets the header downloader.
	 *
	 * @return the header downloader
	 */
	public HeaderDownloadWorker getHeaderDownloader() {
		return this.headerDownloader;
	}

	/**
	 * Gets the locations filter.
	 *
	 * @return the locations filter
	 */
	public Set<String> getLocationsFilter() {
		return this.countriesFilter;
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
	 * @param session1
	 *            the session
	 * @return the messages
	 */
	public Set<Message> getMessages(final Topic topic, final Session session1) {
		final String query = "from  Message  where topic_id = " + topic.getId();
		final HashSet<Message> returnVal = new HashSet<>();
		for (final Message message1 : (List<Message>) this.helper.runQuery(query, session1,
		        Message.class)) {
			if (((null == this.selectedMessages) || (this.selectedMessages.size() == 0)
			        || this.selectedMessages.contains(message1))
			        && ((null == this.selectedPosters) || (this.selectedPosters.size() == 0)
			                || this.selectedPosters.contains(message1.getPoster()))) {
				returnVal.add(message1);
			}
		}

		return returnVal;
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
	 * Gets the nntp host.
	 *
	 * @return the nntp host
	 */
	public String getNntpHost() {
		return this.nntpHost;
	}

	/**
	 * Gets the nntp reader.
	 *
	 * @return the nntp reader
	 */
	public NewsGroupReader getNntpReader() {
		return this.nntpReader;
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
		return this.selectedNewsgroups;
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
		return this.selectedPosters;
	}

	/**
	 * Gets the top countries list.
	 *
	 * @return the top countries list
	 */
	public List<ResultRow> getTopCountriesList() {
		List<ResultRow> results = null;
		final HashMap<String, Integer> summaryMap = new HashMap<>();

		for (final ListIterator<Message> iter = this.messagesFilter.listIterator(); iter
		        .hasNext();) {
			final Message nextMessage = iter.next();

			String nextCountry;
			if ((null != nextMessage.getPoster()) && (null != nextMessage.getPoster().getLocation())
			        && (null != nextMessage.getPoster().getLocation().getCountry())) {
				nextCountry = nextMessage.getPoster().getLocation().getCountry();
			}
			else {
				nextCountry = "UNKNOWN";
			}

			Integer count = summaryMap.get(nextCountry);
			if (null == count) {
				count = Integer.valueOf(0);
			}
			summaryMap.put(nextCountry, Integer.valueOf(count.intValue() + 1));
		}
		results = new Vector<>();
		for (final Entry<String, Integer> entry : summaryMap.entrySet()) {
			results.add(new ResultRow(entry.getKey(), entry.getValue().intValue(), Location.class));
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
		final HashMap<NewsGroup, Integer> summaryMap = new HashMap<>();

		for (final ListIterator<Message> iter = this.messagesFilter.listIterator(); iter
		        .hasNext();) {
			for (final NewsGroup nextGroup : iter.next().getNewsgroups()) {
				if ((null == this.getSelectedNewsgroups())
				        || (this.getSelectedNewsgroups().size() == 0)
				        || this.getSelectedNewsgroups().contains(nextGroup)) {
					Integer count = summaryMap.get(nextGroup);
					if (null == count) {
						count = Integer.valueOf(0);
					}
					summaryMap.put(nextGroup, Integer.valueOf(count.intValue() + 1));

				}
			}
		}
		results = new Vector<>();
		for (final Entry<NewsGroup, Integer> entry : summaryMap.entrySet()) {
			results.add(
			        new ResultRow(entry.getKey(), entry.getValue().intValue(), NewsGroup.class));
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
	public Vector<Vector<Object>> getTopGroupsVector() throws HibernateException {

		final String sql = "SELECT count(*) as posts, newsgroup_id FROM newsgroup_message "
		        + " group by newsgroup_id " + " order by posts desc";

		final SQLQuery query = this.session.createSQLQuery(sql);

		final List<?> returnVal = query.list();

		final Vector<Vector<Object>> tableData = new Vector<>();
		final Iterator<?> iter = returnVal.iterator();
		while (iter.hasNext()) {
			final Vector<Object> row = new Vector<>();
			final Object[] array = (Object[]) iter.next();
			final int userID = ((Integer) array[1]).intValue();

			final List<NewsGroup> posters = this.helper.runQuery(
			        "from " + NewsGroup.class.getName() + " where id = " + userID, this.session,
			        NewsGroup.class);
			if (posters.size() > 0) {
				final NewsGroup usenetUser = posters.get(0);
				row.add(new GUIItem<>(usenetUser.getFullName(), usenetUser));
				row.add(array[0].toString());
			}
			else {
				UNISoNController.logger.warn("Poster " + userID + " not found");
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
		return this.topicsFilter;
	}

	/**
	 * Gets the top news groups.
	 *
	 * @return the top news groups
	 */
	public Set<NewsGroup> getTopNewsGroups() {
		return this.topsNewsgroups;
	}

	/**
	 * Gets the top posters.
	 *
	 * @return the top posters
	 */
	public Vector<ResultRow> getTopPosters() {
		Vector<ResultRow> results = null;
		final HashMap<UsenetUser, Integer> summaryMap = new HashMap<>();

		for (final ListIterator<Message> iter = this.messagesFilter.listIterator(); iter
		        .hasNext();) {
			final Message next = iter.next();

			// Want to check if any of the groups are selected
			boolean keep = true;
			if ((null != this.getSelectedNewsgroups())
			        && (this.getSelectedNewsgroups().size() > 0)) {
				final Set<NewsGroup> newsgroupsCopy = new HashSet<>();
				newsgroupsCopy.addAll(next.getNewsgroups());
				newsgroupsCopy.removeAll(this.getSelectedNewsgroups());
				if (newsgroupsCopy.size() == next.getNewsgroups().size()) {
					keep = false;
				}
			}

			final UsenetUser poster = next.getPoster();
			if (keep && ((null == this.getSelectedPosters())
			        || (this.getSelectedPosters().size() == 0)
			        || this.getSelectedPosters().contains(poster))) {
				Integer count = summaryMap.get(poster);
				if (null == count) {
					count = Integer.valueOf(0);
				}
				summaryMap.put(poster, Integer.valueOf(count.intValue() + 1));
			}
		}
		results = new Vector<>();
		for (final Entry<UsenetUser, Integer> entry : summaryMap.entrySet()) {
			results.add(
			        new ResultRow(entry.getKey(), entry.getValue().intValue(), UsenetUser.class));
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
		return this.usenetUsersFilter;
	}

	/**
	 * Helper.
	 *
	 * @return the hibernate helper
	 */
	public HibernateHelper helper() {
		return this.helper;
	}

	/**
	 * This is the main initialisation for the whole application.
	 */
	public void initialiseGUI() {
		this.frame = new JFrame();

		final JSplashScreen splash = new JSplashScreen("loading", this.frame);

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
	public Set<NNTPNewsGroup> listNewsgroups(final String searchString, final String host)
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
		this.setChanged();
		super.notifyObservers();
	}

	/**
	 * Quick download.
	 *
	 * @param groups
	 *            the groups
	 * @param fromDate1
	 *            the from date
	 * @param toDate1
	 *            the to date
	 * @param log
	 *            the log
	 * @param mode
	 *            the mode
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	public void quickDownload(final Set<NNTPNewsGroup> groups, final Date fromDate1,
	        final Date toDate1, final UNISoNLogger log, final DownloadMode mode)
	                throws UNISoNException {

		for (final NNTPNewsGroup group : groups) {
			try {
				this.nntpReader.client.reconnect();
				this.nntpReader.client.selectNewsgroup(group.getNewsgroup());
				this.nntpReader.setMessageCount(group.getArticleCount());
				this.headerDownloader.initialise(this.nntpReader, group.getFirstArticle(),
				        group.getLastArticle(), this.nntpHost, group.getNewsgroup(), log, mode,
				        fromDate1, toDate1);
			}
			catch (final IOException e) {
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
		        this.getSelectedPosters(), this.session, this.fromDate, this.toDate, this.filtered,
		        this.getSelectedNewsgroups(), this.getSelectedCountries());

		this.usenetUsersFilter = new Vector<>();
		this.newsgroupFilter = new HashSet<>();
		this.topsNewsgroups = new HashSet<>();
		this.topicsFilter = new HashSet<>();
		this.countriesFilter = new HashSet<>();
		for (final Message message1 : this.messagesFilter) {
			UsenetUser poster = null;
			try {
				if (this.session.contains(message1)) {
					this.session.refresh(message1);
				}
			}
			catch (final org.hibernate.UnresolvableObjectException e) {
				UNISoNController.logger.warn(e);
			}
			try {
				poster = message1.getPoster();
				if (this.session.contains(poster)) {
					this.session.refresh(poster);
				}
			}
			catch (final org.hibernate.UnresolvableObjectException e) {
				UNISoNController.logger.warn(e);
			}

			if (!this.usenetUsersFilter.contains(poster)) {
				// if (!filtered || null == selectedPosters
				// || selectedPosters.contains(message.getPoster())) {
				this.usenetUsersFilter.add(poster);
				String country;
				if ((null != poster) && (null != poster.getLocation())
				        && (null != poster.getLocation().getCountry())) {
					country = poster.getLocation().getCountry();
				}
				else {
					country = "UNKNOWN";
				}

				this.countriesFilter.add(country);
			}
			if (!this.topicsFilter.contains(message1.getTopic())) {
				this.topicsFilter.add(message1.getTopic());
			}

			for (NewsGroup group : message1.getNewsgroups()) {
				try {
					if (this.session.contains(group)) {
						this.session.refresh(group);
					}
				}
				catch (final org.hibernate.UnresolvableObjectException e) {
					UNISoNController.logger.warn(e);
				}

				if (!this.newsgroupFilter.contains(group)) {
					this.newsgroupFilter.add(group);
					while (null != group.getParentNewsGroup()) {
						group = group.getParentNewsGroup();
					}
					this.topsNewsgroups.add(group);
				}
			}
		}
		this.notifyObservers();

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
		this.setButtonState(false, true, false, true);
	}

	/**
	 * Sets the connecting state.
	 */
	public void setConnectingState() {
		this.setButtonState(false, false, false, true);
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
		this.setButtonState(false, false, true, true);
	}

	/**
	 * Sets the download panel.
	 *
	 * @param downloadPanel
	 *            the new download panel
	 */
	public void setDownloadPanel(final UNISoNLogger downloadPanel) {
		this.downloadPanel = downloadPanel;
	}

	/**
	 * Once the header download worker completes it will call this. This method will tell the
	 * download panel to update itself.
	 */
	public void setHeaderDownloaderFinished() {
		this.headerDownloader.notifyObservers();
	}

	/**
	 * Sets the idle state.
	 */
	public void setIdleState() {
		this.setButtonState(true, false, false, false);
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
	public void setNntpHost(final String nntpHost) {
		this.nntpHost = nntpHost;
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
		if (!groupName.equals("")) {
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

	/**
	 * Show alert.
	 *
	 * @param message
	 *            the message
	 */
	public void showAlert(final String messageText) {
		JOptionPane.showMessageDialog(this.frame, messageText);
	}

	/**
	 * Show error message.
	 *
	 * @param message
	 *            the message
	 */
	private void showErrorMessage(final String messageText) {
		// this.frame.showErrorMessage(message);
		UNISoNController.logger.warn(messageText);
	}

	/**
	 * Show status.
	 *
	 * @param message1
	 *            the message
	 */
	public void showStatus(final String message1) {
		// CLI version does not do this
		if (null != this.frame) {
			this.showAlert(message1);
		}
	}

	/**
	 * Stop download.
	 */
	public void stopDownload() {
		DataHibernatorWorker.stopDownload();
		this.setIdleState();
	}

	/**
	 * Store newsgroups.
	 *
	 * @param newsgroups
	 *            the newsgroups
	 */
	public void storeNewsgroups(final Set<NNTPNewsGroup> newsgroups) {
		this.helper.storeNewsgroups(newsgroups, this.session);
	}

	/**
	 * Switch filtered.
	 *
	 * @param on
	 *            the on
	 */
	public void switchFiltered(final boolean on) {
		this.filtered = on;
		this.refreshDataFromDatabase();
	}

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
}
