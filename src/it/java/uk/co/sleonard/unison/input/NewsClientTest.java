/**
 * NewsClientTest
 *
 * @author Stephen <github@leonarduk.com>
 * @since 22-May-2016
 */
package uk.co.sleonard.unison.input;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.junit.Test;

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
public class NewsClientTest {

	/**
	 * @throws IOException
	 * @throws SocketException
	 *
	 */
	@Test(expected = UnknownHostException.class)
	public void testConnectForInvalidServer() throws SocketException, IOException {
		final NewsClient client = new NewsClient();
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
		final NewsClient client = new NewsClient();
		final String[] servers = StringUtils.loadServerList();
		for (final String hostname : servers) {
			client.connect(hostname, 119);
		}
	}

}
