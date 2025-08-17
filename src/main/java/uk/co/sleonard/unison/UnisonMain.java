package uk.co.sleonard.unison;

import javax.swing.*;
import uk.co.sleonard.unison.gui.generated.UNISoNTabbedFrame;

/** Application entry point for the Unison UI. */
public class UnisonMain {
  public static void main(final String[] args) {
    SwingUtilities.invokeLater(
        () -> {
          try {
            final UNISoNTabbedFrame frame = new UNISoNTabbedFrame();
            frame.setVisible(true);
          } catch (final UNISoNException e) {
            e.printStackTrace();
          }
        });
  }
}
