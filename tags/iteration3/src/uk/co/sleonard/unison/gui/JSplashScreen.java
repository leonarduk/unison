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
	/**
	 * 
	 */
	private static final long serialVersionUID = -6087186647558684188L;
	JProgressBar statusBar;

	public static ImageIcon createImageIcon(String path) {
		URL imgURL = JSplashScreen.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		}
		logger.error("Couldn't find file [" + path + "]");
		return null;
	}

	private static Logger logger = Logger.getLogger("UNISoNController");

	public void setProgress(int value) {
		statusBar.setValue(value);
	}
	
	public JSplashScreen(String filePath) {
		this(createImageIcon(filePath));
	}

	// JNews's constructor
	public JSplashScreen(ImageIcon icon) {
		super(new Frame());

		// Create a JPanel so we can use a BevelBorder
		JPanel PanelForBorder = new JPanel(new BorderLayout());
		PanelForBorder.setLayout(new BorderLayout());
		PanelForBorder.add(new JLabel(icon), BorderLayout.CENTER);
		PanelForBorder.add(statusBar = new JProgressBar(0, 100),
				BorderLayout.SOUTH);
		PanelForBorder.setBorder(new BevelBorder(BevelBorder.RAISED));

		add(PanelForBorder);
		pack();

		// Plonk it on center of screen
		// Todo -- Multimonitor fix.
		Dimension WindowSize = getSize(), ScreenSize = Toolkit
				.getDefaultToolkit().getScreenSize();

		setBounds((ScreenSize.width - WindowSize.width) / 2,
				(ScreenSize.height - WindowSize.height) / 2, WindowSize.width,
				WindowSize.height);
		setVisible(true);
	}

	public void showStatus(int currentStatus) {
		// Update Splash-Screen's status bar in AWT thread
		statusBar.setValue(currentStatus);
	}

	public void setWidth(int maxCount) {
		statusBar.setMaximum(maxCount);
	}

	public void setWidthValue(int maxCount, int currentStatus) {
		statusBar.setMaximum(maxCount);
		statusBar.setValue(currentStatus);
	}

	public void close() {
		// Close and dispose Window in AWT thread
		SwingUtilities.invokeLater(new CloseJNSplash());
	}

	class CloseJNSplash implements Runnable {
		public synchronized void run() {
			setVisible(false);
			dispose();
		}
	}
}
