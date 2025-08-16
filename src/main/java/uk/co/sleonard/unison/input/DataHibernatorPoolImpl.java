/**
 * DataHibernatorPoolImpl
 *
 * @author ${author}
 * @since 19-Jun-2016
 */
package uk.co.sleonard.unison.input;

public class DataHibernatorPoolImpl implements DataHibernatorPool {
    @Override
    public void stopAllDownloads() {
        DataHibernatorWorker.stopDownload();
    }

}
