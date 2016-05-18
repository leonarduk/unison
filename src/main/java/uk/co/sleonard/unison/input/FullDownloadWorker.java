package uk.co.sleonard.unison.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.net.MalformedServerReplyException;

import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.gui.UNISoNController;
import uk.co.sleonard.unison.gui.UNISoNException;
import uk.co.sleonard.unison.gui.UNISoNLogger;
import uk.co.sleonard.unison.utils.HttpDateObject;

/**
 * Class to create a separate Thread for downloading messages.
 *
 * @author steve
 */
public class FullDownloadWorker extends SwingWorker {

	/** The client. */
	private final NewsClient client;

	/** The start index. */
	private static int startIndex = 0;

	/** The download. */
	private boolean download = true;

	/**
	 * Instantiates a new full download worker.
	 *
	 * @param server
	 *            the server
	 * @param outQueue
	 *            the out queue
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	public FullDownloadWorker(final String server, LinkedBlockingQueue<NewsArticle> outQueue)
	        throws UNISoNException {
		super("FullDownload");
		startIndex++;
		this.client = new NewsClient();
		try {
			this.client.connect(server);
		}
		catch (IOException e) {
			throw new UNISoNException(e);
		}

		this.start();
	}

	/** The download queue. */
	private static LinkedBlockingQueue<DownloadRequest> downloadQueue = new LinkedBlockingQueue<DownloadRequest>();

	/** The log. */
	private static UNISoNLogger log;

	/**
	 * Adds the download request.
	 *
	 * @param usenetID
	 *            the usenet id
	 * @param mode
	 *            the mode
	 * @param log
	 *            the log
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	public synchronized static void addDownloadRequest(String usenetID, DownloadMode mode,
	        UNISoNLogger log) throws UNISoNException {
		DownloadRequest request = new DownloadRequest(usenetID, mode);

		FullDownloadWorker.log = log;
		downloadQueue.add(request);
		if (downloaders.size() < 1) {
			startDownloaders(1);
		}
		DataHibernatorWorker.startHibernators();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.sleonard.unison.input.SwingWorker#construct()
	 */
	@Override
	public Object construct() {

		System.out.println("construct");

		try {
			while (download) {
				while (downloadQueue.size() > 0) {
					storeNextMessage();
				}
				// Wait a bit. If no messages then close downloader
				Thread.sleep(5000);
				if (downloadQueue.size() == 0 && downloaders.size() > 1) {
					download = false;
				}
			}
		}
		catch (final InterruptedException e) {
			return ("Interrupted");
		}
		catch (UNISoNException e) {
			UNISoNController.getInstance().showAlert("Error in download:" + e);
			e.printStackTrace();
			return ("FAIL");
		}
		return ("Completed");
	}

	/**
	 * Convert header string to article.
	 *
	 * @param theInfo
	 *            the the info
	 * @return the news article
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	private NewsArticle convertHeaderStringToArticle(final String theInfo) throws UNISoNException {
		final HashMap<String, String> headerFields = new HashMap<String, String>();

		final StringTokenizer st = new StringTokenizer(theInfo, "\n");
		String key = null;
		String value = null;

		// Skip these as they can contain illegal characters and we don't need
		// them anyway
		final List<String> ignoreList = new ArrayList<String>();
		ignoreList.add("X-FACE");
		ignoreList.add("FACE");
		ignoreList.add("CANCEL-LOCK");

		boolean body = false;
		StringBuffer bodyBuffer = new StringBuffer();

		while (st.hasMoreTokens()) {
			final String row = st.nextToken();

			if (row.startsWith(" ")) {
				value += "," + row;
			}
			else {
				if ((null != key) && !ignoreList.contains(key)) {
					headerFields.put(key, value);
				}
				final int indexOf = row.indexOf(":");
				if (indexOf > -1) {
					key = row.substring(0, indexOf);
				}
				else {
					value += "," + row;
				}

				// System.out.println("KEY: " + key + " ROW:" + row);

				// this is the message body
				if (key.equalsIgnoreCase("X-Received-Date")) {
					body = true;
				}
				else if (body) {
					bodyBuffer.append(row + "\n");
				}
				if ((null != key) && !ignoreList.contains(key)) {
					value = row.replaceFirst(key + ":", "").trim();
				}
			}
			if (null != key) {
				// Store with upper case as NNTP servers differs
				headerFields.put(key.toUpperCase(), value);
			}
		}
		// System.out.println("MAP:" + headerFields);
		final int messageNumber = -1;
		final String references = headerFields.get("REFERENCES");
		Date date;
		try {
			date = HttpDateObject.getParser().parse(headerFields.get("DATE"));
		}
		catch (ParseException e) {
			throw new UNISoNException(e);
		}

		final NewsArticle article = new NewsArticle(headerFields.get("MESSAGE-ID"), messageNumber,
		        date, headerFields.get("FROM"), headerFields.get("SUBJECT"), references,
		        bodyBuffer.toString(), headerFields.get("NEWSGROUPS"),
		        headerFields.get("NNTP-POSTING-HOST"));

		if (body) {
			article.setContent(bodyBuffer.toString());
		}
		return article;
	}

	/**
	 * Convert reader to article.
	 *
	 * @param reader
	 *            the reader
	 * @return the news article
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	private NewsArticle convertReaderToArticle(final Reader reader) throws UNISoNException {
		if (reader != null) {
			final String theInfo = this.readerToString(reader);
			return this.convertHeaderStringToArticle(theInfo);

		}
		else {
			return null;
		}
	}

	/**
	 * Download article.
	 *
	 * @param request
	 *            the request
	 * @return the news article
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	public NewsArticle downloadArticle(DownloadRequest request) throws UNISoNException {
		NewsArticle article = null;
		try {
			switch (request.getMode()) {
				case HEADERS:
					article = downloadHeader(request);
					break;
				case ALL:
					article = downloadFullMessage(request);
					break;
			}
		}
		catch (MalformedServerReplyException e) {
			throw new UNISoNException("Failed to download message:" + e.getMessage(), e);
		}
		catch (Throwable e) {
			throw new UNISoNException("Failed to download message:", e);
		}

		if (null == article) {
			log.log("Skipped message " + request.getUsenetID());
		}
		return article;
	}

	/**
	 * Download full message.
	 *
	 * @param request
	 *            the request
	 * @return the news article
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	public NewsArticle downloadFullMessage(DownloadRequest request)
	        throws IOException, UNISoNException {
		Reader reader;
		reader = this.client.retrieveArticle(request.getUsenetID());
		NewsArticle article = null;
		if (null != reader) {
			article = this.convertReaderToArticle(reader);
		}
		else {
			System.err.println("No message returned");
		}
		return article;
	}

	/**
	 * Download header.
	 *
	 * @param request
	 *            the request
	 * @return the news article
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	private NewsArticle downloadHeader(DownloadRequest request)
	        throws IOException, UNISoNException {
		Reader reader;
		reader = this.client.retrieveArticleHeader(request.getUsenetID());
		NewsArticle article = null;
		if (null != reader) {
			article = this.convertReaderToArticle(reader);
		}
		else {
			System.err.println("No message returned");
		}
		return article;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.sleonard.unison.input.SwingWorker#finished()
	 */
	@Override
	public void finished() {
		downloaders.remove(this);
		if (downloaders.size() == 0) {
			log.alert("Download of extra fields complete");
		}
	}

	/**
	 * Convert a {@link Reader} instance to a String.
	 *
	 * @param reader
	 *            The Reader instance
	 * @return String
	 */
	public String readerToString(final Reader reader) {
		String temp = null;
		StringBuffer sb = null;
		final BufferedReader bufReader = new BufferedReader(reader);

		sb = new StringBuffer();
		try {
			temp = bufReader.readLine();
			while (temp != null) {
				sb.append(temp);
				sb.append("\n");
				temp = bufReader.readLine();
			}
		}
		catch (final IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	/**
	 * Start downloaders.
	 *
	 * @param numberOfDownloaders
	 *            the number of downloaders
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	public static void startDownloaders(final int numberOfDownloaders) throws UNISoNException {

		for (int i = 0; i < numberOfDownloaders; i++) {
			String host = UNISoNController.getInstance().getNntpHost();
			FullDownloadWorker.downloaders
			        .add(new FullDownloadWorker(host, UNISoNController.getInstance().getQueue()));
		}
	}

	/** The Constant downloaders. */
	private final static ArrayList<FullDownloadWorker> downloaders = new ArrayList<FullDownloadWorker>();

	/**
	 * Store next message.
	 *
	 * @return true, if successful
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	private boolean storeNextMessage() throws UNISoNException {
		DownloadRequest request = pollQueue();

		final NewsArticle article = this.downloadArticle(request);
		if (null != article) {
			LinkedBlockingQueue<NewsArticle> queue = UNISoNController.getInstance().getQueue();

			log.log("Got:" + article.getSubject() + " " + article.getFrom() + " "
			        + article.getArticleId() + " to q: " + queue.size() + "[" + new Date());

			queue.add(article);
			return true;
		}
		return false;
	}

	/**
	 * Poll queue.
	 *
	 * @return the download request
	 */
	private synchronized DownloadRequest pollQueue() {
		DownloadRequest request = downloadQueue.poll();
		return request;
	}

	/**
	 * Queue size.
	 *
	 * @return the int
	 */
	public static int queueSize() {
		return downloadQueue.size();
	}
}
