package uk.co.sleonard.unison.gui.generated;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Vector;
import javax.swing.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import uk.co.sleonard.unison.UNISoNController;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.gui.UNISoNGUI;
import uk.co.sleonard.unison.output.ExportToCSV;

/** Tests for {@link PajekPanel}. */
public class PajekPanelTest {

  @Test
  public void testIncMissingCheckFieldRemoved() {
    boolean found = false;
    for (Field field : PajekPanel.class.getDeclaredFields()) {
      if ("incMissingCheck".equals(field.getName())) {
        found = true;
        break;
      }
    }
    assertFalse("incMissingCheck field should have been removed", found);
  }

  @Test
  public void csvButtonLogsAndAlertsOnException() throws Exception {
    PajekPanel panel =
        Mockito.mock(
            PajekPanel.class, Mockito.withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));

    ExportToCSV csvMock = Mockito.mock(ExportToCSV.class);
    UNISoNException ex = new UNISoNException("boom");
    Mockito.doThrow(ex).when(csvMock).exportTableToCSV(Mockito.any(JTable.class), Mockito.any());

    UNISoNController controllerMock = Mockito.mock(UNISoNController.class);
    UNISoNGUI guiMock = Mockito.mock(UNISoNGUI.class);
    Mockito.when(controllerMock.getGui()).thenReturn(guiMock);

    setField(panel, "csvExporter", csvMock);
    setField(panel, "resultsMatrixTable", new JTable());
    setField(panel, "pajekHeader", new Vector<String>());
    setField(panel, "controller", controllerMock);

    Logger logger = (Logger) LoggerFactory.getLogger(PajekPanel.class);
    ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
    listAppender.start();
    logger.addAppender(listAppender);

    Method m =
        PajekPanel.class.getDeclaredMethod(
            "csvButtonActionPerformed", java.awt.event.ActionEvent.class);
    m.setAccessible(true);
    m.invoke(panel, new ActionEvent(panel, ActionEvent.ACTION_PERFORMED, "csv"));

    Mockito.verify(guiMock).showAlert("Error: " + ex.getMessage());
    assertTrue(
        listAppender.list.stream()
            .anyMatch(
                event ->
                    event.getLevel() == Level.ERROR
                        && event.getMessage().contains("Error exporting CSV")));
  }

  private static void setField(final Object target, final String name, final Object value)
      throws Exception {
    Field field = PajekPanel.class.getDeclaredField(name);
    field.setAccessible(true);
    field.set(target, value);
  }
}
