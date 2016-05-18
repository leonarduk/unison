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
 */
public class MessageStoreViewer extends javax.swing.JPanel implements Observer, UNISoNLogger {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4431795072981463365L;

	/** The body pane. */
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JTextArea bodyPane;

	/** The body scroll pane. */
	private javax.swing.JScrollPane bodyScrollPane;

	/** The crosspost combo box. */
	private javax.swing.JComboBox crosspostComboBox;

	/** The filter toggle. */
	private javax.swing.JToggleButton filterToggle;

	/** The from date field. */
	private javax.swing.JTextField fromDateField;

	/** The from date label. */
	private javax.swing.JLabel fromDateLabel;

	/** The get body button. */
	private javax.swing.JButton getBodyButton;

	/** The groups hierarchy. */
	private javax.swing.JTree groupsHierarchy;

	/** The groups scroll pane. */
	private javax.swing.JScrollPane groupsScrollPane;

	/** The headers button. */
	private javax.swing.JButton headersButton;

	/** The location field. */
	private javax.swing.JTextField locationField;

	/** The location label. */
	private javax.swing.JLabel locationLabel;

	/** The missing messages check. */
	private javax.swing.JCheckBox missingMessagesCheck;

	/** The refresh button. */
	private javax.swing.JButton refreshButton;

	/** The sender field. */
	private javax.swing.JTextField senderField;

	/** The sender label. */
	private javax.swing.JLabel senderLabel;

	/** The sent date field. */
	private javax.swing.JTextField sentDateField;

	/** The sent date label. */
	private javax.swing.JLabel sentDateLabel;

	/** The stats tab pane. */
	private javax.swing.JTabbedPane statsTabPane;

	/** The subject field. */
	private javax.swing.JTextField subjectField;

	/** The subject label. */
	private javax.swing.JLabel subjectLabel;

	/** The to date field. */
	private javax.swing.JTextField toDateField;

	/** The todate label. */
	private javax.swing.JLabel todateLabel;

	/** The top countries list. */
	private javax.swing.JList topCountriesList;

	/** The top countries scroll pane. */
	private javax.swing.JScrollPane topCountriesScrollPane;

	/** The top groups list. */
	private javax.swing.JList topGroupsList;

	/** The top groups scroll pane. */
	private javax.swing.JScrollPane topGroupsScrollPane;

	/** The top posters list. */
	private javax.swing.JList topPostersList;

	/** The top posters scroll pane. */
	private javax.swing.JScrollPane topPostersScrollPane;

	/** The topics hierarchy. */
	private javax.swing.JTree topicsHierarchy;

	/** The topics scroll pane. */
	private javax.swing.JScrollPane topicsScrollPane;

	// End of variables declaration//GEN-END:variables
	/**
	 * Creates new form MessageStoreViewer.
	 */
	public MessageStoreViewer() {
		this.initComponents();

		Dimension size = sentDateField.getPreferredSize();
		sentDateField.setMaximumSize(size);
		sentDateField.setPreferredSize(size);
		subjectField.setMaximumSize(size);
		subjectField.setPreferredSize(size);
		senderField.setMaximumSize(size);
		senderField.setPreferredSize(size);

		try {

			session = UNISoNController.getInstance().helper().getHibernateSession();

			// FIXME disable all non-workng parts
			// headersButton.setVisible(false);
			getBodyButton.setVisible(false);
			missingMessagesCheck.setVisible(false);

			switchFilter(filterToggle.isSelected());
		}
		catch (UNISoNException e) {
			UNISoNController.getInstance().showAlert("Error :" + e.getMessage());
		}
	}

	/** The session. */
	private Session session;

	/** The newsgroup tree root. */
	private TreeNode newsgroupTreeRoot;

	/** The topic root. */
	private TreeNode topicRoot;

	/** The parser. */
	private HttpDateObject parser = new HttpDateObject();

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
	public void alert(String message) {
		log(message);
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
	        final Object matchId, boolean fillInMissing) {
		final Set<TreeNode> matches = new HashSet<TreeNode>();
		final Set<Message> copy = new HashSet<Message>(set);

		for (final Iterator<Message> iter = set.iterator(); iter.hasNext();) {
			final Message next = iter.next();

			// compare to the last refered message, ie. the one they replied to
			String previousId = "ROOT";
			try {
				List<String> msgList = StringUtils.convertStringToList(next.getReferencedMessages(),
				        " ");

				if (msgList.size() > 0) {
					String lastMessageId = msgList.get(0);
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
		for (final Iterator<TreeNode> iter = matches.iterator(); iter.hasNext();) {
			final TreeNode next = iter.next();
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
	private void crosspostComboBoxActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_crosspostComboBoxActionPerformed
		UNISoNController controller = UNISoNController.getInstance();
		NewsGroup selectedGroup = (NewsGroup) crosspostComboBox.getSelectedItem();
		controller.setSelectedNewsgroup(selectedGroup);
		refreshTopicHierarchy();
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
	protected void expandNode(final TreeNode root, boolean fillInMissing) {

		final Object userObject = root.getUserObject();
		if (userObject instanceof Topic) {
			final Topic topic = (Topic) userObject;
			this.createMessageHierarchy(UNISoNController.getInstance().getMessages(topic, session),
			        root, "ROOT", fillInMissing);
		}
	}

	/**
	 * Filter toggle action performed.
	 *
	 * @param evt
	 *            the evt
	 */
	private void filterToggleActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_filterToggleActionPerformed
		try {
			switchFilter(filterToggle.isSelected());
		}
		catch (UNISoNException e) {
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
	private void getBodyButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_getBodyButtonActionPerformed
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
	private void groupsHierarchyValueChanged(javax.swing.event.TreeSelectionEvent evt) {// GEN-FIRST:event_groupsHierarchyValueChanged
		final TreePath tp = evt.getPath();
		final TreeNode root = (TreeNode) tp.getLastPathComponent();

		// as root is not a newsgroup
		if (root.getUserObject() instanceof NewsGroup) {
			UNISoNController.getInstance().setSelectedNewsgroup((NewsGroup) root.getUserObject());
		}
		else {
			UNISoNController.getInstance().setSelectedNewsgroup((String) root.getUserObject());
		}

		notifySelectedNewsGroupObservers();
	}// GEN-LAST:event_groupsHierarchyValueChanged

	/**
	 * Headers button action performed.
	 *
	 * @param evt
	 *            the evt
	 */
	private void headersButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_headersButtonActionPerformed
		try {
			for (Message message : UNISoNController.getInstance().getMessagesFilter()) {
				// only download for messages that need it
				if (null == message.getPoster().getLocation()) {
					FullDownloadWorker.addDownloadRequest(message.getUsenetMessageID(),
					        DownloadMode.HEADERS,
					        UNISoNController.getInstance().getDownloadPanel());
				}
			}
		}
		catch (UNISoNException e) {
			alert("Failed to download extra fields: " + e.getMessage());
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
		groupsScrollPane = new javax.swing.JScrollPane();
		this.newsgroupTreeRoot = new TreeNode(null, "NewsGroups                                 ");
		groupsHierarchy = new javax.swing.JTree(this.newsgroupTreeRoot);
		bodyScrollPane = new javax.swing.JScrollPane();
		bodyPane = new javax.swing.JTextArea();
		topicsScrollPane = new javax.swing.JScrollPane();
		topicRoot = new TreeNode(null, "Topics");
		topicsHierarchy = new javax.swing.JTree(this.topicRoot);
		senderLabel = new javax.swing.JLabel();
		sentDateLabel = new javax.swing.JLabel();
		senderField = new javax.swing.JTextField();
		sentDateField = new javax.swing.JTextField();
		subjectLabel = new javax.swing.JLabel();
		subjectField = new javax.swing.JTextField();
		locationLabel = new javax.swing.JLabel();
		locationField = new javax.swing.JTextField();
		crosspostComboBox = new javax.swing.JComboBox();
		statsTabPane = new javax.swing.JTabbedPane();
		topPostersScrollPane = new javax.swing.JScrollPane();
		topPostersList = new javax.swing.JList();
		topGroupsScrollPane = new javax.swing.JScrollPane();
		topGroupsList = new javax.swing.JList();
		topCountriesScrollPane = new javax.swing.JScrollPane();
		topCountriesList = new javax.swing.JList();
		fromDateLabel = new javax.swing.JLabel();
		todateLabel = new javax.swing.JLabel();
		fromDateField = new javax.swing.JTextField();
		toDateField = new javax.swing.JTextField();
		missingMessagesCheck = new javax.swing.JCheckBox();
		refreshButton = new javax.swing.JButton();
		getBodyButton = new javax.swing.JButton();
		headersButton = new javax.swing.JButton();
		filterToggle = new javax.swing.JToggleButton();

		setPreferredSize(new java.awt.Dimension(461, 281));
		groupsHierarchy.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
			@Override
			public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
				groupsHierarchyValueChanged(evt);
			}
		});

		groupsScrollPane.setViewportView(groupsHierarchy);

		bodyPane.setColumns(20);
		bodyPane.setEditable(false);
		bodyPane.setRows(5);
		bodyScrollPane.setViewportView(bodyPane);

		topicsHierarchy.setAutoscrolls(true);
		topicsHierarchy.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
			@Override
			public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
				topicsHierarchyValueChanged(evt);
			}
		});

		topicsScrollPane.setViewportView(topicsHierarchy);

		senderLabel.setText("Sender");

		sentDateLabel.setText("Sent ");

		senderField.setEditable(false);

		sentDateField.setEditable(false);

		subjectLabel.setText("Subject");

		subjectField.setEditable(false);

		locationLabel.setText("Location");

		locationField.setEditable(false);

		crosspostComboBox.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				crosspostComboBoxActionPerformed(evt);
			}
		});

		topPostersList.setModel(new javax.swing.AbstractListModel() {
			String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

			@Override
			public int getSize() {
				return strings.length;
			}

			@Override
			public Object getElementAt(int i) {
				return strings[i];
			}
		});
		topPostersList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			@Override
			public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
				topPostersListValueChanged(evt);
			}
		});

		topPostersScrollPane.setViewportView(topPostersList);

		statsTabPane.addTab(" Posters", topPostersScrollPane);

		topGroupsList.setModel(new javax.swing.AbstractListModel() {
			String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

			@Override
			public int getSize() {
				return strings.length;
			}

			@Override
			public Object getElementAt(int i) {
				return strings[i];
			}
		});
		topGroupsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			@Override
			public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
				topGroupsListValueChanged(evt);
			}
		});

		topGroupsScrollPane.setViewportView(topGroupsList);

		statsTabPane.addTab("Groups", topGroupsScrollPane);

		topCountriesList.setModel(new javax.swing.AbstractListModel() {
			String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

			@Override
			public int getSize() {
				return strings.length;
			}

			@Override
			public Object getElementAt(int i) {
				return strings[i];
			}
		});
		topCountriesList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			@Override
			public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
				topCountriesListValueChanged(evt);
			}
		});

		topCountriesScrollPane.setViewportView(topCountriesList);

		statsTabPane.addTab("Countries", topCountriesScrollPane);

		fromDateLabel.setText("Date from : ");

		todateLabel.setText("Date To:");

		fromDateField.setToolTipText("In YYYYMMDD format, e.g. 20070101");

		toDateField.setToolTipText("In YYYYMMDD format, e.g. 20070101");

		missingMessagesCheck.setText("Show ");
		missingMessagesCheck.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		missingMessagesCheck.setMargin(new java.awt.Insets(0, 0, 0, 0));
		missingMessagesCheck.addItemListener(new java.awt.event.ItemListener() {
			@Override
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				missingMessagesCheckItemStateChanged(evt);
			}
		});

		refreshButton.setText("Refresh Data");
		refreshButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				refreshButtonActionPerformed(evt);
			}
		});

		getBodyButton.setText("Get Body");
		getBodyButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				getBodyButtonActionPerformed(evt);
			}
		});

		headersButton.setText("Get Extras");
		headersButton.setToolTipText(
		        "Download extra fields: location and crossposts for messages in filter");
		headersButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				headersButtonActionPerformed(evt);
			}
		});

		filterToggle.setText("Filter");
		filterToggle.setToolTipText(
		        "Enter date values, select groups or posters in lists, or combination then click filter");
		filterToggle.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				filterToggleActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(
		        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
		                javax.swing.GroupLayout.Alignment.TRAILING,
		                layout.createSequentialGroup()
		                        .addGroup(layout
		                                .createParallelGroup(
		                                        javax.swing.GroupLayout.Alignment.LEADING, false)
		                                .addComponent(topicsScrollPane,
		                                        javax.swing.GroupLayout.DEFAULT_SIZE, 179,
		                                        Short.MAX_VALUE)
		                                .addComponent(groupsScrollPane,
		                                        javax.swing.GroupLayout.DEFAULT_SIZE, 179,
		                                        Short.MAX_VALUE))
		                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                        .addGroup(layout
		                                .createParallelGroup(
		                                        javax.swing.GroupLayout.Alignment.LEADING, false)
		                                .addComponent(bodyScrollPane,
		                                        javax.swing.GroupLayout.DEFAULT_SIZE, 175,
		                                        Short.MAX_VALUE)
		                                .addGroup(layout.createSequentialGroup().addGap(4, 4, 4)
		                                        .addComponent(crosspostComboBox,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE, 171,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE))
		                                .addGroup(layout.createSequentialGroup()
		                                        .addGroup(layout
		                                                .createParallelGroup(
		                                                        javax.swing.GroupLayout.Alignment.LEADING)
		                                                .addComponent(subjectLabel)
		                                                .addComponent(locationLabel)
		                                                .addComponent(sentDateLabel)
		                                                .addComponent(senderLabel))
		                                        .addGap(9, 9, 9)
		                                        .addGroup(layout
		                                                .createParallelGroup(
		                                                        javax.swing.GroupLayout.Alignment.LEADING,
		                                                        false)
		                                                .addComponent(subjectField,
		                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                        118, Short.MAX_VALUE)
		                                                .addComponent(senderField)
		                                                .addComponent(locationField)
		                                                .addComponent(sentDateField))))
		                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                        .addGroup(layout
		                                .createParallelGroup(
		                                        javax.swing.GroupLayout.Alignment.LEADING, false)
		                                .addGroup(layout.createSequentialGroup()
		                                        .addComponent(statsTabPane,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE, 179,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE)
		                                        .addPreferredGap(
		                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
		                                                45, Short.MAX_VALUE))
		                                .addGroup(layout.createSequentialGroup()
		                                        .addGroup(layout
		                                                .createParallelGroup(
		                                                        javax.swing.GroupLayout.Alignment.LEADING)
		                                                .addComponent(todateLabel)
		                                                .addComponent(fromDateLabel))
		                                        .addPreferredGap(
		                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                        .addGroup(layout
		                                                .createParallelGroup(
		                                                        javax.swing.GroupLayout.Alignment.TRAILING)
		                                                .addComponent(toDateField,
		                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                        118, Short.MAX_VALUE)
		                                                .addComponent(fromDateField,
		                                                        javax.swing.GroupLayout.Alignment.LEADING,
		                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                        118, Short.MAX_VALUE)))
		                                .addGroup(layout.createSequentialGroup()
		                                        .addComponent(refreshButton)
		                                        .addPreferredGap(
		                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                        .addComponent(getBodyButton))
		                                .addGroup(layout.createSequentialGroup()
		                                        .addPreferredGap(
		                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                        .addGroup(layout
		                                                .createParallelGroup(
		                                                        javax.swing.GroupLayout.Alignment.LEADING,
		                                                        false)
		                                                .addComponent(filterToggle,
		                                                        javax.swing.GroupLayout.Alignment.TRAILING,
		                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                        Short.MAX_VALUE)
		                                                .addComponent(headersButton,
		                                                        javax.swing.GroupLayout.Alignment.TRAILING,
		                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                        Short.MAX_VALUE))
		                                        .addGap(26, 26, 26)
		                                        .addComponent(missingMessagesCheck)
		                                        .addGap(29, 29, 29)))
		                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
		                                Short.MAX_VALUE)));

		layout.linkSize(javax.swing.SwingConstants.HORIZONTAL,
		        new java.awt.Component[] { filterToggle, refreshButton });

		layout.linkSize(javax.swing.SwingConstants.HORIZONTAL,
		        new java.awt.Component[] { bodyScrollPane, statsTabPane, topicsScrollPane });

		layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {
		        locationField, senderField, sentDateField, subjectField });

		layout.setVerticalGroup(layout
		        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		        .addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout
		                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                .addComponent(groupsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 142,
		                        javax.swing.GroupLayout.PREFERRED_SIZE)
		                .addGroup(layout.createSequentialGroup().addGroup(layout
		                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                        .addGroup(layout.createSequentialGroup()
		                                .addGroup(layout
		                                        .createParallelGroup(
		                                                javax.swing.GroupLayout.Alignment.BASELINE)
		                                        .addComponent(subjectLabel)
		                                        .addComponent(subjectField,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE,
		                                                javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE))
		                                .addPreferredGap(
		                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                .addGroup(layout
		                                        .createParallelGroup(
		                                                javax.swing.GroupLayout.Alignment.BASELINE)
		                                        .addComponent(sentDateLabel)
		                                        .addComponent(sentDateField,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE,
		                                                javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE))
		                                .addPreferredGap(
		                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                .addGroup(layout
		                                        .createParallelGroup(
		                                                javax.swing.GroupLayout.Alignment.BASELINE)
		                                        .addComponent(senderLabel)
		                                        .addComponent(senderField,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE,
		                                                javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE)
		                                        .addComponent(headersButton))
		                                .addPreferredGap(
		                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                .addGroup(layout
		                                        .createParallelGroup(
		                                                javax.swing.GroupLayout.Alignment.BASELINE)
		                                        .addComponent(locationLabel)
		                                        .addComponent(filterToggle)
		                                        .addComponent(locationField,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE,
		                                                javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
		                        .addGroup(layout.createSequentialGroup()
		                                .addGroup(layout
		                                        .createParallelGroup(
		                                                javax.swing.GroupLayout.Alignment.BASELINE)
		                                        .addComponent(fromDateLabel)
		                                        .addComponent(fromDateField,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE,
		                                                javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE))
		                                .addPreferredGap(
		                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                .addGroup(layout
		                                        .createParallelGroup(
		                                                javax.swing.GroupLayout.Alignment.BASELINE)
		                                        .addComponent(todateLabel).addComponent(toDateField,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE,
		                                                javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                javax.swing.GroupLayout.PREFERRED_SIZE))
		                                .addGap(21, 21, 21).addComponent(missingMessagesCheck)))
		                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                        .addGroup(layout
		                                .createParallelGroup(
		                                        javax.swing.GroupLayout.Alignment.BASELINE)
		                                .addComponent(refreshButton).addComponent(getBodyButton)
		                                .addComponent(crosspostComboBox,
		                                        javax.swing.GroupLayout.PREFERRED_SIZE,
		                                        javax.swing.GroupLayout.DEFAULT_SIZE,
		                                        javax.swing.GroupLayout.PREFERRED_SIZE))))
		                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                .addGroup(layout
		                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                        .addComponent(topicsScrollPane,
		                                javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
		                        .addComponent(statsTabPane, javax.swing.GroupLayout.DEFAULT_SIZE,
		                                121, Short.MAX_VALUE)
		                        .addComponent(bodyScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE,
		                                121, Short.MAX_VALUE))));
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
	public void log(String message) {
		// notesArea.append(message + "\n");
	}

	/**
	 * Missing messages check item state changed.
	 *
	 * @param evt
	 *            the evt
	 */
	private void missingMessagesCheckItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_missingMessagesCheckItemStateChanged
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
	private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_refreshButtonActionPerformed
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
		UNISoNController controller = UNISoNController.getInstance();
		HashMap<String, TreeNode> nodeMap = new HashMap<String, TreeNode>();

		List<NewsGroup> newsgroupFilter = new ArrayList<NewsGroup>();
		newsgroupFilter.addAll(controller.getNewsgroupFilter());
		Collections.sort(newsgroupFilter);

		for (NewsGroup group : newsgroupFilter) {
			final String[] nameparts = group.getFullName().split("\\.");
			String pathSoFar = "";
			TreeNode parent = newsgroupTreeRoot;

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
		List<ResultRow> results = UNISoNController.getInstance().getTopCountriesList();

		this.topCountriesList.setModel(this.getListModel(results));
	}

	/**
	 * Refresh top groups.
	 */
	private void refreshTopGroups() {
		List<ResultRow> results = UNISoNController.getInstance().getTopGroupsList();

		this.topGroupsList.setModel(this.getListModel(results));
	}

	/**
	 * Refresh topic hierarchy.
	 */
	private void refreshTopicHierarchy() {
		// TODO reinstate that topics reflect the highlighted newsgroup

		this.topicRoot.removeAllChildren();

		UNISoNController controller = UNISoNController.getInstance();
		NewsGroup selectedNewsgroup = controller.getSelectedNewsgroup();
		if (null != selectedNewsgroup) {
			this.topicRoot.setName(selectedNewsgroup.getFullName());
			Set<Topic> topics = selectedNewsgroup.getTopics();
			Set<Topic> topicsFilter = controller.getTopicsFilter();
			for (Topic topic : topics) {
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
		Vector<ResultRow> results = UNISoNController.getInstance().getTopPosters();

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
	private void switchFilter(boolean on) throws UNISoNException {
		try {
			UNISoNController controller = UNISoNController.getInstance();

			if (on) {
				Date fromDate = parser.parseDate(fromDateField.getText());
				Date toDate = parser.parseDate(toDateField.getText());
				controller.setDates(fromDate, toDate);

				Object[] selectedCountries = topCountriesList.getSelectedValues();
				Set<String> countries = new HashSet<String>();
				for (Object country : selectedCountries) {
					GUIItem<ResultRow> row = (GUIItem<ResultRow>) country;
					String selectedcountry = (String) row.getItem().getKey();
					countries.add(selectedcountry);
				}
				controller.setSelectedCountries(countries);

				Object[] selectedNewsgroups = topGroupsList.getSelectedValues();
				Vector<NewsGroup> groups = new Vector<NewsGroup>();
				for (Object group : selectedNewsgroups) {
					GUIItem<ResultRow> row = (GUIItem<ResultRow>) group;
					NewsGroup selectedgroup = (NewsGroup) row.getItem().getKey();
					groups.add(selectedgroup);
				}
				controller.setSelectedNewsgroups(groups);

				Object[] selectedPosters = topPostersList.getSelectedValues();
				Vector<UsenetUser> posters = new Vector<UsenetUser>();
				for (Object poster : selectedPosters) {
					GUIItem<ResultRow> row = (GUIItem<ResultRow>) poster;
					UsenetUser selectedUser = (UsenetUser) row.getItem().getKey();
					posters.add(selectedUser);
				}
				controller.setSelectedPosters(posters);
				filterToggle.setText("Filtered");
				filterToggle.setToolTipText("Click again to remove filter");
				refreshButton.setEnabled(false);
			}
			else {
				filterToggle.setText("Filter");
				filterToggle.setToolTipText(
				        "Enter date values, select groups or posters in lists, or combination then click filter");
				refreshButton.setEnabled(true);
			}
			fromDateField.setEditable(!on);
			toDateField.setEditable(!on);
			controller.switchFiltered(on);
		}
		catch (ParseException e) {
			alert("Failed to parse date : " + e.getMessage());
			filterToggle.setSelected(false);
		}
	}

	/**
	 * Top countries list value changed.
	 *
	 * @param evt
	 *            the evt
	 */
	private void topCountriesListValueChanged(javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_topCountriesListValueChanged
		// TODO add your handling code here:
	}// GEN-LAST:event_topCountriesListValueChanged

	/**
	 * Top groups list value changed.
	 *
	 * @param evt
	 *            the evt
	 */
	private void topGroupsListValueChanged(javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_topGroupsListValueChanged
		// TODO add your handling code here:
	}// GEN-LAST:event_topGroupsListValueChanged

	/**
	 * Topics hierarchy value changed.
	 *
	 * @param evt
	 *            the evt
	 */
	private void topicsHierarchyValueChanged(javax.swing.event.TreeSelectionEvent evt) {// GEN-FIRST:event_topicsHierarchyValueChanged
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
		notifySelectedMessageObservers();
	}// GEN-LAST:event_topicsHierarchyValueChanged

	/**
	 * Top posters list value changed.
	 *
	 * @param evt
	 *            the evt
	 */
	private void topPostersListValueChanged(javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_topPostersListValueChanged
		// TODO add your handling code here:
	}// GEN-LAST:event_topPostersListValueChanged

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object arg1) {
		refreshGUIData();
	}

}
