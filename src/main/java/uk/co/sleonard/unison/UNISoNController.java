/**
 * UNISoNController
 *
 * @author Stephen <github@leonarduk.com>
 * @since 22-May-2016
 */
package uk.co.sleonard.unison;

import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;

import org.hibernate.Session;

import uk.co.sleonard.unison.datahandling.DataQuery;
import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.UNISoNDatabase;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.gui.UNISoNGUI;
import uk.co.sleonard.unison.input.DataHibernatorPool;
import uk.co.sleonard.unison.input.DataHibernatorPoolImpl;
import uk.co.sleonard.unison.input.DataHibernatorWorker;
import uk.co.sleonard.unison.input.HeaderDownloadWorker;
import uk.co.sleonard.unison.input.NewsArticle;
import uk.co.sleonard.unison.input.NewsClient;
import uk.co.sleonard.unison.input.NewsGroupReader;
import uk.co.sleonard.unison.utils.DownloaderImpl;

/**
 * The Class UNISoNController.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
public class UNISoNController {

	/** The instance. */
	private static UNISoNController instance;

	private static UNISoNGUI gui;

	/** The message queue. */
	private final LinkedBlockingQueue<NewsArticle> messageQueue;

	/** The nntp reader. */
	private NewsGroupReader nntpReader;

	/** The header downloader. */
	private HeaderDownloadWorker headerDownloader;

	/** The helper. */
	private final HibernateHelper helper;

	/** The nntp host. */
	private String nntpHost;

	/** The session. */
	private Session session;

	/** The download panel. */
	private UNISoNLogger downloadPanel;

	private final UNISoNAnalysis analysis;

	private final UNISoNDatabase database;

	private final NewsGroupFilter filter;

	private final DataHibernatorPool pool;

	private NewsClient client;

	/**
	 * Creates the.
	 *
	 * @param frame
	 *            the frame
	 * @return the UNI so n controller
	 * @throws UNISoNException
	 */
	public static UNISoNController create(final JFrame frame) throws UNISoNException {
		return UNISoNController.create(frame, new DataHibernatorPoolImpl());
	}

	private static UNISoNController create(final JFrame frame, final DataHibernatorPool pool)
	        throws UNISoNException {
		UNISoNController.instance = new UNISoNController(pool);
		UNISoNController.setGui(new UNISoNGUI(frame));
		return UNISoNController.instance;
	}
	// private static UNISoNController instance;

	public static UNISoNGUI getGui() {
		return UNISoNController.gui;
	}

	/**
	 * Gets the single instance of UNISoNController.
	 *
	 * @return single instance of UNISoNController
	 */
	public static UNISoNController getInstance() {
		return UNISoNController.instance;
	}

	private static void setGui(final UNISoNGUI gui) {
		UNISoNController.gui = gui;
	}

	private UNISoNController() throws UNISoNException {
		this(new DataHibernatorPoolImpl());
	}

	/**
	 * Instantiates a new UNI so n controller.
	 *
	 * @throws UNISoNException
	 */
	private UNISoNController(final DataHibernatorPool hibernatorPool) throws UNISoNException {
		this.pool = hibernatorPool;

		this.messageQueue = new LinkedBlockingQueue<>();
		this.helper = new HibernateHelper(UNISoNController.gui);
		try {
			final Session hibernateSession = this.getHelper().getHibernateSession();
			this.setSession(hibernateSession);
			this.filter = new NewsGroupFilter(hibernateSession, this.helper);
			this.analysis = new UNISoNAnalysis(this.filter, hibernateSession, this.helper);
			this.database = new UNISoNDatabase(this.filter, hibernateSession, this.helper,
			        new DataQuery(this.helper));
		}
		catch (final UNISoNException e) {
			UNISoNController.getGui().showAlert("Error:" + e.getMessage());
			throw e;
		}

		this.nntpReader = new NewsGroupReader(this);
	}

	public UNISoNAnalysis getAnalysis() {
		return this.analysis;
	}

	public UNISoNDatabase getDatabase() {
		return this.database;
	}

	/**
	 * Gets the download panel.
	 *
	 * @return the download panel
	 */
	public UNISoNLogger getDownloadPanel() {
		return this.downloadPanel;
	}

	public NewsGroupFilter getFilter() {
		return this.filter;
	}

	/**
	 * Gets the header downloader.
	 *
	 * @return the header downloader
	 */
	public HeaderDownloadWorker getHeaderDownloader() {
		return this.headerDownloader;
	}

	public HibernateHelper getHelper() {
		return this.helper;
	}

	/**
	 * Gets the nntp host.
	 *
	 * @return the nntp host
	 */
	public String getNntpHost() {
		return this.nntpHost;
	}

	/**
	 * Gets the nntp reader.
	 *
	 * @return the nntp reader
	 */
	public NewsGroupReader getNntpReader() {
		return this.nntpReader;
	}

	/**
	 * Gets the queue.
	 *
	 * @return the queue
	 */
	public LinkedBlockingQueue<NewsArticle> getQueue() {
		DataHibernatorWorker.startHibernators(this.getNntpReader(), this.helper, this.messageQueue,
		        this.session);
		return this.messageQueue;
	}

	public Session getSession() {
		return this.session;
	}

	/**
	 * Helper.
	 *
	 * @return the hibernate helper
	 */
	public HibernateHelper helper() {
		return this.getHelper();
	}

	/**
	 * List newsgroups.
	 *
	 * @param searchString
	 *            the search string
	 * @param host
	 *            the host
	 * @return the sets the
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	public Set<NewsGroup> listNewsgroups(final String searchString, final String host)
	        throws UNISoNException {
		this.setNntpHost(host);
		return this.getNntpReader().getClient().listNewsGroups(searchString, host);
	}

	/**
	 * Quick download.
	 *
	 * @param groups
	 *            the groups
	 * @param fromDate1
	 *            the from date
	 * @param toDate1
	 *            the to date
	 * @param log
	 *            the log
	 * @param mode
	 *            the mode
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	public void quickDownload(final Set<NewsGroup> groups, final Date fromDate1, final Date toDate1,
	        final UNISoNLogger log, final DownloadMode mode) throws UNISoNException {
		final NewsGroupReader reader = this.getNntpReader();
		this.client = reader.getClient();
		final HeaderDownloadWorker headerDownloader2 = this.getHeaderDownloader();
		final String nntpHost2 = this.getNntpHost();

		for (final NewsGroup group : groups) {
			try {
				this.client.reconnect();
				this.client.selectNewsgroup(group.getName());
				reader.setMessageCount(group.getArticleCount());
				headerDownloader2.initialise(reader, group.getFirstMessage(),
				        group.getLastMessage(), nntpHost2, group.getName(), log, mode, fromDate1,
				        toDate1);
			}
			catch (final IOException e) {
				e.printStackTrace();
				throw new UNISoNException(
				        "Error downloading messages. Check your internet connection: ", e);
			}
		}
	}

	/**
	 * Sets the button state.
	 *
	 * @param connectButtonState
	 *            the connect button state
	 * @param downloadButtonState
	 *            the download button state
	 * @param pauseButtonState
	 *            the pause button state
	 * @param cancelButtonState
	 *            the cancel button state
	 */
	private void setButtonState(final boolean connectButtonState, final boolean downloadButtonState,
	        final boolean pauseButtonState, final boolean cancelButtonState) {
	}

	/**
	 * Sets the downloading state.
	 *
	 * @param progress
	 *            the new downloading state
	 */
	public void setDownloadingState(final int progress) {
		this.setButtonState(false, false, true, true);
	}

	/**
	 * Sets the download panel.
	 *
	 * @param downloadPanel
	 *            the new download panel
	 */
	public void setDownloadPanel(final UNISoNLogger downloadPanel) {
		this.downloadPanel = downloadPanel;
	}

	public void setHeaderDownloader(final HeaderDownloadWorker downloadWorker) {
		this.headerDownloader = downloadWorker;
	}

	/**
	 * Sets the idle state.
	 */
	private void setIdleState() {
		this.setButtonState(true, false, false, false);
	}

	/**
	 * Sets the nntp host.
	 *
	 * @param nntpHost
	 *            the new nntp host
	 */
	public void setNntpHost(final String nntpHost) {
		this.nntpHost = nntpHost;
		this.headerDownloader = new HeaderDownloadWorker(this.messageQueue,
		        new DownloaderImpl(this.nntpHost, this.messageQueue, this.client, this.nntpReader,
		                this.helper, this.session));
		this.headerDownloader.initialise();

	}

	public void setNntpReader(final NewsGroupReader reader) {
		this.nntpReader = reader;
	}

	public void setSession(final Session session) {
		this.session = session;
	}

	/**
	 * Stop download.
	 */
	public void stopDownload() {
		this.pool.stopAllDownloads();
		this.setIdleState();
	}

	/**
	 * Switch filtered.
	 *
	 * @param on
	 *            the on
	 */
	public void switchFiltered(final boolean on) {
		this.getFilter().setFiltered(on);
		this.getDatabase().refreshDataFromDatabase();
	}
}
