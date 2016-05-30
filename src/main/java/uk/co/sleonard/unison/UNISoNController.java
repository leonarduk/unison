/**
 * UNISoNController
 *
 * @author Stephen <github@leonarduk.com>
 * @since 22-May-2016
 */
package uk.co.sleonard.unison;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import uk.co.sleonard.unison.datahandling.DataQuery;
import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.gui.UNISoNGUI;
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
	private static Logger logger = Logger.getLogger(UNISoNController.class);

	private static UNISoNGUI gui;

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

	/** The usenet users filter. */
	private Vector<UsenetUser> usenetUsersFilter = null;

	/** The download panel. */
	private UNISoNLogger downloadPanel;

	private final UNISoNAnalysis analysis;

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
		UNISoNController.setGui(new UNISoNGUI(frame));
		return UNISoNController.instance;
	}

	public static UNISoNGUI getGui() {
		return UNISoNController.gui;
	}

	// private static UNISoNController instance;

	/**
	 * Gets the single instance of UNISoNController.
	 *
	 * @return single instance of UNISoNController
	 */
	public static UNISoNController getInstance() {
		return UNISoNController.instance;
	}

	/**
	 * Sets the frame.
	 *
	 * @param frame2
	 *            the new frame
	 */
	protected static void setFrame(final UNISoNTabbedFrame frame2) {
		// TODO Auto-generated method stub

	}

	public static void setGui(final UNISoNGUI gui) {
		UNISoNController.gui = gui;
	}

	/**
	 * Instantiates a new UNI so n controller.
	 */
	private UNISoNController() {
		this.analysis = new UNISoNAnalysis(this);
		this.helper = new HibernateHelper(this);
		this.nntpReader = new NewsGroupReader(this);
		try {
			this.setSession(this.getHelper().getHibernateSession());
		}
		catch (final UNISoNException e) {
			UNISoNController.getGui().showAlert("Error:" + e.getMessage());
		}
		this.messageQueue = new LinkedBlockingQueue<>();

	}

	/**
	 * Cancel download.
	 */
	public void cancelDownload() {
		this.stopDownload();
	}

	/**
	 * Connect to news group.
	 *
	 * @param newsgroup
	 *            the newsgroup
	 * @deprecated
	 */
	@Deprecated
	private void connectToNewsGroup(final String newsgroup) {
		this.setConnectingState();
		UNISoNController.getGui().showStatus("Connect to " + newsgroup);

		// TODO need to filter by data and allow more than one newsgroup
		this.setSelectedNewsgroup(newsgroup);
		final String host = null;// this.frame.getSelectedHost();
		try {
			this.nntpReader.client.connectToNewsGroup(host, newsgroup);
			this.setConnectedState();

			UNISoNController.getGui()
			        .showStatus("MESSAGES:" + this.nntpReader.getNumberOfMessages());
		}
		catch (@SuppressWarnings("unused") final java.net.UnknownHostException e) {
			this.showErrorMessage(newsgroup + " not found on " + host);
		}
		catch (final Exception e) {
			this.showErrorMessage("ERROR: " + e);
		}
	}

	public UNISoNAnalysis getAnalysis() {
		return this.analysis;
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

	public HibernateHelper getHelper() {
		return this.helper;
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
		for (final Message message1 : (List<Message>) this.getHelper().runQuery(query, session1,
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
	public Vector<NewsGroup> getSelectedNewsgroups() {
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
	public Vector<UsenetUser> getSelectedPosters() {
		if (!this.filtered) {
			return null;
		}
		return this.selectedPosters;
	}

	public Session getSession() {
		return this.session;
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
		return this.getHelper();
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
		        this.getSelectedPosters(), this.getSession(), this.fromDate, this.toDate,
		        this.filtered, this.getSelectedNewsgroups(), this.getSelectedCountries());

		this.usenetUsersFilter = new Vector<>();
		this.newsgroupFilter = new HashSet<>();
		this.topsNewsgroups = new HashSet<>();
		this.topicsFilter = new HashSet<>();
		this.countriesFilter = new HashSet<>();
		for (final Message message1 : this.messagesFilter) {
			UsenetUser poster = null;
			try {
				if (this.getSession().contains(message1)) {
					this.getSession().refresh(message1);
				}
			}
			catch (final org.hibernate.UnresolvableObjectException e) {
				UNISoNController.logger.warn(e);
			}
			try {
				poster = message1.getPoster();
				if (this.getSession().contains(poster)) {
					this.getSession().refresh(poster);
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
					if (this.getSession().contains(group)) {
						this.getSession().refresh(group);
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
			group = this.getHelper().getNewsgroupByFullName(groupName, this.getSession());
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

	public void setSession(final Session session) {
		this.session = session;
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
		this.getHelper().storeNewsgroups(newsgroups, this.getSession());
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
}
