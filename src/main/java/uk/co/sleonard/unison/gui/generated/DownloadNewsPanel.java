/*
 * DownloadNewsPanel.java
 *
 * Created on 27 November 2007, 08:58
 */

package uk.co.sleonard.unison.gui.generated;

import lombok.extern.slf4j.Slf4j;
import uk.co.sleonard.unison.StatusMonitor;
import uk.co.sleonard.unison.UNISoNController;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.input.HeaderDownloadWorker;
import uk.co.sleonard.unison.utils.StringUtils;

import java.beans.PropertyChangeEvent;
import uk.co.sleonard.unison.DataChangeListener;
import java.util.Set;

/**
 * The Class DownloadNewsPanel.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 */
@Slf4j
@SuppressWarnings("rawtypes")
public class DownloadNewsPanel extends javax.swing.JPanel
        implements DataChangeListener, StatusMonitor {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 6581138636992116397L;

    /**
     * The available newsgroups.
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<NewsGroup> availableNewsgroups;

    /**
     * The cancel button.
     */
    private javax.swing.JButton cancelButton;

    /**
     * The download button.
     */
    private javax.swing.JButton downloadButton;

    /**
     * The find button.
     */
    private javax.swing.JButton findButton;

    /**
     * The from date field.
     */
    private javax.swing.JTextField fromDateField;

    /**
     * The from date label.
     */
    private javax.swing.JLabel fromDateLabel;

    /**
     * The get location check.
     */
    private javax.swing.JCheckBox getLocationCheck;


    /**
     * The host combo.
     */
    private javax.swing.JComboBox hostCombo;

    /**
     * The host label.
     */
    private javax.swing.JLabel hostLabel;

    /**
     * The j scroll pane1.
     */
    private javax.swing.JScrollPane jScrollPane1;

    /**
     * The j scroll pane3.
     */
    private javax.swing.JScrollPane jScrollPane3;

    /**
     * The newsgroup field.
     */
    private javax.swing.JTextField newsgroupField;

    /**
     * The newsgroup label.
     */
    private javax.swing.JLabel newsgroupLabel;

    /**
     * The notes area.
     */
    private javax.swing.JTextArea notesArea;

    /**
     * The pause button.
     */
    private javax.swing.JButton pauseButton;

    /**
     * The progress bar.
     */
    private javax.swing.JProgressBar progressBar;

    /**
     * The to date field.
     */
    private javax.swing.JTextField toDateField;
    /**
     * The to date label.
     */
    private javax.swing.JLabel toDateLabel;

    /**
     * The available groups.
     */
    private Set<NewsGroup> availableGroups;

    /**
     * The controller.
     */
    private final UNISoNController controller;

    private StringBuffer logText;

    /**
     * The main method.
     *
     * @param args the command line arguments
     */
    public static void main(final String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            UNISoNTabbedFrame frame;
            try {

                frame = new UNISoNTabbedFrame();
                frame.setVisible(true);
                final DownloadNewsPanel panel = new DownloadNewsPanel(
                        UNISoNController.getInstance());
                frame.add(panel);
                panel.setVisible(true);
            } catch (final Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    // End of variables declaration//GEN-END:variables

    /**
     * Creates new form DownloadNewsPanel.
     *
     * @param unisonController
     */
    public DownloadNewsPanel(final UNISoNController unisonController) {
        this.controller = unisonController;

        this.initComponents();

        this.controller.setNntpHost(StringUtils.loadServerList()[0]);
        this.controller.getHeaderDownloader().addDataChangeListener(this);

        this.controller.setNntpHost(this.hostCombo.getSelectedItem().toString());
        this.downloadEnabled(false);
        this.getLocationCheck.setToolTipText("Downloads Location & Crossposts");
        this.getLocationCheck.setText("Get Extras");

        // Need to set these manually to false as previous method sets them to
        // true
        this.cancelButton.setEnabled(false);
        this.pauseButton.setEnabled(false);

        this.controller.setDownloadPanel(this);

    }

    /*
     * (non-Javadoc)
     *
     */
    private void appendNote(final String message) {
        this.logText.append(message + "\n");
        this.notesArea.setText(this.logText.toString());
    }

    /**
     * Available newsgroups value changed.
     *
     * @param evt the evt
     */
    private void availableNewsgroupsValueChanged(final javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_availableNewsgroupsValueChanged

    }// GEN-LAST:event_availableNewsgroupsValueChanged

    /**
     * Cancel button action performed.
     *
     * @param evt the evt
     */
    private void cancelButtonActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cancelButtonActionPerformed
        this.controller.cancel();
    }// GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Download button action performed.
     *
     * @param evt the evt
     */
    private void downloadButtonActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_downloadButtonActionPerformed
        final NewsGroup[] selected = this.availableNewsgroups.getSelectedValuesList()
                .toArray(new NewsGroup[0]);
        try {
            this.controller.download(this, selected, this.fromDateField.getText(),
                    this.toDateField.getText(), this.getLocationCheck.isSelected(),
                    false);
        } catch (final Exception e) {
            final String message = "Failed to start download: " + e.getMessage();
            log.warn(message, e);
            this.appendNote(message);
            if (this.controller.getGui() != null) {
                this.controller.getGui().showAlert(message);
            }
        }
    }// GEN-LAST:event_downloadButtonActionPerformed

    /**
     * Download enabled.
     *
     * @param enabled the enabled
     */
    @Override
    public void downloadEnabled(final boolean enabled) {
        this.downloadButton.setEnabled(enabled);
        this.fromDateLabel.setEnabled(enabled);
        this.toDateLabel.setEnabled(enabled);
        this.fromDateField.setEnabled(enabled);
        this.toDateField.setEnabled(enabled);
        this.getLocationCheck.setEnabled(enabled);

        // these are off when download on & vice versa
        this.cancelButton.setEnabled(!enabled);
        this.pauseButton.setEnabled(!enabled);
    }

    /**
     * Find button action performed.
     *
     * @param evt the evt
     */
    private void findButtonActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_findButtonActionPerformed
        this.controller.requestDownload(this.newsgroupField.getText(),
                this.hostCombo.getSelectedItem().toString().trim(), this,
                this.controller.getNntpReader().getClient());
    }// GEN-LAST:event_findButtonActionPerformed

    /**
     * Form mouse pressed.
     *
     * @param evt the evt
     */
    void formMousePressed(final java.awt.event.MouseEvent evt) {// GEN-FIRST:event_formMousePressed
        // TODO add your handling code here:
    }// GEN-LAST:event_formMousePressed

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code
    // <editor-fold defaultstate="collapsed" desc=" Generated Code
    // <editor-fold defaultstate="collapsed" desc=" Generated Code
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    @SuppressWarnings("unchecked")
    private void initComponents() {
        this.newsgroupLabel = new javax.swing.JLabel();
        this.newsgroupField = new javax.swing.JTextField();
        this.findButton = new javax.swing.JButton();
        this.fromDateLabel = new javax.swing.JLabel();
        this.toDateLabel = new javax.swing.JLabel();
        this.toDateField = new javax.swing.JTextField();
        this.fromDateField = new javax.swing.JTextField();
        this.downloadButton = new javax.swing.JButton();
        this.pauseButton = new javax.swing.JButton();
        this.cancelButton = new javax.swing.JButton();
        this.jScrollPane3 = new javax.swing.JScrollPane();
        this.notesArea = new javax.swing.JTextArea();
        this.logText = new StringBuffer();
        this.hostLabel = new javax.swing.JLabel();
        this.getLocationCheck = new javax.swing.JCheckBox();
        this.hostCombo = new javax.swing.JComboBox();
        this.jScrollPane1 = new javax.swing.JScrollPane();
        this.availableNewsgroups = new javax.swing.JList<>();
        this.progressBar = new javax.swing.JProgressBar();
        this.progressBar.setStringPainted(true);

        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(final java.awt.event.MouseEvent evt) {
                DownloadNewsPanel.this.formMousePressed(evt);
            }
        });

        this.newsgroupLabel.setText("Newsgroup ");
        this.newsgroupLabel.setToolTipText(
                "Type in full or part name (use * for wild character, e.g. *senior* for all groups with \"senior\" in the name)");

        this.newsgroupField.setToolTipText(
                "Type in full or part name (use * for wild character, e.g. *senior* for all groups with \"senior\" in the name)");

        this.findButton.setText("Find groups");
        this.findButton
                .setToolTipText("Enter name or part name (* for wild chararcter) the n click here");
        this.findButton
                .addActionListener(evt -> DownloadNewsPanel.this.findButtonActionPerformed(evt));

        this.fromDateLabel.setText("Date from");
        this.fromDateLabel.setToolTipText("In YYYYMMDD format eg 20071201");

        this.toDateLabel.setText("Date To");
        this.toDateLabel.setToolTipText("In YYYYMMDD format eg 20071201");

        this.toDateField.setToolTipText("In YYYYMMDD format eg 20071201");

        this.fromDateField.setToolTipText("In YYYYMMDD format eg 20071201");

        this.downloadButton.setText("Download");
        this.downloadButton.addActionListener(
                evt -> DownloadNewsPanel.this.downloadButtonActionPerformed(evt));

        this.pauseButton.setText("Pause");
        this.pauseButton
                .addActionListener(evt -> DownloadNewsPanel.this.pauseButtonActionPerformed(evt));

        this.cancelButton.setText("Cancel");
        this.cancelButton
                .addActionListener(evt -> DownloadNewsPanel.this.cancelButtonActionPerformed(evt));

        this.notesArea.setColumns(20);
        this.notesArea.setRows(5);
        this.jScrollPane3.setViewportView(this.notesArea);

        this.hostLabel.setText(" Server");
        this.hostLabel.setToolTipText(
                "Look at http://freeusenetnews.com/newspage.html?sortby=articles for other hosts if these are broken. Can enter name here.");

        this.getLocationCheck.setText("Get Location");
        this.getLocationCheck.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.getLocationCheck.setMargin(new java.awt.Insets(0, 0, 0, 0));

        this.hostCombo.setEditable(true);
        this.hostCombo.setModel(new javax.swing.DefaultComboBoxModel(StringUtils.loadServerList()));
        this.hostCombo.setToolTipText(
                "Look at http://freeusenetnews.com/newspage.html?sortby=articles for other hosts if these are broken. Can enter name here.");

        this.updateAvailableGroups(this.availableGroups);
        this.availableNewsgroups.setToolTipText(
                "The number in brackets is an estimate (provided by the server) which is likely to be higher than actual number of messages as many messages are deleted.");
        this.availableNewsgroups.addListSelectionListener(
                evt -> DownloadNewsPanel.this.availableNewsgroupsValueChanged(evt));

        this.jScrollPane1.setViewportView(this.availableNewsgroups);

        final javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(this.jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 262,
                                                Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(this.newsgroupLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(this.newsgroupField,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(this.hostLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                        17, Short.MAX_VALUE)
                                                .addComponent(this.hostCombo,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(
                                                        javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(layout.createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(this.downloadButton)
                                                        .addComponent(this.fromDateLabel)
                                                        .addComponent(this.fromDateField,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(this.toDateField,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(this.toDateLabel)
                                                        .addComponent(this.cancelButton)
                                                        .addComponent(this.pauseButton))
                                                .addComponent(this.findButton))
                                        .addComponent(this.getLocationCheck))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(this.jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 193,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                        .addComponent(this.progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL,
                new java.awt.Component[]{this.cancelButton, this.downloadButton, this.findButton,
                        this.fromDateField, this.pauseButton, this.toDateField});

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup().addContainerGap()
                                                .addGroup(layout.createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(55, 55, 55)
                                                                .addComponent(this.getLocationCheck)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(this.fromDateLabel,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 14,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(this.fromDateField,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 20,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(this.toDateLabel,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 14,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(this.toDateField,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 20,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                        59, Short.MAX_VALUE)
                                                                .addComponent(this.downloadButton,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 23,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(this.pauseButton,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 23,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(this.cancelButton))
                                                        .addComponent(this.findButton)))
                                        .addGroup(layout.createSequentialGroup().addContainerGap()
                                                .addComponent(this.jScrollPane3,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, 325,
                                                        Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup().addGap(46, 46, 46)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(this.hostLabel)
                                                                        .addComponent(this.hostCombo,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                        .addGroup(layout.createSequentialGroup().addContainerGap()
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(this.newsgroupLabel)
                                                                        .addComponent(this.newsgroupField,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                .addPreferredGap(
                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(this.jScrollPane1,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, 220,
                                                        Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(this.progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap()));
    }// </editor-fold>//GEN-END:initComponents

    /*
     * (non-Javadoc)
     *
     */

    /**
     * Updates the download progress bar.
     *
     * @param progress the new progress value
     */
    public void setProgress(final int progress) {
        this.progressBar.setValue(progress);
    }

    /**
     * Pause button action performed.
     *
     * @param evt the evt
     */
    private void pauseButtonActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_pauseButtonActionPerformed
        final HeaderDownloadWorker headerDownloader = this.controller.getHeaderDownloader();
        if (headerDownloader.isDownloading()) {
            headerDownloader.pause();
            this.pauseButton.setText("Resume");
        } else {
            this.pauseButton.setText("Pause");
            headerDownloader.resume();
        }
    }// GEN-LAST:event_pauseButtonActionPerformed

    /*
     * (non-Javadoc)
     *
     * @see uk.co.sleonard.unison.DataChangeListener#dataChanged(java.beans.PropertyChangeEvent)
     */
    @Override
    public void dataChanged(final PropertyChangeEvent evt) {
        final Object source = evt.getSource();
        if (source instanceof HeaderDownloadWorker) {
            final HeaderDownloadWorker headerDownloader = (HeaderDownloadWorker) source;
            if (!headerDownloader.isDownloading()) {
                this.downloadEnabled(true);
            }
        }
    }

    @Override
    public void updateAvailableGroups(final Set<NewsGroup> availableGroups2) {
        this.availableNewsgroups
                .setModel(this.controller.getAvailableGroupsModel(availableGroups2));
    }
}
