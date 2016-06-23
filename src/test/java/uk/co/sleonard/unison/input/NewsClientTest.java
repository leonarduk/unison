/**
 * NewsClientTest
 *
 * @author ${author}
 * @since 29-May-2016
 */
package uk.co.sleonard.unison.input;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;

import org.apache.commons.net.nntp.NNTPClient;
import org.apache.commons.net.nntp.NewsgroupInfo;
import org.apache.commons.net.nntp.NewsgroupInfoFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.utils.StringUtils;

public class NewsClientTest {

	private NewsClient	client;
	private NNTPClient	mock;

	@Before
	public void setUp() throws Exception {
		this.mock = Mockito.mock(NNTPClient.class);
		this.client = new NewsClientImpl(this.mock);
	}

	@Test(expected = UNISoNException.class)
	public final void testConnectConnectException() throws IOException, UNISoNException {
		final String server = "";
		Mockito.when(this.mock.authenticate(Matchers.anyString(), Matchers.anyString()))
		        .thenThrow(new ConnectException());
		this.client.connect(server, 1, "user", "pwd");
	}

	@Test(expected = UNISoNException.class)
	public final void testConnectException() throws IOException, UNISoNException {
		final String server = "";
		Mockito.when(this.mock.authenticate(Matchers.anyString(), Matchers.anyString()))
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
		Mockito.when(this.mock.authenticate(Matchers.anyString(), Matchers.anyString()))
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
		final NewsgroupInfo[] groups = { NewsgroupInfoFactory.newsgroupInfo(1, 1, 1, "alt") };
		Mockito.when(this.mock.listNewsgroups(Matchers.anyString())).thenThrow(new IOException());
		this.client.listNewsGroups(wildcard, nntpserver);
	}

	@Test
	public final void testListNewsGroupsNoResults() throws UNISoNException, IOException {
		final String wildcard = "";
		final String nntpserver = "";
		final NewsgroupInfo[] groups = { NewsgroupInfoFactory.newsgroupInfo(1, 1, 1, "alt") };
		Mockito.when(this.mock.listNewsgroups(Matchers.anyString())).thenReturn(groups);
		this.client.listNewsGroups(wildcard, nntpserver);
	}

	@Test
	public final void testListNewsGroupsResults() throws UNISoNException, IOException {
		final String wildcard = "";
		final String nntpserver = "";
		final NewsgroupInfo[] groups = { NewsgroupInfoFactory.newsgroupInfo(1, 1, 2, "alt") };
		Mockito.when(this.mock.listNewsgroups(Matchers.anyString())).thenReturn(groups);
		this.client.listNewsGroups(wildcard, nntpserver);
	}

	@Test
	public final void testListNewsGroupsZeroResults() throws UNISoNException, IOException {
		final String wildcard = "";
		final String nntpserver = "";
		final NewsgroupInfo[] groups = { NewsgroupInfoFactory.newsgroupInfo(0, 0, 0, "alt") };
		Mockito.when(this.mock.listNewsgroups(Matchers.anyString())).thenReturn(groups);
		this.client.listNewsGroups(wildcard, nntpserver);
	}

	@Test
	public void testName() throws Exception {
		this.client.connectToNewsGroup("host", "group");
	}

	@Test
	public final void testReconnect() throws UNISoNException {
		this.client.reconnect();
	}

	@Test(expected = UNISoNException.class)
	public final void testReconnectException()
	        throws UNISoNException, SocketException, IOException {
		Mockito.doThrow(new IOException("sds")).when(this.mock).connect(Matchers.anyString(),
		        Matchers.anyInt());
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

}
