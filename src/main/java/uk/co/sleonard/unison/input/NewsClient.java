/**
 * NewsClient
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Set;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;

public interface NewsClient {

  /**
   * Connects to an NNTP server using the default port.
   *
   * @param server the host name of the server
   * @throws IOException if the connection cannot be established
   */
  void connect(final String server) throws IOException;

  /**
   * Connects to an NNTP server using the supplied port.
   *
   * @param hostname the server host name
   * @param port the port to connect to
   * @throws IOException if the connection fails
   */
  void connect(String hostname, int port) throws IOException;

  /**
   * Connects to an NNTP server using explicit credentials.
   *
   * @param server the server host name
   * @param port the server port
   * @param username the username to authenticate with
   * @param password the password to authenticate with
   * @throws UNISoNException if authentication or connection fails
   */
  void connect(String server, int port, String username, String password) throws UNISoNException;

  /**
   * Establishes a connection to the specified newsgroup on the given host.
   *
   * @param host the NNTP server host
   * @param newsgroup the name of the newsgroup to join
   * @throws Exception if the newsgroup cannot be selected
   */
  void connectToNewsGroup(String host, String newsgroup) throws Exception;

  /** Disconnects from the currently connected server. */
  void disconnect();

  /**
   * Gets the number of messages reported by the server for the selected group.
   *
   * @return the message count
   */
  int getMessageCount();

  /**
   * Indicates whether the client is currently connected to a server.
   *
   * @return {@code true} if connected; {@code false} otherwise
   */
  boolean isConnected();

  /**
   * Lists available newsgroups matching the provided search text.
   *
   * @param searchString optional text to filter group names
   * @param host the NNTP server to query
   * @return the set of matching newsgroups
   * @throws UNISoNException if the list cannot be retrieved
   */
  Set<NewsGroup> listNewsGroups(String searchString, String host) throws UNISoNException;

  /**
   * Issues a QUIT command to the server.
   *
   * @return the response code from the server
   * @throws IOException if an I/O error occurs
   */
  int quit() throws IOException;

  /**
   * Re-establishes a connection using previously supplied settings.
   *
   * @throws UNISoNException if the reconnect attempt fails
   */
  void reconnect() throws UNISoNException;

  /**
   * Retrieves a full article by its message identifier.
   *
   * @param usenetID the message ID of the article
   * @return a reader over the article content
   * @throws IOException if the article cannot be retrieved
   */
  Reader retrieveArticle(String usenetID) throws IOException;

  /**
   * Retrieves only the headers for a particular article.
   *
   * @param articleId the message ID of the article
   * @return a reader over the article headers
   * @throws IOException if the headers cannot be retrieved
   */
  Reader retrieveArticleHeader(final String articleId) throws IOException;

  /**
   * Retrieves article metadata between two message numbers.
   *
   * @param lowArticleNumber the first article number to fetch
   * @param highArticleNumber the last article number to fetch
   * @return a reader over the range information
   * @throws IOException if the information cannot be retrieved
   */
  BufferedReader retrieveArticleInfo(final long lowArticleNumber, final long highArticleNumber)
      throws IOException;

  /**
   * Selects the given newsgroup as the current group.
   *
   * @param newsgroup the newsgroup to select
   * @return {@code true} if selection was successful
   * @throws IOException if the group cannot be selected
   */
  boolean selectNewsgroup(String newsgroup) throws IOException;

  /**
   * Sets the message count for the current group.
   *
   * @param messageCount the number of messages reported by the server
   */
  void setMessageCount(int messageCount);
}
