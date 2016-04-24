package uk.co.sleonard.unison.gui;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import javax.mail.MessagingException;
import javax.swing.DefaultListModel;
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
import uk.co.sleonard.unison.datahandling.filter.DataQuery;
import uk.co.sleonard.unison.gui.generated.UNISoNFrame;
import uk.co.sleonard.unison.input.NewsGroupReader;

public class UNISoNController {
	public class TopicComparator implements Comparator<Topic> {

		public int compare(Topic first, Topic second) {
			return first.getSubject().compareTo(second.getSubject());
		}

	}

	private Vector<Location> selectedLocations = null;

	private Vector<UsenetUser> selectedUsenetUsers = null;

	private Vector<Message> selectedMessages = null;

	private Vector<Topic> selectedTopics = null;

	private boolean filtered = false;

	private Vector<NewsGroup> selectedNewsgroups;

	private static Logger logger = Logger.getLogger("UNISonController");

	public void refreshDataFromDatabase() {
		logger.debug("refreshDataFromDatabase");

		// reset values - will fill again if filtered
		countriesFilter = new Vector<String>();
		usersFilter = new Vector<UsenetUser>();

		if (filtered) {
			Object[] items = frame.getSelectedCountries();
			countriesFilter.copyInto(items);
			for (int i = 0; i < items.length; i++) {
				String countryName = (String) items[i];
				countriesFilter.add(countryName);
			}

			Object[] posterItems = frame.getSelectedPosters();

			for (int i = 0; i < posterItems.length; i++) {
				GUIItem<UsenetUser> userItem = (GUIItem<UsenetUser>) posterItems[i];
				usersFilter.add(userItem.getItem());
			}
		}

		selectedLocations = DataQuery.getLocations(countriesFilter);

		selectedUsenetUsers = DataQuery.getUsenetUsers(usersFilter,
				selectedLocations);
		selectedMessages = DataQuery.getMessages(messagesFilter,
				selectedUsenetUsers);
		selectedNewsgroups = DataQuery.getNewsGroups(newsgroupFilter,
				selectedMessages);

		frame.refreshGUIData();
	}

	private Vector<UsenetUser> usersFilter = null;

	private Vector<Message> messagesFilter = null;

	private Vector<Topic> topicsFilter = null;

	private Vector<NewsGroup> newsgroupFilter = null;

	Vector<String> countriesFilter = null;

	// private Vector<NewsGroup> selectedNewsgroups = null;

	public ListIterator<Topic> getSelectedTopicsFromDB(
			Vector<NewsGroup> chosenGroup) {
		selectedTopics = DataQuery.getTopics(topicsFilter, selectedMessages,
				chosenGroup);

		Collections.sort(selectedTopics, new TopicComparator());
		ListIterator<Topic> iter = selectedTopics.listIterator();
		return iter;
	}

	public Vector getTopPostersVector() throws HibernateException {

		String sql = "SELECT count(*) as posts, usenetuser_id FROM message "
				+ " group by usenetuser_id " + " order by posts desc";

		SQLQuery query = HibernateHelper.getHibernateSession().createSQLQuery(
				sql);

		List returnVal = query.list();

		Vector<Vector<Object>> tableData = new Vector<Vector<Object>>();
		Iterator iter = returnVal.iterator();
		while (iter.hasNext()) {
			Vector<Object> row = new Vector<Object>();
			Object[] array = (Object[]) iter.next();
			int userID = (Integer) array[1];

			List<UsenetUser> posters = (List<UsenetUser>) HibernateHelper
					.runQuery("from " + UsenetUser.class.getName()
							+ " where id = " + userID);
			if (posters.size() > 0) {
				UsenetUser usenetUser = posters.get(0);
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

	public Vector getTopGroupsVector() throws HibernateException {

		String sql = "SELECT count(*) as posts, newsgroup_id FROM newsgroup_message "
				+ " group by newsgroup_id " + " order by posts desc";

		SQLQuery query = HibernateHelper.getHibernateSession().createSQLQuery(
				sql);

		List returnVal = query.list();

		Vector<Vector<Object>> tableData = new Vector<Vector<Object>>();
		Iterator iter = returnVal.iterator();
		while (iter.hasNext()) {
			Vector<Object> row = new Vector<Object>();
			Object[] array = (Object[]) iter.next();
			int userID = (Integer) array[1];

			List<NewsGroup> posters = (List<NewsGroup>) HibernateHelper
					.runQuery("from " + NewsGroup.class.getName()
							+ " where id = " + userID);
			if (posters.size() > 0) {
				NewsGroup usenetUser = posters.get(0);
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

	public Message getSelectedMessage() {
		return message;
	}

	public NewsGroup getSelectedNewsgroup() {
		return selectedNewsgroup;
	}

	public void setSelectedNewsgroup(String groupName) {
		NewsGroup group = null;
		if (!groupName.equals("")) {
			group = HibernateHelper.getNewsgroupByFullName(groupName);
		}
		setSelectedNewsgroup(group);
	}

	private MatrixType matrixType;

	private Message message;

	private NewsGroup selectedNewsgroup;

	public void setSelectedNewsgroup(NewsGroup group) {
		this.selectedNewsgroup = group;
		frame.setSelectedNewsgroup(group);
	}

	public void setMatrixType(MatrixType type) {
		this.matrixType = type;
	}

	public MatrixType getMatrixType() {
		return matrixType;
	}

	public static final String LOCATION = "Location";

	public static final String USENETUSER = UsenetUser.class.getName();

	public void setSelectedMessage(Message message) {
		this.message = message;
	}

	public List<NewsGroup> getTopNewsGroups() {
		List<NewsGroup> newsgroups = HibernateHelper.fetchTopNewsGroups();
		Collections.sort(newsgroups, new NewsGroupComparator());
		return newsgroups;
	}

	public enum MatrixType {
		REPLY_TO_LAST, REPLY_TO_FIRST, REPLY_TO_ALL
	};

	class NewsGroupComparator implements Comparator<NewsGroup> {

		public int compare(NewsGroup first, NewsGroup second) {
			return first.getName().compareTo(second.getName());
		}

	}

	private static UNISoNController instance;

	private NewsGroupReader nntpReader;

	private UNISoNFrame frame;

	private UNISoNController(UNISoNFrame frame) {
		this.frame = frame;
	}

	private UNISoNController() {
	}

	public void setConnectingState() {
		frame.setButtonState(false, false, false, true);
	}

	public void setConnectedState() {
		frame.setButtonState(false, true, false, true);
	}

	public void setIdleState() {
		frame.setButtonState(true, false, false, false);
	}

	public void setDownloadingState(int progress) {
		frame.setButtonState(false, false, true, true);
	}

	/**
	 * This is the main initialisation for the whole application
	 * 
	 */
	public void initialiseController() {
		// Generated part
		JSplashScreen splash = new JSplashScreen("./lucy.JPG");
		splash.setProgress(10);

		frame = new UNISoNFrame(this);
		frame.setVisible(true);
		splash.setProgress(50);

		refreshDataFromDatabase();

		setIdleState();

		splash.setProgress(100);

		splash.close();
	}

	public static UNISoNController createInstance(UNISoNFrame frame) {
		return instance = new UNISoNController(frame);
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					instance = new UNISoNController();
					instance.initialiseController();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static UNISoNController getInstance() throws NotInitialisedException {
		if (null == instance) {
			throw new NotInitialisedException(
					"UNISonController has not been initialised");
		}
		return instance;
	}

	public int askQuestion(String question, String[] options, String title,
			String defaultOption) {
		int response = JOptionPane.showOptionDialog(frame, question, title,
				JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null,
				options, defaultOption);
		return response;
	}

	public void cancelDownload() {
		setIdleState();
		nntpReader.stopDownload();
	}

	public void downloadMessages() {
		setDownloadingState(0);
		if (null != nntpReader && nntpReader.isConnected()) {
			nntpReader.startDownload();
		} else {
			System.out.println("Not connected!");
		}
	}

	public void connectToNewsGroup(String newsgroup) {
		setConnectingState();
		nntpReader = new NewsGroupReader(this);
		showStatus("Connect to " + newsgroup);

		setSelectedNewsgroup(newsgroup);
		String host = frame.getSelectedHost();
		try {
			nntpReader.connectToNewsGroup(host, newsgroup);
			setConnectedState();

			showStatus("MESSAGES:" + nntpReader.getNumberOfMessages());
		} catch (java.net.UnknownHostException e) {
			showErrorMessage(newsgroup + " not found on " + host);
		} catch (javax.mail.FolderNotFoundException ex) {
			showErrorMessage(newsgroup + " not found on " + host);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	private void showErrorMessage(String message) {
		frame.showErrorMessage(message);
	}

	public void showStatus(String message) {
		frame.showStatus(message);
	}

	public boolean isFiltered() {
		return filtered;
	}

	public void switchFiltered() {
		filtered = !filtered;
	}

	public DefaultListModel getCountriesFilter() {
		// TODO Auto-generated method stub
		return null;
	}
}
