/*
 * MessageStoreViewer.java
 *
 * Created on 28 November 2007, 09:04
 */

package uk.co.sleonard.unison.gui.generated;

import java.awt.Dimension;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;

import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.datahandling.DAO.Location;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.ResultRow;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.gui.GUIItem;
import uk.co.sleonard.unison.gui.TreeNode;
import uk.co.sleonard.unison.gui.UNISoNController;
import uk.co.sleonard.unison.gui.UNISoNException;
import uk.co.sleonard.unison.gui.UNISoNLogger;
import uk.co.sleonard.unison.input.FullDownloadWorker;
import uk.co.sleonard.unison.utils.HttpDateObject;
import uk.co.sleonard.unison.utils.StringUtils;

/**
 * The Class MessageStoreViewer.
 *
 * @author Steve
 * @since
 * 
 */
public class MessageStoreViewer extends javax.swing.JPanel implements Observer, UNISoNLogger {

	/** The Constant serialVersionUID. */
	private static final long			serialVersionUID	= -4431795072981463365L;

	/** The body pane. */
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JTextArea		bodyPane;

	/** The body scroll pane. */
	private javax.swing.JScrollPane		bodyScrollPane;

	/** The crosspost combo box. */
	private javax.swing.JComboBox		crosspostComboBox;

	/** The filter toggle. */
	private javax.swing.JToggleButton	filterToggle;

	/** The from date field. */
	private javax.swing.JTextField		fromDateField;

	/** The from date label. */
	private javax.swing.JLabel			fromDateLabel;

	/** The get body button. */
	private javax.swing.JButton			getBodyButton;

	/** The groups hierarchy. */
	private javax.swing.JTree			groupsHierarchy;

	/** The groups scroll pane. */
	private javax.swing.JScrollPane		groupsScrollPane;

	/** The headers button. */
	private javax.swing.JButton			headersButton;

	/** The location field. */
	private javax.swing.JTextField		locationField;

	/** The location label. */
	private javax.swing.JLabel			locationLabel;

	/** The missing messages check. */
	private javax.swing.JCheckBox		missingMessagesCheck;

	/** The refresh button. */
	private javax.swing.JButton			refreshButton;

	/** The sender field. */
	private javax.swing.JTextField		senderField;

	/** The sender label. */
	private javax.swing.JLabel			senderLabel;

	/** The sent date field. */
	private javax.swing.JTextField		sentDateField;

	/** The sent date label. */
	private javax.swing.JLabel			sentDateLabel;

	/** The stats tab pane. */
	private javax.swing.JTabbedPane		statsTabPane;

	/** The subject field. */
	private javax.swing.JTextField		subjectField;

	/** The subject label. */
	private javax.swing.JLabel			subjectLabel;

	/** The to date field. */
	private javax.swing.JTextField		toDateField;

	/** The todate label. */
	private javax.swing.JLabel			todateLabel;

	/** The top countries list. */
	private javax.swing.JList			topCountriesList;

	/** The top countries scroll pane. */
	private javax.swing.JScrollPane		topCountriesScrollPane;

	/** The top groups list. */
	private javax.swing.JList			topGroupsList;

	/** The top groups scroll pane. */
	private javax.swing.JScrollPane		topGroupsScrollPane;

	/** The top posters list. */
	private javax.swing.JList			topPostersList;

	/** The top posters scroll pane. */
	private javax.swing.JScrollPane		topPostersScrollPane;

	/** The topics hierarchy. */
	private javax.swing.JTree			topicsHierarchy;

	/** The topics scroll pane. */
	private javax.swing.JScrollPane		topicsScrollPane;

	/** The session. */
	private Session						session;

	/** The newsgroup tree root. */
	private TreeNode					newsgroupTreeRoot;

	/** The topic root. */
	private TreeNode					topicRoot;

	/** The parser. */
	private final HttpDateObject		parser				= new HttpDateObject();

	// End of variables declaration//GEN-END:variables
	/**
	 * Creates new form MessageStoreViewer.
	 */
	public MessageStoreViewer() {
		this.initComponents();

		final Dimension size = this.sentDateField.getPreferredSize();
		this.sentDateField.setMaximumSize(size);
		this.sentDateField.setPreferredSize(size);
		this.subjectField.setMaximumSize(size);
		this.subjectField.setPreferredSize(size);
		this.senderField.setMaximumSize(size);
		this.senderField.setPreferredSize(size);

		try {

			this.session = UNISoNController.getInstance().helper().getHibernateSession();

			// FIXME disable all non-workng parts
			// headersButton.setVisible(false);
			this.getBodyButton.setVisible(false);
			this.missingMessagesCheck.setVisible(false);

			this.switchFilter(this.filterToggle.isSelected());
		}
		catch (final UNISoNException e) {
			UNISoNController.getInstance().showAlert("Error :" + e.getMessage());
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
	protected TreeNode addChildNode(final TreeNode root, final Object childObject, String name) {
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
			name += UNISoNController.getInstance().helper().getText(childObject);
		}

		final TreeNode child = new TreeNode(childObject, name);
		root.add(child);

		return child;
	}

	/**
	 * Adds the children.
	 *
	 * @param set
	 *            the set
	 * @param msgRoot
	 *            the msg root
	 */
	@SuppressWarnings("unchecked")
	private void addChildren(final Set set, final TreeNode msgRoot) {
		if (null != set) {
			this.iterateCollection(set.iterator(), msgRoot);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see uk.co.sleonard.unison.gui.UNISoNLogger#alert(java.lang.String)
	 */
	@Override
	public void alert(final String message) {
		this.log(message);
		UNISoNController.getInstance().showAlert(message);
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
		final Set<TreeNode> matches = new HashSet<TreeNode>();
		final Set<Message> copy = new HashSet<Message>(set);

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
		Set<Message> remainder = new HashSet<Message>(copy);
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
		final UNISoNController controller = UNISoNController.getInstance();
		final NewsGroup selectedGroup = (NewsGroup) this.crosspostComboBox.getSelectedItem();
		controller.setSelectedNewsgroup(selectedGroup);
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
			        UNISoNController.getInstance().getMessages(topic, this.session), root, "ROOT",
			        fillInMissing);
		}
	}

	/**
	 * Filter toggle action performed.
	 *
	 * @param evt
	 *            the evt
	 */
	private void filterToggleActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_filterToggleActionPerformed
		try {
			this.switchFilter(this.filterToggle.isSelected());
		}
		catch (final UNISoNException e) {
			UNISoNController.getInstance().showAlert("Error :" + e.getMessage());
		}
	}// GEN-LAST:event_filterToggleActionPerformed

	/**
	 * Gets the body button action performed.
	 *
	 * @param evt
	 *            the evt
	 * @return the body button action performed
	 */
	private void getBodyButtonActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_getBodyButtonActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_getBodyButtonActionPerformed

	/**
	 * Gets the list model.
	 *
	 * @param results
	 *            the results
	 * @return the list model
	 */
	private ListModel getListModel(final List<ResultRow> results) {
		final DefaultListModel model = new DefaultListModel();
		for (final ListIterator<ResultRow> iter = results.listIterator(); iter.hasNext();) {
			final Object next = iter.next();
			String name = next.toString();
			if (next instanceof UsenetUser) {
				name = ((UsenetUser) next).getName() + "<" + ((UsenetUser) next).getEmail() + ">";
			}
			else if (next instanceof Location) {
				name = ((Location) next).getCountry();
			}

			model.addElement(new GUIItem<Object>(name, next));
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
			UNISoNController.getInstance().setSelectedNewsgroup((NewsGroup) root.getUserObject());
		}
		else {
			UNISoNController.getInstance().setSelectedNewsgroup((String) root.getUserObject());
		}

		this.notifySelectedNewsGroupObservers();
	}// GEN-LAST:event_groupsHierarchyValueChanged

	/**
	 * Headers button action performed.
	 *
	 * @param evt
	 *            the evt
	 */
	private void headersButtonActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_headersButtonActionPerformed
		try {
			for (final Message message : UNISoNController.getInstance().getMessagesFilter()) {
				// only download for messages that need it
				if (null == message.getPoster().getLocation()) {
					FullDownloadWorker.addDownloadRequest(message.getUsenetMessageID(),
					        DownloadMode.HEADERS,
					        UNISoNController.getInstance().getDownloadPanel());
				}
			}
		}
		catch (final UNISoNException e) {
			this.alert("Failed to download extra fields: " + e.getMessage());
		}
	}// GEN-LAST:event_headersButtonActionPerformed

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT
	 * modify this code. The content of this method is always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// ">//GEN-BEGIN:initComponents
	private void initComponents() {
		this.groupsScrollPane = new javax.swing.JScrollPane();
		this.newsgroupTreeRoot = new TreeNode(null, "NewsGroups                                 ");
		this.groupsHierarchy = new javax.swing.JTree(this.newsgroupTreeRoot);
		this.bodyScrollPane = new javax.swing.JScrollPane();
		this.bodyPane = new javax.swing.JTextArea();
		this.topicsScrollPane = new javax.swing.JScrollPane();
		this.topicRoot = new TreeNode(null, "Topics");
		this.topicsHierarchy = new javax.swing.JTree(this.topicRoot);
		this.senderLabel = new javax.swing.JLabel();
		this.sentDateLabel = new javax.swing.JLabel();
		this.senderField = new javax.swing.JTextField();
		this.sentDateField = new javax.swing.JTextField();
		this.subjectLabel = new javax.swing.JLabel();
		this.subjectField = new javax.swing.JTextField();
		this.locationLabel = new javax.swing.JLabel();
		this.locationField = new javax.swing.JTextField();
		this.crosspostComboBox = new javax.swing.JComboBox();
		this.statsTabPane = new javax.swing.JTabbedPane();
		this.topPostersScrollPane = new javax.swing.JScrollPane();
		this.topPostersList = new javax.swing.JList();
		this.topGroupsScrollPane = new javax.swing.JScrollPane();
		this.topGroupsList = new javax.swing.JList();
		this.topCountriesScrollPane = new javax.swing.JScrollPane();
		this.topCountriesList = new javax.swing.JList();
		this.fromDateLabel = new javax.swing.JLabel();
		this.todateLabel = new javax.swing.JLabel();
		this.fromDateField = new javax.swing.JTextField();
		this.toDateField = new javax.swing.JTextField();
		this.missingMessagesCheck = new javax.swing.JCheckBox();
		this.refreshButton = new javax.swing.JButton();
		this.getBodyButton = new javax.swing.JButton();
		this.headersButton = new javax.swing.JButton();
		this.filterToggle = new javax.swing.JToggleButton();

		this.setPreferredSize(new java.awt.Dimension(461, 281));
		this.groupsHierarchy.addTreeSelectionListener(
		        evt -> MessageStoreViewer.this.groupsHierarchyValueChanged(evt));

		this.groupsScrollPane.setViewportView(this.groupsHierarchy);

		this.bodyPane.setColumns(20);
		this.bodyPane.setEditable(false);
		this.bodyPane.setRows(5);
		this.bodyScrollPane.setViewportView(this.bodyPane);

		this.topicsHierarchy.setAutoscrolls(true);
		this.topicsHierarchy.addTreeSelectionListener(
		        evt -> MessageStoreViewer.this.topicsHierarchyValueChanged(evt));

		this.topicsScrollPane.setViewportView(this.topicsHierarchy);

		this.senderLabel.setText("Sender");

		this.sentDateLabel.setText("Sent ");

		this.senderField.setEditable(false);

		this.sentDateField.setEditable(false);

		this.subjectLabel.setText("Subject");

		this.subjectField.setEditable(false);

		this.locationLabel.setText("Location");

		this.locationField.setEditable(false);

		this.crosspostComboBox.addActionListener(
		        evt -> MessageStoreViewer.this.crosspostComboBoxActionPerformed(evt));

		this.topPostersList.setModel(new javax.swing.AbstractListModel() {
			String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

			@Override
			public Object getElementAt(final int i) {
				return this.strings[i];
			}

			@Override
			public int getSize() {
				return this.strings.length;
			}
		});
		this.topPostersList.addListSelectionListener(
		        evt -> MessageStoreViewer.this.topPostersListValueChanged(evt));

		this.topPostersScrollPane.setViewportView(this.topPostersList);

		this.statsTabPane.addTab(" Posters", this.topPostersScrollPane);

		this.topGroupsList.setModel(new javax.swing.AbstractListModel() {
			String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

			@Override
			public Object getElementAt(final int i) {
				return this.strings[i];
			}

			@Override
			public int getSize() {
				return this.strings.length;
			}
		});
		this.topGroupsList.addListSelectionListener(
		        evt -> MessageStoreViewer.this.topGroupsListValueChanged(evt));

		this.topGroupsScrollPane.setViewportView(this.topGroupsList);

		this.statsTabPane.addTab("Groups", this.topGroupsScrollPane);

		this.topCountriesList.setModel(new javax.swing.AbstractListModel() {
			String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

			@Override
			public Object getElementAt(final int i) {
				return this.strings[i];
			}

			@Override
			public int getSize() {
				return this.strings.length;
			}
		});
		this.topCountriesList.addListSelectionListener(
		        evt -> MessageStoreViewer.this.topCountriesListValueChanged(evt));

		this.topCountriesScrollPane.setViewportView(this.topCountriesList);

		this.statsTabPane.addTab("Countries", this.topCountriesScrollPane);

		this.fromDateLabel.setText("Date from : ");

		this.todateLabel.setText("Date To:");

		this.fromDateField.setToolTipText("In YYYYMMDD format, e.g. 20070101");

		this.toDateField.setToolTipText("In YYYYMMDD format, e.g. 20070101");

		this.missingMessagesCheck.setText("Show ");
		this.missingMessagesCheck
		        .setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		this.missingMessagesCheck.setMargin(new java.awt.Insets(0, 0, 0, 0));
		this.missingMessagesCheck.addItemListener(
		        evt -> MessageStoreViewer.this.missingMessagesCheckItemStateChanged(evt));

		this.refreshButton.setText("Refresh Data");
		this.refreshButton.addActionListener(
		        evt -> MessageStoreViewer.this.refreshButtonActionPerformed(evt));

		this.getBodyButton.setText("Get Body");
		this.getBodyButton.addActionListener(
		        evt -> MessageStoreViewer.this.getBodyButtonActionPerformed(evt));

		this.headersButton.setText("Get Extras");
		this.headersButton.setToolTipText(
		        "Download extra fields: location and crossposts for messages in filter");
		this.headersButton.addActionListener(
		        evt -> MessageStoreViewer.this.headersButtonActionPerformed(evt));

		this.filterToggle.setText("Filter");
		this.filterToggle.setToolTipText(
		        "Enter date values, select groups or posters in lists, or combination then click filter");
		this.filterToggle
		        .addActionListener(evt -> MessageStoreViewer.this.filterToggleActionPerformed(evt));

		final javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(
		        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
		                javax.swing.GroupLayout.Alignment.TRAILING,
		                layout.createSequentialGroup()
		                        .addGroup(layout
		                                .createParallelGroup(
		                                        javax.swing.GroupLayout.Alignment.LEADING, false)
		                                .addComponent(this.topicsScrollPane,
		                                        javax.swing.GroupLayout.DEFAULT_SIZE, 179,
		                                        Short.MAX_VALUE)
		                                .addComponent(this.groupsScrollPane,
		                                        javax.swing.GroupLayout.DEFAULT_SIZE, 179,
		                                        Short.MAX_VALUE))
		                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                        .addGroup(layout
		                                .createParallelGroup(
		                                        javax.swing.GroupLayout.Alignment.LEADING, false)
		                                .addComponent(this.bodyScrollPane,
		                                        javax.swing.GroupLayout.DEFAULT_SIZE, 175,
		                                        Short.MAX_VALUE)
		                                .addGroup(layout.createSequentialGroup().addGap(4, 4, 4)
		                                        .addComponent(this.crosspostComboBox,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE, 171,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE))
		                                .addGroup(layout.createSequentialGroup()
		                                        .addGroup(layout
		                                                .createParallelGroup(
		                                                        javax.swing.GroupLayout.Alignment.LEADING)
		                                                .addComponent(this.subjectLabel)
		                                                .addComponent(this.locationLabel)
		                                                .addComponent(this.sentDateLabel)
		                                                .addComponent(this.senderLabel))
		                                        .addGap(9, 9, 9)
		                                        .addGroup(layout
		                                                .createParallelGroup(
		                                                        javax.swing.GroupLayout.Alignment.LEADING,
		                                                        false)
		                                                .addComponent(this.subjectField,
		                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                        118, Short.MAX_VALUE)
		                                                .addComponent(this.senderField)
		                                                .addComponent(this.locationField)
		                                                .addComponent(this.sentDateField))))
		                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                        .addGroup(layout
		                                .createParallelGroup(
		                                        javax.swing.GroupLayout.Alignment.LEADING, false)
		                                .addGroup(layout.createSequentialGroup()
		                                        .addComponent(this.statsTabPane,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE, 179,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE)
		                                        .addPreferredGap(
		                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
		                                                45, Short.MAX_VALUE))
		                                .addGroup(layout.createSequentialGroup()
		                                        .addGroup(layout
		                                                .createParallelGroup(
		                                                        javax.swing.GroupLayout.Alignment.LEADING)
		                                                .addComponent(this.todateLabel)
		                                                .addComponent(this.fromDateLabel))
		                                        .addPreferredGap(
		                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                        .addGroup(layout
		                                                .createParallelGroup(
		                                                        javax.swing.GroupLayout.Alignment.TRAILING)
		                                                .addComponent(this.toDateField,
		                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                        118, Short.MAX_VALUE)
		                                                .addComponent(this.fromDateField,
		                                                        javax.swing.GroupLayout.Alignment.LEADING,
		                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                        118, Short.MAX_VALUE)))
		                                .addGroup(layout.createSequentialGroup()
		                                        .addComponent(this.refreshButton)
		                                        .addPreferredGap(
		                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                        .addComponent(this.getBodyButton))
		                                .addGroup(layout.createSequentialGroup()
		                                        .addPreferredGap(
		                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                        .addGroup(layout
		                                                .createParallelGroup(
		                                                        javax.swing.GroupLayout.Alignment.LEADING,
		                                                        false)
		                                                .addComponent(this.filterToggle,
		                                                        javax.swing.GroupLayout.Alignment.TRAILING,
		                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                        Short.MAX_VALUE)
		                                                .addComponent(this.headersButton,
		                                                        javax.swing.GroupLayout.Alignment.TRAILING,
		                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                        Short.MAX_VALUE))
		                                        .addGap(26, 26, 26)
		                                        .addComponent(this.missingMessagesCheck)
		                                        .addGap(29, 29, 29)))
		                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
		                                Short.MAX_VALUE)));

		layout.linkSize(javax.swing.SwingConstants.HORIZONTAL,
		        new java.awt.Component[] { this.filterToggle, this.refreshButton });

		layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {
		        this.bodyScrollPane, this.statsTabPane, this.topicsScrollPane });

		layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {
		        this.locationField, this.senderField, this.sentDateField, this.subjectField });

		layout.setVerticalGroup(layout
		        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		        .addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout
		                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                .addComponent(this.groupsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE,
		                        142, javax.swing.GroupLayout.PREFERRED_SIZE)
		                .addGroup(layout.createSequentialGroup().addGroup(layout
		                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                        .addGroup(layout.createSequentialGroup()
		                                .addGroup(layout
		                                        .createParallelGroup(
		                                                javax.swing.GroupLayout.Alignment.BASELINE)
		                                        .addComponent(this.subjectLabel)
		                                        .addComponent(this.subjectField,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE,
		                                                javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE))
		                                .addPreferredGap(
		                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                .addGroup(layout
		                                        .createParallelGroup(
		                                                javax.swing.GroupLayout.Alignment.BASELINE)
		                                        .addComponent(this.sentDateLabel)
		                                        .addComponent(this.sentDateField,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE,
		                                                javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE))
		                                .addPreferredGap(
		                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                .addGroup(layout
		                                        .createParallelGroup(
		                                                javax.swing.GroupLayout.Alignment.BASELINE)
		                                        .addComponent(this.senderLabel)
		                                        .addComponent(this.senderField,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE,
		                                                javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE)
		                                        .addComponent(this.headersButton))
		                                .addPreferredGap(
		                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                .addGroup(layout
		                                        .createParallelGroup(
		                                                javax.swing.GroupLayout.Alignment.BASELINE)
		                                        .addComponent(this.locationLabel)
		                                        .addComponent(this.filterToggle)
		                                        .addComponent(this.locationField,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE,
		                                                javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
		                        .addGroup(layout.createSequentialGroup()
		                                .addGroup(layout
		                                        .createParallelGroup(
		                                                javax.swing.GroupLayout.Alignment.BASELINE)
		                                        .addComponent(this.fromDateLabel)
		                                        .addComponent(this.fromDateField,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE,
		                                                javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE))
		                                .addPreferredGap(
		                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                .addGroup(layout
		                                        .createParallelGroup(
		                                                javax.swing.GroupLayout.Alignment.BASELINE)
		                                        .addComponent(this.todateLabel)
		                                        .addComponent(this.toDateField,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE,
		                                                javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE))
		                                .addGap(21, 21, 21)
		                                .addComponent(this.missingMessagesCheck)))
		                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                        .addGroup(layout
		                                .createParallelGroup(
		                                        javax.swing.GroupLayout.Alignment.BASELINE)
		                                .addComponent(this.refreshButton)
		                                .addComponent(this.getBodyButton)
		                                .addComponent(this.crosspostComboBox,
		                                        javax.swing.GroupLayout.PREFERRED_SIZE,
		                                        javax.swing.GroupLayout.DEFAULT_SIZE,
		                                        javax.swing.GroupLayout.PREFERRED_SIZE))))
		                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                .addGroup(layout
		                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                        .addComponent(this.topicsScrollPane,
		                                javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
		                        .addComponent(this.statsTabPane,
		                                javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
		                        .addComponent(this.bodyScrollPane,
		                                javax.swing.GroupLayout.DEFAULT_SIZE, 121,
		                                Short.MAX_VALUE))));
	}// </editor-fold>//GEN-END:initComponents

	/**
	 * Iterate collection.
	 *
	 * @param iter2
	 *            the iter2
	 * @param msgRoot
	 *            the msg root
	 */
	private void iterateCollection(final Iterator<?> iter2, final TreeNode msgRoot) {
		while (iter2.hasNext()) {
			final Object object = iter2.next();
			this.addChildNode(msgRoot, object);
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
	 * Missing messages check item state changed.
	 *
	 * @param evt
	 *            the evt
	 */
	private void missingMessagesCheckItemStateChanged(final java.awt.event.ItemEvent evt) {// GEN-FIRST:event_missingMessagesCheckItemStateChanged
		// TODO add your handling code here:
	}// GEN-LAST:event_missingMessagesCheckItemStateChanged

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
	 * @param evt
	 *            the evt
	 */
	private void refreshButtonActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_refreshButtonActionPerformed
		UNISoNController.getInstance().refreshDataFromDatabase();
	}// GEN-LAST:event_refreshButtonActionPerformed

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
		final Message message = UNISoNController.getInstance().getSelectedMessage();

		if (null != message) {
			// final DefaultListModel model = this.getCrossPostsModel(message);
			// this.crosspostComboBox.setModel(model);

			String subject = message.getSubject();
			if (subject.length() > 18) {
				subject = subject.substring(0, 15) + "...";
			}
			this.subjectField.setText(subject);
			this.subjectField.setToolTipText(message.getSubject());

			String name2 = message.getPoster().getName();
			if (name2.length() > 18) {
				name2 = name2.substring(0, 15) + "...";
			}
			this.senderField.setText(name2);
			this.senderField.setToolTipText(message.getPoster().toString());

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
			this.locationField.setToolTipText(fullLocation);

			this.sentDateField.setText(
			        new SimpleDateFormat("dd MMM yyyy hh:mm").format(message.getDateCreated()));
			this.crosspostComboBox
			        .setModel(new DefaultComboBoxModel(message.getNewsgroups().toArray()));
			try {
				this.bodyPane.setText(StringUtils.decompress(message.getMessageBody()));
			}
			catch (final IOException e) {
				// TODO Auto-generated catch block
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
		final UNISoNController controller = UNISoNController.getInstance();
		final HashMap<String, TreeNode> nodeMap = new HashMap<String, TreeNode>();

		final List<NewsGroup> newsgroupFilter = new ArrayList<NewsGroup>();
		newsgroupFilter.addAll(controller.getNewsgroupFilter());
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
		((DefaultTreeModel) this.groupsHierarchy.getModel()).reload();
	}

	/**
	 * Refresh top countries.
	 */
	private void refreshTopCountries() {
		final List<ResultRow> results = UNISoNController.getInstance().getTopCountriesList();

		this.topCountriesList.setModel(this.getListModel(results));
	}

	/**
	 * Refresh top groups.
	 */
	private void refreshTopGroups() {
		final List<ResultRow> results = UNISoNController.getInstance().getTopGroupsList();

		this.topGroupsList.setModel(this.getListModel(results));
	}

	/**
	 * Refresh topic hierarchy.
	 */
	private void refreshTopicHierarchy() {
		// TODO reinstate that topics reflect the highlighted newsgroup

		this.topicRoot.removeAllChildren();

		final UNISoNController controller = UNISoNController.getInstance();
		final NewsGroup selectedNewsgroup = controller.getSelectedNewsgroup();
		if (null != selectedNewsgroup) {
			this.topicRoot.setName(selectedNewsgroup.getFullName());
			final Set<Topic> topics = selectedNewsgroup.getTopics();
			final Set<Topic> topicsFilter = controller.getTopicsFilter();
			for (final Topic topic : topics) {
				if (null == topicsFilter || topicsFilter.contains(topic)) {
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
		((DefaultTreeModel) this.topicsHierarchy.getModel()).reload();
	}

	/**
	 * Refresh top posters.
	 */
	private void refreshTopPosters() {
		final Vector<ResultRow> results = UNISoNController.getInstance().getTopPosters();

		this.topPostersList.setModel(this.getListModel(results));
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
			final UNISoNController controller = UNISoNController.getInstance();

			if (on) {
				final Date fromDate = this.parser.parseDate(this.fromDateField.getText());
				final Date toDate = this.parser.parseDate(this.toDateField.getText());
				controller.setDates(fromDate, toDate);

				final Object[] selectedCountries = this.topCountriesList.getSelectedValues();
				final Set<String> countries = new HashSet<String>();
				for (final Object country : selectedCountries) {
					final GUIItem<ResultRow> row = (GUIItem<ResultRow>) country;
					final String selectedcountry = (String) row.getItem().getKey();
					countries.add(selectedcountry);
				}
				controller.setSelectedCountries(countries);

				final Object[] selectedNewsgroups = this.topGroupsList.getSelectedValues();
				final Vector<NewsGroup> groups = new Vector<NewsGroup>();
				for (final Object group : selectedNewsgroups) {
					final GUIItem<ResultRow> row = (GUIItem<ResultRow>) group;
					final NewsGroup selectedgroup = (NewsGroup) row.getItem().getKey();
					groups.add(selectedgroup);
				}
				controller.setSelectedNewsgroups(groups);

				final Object[] selectedPosters = this.topPostersList.getSelectedValues();
				final Vector<UsenetUser> posters = new Vector<UsenetUser>();
				for (final Object poster : selectedPosters) {
					final GUIItem<ResultRow> row = (GUIItem<ResultRow>) poster;
					final UsenetUser selectedUser = (UsenetUser) row.getItem().getKey();
					posters.add(selectedUser);
				}
				controller.setSelectedPosters(posters);
				this.filterToggle.setText("Filtered");
				this.filterToggle.setToolTipText("Click again to remove filter");
				this.refreshButton.setEnabled(false);
			}
			else {
				this.filterToggle.setText("Filter");
				this.filterToggle.setToolTipText(
				        "Enter date values, select groups or posters in lists, or combination then click filter");
				this.refreshButton.setEnabled(true);
			}
			this.fromDateField.setEditable(!on);
			this.toDateField.setEditable(!on);
			controller.switchFiltered(on);
		}
		catch (final ParseException e) {
			this.alert("Failed to parse date : " + e.getMessage());
			this.filterToggle.setSelected(false);
		}
	}

	/**
	 * Top countries list value changed.
	 *
	 * @param evt
	 *            the evt
	 */
	private void topCountriesListValueChanged(final javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_topCountriesListValueChanged
		// TODO add your handling code here:
	}// GEN-LAST:event_topCountriesListValueChanged

	/**
	 * Top groups list value changed.
	 *
	 * @param evt
	 *            the evt
	 */
	private void topGroupsListValueChanged(final javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_topGroupsListValueChanged
		// TODO add your handling code here:
	}// GEN-LAST:event_topGroupsListValueChanged

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
			UNISoNController.getInstance().setSelectedMessage(msg);
			this.notifySelectedMessageObservers();
		}
		else {
			this.expandNode(root, this.missingMessagesCheck.isSelected());
		}
		this.notifySelectedMessageObservers();
	}// GEN-LAST:event_topicsHierarchyValueChanged

	/**
	 * Top posters list value changed.
	 *
	 * @param evt
	 *            the evt
	 */
	private void topPostersListValueChanged(final javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_topPostersListValueChanged
		// TODO add your handling code here:
	}// GEN-LAST:event_topPostersListValueChanged

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
