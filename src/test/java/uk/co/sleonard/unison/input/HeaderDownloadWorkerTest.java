/**
 * HeaderDownloadWorkerTest
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.input;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import uk.co.sleonard.unison.UNISoNController;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.utils.Downloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Class HeaderDownloadWorker.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 */
@Slf4j
class HeaderDownloadWorkerTest {

    private HeaderDownloadWorker worker;

    /**
     * Setup.
     */
    @BeforeEach
    void setUp() {
        this.worker = new HeaderDownloadWorker(new LinkedBlockingQueue<>(),
                Mockito.mock(Downloader.class));
    }

    /**
     * Test if download then finished.
     */
    @Test
    void testFinished() {
        this.worker.resume();
        Assertions.assertTrue(this.worker.isDownloading());
        this.worker.finished();
        Assertions.assertFalse(this.worker.isDownloading());
    }

    /**
     * Test fullStop
     */
    @Test
    void testFullstop() {
        log.info("Wait 2 secs and stop");
        try {
            Thread.sleep(2000);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Stop");
        this.worker.fullstop();
    }

    /**
     * Test Initialize
     *
     * @throws UNISoNException Signals that an exception has occurred.
     */
    @Test

    void testInitialise() throws UNISoNException {

        final NewsClient nc = Mockito.mock(NewsClient.class);
        final NewsGroupReader ngr = new NewsGroupReader(nc, Mockito.mock(UNISoNController.class));
        final Date fromAndTo = Calendar.getInstance().getTime();
        Assertions.assertFalse(this.worker.isDownloading());

        this.worker.initialise(ngr, 0, 1, "server", "newsgroup", DownloadMode.ALL,
                fromAndTo, fromAndTo);

        Assertions.assertTrue(this.worker.isDownloading());
    }

    /**
     * Test if in download
     */
    @Test
    void testIsDownloading() {
        Assertions.assertFalse(this.worker.isDownloading());
        this.worker.resume();
        Assertions.assertTrue(this.worker.isDownloading());
    }

    /**
     * Test listener notification.
     */
    @Test
    void testNotifyListeners() {
        final AtomicInteger counter = new AtomicInteger(0);
        this.worker.addDataChangeListener(evt -> counter.incrementAndGet());
        this.worker.notifyListeners();
        Assertions.assertEquals(1, counter.get());
    }

    /**
     * Test Pause
     */
    @Test
    void testPause() {
        this.worker.resume();
        Assertions.assertTrue(this.worker.isDownloading());
        this.worker.pause();
        Assertions.assertFalse(this.worker.isDownloading());
    }

//    @Test()
//    void testQueueMessagesAbort() throws IOException, UNISoNException {
//        final LinkedBlockingQueue<NewsArticle> queue1 = new LinkedBlockingQueue<>();
//        final String messages = "sdsds";
//        this.worker.fullstop();
//        try (Reader reader = new StringReader(messages);) {
//            this.worker.queueMessages(queue1, reader);
//        }
//    }

    @Test
    void testQueueMessagesAllDownload() throws IOException, UNISoNException {
        final LinkedBlockingQueue<NewsArticle> queue1 = new LinkedBlockingQueue<>();
        final StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            buffer.append(i);
            buffer.append("\t");
            buffer.append("This is the subject\t");
            buffer.append("sender@email.com\t" + "2016-04-01\t");
            buffer.append("1234\t");
            buffer.append("123@email.com");
        }
        this.worker.resume();
        this.worker.setMode(DownloadMode.ALL);
        try (Reader reader = new StringReader(buffer.toString());) {
            this.worker.queueMessages(queue1, reader);
        }
    }

    @Test
    void testQueueMessagesBasicDownload() throws IOException, UNISoNException {
        final LinkedBlockingQueue<NewsArticle> queue1 = new LinkedBlockingQueue<>();
        final StringBuilder buffer = new StringBuilder();
        buffer.append("300\t");
        buffer.append("This is the subject\t");
        buffer.append("sender@email.com\t" + "2016-04-01\t");
        buffer.append("1234\t");
        buffer.append("123@email.com");
        this.worker.resume();
        this.worker.setMode(DownloadMode.BASIC);
        try (Reader reader = new StringReader(buffer.toString());) {
            this.worker.queueMessages(queue1, reader);
        }
    }

    @Test
    void testProcessMessageSetsNewsgroup() throws Exception {
        final LinkedBlockingQueue<NewsArticle> queue = new LinkedBlockingQueue<>();
        final NewsClient nc = Mockito.mock(NewsClient.class);
        final NewsGroupReader ngr = new NewsGroupReader(nc, Mockito.mock(UNISoNController.class));

        this.worker.initialise(ngr, 0, 1, "server", "alt.test", DownloadMode.BASIC, null, null);

        final String line = "1\tSubject\tfrom@example.com\t2016-04-01\t<id>\t<ref>";
        this.worker.processMessage(queue, line);

        final NewsArticle article = queue.poll();
        Assertions.assertNotNull(article);
        Assertions.assertEquals("alt.test", article.getNewsgroups());
    }

    /**
     * Test construct
     *
     * @throws InterruptedException
     * @throws UNISoNException
     * @throws IOException
     */

    @Test
    void testQueueMessagesNullReader()
            throws InterruptedException, IOException, UNISoNException {
        Assertions.assertTrue(this.worker.queueMessages(new LinkedBlockingQueue<>(), null));
    }

    /**
     * Test Resume
     */
    @Test
    void testResume() {
        Assertions.assertFalse(this.worker.isDownloading());
        this.worker.resume();
        Assertions.assertTrue(this.worker.isDownloading());
    }

    /**
     * Test sToreArticleInfo
     *
     * @throws UNISoNException Signals that an exception has occurred.
     */
    @Test
    void testStoreArticleInfo() throws UNISoNException {
        final LinkedBlockingQueue<NewsArticle> queue = new LinkedBlockingQueue<>();
        final boolean actual = this.worker.storeArticleInfo(queue);
        Assertions.assertTrue(actual);
    }

    @Test
    void testStoreArticleInfoDoesNotRequestBeyondEndIndex() throws Exception {
        final LinkedBlockingQueue<NewsArticle> queue = new LinkedBlockingQueue<>();

        final NewsClient client = Mockito.mock(NewsClient.class);
        final NewsGroupReader reader = new NewsGroupReader(client, Mockito.mock(UNISoNController.class));
        Mockito.when(client.retrieveArticleInfo(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(new BufferedReader(new StringReader("")));

        // configure worker
        this.worker.resume();
        setField(this.worker, "startIndex", 0);
        setField(this.worker, "endIndex", 600);
        setField(this.worker, "newsReader", reader);

        // ensure complete article information is downloaded
        this.worker.setMode(DownloadMode.ALL);

        this.worker.storeArticleInfo(queue);

        final ArgumentCaptor<Long> endCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(client, Mockito.atLeastOnce())
                .retrieveArticleInfo(Mockito.anyLong(), endCaptor.capture());

        for (final Long end : endCaptor.getAllValues()) {
            Assertions.assertTrue(end <= 600L);
        }
    }

    @Test
    void testResumeTriggersStoreArticleInfo() throws Exception {
        final HeaderDownloadWorker spyWorker = Mockito.spy(
                new HeaderDownloadWorker(new LinkedBlockingQueue<>(),
                        Mockito.mock(Downloader.class)));
        final CountDownLatch latch = new CountDownLatch(1);
        Mockito.doAnswer(invocation -> {
            latch.countDown();
            return true;
        }).when(spyWorker).storeArticleInfo(Mockito.any(LinkedBlockingQueue.class));

        spyWorker.initialise();
        spyWorker.resume();

        try {
            Assertions.assertTrue(latch.await(1, TimeUnit.SECONDS),
                    "storeArticleInfo was not called");
            Mockito.verify(spyWorker).storeArticleInfo(Mockito.any(LinkedBlockingQueue.class));
        } finally {
            spyWorker.fullstop();
        }
    }

    @Test
    void testAwaitCompletion() throws Exception {
        class TestWorker extends HeaderDownloadWorker {
            TestWorker(LinkedBlockingQueue<NewsArticle> q, Downloader d) {
                super(q, d);
            }

            @Override
            boolean storeArticleInfo(final LinkedBlockingQueue<NewsArticle> q1) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return true;
            }
        }

        final TestWorker worker = new TestWorker(new LinkedBlockingQueue<>(),
                Mockito.mock(Downloader.class));
        final NewsClient client = Mockito.mock(NewsClient.class);
        final NewsGroupReader ngr = new NewsGroupReader(client,
                Mockito.mock(UNISoNController.class));
        worker.initialise();
        worker.initialise(ngr, 0, 1, "server", "group", DownloadMode.BASIC, null, null);
        final long start = System.currentTimeMillis();
        worker.awaitCompletion();
        final long elapsed = System.currentTimeMillis() - start;
        Assertions.assertTrue(elapsed >= 200, () -> "Elapsed " + elapsed);
        worker.fullstop();
    }

    private void setField(final Object target, final String fieldName, final Object value) throws Exception {
        final Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
