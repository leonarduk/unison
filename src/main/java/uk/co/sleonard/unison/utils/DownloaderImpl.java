/**
 * DownloaderImpl
 *
 * @author ${author}
 * @since 16-Jun-2016
 */
package uk.co.sleonard.unison.utils;

import java.util.concurrent.LinkedBlockingQueue;

import org.hibernate.Session;

import uk.co.sleonard.unison.UNISoNController;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.UNISoNLogger;
import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.input.FullDownloadWorker;
import uk.co.sleonard.unison.input.NewsArticle;
import uk.co.sleonard.unison.input.NewsClient;
import uk.co.sleonard.unison.input.NewsClientImpl;
import uk.co.sleonard.unison.input.NewsGroupReader;

public class DownloaderImpl implements Downloader {

	private final String							nntpHost;
	private final LinkedBlockingQueue<NewsArticle>	queue;
	private final NewsClient						newsClient;
	private final NewsGroupReader					nntpReader;
	private final HibernateHelper					helper;
	private final Session							session;

	public DownloaderImpl() {
		this(UNISoNController.getInstance().getNntpHost(),
		        UNISoNController.getInstance().getQueue(), new NewsClientImpl(),
		        UNISoNController.getInstance().getNntpReader(),
		        UNISoNController.getInstance().getHelper(),
		        UNISoNController.getInstance().getSession());
	}

	public DownloaderImpl(final String nntpHost, final LinkedBlockingQueue<NewsArticle> queue1,
	        final NewsClient newsClient1, final NewsGroupReader reader,
	        final HibernateHelper helper2, final Session session2) {
		this.nntpHost = nntpHost;
		this.queue = queue1;
		this.newsClient = newsClient1;
		this.nntpReader = reader;
		this.helper = helper2;
		this.session = session2;
	}

	@Override
	public void addDownloadRequest(final String usenetID, final DownloadMode mode,
	        final UNISoNLogger log1) throws UNISoNException {
		FullDownloadWorker.addDownloadRequest(usenetID, mode, log1, this.nntpHost, this.queue,
		        this.newsClient, this.nntpReader, this.helper, this.session);
	}

}
