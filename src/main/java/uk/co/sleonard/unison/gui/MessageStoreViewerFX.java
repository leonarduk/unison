/*
 * MessageStoreViewer.java
 *
 * Created on 28 November 2007, 09:04
 */

package uk.co.sleonard.unison.gui;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.tree.TreePath;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeView;
import uk.co.sleonard.unison.UNISoNControllerFX;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.UNISoNLogger;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.datahandling.DAO.GUIItem;
import uk.co.sleonard.unison.datahandling.DAO.Location;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.ResultRow;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.input.FullDownloadWorker;
import uk.co.sleonard.unison.utils.StringUtils;
import uk.co.sleonard.unison.utils.TreeNode;

/**
 * The class MessageStoreViewer, Controller of the Tab View Saved Data.
 *
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since 28-Jun-2016
 */
public class MessageStoreViewerFX implements Observer, UNISoNLogger {

	/** -------------------- Components Variables ------------------ */
	/** The body pane. */
	@FXML
	private TextArea bodyPane; // XXX
	/** The body scroll pane. */
	@FXML
	private ScrollPane bodyScrollPane; // XXX
	/** The crosspost combo box. */
	@FXML
	private ComboBox<NewsGroup> crosspostComboBox; // XXX
	/** The filter toggle. */
	@FXML
	private ToggleButton filterToggle;  // XXX
	/** The from date field. */
	@FXML
	private TextField fromDateField; // XXX
	/** The from date label. */
	@FXML
	private Label fromDateLabel; // XXX
	/** The get body button. */
	// private javax.swing.JButton getBodyButton; Maybe no implemented yet
	/** The groups hierarchy. */
	@FXML
	private TreeView<Object> groupsHierarchy; // XXX VERIFY TYPE
	/** The groups scroll pane. */
	@FXML
	private ScrollPane groupsScrollPane; // XXX
	/** The headers button. */
	@FXML
	private Button headersButton; // XXX
	/** The location field. */
	@FXML
	private TextField locationField; // XXX
	/** The location label. */
	@FXML
	private Label locationLabel; // XXX
	/** The missing messages check. */
	// private javax.swing.JCheckBox missingMessagesCheck; Maybe no implemented yet
	/** The refresh button. */
	@FXML
	private Button refreshButton; // XXX
	/** The sender field. */
	@FXML
	private TextField senderField; // XXX
	/** The sender label. */
	@FXML
	private Label senderLabel; // XXX
	/** The sent date field. */
	@FXML
	private TextField sentDateField; // XXX
	/** The sent date label. */
	@FXML
	private Label sentDateLabel; // XXX
	/** The stats tab pane. */
	// private javax.swing.JTabbedPane statsTabPane; Maybe no implemented yet
	/** The subject field. */
	@FXML
	private TextField subjectField; // XXX
	/** The subject label. */
	@FXML
	private Label subjectLabel; // XXX
	/** The to date field. */
	@FXML
	private TextField toDateField; // XXX
	/** The todate label. */
	@FXML
	private Label toDateLabel; // XXX
	/** The top countries list. */
	@FXML
	private ListView<GUIItem<Object>> topCountriesList; // XXX
	/** The top countries scroll pane. */
	@FXML
	private ScrollPane topCountriesScrollPane; // XXX
	/** The top groups list. */
	@FXML
	private ListView<GUIItem<Object>> topGroupsList; // XXX
	/** The top groups scroll pane. */
	@FXML
	private ScrollPane topGroupsScrollPane; // XXX
	/** The top posters list. */
	@FXML
	private ListView<GUIItem<Object>> topPostersList; // XXX
	/** The top posters scroll pane. */
	@FXML
	private ScrollPane topPostersScrollPane; // XXX
	/** The topics hierarchy. */
	@FXML
	private TreeView<Object> topicsHierarchy; // XXX VERIFY TYPE
	/** The topics scroll pane. */
	@FXML
	private ScrollPane topicsScrollPane; // XXX
	/** The session. */
	private Session session;
	/** The newsgroup tree root. */
	private TreeNode newsgroupTreeRoot;
	/** The topic root. */
	private TreeNode topicRoot;

	/** -------------------- Components Variables ------------------ */

	/**
	 * Creates new form MessageStoreViewer.
	 */
	public MessageStoreViewerFX() {
	}

	@FXML
	private void initialize() {
		try {

			this.session = UNISoNControllerFX.getInstance().getHelper().getHibernateSession();

			// FIXME disable all non-workng parts
			// headersButton.setVisible(false);
			// this.getBodyButton.setVisible(false);
			// this.missingMessagesCheck.setVisible(false);

			this.switchFilter(this.filterToggle.isSelected());
		}
		catch (final UNISoNException e) {
			UNISoNControllerFX.getInstance();
			UNISoNControllerFX.getGui().showAlert("Error :" + e.getMessage());
		}
	}

	/**
	 * Adds the child node.
	 *
	 * @param root
	 *            the root
	 * @param childObject
	 *            the child object
	 * @return the tree node
	 */
	protected TreeNode addChildNode(final TreeNode root, final Object childObject) {
		return this.addChildNode(root, childObject, "");
	}

	/**
	 * Adds the child node.
	 *
	 * @param root
	 *            the root
	 * @param childObject
	 *            the child object
	 * @param name
	 *            the name
	 * @return the tree node
	 */
	protected TreeNode addChildNode(final TreeNode root, final Object childObject,
	        final String nameInput) {
		String name = nameInput;
		if (childObject instanceof Set<?>) {
			if (((Set<?>) childObject).size() == 0) {
				// if no entries then don't add it
				return null;
			}
		}
		else if (childObject instanceof String) {
			name += " : " + childObject;
		}
		else {
			name += UNISoNControllerFX.getInstance().getHelper().getText(childObject);
		}

		final TreeNode child = new TreeNode(childObject, name);
		root.add(child);

		return child;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see uk.co.sleonard.unison.gui.UNISoNLogger#alert(java.lang.String)
	 */
	@Override
	public void alert(final String message) {
		this.log(message);
		UNISoNControllerFX.getInstance();
		UNISoNControllerFX.getGui().showAlert(message);
	}

	/**
	 * Creates the message hierarchy.
	 *
	 * @param set
	 *            the set
	 * @param root
	 *            the root
	 * @param matchId
	 *            the match id
	 * @param fillInMissing
	 *            the fill in missing
	 * @return the sets the
	 */
	private Set<Message> createMessageHierarchy(final Set<Message> set, final TreeNode root,
	        final Object matchId, final boolean fillInMissing) {
		final Set<TreeNode> matches = new HashSet<>();
		final Set<Message> copy = new HashSet<>(set);

		for (final Message next : set) {
			// compare to the last refered message, ie. the one they replied to
			String previousId = "ROOT";
			try {
				final List<String> msgList = StringUtils
				        .convertStringToList(next.getReferencedMessages(), " ");

				if (msgList.size() > 0) {
					final String lastMessageId = msgList.get(0);
					if (fillInMissing) {
						previousId = lastMessageId;

					}
					// else ignore it and add to root
				}
			}
			catch (final ObjectNotFoundException e) {
				e.printStackTrace();
			}

			// if it matches then it refers to previous so add as a child to
			// previous
			if (previousId.equals(matchId)) {
				final TreeNode child = this.addChildNode(root, next);
				matches.add(child);
				copy.remove(next);
			}
		}
		Set<Message> remainder = new HashSet<>(copy);
		for (final TreeNode next : matches) {
			remainder = this.createMessageHierarchy(remainder, next,
			        ((Message) next.getUserObject()).getUsenetMessageID(), fillInMissing);
		}
		return copy;
	}

	/**
	 * Crosspost combo box action performed.
	 *
	 * @param evt
	 *            the evt
	 */
	private void crosspostComboBoxActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_crosspostComboBoxActionPerformed
		final UNISoNControllerFX controller = UNISoNControllerFX.getInstance();
		final NewsGroup selectedGroup = (NewsGroup) this.crosspostComboBox.getSelectionModel()
		        .getSelectedItem();
		controller.getFilter().setSelectedNewsgroup(selectedGroup);
		this.refreshTopicHierarchy();
		// controller.showAlert("You chose " + selectedGroup);
	}// GEN-LAST:event_crosspostComboBoxActionPerformed

	/**
	 * Expand node.
	 *
	 * @param root
	 *            the root
	 * @param fillInMissing
	 *            the fill in missing
	 */
	protected void expandNode(final TreeNode root, final boolean fillInMissing) {

		final Object userObject = root.getUserObject();
		if (userObject instanceof Topic) {
			final Topic topic = (Topic) userObject;
			this.createMessageHierarchy(
			        UNISoNControllerFX.getInstance().getDatabase().getMessages(topic, this.session),
			        root, "ROOT", fillInMissing);
		}
	}

	/**
	 * Filter toggle action performed.
	 *
	 */
	@FXML
	private void filterToggle() {
		try {
			this.switchFilter(this.filterToggle.isSelected());
		}
		catch (final UNISoNException e) {
			UNISoNControllerFX.getInstance();
			UNISoNControllerFX.getGui().showAlert("Error :" + e.getMessage());
		}
	}

	/**
	 * Gets the list model.
	 *
	 * @param results
	 *            the results
	 * @return the list model
	 */
	private ListModel<GUIItem<Object>> getListModel(final List<ResultRow> results) {
		final DefaultListModel<GUIItem<Object>> model = new DefaultListModel<>();
		for (final ListIterator<ResultRow> iter = results.listIterator(); iter.hasNext();) {
			final Object next = iter.next();
			String name = next.toString();
			if (next instanceof UsenetUser) {
				name = ((UsenetUser) next).getName() + "<" + ((UsenetUser) next).getEmail() + ">";
			}
			else if (next instanceof Location) {
				name = ((Location) next).getCountry();
			}

			model.addElement(new GUIItem<>(name, next));
		}
		return model;
	}

	/**
	 * Groups hierarchy value changed.
	 *
	 * @param evt
	 *            the evt
	 */
	private void groupsHierarchyValueChanged(final javax.swing.event.TreeSelectionEvent evt) {// GEN-FIRST:event_groupsHierarchyValueChanged
		final TreePath tp = evt.getPath();
		final TreeNode root = (TreeNode) tp.getLastPathComponent();

		// as root is not a newsgroup
		if (root.getUserObject() instanceof NewsGroup) {
			UNISoNControllerFX.getInstance().getFilter()
			        .setSelectedNewsgroup((NewsGroup) root.getUserObject());
		}
		else {
			UNISoNControllerFX.getInstance().getFilter()
			        .setSelectedNewsgroup((String) root.getUserObject());
		}

		this.notifySelectedNewsGroupObservers();
	}// GEN-LAST:event_groupsHierarchyValueChanged

	/**
	 * Headers button action performed.
	 *
	 */
	@FXML
	private void headersButton() {
		try {
			for (final Message message : UNISoNControllerFX.getInstance().getFilter()
			        .getMessagesFilter()) {
				// only download for messages that need it
				if (null == message.getPoster().getLocation()) {
					FullDownloadWorker.addDownloadRequest(message.getUsenetMessageID(),
					        DownloadMode.HEADERS,
					        UNISoNControllerFX.getInstance().getDownloadPanel());
				}
			}
		}
		catch (final UNISoNException e) {
			this.alert("Failed to download extra fields: " + e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see uk.co.sleonard.unison.gui.UNISoNLogger#log(java.lang.String)
	 */
	@Override
	public void log(final String message) {
		// notesArea.append(message + "\n");
	}

	/**
	 * Notify selected message observers.
	 */
	public void notifySelectedMessageObservers() {
		this.refreshMessagePane();
	}

	/**
	 * Notify selected news group observers.
	 */
	public void notifySelectedNewsGroupObservers() {
		this.refreshTopicHierarchy();
	}

	/**
	 * Refresh button action performed.
	 *
	 */
	@FXML
	private void refreshDataButton() {
		final UNISoNControllerFX controller = UNISoNControllerFX.getInstance();
		controller.getDatabase().refreshDataFromDatabase();
	}

	/**
	 * Key method - this refreshes all the GUI components with fresh data from the database.
	 */
	public void refreshGUIData() {
		// this.refreshTopPostersTable();

		this.refreshMessagePane();
		this.refreshTopicHierarchy();
		this.refreshNewsGroupHierarchy();

		this.refreshTopCountries();
		this.refreshTopPosters();
		this.refreshTopGroups();
	}

	/**
	 * Refresh message pane.
	 */
	public void refreshMessagePane() {
		final Message message = UNISoNControllerFX.getInstance().getFilter().getSelectedMessage();

		if (null != message) {
			// final DefaultListModel model = this.getCrossPostsModel(message);
			// this.crosspostComboBox.setModel(model);

			String subject = message.getSubject();
			if (subject.length() > 18) {
				subject = subject.substring(0, 15) + "...";
			}
			this.subjectField.setText(subject);
			this.subjectField.setTooltip(new Tooltip(message.getSubject()));

			String name2 = message.getPoster().getName();
			if (name2.length() > 18) {
				name2 = name2.substring(0, 15) + "...";
			}
			this.senderField.setText(name2);
			this.senderField.setTooltip(new Tooltip(message.getPoster().toString()));

			String location;
			String fullLocation;
			if (null == message.getPoster().getLocation()) {
				location = "UNKNOWN";
				fullLocation = "Download header to get location";
			}
			else {
				location = message.getPoster().getLocation().toString();
				fullLocation = message.getPoster().getLocation().fullString();
			}

			this.locationField.setText(location);
			this.locationField.setTooltip(new Tooltip(fullLocation));

			this.sentDateField.setText(
			        new SimpleDateFormat("dd MMM yyyy hh:mm").format(message.getDateCreated()));
			final DefaultComboBoxModel<NewsGroup> aModel = new DefaultComboBoxModel<>(
			        new Vector<>(message.getNewsgroups()));
			// this.crosspostComboBox.setModel(aModel); TODO ADAPT TO JAVAFX
			try {
				this.bodyPane.setText(StringUtils.decompress(message.getMessageBody()));
			}
			catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Refresh news group hierarchy.
	 */
	protected void refreshNewsGroupHierarchy() {
		this.newsgroupTreeRoot.removeAllChildren();
		// FIXME split out from name - ignore db stuff
		final UNISoNControllerFX controller = UNISoNControllerFX.getInstance();
		final HashMap<String, TreeNode> nodeMap = new HashMap<>();

		final List<NewsGroup> newsgroupFilter = new ArrayList<>();
		newsgroupFilter.addAll(controller.getFilter().getNewsgroupFilter());
		Collections.sort(newsgroupFilter);

		for (final NewsGroup group : newsgroupFilter) {
			final String[] nameparts = group.getFullName().split("\\.");
			String pathSoFar = "";
			TreeNode parent = this.newsgroupTreeRoot;

			for (final String namePart : nameparts) {
				if (!pathSoFar.equals("")) {
					pathSoFar += ".";
				}
				pathSoFar += namePart;
				TreeNode node = nodeMap.get(pathSoFar);

				Object data = pathSoFar;
				if (namePart.equals(group.getName())) {
					// base part
					data = group;
				}

				if (null == node) {
					node = new TreeNode(data, namePart);
					parent.add(node);
					node.setParent(parent);
					nodeMap.put(pathSoFar, node);
				}
				else {
					// If node created by earlier newsgroup
					if (node.getUserObject() instanceof String) {
						node.setUserObject(data);
					}
				}
				// ready for next iteration
				parent = node;
			}
		}
		// Set<NewsGroup> topNewsGroups = controller.getTopNewsGroups();
		// Set<NewsGroup> newsgroupsFilter = controller.getNewsgroupFilter();
		// Set<NewsGroup> groups = new HashSet<NewsGroup>();
		// final Iterator<NewsGroup> iter = topNewsGroups.iterator();
		// while (iter.hasNext()) {
		// final NewsGroup group = iter.next();
		// boolean addGroup = true;
		// if (null != newsgroupsFilter) {
		// addGroup = false;
		// for (Iterator<NewsGroup> iter2 = newsgroupsFilter.iterator();
		// !addGroup
		// && iter2.hasNext();) {
		// String fullName = iter2.next().getFullName();
		// int indexOf = fullName.indexOf(".");
		// String topLevel = fullName;
		// if (indexOf > -1) {
		// topLevel = fullName.substring(0, indexOf);
		// }
		// if (group.getFullName().equals(topLevel)) {
		// addGroup = true;
		// continue;
		// }
		// }
		// }
		// if (addGroup && !groups.contains(group)) {
		// groups.add(group);
		// }
		// }
		// for (NewsGroup group : groups) {
		// this.addChildNode(this.newsgroupTreeRoot, group);
		// }
		// This actually refreshes the tree

		// ((DefaultTreeModel) this.groupsHierarchy.getModel()).reload(); TODO ADAPT TO JAVAFX
	}

	/**
	 * Refresh top countries.
	 */
	private void refreshTopCountries() {
		final List<ResultRow> results = UNISoNControllerFX.getInstance().getAnalysis()
		        .getTopCountriesList();

		// this.topCountriesList.setModel(this.getListModel(results)); TODO ADAPT TO JAVAFX
	}

	/**
	 * Refresh top groups.
	 */
	private void refreshTopGroups() {
		final List<ResultRow> results = UNISoNControllerFX.getInstance().getAnalysis()
		        .getTopGroupsList();

		// this.topGroupsList.setModel(this.getListModel(results)); TODO ADAPT TO JAVAFX
	}

	/**
	 * Refresh topic hierarchy.
	 */
	private void refreshTopicHierarchy() {
		// TODO reinstate that topics reflect the highlighted newsgroup

		this.topicRoot.removeAllChildren();

		final UNISoNControllerFX controller = UNISoNControllerFX.getInstance();
		final NewsGroup selectedNewsgroup = controller.getFilter().getSelectedNewsgroup();
		if (null != selectedNewsgroup) {
			this.topicRoot.setName(selectedNewsgroup.getFullName());
			final Set<Topic> topics = selectedNewsgroup.getTopics();
			final Set<Topic> topicsFilter = controller.getFilter().getTopicsFilter();
			for (final Topic topic : topics) {
				if ((null == topicsFilter) || topicsFilter.contains(topic)) {
					final int lastIndex = topic.getSubject().length();
					this.addChildNode(this.topicRoot, topic,
					        topic.getSubject().substring(0, lastIndex));
				}
			}

		}
		else {
			this.topicRoot.setName("No group selected");
		}

		// This actually refreshes the tree
		// ((DefaultTreeModel) this.topicsHierarchy.getModel()).reload(); TODO ADAPT TO JAVAFX
	}

	/**
	 * Refresh top posters.
	 */
	private void refreshTopPosters() {
		final Vector<ResultRow> results = UNISoNControllerFX.getInstance().getAnalysis()
		        .getTopPosters();

		// this.topPostersList.setModel(this.getListModel(results)); TODO ADAPT TO JAVAFX
	}

	/**
	 * Switch filter.
	 *
	 * @param on
	 *            the on
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	@SuppressWarnings("unchecked")
	private void switchFilter(final boolean on) throws UNISoNException {
		try {
			final UNISoNControllerFX controller = UNISoNControllerFX.getInstance();

			if (on) {
				final Date fromDate = StringUtils.stringToDate(this.fromDateField.getText());
				final Date toDate = StringUtils.stringToDate(this.toDateField.getText());
				controller.getFilter().setDates(fromDate, toDate);

				final List<GUIItem<Object>> selectedCountries = this.topCountriesList
				        .getSelectionModel().getSelectedItems();
				final Set<String> countries = new HashSet<>();
				for (final Object country : selectedCountries) {
					final GUIItem<ResultRow> row = (GUIItem<ResultRow>) country;
					final String selectedcountry = (String) row.getItem().getKey();
					countries.add(selectedcountry);
				}
				controller.getFilter().setSelectedCountries(countries);

				final List<GUIItem<Object>> selectedNewsgroups = this.topGroupsList
				        .getSelectionModel().getSelectedItems();
				final Vector<NewsGroup> groups = new Vector<>();
				for (final Object group : selectedNewsgroups) {
					final GUIItem<ResultRow> row = (GUIItem<ResultRow>) group;
					final NewsGroup selectedgroup = (NewsGroup) row.getItem().getKey();
					groups.add(selectedgroup);
				}
				controller.getFilter().setSelectedNewsgroups(groups);

				final List<GUIItem<Object>> selectedPosters = this.topPostersList
				        .getSelectionModel().getSelectedItems();
				final Vector<UsenetUser> posters = new Vector<>();
				for (final Object poster : selectedPosters) {
					final GUIItem<ResultRow> row = (GUIItem<ResultRow>) poster;
					final UsenetUser selectedUser = (UsenetUser) row.getItem().getKey();
					posters.add(selectedUser);
				}
				controller.getFilter().setSelectedPosters(posters);
				this.filterToggle.setText("Filtered");
				this.filterToggle.setTooltip(new Tooltip("Click again to remove filter"));
				this.refreshButton.setDisable(true);
			}
			else {
				this.filterToggle.setText("Filter");
				this.filterToggle.setTooltip(new Tooltip(
				        "Enter date values, select groups or posters in lists, or combination then click filter"));
				this.refreshButton.setDisable(false);
			}
			this.fromDateField.setEditable(!on);
			this.toDateField.setEditable(!on);
			controller.switchFiltered(on);
		}
		catch (final DateTimeParseException e) {
			this.alert("Failed to parse date : " + e.getMessage());
			this.filterToggle.setSelected(false);
		}
	}

	/**
	 * Topics hierarchy value changed.
	 *
	 * @param evt
	 *            the evt
	 */
	private void topicsHierarchyValueChanged(final javax.swing.event.TreeSelectionEvent evt) {// GEN-FIRST:event_topicsHierarchyValueChanged
		final TreePath tp = evt.getPath();
		final TreeNode root = (TreeNode) tp.getLastPathComponent();

		final Object datanode = root.getUserObject();
		if (datanode instanceof Message) {
			final Message msg = (Message) datanode;
			UNISoNControllerFX.getInstance().getFilter().setSelectedMessage(msg);
			this.notifySelectedMessageObservers();
		}
		else {
			// this.expandNode(root, this.missingMessagesCheck.isSelected()); TODO VERIFY LATER
		}
		this.notifySelectedMessageObservers();
	}// GEN-LAST:event_topicsHierarchyValueChanged

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(final Observable observable, final Object arg1) {
		this.refreshGUIData();
	}

}
