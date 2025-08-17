/**
 * ExportToCSVTest
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.output;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.sleonard.unison.UNISoNException;

/**
 * The Class ExportToCSV.
 *
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.2.0
 */
public class ExportToCSVTest {

  /** The export. */
  private ExportToCSV export;

  /**
   * Generate JTable with test data.
   *
   * @return JTable filled.
   */
  private JTable generateJTableToTest() {
    DefaultTableModel model;
    model = new DefaultTableModel();
    final JTable jTable = new JTable(model);
    model.addColumn(null);
    model.addColumn(null);
    model.addColumn(null);
    model.addRow(new Object[] {"element1", "element2", "test"});
    model.addRow(new Object[] {"element3", "element4", "test"});
    return jTable;
  }

  /**
   * Setup.
   *
   * @throws Exception the exception
   */
  @Before
  public void setUp() throws Exception {
    this.export = new ExportToCSV();
  }

  /** Create the data table, export in text file, read it and delete. */
  @Test
  public void testExportTable() {
    final String archiveName = new String("export_test");
    final Vector<String> fieldNames = new Vector<>();
    fieldNames.addElement("column1");
    fieldNames.addElement("column2");
    fieldNames.addElement("test");
    try {
      this.export.exportTable(archiveName, this.generateJTableToTest(), fieldNames);
      final File file = new File(archiveName);
      final FileReader fileReader = new FileReader(file.getCanonicalPath());
      final BufferedReader reader = new BufferedReader(fileReader);
      String currentLine = reader.readLine();
      while (currentLine != null) {
        Assert.assertTrue(currentLine.contains("test"));
        currentLine = reader.readLine();
      }
      reader.close();
      file.delete();
    } catch (UNISoNException | IOException f) {
      Assert.fail("ERROR: " + f.getMessage());
    }
  }
}
