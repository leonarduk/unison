package uk.co.sleonard.unison;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import javafx.application.Platform;
import javafx.fxml.FXML;
import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.UNISoNDatabase;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.gui.UNISoNGUI;
import uk.co.sleonard.unison.gui.UNISoNGUIFX;
import uk.co.sleonard.unison.gui.generated.UNISoNTabbedFrameFX;
import uk.co.sleonard.unison.input.HeaderDownloadWorker;
import uk.co.sleonard.unison.input.NewsArticle;
import uk.co.sleonard.unison.input.NewsGroupReader;

public class UNISoNControllerFX {

	String lineSeparator = System.getProperty("line.separator");

	// Components Variables

	// Reference to the main application
	private UNISoNTabbedFrameFX unisonTabbedFrameFX;

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

	public void setUnisonTabbedFrameFX(UNISoNTabbedFrameFX unisonTabbedFrameFX) {
		this.unisonTabbedFrameFX = unisonTabbedFrameFX;
	}

	@FXML
	private void closeApplication() {
		Platform.exit();
		System.exit(1);
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
	
	public static UNISoNGUIFX getGui() {
		return UNISoNControllerFX.gui;
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
