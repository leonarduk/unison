/**
 * HeaderDownloadWorker
 *
 * @author Stephen <github@leonarduk.com>
 * @since 22-May-2016
 */
package uk.co.sleonard.unison.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.net.nntp.Article;
import org.apache.commons.net.nntp.NNTPClient;
import org.apache.log4j.Logger;

import uk.co.sleonard.unison.UNISoNController;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.UNISoNLogger;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.utils.HttpDateObject;

/**
 * Class to create a separate Thread for downloading messages.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
public class HeaderDownloadWorker extends SwingWorker {

	/** The logger. */
	private static Logger logger = Logger.getLogger(HeaderDownloadWorker.class);

	/** The end index. */
	private int endIndex;

	/** The news reader. */
	private NewsGroupReader newsReader = null;

	/** The start index. */
	private int startIndex;

	/** The newsgroup. */
	private String newsgroup;

	/** The from date. */
	private Date fromDate;

	/** The to date. */
	private Date toDate;

	/** The log. */
	private UNISoNLogger log;

	/** The downloading. */
	private boolean downloading = false;

	/** The running. */
	private boolean running = true;

	/** The log tally. */
	private int logTally = 0;

	/** The index. */
	private int index = 0;

	/** The skipped. */
	private int skipped = 0;

	/** The kept. */
	private int kept = 0;

	/** The mode. */
	private DownloadMode mode;

	/**
	 * Instantiates a new header download worker.
	 */
	public HeaderDownloadWorker() {
		super("HeaderDownloader");
		this.running = true;
		this.downloading = false;
		this.start();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see uk.co.sleonard.unison.input.SwingWorker#construct()
	 */
	@Override
	public Object construct() {

		while (this.running) {
			if (this.downloading) {

				try {
					this.storeArticleInfo();
				}
				catch (final UNISoNException e) {
					this.log.alert("ERROR:" + e);
					e.printStackTrace();
					return "FAIL";
				}
				this.downloading = false;
				this.notifyObservers();
			}

		}
		return "Completed";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see uk.co.sleonard.unison.input.SwingWorker#finished()
	 */
	@Override
	public void finished() {
		this.downloading = false;
		this.notifyObservers();
	}

	/**
	 * Method to stop all downloading and end the thread.
	 */
	public void fullstop() {
		this.running = false;
		this.downloading = false;
		try {
			if (null != this.newsReader.client) {
				this.newsReader.client.quit();
			}
		}
		catch (final IOException e) {
			e.printStackTrace();
		}
		this.notifyObservers();
	}

	/**
	 * In date range.
	 *
	 * @param fromDate1
	 *            the from date
	 * @param toDate1
	 *            the to date
	 * @param date
	 *            the date
	 * @return true, if successful
	 */
	private boolean inDateRange(final Date fromDate1, final Date toDate1, final Date date) {
		if (null == date) {
			return false;
		}
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		if (null != fromDate1) {
			final Calendar from = Calendar.getInstance();
			from.setTime(fromDate1);
			// zeroHours(from);

			if (cal.before(from)) {
				return false;
			}
		}
		if (null != toDate1) {
			final Calendar to = Calendar.getInstance();
			to.setTime(toDate1);

			// add a day to allow for time past midnight
			to.add(Calendar.DAY_OF_MONTH, 1);
			// zeroHours(to);

			if (cal.after(to)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Initialise.
	 *
	 * @param reader
	 *            the reader
	 * @param startIndex1
	 *            the start index
	 * @param endIndex1
	 *            the end index
	 * @param server
	 *            the server
	 * @param newsgroup1
	 *            the newsgroup
	 * @param log1
	 *            the log
	 * @param mode1
	 *            the mode
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	public void initialise(final NewsGroupReader reader, final int startIndex1, final int endIndex1,
	        final String server, final String newsgroup1, final UNISoNLogger log1,
	        final DownloadMode mode1, final Date from, final Date to) throws UNISoNException {
		this.log = log1;
		this.mode = mode1;
		this.startIndex = startIndex1;
		this.endIndex = endIndex1;
		this.newsReader = reader;
		this.newsgroup = newsgroup1;
		this.fromDate = from;
		this.toDate = to;

		HeaderDownloadWorker.logger.info(" Server: " + server + " Newsgroup: " + newsgroup1);
		try {
			this.newsReader.client.connect(server);
			this.newsReader.client.selectNewsgroup(newsgroup1);
		}
		catch (final Exception e) {
			HeaderDownloadWorker.logger.warn(e.getMessage(), e);
			throw new UNISoNException("Failed to initialise downloader", e);
		}

		this.downloading = true;
		HeaderDownloadWorker.logger
		        .info("Creating " + this.getClass() + " " + reader.getNumberOfMessages());
	}

	/**
	 * Checks if is downloading.
	 *
	 * @return true, if is downloading
	 */
	public boolean isDownloading() {
		return this.downloading;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Observable#notifyObservers()
	 */
	@Override
	public void notifyObservers() {
		this.setChanged();
		super.notifyObservers();
	}

	// private void zeroHours(Calendar cal) {
	// // want to zero these to improve comparison
	// cal.set(Calendar.HOUR_OF_DAY, 0);
	// cal.set(Calendar.MINUTE, 0);
	// cal.set(Calendar.SECOND, 0);
	//
	// }

	/**
	 * Pause.
	 */
	public void pause() {
		this.downloading = false;
	}

	/**
	 * Queue messages.
	 *
	 * @param reader
	 *            the reader
	 * @return true, if successful
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	private boolean queueMessages(final Reader reader) throws IOException, UNISoNException {
		if (reader != null) {

			final BufferedReader bufReader = new BufferedReader(reader);
			final LinkedBlockingQueue<NewsArticle> queue = UNISoNController.getInstance()
			        .getQueue();

			for (String line = bufReader.readLine(); line != null; line = bufReader.readLine()) {
				if (!this.running) {
					this.log.alert("Download aborted");
					this.notifyObservers();
					throw new UNISoNException("Download aborted");
				}
				// If told to pause or queue is getting a bit full wait
				while (!this.downloading || (queue.size() > 1000)) {
					try {
						Thread.sleep(1000);
					}
					catch (final InterruptedException e) {
						e.printStackTrace();
					}
				}

				// Extract the article information
				// Mandatory format (from NNTP RFC 2980) is :
				// Subject\tAuthor\tDate\tID\tReference(s)\tByte Count\tLine
				// Count
				final StringTokenizer stt = new StringTokenizer(line, "\t");
				final int articleNumber = Integer.parseInt(stt.nextToken());
				final String subject = stt.nextToken();
				final String from = stt.nextToken();
				Date date;
				try {
					date = HttpDateObject.getParser().parseDate(stt.nextToken());
				}
				catch (final ParseException e) {
					throw new UNISoNException(e);
				}
				final String articleId = stt.nextToken();
				String references = stt.nextToken();
				if (!references.contains("@")) {
					references = "";
				}

				if (this.inDateRange(this.fromDate, this.toDate, date)) {
					final String postingHost = null;
					final String content = null;

					final NewsArticle article = new NewsArticle(articleId, articleNumber, date,
					        from, subject, references, content, this.newsgroup, postingHost);

					queue.add(article);
					if (!this.mode.equals(DownloadMode.BASIC)) {
						FullDownloadWorker.addDownloadRequest(articleId, this.mode, this.log);
					}
					this.kept++;
				}
				else {
					this.skipped++;
				}
				this.index++;
				this.logTally++;
				if (this.logTally == 100) {
					int size = queue.size();
					if (!this.mode.equals(DownloadMode.BASIC)) {
						size += FullDownloadWorker.queueSize();
					}
					this.log.log("Downloaded " + this.index + " kept " + this.kept + " skipped "
					        + this.skipped + " to process: " + size + " [" + new Date() + "]");
					this.logTally = 0;
				}
			}
			this.notifyObservers();
			int size = queue.size();
			if (!this.mode.equals(DownloadMode.BASIC)) {
				size += FullDownloadWorker.queueSize();
			}
			this.log.log(
			        "Downloaded " + this.index + " kept " + this.kept + " to process: " + size);
		}
		return true;
	}

	/**
	 * Resume.
	 */
	public void resume() {
		this.downloading = true;
	}

	/**
	 * Given an {@link NNTPClient} instance, and an integer range of messages, return an array of
	 * {@link Article} instances.
	 *
	 * @return Article[] An array of Article
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	public boolean storeArticleInfo() throws UNISoNException {
		try {
			this.logTally = 0;
			this.index = 0;
			this.skipped = 0;
			this.kept = 0;

			// fetch back 500 messages at a time
			for (int i = this.startIndex; i < this.endIndex; i += 500) {
				try (final Reader reader = this.newsReader.client.retrieveArticleInfo(
				        Long.valueOf(i).longValue(), Long.valueOf(i + 500).longValue());) {
					this.queueMessages(reader);
				}
			}
		}
		catch (final IOException e1) {
			this.log.alert("ERROR: " + e1);
			e1.printStackTrace();
			return false;
		}

		return true;
	}
}
