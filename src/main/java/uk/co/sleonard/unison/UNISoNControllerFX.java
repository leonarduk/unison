/**
 * UNISoNControllerFX
 *
 * @author ${author}
 * @since 25-Jun-2016
 */
package uk.co.sleonard.unison;

import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import javafx.application.Platform;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.UNISoNDatabase;
<<<<<<< HEAD
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
=======
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
>>>>>>> ee6cdbcb2a1e8d940a2e093d7142782f4264409c
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.gui.DownloadNewsPanelFX;
import uk.co.sleonard.unison.gui.UNISoNGUIFX;
import uk.co.sleonard.unison.gui.UNISoNTabbedFrameFX;
import uk.co.sleonard.unison.input.DataHibernatorWorker;
import uk.co.sleonard.unison.input.HeaderDownloadWorker;
import uk.co.sleonard.unison.input.NewsArticle;
import uk.co.sleonard.unison.input.NewsGroupReader;
import uk.co.sleonard.unison.utils.DownloaderImpl;

/**
 * The class UNISoNController
 * 
 * @author Elton Nunes <elton_12_nunes@hotmail.com>
 * @since 25-jun-2016
 *
 */
public class UNISoNControllerFX {

	/** The instance. */
	private static UNISoNControllerFX instance;

	/** The logger. */
	private static Logger logger = Logger.getLogger(UNISoNControllerFX.class);

	private static UNISoNGUIFX gui;

	/** The Constant LOCATION. */
	public static final String LOCATION = "Location";

	/** The Constant USENETUSER. */
	public static final String USENETUSER = UsenetUser.class.getName();

	String			lineSeparator	= System.getProperty("line.separator");
	// Components Variables
	/** The status label. */
	@FXML
	private Label	statusLabel;

	/** The status progress. */
	@FXML
	private ProgressBar statusProgress;

	// Reference to the main application
	private UNISoNTabbedFrameFX unisonTabbedFrameFX;

	// TODO Auto-generated constructor stub
	// Reference to download news panel controller
	private DownloadNewsPanelFX downloadNewsPanelFX;

	/** The message queue. */
	private final LinkedBlockingQueue<NewsArticle> messageQueue;

	/** The nntp reader. */
	private final NewsGroupReader nntpReader;

	/** The header downloader. */
	private final HeaderDownloadWorker headerDownloader;

	/** The helper. */
	private final HibernateHelper helper;

	/** The matrix type. */
	private MatrixType matrixType;

	/** The nntp host. */
	private String nntpHost;

	/** The session. */
	private Session session;

	/** The download panel. */
	private UNISoNLogger downloadPanel;

	private final UNISoNAnalysis analysis;

	private final UNISoNDatabase database;

	private final NewsGroupFilter filter;

	public static UNISoNGUIFX getGui() {
		return UNISoNControllerFX.gui;
	}

	/**
	 * Gets the single instance of UNISoNController.
	 *
	 * @return single instance of UNISoNController
	 */
	public static UNISoNControllerFX getInstance() {
		return UNISoNControllerFX.instance;
	}

	public UNISoNControllerFX() throws UNISoNException {
		UNISoNControllerFX.gui = new UNISoNGUIFX();	// Create a instance of UNISoNGui
		this.messageQueue = new LinkedBlockingQueue<>();
		this.headerDownloader = new HeaderDownloadWorker(this.messageQueue, new DownloaderImpl());
		this.headerDownloader.initialise();
		this.helper = new HibernateHelper(UNISoNControllerFX.gui);
		try {
			final Session hibernateSession = this.getHelper().getHibernateSession();
			this.setSession(hibernateSession);
			this.filter = new NewsGroupFilter(hibernateSession, this.helper);
			this.analysis = new UNISoNAnalysis(this.filter, hibernateSession, this.helper);
			this.database = new UNISoNDatabase(this.filter, hibernateSession, this.helper);
		}
		catch (final UNISoNException e) {
			UNISoNControllerFX.getGui().showAlert("Error:" + e.getMessage());
			throw e;
		}

		this.nntpReader = new NewsGroupReader(this);
	}

<<<<<<< HEAD
=======
	@FXML
	private void initialize() {

	}

	/**
	 * Called by UNISoNTabbedFrameFX to set a instance of UNISoNControllerFX into instance variable
	 * of this class.
	 */
	public void setInstance() {
		UNISoNControllerFX.instance = this.unisonTabbedFrameFX.getUnisonController();
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
	public Set<NNTPNewsGroup> listNewsgroups(final String searchString, final String host)
	        throws UNISoNException {

		this.nntpHost = host;
		return this.nntpReader.client.listNNTPNewsgroups(searchString, host);
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
	public void quickDownload(final Set<NNTPNewsGroup> groups, final Date fromDate1,
	        final Date toDate1, final UNISoNLogger log, final DownloadMode mode)
	        throws UNISoNException {

		for (final NNTPNewsGroup group : groups) {
			try {
				this.nntpReader.client.reconnect();
				this.nntpReader.client.selectNewsgroup(group.getNewsgroup());
				this.nntpReader.setMessageCount(group.getArticleCount());
				this.headerDownloader.initialise(this.nntpReader, group.getFirstArticle(),
				        group.getLastArticle(), this.nntpHost, group.getNewsgroup(), log, mode,
				        fromDate1, toDate1);
			}
			catch (final IOException e) {
				e.printStackTrace();
				throw new UNISoNException(
				        "Error downloading messages. Check your internet connection: ", e);
			}
		}
	}

	/**
	 * Change status label
	 * 
	 * @param text
	 *            Text will be appear on status.
	 */
	public void setStatusLabel(String text) {
		this.statusLabel.setText(text);
	}

	/**
	 * Change status progress
	 * 
	 * @param progress
	 *            The actual progress of the ProgressIndicator. A negative value for progress
	 *            indicates that the progress is indeterminate. A positive value between 0 and 1
	 *            indicates the percentage of progress where 0 is 0% and 1 is 100%. Any value
	 *            greater than 1 is interpreted as 100%.
	 */
	public void setStatusProgress(Double progress) {
		this.statusProgress.setProgress(progress);
	}

>>>>>>> ee6cdbcb2a1e8d940a2e093d7142782f4264409c
	/**
	 * Cancel download.
	 */
	public void cancelDownload() {
		this.stopDownload();
	}

	/**
	 * Stop download.
	 */
	public void stopDownload() {
		DataHibernatorWorker.stopDownload();
		this.setIdleState();
	}

	/**
	 * Store newsgroups.
	 *
	 * @param newsgroups
	 *            the newsgroups
	 */
	public void storeNewsgroups(final Set<NNTPNewsGroup> newsgroups) {
		this.getHelper().storeNewsgroups(newsgroups, this.getSession());
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

	/**
	 * Sets the idle state.
	 */
	public void setIdleState() {
		this.setButtonState(true, false, false, false);
	}

	/**
	 * Sets the connected state.
	 */
	public void setConnectedState() {
		this.setButtonState(false, true, false, true);
	}

	/**
	 * Sets the connecting state.
	 */
	public void setConnectingState() {
		this.setButtonState(false, false, false, true);
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
	 * Connect to news group.
	 *
	 * @param newsgroup
	 *            the newsgroup
	 * @deprecated
	 */
	@Deprecated
	private void connectToNewsGroup(final String newsgroup) {
		this.setConnectingState();
		UNISoNControllerFX.getGui().showStatus("Connect to " + newsgroup);

		// TODO need to filter by data and allow more than one newsgroup
		this.getFilter().setSelectedNewsgroup(newsgroup);
		final String host = null;// this.frame.getSelectedHost();
		try {
			this.nntpReader.client.connectToNewsGroup(host, newsgroup);
			this.setConnectedState();

			UNISoNControllerFX.getGui()
			        .showStatus("MESSAGES:" + this.nntpReader.getNumberOfMessages());
		}
		catch (final java.net.UnknownHostException e) {
			this.showErrorMessage(newsgroup + " not found on " + host);
		}
		catch (final Exception e) {
			this.showErrorMessage("ERROR: " + e);
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
		// The command line version does not do this
		// if (null != this.frame) {
		// this.frame.setButtonState(connectButtonState, downloadButtonState,
		// pauseButtonState, cancelButtonState);
		// }
	}

	/**
	 * Close application
	 */
	@FXML
	private void closeApplication() {
		Platform.exit();
		System.exit(1);
	}

<<<<<<< HEAD
	public UNISoNDatabase getDatabase() {
		return this.database;
=======
	private void showErrorMessage(final String messageText) {
		// this.frame.showErrorMessage(message);
		UNISoNControllerFX.logger.warn(messageText);
	}

	/**
	 * Once the header download worker completes it will call this. This method will tell the
	 * download panel to update itself.
	 */
	public void setHeaderDownloaderFinished() {
		this.headerDownloader.notifyObservers();
	}

	/**
	 * Sets the matrix type.
	 *
	 * @param type
	 *            the new matrix type
	 */
	public void setMatrixType(final MatrixType type) {
		this.matrixType = type;
	}

	/**
	 * Show about dialog, Called by MenuItem About
	 */
	@FXML
	private void showAboutDialog() {
		this.unisonTabbedFrameFX.showAboutDialog();
	}

	public void setUnisonTabbedFrameFX(UNISoNTabbedFrameFX unisonTabbedFrameFX) {
		this.unisonTabbedFrameFX = unisonTabbedFrameFX;
>>>>>>> ee6cdbcb2a1e8d940a2e093d7142782f4264409c
	}

	public DownloadNewsPanelFX getDownloadNewsPanelFX() {
		return this.downloadNewsPanelFX;
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
<<<<<<< HEAD
	 * Gets the nntp host.
=======
	 * Gets the matrix type.
	 *
	 * @return the matrix type
	 */
	public MatrixType getMatrixType() {
		return this.matrixType;
	}

	/**
	 * Gets the queue.
	 *
	 * @return the queue
	 */
	public LinkedBlockingQueue<NewsArticle> getQueue() {
		DataHibernatorWorker.startHibernators();
		return this.messageQueue;
	}

	/**
	 * Gets the single instance of UNISoNController.
>>>>>>> ee6cdbcb2a1e8d940a2e093d7142782f4264409c
	 *
	 * @return the nntp host
	 */
	public String getNntpHost() {
		return this.nntpHost;
	}

<<<<<<< HEAD
	@FXML
	private void initialize() {

	}

	/**
	 * List newsgroups.
=======
	public Session getSession() {
		return this.session;
	}

	public static UNISoNGUIFX getGui() {
		return UNISoNControllerFX.gui;
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

	public static void setGui(final UNISoNGUIFX gui) {
		UNISoNControllerFX.gui = gui;
	}

	/**
	 * Gets the nntp host.
>>>>>>> ee6cdbcb2a1e8d940a2e093d7142782f4264409c
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

		this.nntpHost = host;
		return this.nntpReader.client.listNewsGroups(searchString, host);
	}

	public void setDownloadNewsPanelFX(final DownloadNewsPanelFX downloadNewsPanelFX) {
		this.downloadNewsPanelFX = downloadNewsPanelFX;
	}

	/**
	 * Called by UNISoNTabbedFrameFX to set a instance of UNISoNControllerFX into instance variable
	 * of this class.
	 */
	public void setInstance() {
		UNISoNControllerFX.instance = this.unisonTabbedFrameFX.getUnisonController();
	}

	public void setMatrixType(final MatrixType replyToAll) {
		// TODO Auto-generated method stub

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
	 * Sets the nntp host.
	 *
	 * @param nntpHost
	 *            the new nntp host
	 */
	public void setNntpHost(final String nntpHost) {
		this.nntpHost = nntpHost;
	}

	public void setSession(final Session session) {
		this.session = session;
	}

	/**
	 * Change status label
	 *
	 * @param text
	 *            Text will be appear on status.
	 */
	public void setStatusLabel(final String text) {
		this.statusLabel.setText(text);
	}

<<<<<<< HEAD
	/**
	 * Change status progress
	 *
	 * @param progress
	 *            The actual progress of the ProgressIndicator. A negative value for progress
	 *            indicates that the progress is indeterminate. A positive value between 0 and 1
	 *            indicates the percentage of progress where 0 is 0% and 1 is 100%. Any value
	 *            greater than 1 is interpreted as 100%.
	 */
	public void setStatusProgress(final Double progress) {
		this.statusProgress.setProgress(progress);
	}

	public void setUnisonTabbedFrameFX(final UNISoNTabbedFrameFX uniSoNTabbedFrameFX2) {
		this.unisonTabbedFrameFX = uniSoNTabbedFrameFX2;
	}

	/**
	 * Show about dialog, Called by MenuItem About
	 */
	@FXML
	private void showAboutDialog() {
		this.unisonTabbedFrameFX.showAboutDialog();
=======
	public UNISoNAnalysis getAnalysis() {
		return this.analysis;
	}

	public UNISoNDatabase getDatabase() {
		return this.database;
>>>>>>> ee6cdbcb2a1e8d940a2e093d7142782f4264409c
	}

	/**
	 * Gets the download panel.
	 *
	 * @return the download panel
	 */
	public UNISoNLogger getDownloadPanel() {
		return this.downloadPanel;
	}

}
