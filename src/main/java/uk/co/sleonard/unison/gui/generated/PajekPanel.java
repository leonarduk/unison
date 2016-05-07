/*
 * PajekPanel.java
 *
 * Created on 03 December 2007, 08:48
 */

package uk.co.sleonard.unison.gui.generated;

import java.awt.FileDialog;
import java.io.File;
import java.io.FilenameFilter;
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

import uk.co.sleonard.unison.datahandling.DataQuery;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.gui.GraphPreviewPanel;
import uk.co.sleonard.unison.gui.UNISoNController;
import uk.co.sleonard.unison.gui.UNISoNException;
import uk.co.sleonard.unison.output.ExportToCSV;
import uk.co.sleonard.unison.output.PajekNetworkFile;
import uk.co.sleonard.unison.utils.StringUtils;

/**
 * 
 * @author Steve
 */
public class PajekPanel extends javax.swing.JPanel implements Observer {

	private static final String PAJEK_NETWORK_FILE_SUFFIX = ".net";

	/**
	 * 
	 */
	private static final long serialVersionUID = 84102596787648747L;

	private PajekNetworkFile pajekFile;

	private JFrame frame;

	@Override
	public void update(Observable observable, Object arg1) {
		if (observable instanceof UNISoNController) {
			// UNISoNController controller = (UNISoNController) observable;
			try {
				refreshPajekMatrixTable();
			} catch (UNISoNException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws UNISoNException {
		JFrame frame = new JFrame();
		UNISoNController.create(frame);

		PajekPanel panel = new PajekPanel(frame);
		frame.add(panel);
		panel.setVisible(true);
		frame.setVisible(true);
		frame.setSize(frame.getPreferredSize());

	}

	private ExportToCSV csvExporter = new ExportToCSV();

	private Session session;

	/**
	 * Creates new form PajekPanel
	 * 
	 * @throws UNISoNException
	 */
	public PajekPanel(JFrame frame) {
		pajekHeader = new Vector<String>(Arrays.asList(new String[] { "Subject", "Date", "FROM", "TO" }));
		try {

			this.frame = frame;
			session = UNISoNController.getInstance().helper().getHibernateSession();
			initComponents();

			// FIXME disable non-working parts
			incMissingCheck.setVisible(false);

			// TODO create file to put the no messages on vector
			// TODO create file to put the location as cluster (use country)

			resultsMatrixTable.setEnabled(false);
			filePreviewArea.setEditable(false);
			previousRadio.setSelected(true);
			refreshPajekMatrixTable();
		} catch (UNISoNException e) {
			UNISoNController.getInstance().showAlert("Error: " + e.getMessage());
		}

	}

	/**
	 * 
	 * @param rowIndex
	 * @param currentMsg
	 * @param originalMsg
	 * @return
	 */
	private Vector<String> createNewRow(final int rowIndex, final Message currentMsg, final Message originalMsg) {
		String toText = "";

		if (null != originalMsg) {
			toText = originalMsg.getPoster().getName() + " [" + originalMsg.getPoster().getEmail() + "]";
		}
		final Vector<String> row = new Vector<String>();
		row.add(currentMsg.getSubject());
		row.add("" + currentMsg.getDateCreated());

		UsenetUser poster = currentMsg.getPoster();

		String fromText = "MISSING";
		if (null != poster) {
			fromText = poster.getName() + " [" + poster.getEmail() + "]";
		}
		row.add(fromText);
		row.add(toText);
		return row;
	}

	@SuppressWarnings("unchecked")
	private Vector<Vector<String>> getLatestPajekMatrixVector() {
		Vector<Vector<String>> tableData;
		final List<Message> messages = UNISoNController.getInstance().getMessagesFilter();
		HashMap<String, Message> msgMap = new HashMap<String, Message>();

		// Load ALL messages into map so can get complete referemce
		Vector<Message> allMessages = DataQuery.getInstance().getMessages(null, null, session, null, null, false, null,
				null);
		for (Object next : allMessages) {
			Message msg = (Message) next;
			msgMap.put(msg.getUsenetMessageID(), msg);
		}

		tableData = new Vector<Vector<String>>();
		int rowIndex = 1;
		for (final ListIterator<Message> msgIter = messages.listIterator(); msgIter.hasNext();) {
			final Message next = msgIter.next();
			Message lastMsg = null;

			List<String> refMsgs = StringUtils.convertStringToList(next.getReferencedMessages(), " ");
			int size;
			try {
				size = refMsgs.size();
			} catch (final Exception e) {
				e.printStackTrace();
				size = 0;
			}
			if ((null != refMsgs) && (size > 0)) {
				List<String> msgList = StringUtils.convertStringToList(next.getReferencedMessages(), " ");

				if (this.allRadio.isSelected()) {

					final ListIterator<String> iter = msgList.listIterator();
					while (iter.hasNext()) {
						Message msg = getReferencedMessage(msgMap, iter.next(), next);
						tableData.add(this.createNewRow(rowIndex++, next, msg));
					}
				} else {
					if (this.previousRadio.isSelected()) {
						lastMsg = getReferencedMessage(msgMap, refMsgs.get(refMsgs.size() - 1), next);
					} else if (this.creatorRadio.isSelected()) {
						lastMsg = getReferencedMessage(msgMap, refMsgs.get(0), next);
					}
					tableData.add(this.createNewRow(rowIndex++, next, lastMsg));
				}
			} else {
				tableData.add(this.createNewRow(rowIndex++, next, null));
			}

		}
		return tableData;
	}

	public Message getReferencedMessage(HashMap<String, Message> msgMap, String key, Message currentMessage) {
		Message message = msgMap.get(key);

		// create dummy message for missing ones
		if (null != key && key.contains("@") && null == message) {
			// This message and poster is not saved to the database
			UsenetUser poster = new UsenetUser("MISSING", key, "UNKNOWN", null, null);
			message = new Message(null, key, currentMessage.getSubject(), poster, currentMessage.getTopic(), null,
					currentMessage.getReferencedMessages().replaceAll(key, ""), null);
		}
		return message;
	}

	@SuppressWarnings("unchecked")
	private void refreshPajekMatrixTable() throws UNISoNException {
		final DefaultTableModel model = (DefaultTableModel) this.resultsMatrixTable.getModel();
		model.setDataVector(this.getLatestPajekMatrixVector(), this.pajekHeader);

		// filePreviewArea
		pajekFile = new PajekNetworkFile();
		pajekFile.createDirectedLinks(((DefaultTableModel) this.resultsMatrixTable.getModel()).getDataVector());
		graphScrollPane.removeAll();

		GraphPreviewPanel previewPanel = pajekFile.getPreviewPanel();
		graphScrollPane.add(previewPanel);
		previewPanel.repaint();
		graphScrollPane.setSize(graphScrollPane.getMaximumSize());

		// clear down for next set of data
		filePreviewArea.setText("");
		OutputStream output = new OutputStream() {

			@Override
			public void write(int b) throws IOException {
				char letter = (char) b;
				filePreviewArea.append("" + letter);
			}

		};
		PrintStream stream = new PrintStream(output, true);
		pajekFile.writeData(stream);
		previewPanel.repaint();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// ">//GEN-BEGIN:initComponents
	private void initComponents() {
		matrixTypeGroup = new javax.swing.ButtonGroup();
		incMissingCheck = new javax.swing.JCheckBox();
		creatorRadio = new javax.swing.JRadioButton();
		previousRadio = new javax.swing.JRadioButton();
		allRadio = new javax.swing.JRadioButton();
		previewButton = new javax.swing.JButton();
		saveButton = new javax.swing.JButton();
		pajekTabPane = new javax.swing.JTabbedPane();
		graphScrollPane = new javax.swing.JScrollPane();
		matrixScrollPane = new javax.swing.JScrollPane();
		resultsMatrixTable = new javax.swing.JTable();
		filePreviewScrollPane = new javax.swing.JScrollPane();
		filePreviewArea = new javax.swing.JTextArea();
		csvButton = new javax.swing.JButton();

		incMissingCheck.setText("Include Missing Messages");
		incMissingCheck
				.setToolTipText("Not all referenced messages can be downloaded. This will show placeholders for them.");
		incMissingCheck.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		incMissingCheck.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		incMissingCheck.setMargin(new java.awt.Insets(0, 0, 0, 0));
		incMissingCheck.addAncestorListener(new javax.swing.event.AncestorListener() {
			@Override
			public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
				incMissingCheckAncestorMoved(evt);
			}

			@Override
			public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
				incMissingCheckAncestorAdded(evt);
			}

			@Override
			public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
			}
		});

		matrixTypeGroup.add(creatorRadio);
		creatorRadio.setText("Thread creator vs Current Poster");
		creatorRadio.setToolTipText(
				"Thsi takes the details of the message's poster along with that of the person who started the thread.");
		creatorRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		creatorRadio.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		creatorRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));

		matrixTypeGroup.add(previousRadio);
		previousRadio.setSelected(true);
		previousRadio.setText("Previous poster vs current");
		previousRadio.setToolTipText(
				"This takes the current poster and that of the last message. i.e. the one they actually replied to.");
		previousRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		previousRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));

		matrixTypeGroup.add(allRadio);
		allRadio.setText("All thread posters to current");
		allRadio.setToolTipText("This uses all the authors in the thread up to the point of this message.");
		allRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		allRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));

		previewButton.setText("Refresh");
		previewButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				previewButtonActionPerformed(evt);
			}
		});

		saveButton.setText("Save To Pajek");
		saveButton.setToolTipText("Save to Pajek Network file (as in Pajek File Preview tab)");
		saveButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveButtonActionPerformed(evt);
			}
		});

		pajekTabPane.addTab("Graph", graphScrollPane);

		resultsMatrixTable
				.setModel(new javax.swing.table.DefaultTableModel(
						new Object[][] { { null, null, null, null }, { null, null, null, null },
								{ null, null, null, null }, { null, null, null, null } },
						new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
		matrixScrollPane.setViewportView(resultsMatrixTable);

		pajekTabPane.addTab("Matrix", matrixScrollPane);

		filePreviewArea.setColumns(20);
		filePreviewArea.setRows(5);
		filePreviewScrollPane.setViewportView(filePreviewArea);

		pajekTabPane.addTab("Pajek File Preview", filePreviewScrollPane);

		pajekTabPane.getAccessibleContext().setAccessibleName("Graph");

		csvButton.setText("Save to CSV");
		csvButton.setToolTipText("Save to Comma-separated  file (as in Matrixtab).  This can be read into Excel.");
		csvButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				csvButtonActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(incMissingCheck)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
										.addComponent(creatorRadio, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(allRadio, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(previousRadio)))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(previewButton)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(saveButton)
						.addComponent(csvButton)).addContainerGap(87, Short.MAX_VALUE))
				.addComponent(pajekTabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE));

		layout.linkSize(javax.swing.SwingConstants.HORIZONTAL,
				new java.awt.Component[] { csvButton, previewButton, saveButton });

		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup()
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(previewButton).addComponent(saveButton))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
										csvButton))
								.addGroup(layout.createSequentialGroup().addComponent(incMissingCheck)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(creatorRadio)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(previousRadio)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(allRadio)))
						.addGap(18, 18, 18)
						.addComponent(pajekTabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)));
	}// </editor-fold>//GEN-END:initComponents

	private void csvButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_csvButtonActionPerformed
		try {
			csvExporter.exportTableToCSV(resultsMatrixTable, pajekHeader);
		} catch (UNISoNException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
	}// GEN-LAST:event_csvButtonActionPerformed

	private void incMissingCheckAncestorMoved(javax.swing.event.AncestorEvent evt) {// GEN-FIRST:event_incMissingCheckAncestorMoved
		// TODO add your handling code here:
	}// GEN-LAST:event_incMissingCheckAncestorMoved

	private void incMissingCheckAncestorAdded(javax.swing.event.AncestorEvent evt) {// GEN-FIRST:event_incMissingCheckAncestorAdded
		// TODO add your handling code here:
	}// GEN-LAST:event_incMissingCheckAncestorAdded

	private void previewButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_previewButtonActionPerformed
		UNISoNController.getInstance().notifyObservers();
	}// GEN-LAST:event_previewButtonActionPerformed

	@SuppressWarnings({ "unchecked", "deprecation" })
	private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveButtonActionPerformed
		FileDialog file = new FileDialog(frame, "Save Pajek Network File", FileDialog.SAVE);
		String initialValue = "*" + PAJEK_NETWORK_FILE_SUFFIX;
		file.setFile(initialValue); // set initial filename filter
		file.setFilenameFilter(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (name.endsWith(PAJEK_NETWORK_FILE_SUFFIX)) {
					return true;
				}
				return false;
			}

		});
		file.show(); // Blocks
		String curFile = null;
		if ((curFile = file.getFile()) != null && !curFile.equals(initialValue)) {

			if (!curFile.endsWith(PAJEK_NETWORK_FILE_SUFFIX)) {
				curFile += PAJEK_NETWORK_FILE_SUFFIX;
			}
			String filename = file.getDirectory() + curFile;
			final File saveFile = new File(filename); // chooser.getSelectedFile();
			final String fileName = saveFile.getAbsolutePath();
			pajekFile.saveToFile(fileName);
			this.showStatus("Saved file " + filename);

		} else {
			this.showStatus("You cancelled.");
		}

	}// GEN-LAST:event_saveButtonActionPerformed

	private void showStatus(String string) {
		UNISoNController.getInstance().showStatus(string);
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JRadioButton allRadio;

	private javax.swing.JRadioButton creatorRadio;

	private javax.swing.JButton csvButton;

	private javax.swing.JTextArea filePreviewArea;

	private javax.swing.JScrollPane filePreviewScrollPane;

	private javax.swing.JScrollPane graphScrollPane;

	private javax.swing.JCheckBox incMissingCheck;

	private javax.swing.JScrollPane matrixScrollPane;

	private javax.swing.ButtonGroup matrixTypeGroup;

	private javax.swing.JTabbedPane pajekTabPane;

	private javax.swing.JButton previewButton;

	private javax.swing.JRadioButton previousRadio;

	private javax.swing.JTable resultsMatrixTable;

	private javax.swing.JButton saveButton;

	// End of variables declaration//GEN-END:variables

	private final Vector<String> pajekHeader;

}
