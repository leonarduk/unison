package uk.co.sleonard.unison.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import uk.co.sleonard.unison.UNISoNController;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.input.FullDownloadWorker;
import uk.co.sleonard.unison.input.NewsArticle;
import uk.co.sleonard.unison.input.NewsClient;
import uk.co.sleonard.unison.input.NewsGroupReader;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Tests for {@link DownloaderImpl}.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(FullDownloadWorker.class)
public class DownloaderImplTest {

    /**
     * Ensures that addDownloadRequest delegates to FullDownloadWorker with correct parameters.
     */
    @Test
    public void testAddDownloadRequestDelegatesCorrectly() throws UNISoNException {
        String nntpHost = "host";
        LinkedBlockingQueue<NewsArticle> queue = new LinkedBlockingQueue<>();
        NewsClient newsClient = Mockito.mock(NewsClient.class);
        NewsGroupReader reader = Mockito.mock(NewsGroupReader.class);
        HibernateHelper helper = Mockito.mock(HibernateHelper.class);

        PowerMockito.mockStatic(FullDownloadWorker.class);

        UNISoNController controller = Mockito.mock(UNISoNController.class);
        DownloaderImpl downloader = new DownloaderImpl(nntpHost, queue, newsClient, reader, helper, controller);
        String usenetId = "<123>";
        DownloadMode mode = DownloadMode.ALL;

        downloader.addDownloadRequest(usenetId, mode);

        PowerMockito.verifyStatic(FullDownloadWorker.class, Mockito.times(1));
        FullDownloadWorker.addDownloadRequest(usenetId, mode, nntpHost, queue, newsClient, reader, helper, controller);
    }
}
