package uk.co.sleonard.unison.input;

import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import uk.co.sleonard.unison.datahandling.HibernateHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tests for {@link DataHibernatorWorker}.
 */
public class DataHibernatorWorkerTest {

    @SuppressWarnings({"unchecked", "rawtypes"})
    @BeforeEach
    public void clearWorkers() throws Exception {
        Field workersField = DataHibernatorWorker.class.getDeclaredField("workers");
        workersField.setAccessible(true);
        ArrayList<DataHibernatorWorker> workers = (ArrayList<DataHibernatorWorker>) workersField.get(null);
        workers.clear();
    }

    /**
     * Verifies starting and stopping hibernators manipulates worker threads correctly.
     */
    @Test
    public void testStartAndStopHibernators() throws Exception {
        // access private fields via reflection
        Field workersField = DataHibernatorWorker.class.getDeclaredField("workers");
        workersField.setAccessible(true);
        Field numField = DataHibernatorWorker.class.getDeclaredField("numberofHibernators");
        numField.setAccessible(true);
        int expectedSize = numField.getInt(null);

        // mocked dependencies
        NewsGroupReader reader = Mockito.mock(NewsGroupReader.class);
        Mockito.when(reader.getNumberOfMessages()).thenReturn(0);
        HibernateHelper helper = Mockito.mock(HibernateHelper.class);
        LinkedBlockingQueue queue = Mockito.mock(LinkedBlockingQueue.class);
        Mockito.when(queue.isEmpty()).thenReturn(true);
        Session session = Mockito.mock(Session.class);

        // start workers
        DataHibernatorWorker.startHibernators(reader, helper, queue, session);

        ArrayList<DataHibernatorWorker> workers = (ArrayList<DataHibernatorWorker>) workersField.get(null);
        Assertions.assertEquals(expectedSize, workers.size());

        // capture threads for state inspection
        Field threadVarField = SwingWorker.class.getDeclaredField("threadVar");
        threadVarField.setAccessible(true);
        List<Thread> threads = new ArrayList<>();
        for (DataHibernatorWorker worker : workers) {
            SwingWorker.ThreadVar tv = (SwingWorker.ThreadVar) threadVarField.get(worker);
            threads.add(tv.get());
        }

        // stop workers
        DataHibernatorWorker.stopDownload();

        // ensure all threads terminated
        for (Thread t : threads) {
            t.join(1000);
            Assertions.assertFalse(t.isAlive());
        }

        workers.clear();
    }

    /**
     * Ensures that an interrupted worker stops processing queued messages.
     */
    @Test
    public void testInterruptStopsProcessing() throws Exception {
        LinkedBlockingQueue<NewsArticle> queue = new LinkedBlockingQueue<>();
        for (int i = 0; i < 5; i++) {
            NewsArticle article = new NewsArticle();
            article.setArticleId("id" + i);
            queue.add(article);
        }

        NewsGroupReader reader = Mockito.mock(NewsGroupReader.class);
        AtomicInteger stored = new AtomicInteger();
        Mockito.doAnswer(invocation -> {
            stored.incrementAndGet();
            return null;
        }).when(reader).incrementMessagesStored();
        Mockito.when(reader.getNumberOfMessages()).thenReturn(0);

        HibernateHelper helper = Mockito.mock(HibernateHelper.class);
        Mockito.when(helper.hibernateData(Mockito.any(NewsArticle.class), Mockito.any(Session.class)))
                .thenAnswer(invocation -> {
                    Thread.sleep(100);
                    return true;
                });

        Session session = Mockito.mock(Session.class);

        Constructor<DataHibernatorWorker> ctor = DataHibernatorWorker.class
                .getDeclaredConstructor(NewsGroupReader.class, HibernateHelper.class,
                        LinkedBlockingQueue.class, Session.class);
        ctor.setAccessible(true);
        DataHibernatorWorker worker = ctor
                .newInstance(reader, helper, queue, session);

        Field threadVarField = SwingWorker.class.getDeclaredField("threadVar");
        threadVarField.setAccessible(true);
        SwingWorker.ThreadVar tv = (SwingWorker.ThreadVar) threadVarField.get(worker);
        Thread thread = tv.get();

        Thread.sleep(150);
        worker.interrupt();
        thread.join(1000);

        int processed = stored.get();
        Assertions.assertTrue("Worker should process at least one message before interrupt",
                processed > 0);
        Assertions.assertTrue("Worker should not process all messages after interrupt",
                processed < 5);
    }
}
