package uk.co.sleonard.unison;

import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.UNISoNDatabase;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.gui.DownloadNewsPanelFX;
import uk.co.sleonard.unison.gui.UNISoNGUIFX;
import uk.co.sleonard.unison.gui.UNISoNTabbedFrameFX;
import uk.co.sleonard.unison.input.HeaderDownloadWorker;
import uk.co.sleonard.unison.input.NNTPNewsGroup;
import uk.co.sleonard.unison.input.NewsArticle;
import uk.co.sleonard.unison.input.NewsGroupReader;

public class UNISoNControllerFX {

	String lineSeparator = System.getProperty("line.separator");

	// Components Variables
	/** The status label. */
	@FXML
	private Label statusLabel;

	/** The status progress. */
	@FXML
	private ProgressBar statusProgress;

	// Reference to the main application
	private UNISoNTabbedFrameFX unisonTabbedFrameFX;

	// Reference to download news panel controller
	private DownloadNewsPanelFX downloadNewsPanelFX;

	/** The instance. */
	private static UNISoNControllerFX instance;
	/** The logger. */
	private static Logger logger = Logger.getLogger(UNISoNControllerFX.class);

	private static UNISoNGUIFX gui;

	/** The Constant LOCATION. */
	public static final String LOCATION = "Location";

	/** The Constant USENETUSER. */
	public static final String USENETUSER = UsenetUser.class.getName();

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

	public UNISoNControllerFX() throws UNISoNException {
		UNISoNControllerFX.gui = new UNISoNGUIFX();	// Create a instance of UNISoNGui
		this.messageQueue = new LinkedBlockingQueue<>();
		this.headerDownloader = new HeaderDownloadWorker();
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
			UNISoNController.getGui().showAlert("Error:" + e.getMessage());
			throw e;
		}

		this.nntpReader = new NewsGroupReader(this);
	}

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

	/**
	 * Close application
	 */
	@FXML
	private void closeApplication() {
		Platform.exit();
		System.exit(1);
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
	}

	public DownloadNewsPanelFX getDownloadNewsPanelFX() {
		return downloadNewsPanelFX;
	}

	public void setDownloadNewsPanelFX(DownloadNewsPanelFX downloadNewsPanelFX) {
		this.downloadNewsPanelFX = downloadNewsPanelFX;
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
	 * Gets the single instance of UNISoNController.
	 *
	 * @return single instance of UNISoNController
	 */
	public static UNISoNControllerFX getInstance() {
		return instance;
	}

	public static UNISoNGUIFX getGui() {
		return UNISoNControllerFX.gui;
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

	public NewsGroupFilter getFilter() {
		return this.filter;
	}

	public UNISoNDatabase getDatabase() {
		return this.database;
	}

}
