package uk.co.sleonard.unison.input;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
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
        DataHibernatorPool pool = new DataHibernatorPoolImpl();
        try (MockedStatic<DataHibernatorWorker> mock = Mockito.mockStatic(DataHibernatorWorker.class)) {
            pool.stopAllDownloads();
            mock.verify(DataHibernatorWorker::stopDownload, Mockito.times(1));
        }
    }
}
