/*
 * UNISoNMain.java
 *
 * Created on 13 October 2007, 21:52
 */

package uk.co.sleonard.unison.gui.generated;

// <editor-fold defaultstate="collapsed" desc=" imports ">

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.SpinnerDateModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hsqldb.util.DatabaseManagerSwing;

import uk.co.sleonard.unison.datahandling.DAO.Location;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.gui.GUIItem;
import uk.co.sleonard.unison.gui.SimpleFileFilter;
import uk.co.sleonard.unison.gui.TreeNode;
import uk.co.sleonard.unison.gui.UNISoNController;
import uk.co.sleonard.unison.gui.UNISoNException;
import uk.co.sleonard.unison.gui.UNISoNController.MatrixType;
import uk.co.sleonard.unison.output.PajekNetworkFile;
import uk.co.sleonard.unison.utils.StringUtils;

// </editor-fold>

/**
 * 
 * @author steve
 */
public class UNISoNFrame extends javax.swing.JFrame {

	private static Logger logger = Logger.getLogger("UNISoNMain");

	private static final String PAJEK_NETWORK_FILE_DESCRIPTION = "Pajek Network File (.net)";

	/**
	 * 
	 */
	private static final long serialVersionUID = 191438424413071292L;

	// <editor-fold defaultstate="collapsed" desc=" Variables ">
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JDialog aboutDialog;

	private javax.swing.JMenuItem aboutMenuItem;

	private javax.swing.JButton cancelButton;

	private javax.swing.JButton closeABoutButton;

	private javax.swing.JButton connectButton;

	private javax.swing.JLabel countriesLabel;

	private javax.swing.JList countryList;

	private javax.swing.JLabel crosspostedGroupsLabel;

	private javax.swing.JScrollPane crossPostingScrollPane;

	private javax.swing.JList crossPostingsList;

	Date currentFromDate = null;

	Date currentToDate = null;

	private javax.swing.JTextField dateField;

	private javax.swing.JLabel dateFromLabel;

	private javax.swing.JLabel dateLabel;

	private javax.swing.JLabel dateToLabel;

	private javax.swing.JMenuItem dbMagrMenuItem;

	private javax.swing.JMenuItem deleteDBMenuItem;

	private javax.swing.JButton downloadButton;

	private javax.swing.JProgressBar downloadProgressBar;

	private javax.swing.JMenuItem exitMenuItem;

	private javax.swing.JMenu fileMenu;

	private javax.swing.JButton filterButton;

	private javax.swing.JSpinner fromDateSpinner;

	private javax.swing.JPanel graphicPanel;

	private final Vector groupsHeading = new Vector(Arrays.asList(new String[] {
			"NewsGroup", "Number of Posts" }));

	private javax.swing.JMenu helpMenu;

	private javax.swing.JSpinner hostChooser;

	private javax.swing.JLabel hostLabel;

	private javax.swing.JLabel jLabel1;

	private javax.swing.JLabel jLabel2;

	private javax.swing.JLabel jLabel3;

	private javax.swing.JLabel jLabel4;

	private javax.swing.JLabel jLabel5;

	private javax.swing.JLabel jLabel6;

	private javax.swing.JLabel jLabel7;

	private javax.swing.JScrollPane jScrollPane1;

	private javax.swing.JScrollPane jScrollPane2;

	private javax.swing.JSeparator jSeparator1;

	private javax.swing.JSeparator jSeparator2;

	private javax.swing.JRadioButton matrixAllCurrent;

	private javax.swing.JRadioButton matrixFirstCurrent;

	private javax.swing.ButtonGroup matrixGroup;

	private javax.swing.JRadioButton matrixLastCurrent;

	private javax.swing.JMenuBar menuBar;

	private javax.swing.JTextField messageArea;

	private javax.swing.JScrollPane messageBodyScrollPane;

	private javax.swing.JTextArea messageBodyTextArea;

	private javax.swing.JComboBox newsgroupDropDown;

	private javax.swing.JTree newsGroupHiearchy;

	private javax.swing.JLabel newsgroupLabel;

	private javax.swing.JPopupMenu newsgroupPopupMenu;

	private javax.swing.JScrollPane newsgroupScrollPane;

	TreeNode newsgroupTreeRoot;

	private final Vector pajekHeader = new Vector(Arrays.asList(new String[] {
			"row", "FROM", "TO" }));

	private javax.swing.JButton pauseButton;

	private final Vector postersHeading = new Vector(Arrays
			.asList(new String[] { "Poster", "Number of Posts" }));

	private javax.swing.JLabel postersLabel;

	private javax.swing.JList postersList;

	String query = "";

	private javax.swing.JButton refreshDataButton;

	private javax.swing.JMenuItem reloadDataMenuItem;

	private javax.swing.JTabbedPane resultsTabbedPane;

	private javax.swing.JTable resultsTable;

	private javax.swing.JScrollPane resultsTableScrollPane;

	private javax.swing.JButton saveToPajekButton;

	private javax.swing.JMenuItem selectGroupMenuItem;

	private javax.swing.JTextField senderField;

	private javax.swing.JLabel senderLabel;

	private javax.swing.JMenuItem showAllSoFarMenuItem;

	private javax.swing.JMenuItem showAsCreatorReplierMenuItem;

	private javax.swing.JMenuItem showAsLastVsReplierMenuItem;

	private javax.swing.JButton showUserButton;

	private javax.swing.JTabbedPane statsTabbedPane;

	private javax.swing.JTextField subjectField;

	private javax.swing.JLabel subjectLabel;

	private javax.swing.JTextField timeLeftField;

	private javax.swing.JLabel timeLeftLabel;

	private javax.swing.JSpinner toDateSpinner;

	private javax.swing.JScrollPane topGroupsScrollPane;

	private javax.swing.JTable topGroupsTable;

	private javax.swing.JTree topicMessageHierarchy;

	private TreeNode topicRoot;

	private javax.swing.JScrollPane topicScrollPane;
	// End of variables declaration//GEN-END:variables
	// </editor-fold>

	private javax.swing.JScrollPane topPostersScrollPane;

	private javax.swing.JTable topPostersTable;

	/** Creates new form UNISoNMain */
	public UNISoNFrame() {
		this.initComponents();

		// My custom part

		this.newsgroupDropDown.setEditable(true);

		this.topGroupsTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(
							final ListSelectionEvent listSelectionEvent) {

						final GUIItem<NewsGroup> item = (GUIItem<NewsGroup>) UNISoNFrame.this.topGroupsTable
								.getModel().getValueAt(
										UNISoNFrame.this.topGroupsTable
												.getSelectedRow(), 0);
						UNISoNController.getInstance().setSelectedNewsgroup(
								item.getItem());
						UNISoNFrame.logger.debug("top groups valueChanged : "
								+ item.toString());

					}
				});

		this.resultsTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(
							final ListSelectionEvent listSelectionEvent) {

						final TableModel model = UNISoNFrame.this.resultsTable
								.getModel();

						final GUIItem<Message> item = (GUIItem<Message>) model
								.getValueAt(UNISoNFrame.this.resultsTable
										.getSelectedRow(), 1);
						UNISoNController.getInstance().setSelectedMessage(item.getItem());
						UNISoNFrame.logger.debug("results valueChanged : "
								+ item.toString());

					}
				});

	}

	private void aboutMenuItemActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_aboutMenuItemActionPerformed
		this.aboutDialog.setSize(new Dimension(330, 265));
		this.aboutDialog.setVisible(true);
	}// GEN-LAST:event_aboutMenuItemActionPerformed

	protected TreeNode addChildNode(final TreeNode root,
			final Object childObject) {
		return this.addChildNode(root, childObject, "");
	}

	protected TreeNode addChildNode(final TreeNode root,
			final Object childObject, String name) {
		if (childObject instanceof Set<?>) {
			if (((Set<?>) childObject).size() == 0) {
				// if no entries then don't add it
				return null;
			}
		} else if (childObject instanceof String) {
			name += " : " + childObject;
		} else {
			name += UNISoNController.getInstance().helper()
					.getText(childObject);
		}

		final TreeNode child = new TreeNode(childObject, name);
		root.add(child);

		return child;
	}

	private void addChildren(final List<?> list, final TreeNode msgRoot) {
		if ((null != list) && (list.size() > 0)) {
			this.iterateCollection(list.iterator(), msgRoot);
		}
	}

	@SuppressWarnings("unchecked")
	private void addChildren(final Set set, final TreeNode msgRoot) {
		if (null != set) {
			this.iterateCollection(set.iterator(), msgRoot);
		}
	}

	private Date boundDateRange(final Date dateToTest, final Date min,
			final Date max, final boolean useMinNotMax) {
		if (null == dateToTest) {
			if (useMinNotMax) {
				return min;
			} else {
				return max;
			}
		} else if (dateToTest.after(max)) {
			return max;
		} else if (dateToTest.before(min)) {
			return min;
		}
		return dateToTest;
	}

	private void cancelButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cancelButtonActionPerformed
		UNISoNController.getInstance().cancelDownload();
	}// GEN-LAST:event_cancelButtonActionPerformed

	/**
	 * 
	 * @param evt
	 */
	private void closeABoutButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_closeABoutButtonActionPerformed
		this.aboutDialog.setVisible(false);
	}// GEN-LAST:event_closeABoutButtonActionPerformed

	private void connectButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_connectButtonActionPerformed
		UNISoNController.getInstance().connectToNewsGroup(
				this.newsgroupDropDown.getSelectedItem().toString());
	}// GEN-LAST:event_connectButtonActionPerformed

	private List<Message> createMessageHierarchy(final List<Message> list,
			final TreeNode root, final Object matchId) {
		final ArrayList<TreeNode> matches = new ArrayList<TreeNode>();
		final ArrayList<Message> copy = new ArrayList<Message>(list);

		for (final ListIterator<Message> iter = list.listIterator(); iter
				.hasNext();) {
			final Message next = iter.next();

			// compare to the last refered message, ie. the one they replied to
			String previousId = "ROOT";
			try {
				List<String> msgList = StringUtils.convertStringToList(next
						.getReferencedMessages(), " ");
				if (msgList.size() > 0) {
					previousId = msgList.get(0);
				}
			} catch (final ObjectNotFoundException e) {
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
		List<Message> remainder = new ArrayList<Message>(copy);
		for (final ListIterator<TreeNode> iter = matches.listIterator(); iter
				.hasNext();) {
			final TreeNode next = iter.next();
			remainder = this.createMessageHierarchy(remainder, next,
					((Message) next.getUserObject()).getUsenetMessageID());
		}
		return copy;
	}

	@SuppressWarnings("unchecked")
	private void crossPostingsListValueChanged(
			final javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_crossPostingsListValueChanged
		final GUIItem<NewsGroup> item = (GUIItem<NewsGroup>) this.crossPostingsList
				.getSelectedValue();
		UNISoNController.getInstance().setSelectedNewsgroup(item.getItem());
	}// GEN-LAST:event_crossPostingsListValueChanged

	private void dbMagrMenuItemActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_dbMagrMenuItemActionPerformed
		final DatabaseManagerSwing dbMgr = new DatabaseManagerSwing();

		dbMgr.main(GUI_ARGS);
	}// GEN-LAST:event_dbMagrMenuItemActionPerformed

	private final static String dbDriver = "org.hsqldb.jdbcDriver";

	private final static String dbUser = "sa";
	private final static String DB_URL = "jdbc:hsqldb:file:DB/projectDB";

	public static final String GUI_ARGS[] = { "-driver", dbDriver, "-url",
			DB_URL, "-user", dbUser };

	private void deleteDBMenuItemActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_deleteDBMenuItemActionPerformed
		final int response = JOptionPane.showConfirmDialog(this,
				"This will delete ALL the data. Are you sure?", "DB Refresh",
				JOptionPane.YES_NO_OPTION);
		switch (response) {
		case JOptionPane.YES_OPTION:
			UNISoNController.getInstance().helper().generateSchema();
			this.showAlert("DB refresh complete");
			UNISoNController.getInstance().refreshDataFromDatabase();
			break;
		default:
			this.showAlert("DB refresh cancelled");
		}

	}// GEN-LAST:event_deleteDBMenuItemActionPerformed

	private void downloadButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_downloadButtonActionPerformed
		try {
			UNISoNController.getInstance().downloadMessages(
					UNISoNController.getInstance().getSelectedNewsgroup()
							.getFullName(), null, null);
		} catch (UNISoNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}// GEN-LAST:event_downloadButtonActionPerformed

	private void exitMenuItemActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_exitMenuItemActionPerformed
		final int response = JOptionPane.showConfirmDialog(this,
				"Are you sure?", "Exit Application", JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}// GEN-LAST:event_exitMenuItemActionPerformed

	protected void expandNode(final TreeNode root) {

		final Object userObject = root.getUserObject();
		if (userObject instanceof NewsGroup) {
			final NewsGroup group = (NewsGroup) userObject;

			this.addChildren(UNISoNController.getInstance().helper()
					.getChildNewsGroups(group), root);
		} else if (userObject instanceof Set) {
			this.addChildren((Set) userObject, root);
			// } else if (userObject instanceof Message) {
			// Message message = (Message) userObject;
		} else if (userObject instanceof Topic) {
			final Topic topic = (Topic) userObject;
			this.createMessageHierarchy(UNISoNController.getInstance().helper()
					.getMessages(topic), root, "ROOT");
		}
	}

	private void filterButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_filterButtonActionPerformed

		// reverse the status from filtered to non-filtered
		UNISoNController.getInstance().switchFiltered();

		if (UNISoNController.getInstance().isFiltered()) {
			this.filterButton.setText("Remove Filter");
		} else {
			this.filterButton.setText("  Apply Filter  ");
		}

	}// GEN-LAST:event_filterButtonActionPerformed

	private DefaultListModel getCrossPostsModel(final Message message) {
		final DefaultListModel model = new DefaultListModel();
		if (null != message) {
			for (final Iterator<NewsGroup> iter = message.getNewsgroups()
					.iterator(); iter.hasNext();) {
				final NewsGroup next = iter.next();
				model.addElement(new GUIItem<NewsGroup>(next.getFullName(),
						next));
			}
		}
		return model;

	}

	private NewsGroup getCurrentNewsGroupNode() {
		NewsGroup group = null;

		final TreePath path = this.newsGroupHiearchy.getSelectionPath();
		if (null != path) {
			final TreeNode node = (TreeNode) path.getLastPathComponent();
			if (node.getUserObject() instanceof NewsGroup) {
				group = ((NewsGroup) node.getUserObject());
			}
		}
		return group;
	}

	private SpinnerDateModel getDateModel(final boolean isFromDate) {
		final String sql = "select MAX(datecreated) as latest, MIN(datecreated) as earliest "
				+ " from Message";
		final Object[] result = (Object[]) UNISoNController.getInstance()
				.helper().runSQLQuery(sql).get(0);

		Date toDate = null;
		Date fromDate = null;
		Date thisDate = null;
		SpinnerDateModel model = new SpinnerDateModel();

		if (null != result[0]) {
			toDate = (Date) result[0];
			fromDate = (Date) result[1];

			if (isFromDate) {
				this.currentFromDate = this.boundDateRange(fromDate, toDate,
						fromDate, true);
				thisDate = this.currentFromDate;
			} else {
				this.currentToDate = this.boundDateRange(fromDate, toDate,
						fromDate, true);
				thisDate = this.currentToDate;
			}
			model = new SpinnerDateModel(thisDate, fromDate, toDate,
					Calendar.DAY_OF_WEEK_IN_MONTH);
		}

		return model;
	}

	private Vector getLatestPajekMatrixVector() {
		final String sql = " from Message where 1=1 ";
		final Object[] posters = this.postersList.getSelectedValues();
		if ((null != posters) && (posters.length > 0)) {
			this.query += " AND usenetuser_id in ( ";
			for (final Object item : posters) {
				final GUIItem<UsenetUser> posterItem = (GUIItem<UsenetUser>) item;
				this.query += posterItem.getItem().getId() + ",";
			}
			this.query = this.query.substring(0, this.query.length() - 1);
			this.query += ") ";
		}

		final Query query = UNISoNController.getInstance().helper()
				.getHibernateSession().createQuery(sql);
		Vector<Vector<String>> tableData;
		final List messages = UNISoNController.getInstance().helper().runQuery(
				query);
		HashMap<String, Message> msgMap = new HashMap<String, Message>();
		for (Object next : messages) {
			Message msg = (Message) next;
			msgMap.put(msg.getUsenetMessageID(), msg);
		}
		UNISoNFrame.logger.debug("Messages" + messages);
		tableData = new Vector<Vector<String>>();
		int rowIndex = 1;
		for (final ListIterator<Message> msgIter = messages.listIterator(); msgIter
				.hasNext();) {
			final Message next = msgIter.next();
			Message lastMsg = null;

			List refMsgs = StringUtils.convertStringToList(next
					.getReferencedMessages(), " ");
			int size;
			try {
				size = refMsgs.size();
			} catch (final Exception e) {
				e.printStackTrace();
				size = 0;
			}
			// if ((null != refMsgs) && (size > 0)) {
			// if (this.matrixAllCurrent.isSelected()) {
			// for (final ListIterator<Message> iter = next
			// .getReferencedMessages().listIterator(); iter
			// .hasNext();) {
			// tableData.add(this.createNewRow(rowIndex++, next, iter
			// .next()));
			// }
			// } else {
			// if (this.matrixFirstCurrent.isSelected()) {
			// lastMsg = (Message) refMsgs.get(refMsgs.size() - 1);
			// } else if (this.matrixLastCurrent.isSelected()) {
			// lastMsg = (Message) refMsgs.get(0);
			// }
			//
			// }
			// tableData.add(this.createNewRow(rowIndex++, next, lastMsg));
			// } else {
			// tableData.add(this.createNewRow(rowIndex++, next, null));
			// }

		}
		return tableData;
	}

	private ListModel getListModel(final Vector list) {
		final DefaultListModel model = new DefaultListModel();
		for (final ListIterator iter = list.listIterator(); iter.hasNext();) {
			final Object next = iter.next();
			String name = next.toString();
			if (next instanceof UsenetUser) {
				name = ((UsenetUser) next).getName() + "<"
						+ ((UsenetUser) next).getEmail() + ">";
			} else if (next instanceof Location) {
				name = ((Location) next).getCountry();
			}

			model.addElement(new GUIItem(name, next));
		}
		return model;
	}

	private Vector getListVector(final String type) {
		final String sql = " from " + type;
		UNISoNFrame.logger.debug("SQL: " + sql);
		final List results = UNISoNController.getInstance().helper().runQuery(
				sql);
		return new Vector(results);
	}

	private Vector getListVector(final String type, final String keyfield) {
		final String sql = "select distinct " + keyfield + " from " + type;
		UNISoNFrame.logger.debug("SQL: " + sql);
		final List results = UNISoNController.getInstance().helper()
				.runSQLQuery(sql);
		return new Vector(results);
	}

	private ComboBoxModel getNewsGroupList() {
		final List<NewsGroup> newsgroups = UNISoNController.getInstance()
				.helper().fetchBaseNewsGroups();
		final Vector itemList = GUIItem.getGUIList(newsgroups, UNISoNController
				.getInstance().helper());

		final ComboBoxModel model = new DefaultComboBoxModel(itemList);
		return model;
	}

	private TableModel getPajekMatrixModel() {
		final Vector tableData = this.getLatestPajekMatrixVector();
		final DefaultTableModel model = new DefaultTableModel(tableData,
				this.pajekHeader);
		return model;
	}

	/**
	 * 
	 * @return
	 */
	public Object[] getSelectedCountries() {
		return this.countryList.getSelectedValues();
	}

	public String getSelectedHost() {
		final String host = this.hostChooser.getValue().toString();
		return host;
	}

	public Object[] getSelectedPosters() {
		return this.postersList.getSelectedValues();
	}

	private TableModel getTopGroupsModel() {
		final DefaultTableModel model = new DefaultTableModel();
		model.setDataVector(
				UNISoNController.getInstance().getTopGroupsVector(),
				this.groupsHeading);
		return model;
	}

	private TableModel getTopPostersModel() {
		final DefaultTableModel model = new DefaultTableModel();
		model.setDataVector(UNISoNController.getInstance()
				.getTopPostersVector(), this.postersHeading);
		return model;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// ">//GEN-BEGIN:initComponents
	private void initComponents() {
		this.aboutDialog = new javax.swing.JDialog();
		this.jLabel1 = new javax.swing.JLabel();
		this.jLabel2 = new javax.swing.JLabel();
		this.closeABoutButton = new javax.swing.JButton();
		this.jLabel3 = new javax.swing.JLabel();
		this.jLabel4 = new javax.swing.JLabel();
		this.jLabel5 = new javax.swing.JLabel();
		this.jLabel6 = new javax.swing.JLabel();
		this.jLabel7 = new javax.swing.JLabel();
		this.newsgroupPopupMenu = new javax.swing.JPopupMenu();
		this.selectGroupMenuItem = new javax.swing.JMenuItem("Connect to ");
		this.showAsCreatorReplierMenuItem = new javax.swing.JMenuItem();
		this.showAsLastVsReplierMenuItem = new javax.swing.JMenuItem();
		this.showAllSoFarMenuItem = new javax.swing.JMenuItem();
		this.matrixGroup = new javax.swing.ButtonGroup();
		this.topicScrollPane = new javax.swing.JScrollPane();
		this.topicRoot = new TreeNode(null, "Topics");
		this.topicMessageHierarchy = new javax.swing.JTree(this.topicRoot);
		this.subjectField = new javax.swing.JTextField();
		this.subjectLabel = new javax.swing.JLabel();
		this.senderLabel = new javax.swing.JLabel();
		this.dateLabel = new javax.swing.JLabel();
		this.senderField = new javax.swing.JTextField();
		this.dateField = new javax.swing.JTextField();
		this.newsgroupScrollPane = new javax.swing.JScrollPane();
		this.newsgroupTreeRoot = new TreeNode(null,
				"NewsGroups                                 ");
		this.newsGroupHiearchy = new javax.swing.JTree(this.newsgroupTreeRoot);
		this.crossPostingScrollPane = new javax.swing.JScrollPane();
		this.crossPostingsList = new javax.swing.JList();
		this.messageBodyScrollPane = new javax.swing.JScrollPane();
		this.messageBodyTextArea = new javax.swing.JTextArea();
		this.hostLabel = new javax.swing.JLabel();
		final String[] hosts = new String[] { "freetext.usenetserver.com" };

		final javax.swing.SpinnerListModel hostsList = new javax.swing.SpinnerListModel(
				hosts);
		this.hostChooser = new javax.swing.JSpinner(hostsList);
		this.newsgroupLabel = new javax.swing.JLabel();
		this.newsgroupDropDown = new javax.swing.JComboBox();
		this.connectButton = new javax.swing.JButton();
		this.downloadButton = new javax.swing.JButton();
		this.pauseButton = new javax.swing.JButton();
		this.crosspostedGroupsLabel = new javax.swing.JLabel();
		this.timeLeftField = new javax.swing.JTextField();
		this.cancelButton = new javax.swing.JButton();
		this.timeLeftLabel = new javax.swing.JLabel();
		this.downloadProgressBar = new javax.swing.JProgressBar();
		this.showUserButton = new javax.swing.JButton();
		this.statsTabbedPane = new javax.swing.JTabbedPane();
		this.topGroupsScrollPane = new javax.swing.JScrollPane();
		this.topGroupsTable = new javax.swing.JTable();
		this.topPostersScrollPane = new javax.swing.JScrollPane();
		this.topPostersTable = new javax.swing.JTable();
		this.resultsTabbedPane = new javax.swing.JTabbedPane();
		this.graphicPanel = new javax.swing.JPanel();
		this.resultsTableScrollPane = new javax.swing.JScrollPane();
		this.resultsTable = new javax.swing.JTable();
		this.refreshDataButton = new javax.swing.JButton();
		this.saveToPajekButton = new javax.swing.JButton();
		this.dateFromLabel = new javax.swing.JLabel();
		this.dateToLabel = new javax.swing.JLabel();
		this.countriesLabel = new javax.swing.JLabel();
		this.postersLabel = new javax.swing.JLabel();
		this.messageArea = new javax.swing.JTextField();
		this.matrixFirstCurrent = new javax.swing.JRadioButton();
		this.matrixLastCurrent = new javax.swing.JRadioButton();
		this.matrixAllCurrent = new javax.swing.JRadioButton();
		this.toDateSpinner = new javax.swing.JSpinner();
		this.fromDateSpinner = new javax.swing.JSpinner();
		this.jScrollPane1 = new javax.swing.JScrollPane();
		this.countryList = new javax.swing.JList();
		this.jScrollPane2 = new javax.swing.JScrollPane();
		this.postersList = new javax.swing.JList();
		this.filterButton = new javax.swing.JButton();
		this.menuBar = new javax.swing.JMenuBar();
		this.fileMenu = new javax.swing.JMenu();
		this.dbMagrMenuItem = new javax.swing.JMenuItem();
		this.reloadDataMenuItem = new javax.swing.JMenuItem();
		this.jSeparator2 = new javax.swing.JSeparator();
		this.exitMenuItem = new javax.swing.JMenuItem();
		this.helpMenu = new javax.swing.JMenu();
		this.deleteDBMenuItem = new javax.swing.JMenuItem();
		this.jSeparator1 = new javax.swing.JSeparator();
		this.aboutMenuItem = new javax.swing.JMenuItem();

		this.aboutDialog.setResizable(false);
		this.jLabel1.setFont(new java.awt.Font("Dialog", 1, 18));
		this.jLabel1.setText("UNISoN ");

		this.jLabel2.setText("Author : Steve Leonard 2007 ");

		this.closeABoutButton.setText("Close");
		this.closeABoutButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						UNISoNFrame.this.closeABoutButtonActionPerformed(evt);
					}
				});

		this.jLabel3.setText("created for a university project part of an ");

		this.jLabel4.setText("MSc in Business Systems Analysis & Design");

		this.jLabel5.setText("at City University, London");

		this.jLabel6.setFont(new java.awt.Font("Dialog", 1, 14));
		this.jLabel6.setText("UseNet Incorporates Social Networks");

		this.jLabel7.setText("http://unison.sleonard.co.uk");

		final org.jdesktop.layout.GroupLayout aboutDialogLayout = new org.jdesktop.layout.GroupLayout(
				this.aboutDialog.getContentPane());
		this.aboutDialog.getContentPane().setLayout(aboutDialogLayout);
		aboutDialogLayout
				.setHorizontalGroup(aboutDialogLayout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								aboutDialogLayout.createSequentialGroup().add(
										78, 78, 78).add(this.jLabel5)
										.addContainerGap(80, Short.MAX_VALUE))
						.add(
								aboutDialogLayout
										.createSequentialGroup()
										.add(
												aboutDialogLayout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.TRAILING)
														.add(
																org.jdesktop.layout.GroupLayout.LEADING,
																aboutDialogLayout
																		.createSequentialGroup()
																		.add(
																				25,
																				25,
																				25)
																		.add(
																				aboutDialogLayout
																						.createParallelGroup(
																								org.jdesktop.layout.GroupLayout.LEADING)
																						.add(
																								aboutDialogLayout
																										.createSequentialGroup()
																										.add(
																												this.jLabel1)
																										.addPreferredGap(
																												org.jdesktop.layout.LayoutStyle.RELATED,
																												106,
																												Short.MAX_VALUE))
																						.add(
																								this.jLabel2))
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED)
																		.add(
																				this.closeABoutButton))
														.add(
																org.jdesktop.layout.GroupLayout.LEADING,
																aboutDialogLayout
																		.createSequentialGroup()
																		.add(
																				41,
																				41,
																				41)
																		.add(
																				this.jLabel6))
														.add(
																aboutDialogLayout
																		.createSequentialGroup()
																		.addContainerGap()
																		.add(
																				aboutDialogLayout
																						.createParallelGroup(
																								org.jdesktop.layout.GroupLayout.LEADING)
																						.add(
																								this.jLabel3)
																						.add(
																								this.jLabel4))))
										.addContainerGap(
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)).add(
								aboutDialogLayout.createSequentialGroup().add(
										82, 82, 82).add(this.jLabel7)
										.addContainerGap(59, Short.MAX_VALUE)));
		aboutDialogLayout
				.setVerticalGroup(aboutDialogLayout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								aboutDialogLayout
										.createSequentialGroup()
										.add(23, 23, 23)
										.add(this.jLabel1)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(this.jLabel6)
										.add(28, 28, 28)
										.add(this.jLabel7)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED,
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.add(this.jLabel3)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(this.jLabel4)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(this.jLabel5)
										.add(28, 28, 28)
										.add(
												aboutDialogLayout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.TRAILING)
														.add(this.jLabel2)
														.add(
																this.closeABoutButton))
										.add(28, 28, 28)));
		this.selectGroupMenuItem.setText("Connect to this newsgroup");
		this.selectGroupMenuItem
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						UNISoNFrame.this
								.selectGroupMenuItemActionPerformed(evt);
					}
				});

		this.newsgroupPopupMenu.add(this.selectGroupMenuItem);

		this.showAsCreatorReplierMenuItem
				.setText("Show Topic Creator vs Current Message Poster Matrix");
		this.showAsCreatorReplierMenuItem
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						UNISoNFrame.this
								.showAsCreatorReplierMenuItemActionPerformed(evt);
					}
				});

		this.newsgroupPopupMenu.add(this.showAsCreatorReplierMenuItem);

		this.showAsLastVsReplierMenuItem
				.setText("Show Last Message Poster vs Current Message Poster Matrix");
		this.showAsLastVsReplierMenuItem
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						UNISoNFrame.this
								.showAsLastVsReplierMenuItemActionPerformed(evt);
					}
				});

		this.newsgroupPopupMenu.add(this.showAsLastVsReplierMenuItem);

		this.showAllSoFarMenuItem
				.setText("Show all previous posters in topic matrix");
		this.showAllSoFarMenuItem
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						UNISoNFrame.this
								.showAllSoFarMenuItemActionPerformed(evt);
					}
				});

		this.newsgroupPopupMenu.add(this.showAllSoFarMenuItem);

		this
				.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		this.setTitle("UNISoN");
		this.topicMessageHierarchy
				.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
					public void valueChanged(
							final javax.swing.event.TreeSelectionEvent evt) {
						UNISoNFrame.this.topicMessageHierarchyValueChanged(evt);
					}
				});

		this.topicScrollPane.setViewportView(this.topicMessageHierarchy);

		this.subjectField.setEditable(false);

		this.subjectLabel.setText("Subject");

		this.senderLabel.setText("Sender");

		this.dateLabel.setText("Sent Date");

		this.senderField.setEditable(false);

		this.dateField.setEditable(false);

		this.newsGroupHiearchy
				.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
					public void valueChanged(
							final javax.swing.event.TreeSelectionEvent evt) {
						UNISoNFrame.this.newsGroupHiearchyValueChanged(evt);
					}
				});
		this.newsGroupHiearchy
				.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mousePressed(final java.awt.event.MouseEvent evt) {
						UNISoNFrame.this.newsGroupHiearchyMousePressed(evt);
					}
				});

		this.newsgroupScrollPane.setViewportView(this.newsGroupHiearchy);

		this.crossPostingsList.setModel(this
				.getCrossPostsModel(UNISoNController.getInstance()
						.getSelectedMessage()));
		this.crossPostingsList
				.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
					public void valueChanged(
							final javax.swing.event.ListSelectionEvent evt) {
						UNISoNFrame.this.crossPostingsListValueChanged(evt);
					}
				});

		this.crossPostingScrollPane.setViewportView(this.crossPostingsList);

		this.messageBodyTextArea.setColumns(20);
		this.messageBodyTextArea.setEditable(false);
		this.messageBodyTextArea.setRows(5);
		this.messageBodyScrollPane.setViewportView(this.messageBodyTextArea);

		this.hostLabel.setText("Host");

		this.hostChooser.setToolTipText("Select the host from the list");

		this.newsgroupLabel.setText("Newsgroup");

		this.newsgroupDropDown.setModel(this.getNewsGroupList());
		this.newsgroupDropDown
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						UNISoNFrame.this.newsgroupDropDownActionPerformed(evt);
					}
				});

		this.connectButton.setText("Connect ");
		this.connectButton.setToolTipText("Connect to the selected newsgroup");
		this.connectButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						UNISoNFrame.this.connectButtonActionPerformed(evt);
					}
				});

		this.downloadButton.setText("Download");
		this.downloadButton
				.setToolTipText("Download messages from chosen newsgroup");
		this.downloadButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						UNISoNFrame.this.downloadButtonActionPerformed(evt);
					}
				});

		this.pauseButton.setText("Pause");
		this.pauseButton.setToolTipText("Pause download");
		this.pauseButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				UNISoNFrame.this.pauseButtonActionPerformed(evt);
			}
		});

		this.crosspostedGroupsLabel.setText("Crossposted Newsgroups");

		this.timeLeftField.setEditable(false);

		this.cancelButton.setText("Cancel");
		this.cancelButton.setToolTipText("Cancel download");
		this.cancelButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						UNISoNFrame.this.cancelButtonActionPerformed(evt);
					}
				});

		this.timeLeftLabel.setText("Time Left: ");

		this.showUserButton.setText("Show User");
		this.showUserButton
				.setToolTipText("Show user details:  name, email, location,");
		this.showUserButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						UNISoNFrame.this.showUserButtonActionPerformed(evt);
					}
				});

		this.statsTabbedPane.setToolTipText("Shows the top posters");
		this.statsTabbedPane.setName("Top Posters");
		this.topGroupsTable.setModel(this.getTopGroupsModel());
		this.topGroupsScrollPane.setViewportView(this.topGroupsTable);

		this.statsTabbedPane.addTab("Top Groups", null,
				this.topGroupsScrollPane,
				"View the NewsGroups with most messages in the database");

		this.topPostersTable.setModel(this.getTopPostersModel());
		this.topPostersScrollPane.setViewportView(this.topPostersTable);

		this.statsTabbedPane.addTab("Top Posters", null,
				this.topPostersScrollPane,
				"View the Usenet posters with most messages in the database");

		this.statsTabbedPane.getAccessibleContext().setAccessibleName(
				"Top Posters");

		this.graphicPanel.setToolTipText("Show preview of Data");
		final org.jdesktop.layout.GroupLayout graphicPanelLayout = new org.jdesktop.layout.GroupLayout(
				this.graphicPanel);
		this.graphicPanel.setLayout(graphicPanelLayout);
		graphicPanelLayout.setHorizontalGroup(graphicPanelLayout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(0, 441, Short.MAX_VALUE));
		graphicPanelLayout.setVerticalGroup(graphicPanelLayout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(0, 223, Short.MAX_VALUE));
		this.resultsTabbedPane.addTab("Preview Pajek Graph", null,
				this.graphicPanel,
				"See a preview graph of the Selected Pajek Data");

		this.resultsTable.setModel(this.getPajekMatrixModel());
		this.resultsTableScrollPane.setViewportView(this.resultsTable);

		this.resultsTabbedPane.addTab("Preview Pajek File", null,
				this.resultsTableScrollPane,
				"Preview the data that will be used to create the Pajek file");

		this.refreshDataButton.setText("Refresh Data");
		this.refreshDataButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						UNISoNFrame.this.refreshDataButtonActionPerformed(evt);
					}
				});

		this.saveToPajekButton.setText("Save to Pajek");
		this.saveToPajekButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						UNISoNFrame.this.saveToPajekButtonActionPerformed(evt);
					}
				});

		this.dateFromLabel.setText("From:");

		this.dateToLabel.setText("To:");

		this.countriesLabel.setText("Countries:");

		this.postersLabel.setText("Posters:");

		this.messageArea.setEditable(false);

		this.matrixGroup.add(this.matrixFirstCurrent);
		this.matrixFirstCurrent.setText("Topic creator/Current");
		this.matrixFirstCurrent.setBorder(javax.swing.BorderFactory
				.createEmptyBorder(0, 0, 0, 0));
		this.matrixFirstCurrent.setMargin(new java.awt.Insets(0, 0, 0, 0));

		this.matrixGroup.add(this.matrixLastCurrent);
		this.matrixLastCurrent.setSelected(true);
		this.matrixLastCurrent.setText("Last message/Current");
		this.matrixLastCurrent.setBorder(javax.swing.BorderFactory
				.createEmptyBorder(0, 0, 0, 0));
		this.matrixLastCurrent.setMargin(new java.awt.Insets(0, 0, 0, 0));

		this.matrixGroup.add(this.matrixAllCurrent);
		this.matrixAllCurrent.setText("All previous/Current");
		this.matrixAllCurrent.setBorder(javax.swing.BorderFactory
				.createEmptyBorder(0, 0, 0, 0));
		this.matrixAllCurrent.setMargin(new java.awt.Insets(0, 0, 0, 0));

		this.toDateSpinner.setModel(this.getDateModel(false));

		this.fromDateSpinner.setModel(this.getDateModel(true));

		this.countryList.setModel(this.getListModel(this.getListVector(
				UNISoNController.LOCATION, "country")));
		this.jScrollPane1.setViewportView(this.countryList);

		this.postersList.setModel(this.getListModel(this
				.getListVector(UNISoNController.USENETUSER)));
		this.jScrollPane2.setViewportView(this.postersList);

		this.filterButton.setText("  Apply Filter  ");
		this.filterButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						UNISoNFrame.this.filterButtonActionPerformed(evt);
					}
				});

		this.fileMenu.setText("File");
		this.dbMagrMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_SPACE,
				java.awt.event.InputEvent.CTRL_MASK));
		this.dbMagrMenuItem.setText("Database Manager");
		this.dbMagrMenuItem
				.setToolTipText("This brings up a GUI to view and edit the database directly");
		this.dbMagrMenuItem
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						UNISoNFrame.this.dbMagrMenuItemActionPerformed(evt);
					}
				});

		this.fileMenu.add(this.dbMagrMenuItem);

		this.reloadDataMenuItem.setAccelerator(javax.swing.KeyStroke
				.getKeyStroke(java.awt.event.KeyEvent.VK_R,
						java.awt.event.InputEvent.CTRL_MASK));
		this.reloadDataMenuItem.setText("Refresh Data");
		this.reloadDataMenuItem
				.setToolTipText("This reloads the data from the database");
		this.reloadDataMenuItem
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						UNISoNFrame.this.reloadDataMenuItemActionPerformed(evt);
					}
				});

		this.fileMenu.add(this.reloadDataMenuItem);

		this.fileMenu.add(this.jSeparator2);

		this.exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_ESCAPE, 0));
		this.exitMenuItem.setText("Exit");
		this.exitMenuItem.setToolTipText("This closes the application");
		this.exitMenuItem
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						UNISoNFrame.this.exitMenuItemActionPerformed(evt);
					}
				});

		this.fileMenu.add(this.exitMenuItem);

		this.menuBar.add(this.fileMenu);

		this.helpMenu.setText("Help");
		this.deleteDBMenuItem.setAccelerator(javax.swing.KeyStroke
				.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE,
						java.awt.event.InputEvent.CTRL_MASK));
		this.deleteDBMenuItem.setText("Delete DB");
		this.deleteDBMenuItem
				.setToolTipText("Deletes ALL the data in the database");
		this.deleteDBMenuItem
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						UNISoNFrame.this.deleteDBMenuItemActionPerformed(evt);
					}
				});

		this.helpMenu.add(this.deleteDBMenuItem);

		this.helpMenu.add(this.jSeparator1);

		this.aboutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_SLASH,
				java.awt.event.InputEvent.SHIFT_MASK
						| java.awt.event.InputEvent.CTRL_MASK));
		this.aboutMenuItem.setText("About UNISoN");
		this.aboutMenuItem
				.setToolTipText("Show details of this program's author");
		this.aboutMenuItem
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						UNISoNFrame.this.aboutMenuItemActionPerformed(evt);
					}
				});

		this.helpMenu.add(this.aboutMenuItem);

		this.menuBar.add(this.helpMenu);

		this.setJMenuBar(this.menuBar);

		final org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
				this.getContentPane());
		this.getContentPane().setLayout(layout);
		layout
				.setHorizontalGroup(layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								org.jdesktop.layout.GroupLayout.TRAILING,
								layout
										.createSequentialGroup()
										.addContainerGap()
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.LEADING)
														.add(
																org.jdesktop.layout.GroupLayout.TRAILING,
																layout
																		.createSequentialGroup()
																		.add(
																				layout
																						.createParallelGroup(
																								org.jdesktop.layout.GroupLayout.TRAILING)
																						.add(
																								layout
																										.createSequentialGroup()
																										.add(
																												this.newsgroupLabel)
																										.add(
																												16,
																												16,
																												16))
																						.add(
																								layout
																										.createSequentialGroup()
																										.add(
																												this.hostLabel,
																												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																												67,
																												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																										.addPreferredGap(
																												org.jdesktop.layout.LayoutStyle.RELATED)))
																		.add(
																				layout
																						.createParallelGroup(
																								org.jdesktop.layout.GroupLayout.LEADING,
																								false)
																						.add(
																								this.newsgroupDropDown,
																								0,
																								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.add(
																								this.hostChooser,
																								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																								133,
																								Short.MAX_VALUE))
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED)
																		.add(
																				layout
																						.createParallelGroup(
																								org.jdesktop.layout.GroupLayout.LEADING,
																								false)
																						.add(
																								layout
																										.createSequentialGroup()
																										.add(
																												this.connectButton)
																										.addPreferredGap(
																												org.jdesktop.layout.LayoutStyle.RELATED)
																										.add(
																												this.downloadButton)
																										.addPreferredGap(
																												org.jdesktop.layout.LayoutStyle.RELATED)
																										.add(
																												this.pauseButton)
																										.add(
																												6,
																												6,
																												6)
																										.add(
																												this.cancelButton))
																						.add(
																								layout
																										.createSequentialGroup()
																										.add(
																												this.timeLeftLabel)
																										.addPreferredGap(
																												org.jdesktop.layout.LayoutStyle.RELATED)
																										.add(
																												this.timeLeftField,
																												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																												115,
																												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																										.addPreferredGap(
																												org.jdesktop.layout.LayoutStyle.RELATED)
																										.add(
																												this.downloadProgressBar,
																												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																												Short.MAX_VALUE)))
																		.add(
																				102,
																				102,
																				102))
														.add(
																layout
																		.createSequentialGroup()
																		.add(
																				layout
																						.createParallelGroup(
																								org.jdesktop.layout.GroupLayout.TRAILING,
																								false)
																						.add(
																								org.jdesktop.layout.GroupLayout.LEADING,
																								this.messageBodyScrollPane)
																						.add(
																								org.jdesktop.layout.GroupLayout.LEADING,
																								layout
																										.createSequentialGroup()
																										.add(
																												layout
																														.createParallelGroup(
																																org.jdesktop.layout.GroupLayout.TRAILING,
																																false)
																														.add(
																																this.newsgroupScrollPane)
																														.add(
																																org.jdesktop.layout.GroupLayout.LEADING,
																																layout
																																		.createSequentialGroup()
																																		.add(
																																				layout
																																						.createParallelGroup(
																																								org.jdesktop.layout.GroupLayout.LEADING)
																																						.add(
																																								this.crossPostingScrollPane,
																																								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																																								212,
																																								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																																						.add(
																																								this.crosspostedGroupsLabel))
																																		.addPreferredGap(
																																				org.jdesktop.layout.LayoutStyle.RELATED)
																																		.add(
																																				layout
																																						.createParallelGroup(
																																								org.jdesktop.layout.GroupLayout.LEADING)
																																						.add(
																																								this.senderLabel)
																																						.add(
																																								this.dateLabel)
																																						.add(
																																								this.subjectLabel))))
																										.addPreferredGap(
																												org.jdesktop.layout.LayoutStyle.RELATED)
																										.add(
																												layout
																														.createParallelGroup(
																																org.jdesktop.layout.GroupLayout.LEADING)
																														.add(
																																layout
																																		.createSequentialGroup()
																																		.add(
																																				layout
																																						.createParallelGroup(
																																								org.jdesktop.layout.GroupLayout.LEADING,
																																								false)
																																						.add(
																																								this.dateField)
																																						.add(
																																								this.senderField)
																																						.add(
																																								this.subjectField,
																																								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																																								205,
																																								Short.MAX_VALUE))
																																		.addPreferredGap(
																																				org.jdesktop.layout.LayoutStyle.RELATED)
																																		.add(
																																				layout
																																						.createParallelGroup(
																																								org.jdesktop.layout.GroupLayout.LEADING,
																																								false)
																																						.add(
																																								this.showUserButton,
																																								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																																								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																																								Short.MAX_VALUE)
																																						.add(
																																								this.saveToPajekButton,
																																								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																																								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																																								Short.MAX_VALUE)
																																						.add(
																																								this.refreshDataButton,
																																								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																																								123,
																																								Short.MAX_VALUE)))
																														.add(
																																this.topicScrollPane,
																																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																																333,
																																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED)))
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.LEADING)
														.add(
																layout
																		.createSequentialGroup()
																		.add(
																				layout
																						.createParallelGroup(
																								org.jdesktop.layout.GroupLayout.LEADING)
																						.add(
																								this.jScrollPane1,
																								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																								196,
																								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																						.add(
																								layout
																										.createSequentialGroup()
																										.add(
																												layout
																														.createParallelGroup(
																																org.jdesktop.layout.GroupLayout.LEADING)
																														.add(
																																this.dateFromLabel)
																														.add(
																																this.dateToLabel))
																										.addPreferredGap(
																												org.jdesktop.layout.LayoutStyle.RELATED)
																										.add(
																												layout
																														.createParallelGroup(
																																org.jdesktop.layout.GroupLayout.LEADING)
																														.add(
																																this.toDateSpinner,
																																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																																80,
																																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																														.add(
																																this.fromDateSpinner,
																																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																																82,
																																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
																		.add(
																				14,
																				14,
																				14)
																		.add(
																				layout
																						.createParallelGroup(
																								org.jdesktop.layout.GroupLayout.LEADING)
																						.add(
																								this.matrixAllCurrent)
																						.add(
																								this.jScrollPane2,
																								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																								271,
																								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																						.add(
																								this.matrixLastCurrent)
																						.add(
																								this.matrixFirstCurrent)))
														.add(
																layout
																		.createSequentialGroup()
																		.add(
																				this.countriesLabel)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED)
																		.add(
																				this.filterButton)
																		.add(
																				20,
																				20,
																				20)
																		.add(
																				this.postersLabel))
														.add(
																layout
																		.createSequentialGroup()
																		.add(
																				20,
																				20,
																				20)
																		.add(
																				layout
																						.createParallelGroup(
																								org.jdesktop.layout.GroupLayout.TRAILING,
																								false)
																						.add(
																								layout
																										.createSequentialGroup()
																										.add(
																												12,
																												12,
																												12)
																										.add(
																												this.resultsTabbedPane,
																												0,
																												0,
																												Short.MAX_VALUE))
																						.add(
																								org.jdesktop.layout.GroupLayout.LEADING,
																								this.statsTabbedPane,
																								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
										.add(20, 20, 20))
						.add(
								layout
										.createSequentialGroup()
										.add(
												this.messageArea,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
												1163,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
										.addContainerGap(34, Short.MAX_VALUE)));

		layout.linkSize(new java.awt.Component[] { this.newsgroupScrollPane,
				this.topicScrollPane },
				org.jdesktop.layout.GroupLayout.HORIZONTAL);

		layout
				.setVerticalGroup(layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								layout
										.createSequentialGroup()
										.addContainerGap()
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.LEADING)
														.add(
																layout
																		.createSequentialGroup()
																		.add(
																				22,
																				22,
																				22)
																		.add(
																				layout
																						.createParallelGroup(
																								org.jdesktop.layout.GroupLayout.LEADING)
																						.add(
																								layout
																										.createSequentialGroup()
																										.add(
																												layout
																														.createParallelGroup(
																																org.jdesktop.layout.GroupLayout.BASELINE)
																														.add(
																																this.hostLabel,
																																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																																15,
																																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																														.add(
																																this.hostChooser,
																																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
																										.add(
																												11,
																												11,
																												11)
																										.add(
																												layout
																														.createParallelGroup(
																																org.jdesktop.layout.GroupLayout.BASELINE)
																														.add(
																																this.newsgroupLabel)
																														.add(
																																this.newsgroupDropDown,
																																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
																						.add(
																								layout
																										.createSequentialGroup()
																										.add(
																												layout
																														.createParallelGroup(
																																org.jdesktop.layout.GroupLayout.BASELINE)
																														.add(
																																this.connectButton)
																														.add(
																																this.downloadButton)
																														.add(
																																this.pauseButton)
																														.add(
																																this.cancelButton))
																										.add(
																												11,
																												11,
																												11)
																										.add(
																												layout
																														.createParallelGroup(
																																org.jdesktop.layout.GroupLayout.LEADING)
																														.add(
																																org.jdesktop.layout.GroupLayout.TRAILING,
																																layout
																																		.createParallelGroup(
																																				org.jdesktop.layout.GroupLayout.BASELINE)
																																		.add(
																																				this.timeLeftLabel,
																																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																																				19,
																																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																																		.add(
																																				this.timeLeftField,
																																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																																				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
																														.add(
																																org.jdesktop.layout.GroupLayout.TRAILING,
																																this.downloadProgressBar,
																																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
														.add(
																layout
																		.createSequentialGroup()
																		.add(
																				layout
																						.createParallelGroup(
																								org.jdesktop.layout.GroupLayout.BASELINE)
																						.add(
																								this.fromDateSpinner,
																								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																						.add(
																								this.dateFromLabel)
																						.add(
																								this.matrixAllCurrent))
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED)
																		.add(
																				layout
																						.createParallelGroup(
																								org.jdesktop.layout.GroupLayout.BASELINE)
																						.add(
																								this.toDateSpinner,
																								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																						.add(
																								this.dateToLabel)
																						.add(
																								this.matrixFirstCurrent,
																								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																								15,
																								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED)
																		.add(
																				this.matrixLastCurrent)))
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.LEADING)
														.add(
																layout
																		.createSequentialGroup()
																		.add(
																				layout
																						.createParallelGroup(
																								org.jdesktop.layout.GroupLayout.TRAILING)
																						.add(
																								layout
																										.createSequentialGroup()
																										.add(
																												14,
																												14,
																												14)
																										.add(
																												this.newsgroupScrollPane,
																												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																												303,
																												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
																						.add(
																								org.jdesktop.layout.GroupLayout.LEADING,
																								layout
																										.createSequentialGroup()
																										.addPreferredGap(
																												org.jdesktop.layout.LayoutStyle.RELATED)
																										.add(
																												layout
																														.createParallelGroup(
																																org.jdesktop.layout.GroupLayout.LEADING)
																														.add(
																																this.filterButton)
																														.add(
																																this.topicScrollPane,
																																0,
																																0,
																																Short.MAX_VALUE))))
																		.add(
																				6,
																				6,
																				6)
																		.add(
																				layout
																						.createParallelGroup(
																								org.jdesktop.layout.GroupLayout.TRAILING)
																						.add(
																								layout
																										.createSequentialGroup()
																										.add(
																												this.crosspostedGroupsLabel)
																										.addPreferredGap(
																												org.jdesktop.layout.LayoutStyle.RELATED)
																										.add(
																												this.crossPostingScrollPane,
																												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																												58,
																												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
																						.add(
																								layout
																										.createSequentialGroup()
																										.add(
																												layout
																														.createParallelGroup(
																																org.jdesktop.layout.GroupLayout.BASELINE)
																														.add(
																																this.subjectLabel)
																														.add(
																																this.subjectField,
																																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																														.add(
																																this.showUserButton))
																										.addPreferredGap(
																												org.jdesktop.layout.LayoutStyle.RELATED)
																										.add(
																												layout
																														.createParallelGroup(
																																org.jdesktop.layout.GroupLayout.BASELINE)
																														.add(
																																this.senderLabel)
																														.add(
																																this.senderField,
																																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																																19,
																																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																														.add(
																																this.refreshDataButton))
																										.addPreferredGap(
																												org.jdesktop.layout.LayoutStyle.RELATED)
																										.add(
																												layout
																														.createParallelGroup(
																																org.jdesktop.layout.GroupLayout.BASELINE)
																														.add(
																																this.dateLabel)
																														.add(
																																this.dateField,
																																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																														.add(
																																this.saveToPajekButton))))
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED))
														.add(
																layout
																		.createSequentialGroup()
																		.add(
																				23,
																				23,
																				23)
																		.add(
																				layout
																						.createParallelGroup(
																								org.jdesktop.layout.GroupLayout.BASELINE)
																						.add(
																								this.postersLabel)
																						.add(
																								this.countriesLabel))
																		.add(
																				16,
																				16,
																				16)
																		.add(
																				layout
																						.createParallelGroup(
																								org.jdesktop.layout.GroupLayout.TRAILING)
																						.add(
																								this.jScrollPane2,
																								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																						.add(
																								this.jScrollPane1,
																								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
																		.add(
																				21,
																				21,
																				21)
																		.add(
																				this.statsTabbedPane,
																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																				202,
																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																		.add(
																				8,
																				8,
																				8)))
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.LEADING)
														.add(
																this.resultsTabbedPane,
																0, 0,
																Short.MAX_VALUE)
														.add(
																this.messageBodyScrollPane,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																250,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(
												this.messageArea,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
										.addContainerGap()));

		layout.linkSize(new java.awt.Component[] { this.newsgroupScrollPane,
				this.topicScrollPane },
				org.jdesktop.layout.GroupLayout.VERTICAL);

		layout.linkSize(new java.awt.Component[] { this.fromDateSpinner,
				this.toDateSpinner }, org.jdesktop.layout.GroupLayout.VERTICAL);

		layout.linkSize(new java.awt.Component[] { this.jScrollPane1,
				this.jScrollPane2 }, org.jdesktop.layout.GroupLayout.VERTICAL);

		this.pack();
	}// </editor-fold>//GEN-END:initComponents

	private void iterateCollection(final Iterator<?> iter2,
			final TreeNode msgRoot) {
		while (iter2.hasNext()) {
			final Object object = iter2.next();
			this.addChildNode(msgRoot, object);
		}
	}

	private void newsgroupDropDownActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_newsgroupDropDownActionPerformed
		// TODO
	}// GEN-LAST:event_newsgroupDropDownActionPerformed

	private void newsGroupHiearchyMousePressed(
			final java.awt.event.MouseEvent evt) {// GEN-FIRST:event_newsGroupHiearchyMousePressed
		if (evt.isPopupTrigger()) {
			this.newsgroupPopupMenu.show(evt.getComponent(), evt.getX(), evt
					.getY());
		}

	}// GEN-LAST:event_newsGroupHiearchyMousePressed

	private void newsGroupHiearchyValueChanged(
			final javax.swing.event.TreeSelectionEvent evt) {// GEN-FIRST:event_newsGroupHiearchyValueChanged
		final TreePath tp = evt.getPath();
		final TreeNode root = (TreeNode) tp.getLastPathComponent();

		// as root is not a newsgroup
		if (root.getUserObject() instanceof NewsGroup) {
			UNISoNController.getInstance().setSelectedNewsgroup(
					(NewsGroup) root.getUserObject());
		} else {
			final NewsGroup dummy = null;
			UNISoNController.getInstance().setSelectedNewsgroup(dummy);
		}

		this.expandNode(root);
	}// GEN-LAST:event_newsGroupHiearchyValueChanged

	public void notifySelectedMessageObservers() {
		this.refreshMessagePane();
	}

	public void notifySelectedNewsGroupObservers() {
		this.refreshTopicHierarchy();
		if (null != UNISoNController.getInstance().getSelectedNewsgroup()) {
			this.newsgroupDropDown.setSelectedItem(UNISoNController
					.getInstance().getSelectedNewsgroup().getFullName());
		}
	}

	private void pauseButtonActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_pauseButtonActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_pauseButtonActionPerformed

	private void refreshCountryFilter() {
		final DefaultListModel model = (DefaultListModel) this.countryList
				.getModel();
		model.removeAllElements();
		for (final Object country : this.getListVector(
				UNISoNController.LOCATION, "country")) {

			model.addElement(country);
			if ((null != UNISoNController.getInstance().getCountriesFilter())
					&& UNISoNController.getInstance().getCountriesFilter()
							.contains(country.toString())) {
				this.countryList.setSelectedValue(country, true);
			}
		}
	}

	private void refreshDataButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_refreshDataButtonActionPerformed
		UNISoNController.getInstance().refreshDataFromDatabase();
	}// GEN-LAST:event_refreshDataButtonActionPerformed

	private void refreshDateFilters() {
		this.toDateSpinner.setModel(this.getDateModel(true));
		this.fromDateSpinner.setModel(this.getDateModel(false));
	}

	/**
	 * Key method - this refreshes all the GUI components with fresh data from
	 * the database
	 */
	public void refreshGUIData() {
		this.refreshTopPostersTable();

		this.refreshMessagePane();
		this.refreshTopicHierarchy();
		this.refreshNewsGroupHierarchy();

		this.refreshPajekMatrixTable();
		this.refreshCountryFilter();
		this.refreshDateFilters();
		this.refreshPosterFilter();
	}

	public void refreshMessagePane() {
		final Message message = UNISoNController.getInstance()
				.getSelectedMessage();

		if (null != message) {
			final DefaultListModel model = this.getCrossPostsModel(message);
			this.crossPostingsList.setModel(model);

			this.subjectField.setText(message.getSubject());
			this.senderField.setText(message.getPoster().getEmail());
			this.dateField.setText(new SimpleDateFormat("dd MMM yyyy hh:mm")
					.format(message.getDateCreated()));
			try {
				this.messageBodyTextArea.setText(StringUtils.decompress(message
						.getMessageBody()));
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected void refreshNewsGroupHierarchy() {
		this.newsgroupTreeRoot.removeAllChildren();

		final ListIterator<NewsGroup> iter = UNISoNController.getInstance()
				.getTopNewsGroups().listIterator();
		while (iter.hasNext()) {
			final NewsGroup group = iter.next();
			this.addChildNode(this.newsgroupTreeRoot, group);
		}
		// This actually refreshes the tree
		((DefaultTreeModel) this.newsGroupHiearchy.getModel()).reload();
	}

	private void refreshPajekMatrixTable() {
		final DefaultTableModel model = (DefaultTableModel) this.resultsTable
				.getModel();
		model
				.setDataVector(this.getLatestPajekMatrixVector(),
						this.pajekHeader);
	}

	private void refreshPosterFilter() {
		this.postersList.setModel(this.getListModel(this
				.getListVector(UNISoNController.USENETUSER)));
	}

	/**
	 * 
	 * 
	 */
	private void refreshTopicHierarchy() {
		// TODO reinstate that topics reflect the highlighted newsgroup

		this.topicRoot.removeAllChildren();

		final Vector<NewsGroup> chosenGroup = new Vector<NewsGroup>();

		if (null != UNISoNController.getInstance().getSelectedNewsgroup()) {
			this.topicRoot.setName(UNISoNController.getInstance()
					.getSelectedNewsgroup().getFullName());
			chosenGroup.add(UNISoNController.getInstance()
					.getSelectedNewsgroup());
		} else {
			this.topicRoot.setName("All Topics");
		}

		UNISoNFrame.logger.warn("refreshTopicHierarchy " + chosenGroup);
		final ListIterator<Topic> iter = UNISoNController.getInstance()
				.getSelectedTopicsFromDB(chosenGroup);

		while (iter.hasNext()) {
			final Topic topic = iter.next();
			final int lastIndex = topic.getSubject().length();

			this.addChildNode(this.topicRoot, topic, topic.getSubject()
					.substring(0, lastIndex));
		}

		// This actually refreshes the tree
		((DefaultTreeModel) this.topicMessageHierarchy.getModel()).reload();
	}

	private void refreshTopPostersTable() {

	}

	private void reloadDataMenuItemActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_reloadDataMenuItemActionPerformed
		UNISoNController.getInstance().refreshDataFromDatabase();
	}// GEN-LAST:event_reloadDataMenuItemActionPerformed

	/**
	 * 
	 * @param evt
	 */
	private void saveToPajekButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveToPajekButtonActionPerformed
		final JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		chooser.setFileFilter(new SimpleFileFilter(new String[] { ".net" },
				UNISoNFrame.PAJEK_NETWORK_FILE_DESCRIPTION));
		// chooser
		// .setFileFilter(new SimpleFileFilter(
		// new String[] { ".ntf" },
		// NETMINER_DATA_FILE_DESCRIPTION));
		final int option = chooser.showSaveDialog(this);

		if (option == JFileChooser.APPROVE_OPTION) {
			final File saveFile = chooser.getSelectedFile();
			final String fileName = saveFile.getAbsolutePath();
			final String fileType = chooser.getFileFilter().getDescription();
			PajekNetworkFile file = new PajekNetworkFile();
			file.createDirectedLinks(((DefaultTableModel) this.resultsTable
					.getModel()).getDataVector());
			file.saveToFile(fileName);
		} else {
			this.showStatus("You cancelled.");
		}

	}// GEN-LAST:event_saveToPajekButtonActionPerformed

	private void selectGroupMenuItemActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_selectGroupMenuItemActionPerformed
		final NewsGroup group = this.getCurrentNewsGroupNode();
		String name = "";
		if (null != group) {
			name = group.getFullName();
		}
		UNISoNController.getInstance().connectToNewsGroup(name);
	}// GEN-LAST:event_selectGroupMenuItemActionPerformed

	/**
	 * 
	 * @param connectButtonState
	 * @param downloadButtonState
	 * @param pauseButtonState
	 * @param cancelButtonState
	 */
	public void setButtonState(final boolean connectButtonState,
			final boolean downloadButtonState, final boolean pauseButtonState,
			final boolean cancelButtonState) {
		this.downloadButton.setEnabled(downloadButtonState);
		this.cancelButton.setEnabled(cancelButtonState);
		this.pauseButton.setEnabled(pauseButtonState);
		this.connectButton.setEnabled(connectButtonState);

	}

	/**
	 * 
	 * @param group
	 */
	public void setSelectedNewsgroup(final NewsGroup group) {
		if (null != group) {
			this.notifySelectedNewsGroupObservers();
		}
	}

	public void showAlert(final String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	/**
	 * This is the action taken when the menu item is selected
	 * 
	 * @param evt
	 */
	private void showAllSoFarMenuItemActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_showAllSoFarMenuItemActionPerformed

		UNISoNController.getInstance().setSelectedNewsgroup(
				this.getCurrentNewsGroupNode());
		UNISoNController.getInstance().setMatrixType(MatrixType.REPLY_TO_ALL);
		this.refreshPajekMatrixTable();
	}// GEN-LAST:event_showAllSoFarMenuItemActionPerformed

	/**
	 * This is the action taken when the menu item is selected
	 * 
	 * @param evt
	 */
	private void showAsCreatorReplierMenuItemActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_showAsCreatorReplierMenuItemActionPerformed
		UNISoNController.getInstance().setSelectedNewsgroup(
				this.getCurrentNewsGroupNode());
		UNISoNController.getInstance().setMatrixType(MatrixType.REPLY_TO_FIRST);
		this.refreshPajekMatrixTable();
	}// GEN-LAST:event_showAsCreatorReplierMenuItemActionPerformed

	/**
	 * This is the action taken when the menu item is selected
	 * 
	 * @param evt
	 */
	private void showAsLastVsReplierMenuItemActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_showAsLastVsReplierMenuItemActionPerformed
		UNISoNController.getInstance().setSelectedNewsgroup(
				this.getCurrentNewsGroupNode());
		UNISoNController.getInstance().setMatrixType(MatrixType.REPLY_TO_LAST);
		this.refreshPajekMatrixTable();
	}// GEN-LAST:event_showAsLastVsReplierMenuItemActionPerformed

	public void showErrorMessage(final String message) {
		JOptionPane.showMessageDialog(this, message);
		this.messageArea.setText(message);
	}

	public void showStatus(final String message) {
		this.messageArea.setText(message);
	}

	private void showUserButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_showUserButtonActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_showUserButtonActionPerformed

	private void topicMessageHierarchyValueChanged(
			final javax.swing.event.TreeSelectionEvent evt) {// GEN-FIRST:event_topicMessageHierarchyValueChanged
		final TreePath tp = evt.getPath();
		final TreeNode root = (TreeNode) tp.getLastPathComponent();

		final Object datanode = root.getUserObject();
		if (datanode instanceof Message) {
			final Message msg = (Message) datanode;
			UNISoNController.getInstance().setSelectedMessage(msg);
			this.notifySelectedMessageObservers();
		} else {
			this.expandNode(root);
		}
	}// GEN-LAST:event_topicMessageHierarchyValueChanged
}
