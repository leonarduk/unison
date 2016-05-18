/*
 * UNISoNTabbedFrame.java
 *
 * Created on 28 November 2007, 08:59
 */

package uk.co.sleonard.unison.gui.generated;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.hsqldb.util.DatabaseManagerSwing;

import uk.co.sleonard.unison.gui.JSplashScreen;
import uk.co.sleonard.unison.gui.UNISoNController;

/**
 * The Class UNISoNTabbedFrame.
 *
 * @author Steve
 */
public class UNISoNTabbedFrame extends javax.swing.JFrame implements Observer {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -273253787613490358L;

	/** The Constant dbDriver. */
	private final static String dbDriver = "org.hsqldb.jdbcDriver";

	/** The Constant dbUser. */
	private final static String dbUser = "sa";

	/** The Constant DB_URL. */
	private final static String DB_URL = "jdbc:hsqldb:file:DB/projectDB";

	/** The Constant GUI_ARGS. */
	// http://www.electric-spoon.com/cgi-bin/man/man2html?hsqldb-databasemanagerswing+1
	public static final String GUI_ARGS[] = { "-driver", UNISoNTabbedFrame.dbDriver, "-url",
	        UNISoNTabbedFrame.DB_URL, "-user", UNISoNTabbedFrame.dbUser, "-noexit" };

	/**
	 * The main method.
	 *
	 * @param args
	 *            the command line arguments
	 */
	public static void main(final String args[]) {
		java.awt.EventQueue.invokeLater(() -> new UNISoNTabbedFrame().setVisible(true));
	}

	/** The about menu item. */
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JMenuItem aboutMenuItem;

	/** The delete db menu item. */
	private javax.swing.JMenuItem deleteDBMenuItem;

	/** The download news panel1. */
	private uk.co.sleonard.unison.gui.generated.DownloadNewsPanel downloadNewsPanel1;

	/** The exit menu item. */
	private javax.swing.JMenuItem exitMenuItem;

	/** The file menu. */
	private javax.swing.JMenu fileMenu;

	/** The help menu. */
	private javax.swing.JMenu helpMenu;

	/** The j menu bar1. */
	private javax.swing.JMenuBar jMenuBar1;

	/** The j separator1. */
	private javax.swing.JSeparator jSeparator1;

	/** The j separator2. */
	private javax.swing.JSeparator jSeparator2;

	/** The message store viewer1. */
	private uk.co.sleonard.unison.gui.generated.MessageStoreViewer messageStoreViewer1;

	/** The pajek panel1. */
	private uk.co.sleonard.unison.gui.generated.PajekPanel pajekPanel1;

	/** The refresh db menu item. */
	private javax.swing.JMenuItem refreshDBMenuItem;

	/** The show d bclient menu item. */
	private javax.swing.JMenuItem showDBclientMenuItem;

	/** The tabbed pane. */
	private javax.swing.JTabbedPane tabbedPane;

	/** The controller. */
	private final UNISoNController controller;

	/** The about dialog. */
	private final AboutDialog aboutDialog;

	// End of variables declaration//GEN-END:variables
	/**
	 * Creates new form UNISoNTabbedFrame.
	 */
	public UNISoNTabbedFrame() {
		this.setTitle("UNISoN");
		final JSplashScreen splash = new JSplashScreen("Loading ...", this);
		splash.setProgress(10);

		this.controller = UNISoNController.create(this);

		splash.setProgress(50);
		this.initComponents();
		splash.setProgress(80);

		this.controller.addObserver(this.downloadNewsPanel1);
		this.controller.addObserver(this.messageStoreViewer1);
		this.controller.addObserver(this.pajekPanel1);
		this.controller.addObserver(this);

		this.aboutDialog = new AboutDialog(this, false);

		this.exitMenuItem
		        .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));

		this.refreshDBMenuItem
		        .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));

		this.deleteDBMenuItem
		        .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, InputEvent.CTRL_MASK));

		this.showDBclientMenuItem
		        .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.CTRL_MASK));

		this.aboutMenuItem
		        .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, InputEvent.CTRL_MASK));

		this.controller.refreshDataFromDatabase();
		splash.setProgress(100);

		splash.close();
	}

	/**
	 * About menu item action performed.
	 *
	 * @param evt
	 *            the evt
	 */
	private void aboutMenuItemActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_aboutMenuItemActionPerformed
		this.aboutDialog.setVisible(true);
	}// GEN-LAST:event_aboutMenuItemActionPerformed

	/**
	 * Delete db menu item action performed.
	 *
	 * @param evt
	 *            the evt
	 */
	private void deleteDBMenuItemActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_deleteDBMenuItemActionPerformed
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

	/**
	 * Exit menu item action performed.
	 *
	 * @param evt
	 *            the evt
	 */
	private void exitMenuItemActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_exitMenuItemActionPerformed
		final int response = JOptionPane.showConfirmDialog(this, "Are you sure?",
		        "Exit Application", JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}// GEN-LAST:event_exitMenuItemActionPerformed

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT
	 * modify this code. The content of this method is always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// ">//GEN-BEGIN:initComponents
	private void initComponents() {
		this.tabbedPane = new javax.swing.JTabbedPane();
		this.downloadNewsPanel1 = new uk.co.sleonard.unison.gui.generated.DownloadNewsPanel();
		this.messageStoreViewer1 = new uk.co.sleonard.unison.gui.generated.MessageStoreViewer();
		this.pajekPanel1 = new uk.co.sleonard.unison.gui.generated.PajekPanel(this);
		this.jMenuBar1 = new javax.swing.JMenuBar();
		this.fileMenu = new javax.swing.JMenu();
		this.refreshDBMenuItem = new javax.swing.JMenuItem();
		this.showDBclientMenuItem = new javax.swing.JMenuItem();
		this.jSeparator1 = new javax.swing.JSeparator();
		this.exitMenuItem = new javax.swing.JMenuItem();
		this.helpMenu = new javax.swing.JMenu();
		this.deleteDBMenuItem = new javax.swing.JMenuItem();
		this.jSeparator2 = new javax.swing.JSeparator();
		this.aboutMenuItem = new javax.swing.JMenuItem();

		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		this.tabbedPane.setName("");
		this.tabbedPane.addTab("Download Messages", this.downloadNewsPanel1);

		this.tabbedPane.addTab("View Saved Data", this.messageStoreViewer1);

		this.tabbedPane.addTab("Transform Data For Pajek", this.pajekPanel1);

		this.fileMenu.setText("File");
		this.refreshDBMenuItem.setText("Refresh Data");
		this.refreshDBMenuItem.setToolTipText("Loads the latest data from the database");
		this.refreshDBMenuItem.addActionListener(
		        evt -> UNISoNTabbedFrame.this.refreshDBMenuItemActionPerformed(evt));

		this.fileMenu.add(this.refreshDBMenuItem);

		this.showDBclientMenuItem.setText("DB GUI");
		this.showDBclientMenuItem.setToolTipText("Brings up a DB client to view the data directly");
		this.showDBclientMenuItem.addActionListener(
		        evt -> UNISoNTabbedFrame.this.showDBclientMenuItemActionPerformed(evt));

		this.fileMenu.add(this.showDBclientMenuItem);

		this.fileMenu.add(this.jSeparator1);

		this.exitMenuItem.setText("Exit");
		this.exitMenuItem.setToolTipText("Exits the application");
		this.exitMenuItem
		        .addActionListener(evt -> UNISoNTabbedFrame.this.exitMenuItemActionPerformed(evt));

		this.fileMenu.add(this.exitMenuItem);

		this.jMenuBar1.add(this.fileMenu);

		this.helpMenu.setText("Help");
		this.deleteDBMenuItem.setText("Delete All Data");
		this.deleteDBMenuItem.setToolTipText(
		        "This clears all the downloaded data - warning you will need to download everything again");
		this.deleteDBMenuItem.addActionListener(
		        evt -> UNISoNTabbedFrame.this.deleteDBMenuItemActionPerformed(evt));

		this.helpMenu.add(this.deleteDBMenuItem);

		this.helpMenu.add(this.jSeparator2);

		this.aboutMenuItem.setText("About UNISoN");
		this.aboutMenuItem.setToolTipText("A little bit about this application.");
		this.aboutMenuItem
		        .addActionListener(evt -> UNISoNTabbedFrame.this.aboutMenuItemActionPerformed(evt));

		this.helpMenu.add(this.aboutMenuItem);

		this.jMenuBar1.add(this.helpMenu);

		this.setJMenuBar(this.jMenuBar1);

		final javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this.getContentPane());
		this.getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
		        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
		                this.tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 566,
		                Short.MAX_VALUE));
		layout.setVerticalGroup(
		        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                .addGroup(layout.createSequentialGroup().addContainerGap().addComponent(
		                        this.tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 373,
		                        Short.MAX_VALUE)));
		this.pack();
	}// </editor-fold>//GEN-END:initComponents

	/**
	 * Refresh db menu item action performed.
	 *
	 * @param evt
	 *            the evt
	 */
	private void refreshDBMenuItemActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_refreshDBMenuItemActionPerformed
		UNISoNController.getInstance().refreshDataFromDatabase();
	}// GEN-LAST:event_refreshDBMenuItemActionPerformed

	/**
	 * Show alert.
	 *
	 * @param message
	 *            the message
	 */
	public void showAlert(final String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	/**
	 * Show d bclient menu item action performed.
	 *
	 * @param evt
	 *            the evt
	 */
	private void showDBclientMenuItemActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_showDBclientMenuItemActionPerformed
		DatabaseManagerSwing.main(UNISoNTabbedFrame.GUI_ARGS);
	}// GEN-LAST:event_showDBclientMenuItemActionPerformed

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(final Observable observable, final Object arg1) {
		if (observable instanceof UNISoNController) {
			this.showAlert("GUI has been refreshed from the database");
		}
	}

}
