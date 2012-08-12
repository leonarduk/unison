package uk.co.sleonard.unison.input;

/**
 * NewsGroupReader - NNTP client
 *
 * @author Steve Leonard Copyright (C) 2007
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import javax.mail.Message;

import org.apache.log4j.Logger;

import uk.co.sleonard.unison.gui.UNISoNController;
import uk.co.sleonard.unison.gui.UNISoNException;
import uk.co.sleonard.unison.gui.UNISoNLogger;

public class NewsGroupReader implements UNISoNLogger {
	// public Message[] getMessages() throws MessagingException {
	// return folder.getMessages();
	// }
	private static Logger logger = Logger.getLogger("NewsGroupReader");

	public NewsClient client;

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
			final NewsGroupReader reader = new NewsGroupReader();
			reader.client.connectToNewsGroup(host, newsgroup);
			Date toDate = null;
			Date fromDate = null;
			// reader.startDownload(newsgroup, fromDate, toDate);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		System.out.println("done");

	}

	private final ArrayList<FullDownloadWorker> downloaders;

	private int messageCount;

	private final LinkedBlockingQueue<NewsArticle> messageQueue;

	@Deprecated
	ArrayList<Message> msgs;

	int msgsSkipped = 0;

	int msgsStored = 0;

	ArrayList<SwingWorker> workers;

	public NewsGroupReader() {
		this.messageQueue = new LinkedBlockingQueue<NewsArticle>();
		this.workers = new ArrayList<SwingWorker>();
		this.downloaders = new ArrayList<FullDownloadWorker>();
		this.client = new NewsClient();
		this.startHibernators(7);
		try {
			startDownloaders(3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public int getMessagesSkipped() {
		return this.msgsSkipped;
	}

	public int getMessagesStored() {
		return this.msgsStored;
	}

	public int getNumberOfMessages() {
		return this.client.getMessageCount();
	}

	public LinkedBlockingQueue<NewsArticle> getQueue() {
		return this.messageQueue;
	}

	public synchronized void incrementMessagesSkipped() {
		this.msgsSkipped++;
	}

	public synchronized void incrementMessagesStored() {
		this.msgsStored++;
	}

	public boolean isConnected() {
		return this.client.isConnected();
	}

	public void setDownloaderFinished(final FullDownloadWorker worker) {
		// throw new RuntimeException("not implemented");
	}

	/**
	 * 
	 * 
	 */
	public synchronized void showDownloadStatus() {
		final int i = this.getMessagesSkipped() + this.getMessagesStored();

		if (this.getMessageCount() > 0) {
			final int progress = (i * 100) / this.getMessageCount();
			UNISoNController.getInstance().setDownloadingState(progress);
		}
		UNISoNController.getInstance().showStatus(
				"Downloaded: " + i + "/" + this.getMessageCount() + "\rAdded: "
						+ this.getMessagesStored() + "\n Skipped:"
						+ this.getMessagesSkipped() + " queued "
						+ this.getQueue().size());

	}

	public void startDownloaders(final int numberOfDownloaders)
			throws IOException {

		for (int i = 0; i < numberOfDownloaders; i++) {
			String host = client.getNNTPHost();
			this.downloaders.add(new FullDownloadWorker(host, this.getQueue()));
		}
	}

	public void startHibernators(final int numberofHibernators) {

		for (int i = 0; i < numberofHibernators; i++) {
			this.workers.add(new DataHibernatorWorker(this));
		}
	}

	public void stopDownload() {
		for (final ListIterator<SwingWorker> iter = this.workers.listIterator(); iter
				.hasNext();) {
			iter.next().interrupt();
		}
		UNISoNController.getInstance().setIdleState();
	}

	public void setMessageCount(int messageCount) {
		this.messageCount = messageCount;
	}

	public int getMessageCount() {
		return messageCount;
	}

	// public void startDownload(String newsgroup, Date toDate, Date fromDate)
	// throws UNISoNException {
	// Set<NNTPNewsGroup> listNewsgroups = UNISoNController.getInstance()
	// .listNewsgroups(newsgroup);
	// UNISoNController.getInstance().quickDownload(listNewsgroups, fromDate,
	// toDate, this);
	// }

	@Override
	public void alert(String message) {
		logger.warn(message);
	}

	@Override
	public void log(String message) {
		logger.info(message);
	}

}