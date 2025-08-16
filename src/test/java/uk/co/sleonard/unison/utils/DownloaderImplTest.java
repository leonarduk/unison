package uk.co.sleonard.unison.utils;

import org.hibernate.Session;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.input.FullDownloadWorker;
import uk.co.sleonard.unison.input.NewsArticle;
import uk.co.sleonard.unison.input.NewsClient;
import uk.co.sleonard.unison.input.NewsGroupReader;

import java.util.concurrent.LinkedBlockingQueue;

public class DownloaderImplTest {

    @Test
    public void testAddDownloadRequest() throws UNISoNException {
        final String host = "host";
        final LinkedBlockingQueue<NewsArticle> queue = new LinkedBlockingQueue<>();
        final NewsClient client = Mockito.mock(NewsClient.class);
        final NewsGroupReader reader = Mockito.mock(NewsGroupReader.class);
        final HibernateHelper helper = Mockito.mock(HibernateHelper.class);
        final Session session = Mockito.mock(Session.class);
        final DownloaderImpl downloader = new DownloaderImpl(host, queue, client, reader, helper, session);

        final String usenetId = "123";
        final DownloadMode mode = DownloadMode.BASIC;
        try (MockedStatic<FullDownloadWorker> worker = Mockito.mockStatic(FullDownloadWorker.class)) {
            downloader.addDownloadRequest(usenetId, mode);
            worker.verify(() -> FullDownloadWorker.addDownloadRequest(usenetId, mode, host, queue, client, reader, helper, session));
        }
    }
}

