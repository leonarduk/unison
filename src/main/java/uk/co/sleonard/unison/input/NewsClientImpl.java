/**
 * NewsClient
 *
 * @author Stephen <github@leonarduk.com>
 * @since 22-May-2016
 */
package uk.co.sleonard.unison.input;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.nntp.NNTPClient;
import org.apache.commons.net.nntp.NewsgroupInfo;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Set;
import java.util.TreeSet;

/**
 * The Class NewsClient.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 */
@Slf4j
public class NewsClientImpl implements NewsClient {

    /**
     * The host.
     */
    private String host;

    /**
     * The message count.
     */
    @Getter(onMethod_ = @Override)
    @Setter(onMethod_ = @Override)
    private int messageCount;
    private final NNTPClient client;

    /**
     * Instantiates a new news client.
     */
    public NewsClientImpl() {
        this(new NNTPClient());
    }

    public NewsClientImpl(final NNTPClient nntpClient) {
        this.client = nntpClient;
    }

    /**
     * Connect to the news server with default settings and anonymous login.
     *
     * @param server the server
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Override
    public void connect(final String server) throws IOException {
        final int port = 119;
        final String username = null;
        final String password = null;
        try {
            this.connect(server, port, username, password);
        } catch (final UNISoNException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void connect(final String host1, final int port) throws SocketException, IOException {
        this.client.connect(host1, port);
    }

    /**
     * Connect.
     *
     * @param server   the server
     * @param port     the port
     * @param username the username
     * @param password the password
     * @throws UNISoNException the UNI so n exception
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
                    this.client.authenticate(username, password);
                }
                this.host = server;
            }
        } catch (final ConnectException e) {
            throw new UNISoNException(
                    "Connection refused. \n" + "Check your settings are correct: server " + server
                            + " username " + username + " password " + password,
                    e);
        } catch (final UnknownHostException e) {
            throw new UNISoNException(
                    "Connection refused. \n" + "Either " + server + " is refusing conections \n"
                            + "or there is no internet connection. \n" + "Try another host.",
                    e);
        } catch (final IOException e) {
            e.printStackTrace();
            throw new UNISoNException("problem connecting to new server");
        }
    }

    /**
     * Connect to news group.
     *
     * @param host      the host
     * @param newsgroup the newsgroup
     * @throws Exception the exception
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
            this.client.logout();
            this.client.disconnect();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isConnected() {
        try {
            return (this.client != null) && this.client.isConnected();
        } catch (final NullPointerException e) {
            return false;
        }
    }

    /**
     * Returns the news groups that satisfy the specified wildcard use asterisk for wild character.
     *
     * @param wildcard   the wildcard
     * @param nntpserver the nntpserver
     * @return the sets the
     * @throws UNISoNException the UNI so n exception
     */
    @SuppressWarnings("deprecation")
    @Override
    public Set<NewsGroup> listNewsGroups(final String wildcard, final String nntpserver)
            throws UNISoNException {

        final Set<NewsGroup> groupSet = new TreeSet<>();

        try {
            this.connect(nntpserver);

            final NewsgroupInfo[] groups = this.client.listNewsgroups(wildcard.toLowerCase());
            log.info("Number of Groups matching " + wildcard + " on " + nntpserver
                    + ": " + (null != groups ? groups.length : 0));
            if (null != groups) {
                for (final NewsgroupInfo element : groups) {
                    if ((element.getArticleCount() > 0)
                            & ((element.getLastArticle() - element.getFirstArticle()) > 0)) {
                        groupSet.add(new NewsGroup(element));
                    }
                }
            }
        } catch (final Exception e) {
            throw new UNISoNException("Failed to connect", e);
        }

        return groupSet;
    }

    @Override
    public int quit() throws IOException {
        try {
            return this.client.quit();
        } catch (final IOException e) {
            log.error("Error quitting NNTP client", e);
            throw e;
        }
    }

    /**
     * Reconnect.
     *
     * @throws UNISoNException the UNI so n exception
     */
    @Override
    public void reconnect() throws UNISoNException {
        // If it should be connected but has timed out
        if (!this.isConnected()) {
            if (this.host == null) {
                throw new UNISoNException("No host specified");
            }
            try {
                this.connect(this.host);
            } catch (final SocketException e) {
                // preserve the original socket exception as the cause
                throw new UNISoNException("Failed to connect", e);
            } catch (final IOException e) {
                // retain the root cause of the IOException where possible
                final Throwable cause = (e.getCause() != null) ? e.getCause() : e;
                throw new UNISoNException("Failed to connect", cause);
            }
        }
    }

    @Override
    public Reader retrieveArticle(final String articleId) throws IOException {
        return this.client.retrieveArticle(articleId);
    }

    @Override
    public Reader retrieveArticleHeader(final String articleId) throws IOException {
        return this.client.retrieveArticleHeader(articleId);
    }

    @Override
    public BufferedReader retrieveArticleInfo(final long lowArticleNumber,
                                              final long highArticleNumber) throws IOException {
        return this.client.retrieveArticleInfo(lowArticleNumber, highArticleNumber);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.commons.net.nntp.NNTPClient#selectNewsgroup(java.lang.String)
     */
    @Override
    public boolean selectNewsgroup(final String newsgroup) throws IOException {
        return this.client.selectNewsgroup(newsgroup);
    }

}
