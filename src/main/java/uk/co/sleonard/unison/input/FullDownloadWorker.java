/**
 * FullDownloadWorker
 *
 * @author Stephen <github@leonarduk.com>
 * @since 22-May-2016
 */
package uk.co.sleonard.unison.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.net.MalformedServerReplyException;

import uk.co.sleonard.unison.UNISoNControllerFX;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.UNISoNLogger;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.utils.StringUtils;

/**
 * Class to create a separate Thread for downloading messages.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
public class FullDownloadWorker extends SwingWorker {

	/** The download queue. */
	private static LinkedBlockingQueue<DownloadRequest> downloadQueue = new LinkedBlockingQueue<>();

	/** The log. */
	private static UNISoNLogger log;

	/** The Constant downloaders. */
	private final static ArrayList<FullDownloadWorker> downloaders = new ArrayList<>();

	/** The client. */
	private final NewsClient client;

	/** The download. */
	private boolean download = true;

	/**
	 * Adds the download request.
	 *
	 * @Deprecated move to DownloaderImpl if we can
	 * @param usenetID
	 *            the usenet id
	 * @param mode
	 *            the mode
	 * @param log1
	 *            the log
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */

	public synchronized static void addDownloadRequest(final String usenetID,
	        final DownloadMode mode, final UNISoNLogger log1) throws UNISoNException {
		final DownloadRequest request = new DownloadRequest(usenetID, mode);

		FullDownloadWorker.log = log1;
		FullDownloadWorker.downloadQueue.add(request);
		if (FullDownloadWorker.downloaders.size() < 1) {
			FullDownloadWorker.startDownloaders(1);
		}
		DataHibernatorWorker.startHibernators();
	}

	/**
	 * Queue size.
	 *
	 * @return the int
	 */
	public static int queueSize() {
		return FullDownloadWorker.downloadQueue.size();
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
			final String host = UNISoNControllerFX.getInstance().getNntpHost();
			FullDownloadWorker.downloaders
			        .add(new FullDownloadWorker(host, UNISoNControllerFX.getInstance().getQueue()));
		}
	}

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
	public FullDownloadWorker(final String server, final LinkedBlockingQueue<NewsArticle> outQueue)
	        throws UNISoNException {
		super("FullDownload");
		this.client = new NewsClientImpl();
		try {
			this.client.connect(server);
		}
		catch (final IOException e) {
			throw new UNISoNException(e);
		}

		this.start();
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
			while (this.download) {
				while (FullDownloadWorker.downloadQueue.size() > 0) {
					this.storeNextMessage();
				}
				// Wait a bit. If no messages then close downloader
				Thread.sleep(5000);
				if ((FullDownloadWorker.downloadQueue.size() == 0)
				        && (FullDownloadWorker.downloaders.size() > 1)) {
					this.download = false;
				}
			}
		}
		catch (final InterruptedException e) {
			return "Interrupted";
		}
		catch (final UNISoNException e) {
			UNISoNControllerFX.getInstance();
			UNISoNControllerFX.getGui().showAlert("Error in download:" + e);
			e.printStackTrace();
			return "FAIL";
		}
		return "Completed";
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
		final HashMap<String, String> headerFields = new HashMap<>();

		final StringTokenizer st = new StringTokenizer(theInfo, "\n");
		String key = null;
		String value = null;

		// Skip these as they can contain illegal characters and we don't need
		// them anyway
		final List<String> ignoreList = new ArrayList<>();
		ignoreList.add("X-FACE");
		ignoreList.add("FACE");
		ignoreList.add("CANCEL-LOCK");

		boolean body = false;
		final StringBuffer bodyBuffer = new StringBuffer();

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
			date = StringUtils.stringToDate(headerFields.get("DATE"));
		}
		catch (final DateTimeParseException e) {
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
		return null;
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
	public NewsArticle downloadArticle(final DownloadRequest request) throws UNISoNException {
		NewsArticle article = null;
		try {
			switch (request.getMode()) {
				case HEADERS:
					article = this.downloadHeader(request);
					break;
				case ALL:
					article = this.downloadFullMessage(request);
					break;
				case BASIC:
					break;
				default:
					break;
			}
		}
		catch (final MalformedServerReplyException e) {
			throw new UNISoNException("Failed to download message:" + e.getMessage(), e);
		}
		catch (final Throwable e) {
			throw new UNISoNException("Failed to download message:", e);
		}

		if (null == article) {
			FullDownloadWorker.log.log("Skipped message " + request.getUsenetID());
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
	public NewsArticle downloadFullMessage(final DownloadRequest request)
	        throws IOException, UNISoNException {
		try (Reader reader = this.client.retrieveArticle(request.getUsenetID());) {
			NewsArticle article = null;
			if (null != reader) {
				article = this.convertReaderToArticle(reader);
			}
			else {
				System.err.println("No message returned");
			}
			return article;
		}
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
	private NewsArticle downloadHeader(final DownloadRequest request)
	        throws IOException, UNISoNException {
		try (Reader reader = this.client.retrieveArticleHeader(request.getUsenetID());) {
			NewsArticle article = null;
			if (null != reader) {
				article = this.convertReaderToArticle(reader);
			}
			else {
				System.err.println("No message returned");
			}
			return article;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see uk.co.sleonard.unison.input.SwingWorker#finished()
	 */
	@Override
	public void finished() {
		FullDownloadWorker.downloaders.remove(this);
		if (FullDownloadWorker.downloaders.size() == 0) {
			FullDownloadWorker.log.alert("Download of extra fields complete");
		}
	}

	/**
	 * Poll queue.
	 *
	 * @return the download request
	 */
	private synchronized DownloadRequest pollQueue() {
		final DownloadRequest request = FullDownloadWorker.downloadQueue.poll();
		return request;
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
	 * Store next message.
	 *
	 * @return true, if successful
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	private boolean storeNextMessage() throws UNISoNException {
		final DownloadRequest request = this.pollQueue();

		final NewsArticle article = this.downloadArticle(request);
		if (null != article) {
			final LinkedBlockingQueue<NewsArticle> queue = UNISoNControllerFX.getInstance()
			        .getQueue();

			FullDownloadWorker.log.log("Got:" + article.getSubject() + " " + article.getFrom() + " "
			        + article.getArticleId() + " to q: " + queue.size() + "[" + new Date());

			queue.add(article);
			return true;
		}
		return false;
	}
}
