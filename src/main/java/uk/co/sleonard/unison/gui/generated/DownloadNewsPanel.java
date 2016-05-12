/*
 * DownloadNewsPanel.java
 *
 * Created on 27 November 2007, 08:58
 */

package uk.co.sleonard.unison.gui.generated;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.gui.UNISoNController;
import uk.co.sleonard.unison.gui.UNISoNException;
import uk.co.sleonard.unison.gui.UNISoNLogger;
import uk.co.sleonard.unison.input.DataHibernatorWorker;
import uk.co.sleonard.unison.input.HeaderDownloadWorker;
import uk.co.sleonard.unison.input.NNTPNewsGroup;
import uk.co.sleonard.unison.utils.HttpDateObject;
import uk.co.sleonard.unison.utils.StringUtils;

/**
 * The Class DownloadNewsPanel.
 *
 * @author Steve
 */
public class DownloadNewsPanel extends javax.swing.JPanel implements
		UNISoNLogger, Observer {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6581138636992116397L;
    
    /** The available newsgroups. */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<NNTPNewsGroup> availableNewsgroups;
    
    /** The cancel button. */
    private javax.swing.JButton cancelButton;
    
    /** The download button. */
    private javax.swing.JButton downloadButton;
    
    /** The download progress bar. */
    private javax.swing.JProgressBar downloadProgressBar;
    
    /** The download progress label. */
    private javax.swing.JLabel downloadProgressLabel;
    
    /** The find button. */
    private javax.swing.JButton findButton;
    
    /** The from date field. */
    private javax.swing.JTextField fromDateField;
    
    /** The from date label. */
    private javax.swing.JLabel fromDateLabel;
    
    /** The get location check. */
    private javax.swing.JCheckBox getLocationCheck;
    
    /** The get text check. */
    private javax.swing.JCheckBox getTextCheck;
    
    /** The host combo. */
    private javax.swing.JComboBox hostCombo;
    
    /** The host label. */
    private javax.swing.JLabel hostLabel;
    
    /** The j scroll pane1. */
    private javax.swing.JScrollPane jScrollPane1;
    
    /** The j scroll pane3. */
    private javax.swing.JScrollPane jScrollPane3;
    
    /** The newsgroup field. */
    private javax.swing.JTextField newsgroupField;
    
    /** The newsgroup label. */
    private javax.swing.JLabel newsgroupLabel;
    
    /** The notes area. */
    private javax.swing.JTextArea notesArea;
    
    /** The pause button. */
    private javax.swing.JButton pauseButton;
    
    /** The to date field. */
    private javax.swing.JTextField toDateField;
    
    /** The to date label. */
    private javax.swing.JLabel toDateLabel;
    // End of variables declaration//GEN-END:variables
	/**
     *  Creates new form DownloadNewsPanel.
     */
	public DownloadNewsPanel() {
		this.controller = UNISoNController.getInstance();

		this.initComponents();

		controller.getHeaderDownloader().addObserver(this);

		controller.setNntpHost(hostCombo.getSelectedItem().toString());
		downloadEnabled(false);
		getLocationCheck.setToolTipText("Downloads Location & Crossposts");
		getLocationCheck.setText("Get Extras");

		// Need to set these manually to false as previous method sets them to
		// true
		this.cancelButton.setEnabled(false);
		this.pauseButton.setEnabled(false);

		DataHibernatorWorker.setLogger(this);
		controller.setDownloadPanel(this);
		
		// FIXME - disable these until they work
		getTextCheck.setVisible(false);
		downloadProgressBar.setVisible(false);
		downloadProgressLabel.setVisible(false);
	}

	/**
	 * Download enabled.
	 *
	 * @param enabled the enabled
	 */
	private void downloadEnabled(boolean enabled) {
		this.downloadButton.setEnabled(enabled);
		fromDateLabel.setEnabled(enabled);
		toDateLabel.setEnabled(enabled);
		fromDateField.setEnabled(enabled);
		toDateField.setEnabled(enabled);
		getTextCheck.setEnabled(enabled);
		getLocationCheck.setEnabled(enabled);

		// these are off when download on & vice versa
		this.cancelButton.setEnabled(!enabled);
		this.pauseButton.setEnabled(!enabled);
	}

	/** The available groups. */
	private Set<NNTPNewsGroup> availableGroups;

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        newsgroupLabel = new javax.swing.JLabel();
        newsgroupField = new javax.swing.JTextField();
        downloadProgressBar = new javax.swing.JProgressBar();
        downloadProgressLabel = new javax.swing.JLabel();
        findButton = new javax.swing.JButton();
        fromDateLabel = new javax.swing.JLabel();
        toDateLabel = new javax.swing.JLabel();
        toDateField = new javax.swing.JTextField();
        fromDateField = new javax.swing.JTextField();
        downloadButton = new javax.swing.JButton();
        pauseButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        notesArea = new javax.swing.JTextArea();
        hostLabel = new javax.swing.JLabel();
        getTextCheck = new javax.swing.JCheckBox();
        getLocationCheck = new javax.swing.JCheckBox();
        hostCombo = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        availableNewsgroups = new javax.swing.JList<NNTPNewsGroup>();

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
			public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });

        newsgroupLabel.setText("Newsgroup ");
        newsgroupLabel.setToolTipText("Type in full or part name (use * for wild character, e.g. *senior* for all groups with \"senior\" in the name)");

        newsgroupField.setToolTipText("Type in full or part name (use * for wild character, e.g. *senior* for all groups with \"senior\" in the name)");

        downloadProgressLabel.setText("Download Status");

        findButton.setText("Find groups");
        findButton.setToolTipText("Enter name or part name (* for wild chararcter) the n click here");
        findButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                findButtonActionPerformed(evt);
            }
        });

        fromDateLabel.setText("Date from");
        fromDateLabel.setToolTipText("In YYYYMMDD format eg 20071201");

        toDateLabel.setText("Date To");
        toDateLabel.setToolTipText("In YYYYMMDD format eg 20071201");

        toDateField.setToolTipText("In YYYYMMDD format eg 20071201");

        fromDateField.setToolTipText("In YYYYMMDD format eg 20071201");

        downloadButton.setText("Download");
        downloadButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadButtonActionPerformed(evt);
            }
        });

        pauseButton.setText("Pause");
        pauseButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        notesArea.setColumns(20);
        notesArea.setRows(5);
        jScrollPane3.setViewportView(notesArea);

        hostLabel.setText(" Server");
        hostLabel.setToolTipText("Look at http://freeusenetnews.com/newspage.html?sortby=articles for other hosts if these are broken. Can enter name here.");

        getTextCheck.setText("Get Text");
        getTextCheck.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        getTextCheck.setMargin(new java.awt.Insets(0, 0, 0, 0));

        getLocationCheck.setText("Get Location");
        getLocationCheck.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        getLocationCheck.setMargin(new java.awt.Insets(0, 0, 0, 0));

        hostCombo.setEditable(true);
        hostCombo.setModel(new javax.swing.DefaultComboBoxModel(StringUtils.loadServerList()));
        hostCombo.setToolTipText("Look at http://freeusenetnews.com/newspage.html?sortby=articles for other hosts if these are broken. Can enter name here.");

        availableNewsgroups.setModel(getAvailableGroupsModel());
        availableNewsgroups.setToolTipText("The number in brackets is an estimate (provided by the server) which is likely to be higher than actual number of messages as many messages are deleted.");
        availableNewsgroups.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
			public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                availableNewsgroupsValueChanged(evt);
            }
        });

        jScrollPane1.setViewportView(availableNewsgroups);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                    .addComponent(downloadProgressLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(newsgroupLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(newsgroupField, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(hostLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                        .addComponent(hostCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(downloadProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(downloadButton)
                            .addComponent(fromDateLabel)
                            .addComponent(fromDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(toDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(toDateLabel)
                            .addComponent(cancelButton)
                            .addComponent(pauseButton))
                        .addComponent(findButton))
                    .addComponent(getTextCheck)
                    .addComponent(getLocationCheck))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, downloadButton, findButton, fromDateField, pauseButton, toDateField});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(55, 55, 55)
                                .addComponent(getTextCheck)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(getLocationCheck)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fromDateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fromDateField, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(toDateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(toDateField, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                                .addComponent(downloadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pauseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cancelButton))
                            .addComponent(findButton)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(46, 46, 46)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(hostLabel)
                                    .addComponent(hostCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(newsgroupLabel)
                                    .addComponent(newsgroupField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downloadProgressLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downloadProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

		/**
		 * Form mouse pressed.
		 *
		 * @param evt the evt
		 */
		private void formMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_formMousePressed
		// TODO add your handling code here:
	}// GEN-LAST:event_formMousePressed

	/**
	 * Available newsgroups value changed.
	 *
	 * @param evt the evt
	 */
	private void availableNewsgroupsValueChanged(
			javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_availableNewsgroupsValueChanged

	}// GEN-LAST:event_availableNewsgroupsValueChanged

	/**
	 * Gets the available groups model.
	 *
	 * @return the available groups model
	 */
	private ListModel<NNTPNewsGroup> getAvailableGroupsModel() {
		final DefaultListModel<NNTPNewsGroup> model = new DefaultListModel<NNTPNewsGroup>();

		if (null != availableGroups) {
			for (final Iterator<NNTPNewsGroup> iter = availableGroups
					.iterator(); iter.hasNext();) {
				final NNTPNewsGroup next = iter.next();
				model.addElement(next);
			}
		}
		return model;
	}

	/**
	 * The main method.
	 *
	 * @param args            the command line arguments
	 */
	public static void main(final String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				UNISoNTabbedFrame frame = new UNISoNTabbedFrame();
				frame.setVisible(true);
				DownloadNewsPanel panel = new DownloadNewsPanel();
				frame.add(panel);
				panel.setVisible(true);
			}
		});
	}

	/**
	 * Cancel button action performed.
	 *
	 * @param evt the evt
	 */
	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cancelButtonActionPerformed
		controller.getHeaderDownloader().fullstop();
	}// GEN-LAST:event_cancelButtonActionPerformed

	/**
	 * Pause button action performed.
	 *
	 * @param evt the evt
	 */
	private void pauseButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_pauseButtonActionPerformed
		HeaderDownloadWorker headerDownloader = controller
				.getHeaderDownloader();
		if (headerDownloader.isDownloading()) {
			headerDownloader.pause();
			this.pauseButton.setText("Resume");
		} else {
			this.pauseButton.setText("Pause");
			headerDownloader.resume();
		}
	}// GEN-LAST:event_pauseButtonActionPerformed

	/** The parser. */
	HttpDateObject parser = HttpDateObject.getParser();
	
	/** The controller. */
	private UNISoNController controller;

	/**
	 * Download button action performed.
	 *
	 * @param evt the evt
	 */
	private void downloadButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_downloadButtonActionPerformed
		downloadEnabled(false);

		Object[] items = availableNewsgroups.getSelectedValues();
		Set<NNTPNewsGroup> groups = new HashSet<NNTPNewsGroup>();
		for (Object item : items) {
			groups.add((NNTPNewsGroup) item);
		}
		if (groups.size() > 0) {
			try {
				log("Download : " + groups);
				Date fromDate = parser.parseDate(fromDateField.getText());
				Date toDate = parser.parseDate(toDateField.getText());

				DownloadMode mode;
				if (getTextCheck.isSelected()) {
					mode = DownloadMode.ALL;
				} else if (getLocationCheck.isSelected()) {
					mode = DownloadMode.HEADERS;
				} else {
					mode = DownloadMode.BASIC;
				}
				controller.quickDownload(groups, fromDate, toDate, this, mode);

				log("Done.");
			} catch (UNISoNException e) {
				alert("Failed to download. Check your internet connection");
				downloadEnabled(true);
			} catch (ParseException e) {
				alert("Failed to parse date : " + e.getMessage());
				downloadEnabled(true);
			}
		}
	}// GEN-LAST:event_downloadButtonActionPerformed

	/* (non-Javadoc)
	 * @see uk.co.sleonard.unison.gui.UNISoNLogger#log(java.lang.String)
	 */
	@Override
	public void log(String message) {
		notesArea.append(message + "\n");
	}

	/* (non-Javadoc)
	 * @see uk.co.sleonard.unison.gui.UNISoNLogger#alert(java.lang.String)
	 */
	@Override
	public void alert(String message) {
		log(message);
		UNISoNController.getInstance().showAlert(message);
	}

	/**
	 * Find button action performed.
	 *
	 * @param evt the evt
	 */
	private void findButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_findButtonActionPerformed
		String host = hostCombo.getSelectedItem().toString().trim();
		controller.setNntpHost(host);
		String group = newsgroupField.getText();
		log("Find groups matching : " + group + " on "
				+ controller.getNntpHost());
		downloadEnabled(false);
		if (null != group) {
			try {
				availableGroups = UNISoNController.getInstance()
						.listNewsgroups(group,host);
			} catch (UNISoNException e) {
				alert("Problem downloading: " + e.getMessage());
			}
			if (null == availableGroups || availableGroups.size() == 0) {
				alert("No groups found for string : " + group + " on "
						+ controller.getNntpHost() + ".\nPerhaps another host?");

			} else {
				downloadEnabled(true);
			}
			this.availableNewsgroups.setModel(this.getAvailableGroupsModel());
		}

	}// GEN-LAST:event_findButtonActionPerformed

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object arg1) {
		if (observable instanceof HeaderDownloadWorker) {
			HeaderDownloadWorker headerDownloader = (HeaderDownloadWorker) observable;
			if (!headerDownloader.isDownloading()) {
				downloadEnabled(true);
			}
//		} else if (observable instanceof UNISoNController) {
//			UNISoNController controller = (UNISoNController) observable;
		}
	}
}
