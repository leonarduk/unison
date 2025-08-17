/**
 * FullDownloadWorkerTest
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.input;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.datahandling.HibernateHelper;

/**
 * The Class FullDownloadWorkerTest.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 */
public class FullDownloadWorkerTest {

  /** The worker. */
  private FullDownloadWorker worker;

  private NewsClient newsClient;
  private LinkedBlockingQueue outQueue;

  /**
   * Setup.
   *
   * @throws Exception the exception
   */
  @Before
  public void setUp() throws Exception {
    this.newsClient = Mockito.mock(NewsClient.class);
    this.outQueue = new LinkedBlockingQueue<>();
    this.worker = new FullDownloadWorker("server", this.outQueue, this.newsClient);
  }

  /** Test AddDownloadRequest. */
  @Test
  public void testAddDownloadRequestAll() {
    try {
      final String nntpHost = "testserver";
      final LinkedBlockingQueue<NewsArticle> queue = new LinkedBlockingQueue<>();
      final NewsGroupReader reader = Mockito.mock(NewsGroupReader.class);
      final HibernateHelper helper = Mockito.mock(HibernateHelper.class);
      FullDownloadWorker.addDownloadRequest(
          "<n9rgdm$g9b$3@news4.open-news-network.org>",
          DownloadMode.ALL,
          nntpHost,
          queue,
          this.newsClient,
          reader,
          helper);
      queue.add(new NewsArticle("123", 1, new Date(), "eg@mail.com", "Lets talk", "", "alt"));
      FullDownloadWorker.addDownloadRequest(
          "<n9rgdm$g9b$3@news4.open-news-network.org>",
          DownloadMode.ALL,
          nntpHost,
          queue,
          this.newsClient,
          reader,
          helper);

      // Assert.assertTrue(FullDownloadWorker.queueSize() >= 1);
    } catch (final UNISoNException e) {
      e.printStackTrace();
      Assert.fail("ERROR: " + e.getMessage());
    }
  }

  /** Test AddDownloadRequest. */
  @Test
  public void testAddDownloadRequestHeader() {
    try {
      final String nntpHost = "testserver";
      final LinkedBlockingQueue<NewsArticle> queue = new LinkedBlockingQueue<>();
      final NewsGroupReader reader = Mockito.mock(NewsGroupReader.class);
      final HibernateHelper helper = Mockito.mock(HibernateHelper.class);
      FullDownloadWorker.addDownloadRequest(
          "<n9rgdm$g9b$3@news4.open-news-network.org>",
          DownloadMode.HEADERS,
          nntpHost,
          queue,
          this.newsClient,
          reader,
          helper);
      queue.add(new NewsArticle("123", 1, new Date(), "eg@mail.com", "Lets talk", "", "alt"));
      FullDownloadWorker.addDownloadRequest(
          "<n9rgdm$g9b$3@news4.open-news-network.org>",
          DownloadMode.HEADERS,
          nntpHost,
          queue,
          this.newsClient,
          reader,
          helper);

      // Assert.assertTrue(FullDownloadWorker.queueSize() >= 1);
    } catch (final UNISoNException e) {
      e.printStackTrace();
      Assert.fail("ERROR: " + e.getMessage());
    }
  }

  /** Test Contructor. */
  @SuppressWarnings("unchecked")
  @Test
  public void testConstruct() {
    FullDownloadWorker actual;
    try {
      actual = new FullDownloadWorker("server", new LinkedBlockingQueue(), this.newsClient);
      Assert.assertNotNull(actual);
    } catch (final UNISoNException e) {
      Assert.fail("ERROR: " + e.getMessage());
    }
  }

  @Test
  public void testConvertHeaderStringToArticle() throws Exception {
    final String info =
        "Path:"
            + " fleegle.mixmin.net!news.mixmin.net!news.glorb.com!xmission!news.alt.net!news.astraweb.com!border5.newsrouter.astraweb.com!not-for-mail\n"
            + "Reply-To: \"Replica\" <replica@yang-online.com>\n"
            + "From: \"Replica\" <replica@yang-online.com\n"
            + "Newsgroups: ab.general,alt.games.duke3d.binaries\n"
            + "Subject: Duke Nukem Hall of Shame (update)\n"
            + "Date: Sun, 20 Jul 2014 18:29:35 +0930\n"
            + "Organization: Yang-Online dot com\n"
            + "X-Priority: 3X-MSMail-Priority: Normal\n"
            + "X-Newsreader: Microsoft Outlook Express 6.00.2900.5931\n"
            + "X-RFC2646: Format=Flowed; Original\n"
            + "X-MimeOLE: Produced By Microsoft MimeOLE V6.00.2900.6157\n"
            + "Lines: 7\n"
            + "Message-ID: <53cb84f9$0$29973$c3e8da3$5496439d@news.astraweb.com>\n"
            + "NNTP-Posting-Host: 5c7da716.news.astraweb.com\n"
            + "X-Trace:"
            + " DXC=B>V1ge@;JZElVZHQGRZg_EL?0kYOcDh@JN7:H2`MmAUCU\\YKDg@=BbC]G;2>V^?kWC48NP]Hm4n:I1KkCUT6UkBL@_;AC_08l\\H\n"
            + "Xref: news.mixmin.net ab.general:12179 alt.games.duke3d.binaries:63\n"
            + "\n"
            + "Coming soon!  email: support@yang-online.com\n"
            + " \n"
            + "Yang's servers are also down, suggest you Duke at www.dukematches.net\n"
            + "\n"
            + "Rep'";
    final NewsArticle actual = this.worker.convertHeaderStringToArticle(info);
  }

  /**
   * Test DownloadArticle.
   *
   * @throws IOException
   */
  @Test(timeout = 1000)
  public void testDownloadArticle() throws IOException {
    final String message =
        "Message-ID: <id123>\n"
            + "From: test@test.com\n"
            + "Subject: Test subject\n"
            + "Date: Sun, 18 Jan 2015 23:40:56 +0000\n"
            + "Newsgroups: alt.test\n"
            + "X-Received-Date: Sun, 18 Jan 2015 23:40:56 +0000\n"
            + "Test body";
    final Reader value = new StringReader(message);
    Mockito.when(this.newsClient.retrieveArticle(ArgumentMatchers.anyString())).thenReturn(value);
    final DownloadRequest request = new DownloadRequest("<id123>", DownloadMode.ALL);
    try {
      final NewsArticle actual = this.worker.downloadArticle(request);
      Assert.assertNotNull(actual);
      Assert.assertEquals("Test subject", actual.getSubject());
    } catch (final UNISoNException e) {
      Assert.fail("ERROR: " + e.getMessage());
    }
  }

  @Test
  public void testFailToConnect() throws Exception {
    Mockito.doThrow(new IOException("Failed to connect"))
        .when(this.newsClient)
        .connect(ArgumentMatchers.anyString());
  }

  /** Test fullDownloadWorker. */
  // 5 2006-04-28 <Baf4g.374$Lj1.115@fe10.lga> Re: Novel brain-penetrating
  // antioxidant 4 2 <1146147483.974309.7550@t31g2000cwb.googlegroups.com>
  // <1146149358.240977.254030@t31g2000cwb.googlegroups.com>
  // <1146149630.481616.212070@i39g2000cwa.googlegroups.com>
  // 4 mult-sclerosis 3 -1 -1 -1 -1 uk.people.support.mult-sclerosis true
  @Test
  public void testFullDownloadWorker() {
    final LinkedBlockingQueue<NewsArticle> queue = new LinkedBlockingQueue<>();
    FullDownloadWorker worker = null;
    try {
      final String nntpHost = "testserver";
      worker = new FullDownloadWorker(nntpHost, queue, this.newsClient);
    } catch (final UNISoNException e1) {
      e1.printStackTrace();
    }
    final DownloadRequest request =
        new DownloadRequest("<Baf4g.374$Lj1.115@fe10.lga>", DownloadMode.ALL);

    try {
      final NewsArticle article = worker.downloadFullMessage(request);
      System.out.println("Downloaded: " + article);
    } catch (final IOException e) {
      e.printStackTrace();
    } catch (final UNISoNException e) {
      e.printStackTrace();
    }
  }
}
