/**
 * NewsClientTest
 *
 * @author Stephen <github@leonarduk.com>
 * @since 22-May-2016
 */
package uk.co.sleonard.unison.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.utils.StringUtils;

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
public class NewsClientIT {
	private static Logger logger = Logger.getLogger("NewsClient");

	public static BufferedReader downloadFirstMessage() throws IOException, UNISoNException {
		return NewsClientIT.downloadFirstMessage(StringUtils.loadServerList()[0]);
	}

	public static BufferedReader downloadFirstMessage(final String server)
	        throws IOException, UNISoNException {
		final NewsClient client = new NewsClientImpl();
		client.connect(server);
		final Set<NNTPNewsGroup> groups = client.listNNTPNewsgroups("", server);
		final NNTPNewsGroup group = groups.iterator().next();
		client.selectNewsgroup(group.getNewsgroup());
		NewsClientIT.logger.info("Connect to " + server + ":" + group.getNewsgroup());
		final long lowArticleNumber = group.getFirstArticle();
		final long highArticleNumber = group.getFirstArticle();
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
	@Test
	public void testConnectForServersProperties() throws SocketException, IOException {
		final NewsClient client = new NewsClientImpl();
		final String[] servers = StringUtils.loadServerList();
		for (final String hostname : servers) {
			client.connect(hostname, 119);
		}
	}

	@Test
	public void testRetrieveArticleInfo() throws Exception {
		final String server = StringUtils.loadServerList()[0];
		final BufferedReader reader = NewsClientIT.downloadFirstMessage(server);
		try (final BufferedReader bufReader = new BufferedReader(reader);) {
			final String line = bufReader.readLine();
			NewsClientIT.logger.info("Message: " + line);
			Assert.assertNotNull(line);
			Assert.assertTrue(line.length() > 0);
			Assert.assertTrue(line.contains(server));
		}
	}
}
