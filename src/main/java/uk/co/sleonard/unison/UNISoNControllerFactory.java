package uk.co.sleonard.unison;

import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.gui.UNISoNGUI;
import uk.co.sleonard.unison.input.DataHibernatorPool;
import uk.co.sleonard.unison.input.NewsArticle;
import uk.co.sleonard.unison.input.NewsClient;
import uk.co.sleonard.unison.input.NewsClientImpl;

import javax.swing.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Factory for {@link UNISoNController} instances.
 */
public class UNISoNControllerFactory {

    /**
     * Create a fully initialised {@link UNISoNController}.
     *
     * @param frame optional Swing frame to back the GUI
     * @param pool  pool controlling download threads
     * @return a new controller
     * @throws UNISoNException if initialisation fails
     */
    public UNISoNController create(final JFrame frame, final DataHibernatorPool pool)
            throws UNISoNException {
        UNISoNGUI gui = (frame != null) ? new UNISoNGUI(frame) : null;
        HibernateHelper helper = new HibernateHelper(gui);
        LinkedBlockingQueue<NewsArticle> queue = new LinkedBlockingQueue<>();
        NewsClient client = new NewsClientImpl();
        return new UNISoNController(client, helper, queue, pool, gui);
    }
}

