/**
 * NewsClientTest
 *
 * @author Stephen <github@leonarduk.com>
 * @since 22-May-2016
 */
package uk.co.sleonard.unison.input;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.utils.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Set;

/**
 *
 *
 * @author Stephen Leonard
 * @since 22 May 2016
 *
 * @version $Author:: $: Author of last commit
 * @version $Rev:: $: Revision of last commit
 * @version $Date:: $: Date of last commit
 *
 */
@Slf4j
@Ignore("Requires a live NNTP server and is disabled to avoid external network calls")
public class NewsClientIT {

	public static BufferedReader downloadFirstMessage() throws IOException, UNISoNException {
                final String[] servers = StringUtils.loadServerList();
                Assert.assertTrue("No servers configured", servers.length > 0);
                return NewsClientIT.downloadFirstMessage(servers[0]);
	}

	public static BufferedReader downloadFirstMessage(final String server)
	        throws IOException, UNISoNException {
		final NewsClient client = new NewsClientImpl();
		client.connect(server);
		final Set<NewsGroup> groups = client.listNewsGroups("", server);
		final NewsGroup group = groups.iterator().next();
		client.selectNewsgroup(group.getName());
		log.info("Connect to " + server + ":" + group.getName());
		final long lowArticleNumber = group.getFirstMessage();
		final long highArticleNumber = group.getFirstMessage();
		final BufferedReader reader = client.retrieveArticleInfo(lowArticleNumber,
		        highArticleNumber);
		return reader;
	}

	/**
	 * @throws IOException
	 * @throws SocketException
	 *
	 */
	@Test(expected = UnknownHostException.class)
	public void testConnectForInvalidServer() throws SocketException, IOException {
		final NewsClient client = new NewsClientImpl();
		final String hostname = "broken.server";
		client.connect(hostname, 119);
	}

	/**
	 * @throws IOException
	 * @throws SocketException
	 *
	 */
	@Ignore
	@Test
	public void testConnectForServersProperties() throws SocketException, IOException {
		final NewsClient client = new NewsClientImpl();
		final String[] servers = StringUtils.loadServerList();
		for (final String hostname : servers) {
			log.info("Try " + hostname);
			client.connect(hostname, 119);
		}
	}

	@Test
	public void testRetrieveArticleInfo() throws Exception {
                final String[] servers = StringUtils.loadServerList();
                Assert.assertTrue("No servers configured", servers.length > 0);
                final String server = servers[0];
                final BufferedReader reader = NewsClientIT.downloadFirstMessage(server);
		try (final BufferedReader bufReader = new BufferedReader(reader);) {
			final String line = bufReader.readLine();
			log.info("Message: " + line);
			Assert.assertNotNull(line);
			Assert.assertTrue(line.length() > 0);
			Assert.assertTrue(line.contains(server));
		}
	}
}
