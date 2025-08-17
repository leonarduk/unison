package uk.co.sleonard.unison.utils;

import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
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

        try (MockedStatic<FullDownloadWorker> mocked = Mockito.mockStatic(FullDownloadWorker.class)) {
            DownloaderImpl downloader = new DownloaderImpl(nntpHost, queue, newsClient, reader, helper);
            String usenetId = "<123>";
            DownloadMode mode = DownloadMode.ALL;

            downloader.addDownloadRequest(usenetId, mode);

            mocked.verify(() -> FullDownloadWorker.addDownloadRequest(usenetId, mode, nntpHost, queue, newsClient, reader, helper),
                    Mockito.times(1));
        }
    }
}
