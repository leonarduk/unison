///**
// * DataHibernatorPoolImplTest
// */
//package uk.co.sleonard.unison.input;
//
//import org.junit.Test;
//import org.mockito.MockedStatic;
//import org.mockito.Mockito;
//
///**
// * Tests for {@link DataHibernatorPoolImpl}.
// */
//public class DataHibernatorPoolImplTest {
//
//    /**
//     * Ensure that stopAllDownloads calls DataHibernatorWorker.stopDownload.
//     */
//    @Test
//    public void testStopAllDownloadsInvokesWorker() {
//        try (MockedStatic<DataHibernatorWorker> worker = Mockito.mockStatic(DataHibernatorWorker.class)) {
//            DataHibernatorPoolImpl pool = new DataHibernatorPoolImpl();
//
//            pool.stopAllDownloads();
//
//            worker.verify(DataHibernatorWorker::stopDownload, Mockito.times(1));
//        }
//    }
//}
