/**
 * UNISoNCLI
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.input;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.hsqldb.util.DatabaseManagerSwing;
import org.junit.Assert;
import org.junit.Ignore;

import uk.co.sleonard.unison.UNISoNController;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.UNISoNLogger;
import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;

/**
 * The Class UNISoNCLI.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
@Slf4j
@Ignore("Command line entry point - not a unit test")
public class UNISoNCLI implements UNISoNLogger {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
        public static void main(String[] args) {

                final UNISoNCLI main = new UNISoNCLI();
                final Optional<ParsedArgs> parsed = parseArgs(args);
                if (parsed.isEmpty()) {
                        log.error("No valid command found in args: {}", Arrays.asList(args));
                        return;
                }

                final String host = "";
                final ParsedArgs p = parsed.get();
                try {
                        main.handleCommand(p.command, p.argument, host);
                }
                catch (final UNISoNException e) {
                        log.error("Error executing command", e);
                }
        }

	/**
	 * Instantiates a new UNI so ncli.
	 */
        public UNISoNCLI() {
        }

        static class ParsedArgs {
                final Command command;
                final String argument;

                ParsedArgs(final Command command, final String argument) {
                        this.command = command;
                        this.argument = argument;
                }
        }

        static Optional<ParsedArgs> parseArgs(final String[] args) {
                if (args == null || args.length < 2) {
                        return Optional.empty();
                }
                try {
                        final Command command = Command.valueOf(args[0].toUpperCase());
                        return Optional.of(new ParsedArgs(command, args[1]));
                }
                catch (final IllegalArgumentException e) {
                        return Optional.empty();
                }
        }

	/*
	 * (non-Javadoc)
	 *
	 * @see uk.co.sleonard.unison.gui.UNISoNLogger#alert(java.lang.String)
	 */
	@Override
	public void alert(final String message) {
                log.warn(message);
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
	private void downloadAll(final String searchString, final String host) throws UNISoNException {
		final UNISoNController instance = UNISoNController.getInstance();
		final Set<NewsGroup> listNewsgroups = instance.listNewsgroups(searchString, host,
		        instance.getNntpReader().getClient());
		instance.quickDownload(listNewsgroups, null, null, this, DownloadMode.ALL);
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
	private void handleCommand(final Command command, final String arg, final String host)
	        throws UNISoNException {
		final Date toDate = null;
		final Date fromDate = null;
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
			default:
				break;
		}
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
	private void listNewsgroups(final String searchString, final String host)
	        throws UNISoNException {
		final UNISoNController instance = UNISoNController.getInstance();
		final Set<NewsGroup> listNewsgroups = instance.listNewsgroups(searchString, host,
		        instance.getNntpReader().getClient());
		Assert.assertTrue(listNewsgroups.size() > 0);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see uk.co.sleonard.unison.gui.UNISoNLogger#log(java.lang.String)
	 */
	@Override
	public void log(final String message) {
            log.info(message);
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
	private void quickDownload(final String arg, final Date toDate, final Date fromDate,
	        final String host) throws UNISoNException {
		final UNISoNController instance = UNISoNController.getInstance();
		final Set<NewsGroup> listNewsgroups = instance.listNewsgroups(arg, host,
		        instance.getNntpReader().getClient());
		// HibernateHelper.generateSchema();

		try {
			instance.quickDownload(listNewsgroups, fromDate, toDate, this, DownloadMode.BASIC);
		}
		catch (final UNISoNException e) {
                    log.error("Error downloading messages", e);
		}
		DatabaseManagerSwing.main(HibernateHelper.GUI_ARGS);

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
	public void startDownload(final String newsgroup, final Date toDate, final Date fromDate) {
		// try {
		// UNISoNController.getInstance().downloadMessages(newsgroup,
		// fromDate, toDate);
		//
		// } catch (UNISoNException e) {
		// logger.fatal("Error downloading messages", e);
		// }
	}

	/**
	 * The Enum Command.
	 */
	enum Command {

		/** The download. */
		DOWNLOAD, /** The find. */
		FIND, /** The finddownload. */
		FINDDOWNLOAD, /** The quickdownload. */
		QUICKDOWNLOAD
	}
}
