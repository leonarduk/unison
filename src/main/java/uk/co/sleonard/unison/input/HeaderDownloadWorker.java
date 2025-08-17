/**
 * HeaderDownloadWorker
 *
 * @author Stephen <github@leonarduk.com>
 * @since 22-May-2016
 */
package uk.co.sleonard.unison.input;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.nntp.Article;
import org.apache.commons.net.nntp.NNTPClient;
import uk.co.sleonard.unison.DataChangeListener;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.utils.Downloader;
import uk.co.sleonard.unison.utils.StringUtils;

import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Class to create a separate Thread for downloading messages.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 */
@Slf4j
public class HeaderDownloadWorker extends SwingWorker {

    /**
     * The end index.
     */
    private int endIndex;

    /**
     * The news reader.
     */
    private NewsGroupReader newsReader = null;

    /**
     * The start index.
     */
    private int startIndex;

    /**
     * The from date.
     */
    private Date fromDate;

    /**
     * The to date.
     */
    private Date toDate;


    /**
     * The downloading.
     */
    private volatile boolean downloading = false;

    /**
     * The running.
     */
    private volatile boolean running = true;

    /**
     * The log tally.
     */
    private int logTally = 0;

    /**
     * The index.
     */
    private int index = 0;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * The skipped.
     */
    private int skipped = 0;

    /**
     * The kept.
     */
    private int kept = 0;

    /**
     * The mode.
     */
    private DownloadMode mode;

    private final LinkedBlockingQueue<NewsArticle> queue;

    private final Downloader downloader;

    /**
     * Instantiates a new header download worker.
     */
    public HeaderDownloadWorker(final LinkedBlockingQueue<NewsArticle> inputQueue,
                                final Downloader downloader1) {
        super(HeaderDownloadWorker.class.getCanonicalName());
        this.queue = inputQueue;
        this.downloader = downloader1;
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
                    this.storeArticleInfo(this.queue);
                } catch (final UNISoNException e) {
                    log.error("Error", e);
                    e.printStackTrace();
                    return "FAIL";
                }
                this.downloading = false;
                this.notifyListeners();
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
        this.notifyListeners();
    }

    /**
     * Method to stop all downloading and end the thread.
     */
    public void fullstop() {
        this.running = false;
        this.downloading = false;
        try {
            final NewsGroupReader newsReader2 = this.newsReader;
            if ((null != newsReader2) && (null != newsReader2.getClient())) {
                newsReader2.getClient().quit();
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        this.notifyListeners();
    }


    /**
     * In date range.
     *
     * @param fromDate1 the from date
     * @param toDate1   the to date
     * @param date      the date
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

    public void initialise() {
        this.running = true;
        this.downloading = false;
        this.start();
    }

    /**
     * Initialise.
     *
     * @param reader      the reader
     * @param startIndex1 the start index
     * @param endIndex1   the end index
     * @param server      the server
     * @param newsgroup1  the newsgroup
     * @param mode1       the mode
     * @param from        the from
     * @param to          the to
     * @throws UNISoNException the UNI so n exception
     */
    public void initialise(final NewsGroupReader reader, final int startIndex1, final int endIndex1,
                           final String server, final String newsgroup1,
                           final DownloadMode mode1, final Date from, final Date to) throws UNISoNException {
        this.mode = mode1;
        this.startIndex = startIndex1;
        this.endIndex = endIndex1;
        this.newsReader = reader;
        this.fromDate = from;
        this.toDate = to;

        log.info("Initialising: server={}, group={}, start={}, end={}, mode={}, from={}, to={}",
                server, newsgroup1, startIndex1, endIndex1, mode1, from, to);
        try {
            reader.getClient().connect(server);
            reader.getClient().selectNewsgroup(newsgroup1);
        } catch (final Exception e) {
            log.warn("Initialisation failed for group {} on {}: {}", newsgroup1, server,
                    e.getMessage(), e);
            throw new UNISoNException("Failed to initialise downloader", e);
        }

        this.downloading = true;
        log.info("Creating " + this.getClass() + " " + newsgroup1 + "["
                + reader.getMessageCount() + "]");
    }

    /**
     * Checks if is downloading.
     *
     * @return true, if is downloading
     */
    public boolean isDownloading() {
        return this.downloading;
    }

    /**
     * Register a listener for download progress.
     *
     * @param listener the listener to add
     */
    public void addDataChangeListener(final DataChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    /**
     * Remove a previously registered listener.
     *
     * @param listener the listener to remove
     */
    public void removeDataChangeListener(final DataChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    /**
     * Notify listeners that the worker state has changed.
     */
    public void notifyListeners() {
        this.pcs.firePropertyChange("download", null, null);
    }

    /**
     * Pause.
     */
    public void pause() {
        this.downloading = false;
    }

    void processMessage(final LinkedBlockingQueue<NewsArticle> queue1, final String line)
            throws UNISoNException {
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
            date = StringUtils.stringToDate(stt.nextToken());
        } catch (final DateTimeParseException e) {
            throw new UNISoNException("Failed to parse date", e);
        }
        final String articleId = stt.nextToken();
        String references = stt.nextToken();
        if (!references.contains("@")) {
            references = "";
        }

        if (this.inDateRange(this.fromDate, this.toDate, date)) {
            queue1.add(new NewsArticle(articleId, articleNumber, date, from, subject, references,
                    references));
            this.kept++;
            if (!this.mode.equals(DownloadMode.BASIC)) {
                this.downloader.addDownloadRequest(articleId, this.mode);
            }
        } else {
            this.skipped++;
        }
    }

    /**
     * Queue messages.
     *
     * @param reader the reader
     * @return true, if successful
     * @throws IOException     Signals that an I/O exception has occurred.
     * @throws UNISoNException the UNI so n exception
     */
    boolean queueMessages(final LinkedBlockingQueue<NewsArticle> queue1, final Reader reader)
            throws IOException, UNISoNException {
        if (reader != null) {
            final BufferedReader bufReader = new BufferedReader(reader);

            for (String line = bufReader.readLine(); line != null; line = bufReader.readLine()) {
                if (!this.running) {
                    log.warn("Download aborted");
                    this.notifyListeners();
                    throw new UNISoNException("Download aborted");
                }
                // If told to pause or queue is getting a bit full wait
                boolean pausedForQueue = false;
                while (!this.downloading || (queue1.size() > 1000)) {
                    if ((queue1.size() > 1000) && !pausedForQueue) {
                        log.info("Pausing as queue size {} exceeds 1000", queue1.size());
                        pausedForQueue = true;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (final InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (pausedForQueue) {
                    log.info("Resuming processing; queue size {}", queue1.size());
                }

                this.processMessage(queue1, line);
                this.index++;
                this.logTally++;
                if (this.logTally == 100) {
                    int size = queue1.size();
                    if (!this.mode.equals(DownloadMode.BASIC)) {
                        size += FullDownloadWorker.queueSize();
                    }
                    log.info("Downloaded {} kept {} skipped {} to process: {} [{}]", this.index, this.kept,
                            this.skipped, size, new Date());
                    this.logTally = 0;
                }
            }
            this.notifyListeners();
            int size = queue1.size();
            if (!this.mode.equals(DownloadMode.BASIC)) {
                size += FullDownloadWorker.queueSize();
            }
            log.info("Downloaded {} kept {} to process: {}", this.index, this.kept, size);
        }
        return true;
    }

    /**
     * Resume.
     */
    public void resume() {
        this.downloading = true;
    }

    public void setMode(final DownloadMode headers) {
        this.mode = headers;
    }

    /**
     * Given an {@link NNTPClient} instance, and an integer range of messages, return an array of
     * {@link Article} instances.
     *
     * @return Article[] An array of Article
     * @throws UNISoNException the UNI so n exception
     */
    boolean storeArticleInfo(final LinkedBlockingQueue<NewsArticle> queue1) throws UNISoNException {

        try {
            this.logTally = 0;
            this.index = 0;
            this.skipped = 0;
            this.kept = 0;

            // fetch back 500 messages at a time
            for (int i = this.startIndex; i < this.endIndex; i += 500) {
                final int batchEndIndex = Math.min(i + 499, this.endIndex);
                log.debug("Starting batch {}-{}", i, batchEndIndex);
                try (final Reader reader = this.newsReader.getClient().retrieveArticleInfo(
                        i, batchEndIndex);) {
                    this.queueMessages(queue1, reader);
                }
                log.debug("Finished batch {}-{}", i, batchEndIndex);
            }
        } catch (final IOException e1) {
            log.error("Error", e1);
            e1.printStackTrace();
            return false;
        }

        return true;
    }
}
