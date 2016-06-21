/**
 * NewsClientTest
 *
 * @author ${author}
 * @since 29-May-2016
 */
package uk.co.sleonard.unison.input;

import java.io.IOException;
import java.util.Date;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.utils.StringUtils;

public class NewsClientTest {

	private NewsClient client;

	@Before
	public void setUp() throws Exception {
		this.client = new NewsClientImpl();
	}

	@Ignore
	@Test
	public final void testConnectString() throws IOException {
		final String server = "";
		this.client.connect(server);
	}

	@Ignore
	@Test
	public final void testConnectStringIntStringString() throws UNISoNException {
		final String server = "";
		final int port = 1;
		final String username = "u";
		final String password = "p";
		this.client.connect(server, port, username, password);
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

	@Ignore
	@Test
	public final void testListNewsGroups() throws UNISoNException {
		final String wildcard = "";
		final String nntpserver = "";
		this.client.listNewsGroups(wildcard, nntpserver);
	}

	@Ignore
	@Test
	public final void testReconnect() throws UNISoNException {
		this.client.reconnect();
	}

	@Ignore
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
