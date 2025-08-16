package uk.co.sleonard.unison.input;

import org.junit.Test;
import org.mockito.Mockito;
import uk.co.sleonard.unison.utils.Downloader;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Tests that setting downloading to true triggers storeArticleInfo.
 */
public class HeaderDownloadWorkerInvokeTest {

    @Test
    public void testDownloadingTriggersStoreArticleInfo() throws Exception {
        LinkedBlockingQueue<NewsArticle> queue = new LinkedBlockingQueue<>();
        Downloader downloader = Mockito.mock(Downloader.class);
        HeaderDownloadWorker worker = Mockito.spy(new HeaderDownloadWorker(queue, downloader));

        Mockito.doAnswer(invocation -> {
            worker.fullStop();
            return true;
        }).when(worker).storeArticleInfo(Mockito.any());

        worker.resume();
        worker.construct();

        Mockito.verify(worker, Mockito.times(1)).storeArticleInfo(Mockito.any());
    }
}
