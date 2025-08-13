/*
 * PajekPanel.java
 *
 * Created on 03 December 2007, 08:48
 */

package uk.co.sleonard.unison.gui.generated;

import java.awt.FileDialog;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.sleonard.unison.UNISoNController;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DataQuery;
import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.UNISoNDatabase;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.gui.GraphPreviewPanel;
import uk.co.sleonard.unison.input.DataHibernatorPoolImpl;
import uk.co.sleonard.unison.output.ExportToCSV;
import uk.co.sleonard.unison.output.PajekNetworkFile;
import uk.co.sleonard.unison.utils.StringUtils;

/**
 * The Class PajekPanel.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
class PajekPanel extends javax.swing.JPanel implements Observer {

        /** The Constant PAJEK_NETWORK_FILE_SUFFIX. */
        private static final String PAJEK_NETWORK_FILE_SUFFIX = ".net";

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 84102596787648747L;

        /** Logger for this class. */
        private static final Logger LOG = LoggerFactory.getLogger(PajekPanel.class);

        /** The pajek file. */
        private PajekNetworkFile pajekFile;

	/** The frame. */
	private JFrame frame;

	/** The csv exporter. */
	private final ExportToCSV csvExporter = new ExportToCSV();

	/** The session. */
	private Session session;

	/** The all radio. */
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JRadioButton allRadio;

	/** The creator radio. */
	private javax.swing.JRadioButton creatorRadio;

	/** The csv button. */
	private javax.swing.JButton csvButton;

	/** The file preview area. */
	private javax.swing.JTextArea filePreviewArea;

	/** The file preview scroll pane. */
	private javax.swing.JScrollPane filePreviewScrollPane;

	/** The graph scroll pane. */
	private javax.swing.JScrollPane graphScrollPane;


        /** The matrix scroll pane. */
        private javax.swing.JScrollPane matrixScrollPane;

	/** The matrix type group. */
	private javax.swing.ButtonGroup matrixTypeGroup;

	/** The pajek tab pane. */
	private javax.swing.JTabbedPane pajekTabPane;

	/** The preview button. */
	private javax.swing.JButton previewButton;

	/** The previous radio. */
	private javax.swing.JRadioButton previousRadio;

	/** The results matrix table. */
	private javax.swing.JTable resultsMatrixTable;

	/** The save button. */
	private javax.swing.JButton saveButton;

	/** The pajek header. */
	private final Vector<String> pajekHeader;

	private UNISoNController controller;

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	public static void main(final String[] args) throws UNISoNException {
		final JFrame frame = new JFrame();
		UNISoNController.create(frame, new DataHibernatorPoolImpl());

		final PajekPanel panel = new PajekPanel(frame);
		frame.add(panel);
		panel.setVisible(true);
		frame.setVisible(true);
		frame.setSize(frame.getPreferredSize());

	}

	/**
	 * Creates new form PajekPanel.
	 *
	 * @param frame
	 *            the frame
	 */
	PajekPanel(final JFrame frame) {
		this.pajekHeader = new Vector<>(
		        Arrays.asList(new String[] { "Subject", "Date", "FROM", "TO" }));
		try {

			this.frame = frame;
			this.controller = UNISoNController.getInstance();
			this.session = this.controller.helper().getHibernateSession();
                        this.initComponents();

                        this.resultsMatrixTable.setEnabled(false);
                        this.getFilePreviewArea().setEditable(false);
			this.previousRadio.setSelected(true);
			this.refreshPajekMatrixTable();
		}
		catch (final UNISoNException e) {
			this.controller.getGui().showAlert("Error: " + e.getMessage());
		}

	}

	/**
	 * Creates the new row.
	 *
	 * @param rowIndex
	 *            the row index
	 * @param currentMsg
	 *            the current msg
	 * @param originalMsg
	 *            the original msg
	 * @return the vector
	 */
	private Vector<String> createNewRow(final int rowIndex, final Message currentMsg,
	        final Message originalMsg) {
		String toText = "";

		if (null != originalMsg) {
			toText = originalMsg.getPoster().getName() + " [" + originalMsg.getPoster().getEmail()
			        + "]";
		}
		final Vector<String> row = new Vector<>();
		row.add(currentMsg.getSubject());
		row.add("" + currentMsg.getDateCreated());

		final UsenetUser poster = currentMsg.getPoster();

		String fromText = "MISSING";
		if (null != poster) {
			fromText = poster.getName() + " [" + poster.getEmail() + "]";
		}
		row.add(fromText);
		row.add(toText);
		return row;
	}

	/**
	 * Csv button action performed.
	 *
	 * @param evt
	 *            the evt
	 */
        private void csvButtonActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_csvButtonActionPerformed
                try {
                        this.csvExporter.exportTableToCSV(this.resultsMatrixTable, this.pajekHeader);
                }
                catch (final UNISoNException e) {
                        LOG.error("Error exporting CSV", e);
                        this.controller.getGui().showAlert("Error: " + e.getMessage());
                }
        }// GEN-LAST:event_csvButtonActionPerformed

	public javax.swing.JTextArea getFilePreviewArea() {
		return this.filePreviewArea;
	}

	/**
	 * Gets the latest pajek matrix vector.
	 *
	 * @return the latest pajek matrix vector
	 */
	private Vector<Vector<String>> getLatestPajekMatrixVector() {
		Vector<Vector<String>> tableData;
		final List<Message> messages = UNISoNController.getInstance().getFilter()
		        .getMessagesFilter();
		final HashMap<String, Message> msgMap = new HashMap<>();

		final HibernateHelper helper = UNISoNController.getInstance().getHelper();
		// Load ALL messages into map so can get complete referemce
		final Vector<Message> allMessages = new DataQuery(helper).getMessages(null, null,
		        this.session, null, null, false, null, null);
		for (final Object next : allMessages) {
			final Message msg = (Message) next;
			msgMap.put(msg.getUsenetMessageID(), msg);
		}

		tableData = new Vector<>();
		int rowIndex = 1;
		for (final ListIterator<Message> msgIter = messages.listIterator(); msgIter.hasNext();) {
			final Message next = msgIter.next();
			Message lastMsg = null;

			final List<String> refMsgs = StringUtils
			        .convertStringToList(next.getReferencedMessages(), " ");
			int size;
			try {
				size = refMsgs.size();
			}
			catch (final Exception e) {
				e.printStackTrace();
				size = 0;
			}
			if ((null != refMsgs) && (size > 0)) {
				final List<String> msgList = StringUtils
				        .convertStringToList(next.getReferencedMessages(), " ");

				if (this.allRadio.isSelected()) {

					final ListIterator<String> iter = msgList.listIterator();
					while (iter.hasNext()) {
						final Message msg = this.getReferencedMessage(msgMap, iter.next(), next);
						tableData.add(this.createNewRow(rowIndex++, next, msg));
					}
				}
				else {
					if (this.previousRadio.isSelected()) {
						lastMsg = this.getReferencedMessage(msgMap, refMsgs.get(refMsgs.size() - 1),
						        next);
					}
					else if (this.creatorRadio.isSelected()) {
						lastMsg = this.getReferencedMessage(msgMap, refMsgs.get(0), next);
					}
					tableData.add(this.createNewRow(rowIndex++, next, lastMsg));
				}
			}
			else {
				tableData.add(this.createNewRow(rowIndex++, next, null));
			}

		}
		return tableData;
	}

	/**
	 * Gets the referenced message.
	 *
	 * @param msgMap
	 *            the msg map
	 * @param key
	 *            the key
	 * @param currentMessage
	 *            the current message
	 * @return the referenced message
	 */
	private Message getReferencedMessage(final HashMap<String, Message> msgMap, final String key,
	        final Message currentMessage) {
		Message message = msgMap.get(key);

		// create dummy message for missing ones
		if ((null != key) && key.contains("@") && (null == message)) {
			// This message and poster is not saved to the database
			final UsenetUser poster = new UsenetUser("MISSING", key, "UNKNOWN", null, null);
			message = new Message(null, key, currentMessage.getSubject(), poster,
			        currentMessage.getTopic(), null,
			        currentMessage.getReferencedMessages().replaceAll(key, ""), null);
		}
		return message;
	}


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
	// ">//GEN-BEGIN:initComponents
	private void initComponents() {
		this.matrixTypeGroup = new javax.swing.ButtonGroup();
                this.creatorRadio = new javax.swing.JRadioButton();
		this.previousRadio = new javax.swing.JRadioButton();
		this.allRadio = new javax.swing.JRadioButton();
		this.previewButton = new javax.swing.JButton();
		this.saveButton = new javax.swing.JButton();
		this.pajekTabPane = new javax.swing.JTabbedPane();
		this.graphScrollPane = new javax.swing.JScrollPane();
		this.matrixScrollPane = new javax.swing.JScrollPane();
		this.resultsMatrixTable = new javax.swing.JTable();
		this.filePreviewScrollPane = new javax.swing.JScrollPane();
		this.setFilePreviewArea(new javax.swing.JTextArea());
		this.csvButton = new javax.swing.JButton();

                this.matrixTypeGroup.add(this.creatorRadio);
		this.creatorRadio.setText("Thread creator vs Current Poster");
		this.creatorRadio.setToolTipText(
		        "Thsi takes the details of the message's poster along with that of the person who started the thread.");
		this.creatorRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		this.creatorRadio.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		this.creatorRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));

		this.matrixTypeGroup.add(this.previousRadio);
		this.previousRadio.setSelected(true);
		this.previousRadio.setText("Previous poster vs current");
		this.previousRadio.setToolTipText(
		        "This takes the current poster and that of the last message. i.e. the one they actually replied to.");
		this.previousRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		this.previousRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));

		this.matrixTypeGroup.add(this.allRadio);
		this.allRadio.setText("All thread posters to current");
		this.allRadio.setToolTipText(
		        "This uses all the authors in the thread up to the point of this message.");
		this.allRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		this.allRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));

		this.previewButton.setText("Refresh");
		this.previewButton
		        .addActionListener(evt -> PajekPanel.this.previewButtonActionPerformed(evt));

		this.saveButton.setText("Save To Pajek");
		this.saveButton.setToolTipText("Save to Pajek Network file (as in Pajek File Preview tab)");
		this.saveButton.addActionListener(evt -> PajekPanel.this.saveButtonActionPerformed(evt));

		this.pajekTabPane.addTab("Graph", this.graphScrollPane);

		this.resultsMatrixTable.setModel(new javax.swing.table.DefaultTableModel(
		        new Object[][] { { null, null, null, null }, { null, null, null, null },
		                { null, null, null, null }, { null, null, null, null } },
		        new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
		this.matrixScrollPane.setViewportView(this.resultsMatrixTable);

		this.pajekTabPane.addTab("Matrix", this.matrixScrollPane);

		this.getFilePreviewArea().setColumns(20);
		this.getFilePreviewArea().setRows(5);
		this.filePreviewScrollPane.setViewportView(this.getFilePreviewArea());

		this.pajekTabPane.addTab("Pajek File Preview", this.filePreviewScrollPane);

		this.pajekTabPane.getAccessibleContext().setAccessibleName("Graph");

		this.csvButton.setText("Save to CSV");
		this.csvButton.setToolTipText(
		        "Save to Comma-separated  file (as in Matrixtab).  This can be read into Excel.");
		this.csvButton.addActionListener(evt -> PajekPanel.this.csvButtonActionPerformed(evt));

		final javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout
		        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		        .addGroup(layout.createSequentialGroup().addGroup(layout
		                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                false)
                                        .addComponent(this.creatorRadio,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(this.allRadio, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(this.previousRadio)))
		                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                .addComponent(this.previewButton)
		                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                .addGroup(layout
		                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                        .addComponent(this.saveButton).addComponent(this.csvButton))
		                .addContainerGap(87, Short.MAX_VALUE))
		        .addComponent(this.pajekTabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 478,
		                Short.MAX_VALUE));

		layout.linkSize(javax.swing.SwingConstants.HORIZONTAL,
		        new java.awt.Component[] { this.csvButton, this.previewButton, this.saveButton });

		layout.setVerticalGroup(
		        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                .addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout
		                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                        .addGroup(layout.createSequentialGroup()
		                                .addGroup(layout
		                                        .createParallelGroup(
		                                                javax.swing.GroupLayout.Alignment.BASELINE)
		                                        .addComponent(this.previewButton)
		                                        .addComponent(this.saveButton))
		                                .addPreferredGap(
		                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                .addComponent(this.csvButton))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(this.creatorRadio)
                                                .addPreferredGap(
                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(this.previousRadio)
                                                .addPreferredGap(
                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(this.allRadio)))
		                        .addGap(18, 18, 18).addComponent(this.pajekTabPane,
		                                javax.swing.GroupLayout.DEFAULT_SIZE, 339,
		                                Short.MAX_VALUE)));
	}// </editor-fold>//GEN-END:initComponents

	/**
	 * Preview button action performed.
	 *
	 * @param evt
	 *            the evt
	 */
	private void previewButtonActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_previewButtonActionPerformed
		UNISoNController.getInstance().getDatabase().notifyObservers();
	}// GEN-LAST:event_previewButtonActionPerformed

	/**
	 * Refresh pajek matrix table.
	 *
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	private void refreshPajekMatrixTable() throws UNISoNException {
		final DefaultTableModel model = (DefaultTableModel) this.resultsMatrixTable.getModel();
		model.setDataVector(this.getLatestPajekMatrixVector(), this.pajekHeader);

		// filePreviewArea
		this.pajekFile = new PajekNetworkFile();
		this.pajekFile.createDirectedLinks(
		        ((DefaultTableModel) this.resultsMatrixTable.getModel()).getDataVector());
		this.graphScrollPane.removeAll();

		final GraphPreviewPanel previewPanel = this.pajekFile.getPreviewPanel();
		this.graphScrollPane.add(previewPanel);
		previewPanel.repaint();
		this.graphScrollPane.setSize(this.graphScrollPane.getMaximumSize());

		// clear down for next set of data
		this.getFilePreviewArea().setText("");
		final OutputStream output = new OutputStream() {

			@Override
			public void write(final int b) throws IOException {
				final char letter = (char) b;
				PajekPanel.this.getFilePreviewArea().append("" + letter);
			}

		};
		final PrintStream stream = new PrintStream(output, true);
		this.pajekFile.writeData(stream);
		previewPanel.repaint();
	}

	/**
	 * Save button action performed.
	 *
	 * @param evt
	 *            the evt
	 */
	@SuppressWarnings({ "deprecation" })
	private void saveButtonActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveButtonActionPerformed
		final FileDialog file = new FileDialog(this.frame, "Save Pajek Network File",
		        FileDialog.SAVE);
		final String initialValue = "*" + PajekPanel.PAJEK_NETWORK_FILE_SUFFIX;
		file.setFile(initialValue); // set initial filename filter
		file.setFilenameFilter((dir, name) -> {
			if (name.endsWith(PajekPanel.PAJEK_NETWORK_FILE_SUFFIX)) {
				return true;
			}
			return false;
		});
		file.show(); // Blocks
		String curFile = null;
		curFile = file.getFile();
		if ((curFile != null) && !curFile.equals(initialValue)) {

			if (!curFile.endsWith(PajekPanel.PAJEK_NETWORK_FILE_SUFFIX)) {
				curFile += PajekPanel.PAJEK_NETWORK_FILE_SUFFIX;
			}
			final String filename = file.getDirectory() + curFile;
			final File saveFile = new File(filename); // chooser.getSelectedFile();
			final String fileName = saveFile.getAbsolutePath();
			this.pajekFile.saveToFile(fileName);
			this.showStatus("Saved file " + filename);

		}
		else {
			this.showStatus("You cancelled.");
		}

	}// GEN-LAST:event_saveButtonActionPerformed

	// End of variables declaration//GEN-END:variables

	public void setFilePreviewArea(final javax.swing.JTextArea filePreviewArea) {
		this.filePreviewArea = filePreviewArea;
	}

	/**
	 * Show status.
	 *
	 * @param string
	 *            the string
	 */
	private void showStatus(final String string) {
		this.controller.getGui().showStatus(string);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(final Observable observable, final Object arg1) {
		if (observable instanceof UNISoNDatabase) {
			// UNISoNController controller = (UNISoNController) observable;
			try {
				this.refreshPajekMatrixTable();
			}
			catch (final UNISoNException e) {
				e.printStackTrace();
			}
		}
	}

}
