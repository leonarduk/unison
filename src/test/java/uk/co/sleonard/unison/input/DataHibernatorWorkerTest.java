package uk.co.sleonard.unison.input;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import uk.co.sleonard.unison.datahandling.HibernateHelper;

/**
 * Tests for {@link DataHibernatorWorker}.
 */
public class DataHibernatorWorkerTest {

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Before
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
        Assert.assertEquals(expectedSize, workers.size());

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
            Assert.assertFalse(t.isAlive());
        }

        workers.clear();
    }
}
