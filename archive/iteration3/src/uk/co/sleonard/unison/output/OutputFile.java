package uk.co.sleonard.unison.output;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Vector;

abstract public class OutputFile {

	protected String SUFFIX;

	private String filename;

	/**
	 * @param args
	 * 
	 */
	public static void main(String[] args) {

	}

	protected List<Relationship> directedLinks;

	protected List<Relationship> undirectedLinks;

	public List createUndirectedLinks(Vector<Vector<String>> tableData) {
		undirectedLinks = createLinks(tableData, undirectedLinks);
		return undirectedLinks;
	}

	abstract protected List<Relationship> createLinks(
			Vector<Vector<String>> tableData, List<Relationship> links);

	public List createDirectedLinks(Vector<Vector<String>> tableData) {
		directedLinks = createLinks(tableData, directedLinks);

		return directedLinks;
	}

	public OutputFile() {

		directedLinks = new Vector<Relationship>();

		undirectedLinks = new Vector<Relationship>();
	}

	public String getFileSuffix() {
		return SUFFIX;
	}

	abstract protected void writeData(PrintStream printStream);

	public void saveToFile(String filename) {
		if (!filename.endsWith(SUFFIX)) {
			filename += SUFFIX;
		}
		this.filename = filename;
		FileOutputStream out; // declare a file output object
		PrintStream p; // declare a print stream object

		// Create a new file output stream
		try {
			out = new FileOutputStream(filename);

			// Connect print stream to the output stream
			p = new PrintStream(out);
			writeData(p);

			p.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Saved to " + filename);
	}

	public String getFilename() {
		return filename;
	}
}
