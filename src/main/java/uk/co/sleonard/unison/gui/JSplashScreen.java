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
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

public class JSplashScreen extends JDialog {
	class CloseJNSplash implements Runnable {
		@Override
		public synchronized void run() {
			JSplashScreen.this.setVisible(false);
			JSplashScreen.this.dispose();
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6087186647558684188L;

	JProgressBar statusBar;

	public JSplashScreen(String description, JFrame frame) {
		super(frame, description, false);

		final JPanel PanelForBorder = new JPanel(new BorderLayout());
		PanelForBorder.setLayout(new BorderLayout());
		JLabel image = new JLabel(description);
		image.setVisible(true);
		PanelForBorder.add(image, BorderLayout.CENTER);
		PanelForBorder.add(this.statusBar = new JProgressBar(0, 100),
				BorderLayout.SOUTH);
		this.statusBar.setVisible(true);
		PanelForBorder.setBorder(new BevelBorder(BevelBorder.RAISED));
		PanelForBorder.setVisible(true);
		this.add(PanelForBorder);
		this.pack();

		// Plonk it on center of screen
		final Dimension WindowSize = this.getSize();
		Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();

		this.setBounds((ScreenSize.width - WindowSize.width) / 2,
				(ScreenSize.height - WindowSize.height) / 2, WindowSize.width,
				WindowSize.height);
		this.setVisible(true);

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
