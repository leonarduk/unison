/**
 * UNISoNControllerTest
 *
 * @author ${author}
 * @since 17-Jun-2016
 */
package uk.co.sleonard.unison;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.input.*;

import javax.swing.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class UNISoNControllerTest {

    private UNISoNController controller;
    private NewsGroupReader reader;
    private NewsClient client;

    public void doDownload(final boolean locationSelected, final boolean getTextSelected) {
        final NewsGroup[] items = {new NewsGroup("alt,news", null, new HashSet<>(),
                new HashSet<>(), 1, 2, 1, 2, "alt.news", true)};
        final String fromDateString = "2016-06-06";
        this.doDownload(locationSelected, getTextSelected, items, fromDateString);
    }

    public void doDownload(final boolean locationSelected, final boolean getTextSelected,
                           final NewsGroup[] items, final String fromDateString) {
        final StatusMonitor monitor = Mockito.mock(StatusMonitor.class);
        final UNISoNLogger logger = new UNISoNCLI();
        final String toDateString = "2016-09-09";
        this.controller.download(monitor, items, fromDateString, toDateString, logger,
                locationSelected, getTextSelected);
    }

    @Before
    public void setUp() throws Exception {
        final JFrame frame = Mockito.mock(JFrame.class);
        this.controller = UNISoNController.create(frame, Mockito.mock(DataHibernatorPool.class));
        this.controller.setNntpHost("testNNTP");
        this.reader = Mockito.mock(NewsGroupReader.class);
        this.client = Mockito.mock(NewsClient.class);
        final HeaderDownloadWorker downloadWorker = Mockito.mock(HeaderDownloadWorker.class);
        this.controller.setHeaderDownloader(downloadWorker);
        Mockito.when(this.reader.getClient()).thenReturn(this.client);
        this.controller.setNntpReader(this.reader);

    }

    @Test
    public void testAvailableGroupsModel() throws Exception {
        Assert.assertEquals(0, this.controller.getAvailableGroupsModel(null).getSize());
        final Set<NewsGroup> availableGroups2 = new HashSet<>();
        Assert.assertEquals(0, this.controller.getAvailableGroupsModel(availableGroups2).getSize());
        availableGroups2.add(Mockito.mock(NewsGroup.class));
        Assert.assertEquals(1, this.controller.getAvailableGroupsModel(availableGroups2).getSize());
    }

    @Test
    public final void testDownload() {
        final boolean locationSelected = true;
        final boolean getTextSelected = true;
        this.doDownload(locationSelected, getTextSelected);
    }

    @Test
    public final void testDownloadEmptyGroup() {
        final boolean locationSelected = true;
        final boolean getTextSelected = true;
        this.doDownload(locationSelected, getTextSelected, new NewsGroup[]{}, "2016-06-06");
    }

    @Test
    public final void testDownloadException() throws UNISoNException {
        final boolean locationSelected = true;
        final boolean getTextSelected = true;
        Mockito.doThrow(new UNISoNException("test")).when(this.client).reconnect();
        this.doDownload(locationSelected, getTextSelected);
    }

    @Test
    public final void testDownloadInvalidDate() {
        final boolean locationSelected = true;
        final boolean getTextSelected = true;
        this.doDownload(locationSelected, getTextSelected,
                new NewsGroup[]{Mockito.mock(NewsGroup.class)}, "Friday next week");
    }

    @Test
    public final void testDownloadTextLocationNotSelected() {
        final boolean locationSelected = false;
        final boolean getTextSelected = false;
        this.doDownload(locationSelected, getTextSelected);
    }

    @Test
    public final void testDownloadTextNotSelected() {
        final boolean locationSelected = true;
        final boolean getTextSelected = false;
        this.doDownload(locationSelected, getTextSelected);
    }

    @Test
    public final void testDownloadWithExtrasNullItems() {
        final StatusMonitor monitor = Mockito.mock(StatusMonitor.class);
        final UNISoNLogger logger = new UNISoNCLI();
        this.controller.download(monitor, null, "2016-06-06", "2016-09-09", logger, true,
                false);
    }

    @Test
    public final void testDownloadWithExtrasUsesHeadersMode() throws Exception {
        final StatusMonitor monitor = Mockito.mock(StatusMonitor.class);
        final UNISoNLogger logger = new UNISoNCLI();
        final NewsGroup[] items = {new NewsGroup("alt,news", null, new HashSet<>(),
                new HashSet<>(), 1, 2, 1, 2, "alt.news", true)};
        final UNISoNController spy = Mockito.spy(this.controller);
        Mockito.doNothing().when(spy).quickDownload(Mockito.anySet(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any());
        spy.download(monitor, items, "2016-06-06", "2016-09-09", logger, true, false);
        Mockito.verify(spy).quickDownload(Mockito.anySet(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.eq(DownloadMode.HEADERS));
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
        this.controller.quickDownload(groups, fromDate1, toDate1, log, mode);

    }

    @Test
    public void testRequestDownload() throws Exception {
        final String group = "group";
        final String host = "host";
        final StatusMonitor monitor = Mockito.mock(StatusMonitor.class);
        final UNISoNLogger logger = new UNISoNCLI();
        final NewsClient client = Mockito.mock(NewsClient.class);
        this.controller.requestDownload(group, host, monitor, logger, client);
    }

    @Test
    public final void testStopDownload() {
        this.controller.stopDownload();
    }

}
