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

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.log4j.Logger;

import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.gui.UNISoNController;
import uk.co.sleonard.unison.gui.generated.UNISoNFrame;

public class NewsGroupReader {
	// public Message[] getMessages() throws MessagingException {
	// return folder.getMessages();
	// }
	private static Logger logger = Logger.getLogger("NewsGroupReader");

	public static void main(String args[]) throws Exception {
		/*
		 * Chosen from http://freeusenetnews.com/newspage.html
		 */
		String host = "freetext.usenetserver.com";

		String newsgroup = "soc.senior.issues";
		if (args.length > 0) {
			newsgroup = args[0];
		}
		System.out.println("Look for " + newsgroup + " on " + host);

		try {
			NewsGroupReader reader = new NewsGroupReader(null);
			reader.connectToNewsGroup(host, newsgroup);
			reader.startDownload();
			reader.closeConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("done");

	}

	private boolean connected = false;

	protected Folder folder = null;

	private String host;

	private LinkedBlockingQueue<NewsGroupArticle> messageQueue;

	ArrayList<Message> msgs;

	private int numberOfMessages = -1;

	protected Store store = null;

	private UNISoNController controller;

	public NewsGroupReader(UNISoNController controller) {
		messageQueue = new LinkedBlockingQueue<NewsGroupArticle>();
		workers = new ArrayList<SwingWorker>();
		downloaders = new ArrayList<DownloadWorker>();
		this.controller = controller;
		// controller = UNISonController.getInstance();
	}

	public LinkedBlockingQueue<NewsGroupArticle> getQueue() {
		return messageQueue;
	}

	public void closeConnection() throws MessagingException {
		if (isConnected()) {
			folder.close(false);
			store.close();
		}
		connected = false;
	}

	public void connectToNewsGroup(String host, NewsGroup newsgroup)
			throws NoSuchProviderException, MessagingException,
			UnknownHostException {

		this.host = host;
		String username = null;
		String password = null;

		// Create empty properties
		Properties props = new Properties();

		// Get session
		Session session = Session.getInstance(props, null);

		// Get the store
		store = session.getStore("nntp");
		try {
			store.connect(host, username, password);
			// Get folder
			folder = store.getFolder(newsgroup.getFullName());
			folder.open(Folder.READ_ONLY);
			connected = true;
		} catch (javax.mail.FolderNotFoundException e) {
			throw e;
		} catch (javax.mail.MessagingException me) {
			Throwable ex = me.getNextException();
			if (ex instanceof java.net.UnknownHostException) {
				throw (java.net.UnknownHostException) ex;
			}
			throw me;
		}

	};

	int msgsStored = 0;

	int msgsSkipped = 0;

	public synchronized void incrementMessagesSkipped() {
		msgsSkipped++;
	}

	public synchronized void incrementMessagesStored() {
		msgsStored++;
	}

	public int getMessagesSkipped() {
		return msgsSkipped;
	}

	public int getMessagesStored() {
		return msgsStored;
	}

	ArrayList<SwingWorker> workers;

	private ArrayList<DownloadWorker> downloaders;

	public void startDownload() {
		logger.debug("startDownload");

		try {
			messageCount = folder.getMessageCount();

			int numberOfDownloaders = 40;
			int numberofHibernators = 1;
			for (int i = 0; i < numberOfDownloaders; i++) {
				downloaders.add(new DownloadWorker(this, numberOfDownloaders,
						i, folder, messageCount));
			}
			for (int i = 0; i < numberofHibernators; i++) {
				workers.add(new DataHibernatorWorker(this));
			}
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int messageCount;

	public synchronized void showDownloadStatus() {
		int i = getMessagesSkipped() + getMessagesStored();

		int progress = (i * 100) / messageCount;
		UNISoNController.getInstance().setDownloadingState(progress);

		controller.showStatus("Downloaded: " + i + "/" + messageCount
				+ "\rAdded: " + getMessagesStored() + "\n Skipped:"
				+ getMessagesSkipped() + " queued " + getQueue().size());

	}

	public void stopDownload() {
		for (ListIterator<SwingWorker> iter = workers.listIterator(); iter
				.hasNext();) {
			iter.next().interrupt();
		}
		UNISoNController.getInstance().setIdleState();
	}

	public void connectToNewsGroup(String host, String newsgroup)
			throws NoSuchProviderException, UnknownHostException,
			MessagingException {
		NewsGroup group = HibernateHelper.findOrCreateNewsGroup(HibernateHelper
				.getHibernateSession(), newsgroup);
		connectToNewsGroup(host, group);
	}

	public Folder getFolder() {
		return folder;
	}

	public int getNumberOfMessages() {
		numberOfMessages = -1;
		if (folder != null) {
			numberOfMessages = -1;
			try {
				numberOfMessages = folder.getMessageCount();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
		return numberOfMessages;
	}

	public boolean isConnected() {
		return connected;
	}

	public void reconnect() throws MessagingException {
		// If it should be connected but has timed out
		if (isConnected() && !store.isConnected()) {
			store.connect(host, null, null);
		}
	}

	// public void storeMessages() throws MessagingException {
	// if (isConnected()) {
	// // Message message[] = getMessages();
	// System.out.println("Msgs: " + folder.getMessageCount());
	// for (int i = 0; i < folder.getMessageCount(); i++) {
	//
	// NewsGroupArticle article = new NewsGroupArticle(folder
	// .getMessage(i));
	//
	// // store message in queue
	// messageQueue.add(article);
	// writeMessageToDatabase();
	// }
	// }
	// }

	private void writeMessageToDatabase() {
		// write to database
		NewsGroupArticle article2 = (NewsGroupArticle) messageQueue.poll();
		HibernateHelper.hibernateData(article2);
	}

	public void setDownloaderFinished(DownloadWorker worker) {

	}

}
