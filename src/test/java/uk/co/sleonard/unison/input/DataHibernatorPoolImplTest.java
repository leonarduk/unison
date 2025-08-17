package uk.co.sleonard.unison.input;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Tests for {@link DataHibernatorPoolImpl}.
 */
class DataHibernatorPoolImplTest {

    /**
     * Verifies stopAllDownloads delegates to DataHibernatorWorker.stopDownload().
     */
    @Test
    void testStopAllDownloadsDelegates() {
        DataHibernatorWorker mocked = Mockito.mock(DataHibernatorWorker.class);
            DataHibernatorPool pool = new DataHibernatorPoolImpl();
            pool.stopAllDownloads();

        Mockito.verify(mocked, Mockito.atLeastOnce()).stopDownload();

    }
}
