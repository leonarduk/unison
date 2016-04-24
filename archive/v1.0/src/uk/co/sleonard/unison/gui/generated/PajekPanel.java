/*
 * PajekPanel.java
 *
 * Created on 03 December 2007, 08:48
 */

package uk.co.sleonard.unison.gui.generated;

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

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.gui.GraphPreviewPanel;
import uk.co.sleonard.unison.gui.SimpleFileFilter;
import uk.co.sleonard.unison.gui.UNISoNController;
import uk.co.sleonard.unison.gui.UNISoNException;
import uk.co.sleonard.unison.output.PajekNetworkFile;
import uk.co.sleonard.unison.utils.StringUtils;

/**
 * 
 * @author Steve
 */
public class PajekPanel extends javax.swing.JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 84102596787648747L;
	private PajekNetworkFile pajekFile;

	@Override
	public void update(Observable observable, Object arg1) {
		if (observable instanceof UNISoNController) {
			UNISoNController controller = (UNISoNController) observable;
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

		PajekPanel panel = new PajekPanel();
		frame.add(panel);
		panel.setVisible(true);
		frame.setVisible(true);
		frame.setSize(frame.getPreferredSize());

	}

	/**
	 * Creates new form PajekPanel
	 * 
	 * @throws UNISoNException
	 */
	public PajekPanel()  {
		pajekHeader = new Vector<String>(Arrays.asList(new String[] { "row",
				"FROM", "TO" }));

		initComponents();

		//FIXME disable non-working parts
		incMissingCheck.setVisible(false);
		
		// TODO create file to put the no messages on vector
		// TODO create file to put the location as cluster (use country)

		resultsMatrixTable.setEnabled(false);
		filePreviewArea.setEditable(false);
		previousRadio.setSelected(true);
		try {
			refreshPajekMatrixTable();
		} catch (UNISoNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	/**
	 * 
	 * @param rowIndex
	 * @param currentMsg
	 * @param originalMsg
	 * @return
	 */
	private Vector<String> createNewRow(final int rowIndex,
			final Message currentMsg, final Message originalMsg) {
		String toText = "";

		if (null != originalMsg) {
			toText = originalMsg.getPoster().getName() + " ["
					+ originalMsg.getPoster().getEmail() + "]";
		}
		final Vector<String> row = new Vector<String>();
		row.add("" + rowIndex);
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
		final String sql = " from Message where 1=1 ";
		Vector<Vector<String>> tableData;
		final List<Message> messages = (List<Message>) UNISoNController
				.getInstance().helper().runQuery(
						UNISoNController.getInstance().helper()
								.getHibernateSession().createQuery(sql));
		HashMap<String, Message> msgMap = new HashMap<String, Message>();
		for (Object next : messages) {
			Message msg = (Message) next;
			msgMap.put(msg.getUsenetMessageID(), msg);
		}

		tableData = new Vector<Vector<String>>();
		int rowIndex = 1;
		for (final ListIterator<Message> msgIter = messages.listIterator(); msgIter
				.hasNext();) {
			final Message next = msgIter.next();
			Message lastMsg = null;

			List<String> refMsgs = StringUtils.convertStringToList(next
					.getReferencedMessages(), " ");
			int size;
			try {
				size = refMsgs.size();
			} catch (final Exception e) {
				e.printStackTrace();
				size = 0;
			}
			if ((null != refMsgs) && (size > 0)) {
				List<String> msgList = StringUtils.convertStringToList(next
						.getReferencedMessages(), " ");

				if (this.allRadio.isSelected()) {

					for (final ListIterator<String> iter = msgList
							.listIterator(); iter.hasNext();) {
						Message msg = msgMap.get(iter.next());
						tableData.add(this.createNewRow(rowIndex++, next, msg));
					}
				} else {
					if (this.creatorRadio.isSelected()) {
						lastMsg = msgMap.get(refMsgs.get(refMsgs.size() - 1));
					} else if (this.previousRadio.isSelected()) {
						lastMsg = msgMap.get(refMsgs.get(0));
					}

				}
				tableData.add(this.createNewRow(rowIndex++, next, lastMsg));
			} else {
				tableData.add(this.createNewRow(rowIndex++, next, null));
			}

		}
		return tableData;
	}

	@SuppressWarnings("unchecked")
	private void refreshPajekMatrixTable() throws UNISoNException {
		final DefaultTableModel model = (DefaultTableModel) this.resultsMatrixTable
				.getModel();
		model
				.setDataVector(this.getLatestPajekMatrixVector(),
						this.pajekHeader);

		// filePreviewArea
		pajekFile = new PajekNetworkFile();
		pajekFile
				.createDirectedLinks(((DefaultTableModel) this.resultsMatrixTable
						.getModel()).getDataVector());
		graphScrollPane.removeAll();

		GraphPreviewPanel previewPanel = pajekFile.getPreviewPanel();
		graphScrollPane.add(previewPanel);
		previewPanel.repaint();
		graphScrollPane.setSize(graphScrollPane.getMaximumSize());
		
		//clear down for next set of data
		filePreviewArea.removeAll();
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

		incMissingCheck.setText("Include Missing Messages");
		incMissingCheck
				.setToolTipText("Not all referenced messages can be downloaded. This will show placeholders for them.");
		incMissingCheck.setBorder(javax.swing.BorderFactory.createEmptyBorder(
				0, 0, 0, 0));
		incMissingCheck
				.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		incMissingCheck.setMargin(new java.awt.Insets(0, 0, 0, 0));
		incMissingCheck
				.addAncestorListener(new javax.swing.event.AncestorListener() {
					public void ancestorMoved(
							javax.swing.event.AncestorEvent evt) {
						incMissingCheckAncestorMoved(evt);
					}

					public void ancestorAdded(
							javax.swing.event.AncestorEvent evt) {
						incMissingCheckAncestorAdded(evt);
					}

					public void ancestorRemoved(
							javax.swing.event.AncestorEvent evt) {
					}
				});

		matrixTypeGroup.add(creatorRadio);
		creatorRadio.setText("Thread creator vs Current Poster");
		creatorRadio
				.setToolTipText("Thsi takes the details of the message's poster along with that of the person who started the thread.");
		creatorRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,
				0, 0, 0));
		creatorRadio
				.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		creatorRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));

		matrixTypeGroup.add(previousRadio);
		previousRadio.setSelected(true);
		previousRadio.setText("Previous poster vs current");
		previousRadio
				.setToolTipText("This takes the current poster and that of the last message. i.e. the one they actually replied to.");
		previousRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,
				0, 0, 0));
		previousRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));

		matrixTypeGroup.add(allRadio);
		allRadio.setText("All thread posters to current");
		allRadio
				.setToolTipText("This uses all the authors in the thread up to the point of this message.");
		allRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0,
				0));
		allRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));

		previewButton.setText("Preview");
		previewButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				previewButtonActionPerformed(evt);
			}
		});

		saveButton.setText("Save To File");
		saveButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveButtonActionPerformed(evt);
			}
		});

		pajekTabPane.addTab("Graph", graphScrollPane);

		resultsMatrixTable.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] { { null, null, null, null },
						{ null, null, null, null }, { null, null, null, null },
						{ null, null, null, null } }, new String[] { "Title 1",
						"Title 2", "Title 3", "Title 4" }));
		matrixScrollPane.setViewportView(resultsMatrixTable);

		pajekTabPane.addTab("Matrix", matrixScrollPane);

		filePreviewArea.setColumns(20);
		filePreviewArea.setRows(5);
		filePreviewScrollPane.setViewportView(filePreviewArea);

		pajekTabPane.addTab("Pajek File Preview", filePreviewScrollPane);

		pajekTabPane.getAccessibleContext().setAccessibleName("Graph");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout
				.setHorizontalGroup(layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								layout
										.createSequentialGroup()
										.addGroup(
												layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																incMissingCheck)
														.addGroup(
																layout
																		.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING,
																				false)
																		.addComponent(
																				creatorRadio,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)
																		.addComponent(
																				allRadio,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)
																		.addComponent(
																				previousRadio)))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																saveButton)
														.addComponent(
																previewButton))
										.addContainerGap(204, Short.MAX_VALUE))
						.addComponent(pajekTabPane,
								javax.swing.GroupLayout.DEFAULT_SIZE, 478,
								Short.MAX_VALUE));

		layout.linkSize(javax.swing.SwingConstants.HORIZONTAL,
				new java.awt.Component[] { previewButton, saveButton });

		layout
				.setVerticalGroup(layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																layout
																		.createSequentialGroup()
																		.addComponent(
																				previewButton)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				saveButton))
														.addGroup(
																layout
																		.createSequentialGroup()
																		.addComponent(
																				incMissingCheck)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				creatorRadio)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				previousRadio)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				allRadio)))
										.addGap(18, 18, 18)
										.addComponent(
												pajekTabPane,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												339, Short.MAX_VALUE)));
	}// </editor-fold>//GEN-END:initComponents

	private void incMissingCheckAncestorMoved(
			javax.swing.event.AncestorEvent evt) {// GEN-FIRST:event_incMissingCheckAncestorMoved
		// TODO add your handling code here:
	}// GEN-LAST:event_incMissingCheckAncestorMoved

	private void incMissingCheckAncestorAdded(
			javax.swing.event.AncestorEvent evt) {// GEN-FIRST:event_incMissingCheckAncestorAdded
		// TODO add your handling code here:
	}// GEN-LAST:event_incMissingCheckAncestorAdded

	private void previewButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_previewButtonActionPerformed
		try {
			refreshPajekMatrixTable();
		} catch (UNISoNException e) {
			e.printStackTrace();
		}
	}// GEN-LAST:event_previewButtonActionPerformed

	private static final String PAJEK_NETWORK_FILE_DESCRIPTION = "Pajek Network File (.net)";

	@SuppressWarnings("unchecked")
	private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveButtonActionPerformed
		final JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		chooser.setFileFilter(new SimpleFileFilter(new String[] { ".net" },
				PAJEK_NETWORK_FILE_DESCRIPTION));
		// chooser
		// .setFileFilter(new SimpleFileFilter(
		// new String[] { ".ntf" },
		// NETMINER_DATA_FILE_DESCRIPTION));
		final int option = chooser.showSaveDialog(this);

		if (option == JFileChooser.APPROVE_OPTION) {
			final File saveFile = chooser.getSelectedFile();
			final String fileName = saveFile.getAbsolutePath();
			PajekNetworkFile file = new PajekNetworkFile();
			file
					.createDirectedLinks(((DefaultTableModel) this.resultsMatrixTable
							.getModel()).getDataVector());
			file.saveToFile(fileName);
		} else {
			this.showStatus("You cancelled.");
		}

	}// GEN-LAST:event_saveButtonActionPerformed

	private void showStatus(String string) {
		// TODO Auto-generated method stub

	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JRadioButton allRadio;
	private javax.swing.JRadioButton creatorRadio;
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
