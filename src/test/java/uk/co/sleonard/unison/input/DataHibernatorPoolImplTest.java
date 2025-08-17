package uk.co.sleonard.unison.input;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

/**
 * Tests for {@link DataHibernatorPoolImpl}.
 */
public class DataHibernatorPoolImplTest {

    /**
     * Verifies stopAllDownloads delegates to DataHibernatorWorker.stopDownload().
     */
    @Test
    public void testStopAllDownloadsDelegates() {
        try (MockedStatic<DataHibernatorWorker> mocked = Mockito.mockStatic(DataHibernatorWorker.class)) {
            DataHibernatorPool pool = new DataHibernatorPoolImpl();
            pool.stopAllDownloads();

            mocked.verify(() -> DataHibernatorWorker.stopDownload(), Mockito.times(1));
        }
    }
}
