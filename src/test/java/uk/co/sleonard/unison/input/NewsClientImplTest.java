/**
 * NewsClientImplTest
 *
 * @author ${author}
 * @since 29-May-2016
 */
package uk.co.sleonard.unison.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Date;
import org.apache.commons.net.nntp.NNTPClient;
import org.apache.commons.net.nntp.NewsgroupInfo;
import org.apache.commons.net.nntp.NewsgroupInfoFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.utils.StringUtils;

public class NewsClientImplTest {

  private NewsClient client;
  private NNTPClient mock;

  @Before
  public void setUp() throws Exception {
    this.mock = Mockito.mock(NNTPClient.class);
    this.client = new NewsClientImpl(this.mock);
  }

  @Test(expected = UNISoNException.class)
  public final void testConnectConnectException() throws IOException, UNISoNException {
    final String server = "";
    Mockito.when(this.mock.authenticate(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
        .thenThrow(new ConnectException());
    this.client.connect(server, 1, "user", "pwd");
  }

  @Test(expected = UNISoNException.class)
  public final void testConnectException() throws IOException, UNISoNException {
    final String server = "";
    Mockito.when(this.mock.authenticate(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
        .thenThrow(new IOException());
    this.client.connect(server, 1, "user", "pwd");
  }

  @Test
  public final void testConnectString() throws IOException {
    final String server = "";
    this.client.connect(server);
    this.client.connect(server);
  }

  @Test
  public final void testConnectStringIntStringString() throws UNISoNException {
    final String server = "";
    final int port = 1;
    final String username = "u";
    final String password = "p";
    this.client.connect(server, port, username, password);
  }

  @Test(expected = UNISoNException.class)
  public final void testConnectUnknownHostException() throws IOException, UNISoNException {
    final String server = "";
    Mockito.when(this.mock.authenticate(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
        .thenThrow(new UnknownHostException());
    this.client.connect(server, 1, "user", "pwd");
  }

  @Test
  public final void testConvertDateToString() {
    final Date date = new Date();
    StringUtils.convertDateToString(date);
  }

  // doesnt seem to be unit test
  @Test
  public final void testDisconnect() {
    this.client.disconnect();
  }

  @Test
  public final void testGetMessageCount() {
    this.client.getMessageCount();
  }

  @Test
  public final void testListNewsGroups() throws UNISoNException {
    final String wildcard = "";
    final String nntpserver = "";
    this.client.listNewsGroups(wildcard, nntpserver);
  }

  @Test(expected = UNISoNException.class)
  public final void testListNewsGroupsException() throws UNISoNException, IOException {
    final String wildcard = "";
    final String nntpserver = "";
    final NewsgroupInfo[] groups = {NewsgroupInfoFactory.newsgroupInfo(1, 1, 1, "alt")};
    Mockito.when(this.mock.listNewsgroups(ArgumentMatchers.anyString()))
        .thenThrow(new IOException());
    this.client.listNewsGroups(wildcard, nntpserver);
  }

  @Test
  public final void testListNewsGroupsNoResults() throws UNISoNException, IOException {
    final String wildcard = "";
    final String nntpserver = "";
    final NewsgroupInfo[] groups = {NewsgroupInfoFactory.newsgroupInfo(1, 1, 1, "alt")};
    Mockito.when(this.mock.listNewsgroups(ArgumentMatchers.anyString())).thenReturn(groups);
    this.client.listNewsGroups(wildcard, nntpserver);
  }

  @Test
  public final void testListNewsGroupsResults() throws UNISoNException, IOException {
    final String wildcard = "";
    final String nntpserver = "";
    final NewsgroupInfo[] groups = {NewsgroupInfoFactory.newsgroupInfo(1, 1, 2, "alt")};
    Mockito.when(this.mock.listNewsgroups(ArgumentMatchers.anyString())).thenReturn(groups);
    this.client.listNewsGroups(wildcard, nntpserver);
  }

  @Test
  public final void testListNewsGroupsZeroResults() throws UNISoNException, IOException {
    final String wildcard = "";
    final String nntpserver = "";
    final NewsgroupInfo[] groups = {NewsgroupInfoFactory.newsgroupInfo(0, 0, 0, "alt")};
    Mockito.when(this.mock.listNewsgroups(ArgumentMatchers.anyString())).thenReturn(groups);
    this.client.listNewsGroups(wildcard, nntpserver);
  }

  @Test
  public void testName() throws Exception {
    this.client.connectToNewsGroup("host", "group");
  }

  @Test
  public final void testReconnect() throws UNISoNException, IOException {
    this.client.connect("host");
    this.client.reconnect();
  }

  @Test(expected = UNISoNException.class)
  public final void testReconnectException() throws UNISoNException, IOException {
    Mockito.doThrow(new IOException("sds"))
        .when(this.mock)
        .connect(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());
    this.client.reconnect();
  }

  @Test
  public final void testSelectNewsgroupString() throws IOException {
    final String newsgroup = "group";
    this.client.selectNewsgroup(newsgroup);
  }

  @Test
  public final void testSetMessageCount() {
    final int messageCount = 10;
    this.client.setMessageCount(messageCount);
  }

  @Test
  public void testQuitReturnsDelegateValue() throws IOException {
    final int expected = 205;
    Mockito.when(this.mock.quit()).thenReturn(expected);
    final int actual = this.client.quit();
    Assert.assertEquals(expected, actual);
    Mockito.verify(this.mock).quit();
  }

  @Test(expected = IOException.class)
  public void testQuitThrowsIOException() throws IOException {
    Mockito.when(this.mock.quit()).thenThrow(new IOException());
    this.client.quit();
  }

  @Test
  public void testRetrieveArticleForwardsCall() throws IOException {
    final String articleId = "id";
    final Reader reader = new StringReader("content");
    Mockito.when(this.mock.retrieveArticle(articleId)).thenReturn(reader);
    final Reader actual = this.client.retrieveArticle(articleId);
    Assert.assertSame(reader, actual);
    Mockito.verify(this.mock).retrieveArticle(articleId);
  }

  @Test(expected = IOException.class)
  public void testRetrieveArticleThrowsIOException() throws IOException {
    final String articleId = "id";
    Mockito.when(this.mock.retrieveArticle(articleId)).thenThrow(new IOException());
    this.client.retrieveArticle(articleId);
  }

  @Test
  public void testRetrieveArticleHeaderForwardsCall() throws IOException {
    final String articleId = "id";
    final Reader header = new StringReader("header");
    Mockito.when(this.mock.retrieveArticleHeader(articleId)).thenReturn(header);
    final Reader actual = this.client.retrieveArticleHeader(articleId);
    Assert.assertSame(header, actual);
    Mockito.verify(this.mock).retrieveArticleHeader(articleId);
  }

  @Test(expected = IOException.class)
  public void testRetrieveArticleHeaderThrowsIOException() throws IOException {
    final String articleId = "id";
    Mockito.when(this.mock.retrieveArticleHeader(articleId)).thenThrow(new IOException());
    this.client.retrieveArticleHeader(articleId);
  }

  @Test
  public void testRetrieveArticleInfoForwardsCall() throws IOException {
    final long low = 1L;
    final long high = 2L;
    final BufferedReader reader = new BufferedReader(new StringReader("info"));
    Mockito.when(this.mock.retrieveArticleInfo(low, high)).thenReturn(reader);
    final BufferedReader actual = this.client.retrieveArticleInfo(low, high);
    Assert.assertSame(reader, actual);
    Mockito.verify(this.mock).retrieveArticleInfo(low, high);
  }

  @Test(expected = IOException.class)
  public void testRetrieveArticleInfoThrowsIOException() throws IOException {
    final long low = 1L;
    final long high = 2L;
    Mockito.when(this.mock.retrieveArticleInfo(low, high)).thenThrow(new IOException());
    this.client.retrieveArticleInfo(low, high);
  }
}
