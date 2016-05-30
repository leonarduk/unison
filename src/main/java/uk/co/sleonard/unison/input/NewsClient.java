/**
 * NewsClient
 *
 * @author Stephen <github@leonarduk.com>
 * @since 22-May-2016
 */
package uk.co.sleonard.unison.input;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.commons.net.io.DotTerminatedMessageReader;
import org.apache.commons.net.nntp.NNTPClient;
import org.apache.commons.net.nntp.NNTPReply;
import org.apache.commons.net.nntp.NewsgroupInfo;
import org.apache.log4j.Logger;

import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;

/**
 * The Class NewsClient.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
public class NewsClient extends NNTPClient {

	/** The logger. */
	private static Logger logger = Logger.getLogger("NewsClient");

	/** The host. */
	private String host;

	/** The message count. */
	private int messageCount;

	/**
	 * Convert date to string.
	 *
	 * @param date
	 *            the date
	 * @return the string
	 */
	/*
	 * Returns an NNTP-format date string. This is only required when clients use the NEWGROUPS or
	 * NEWNEWS methods, therefore rarely: we don't cache any of the variables here.
	 */
	public static String convertDateToString(final Date date) {
		final String NNTP_DATE_FORMAT = "yyyyMMdd HHmmss 'GMT'";

		final DateFormat df = new SimpleDateFormat(NNTP_DATE_FORMAT);
		final Calendar cal = new GregorianCalendar();
		final TimeZone gmt = TimeZone.getTimeZone("GMT");
		cal.setTimeZone(gmt);
		df.setCalendar(cal);
		cal.setTime(date);
		return df.format(date);
	}

	/**
	 * Instantiates a new news client.
	 */
	public NewsClient() {
	}

	public NewsClient(final BufferedWriter writer, final BufferedReader reader) {
		this._writer_ = writer;
		this._reader_ = reader;
	}

	/**
	 * Close connection.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void closeConnection() throws IOException {
		if (this.isConnected()) {
			this.disconnect();
		}
	}

	/**
	 * Connect to the news server with default settings and anonymous login.
	 *
	 * @param server
	 *            the server
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Override
	public void connect(final String server) throws IOException {
		final int port = 119;
		final String username = null;
		final String password = null;
		try {
			this.connect(server, port, username, password);
		}
		catch (final UNISoNException e) {
			throw new IOException(e);
		}
	}

	/**
	 * Connect.
	 *
	 * @param server
	 *            the server
	 * @param port
	 *            the port
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	public void connect(final String server, final int port, final String username,
	        final String password) throws UNISoNException {
		try {
			if ((null == this.host) || !this.host.equals(server) || !this.isConnected()) {
				// Connect to the news server
				this.connect(server, port);

				// See if we need to authenticate
				if (username != null) {
					this.authenticate(username, password);
				}
				this.host = server;
			}
		}
		catch (final ConnectException e) {
			throw new UNISoNException(
			        "Connection refused. \n" + "Check your settings are correct: server " + server
			                + " username " + username + " password " + password,
			        e);
		}
		catch (final UnknownHostException e) {
			throw new UNISoNException(
			        "Connection refused. \n" + "Either " + server + " is refusing conections \n"
			                + "or there is no internet connection. \n" + "Try another host.",
			        e);
		}
		catch (final IOException e) {
			e.printStackTrace();
			throw new UNISoNException("problem connecting to new server");
		}
	}

	/**
	 * Connect to news group.
	 *
	 * @param hostInput
	 *            the host
	 * @param newsgroup
	 *            the newsgroup
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	public void connectToNewsGroup(final String hostInput, final NewsGroup newsgroup)
	        throws UNISoNException {
		try {
			this.connectToNewsGroup(hostInput, newsgroup.getFullName());
		}
		catch (final Exception e) {
			throw new UNISoNException(e);
		}
	}

	/**
	 * Connect to news group.
	 *
	 * @param host
	 *            the host
	 * @param newsgroup
	 *            the newsgroup
	 * @throws Exception
	 *             the exception
	 */
	public void connectToNewsGroup(final String hostInput, final String newsgroup)
	        throws Exception {
		if (!this.isConnected()) {
			this.connect(hostInput);
		}
		this.selectNewsgroup(newsgroup);
	}

	/**
	 * Disconnect from the News Server.
	 */
	@Override
	public void disconnect() {
		try {
			this.logout();
			this.disconnect();
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the message count.
	 *
	 * @return the message count
	 */
	public int getMessageCount() {
		return this.messageCount;
	}

	/**
	 * Returns the news groups that satisfy the specified wildcard use asterisk for wild character.
	 *
	 * @param wildcard
	 *            the wildcard
	 * @param nntpserver
	 *            the nntpserver
	 * @return the sets the
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	@SuppressWarnings("deprecation")
	public Set<NNTPNewsGroup> listNNTPNewsgroups(final String wildcard, final String nntpserver)
	        throws UNISoNException {

		final Set<NNTPNewsGroup> groupSet = new TreeSet<>();

		try {
			this.connect(nntpserver);

			final NewsgroupInfo[] groups = this.listNewsgroups(wildcard.toLowerCase());
			NewsClient.logger.info("Number of Groups matching " + wildcard + " on " + nntpserver
			        + ": " + (null != groups ? groups.length : 0));
			if (null != groups) {
				for (final NewsgroupInfo element : groups) {
					if ((element.getArticleCount() > 0)
					        & ((element.getLastArticle() - element.getFirstArticle()) > 0)) {
						groupSet.add(new NNTPNewsGroup(element));
					}
				}
			}
		}
		catch (final Exception e) {
			throw new UNISoNException(e);
		}

		return groupSet;
	}

	/**
	 * Reconnect.
	 *
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	public void reconnect() throws UNISoNException {
		// If it should be connected but has timed out
		if (!this.isConnected()) {
			try {
				this.connect(this.host);
			}
			catch (final IOException e) {
				throw new UNISoNException(e);
			}
		}
	}

	/**
	 * Run command.
	 *
	 * @param command
	 *            the command
	 * @param args
	 *            the args
	 */
	public void runCommand(final String command, final String args) {

		int result;
		try {
			result = this.sendCommand(command, args);
			if (!NNTPReply.isPositiveCompletion(result)) {
				throw new RuntimeException("ERROR: " + result);
			}
			final Vector<String> list = new Vector<>();

			try (final BufferedReader reader = new BufferedReader(
			        new DotTerminatedMessageReader(this._reader_));) {

				String line = reader.readLine();
				while (line != null) {
					list.addElement(line);
					line = reader.readLine();
				}
			}
		}
		catch (final IOException e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.apache.commons.net.nntp.NNTPClient#selectNewsgroup(java.lang.String)
	 */
	@Override
	public boolean selectNewsgroup(final String newsgroup) throws IOException {
		return super.selectNewsgroup(newsgroup);
	}

	/**
	 * Sets the message count.
	 *
	 * @param messageCount
	 *            the new message count
	 */
	public void setMessageCount(final int messageCount) {
		this.messageCount = messageCount;
	}
}
