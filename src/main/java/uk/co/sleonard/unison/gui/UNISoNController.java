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
import java.util.Observable;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;
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
import uk.co.sleonard.unison.datahandling.DAO.Location;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.ResultRow;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.gui.generated.UNISoNTabbedFrame;
import uk.co.sleonard.unison.input.DataHibernatorWorker;
import uk.co.sleonard.unison.input.HeaderDownloadWorker;
import uk.co.sleonard.unison.input.NNTPNewsGroup;
import uk.co.sleonard.unison.input.NewsArticle;
import uk.co.sleonard.unison.input.NewsGroupReader;

public class UNISoNController extends Observable {

	public enum MatrixType {
		REPLY_TO_ALL, REPLY_TO_FIRST, REPLY_TO_LAST
	}

	class NewsGroupComparator implements Comparator<NewsGroup> {

		public int compare(final NewsGroup first, final NewsGroup second) {
			return first.getName().compareTo(second.getName());
		}

	}

	public class TopicComparator implements Comparator<Topic> {

		public int compare(final Topic first, final Topic second) {
			return first.getSubject().compareTo(second.getSubject());
		}

	}

	private static UNISoNController instance;

	// private static UNISoNController instance;

	public static final String LOCATION = "Location";

	private static Logger logger = Logger.getLogger("UNISonController");

	public static final String USENETUSER = UsenetUser.class.getName();

	public static void create() {
		instance = new UNISoNController();
	}

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

	public static UNISoNController getInstance() {
		return instance;
	}

	protected static void setFrame(UNISoNTabbedFrame frame2) {
		// TODO Auto-generated method stub

	}

	private boolean filtered = false;

	private JFrame frame;

	private Date fromDate;

	private HeaderDownloadWorker headerDownloader = new HeaderDownloadWorker();

	private HibernateHelper helper;

	// private Vector<NewsGroup> selectedNewsgroups = null;

	private Set<String> countriesFilter = null;

	private MatrixType matrixType;

	private Message message;

	private final LinkedBlockingQueue<NewsArticle> messageQueue;

	private Vector<Message> messagesFilter = null;

	private Set<NewsGroup> newsgroupFilter = null;

	private String nntpHost;

	private NewsGroupReader nntpReader;

	Set<String> selectedCountries = null;

	private final Vector<Message> selectedMessages = null;

	private NewsGroup selectedNewsgroup;

	private Vector<NewsGroup> selectedNewsgroups;

	private Vector<UsenetUser> selectedPosters;

	private Session session;

	private Date toDate;

	private Set<Topic> topicsFilter = null;

	private Set<NewsGroup> topsNewsgroups;

	private Vector<UsenetUser> usenetUsersFilter = null;

	private UNISoNLogger downloadPanel;

	private UNISoNController() {
		helper = new HibernateHelper(this);
		this.nntpReader = new NewsGroupReader(this);
		try {
			this.session = helper.getHibernateSession();
		} catch (UNISoNException e) {
			showAlert("Error:" + e.getMessage());
		}
		this.messageQueue = new LinkedBlockingQueue<NewsArticle>();

	}

	public int askQuestion(final String question, final String[] options, final String title,
			final String defaultOption) {
		final int response = JOptionPane.showOptionDialog(this.frame, question, title, JOptionPane.DEFAULT_OPTION,
				JOptionPane.ERROR_MESSAGE, null, options, defaultOption);
		return response;
	}

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
	 * @deprecated
	 * @param newsgroup
	 */
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
		} catch (final java.net.UnknownHostException e) {
			this.showErrorMessage(newsgroup + " not found on " + host);
		} catch (final Exception e) {
			this.showErrorMessage("ERROR: " + e);
		}
	}

	/**
	 * @param newsgroup
	 * @param toDate
	 * @param fromDate
	 * @param listNewsgroups
	 * @throws UNISoNException
	 * 
	 * 
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

	public HeaderDownloadWorker getHeaderDownloader() {
		return headerDownloader;
	}

	public Set<String> getLocationsFilter() {
		return countriesFilter;
	}

	/**
	 * 
	 * @return
	 */
	public MatrixType getMatrixType() {
		return this.matrixType;
	}

	@SuppressWarnings("unchecked")
	public Set<Message> getMessages(final Topic topic, Session session) {
		final String query = "from  Message  where topic_id = " + topic.getId();
		final HashSet<Message> returnVal = new HashSet<Message>();
		for (Message message : (List<Message>) helper.runQuery(query, session, Message.class)) {
			if ((null == selectedMessages || selectedMessages.size() == 0 || selectedMessages.contains(message))
					&& (null == selectedPosters || selectedPosters.size() == 0
							|| selectedPosters.contains(message.getPoster())))
				returnVal.add(message);
		}

		return returnVal;
	}

	public Vector<Message> getMessagesFilter() {
		return messagesFilter;
	}

	public Set<NewsGroup> getNewsgroupFilter() {
		return newsgroupFilter;
	}

	public String getNntpHost() {
		return nntpHost;
	}

	public NewsGroupReader getNntpReader() {
		return nntpReader;
	}

	public LinkedBlockingQueue<NewsArticle> getQueue() {
		DataHibernatorWorker.startHibernators();
		return this.messageQueue;
	}

	private Set<String> getSelectedCountries() {
		return this.selectedCountries;
	}

	public Message getSelectedMessage() {
		if (this.filtered && !this.messagesFilter.contains(this.message)) {
			return null;
		}
		return this.message;
	}

	public NewsGroup getSelectedNewsgroup() {
		if (this.filtered && !this.newsgroupFilter.contains(this.selectedNewsgroup)) {
			return null;
		}
		return this.selectedNewsgroup;
	}

	private Vector<NewsGroup> getSelectedNewsgroups() {
		if (!this.filtered) {
			return null;
		}
		return selectedNewsgroups;
	}

	private Vector<UsenetUser> getSelectedPosters() {
		if (!this.filtered) {
			return null;
		}
		return selectedPosters;
	}

	public List<ResultRow> getTopCountriesList() {
		List<ResultRow> results = null;
		HashMap<String, Integer> summaryMap = new HashMap<String, Integer>();

		for (ListIterator<Message> iter = messagesFilter.listIterator(); iter.hasNext();) {
			Message nextMessage = iter.next();

			String nextCountry;
			if (null != nextMessage.getPoster() && null != nextMessage.getPoster().getLocation()
					&& null != nextMessage.getPoster().getLocation().getCountry()) {
				nextCountry = nextMessage.getPoster().getLocation().getCountry();
			} else {
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
		for (Iterator<Entry<String, Integer>> iter = summaryMap.entrySet().iterator(); iter.hasNext();) {
			Entry<String, Integer> entry = iter.next();
			results.add(new ResultRow(entry.getKey(), entry.getValue(), Location.class));
		}
		Collections.sort(results);
		return results;
	}

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
		for (Iterator<Entry<NewsGroup, Integer>> iter = summaryMap.entrySet().iterator(); iter.hasNext();) {
			Entry<NewsGroup, Integer> entry = iter.next();
			results.add(new ResultRow(entry.getKey(), entry.getValue(), NewsGroup.class));
		}
		Collections.sort(results);
		return results;
	}

	@SuppressWarnings({ "unchecked", "unchecked" })
	public Vector<Vector<Object>> getTopGroupsVector() throws HibernateException {

		final String sql = "SELECT count(*) as posts, newsgroup_id FROM newsgroup_message " + " group by newsgroup_id "
				+ " order by posts desc";

		final SQLQuery query = session.createSQLQuery(sql);

		final List<?> returnVal = query.list();

		final Vector<Vector<Object>> tableData = new Vector<Vector<Object>>();
		final Iterator<?> iter = returnVal.iterator();
		while (iter.hasNext()) {
			final Vector<Object> row = new Vector<Object>();
			final Object[] array = (Object[]) iter.next();
			final int userID = (Integer) array[1];

			final List<NewsGroup> posters = (List<NewsGroup>) helper
					.runQuery("from " + NewsGroup.class.getName() + " where id = " + userID, session, NewsGroup.class);
			if (posters.size() > 0) {
				final NewsGroup usenetUser = posters.get(0);
				row.add(new GUIItem<NewsGroup>(usenetUser.getFullName(), usenetUser));
				row.add(array[0].toString());
			} else {
				logger.warn("Poster " + userID + " not found");
			}
			tableData.add(row);
		}
		return tableData;
	}

	public Set<Topic> getTopicsFilter() {
		return topicsFilter;
	}

	public Set<NewsGroup> getTopNewsGroups() {
		return topsNewsgroups;
	}

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
		for (Iterator<Entry<UsenetUser, Integer>> iter = summaryMap.entrySet().iterator(); iter.hasNext();) {
			Entry<UsenetUser, Integer> entry = iter.next();
			results.add(new ResultRow(entry.getKey(), entry.getValue(), UsenetUser.class));
		}
		Collections.sort(results);
		return results;
	}

	public Vector<UsenetUser> getUsenetUsersFilter() {
		return usenetUsersFilter;
	}

	public HibernateHelper helper() {
		return helper;
	}

	/**
	 * This is the main initialisation for the whole application
	 * 
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
	 * 
	 * @return
	 */
	public boolean isFiltered() {
		return this.filtered;
	}

	public Set<NNTPNewsGroup> listNewsgroups(final String searchString, String host) throws UNISoNException {

		this.nntpHost = host;
		return this.nntpReader.client.listNNTPNewsgroups(searchString, host);
	}

	@Override
	public void notifyObservers() {
		setChanged();
		super.notifyObservers();
	}

	public void quickDownload(Set<NNTPNewsGroup> groups, Date fromDate, Date toDate, UNISoNLogger log,
			DownloadMode mode) throws UNISoNException {

		for (final NNTPNewsGroup group : groups) {
			try {
				this.nntpReader.client.reconnect();
				this.nntpReader.client.selectNewsgroup(group.getNewsgroup());
				this.nntpReader.setMessageCount(group.getArticleCount());
				headerDownloader.initialise(nntpReader, group.getFirstArticle(), group.getLastArticle(), nntpHost,
						group.getNewsgroup(), log, mode, fromDate, toDate);
			} catch (IOException e) {
				e.printStackTrace();
				throw new UNISoNException("Error downloading messages. Check your internet connection: ", e);
			}
		}
	}

	/**
	 * 
	 * 
	 */
	public void refreshDataFromDatabase() {

		UNISoNController.logger.debug("refreshDataFromDatabase");

		this.messagesFilter = DataQuery.getInstance().getMessages(this.selectedMessages, this.getSelectedPosters(),
				session, this.fromDate, this.toDate, this.filtered, this.getSelectedNewsgroups(),
				this.getSelectedCountries());

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
			} catch (org.hibernate.UnresolvableObjectException e) {
				// ignore
			}
			try {
				poster = message.getPoster();
				if (session.contains(poster)) {
					session.refresh(poster);
				}
			} catch (org.hibernate.UnresolvableObjectException e) {
				// ignore
			}

			if (!usenetUsersFilter.contains(poster)) {
				// if (!filtered || null == selectedPosters
				// || selectedPosters.contains(message.getPoster())) {
				usenetUsersFilter.add(poster);
				String country;
				if (null != poster && null != poster.getLocation() && null != poster.getLocation().getCountry()) {
					country = poster.getLocation().getCountry();
				} else {
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
				} catch (org.hibernate.UnresolvableObjectException e) {
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

	private void setButtonState(final boolean connectButtonState, final boolean downloadButtonState,
			final boolean pauseButtonState, final boolean cancelButtonState) {
		// The command line version does not do this
		// if (null != this.frame) {
		// this.frame.setButtonState(connectButtonState, downloadButtonState,
		// pauseButtonState, cancelButtonState);
		// }
	}

	public void setConnectedState() {
		setButtonState(false, true, false, true);
	}

	public void setConnectingState() {
		setButtonState(false, false, false, true);
	}

	public void setDates(Date fromDate2, Date toDate2) {
		this.toDate = toDate2;
		this.fromDate = fromDate2;
	}

	public void setDownloadingState(final int progress) {
		setButtonState(false, false, true, true);
	}

	/**
	 * Once the header download worker completes it will call this. This method
	 * will tell the download panel to update itself.
	 */
	public void setHeaderDownloaderFinished() {
		headerDownloader.notifyObservers();
	}

	public void setIdleState() {
		setButtonState(true, false, false, false);
	}

	public void setMatrixType(final MatrixType type) {
		this.matrixType = type;
	}

	public void setNntpHost(String nntpHost) {
		this.nntpHost = nntpHost;
	}

	public void setSelectedMessage(final Message message) {
		this.message = message;
	}

	public void setSelectedNewsgroup(final NewsGroup group) {
		this.selectedNewsgroup = group;
		// this.frame.setSelectedNewsgroup(group);
	}

	public void setSelectedNewsgroup(final String groupName) {
		NewsGroup group = null;
		if (!groupName.equals("")) {
			group = helper.getNewsgroupByFullName(groupName, session);
		}
		this.setSelectedNewsgroup(group);
	}

	public void setSelectedNewsgroups(Vector<NewsGroup> groups) {
		this.selectedNewsgroups = groups;
	}

	public void setSelectedPosters(Vector<UsenetUser> posters) {
		this.selectedPosters = posters;
	}

	public void showAlert(String message) {
		JOptionPane.showMessageDialog(this.frame, message);
	}

	private void showErrorMessage(final String message) {
		// this.frame.showErrorMessage(message);
	}

	public void showStatus(final String message) {
		// CLI version does not do this
		if (null != this.frame) {
			showAlert(message);
		}
	}

	public void stopDownload() {
		DataHibernatorWorker.stopDownload();
		setIdleState();
	}

	public void storeNewsgroups(final Set<NNTPNewsGroup> newsgroups) {
		helper.storeNewsgroups(newsgroups, session);
	}

	public void switchFiltered(boolean on) {
		this.filtered = on;
		this.refreshDataFromDatabase();
	}

	public void setSelectedCountries(Set<String> countries) {
		this.selectedCountries = countries;
	}

	public UNISoNLogger getDownloadPanel() {
		return downloadPanel;
	}

	public void setDownloadPanel(UNISoNLogger downloadPanel) {
		this.downloadPanel = downloadPanel;
	}
}
