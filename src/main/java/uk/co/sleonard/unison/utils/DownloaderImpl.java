/**
 * DownloaderImpl
 *
 * @author ${author}
 * @since 16-Jun-2016
 */
package uk.co.sleonard.unison.utils;

import uk.co.sleonard.unison.UNISoNController;
import org.hibernate.Session;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.input.*;

import java.util.concurrent.LinkedBlockingQueue;

public class DownloaderImpl implements Downloader {

    private final String nntpHost;
    private final LinkedBlockingQueue<NewsArticle> queue;
    private final NewsClient newsClient;
    private final NewsGroupReader nntpReader;
    private final HibernateHelper helper;

    public DownloaderImpl() {
        this(UNISoNController.getInstance().getNntpHost(),
                UNISoNController.getInstance().getQueue(), new NewsClientImpl(),
                UNISoNController.getInstance().getNntpReader(),
                UNISoNController.getInstance().getHelper());
    }

    public DownloaderImpl(final String nntpHost, final LinkedBlockingQueue<NewsArticle> queue1,
                          final NewsClient newsClient1, final NewsGroupReader reader,
                          final HibernateHelper helper2) {
        this.nntpHost = nntpHost;
        this.queue = queue1;
        this.newsClient = newsClient1;
        this.nntpReader = reader;
        this.helper = helper2;
    }

    @Override
    public void addDownloadRequest(final String usenetID, final DownloadMode mode) throws UNISoNException {
        FullDownloadWorker.addDownloadRequest(usenetID, mode, this.nntpHost, this.queue,
                this.newsClient, this.nntpReader, this.helper);
    }

}
