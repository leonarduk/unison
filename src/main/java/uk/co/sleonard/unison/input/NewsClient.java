package uk.co.sleonard.unison.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.commons.net.io.DotTerminatedMessageReader;
import org.apache.commons.net.nntp.NNTPClient;
import org.apache.commons.net.nntp.NNTPReply;
import org.apache.commons.net.nntp.NewGroupsOrNewsQuery;
import org.apache.commons.net.nntp.NewsgroupInfo;
import org.apache.log4j.Logger;

import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.gui.UNISoNException;

/**
 * The Class NewsClient.
 * 
 * @author
 * @since
 *
 */
public class NewsClient extends NNTPClient {

	/** There is a maximum number of connections allowed per host. */
	private static HashMap<String, Integer> maxconnections = new HashMap<String, Integer>();

	static {
		NewsClient.maxconnections.put("freetext.usenetserver.com", 3);
		NewsClient.maxconnections.put("news.readfreenews.net", 2);
	}

	/** The logger. */
	private static Logger logger = Logger.getLogger("NewsClient");

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

	/** The host. */
	private String	host;

	/** The message count. */
	private int		messageCount;

	/**
	 * Instantiates a new news client.
	 */
	public NewsClient() {
		// TODO Auto-generated constructor stub
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
			if (null == this.host || !this.host.equals(server) || !this.isConnected()) {
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
			                + " username " + username + " password " + password);
		}
		catch (final UnknownHostException e) {
			throw new UNISoNException(
			        "Connection refused. \n" + "Either " + server + " is refusing conections \n"
			                + "or there is no internet connection. \n" + "Try another host.");
		}
		catch (final IOException e) {
			e.printStackTrace();
			throw new UNISoNException("problem connecting to new server");
		}
	}

	/**
	 * Connect to news group.
	 *
	 * @param host
	 *            the host
	 * @param newsgroup
	 *            the newsgroup
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	public void connectToNewsGroup(final String host, final NewsGroup newsgroup)
	        throws UNISoNException {
		try {
			this.connectToNewsGroup(host, newsgroup.getFullName());
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
	public void connectToNewsGroup(final String host, final String newsgroup) throws Exception {
		if (!this.isConnected()) {
			this.connect(host);
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
	 * Fetch news article ids.
	 *
	 * @param fromDate
	 *            the from date
	 * @param newsgroups
	 *            the newsgroups
	 * @return the string[]
	 * @deprecated uses NEWNEWS
	 */
	@Deprecated
	public String[] fetchNewsArticleIds(final Date fromDate, final Set<NNTPNewsGroup> newsgroups) {
		final Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(fromDate);

		final boolean isGMT = false;
		final NewGroupsOrNewsQuery query = new NewGroupsOrNewsQuery(fromCalendar, isGMT);
		for (final NNTPNewsGroup newsgroup : newsgroups) {
			query.addNewsgroup(newsgroup.getNewsgroup());
			NewsClient.logger.info("Add " + newsgroup.getNewsgroup());
		}
		final String[] articleIds = null;// listNewNews(query);
		this.runCommand("newnews", "soc.senior.issues " + NewsClient.convertDateToString(fromDate));
		if (null == articleIds) {
			throw new RuntimeException("No results found");
		}
		return articleIds;
	}

	/**
	 * Gets the article ids.
	 *
	 * @param groups
	 *            the groups
	 * @param fromDate
	 *            the from date
	 * @return the article ids
	 * @throws Exception
	 *             the exception
	 * @deprecated uses NEWNEWS
	 */
	@Deprecated
	public String[] getArticleIds(final Set<NNTPNewsGroup> groups, final Date fromDate)
	        throws Exception {
		this.reconnect();

		NewsClient.logger.debug("GEt News for " + groups.size() + " groups from " + fromDate);
		final String[] newsArticleIds = this.fetchNewsArticleIds(fromDate, groups);
		return newsArticleIds;
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
					if (element.getArticleCount() > 0
					        & element.getLastArticle() - element.getFirstArticle() > 0) {
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
			final Vector<String> list = new Vector<String>();
			final BufferedReader reader = new BufferedReader(
			        new DotTerminatedMessageReader(this._reader_));

			String line;
			while ((line = reader.readLine()) != null) {
				list.addElement(line);
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
		// TODO Auto-generated method stub
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
