package uk.co.sleonard.unison.gui;

/*
 * Copyright (c) 2000-2005 CyberFOX Software, Inc. All Rights Reserved.
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this library; if not, write to the
 *  Free Software Foundation, Inc.
 *  59 Temple Place
 *  Suite 330
 *  Boston, MA 02111-1307
 *  USA
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.apache.log4j.Logger;

public class JSplashScreen extends Window {
	class CloseJNSplash implements Runnable {
		public synchronized void run() {
			JSplashScreen.this.setVisible(false);
			JSplashScreen.this.dispose();
		}
	}

	private static Logger logger = Logger.getLogger("UNISoNController");

	/**
	 * 
	 */
	private static final long serialVersionUID = -6087186647558684188L;

	public static ImageIcon createImageIcon(final String path) {
		if (null == path) {
			return null;
		}
		final URL imgURL = JSplashScreen.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		}
		JSplashScreen.logger.error("Couldn't find file [" + path + "]");
		return null;
	}

	JProgressBar statusBar;

	// JNews's constructor
	public JSplashScreen(final ImageIcon icon) {
		super(new Frame());

		// Create a JPanel so we can use a BevelBorder
		final JPanel PanelForBorder = new JPanel(new BorderLayout());
		PanelForBorder.setLayout(new BorderLayout());
		PanelForBorder.add(new JLabel(icon), BorderLayout.CENTER);
		PanelForBorder.add(this.statusBar = new JProgressBar(0, 100),
				BorderLayout.SOUTH);
		PanelForBorder.setBorder(new BevelBorder(BevelBorder.RAISED));

		this.add(PanelForBorder);
		this.pack();

		// Plonk it on center of screen
		// Todo -- Multimonitor fix.
		final Dimension WindowSize = this.getSize(), ScreenSize = Toolkit
				.getDefaultToolkit().getScreenSize();

		this.setBounds((ScreenSize.width - WindowSize.width) / 2,
				(ScreenSize.height - WindowSize.height) / 2, WindowSize.width,
				WindowSize.height);
		this.setVisible(true);
	}

	public JSplashScreen(final String filePath) {
		this(JSplashScreen.createImageIcon(filePath));
	}

	public void close() {
		// Close and dispose Window in AWT thread
		SwingUtilities.invokeLater(new CloseJNSplash());
	}

	public void setProgress(final int value) {
		this.statusBar.setValue(value);
	}

	public void setWidth(final int maxCount) {
		this.statusBar.setMaximum(maxCount);
	}

	public void setWidthValue(final int maxCount, final int currentStatus) {
		this.statusBar.setMaximum(maxCount);
		this.statusBar.setValue(currentStatus);
	}

	public void showStatus(final int currentStatus) {
		// Update Splash-Screen's status bar in AWT thread
		this.statusBar.setValue(currentStatus);
	}
}
