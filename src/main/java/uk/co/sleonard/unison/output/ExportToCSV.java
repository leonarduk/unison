/**
 * ExportToCSV
 *
 * @author Stephen <github@leonarduk.com>
 * @since 22-May-2016
 */
package uk.co.sleonard.unison.output;

import java.awt.FileDialog;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import uk.co.sleonard.unison.UNISoNException;

/**
 * The Class ExportToCSV.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
public class ExportToCSV {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {
		final ExportToCSV test = new ExportToCSV();
		final String data = "M'I-5'Persecut ion , Bern ard Le vin expre sses h is v iews";
		test.extractCommas(data);
	}

	/**
	 * Export table.
	 *
	 * @param fileName
	 *            the file name
	 * @param table
	 *            the table
	 * @param fieldNames
	 *            the field names
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	void exportTable(final String fileName, final JTable table, final Vector<String> fieldNames)
	        throws UNISoNException {
		try {
			final File file = new File(fileName);
			if (file != null) {

				// clear old file if it exists
				if (file.exists()) {
					file.delete();
				}
				try (final BufferedWriter bufferedWriter = new BufferedWriter(
				        new FileWriter(file, true));
				        final PrintWriter fileWriter = new PrintWriter(bufferedWriter);) {
					String data;
					for (int j = 0; j < table.getColumnCount(); ++j) {

						// replace any commas in data!
						data = this.extractCommas(fieldNames.get(j));

						fileWriter.print(data + ",");
					}
					fileWriter.println("");
					for (int i = 0; i < table.getRowCount(); ++i) {
						for (int j = 0; j < table.getColumnCount(); ++j) {
							data = this.extractCommas(table.getValueAt(i, j).toString());
							fileWriter.print(data + ",");
						}
						fileWriter.println("");
					}
					fileWriter.close();
				}
				catch (final Exception e) {
					JOptionPane.showMessageDialog(null, "Error " + e);
				}
			}

		}
		catch (final Exception e) {
			throw new UNISoNException("Failed to export to CSV", e);
		}
	}// export
	 // Table

	/**
	 * Export table to csv.
	 *
	 * @param table
	 *            the table
	 * @param fieldNames
	 *            the field names
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	@SuppressWarnings("deprecation")
	public void exportTableToCSV(final JTable table, final Vector<String> fieldNames)
	        throws UNISoNException {
		final FileDialog file = new FileDialog(new JFrame(), "Save CSV Network File",
		        FileDialog.SAVE);
		final String CSV_FILE_SUFFIX = ".csv";
		final String initialValue = "*" + CSV_FILE_SUFFIX;
		file.setFile(initialValue); // set initial filename filter
		file.setFilenameFilter((dir, name) -> {
			if (name.endsWith(CSV_FILE_SUFFIX)) {
				return true;
			}
			return false;
		});
		file.show(); // Blocks
		String curFile = null;
		curFile = file.getFile();
		if ((curFile != null) && !curFile.equals(initialValue)) {

			if (!curFile.endsWith(CSV_FILE_SUFFIX)) {
				curFile += CSV_FILE_SUFFIX;
			}
			final String filename = file.getDirectory() + curFile;

			this.exportTable(filename, table, fieldNames);
		}

	}

	/**
	 * Extract commas.
	 *
	 * @param data
	 *            the data
	 * @return the string
	 */
	private String extractCommas(final String dataInput) {
		if (dataInput.indexOf(',') > -1) {
			return dataInput.replaceAll(",", ";");
		}
		return dataInput;
	}

}
