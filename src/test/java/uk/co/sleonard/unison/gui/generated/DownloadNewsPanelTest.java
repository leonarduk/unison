package uk.co.sleonard.unison.gui.generated;

import org.hibernate.Session;
import org.junit.Test;
import org.mockito.Mockito;
import uk.co.sleonard.unison.UNISoNController;
import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.input.*;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link DownloadNewsPanel}.
 */
public class DownloadNewsPanelTest {

    @Test
    public void progressBarUpdatesViaController() throws Exception {
        NewsClient client = Mockito.mock(NewsClient.class);
        HibernateHelper helper = Mockito.mock(HibernateHelper.class);
        Session session = Mockito.mock(Session.class);
        Mockito.when(helper.getHibernateSession()).thenReturn(session);
        LinkedBlockingQueue<NewsArticle> queue = new LinkedBlockingQueue<>();
        DataHibernatorPool pool = Mockito.mock(DataHibernatorPool.class);

        UNISoNController controller = new UNISoNController(client, helper, queue, pool, null);
        UNISoNController controllerSpy = Mockito.spy(controller);
        HeaderDownloadWorker worker = Mockito.mock(HeaderDownloadWorker.class);
        controllerSpy.setHeaderDownloader(worker);
        Mockito.doNothing().when(controllerSpy).setNntpHost(Mockito.anyString());

        DownloadNewsPanel panel = new DownloadNewsPanel(controllerSpy);

        controllerSpy.setDownloadingState(42);

        Field barField = DownloadNewsPanel.class.getDeclaredField("progressBar");
        barField.setAccessible(true);
        JProgressBar bar = (JProgressBar) barField.get(panel);
        assertEquals(42, bar.getValue());
    }
}
