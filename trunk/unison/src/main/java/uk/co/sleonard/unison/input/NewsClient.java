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

public class NewsClient extends NNTPClient {
	private String host;

	/**
	 * 
	 * @param host
	 * @param newsgroup
	 * @throws Exception
	 */
	public void connectToNewsGroup(final String host, final String newsgroup)
			throws Exception {
		if (!this.isConnected()) {
			this.connect(host);
		}
		this.selectNewsgroup(newsgroup);
	}

	/**
	 * Disconnect from the News Server
	 */
	public void disconnect() {
		try {
			this.logout();
			this.disconnect();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Connect to the news server with default settings and anonymous login
	 * 
	 * @throws UNISoNException
	 * 
	 * @throws UNISoNException
	 */
	public void connect(final String server) throws UNISoNException {
		final int port = 119;
		final String username = null;
		final String password = null;
		this.connect(server, port, username, password);
	}

	/**
	 * There is a maximum number of connections allowed per host
	 */
	private static HashMap<String, Integer> maxconnections = new HashMap<String, Integer>();
	static {
		maxconnections.put("freetext.usenetserver.com", 3);
		maxconnections.put("news.readfreenews.net", 2);
	}

	/**
	 * Returns the news groups that satisfy the specified wildcard use asterisk
	 * for wild character
	 * 
	 * @param nntpserver
	 * 
	 * @throws UNISoNException
	 */
	public Set<NNTPNewsGroup> listNNTPNewsgroups(final String wildcard,
			String nntpserver) throws UNISoNException {

		final Set<NNTPNewsGroup> groupSet = new TreeSet<NNTPNewsGroup>();

		try {
			this.connect(nntpserver);

			final NewsgroupInfo[] groups = this.listNewsgroups(wildcard
					.toLowerCase());
			logger.info("Number of Groups matching " + wildcard + " on "
					+ nntpserver + ": "
					+ ((null != groups) ? groups.length : 0));
			if (null != groups) {
				for (final NewsgroupInfo element : groups) {
					if (element.getArticleCount() > 0
							& (element.getLastArticle()
									- element.getFirstArticle() > 0)) {
						groupSet.add(new NNTPNewsGroup(element));
					}
				}
			}
		} catch (final Exception e) {
			throw new UNISoNException(e);
		}

		return groupSet;
	}

	public void reconnect() throws UNISoNException {
		// If it should be connected but has timed out
		if (!this.isConnected()) {
			this.connect(this.host);
		}
	}

	public void connect(final String server, final int port,
			final String username, final String password)
			throws UNISoNException {
		try {
			if (null == this.host || !this.host.equals(server)
					|| !this.isConnected()) {
				// Connect to the news server
				this.connect(server, port);

				// See if we need to authenticate
				if (username != null) {
					this.authenticate(username, password);
				}
				this.host = server;
			}
		} catch (ConnectException e) {
			throw new UNISoNException("Connection refused. \n"
					+ "Check your settings are correct: server " + server
					+ " username " + username + " password " + password);
		} catch (final UnknownHostException e) {
			throw new UNISoNException("Connection refused. \n" + "Either "
					+ server + " is refusing conections \n"
					+ "or there is no internet connection. \n"
					+ "Try another host.");
		} catch (final IOException e) {
			e.printStackTrace();
			throw new UNISoNException("problem connecting to new server");
		}
	}

	public void closeConnection() throws IOException {
		if (this.isConnected()) {
			this.disconnect();
		}
	}

	/**
	 * @deprecated uses NEWNEWS
	 * @param groups
	 * @param fromDate
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public String[] getArticleIds(final Set<NNTPNewsGroup> groups,
			final Date fromDate) throws Exception {
		this.reconnect();

		logger.debug("GEt News for " + groups.size() + " groups from "
				+ fromDate);
		final String[] newsArticleIds = this.fetchNewsArticleIds(fromDate,
				groups);
		return newsArticleIds;
	}

	public void connectToNewsGroup(final String host, final NewsGroup newsgroup)
			throws UNISoNException {
		try {
			this.connectToNewsGroup(host, newsgroup.getFullName());
		} catch (Exception e) {
			throw new UNISoNException(e);
		}
	}

	private static Logger logger = Logger.getLogger("NewsClient");

	private int messageCount;

	public NewsClient() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @deprecated uses NEWNEWS
	 * @param fromDate
	 * @param newsgroups
	 * @return
	 */
	@Deprecated
	public String[] fetchNewsArticleIds(final Date fromDate,
			final Set<NNTPNewsGroup> newsgroups) {
		final Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(fromDate);

		final boolean isGMT = false;
		final NewGroupsOrNewsQuery query = new NewGroupsOrNewsQuery(
				fromCalendar, isGMT);
		for (final NNTPNewsGroup newsgroup : newsgroups) {
			query.addNewsgroup(newsgroup.getNewsgroup());
			NewsClient.logger.info("Add " + newsgroup.getNewsgroup());
		}
		final String[] articleIds = null;// listNewNews(query);
		this.runCommand("newnews", "soc.senior.issues "
				+ convertDateToString(fromDate));
		if (null == articleIds) {
			throw new RuntimeException("No results found");
		}
		return articleIds;
	}

	/*
	 * Returns an NNTP-format date string. This is only required when clients
	 * use the NEWGROUPS or NEWNEWS methods, therefore rarely: we don't cache
	 * any of the variables here.
	 */
	public static String convertDateToString(final Date date) {
		String NNTP_DATE_FORMAT = "yyyyMMdd HHmmss 'GMT'";

		final DateFormat df = new SimpleDateFormat(NNTP_DATE_FORMAT);
		final Calendar cal = new GregorianCalendar();
		final TimeZone gmt = TimeZone.getTimeZone("GMT");
		cal.setTimeZone(gmt);
		df.setCalendar(cal);
		cal.setTime(date);
		return df.format(date);
	}

	public int getMessageCount() {
		return this.messageCount;
	}

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

		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean selectNewsgroup(final String newsgroup) throws IOException {
		// TODO Auto-generated method stub
		return super.selectNewsgroup(newsgroup);
	}

	public void setMessageCount(final int messageCount) {
		this.messageCount = messageCount;
	}
}
