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
public class HeaderDownloadWorker extends SwingWorker {

	/** The logger. */
	private static Logger logger = Logger.getLogger(HeaderDownloadWorker.class);

	// private static final int MAX_DEPTH = 0;

	/** The end index. */
	private int endIndex;

	// private int messageCount;

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

	/**
	 * Checks if is downloading.
	 *
	 * @return true, if is downloading
	 */
	public boolean isDownloading() {
		return downloading;
	}

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
	 * Given an {@link NNTPClient} instance, and an integer range of messages,
	 * return an array of {@link Article} instances.
	 *
	 * @return Article[] An array of Article
	 * @throws UNISoNException the UNI so n exception
	 */
	public boolean storeArticleInfo() throws UNISoNException {
		Reader reader = null;
		try {
			logTally = 0;
			index = 0;
			skipped = 0;
			kept = 0;

			// fetch back 500 messages at a time
			for (int i = startIndex; i < endIndex; i += 500) {
				reader = newsReader.client.retrieveArticleInfo(i, i + 500);
				queueMessages(reader);
			}

		} catch (IOException e1) {
			log.alert("ERROR: " + e1);
			e1.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Queue messages.
	 *
	 * @param reader the reader
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws UNISoNException the UNI so n exception
	 */
	private boolean queueMessages(Reader reader) throws IOException, UNISoNException {
		if (reader != null) {

			final BufferedReader bufReader = new BufferedReader(reader);
			LinkedBlockingQueue<NewsArticle> queue = UNISoNController.getInstance().getQueue();

			for (String line = bufReader.readLine(); line != null; line = bufReader.readLine()) {
				if (!running) {
					log.alert("Download aborted");
					notifyObservers();
					throw new UNISoNException("Download aborted");
				}
				// If told to pause or queue is getting a bit full wait
				while (!downloading || queue.size() > 1000) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// Extract the article information
				// Mandatory format (from NNTP RFC 2980) is :
				// Subject\tAuthor\tDate\tID\tReference(s)\tByte Count\tLine
				// Count
				final StringTokenizer stt = new StringTokenizer(line, "\t");
				int articleNumber = Integer.parseInt(stt.nextToken());
				String subject = stt.nextToken();
				String from = stt.nextToken();
				Date date;
				try {
					date = HttpDateObject.getParser().parseDate(stt.nextToken());
				} catch (ParseException e) {
					throw new UNISoNException(e);
				}
				String articleId = stt.nextToken();
				String references = stt.nextToken();
				if (!references.contains("@")) {
					references = "";
				}

				if (inDateRange(fromDate, toDate, date)) {
					String postingHost = null;
					String content = null;

					final NewsArticle article = new NewsArticle(articleId, articleNumber, date, from, subject,
							references, content, newsgroup, postingHost);

					queue.add(article);
					if (!mode.equals(DownloadMode.BASIC)) {
						FullDownloadWorker.addDownloadRequest(articleId, mode, log);
					}
					kept++;
				} else {
					skipped++;
				}
				index++;
				if (++logTally == 100) {
					int size = queue.size();
					if (!mode.equals(DownloadMode.BASIC)) {
						size += FullDownloadWorker.queueSize();
					}
					log.log("Downloaded " + index + " kept " + kept + " skipped " + skipped + " to process: " + size
							+ " [" + new Date() + "]");
					logTally = 0;
				}
			}
			notifyObservers();
			int size = queue.size();
			if (!mode.equals(DownloadMode.BASIC)) {
				size += FullDownloadWorker.queueSize();
			}
			log.log("Downloaded " + index + " kept " + kept + " to process: " + size);
		}
		return true;
	}

	/**
	 * Method to stop all downloading and end the thread.
	 */
	public void fullstop() {
		running = false;
		downloading = false;
		try {
			if (null != newsReader.client) {
				newsReader.client.quit();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		notifyObservers();
	}

	/* (non-Javadoc)
	 * @see java.util.Observable#notifyObservers()
	 */
	@Override
	public void notifyObservers() {
		setChanged();
		super.notifyObservers();
	}

	/**
	 * Instantiates a new header download worker.
	 */
	public HeaderDownloadWorker() {
		super("HeaderDownloader");
		running = true;
		downloading = false;
		this.start();
	}

	/**
	 * Initialise.
	 *
	 * @param reader the reader
	 * @param startIndex the start index
	 * @param endIndex the end index
	 * @param server the server
	 * @param newsgroup the newsgroup
	 * @param log the log
	 * @param mode the mode
	 * @param from the from
	 * @param to the to
	 * @throws UNISoNException the UNI so n exception
	 */
	public void initialise(final NewsGroupReader reader, final int startIndex, final int endIndex, final String server,
			String newsgroup, UNISoNLogger log, DownloadMode mode, Date from, Date to) throws UNISoNException {
		this.log = log;
		this.mode = mode;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.newsReader = reader;
		this.newsgroup = newsgroup;
		this.fromDate = from;
		this.toDate = to;

		logger.info(" Server: " + server + " Newsgroup: " + newsgroup);
		try {
			this.newsReader.client.connect(server);
			this.newsReader.client.selectNewsgroup(newsgroup);
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			throw new UNISoNException("Failed to initialise downloader", e);
		}

		this.downloading = true;
		logger.info("Creating " + this.getClass() + " " + reader.getNumberOfMessages());
	}

	/**
	 * In date range.
	 *
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param date the date
	 * @return true, if successful
	 */
	private boolean inDateRange(Date fromDate, Date toDate, Date date) {
		if (null == date) {
			return false;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		if (null != fromDate) {
			Calendar from = Calendar.getInstance();
			from.setTime(fromDate);
			// zeroHours(from);

			if (cal.before(from)) {
				return false;
			}
		}
		if (null != toDate) {
			Calendar to = Calendar.getInstance();
			to.setTime(toDate);

			// add a day to allow for time past midnight
			to.add(Calendar.DAY_OF_MONTH, 1);
			// zeroHours(to);

			if (cal.after(to)) {
				return false;
			}
		}
		return true;
	}

	// private void zeroHours(Calendar cal) {
	// // want to zero these to improve comparison
	// cal.set(Calendar.HOUR_OF_DAY, 0);
	// cal.set(Calendar.MINUTE, 0);
	// cal.set(Calendar.SECOND, 0);
	//
	// }

	/* (non-Javadoc)
	 * @see uk.co.sleonard.unison.input.SwingWorker#construct()
	 */
	@Override
	public Object construct() {

		while (running) {
			if (downloading) {

				try {
					this.storeArticleInfo();
				} catch (UNISoNException e) {
					log.alert("ERROR:" + e);
					e.printStackTrace();
					return ("FAIL");
				}
				downloading = false;
				notifyObservers();
			}

		}
		return ("Completed");
	}

	/* (non-Javadoc)
	 * @see uk.co.sleonard.unison.input.SwingWorker#finished()
	 */
	@Override
	public void finished() {
		downloading = false;
		notifyObservers();
	}

	/**
	 * Resume.
	 */
	public void resume() {
		downloading = true;
	}

	/**
	 * Pause.
	 */
	public void pause() {
		downloading = false;
	}
}
