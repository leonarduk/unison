package uk.co.sleonard.unison.output.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.junit.Before;
import org.junit.Test;

import uk.co.sleonard.unison.gui.UNISoNException;
import uk.co.sleonard.unison.output.ExportToCSV;

/**
 * The Class ExportToCSV.
 * 
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.2.0
 *
 */
public class ExportToCSVTest {

	/** The export. */
	private ExportToCSV export;

	/**
	 * Setup.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		export = new ExportToCSV();

	}

	/**
	 * Create the data table, export in text file, read it and delete.
	 */
	@Test
	public void testExportTable() {
		String archiveName = new String("export_test");
		Vector<String> fieldNames = new Vector<>();
		fieldNames.addElement("column1");
		fieldNames.addElement("column2");
		fieldNames.addElement("test");
		try {
			String currentLine;
			this.export.exportTable(archiveName, generateJTableToTest(), fieldNames);
			File file = new File(archiveName);
			FileReader fileReader = new FileReader(file.getCanonicalPath());
			BufferedReader reader = new BufferedReader(fileReader);
			while ((currentLine = reader.readLine()) != null) {
				assertTrue(currentLine.contains("test"));
			}
			reader.close();
			file.delete();
		}
		catch (UNISoNException | IOException f) {
			fail("ERROR: " + f.getMessage());
		}
	}

	/**
	 * Generate JTable with test data.
	 * 
	 * @return JTable filled.
	 */
	private JTable generateJTableToTest() {
		DefaultTableModel model;
		model = new DefaultTableModel();
		JTable jTable = new JTable(model);
		model.addColumn(null);
		model.addColumn(null);
		model.addColumn(null);
		model.addRow(new Object[] { "element1", "element2", "test" });
		model.addRow(new Object[] { "element3", "element4", "test" });
		return jTable;
	}
}
