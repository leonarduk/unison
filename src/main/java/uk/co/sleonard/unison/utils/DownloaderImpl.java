/**
 * DownloaderImpl
 *
 * @author ${author}
 * @since 16-Jun-2016
 */
package uk.co.sleonard.unison.utils;

import org.jetbrains.annotations.NotNull;
import uk.co.sleonard.unison.UNISoNController;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.input.FullDownloadWorker;
import uk.co.sleonard.unison.input.NewsArticle;
import uk.co.sleonard.unison.input.NewsClient;
import uk.co.sleonard.unison.input.NewsGroupReader;

import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;

public class DownloaderImpl implements Downloader {

    private final String nntpHost;
    private final LinkedBlockingQueue<NewsArticle> queue;
    private final NewsClient newsClient;
    private final NewsGroupReader nntpReader;
    private final HibernateHelper helper;
    private final UNISoNController controller;

    public DownloaderImpl(final @NotNull String nntpHost,
                          final @NotNull LinkedBlockingQueue<NewsArticle> queue1,
                          final @NotNull NewsClient newsClient1,
                          final @NotNull NewsGroupReader reader,
                          final @NotNull HibernateHelper helper2,
                          final @NotNull UNISoNController controller) {
        this.nntpHost = Objects.requireNonNull(nntpHost, "nntpHost");
        this.queue = Objects.requireNonNull(queue1, "queue");
        this.newsClient = Objects.requireNonNull(newsClient1, "newsClient");
        this.nntpReader = Objects.requireNonNull(reader, "reader");
        this.helper = Objects.requireNonNull(helper2, "helper");
        this.controller = Objects.requireNonNull(controller, "controller");
    }

    @Override
    public void addDownloadRequest(final @NotNull String usenetID, final @NotNull DownloadMode mode)
            throws UNISoNException {
        FullDownloadWorker.addDownloadRequest(Objects.requireNonNull(usenetID, "usenetID"),
                Objects.requireNonNull(mode, "mode"), this.nntpHost, this.queue,
                this.newsClient, this.nntpReader, this.helper, this.controller);
    }

}
