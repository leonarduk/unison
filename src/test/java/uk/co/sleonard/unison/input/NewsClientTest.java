/**
 * NewsClientTest
 *
 * @author ${author}
 * @since 29-May-2016
 */
package uk.co.sleonard.unison.input;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.utils.StringUtils;

public class NewsClientTest {

	private NewsClient		client;
	private BufferedReader	reader;

	@Before
	public void setUp() throws Exception {
		final BufferedWriter writer = Mockito.mock(BufferedWriter.class);
		this.reader = Mockito.mock(BufferedReader.class);
		this.client = new NewsClientImpl(writer, this.reader);
	}

	@Test
	public final void testCloseConnection() throws IOException {
		this.client.closeConnection();
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

	@Ignore
	@Test
	public final void testConnectToNewsGroupStringNewsGroup() throws UNISoNException {
		final String hostInput = "s";
		final NewsGroup newsgroup = new NewsGroup("test");
		this.client.connectToNewsGroup(hostInput, newsgroup);
	}

	@Test
	public final void testConvertDateToString() {
		final Date date = new Date();
		StringUtils.convertDateToString(date);
	}

	@Ignore  // doesnt seem to be unit test
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

	@Test
	@Ignore
	public final void testRunCommand() throws IOException {
		final String command = "sds";
		final String args = "";
		Mockito.when(this.reader.readLine()).thenReturn("200 Test");
		final List<String> responses = new ArrayList<>();
		responses.add("200");
		responses.add("Test line");
		final char[] cbuf = {};
		final int off = -1;
		final int len = -1;
		Mockito.when(this.reader.read(cbuf, off, len)).thenReturn(-1);
		Mockito.when(this.reader.readLine()).thenAnswer(new Answer() {
			private int count = 0;

			@Override
			public Object answer(final InvocationOnMock invocation) {
				this.count++;
				if (this.count == 1) {
					return "200 ";
				}
				if (this.count == 2) {
					return "test ";
				}
				return null;
				// if (this.count++ < responses.size()) {
		        // return responses.get(this.count - 1);
		        // }
		        //
		        // return null;
			}
		});

		this.client.runCommand(command, args);
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
