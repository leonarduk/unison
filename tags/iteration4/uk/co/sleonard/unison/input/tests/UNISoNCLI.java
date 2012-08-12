package uk.co.sleonard.unison.input.tests;

import java.io.IOException;
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

public class UNISoNCLI implements UNISoNLogger {
	enum Command {
		DOWNLOAD, FIND, FINDDOWNLOAD, QUICKDOWNLOAD
	}

	private static Logger logger = Logger.getLogger("UNISoNCLI");;

	@Override
	public void alert(String message) {
		logger.warn(message);
	}

	@Override
	public void log(String message) {
		logger.info(message);
	}

	public static void main(String[] args) {

		final UNISoNCLI main = new UNISoNCLI();

		Command command = null;
		args = new String[] { "QUICKDOWNLOAD", "*ubuntu*" };

		for (final String arg : args) {
			if (null != command) {
				UNISoNCLI.logger.info("Run " + command + " with " + arg);
				try {
					main.handleCommand(command, arg);
				} catch (UNISoNException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				UNISoNCLI.logger.debug("arg: " + arg);
			}
			try {
				command = Command.valueOf(arg.toUpperCase());
			} catch (final IllegalArgumentException e) {
				// ignore as this just means its not in the enum list
			}
		}
		if (null == command) {
			UNISoNCLI.logger.fatal("No valid command found in args: "
					+ Arrays.asList(args));
			System.exit(1);
		}
	}

	public UNISoNCLI() {
	}

	/**
	 * 
	 * @param searchString
	 * @throws UNISoNException
	 * @throws IOException
	 */
	private void downloadAll(final String searchString) throws UNISoNException {
		final Set<NNTPNewsGroup> listNewsgroups = UNISoNController
				.getInstance().listNewsgroups(searchString);
		UNISoNController.getInstance().quickDownload(listNewsgroups, null,
				null, this, DownloadMode.ALL);
	}

	private void handleCommand(final Command command, final String arg)
			throws UNISoNException {
		Date toDate = null;
		Date fromDate = null;
		switch (command) {
		case DOWNLOAD:
			this.startDownload(arg, fromDate, toDate);
			break;
		case FIND:
			this.listNewsgroups(arg);
			break;
		case FINDDOWNLOAD:
			try {
				this.downloadAll(arg);
			} catch (final UNISoNException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case QUICKDOWNLOAD:
			this.quickDownload(arg, fromDate, toDate);
		}
	}

	private void quickDownload(String arg, Date toDate, Date fromDate)
			throws UNISoNException {
		UNISoNController.create();
		final Set<NNTPNewsGroup> listNewsgroups = UNISoNController
				.getInstance().listNewsgroups(arg);
		// HibernateHelper.generateSchema();

		try {
			UNISoNController.create();
			UNISoNController.getInstance().quickDownload(listNewsgroups,
					fromDate, toDate, this, DownloadMode.BASIC);
		} catch (UNISoNException e) {
			logger.fatal("Error downloading messages", e);
		}
		DatabaseManagerSwing.main(HibernateHelper.GUI_ARGS);

	}

	/**
	 * This method finds the newsgroups that match a search expression (* for
	 * wild character) and saves them to the database.
	 * 
	 * @param searchString
	 * @throws UNISoNException
	 */
	private void listNewsgroups(final String searchString)
			throws UNISoNException {
		final Set<NNTPNewsGroup> listNewsgroups = UNISoNController
				.getInstance().listNewsgroups(searchString);
		UNISoNController.getInstance().storeNewsgroups(listNewsgroups);

	}

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
