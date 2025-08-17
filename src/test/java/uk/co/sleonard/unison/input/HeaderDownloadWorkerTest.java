/**
 * HeaderDownloadWorkerTest
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.utils.Downloader;

/**
 * The Class HeaderDownloadWorker.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 */
public class HeaderDownloadWorkerTest {

  private HeaderDownloadWorker worker;

  /** Setup. */
  @Before
  public void setUp() throws Exception {
    this.worker =
        new HeaderDownloadWorker(new LinkedBlockingQueue<>(), Mockito.mock(Downloader.class));
  }

  /** Test if download then finished. */
  @Test
  public void testFinished() {
    this.worker.resume();
    Assert.assertTrue(this.worker.isDownloading());
    this.worker.finished();
    Assert.assertFalse(this.worker.isDownloading());
  }

  /** Test fullStop */
  @Test
  public void testFullstop() {
    System.out.println("Wait 2 secs and stop");
    try {
      Thread.sleep(2000);
    } catch (final InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Stop");
    this.worker.fullstop();
  }

  /**
   * Test Initialize
   *
   * @throws UNISoNException Signals that an exception has occurred.
   */
  @Test
  public void testInitialise() throws UNISoNException {

    final NewsClient nc = Mockito.mock(NewsClient.class);
    final NewsGroupReader ngr = new NewsGroupReader(nc);
    final Date fromAndTo = Calendar.getInstance().getTime();
    Assert.assertFalse(this.worker.isDownloading());

    this.worker.initialise(
        ngr, 0, 1, "server", "newsgroup", DownloadMode.ALL, fromAndTo, fromAndTo);

    Assert.assertTrue(this.worker.isDownloading());
  }

  /** Test if in download */
  @Test
  public void testIsDownloading() {
    Assert.assertFalse(this.worker.isDownloading());
    this.worker.resume();
    Assert.assertTrue(this.worker.isDownloading());
  }

  /** Test listener notification. */
  @Test
  public void testNotifyListeners() {
    final AtomicInteger counter = new AtomicInteger(0);
    this.worker.addDataChangeListener(evt -> counter.incrementAndGet());
    this.worker.notifyListeners();
    Assert.assertEquals(1, counter.get());
  }

  /** Test Pause */
  @Test
  public void testPause() {
    this.worker.resume();
    Assert.assertTrue(this.worker.isDownloading());
    this.worker.pause();
    Assert.assertFalse(this.worker.isDownloading());
  }

  @Test(expected = UNISoNException.class)
  public void testQueueMessagesAbort() throws IOException, UNISoNException {
    final LinkedBlockingQueue<NewsArticle> queue1 = new LinkedBlockingQueue<>();
    final String messages = "sdsds";
    this.worker.fullstop();
    try (Reader reader = new StringReader(messages); ) {
      this.worker.queueMessages(queue1, reader);
    }
  }

  @Test
  public void testQueueMessagesAllDownload() throws IOException, UNISoNException {
    final LinkedBlockingQueue<NewsArticle> queue1 = new LinkedBlockingQueue<>();
    final StringBuilder buffer = new StringBuilder();
    for (int i = 0; i < 500; i++) {
      buffer.append(i);
      buffer.append("\t");
      buffer.append("This is the subject\t");
      buffer.append("sender@email.com\t" + "2016-04-01\t");
      buffer.append("1234\t");
      buffer.append("123@email.com");
    }
    this.worker.resume();
    this.worker.setMode(DownloadMode.ALL);
    try (Reader reader = new StringReader(buffer.toString()); ) {
      this.worker.queueMessages(queue1, reader);
    }
  }

  @Test
  public void testQueueMessagesBasicDownload() throws IOException, UNISoNException {
    final LinkedBlockingQueue<NewsArticle> queue1 = new LinkedBlockingQueue<>();
    final StringBuilder buffer = new StringBuilder();
    buffer.append("300\t");
    buffer.append("This is the subject\t");
    buffer.append("sender@email.com\t" + "2016-04-01\t");
    buffer.append("1234\t");
    buffer.append("123@email.com");
    this.worker.resume();
    this.worker.setMode(DownloadMode.BASIC);
    try (Reader reader = new StringReader(buffer.toString()); ) {
      this.worker.queueMessages(queue1, reader);
    }
  }

  /**
   * Test construct
   *
   * @throws InterruptedException
   * @throws UNISoNException
   * @throws IOException
   */
  @Test
  public void testQueueMessagesNullReader()
      throws InterruptedException, IOException, UNISoNException {
    Assert.assertTrue(this.worker.queueMessages(new LinkedBlockingQueue<>(), null));
  }

  /** Test Resume */
  @Test
  public void testResume() {
    Assert.assertFalse(this.worker.isDownloading());
    this.worker.resume();
    Assert.assertTrue(this.worker.isDownloading());
  }

  /**
   * Test sToreArticleInfo
   *
   * @throws UNISoNException Signals that an exception has occurred.
   */
  @Test
  public void testStoreArticleInfo() throws UNISoNException {
    final LinkedBlockingQueue<NewsArticle> queue = new LinkedBlockingQueue<>();
    final boolean actual = this.worker.storeArticleInfo(queue);
    Assert.assertTrue(actual);
  }

  @Test
  public void testStoreArticleInfoDoesNotRequestBeyondEndIndex() throws Exception {
    final LinkedBlockingQueue<NewsArticle> queue = new LinkedBlockingQueue<>();

    final NewsGroupReader reader = new NewsGroupReader(null);
    final NewsClient client = Mockito.mock(NewsClient.class);
    Mockito.when(client.retrieveArticleInfo(Mockito.anyLong(), Mockito.anyLong()))
        .thenReturn(new BufferedReader(new StringReader("")));
    reader.setClient(client);

    // configure worker
    this.worker.resume();
    setField(this.worker, "startIndex", 0);
    setField(this.worker, "endIndex", 600);
    setField(this.worker, "newsReader", reader);

    this.worker.storeArticleInfo(queue);

    final ArgumentCaptor<Long> endCaptor = ArgumentCaptor.forClass(Long.class);
    Mockito.verify(client, Mockito.atLeastOnce())
        .retrieveArticleInfo(Mockito.anyLong(), endCaptor.capture());

    for (final Long end : endCaptor.getAllValues()) {
      Assert.assertTrue("Requested message beyond end index", end <= 600L);
    }
  }

  private void setField(final Object target, final String fieldName, final Object value)
      throws Exception {
    final Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }
}
