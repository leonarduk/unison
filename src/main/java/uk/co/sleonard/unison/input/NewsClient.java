/**
 * NewsClient
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.input;

import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Set;

public interface NewsClient {

    public void connect(final String server) throws IOException;

    public void connect(String hostname, int i) throws IOException;

    public void connect(String server, int port, String username, String password)
            throws UNISoNException;

    public void connectToNewsGroup(String host, String newsgroup) throws Exception;

    public void disconnect();

    public int getMessageCount();

    public boolean isConnected();

    public Set<NewsGroup> listNewsGroups(String searchString, String host) throws UNISoNException;

    public int quit() throws IOException;

    public void reconnect() throws UNISoNException;

    public Reader retrieveArticle(String usenetID) throws IOException;

    public Reader retrieveArticleHeader(final String articleId) throws IOException;

    public BufferedReader retrieveArticleInfo(final long lowArticleNumber,
                                              final long highArticleNumber) throws IOException;

    public boolean selectNewsgroup(String newsgroup1) throws IOException;

    public void setMessageCount(int messageCount);

}
