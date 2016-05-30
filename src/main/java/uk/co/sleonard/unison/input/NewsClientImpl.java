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
import java.io.Reader;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Set;
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
public class NewsClientImpl extends NNTPClient implements NewsClient {

	/** The logger. */
	private static Logger	logger	= Logger.getLogger("NewsClient");
	/** The host. */
	private String			host;

	/** The message count. */
	private int messageCount;

	/**
	 * Instantiates a new news client.
	 */
	public NewsClientImpl() {
	}

	public NewsClientImpl(final BufferedWriter writer, final BufferedReader reader) {
		this._writer_ = writer;
		this._reader_ = reader;
	}

	/**
	 * Close connection.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Override
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

	@Override
	public void connect(final String host, final int port) throws SocketException, IOException {
		super.connect(host, port);
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
	@SuppressWarnings("deprecation")
	public Set<NNTPNewsGroup> listNNTPNewsgroups(final String wildcard, final String nntpserver)
	        throws UNISoNException {

		final Set<NNTPNewsGroup> groupSet = new TreeSet<>();

		try {
			this.connect(nntpserver);

			final NewsgroupInfo[] groups = this.listNewsgroups(wildcard.toLowerCase());
			NewsClientImpl.logger.info("Number of Groups matching " + wildcard + " on " + nntpserver
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
	@Override
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

	@Override
	public Reader retrieveArticle(final String articleId) throws IOException {
		return super.retrieveArticle(articleId);
	}

	@Override
	public Reader retrieveArticleHeader(final String articleId) throws IOException {
		return super.retrieveArticleHeader(articleId);
	}

	@Override
	public BufferedReader retrieveArticleInfo(final long lowArticleNumber,
	        final long highArticleNumber) throws IOException {
		return super.retrieveArticleInfo(lowArticleNumber, highArticleNumber);
	}

	/**
	 * Run command.
	 *
	 * @param command
	 *            the command
	 * @param args
	 *            the args
	 */
	@Override
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
	@Override
	public void setMessageCount(final int messageCount) {
		this.messageCount = messageCount;
	}
}
