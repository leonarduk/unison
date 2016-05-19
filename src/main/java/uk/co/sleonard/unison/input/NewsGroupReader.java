package uk.co.sleonard.unison.input;

/**
 * NewsGroupReader - NNTP client
 *
 * @author Steve Leonard Copyright (C) 2007 This program is free software; you can redistribute it
 *         and/or modify it under the terms of the GNU General Public License as published by the
 *         Free Software Foundation; either version 2 of the License, or (at your option) any later
 *         version.
 *
 *         This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *         without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *         See the GNU General Public License for more details.
 *
 *         You should have received a copy of the GNU General Public License along with this
 *         program; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *         Boston, MA 02111-1307 USA
 *
 */

import java.util.ArrayList;

import javax.mail.Message;

import org.apache.log4j.Logger;

import uk.co.sleonard.unison.gui.UNISoNController;
import uk.co.sleonard.unison.gui.UNISoNLogger;

/**
 * The Class NewsGroupReader.
 * 
 * @author
 * @since
 *
 */
public class NewsGroupReader implements UNISoNLogger {
	// public Message[] getMessages() throws MessagingException {
	// return folder.getMessages();
	/** The logger. */
	// }
	private static Logger logger = Logger.getLogger("NewsGroupReader");

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws Exception
	 *             the exception
	 */
	public static void main(final String args[]) throws Exception {
		/*
		 * Chosen from http://freeusenetnews.com/newspage.html
		 *
		 * or http://www.elfqrin.com/hacklab/pages/nntpserv.php
		 */
		final String host = "freetext.usenetserver.com";

		String newsgroup = "soc.senior.issues";
		if (args.length > 0) {
			newsgroup = args[0];
		}
		System.out.println("Look for " + newsgroup + " on " + host);

		try {
			final NewsGroupReader reader = new NewsGroupReader(null);
			reader.client.connectToNewsGroup(host, newsgroup);
			// Date toDate = null;
			// Date fromDate = null;
			// reader.startDownload(newsgroup, fromDate, toDate);
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
		System.out.println("done");

	}

	/** The client. */
	public NewsClient	client;

	/** The message count. */
	private int			messageCount;

	/** The msgs. */
	@Deprecated
	ArrayList<Message>	msgs;

	/** The msgs skipped. */
	int					msgsSkipped	= 0;

	/** The msgs stored. */
	int					msgsStored	= 0;

	/**
	 * Instantiates a new news group reader.
	 *
	 * @param controller
	 *            the controller
	 */
	public NewsGroupReader(final UNISoNController controller) {
		this.client = new NewsClient();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see uk.co.sleonard.unison.gui.UNISoNLogger#alert(java.lang.String)
	 */
	@Override
	public void alert(final String message) {
		NewsGroupReader.logger.warn(message);
	}

	/**
	 * Gets the message count.
	 *
	 * @return the message count
	 */
	public int getMessageCount() {
		return this.messageCount;
	}

	/**
	 * Gets the messages skipped.
	 *
	 * @return the messages skipped
	 */
	public int getMessagesSkipped() {
		return this.msgsSkipped;
	}

	/**
	 * Gets the messages stored.
	 *
	 * @return the messages stored
	 */
	public int getMessagesStored() {
		return this.msgsStored;
	}

	/**
	 * Gets the number of messages.
	 *
	 * @return the number of messages
	 */
	public int getNumberOfMessages() {
		return this.client.getMessageCount();
	}

	/**
	 * Increment messages skipped.
	 */
	public synchronized void incrementMessagesSkipped() {
		this.msgsSkipped++;
	}

	/**
	 * Increment messages stored.
	 */
	public synchronized void incrementMessagesStored() {
		this.msgsStored++;
	}

	/**
	 * Checks if is connected.
	 *
	 * @return true, if is connected
	 */
	public boolean isConnected() {
		return this.client.isConnected();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see uk.co.sleonard.unison.gui.UNISoNLogger#log(java.lang.String)
	 */
	@Override
	public void log(final String message) {
		NewsGroupReader.logger.info(message);
	}

	/**
	 * Sets the downloader finished.
	 *
	 * @param worker
	 *            the new downloader finished
	 */
	public void setDownloaderFinished(final FullDownloadWorker worker) {
		// throw new RuntimeException("not implemented");
	}

	// public void startDownload(String newsgroup, Date toDate, Date fromDate)
	// throws UNISoNException {
	// Set<NNTPNewsGroup> listNewsgroups = UNISoNController.getInstance()
	// .listNewsgroups(newsgroup);
	// UNISoNController.getInstance().quickDownload(listNewsgroups, fromDate,
	// toDate, this);
	// }

	/**
	 * Sets the message count.
	 *
	 * @param messageCount
	 *            the new message count
	 */
	public void setMessageCount(final int messageCount) {
		this.messageCount = messageCount;
	}

	/**
	 * Show download status.
	 */
	public synchronized void showDownloadStatus() {
		final int i = this.getMessagesSkipped() + this.getMessagesStored();

		if (this.getMessageCount() > 0) {
			final int progress = i * 100 / this.getMessageCount();
			UNISoNController.getInstance().setDownloadingState(progress);
		}

	}

}
