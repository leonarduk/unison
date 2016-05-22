package uk.co.sleonard.unison.gui;

/*
 * Copyright (c) 2000-2005 CyberFOX Software, Inc. All Rights Reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Library General Public License as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc. 59 Temple Place Suite 330 Boston, MA
 * 02111-1307 USA
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

/**
 * The Class JSplashScreen.
 * 
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 * 
 */
public class JSplashScreen extends JDialog {

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= -6087186647558684188L;

	/** The status bar. */
	JProgressBar				statusBar;

	/**
	 * The Class CloseJNSplash.
	 */
	class CloseJNSplash implements Runnable {

		/*
		 * (non-Javadoc)
		 *
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public synchronized void run() {
			JSplashScreen.this.setVisible(false);
			JSplashScreen.this.dispose();
		}
	}

	/**
	 * Instantiates a new j splash screen.
	 *
	 * @param description
	 *            the description
	 * @param frame
	 *            the frame
	 */
	public JSplashScreen(final String description, final JFrame frame) {
		super(frame, description, false);

		final JPanel PanelForBorder = new JPanel(new BorderLayout());
		PanelForBorder.setLayout(new BorderLayout());
		final JLabel image = new JLabel(description);
		image.setVisible(true);
		PanelForBorder.add(image, BorderLayout.CENTER);
		PanelForBorder.add(this.statusBar = new JProgressBar(0, 100), BorderLayout.SOUTH);
		this.statusBar.setVisible(true);
		PanelForBorder.setBorder(new BevelBorder(BevelBorder.RAISED));
		PanelForBorder.setVisible(true);
		this.add(PanelForBorder);
		this.pack();

		// Plonk it on center of screen
		final Dimension WindowSize = this.getSize();
		final Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();

		this.setBounds((ScreenSize.width - WindowSize.width) / 2,
		        (ScreenSize.height - WindowSize.height) / 2, WindowSize.width, WindowSize.height);
		this.setVisible(true);

	}

	/**
	 * Close.
	 */
	public void close() {
		// Close and dispose Window in AWT thread
		SwingUtilities.invokeLater(new CloseJNSplash());
	}

	/**
	 * Sets the progress.
	 *
	 * @param value
	 *            the new progress
	 */
	public void setProgress(final int value) {
		this.statusBar.setValue(value);
	}

	/**
	 * Sets the width.
	 *
	 * @param maxCount
	 *            the new width
	 */
	public void setWidth(final int maxCount) {
		this.statusBar.setMaximum(maxCount);
	}

	/**
	 * Sets the width value.
	 *
	 * @param maxCount
	 *            the max count
	 * @param currentStatus
	 *            the current status
	 */
	public void setWidthValue(final int maxCount, final int currentStatus) {
		this.statusBar.setMaximum(maxCount);
		this.statusBar.setValue(currentStatus);
	}

	/**
	 * Show status.
	 *
	 * @param currentStatus
	 *            the current status
	 */
	public void showStatus(final int currentStatus) {
		// Update Splash-Screen's status bar in AWT thread
		this.statusBar.setValue(currentStatus);
	}
}
