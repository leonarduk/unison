/**
 * DataHibernatorPool
 *
 * @author ${author}
 * @since 19-Jun-2016
 */
package uk.co.sleonard.unison.input;

public interface DataHibernatorPool {

  /** Requests that all active download processes terminate. */
  void stopAllDownloads();
}
