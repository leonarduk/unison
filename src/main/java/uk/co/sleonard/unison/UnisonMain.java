package uk.co.sleonard.unison;

import lombok.extern.slf4j.Slf4j;
import uk.co.sleonard.unison.gui.generated.UNISoNTabbedFrame;

import javax.swing.*;

/**
 * Application entry point for the Unison UI.
 */
@Slf4j
public class UnisonMain {
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                final UNISoNTabbedFrame frame = new UNISoNTabbedFrame();
                frame.setVisible(true);
            } catch (final UNISoNException e) {
                log.error("Failed to start UNISoN", e);
            }
        });
    }
}
