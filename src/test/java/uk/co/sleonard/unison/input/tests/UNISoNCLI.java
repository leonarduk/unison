package uk.co.sleonard.unison.input.tests;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hsqldb.util.DatabaseManagerSwing;

import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.gui.UNISoNController;
import uk.co.sleonard.unison.gui.UNISoNException;
import uk.co.sleonard.unison.gui.UNISoNLogger;
import uk.co.sleonard.unison.input.NNTPNewsGroup;

/**
 * The Class UNISoNCLI.
 * 
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
public class UNISoNCLI implements UNISoNLogger {

	/**
	 * The Enum Command.
	 */
	enum Command {

		/** The download. */
		DOWNLOAD,
		/** The find. */
		FIND,
		/** The finddownload. */
		FINDDOWNLOAD,
		/** The quickdownload. */
		QUICKDOWNLOAD
	}

	/** The logger. */
	private static Logger logger = Logger.getLogger("UNISoNCLI");;

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.sleonard.unison.gui.UNISoNLogger#alert(java.lang.String)
	 */
	@Override
	public void alert(String message) {
		logger.warn(message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.sleonard.unison.gui.UNISoNLogger#log(java.lang.String)
	 */
	@Override
	public void log(String message) {
		logger.info(message);
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		final UNISoNCLI main = new UNISoNCLI();

		Command command = null;
		args = new String[] { "QUICKDOWNLOAD", "*ubuntu*" };

		for (final String arg : args) {
			if (null != command) {
				UNISoNCLI.logger.info("Run " + command + " with " + arg);
				try {
					String host = "";
					main.handleCommand(command, arg, host);
				}
				catch (UNISoNException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				UNISoNCLI.logger.debug("arg: " + arg);
			}
			try {
				command = Command.valueOf(arg.toUpperCase());
			}
			catch (final IllegalArgumentException e) {
				// ignore as this just means its not in the enum list
			}
		}
		if (null == command) {
			UNISoNCLI.logger.fatal("No valid command found in args: " + Arrays.asList(args));
			System.exit(1);
		}
	}

	/**
	 * Instantiates a new UNI so ncli.
	 */
	public UNISoNCLI() {
	}

	/**
	 * Download all.
	 *
	 * @param searchString
	 *            the search string
	 * @param host
	 *            the host
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	private void downloadAll(final String searchString, String host) throws UNISoNException {
		final Set<NNTPNewsGroup> listNewsgroups = UNISoNController.getInstance()
		        .listNewsgroups(searchString, host);
		UNISoNController.getInstance().quickDownload(listNewsgroups, null, null, this,
		        DownloadMode.ALL);
	}

	/**
	 * Handle command.
	 *
	 * @param command
	 *            the command
	 * @param arg
	 *            the arg
	 * @param host
	 *            the host
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	private void handleCommand(final Command command, final String arg, String host)
	        throws UNISoNException {
		Date toDate = null;
		Date fromDate = null;
		switch (command) {
			case DOWNLOAD:
				this.startDownload(arg, fromDate, toDate);
				break;
			case FIND:
				this.listNewsgroups(arg, host);
				break;
			case FINDDOWNLOAD:
				try {
					this.downloadAll(arg, host);
				}
				catch (final UNISoNException e) {
					e.printStackTrace();
				}
				break;
			case QUICKDOWNLOAD:
				this.quickDownload(arg, fromDate, toDate, host);
				break;
		}
	}

	/**
	 * Quick download.
	 *
	 * @param arg
	 *            the arg
	 * @param toDate
	 *            the to date
	 * @param fromDate
	 *            the from date
	 * @param host
	 *            the host
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	private void quickDownload(String arg, Date toDate, Date fromDate, String host)
	        throws UNISoNException {
		UNISoNController.create();
		final Set<NNTPNewsGroup> listNewsgroups = UNISoNController.getInstance().listNewsgroups(arg,
		        host);
		// HibernateHelper.generateSchema();

		try {
			UNISoNController.create();
			UNISoNController.getInstance().quickDownload(listNewsgroups, fromDate, toDate, this,
			        DownloadMode.BASIC);
		}
		catch (UNISoNException e) {
			logger.fatal("Error downloading messages", e);
		}
		DatabaseManagerSwing.main(HibernateHelper.GUI_ARGS);

	}

	/**
	 * This method finds the newsgroups that match a search expression (* for wild character) and
	 * saves them to the database.
	 *
	 * @param searchString
	 *            the search string
	 * @param host
	 *            the host
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	private void listNewsgroups(final String searchString, String host) throws UNISoNException {
		final Set<NNTPNewsGroup> listNewsgroups = UNISoNController.getInstance()
		        .listNewsgroups(searchString, host);
		UNISoNController.getInstance().storeNewsgroups(listNewsgroups);

	}

	/**
	 * Start download.
	 *
	 * @param newsgroup
	 *            the newsgroup
	 * @param toDate
	 *            the to date
	 * @param fromDate
	 *            the from date
	 */
	public void startDownload(final String newsgroup, Date toDate, Date fromDate) {
		// try {
		// UNISoNController.getInstance().downloadMessages(newsgroup,
		// fromDate, toDate);
		//
		// } catch (UNISoNException e) {
		// logger.fatal("Error downloading messages", e);
		// }
	}
}
