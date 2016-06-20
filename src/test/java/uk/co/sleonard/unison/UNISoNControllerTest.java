/**
 * UNISoNControllerTest
 *
 * @author ${author}
 * @since 17-Jun-2016
 */
package uk.co.sleonard.unison;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.input.HeaderDownloadWorker;
import uk.co.sleonard.unison.input.NewsClient;
import uk.co.sleonard.unison.input.NewsGroupReader;
import uk.co.sleonard.unison.input.UNISoNCLI;

public class UNISoNControllerTest {

	private UNISoNController controller;

	@Before
	public void setUp() throws Exception {
		final JFrame frame = Mockito.mock(JFrame.class);
		this.controller = UNISoNController.create(frame);
	}

	@Test
	public final void testQuickDownload() throws UNISoNException {
		final Set<NewsGroup> groups = new HashSet<>();
		final NewsGroup parentNewsGroup = null;
		final Set<Topic> topics = null;
		final Set<Message> messages = null;
		final int lastIndexDownloaded = 1;
		final int lastMessageTotal = 2;
		final int firstMessage = 1;
		final int lastMessage = 2;
		final String fullName = "alt.news";
		final boolean lastNode = true;
		groups.add(new NewsGroup("alt.news", parentNewsGroup, topics, messages, lastIndexDownloaded,
		        lastMessageTotal, firstMessage, lastMessage, fullName, lastNode));
		final Date fromDate1 = null;
		final Date toDate1 = null;
		final UNISoNLogger log = new UNISoNCLI();
		final DownloadMode mode = DownloadMode.ALL;
		this.controller.setNntpHost("testNNTP");
		final NewsGroupReader reader = Mockito.mock(NewsGroupReader.class);
		final NewsClient client = Mockito.mock(NewsClient.class);
		final HeaderDownloadWorker downloadWorker = Mockito.mock(HeaderDownloadWorker.class);
		this.controller.setHeaderDownloader(downloadWorker);
		Mockito.when(reader.getClient()).thenReturn(client);
		this.controller.setNntpReader(reader);
		this.controller.quickDownload(groups, fromDate1, toDate1, log, mode);

	}

	@Test
	public final void testStopDownload() {
		this.controller.stopDownload();
	}

}
