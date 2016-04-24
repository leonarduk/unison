package uk.co.sleonard.unison.gui;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;

import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.DAO.Location;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.gui.generated.UNISoNTabbedFrame;
import uk.co.sleonard.unison.input.HeaderDownloadWorker;
import uk.co.sleonard.unison.input.NNTPNewsGroup;
import uk.co.sleonard.unison.input.NewsClient;
import uk.co.sleonard.unison.input.NewsGroupReader;

public class UNISoNController extends Observable {

	@Override
	public void notifyObservers() {
		setChanged();
		super.notifyObservers();
	}

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

	// private static UNISoNController instance;

	private static UNISoNController instance;

	public static final String LOCATION = "Location";

	private static Logger logger = Logger.getLogger("UNISonController");

	public static final String USENETUSER = UsenetUser.class.getName();

	// public static UNISoNController createInstance(final JFrame frame) {
	// return UNISoNController.instance = new UNISoNController(frame);
	// }
	//
	// public static UNISoNController getInstance() {
	// return UNISoNController.instance;
	// }

	public static void create() {
		instance = new UNISoNController();
	}

	public static UNISoNController create(JFrame frame) {
		instance = new UNISoNController();
		instance.frame = frame;
		return instance;
	}

	public static UNISoNController getInstance() {
		return instance;
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(final String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UNISoNController.instance = new UNISoNController();

					UNISoNTabbedFrame frame = new UNISoNTabbedFrame();
					frame.setSize(500, 500);
					UNISoNController.setFrame(frame);
					UNISoNController.instance.initialiseGUI();
				} catch (final Throwable e) {
					e.printStackTrace();
				}
			}
		});
	}

	protected static void setFrame(UNISoNTabbedFrame frame2) {
		// TODO Auto-generated method stub

	}

	Vector<String> countriesFilter = null;

	private boolean filtered = false;

	private JFrame frame;

	// private Vector<NewsGroup> selectedNewsgroups = null;

	private HibernateHelper helper;

	private MatrixType matrixType;

	private Message message;

	private final Vector<Message> messagesFilter = null;

	private final Vector<NewsGroup> newsgroupFilter = null;

	private NewsGroupReader nntpReader;

	private Vector<Location> selectedLocations = null;

	private Vector<Message> selectedMessages = null;

	private NewsGroup selectedNewsgroup;

	private Vector<NewsGroup> selectedNewsgroups;

	private Vector<Topic> selectedTopics = null;

	private Vector<UsenetUser> selectedUsenetUsers = null;

	private final Vector<Topic> topicsFilter = null;

	private Vector<UsenetUser> usersFilter = null;

	private HeaderDownloadWorker headerDownloader = new HeaderDownloadWorker();

	private UNISoNController() {
		helper = new HibernateHelper(this);
		this.nntpReader = new NewsGroupReader();

	}

	public int askQuestion(final String question, final String[] options,
			final String title, final String defaultOption) {
		final int response = JOptionPane.showOptionDialog(this.frame, question,
				title, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
				null, options, defaultOption);
		return response;
	}

	public void cancelDownload() {
		this.setIdleState();
		this.nntpReader.stopDownload();
	}

	/**
	 * @deprecated
	 * @param newsgroup
	 */
	public void connectToNewsGroup(final String newsgroup) {
		this.setConnectingState();
		this.nntpReader = new NewsGroupReader();
		this.showStatus("Connect to " + newsgroup);

		// TODO need to filter by data and allow more than one newsgroup
		this.setSelectedNewsgroup(newsgroup);
		final String host = null;// this.frame.getSelectedHost();
		try {
			this.nntpReader.client.connectToNewsGroup(host, newsgroup);
			this.setConnectedState();

			this
					.showStatus("MESSAGES:"
							+ this.nntpReader.getNumberOfMessages());
		} catch (final java.net.UnknownHostException e) {
			this.showErrorMessage(newsgroup + " not found on " + host);
		} catch (final Exception e) {
			this.showErrorMessage("ERROR: " + e);
		}
	}

//	/**
//	 * 
//	 * @param groups
//	 * @param updateLocation
//	 * @throws UNISoNException
//	 * @throws IOException
//	 */
//	public void downloadMessages(final Set<NNTPNewsGroup> groups)
//			throws UNISoNException {
//		this.nntpReader.startHibernators(1);
//
//		for (final NNTPNewsGroup group : groups) {
//			try {
//				this.nntpReader.startDownloaders(3);
//			} catch (IOException e) {
//				throw new UNISoNException(e);
//			}
//		}
//	}

	/**
	 * @param newsgroup
	 * @param toDate
	 * @param fromDate
	 * @param listNewsgroups
	 * @throws UNISoNException
	 * 
	 * 
	 */
//	public void downloadMessages(String newsgroup, Date toDate, Date fromDate)
//			throws UNISoNException {
//		this.setDownloadingState(0);
//		logger.info("Start download");
//		if ((null != this.nntpReader) && this.nntpReader.isConnected()) {
//			this.nntpReader.startDownload(newsgroup, fromDate, toDate);
//		} else {
//			System.out.println("Not connected!");
//		}
//	}

	/**
	 * 
	 * @return
	 */
	public DefaultListModel getCountriesFilter() {

		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public MatrixType getMatrixType() {
		return this.matrixType;
	}

	public Message getSelectedMessage() {
		return this.message;
	}

	public NewsGroup getSelectedNewsgroup() {
		return this.selectedNewsgroup;
	}

	public ListIterator<Topic> getSelectedTopicsFromDB(
			final Vector<NewsGroup> chosenGroup) {
		this.selectedTopics = DataQuery.getTopics(this.topicsFilter,
				this.selectedMessages, chosenGroup);

		Collections.sort(this.selectedTopics, new TopicComparator());
		final ListIterator<Topic> iter = this.selectedTopics.listIterator();
		return iter;
	}

	@SuppressWarnings( { "unchecked", "unchecked" })
	public Vector<Vector<Object>> getTopGroupsVector()
			throws HibernateException {

		final String sql = "SELECT count(*) as posts, newsgroup_id FROM newsgroup_message "
				+ " group by newsgroup_id " + " order by posts desc";

		final SQLQuery query = helper.getHibernateSession().createSQLQuery(sql);

		final List<?> returnVal = query.list();

		final Vector<Vector<Object>> tableData = new Vector<Vector<Object>>();
		final Iterator<?> iter = returnVal.iterator();
		while (iter.hasNext()) {
			final Vector<Object> row = new Vector<Object>();
			final Object[] array = (Object[]) iter.next();
			final int userID = (Integer) array[1];

			final List<NewsGroup> posters = (List<NewsGroup>) helper
					.runQuery("from " + NewsGroup.class.getName()
							+ " where id = " + userID);
			if (posters.size() > 0) {
				final NewsGroup usenetUser = posters.get(0);
				row.add(new GUIItem<NewsGroup>(usenetUser.getFullName(),
						usenetUser));
				row.add(array[0].toString());
			} else {
				logger.warn("Poster " + userID + " not found");
			}
			tableData.add(row);
		}
		return tableData;
	}

	public List<NewsGroup> getTopNewsGroups() {
		final List<NewsGroup> newsgroups = helper.fetchTopNewsGroups();
		Collections.sort(newsgroups, new NewsGroupComparator());
		return newsgroups;
	}

	/**
	 * 
	 * @return
	 * @throws HibernateException
	 */
	@SuppressWarnings("unchecked")
	public Vector<Vector<Object>> getTopPostersVector()
			throws HibernateException {
		// TODO use a non-SQL query for this
		final String sql = "SELECT count(*) as posts, usenetuser_id FROM message "
				+ " group by usenetuser_id " + " order by posts desc";

		final SQLQuery query = helper.getHibernateSession().createSQLQuery(sql);

		final List<?> returnVal = query.list();

		final Vector<Vector<Object>> tableData = new Vector<Vector<Object>>();
		final Iterator<?> iter = returnVal.iterator();
		while (iter.hasNext()) {
			final Vector<Object> row = new Vector<Object>();
			final Object[] array = (Object[]) iter.next();
			final int userID = (Integer) array[1];

			final List<UsenetUser> posters = (List<UsenetUser>) helper
					.runQuery("from " + UsenetUser.class.getName()
							+ " where id = " + userID);
			if (posters.size() > 0) {
				final UsenetUser usenetUser = posters.get(0);
				row.add(new GUIItem<UsenetUser>(usenetUser.getName() + "<"
						+ usenetUser.getEmail() + ">", usenetUser));
				row.add(array[0].toString());
			} else {
				logger.warn("Poster " + userID + " not found");
			}
			tableData.add(row);
		}
		return tableData;
	}

	public HibernateHelper helper() {
		return helper;
	}

	/**
	 * This is the main initialisation for the whole application
	 * 
	 */
	public void initialiseGUI() {
		// TODO put in valid path to an image file
		final String filepath = null;
		final JSplashScreen splash = new JSplashScreen(filepath);

		splash.setProgress(10);

		this.frame = new JFrame();
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

	public Set<NNTPNewsGroup> listNewsgroups(final String searchString)
			throws UNISoNException {

		return this.nntpReader.client.listNNTPNewsgroups(searchString);
	}

	public void quickDownload(Set<NNTPNewsGroup> groups, Date fromDate,
			Date toDate, UNISoNLogger log, DownloadMode mode) throws UNISoNException {

		for (final NNTPNewsGroup group : groups) {
			try {
				this.nntpReader.client.selectNewsgroup(group.getNewsgroup());
				this.nntpReader.setMessageCount(group.getArticleCount());
				String server = NewsClient.getNNTPHost();
				headerDownloader.initialise(nntpReader,
						group.getFirstArticle(), group.getLastArticle(),
						server, group.getArticleCount(), group.getNewsgroup(),
						log, mode, fromDate, toDate);
			} catch (IOException e) {
				e.printStackTrace();
				throw new UNISoNException(
						"Error downloading messages. Check your internet connection: ",
						e);
			}
		}
	}

	public HeaderDownloadWorker getHeaderDownloader() {
		return headerDownloader;
	}

	/**
	 * 
	 * 
	 */
	public void refreshDataFromDatabase() {

		UNISoNController.logger.debug("refreshDataFromDatabase");

		// reset values - will fill again if filtered
		this.countriesFilter = new Vector<String>();
		this.usersFilter = new Vector<UsenetUser>();

		if (this.filtered) {

			UNISoNController.logger.debug("Country filter: "
					+ this.countriesFilter + " posters filter "
					+ this.usersFilter);

		}

		this.selectedLocations = DataQuery.getLocations(this.countriesFilter);

		this.selectedUsenetUsers = DataQuery.getUsenetUsers(this.usersFilter,
				this.selectedLocations);
		this.selectedMessages = DataQuery.getMessages(this.messagesFilter,
				this.selectedUsenetUsers);
		this.selectedNewsgroups = DataQuery.getNewsGroups(this.newsgroupFilter,
				this.selectedMessages);

		UNISoNController.logger.debug("locations : " + this.selectedLocations
				+ " users " + this.selectedUsenetUsers + " messages "
				+ this.selectedMessages + " groups " + this.selectedNewsgroups
				+ " topics " + this.selectedTopics);
		notifyObservers();

	}

	private void setButtonState(final boolean connectButtonState,
			final boolean downloadButtonState, final boolean pauseButtonState,
			final boolean cancelButtonState) {
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

	public void setDownloadingState(final int progress) {
		setButtonState(false, false, true, true);
	}

	public void setIdleState() {
		setButtonState(true, false, false, false);
	}

	public void setMatrixType(final MatrixType type) {
		this.matrixType = type;
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
			group = helper.getNewsgroupByFullName(groupName);
		}
		this.setSelectedNewsgroup(group);
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
			// this.frame.showStatus(message);
		}
	}

	public void storeNewsgroups(final Set<NNTPNewsGroup> newsgroups) {
		helper.storeNewsgroups(newsgroups);
	}

	public void switchFiltered() {
		this.filtered = !this.filtered;
		this.refreshDataFromDatabase();
	}

	/**
	 * Once the header download worker completes it will call this. This method
	 * will tell the download panel to update itself.
	 */
	public void setHeaderDownloaderFinished() {
		headerDownloader.notifyObservers();
	}
}
