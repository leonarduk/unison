package uk.co.sleonard.unison.input;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link SwingWorker}.
 */
public class SwingWorkerTest {

    private static class TestSwingWorker extends SwingWorker {
        private final CountDownLatch constructLatch;
        private final CountDownLatch finishedLatch;
        volatile boolean constructCalled = false;
        volatile boolean finishedCalled = false;

        TestSwingWorker(CountDownLatch constructLatch, CountDownLatch finishedLatch) {
            super("TestSwingWorker");
            this.constructLatch = constructLatch;
            this.finishedLatch = finishedLatch;
        }

        @Override
        public Object construct() {
            constructCalled = true;
            constructLatch.countDown();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // ignore
            }
            return null;
        }

        @Override
        public void finished() {
            finishedCalled = true;
            finishedLatch.countDown();
        }

        Thread getThread() throws Exception {
            Field field = SwingWorker.class.getDeclaredField("threadVar");
            field.setAccessible(true);
            SwingWorker.ThreadVar tv = (SwingWorker.ThreadVar) field.get(this);
            return tv.get();
        }
    }

    @Test
    public void testFinishedRunsOnCompletion() throws Exception {
        CountDownLatch constructLatch = new CountDownLatch(1);
        CountDownLatch finishedLatch = new CountDownLatch(1);
        TestSwingWorker worker = new TestSwingWorker(constructLatch, finishedLatch);
        worker.start();
        assertTrue(constructLatch.await(1, TimeUnit.SECONDS), "Construct never started");
        assertTrue(finishedLatch.await(2, TimeUnit.SECONDS) && worker.finishedCalled,
                "Finished did not execute");
    }

    @Test
    public void testInterruptClearsThread() throws Exception {
        CountDownLatch constructLatch = new CountDownLatch(1);
        CountDownLatch finishedLatch = new CountDownLatch(1);
        TestSwingWorker worker = new TestSwingWorker(constructLatch, finishedLatch);
        worker.start();
        assertTrue(constructLatch.await(1, TimeUnit.SECONDS));
        worker.interrupt();
        assertNull(worker.getThread());
        finishedLatch.await(1, TimeUnit.SECONDS);
    }
}

