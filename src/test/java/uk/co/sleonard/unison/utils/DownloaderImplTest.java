package uk.co.sleonard.unison.utils;

/**
 * Tests for {@link DownloaderImpl}.
 */
public class DownloaderImplTest {

    /**
     * Ensures that addDownloadRequest delegates to FullDownloadWorker with correct parameters.
     */
//    @Test
//    public void testAddDownloadRequestDelegatesCorrectly() throws UNISoNException {
//        String nntpHost = "host";
//        LinkedBlockingQueue<NewsArticle> queue = new LinkedBlockingQueue<>();
//        NewsClient newsClient = Mockito.mock(NewsClient.class);
//        NewsGroupReader reader = Mockito.mock(NewsGroupReader.class);
//        HibernateHelper helper = Mockito.mock(HibernateHelper.class);
//
//        try (MockedStatic<FullDownloadWorker> mocked = Mockito.mockStatic(FullDownloadWorker.class)) {
//            @NotNull UNISoNController controller = Mockito.mock(UNISoNController.class);
//             DownloaderImpl downloader = new DownloaderImpl(nntpHost, queue, newsClient, reader, helper, controller);
//            String usenetId = "<123>";
//            DownloadMode mode = DownloadMode.ALL;
//
//            downloader.addDownloadRequest(usenetId, mode);
//
//            mocked.verify(() ->
//                            FullDownloadWorker
//                                    .addDownloadRequest(
//                                            usenetId,
//                                            mode,
//                                            nntpHost,
//                                            queue,
//                                            newsClient,
//                                            reader,
//                                            helper,
//                                            controller),
//                    Mockito.times(1));
//        }
//    }
}
