package uk.co.sleonard.unison.gui.generated;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;

import javax.swing.JProgressBar;
import javax.swing.JFrame;

import org.junit.Test;
import org.mockito.Mockito;

import uk.co.sleonard.unison.UNISoNController;
import uk.co.sleonard.unison.input.DataHibernatorPool;
import uk.co.sleonard.unison.input.HeaderDownloadWorker;

/**
 * Tests for {@link DownloadNewsPanel}.
 */
public class DownloadNewsPanelTest {

    @Test
    public void progressBarUpdatesViaController() throws Exception {
        JFrame frame = Mockito.mock(JFrame.class);
        DataHibernatorPool pool = Mockito.mock(DataHibernatorPool.class);
        UNISoNController controller = UNISoNController.create(frame, pool);
        HeaderDownloadWorker worker = Mockito.mock(HeaderDownloadWorker.class);
        controller.setHeaderDownloader(worker);

        DownloadNewsPanel panel = new DownloadNewsPanel(controller);

        controller.setDownloadingState(42);

        Field barField = DownloadNewsPanel.class.getDeclaredField("progressBar");
        barField.setAccessible(true);
        JProgressBar bar = (JProgressBar) barField.get(panel);
        assertEquals(42, bar.getValue());
    }
}
