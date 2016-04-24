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

import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.gui.UNISoNController;
import uk.co.sleonard.unison.gui.UNISoNException;
import uk.co.sleonard.unison.gui.UNISoNLogger;
import uk.co.sleonard.unison.utils.HttpDateObject;

/**
 * Class to create a separate Thread for downloading messages
 * 
 * @author steve
 * 
 */
public class HeaderDownloadWorker extends SwingWorker {

	// private static Logger logger = Logger.getLogger("DownloadWorker");

	// private static final int MAX_DEPTH = 0;

	private int endIndex;

	// private int messageCount;

	private NewsGroupReader newsReader = null;

	private int startIndex;

	private String newsgroup;

	private Date fromDate;

	private Date toDate;

	private UNISoNLogger log;

	private boolean downloading = false;

	public boolean isDownloading() {
		return downloading;
	}

	private boolean running = true;

	private int logTally = 0;

	private int index = 0;

	private int skipped = 0;

	private int kept = 0;

	private DownloadMode mode;

	/**
	 * Given an {@link NNTPClient} instance, and an integer range of messages,
	 * return an array of {@link Article} instances.
	 * 
	 * @param newsGroupReader
	 * @param lowArticleNumber
	 * @param highArticleNumber
	 * @param newsgroup
	 * @param toDate
	 * @param fromDate
	 * @param log
	 * @return Article[] An array of Article
	 * @throws UNISoNException
	 * @throws IOException
	 * @throws UNISoNException
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

	private boolean queueMessages(Reader reader) throws IOException,
			UNISoNException {
		if (reader != null) {

			final BufferedReader bufReader = new BufferedReader(reader);
			LinkedBlockingQueue<NewsArticle> queue = UNISoNController
					.getInstance().getQueue();

			for (String line = bufReader.readLine(); line != null; line = bufReader
					.readLine()) {
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
					date = HttpDateObject.getParser()
							.parseDate(stt.nextToken());
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

					final NewsArticle article = new NewsArticle(articleId,
							articleNumber, date, from, subject, references,
							content, newsgroup, postingHost);

					queue.add(article);
					if (!mode.equals(DownloadMode.BASIC)) {
						FullDownloadWorker.addDownloadRequest(articleId, mode,
								log);
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
					log.log("Downloaded " + index + " kept " + kept
							+ " skipped " + skipped + " to process: " + size
							+ " [" + new Date() + "]");
					logTally = 0;
				}
			}
			notifyObservers();
			int size = queue.size();
			if (!mode.equals(DownloadMode.BASIC)) {
				size += FullDownloadWorker.queueSize();
			}
			log.log("Downloaded " + index + " kept " + kept + " to process: "
					+ size);
		}
		return true;
	}

	/**
	 * Method to stop all downloading and end the thread
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

	@Override
	public void notifyObservers() {
		setChanged();
		super.notifyObservers();
	}

	public HeaderDownloadWorker() {
		super("HeaderDownloader");
		running = true;
		downloading = false;
		this.start();
	}

	public void initialise(final NewsGroupReader reader, final int startIndex,
			final int endIndex, final String server, String newsgroup,
			UNISoNLogger log, DownloadMode mode, Date from, Date to)
			throws UNISoNException {
		this.log = log;
		this.mode = mode;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.newsReader = reader;
		this.newsgroup = newsgroup;
		this.fromDate = from;
		this.toDate = to;

		try {
			this.newsReader.client.connect(server);
			this.newsReader.client.selectNewsgroup(newsgroup);
		} catch (IOException e) {
			throw new UNISoNException("Failed to initialise downloader", e);
		} catch (Exception e) {
			throw new UNISoNException("Failed to initialise downloader", e);
		}

		this.downloading = true;
		System.out.println("Creating " + this.getClass() + " "
				+ reader.getNumberOfMessages());
	}

	/**
	 * 
	 * @param fromDate
	 * @param toDate
	 * @param date
	 * @return
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

	@Override
	public void finished() {
		downloading = false;
		notifyObservers();
	}

	public void resume() {
		downloading = true;
	}

	public void pause() {
		downloading = false;
	}
}
