package uk.co.sleonard.unison.input;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/** Tests for {@link DataHibernatorPoolImpl}. */
@RunWith(PowerMockRunner.class)
@PrepareForTest(DataHibernatorWorker.class)
public class DataHibernatorPoolImplTest {

  /** Verifies stopAllDownloads delegates to DataHibernatorWorker.stopDownload(). */
  @Test
  public void testStopAllDownloadsDelegates() {
    PowerMockito.mockStatic(DataHibernatorWorker.class);

    DataHibernatorPool pool = new DataHibernatorPoolImpl();
    pool.stopAllDownloads();

    PowerMockito.verifyStatic(DataHibernatorWorker.class, Mockito.times(1));
    DataHibernatorWorker.stopDownload();
  }
}
