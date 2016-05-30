/**
 * PajekNetworkFile
 *
 * @author Stephen <github@leonarduk.com>
 * @since 22-May-2016
 */
package uk.co.sleonard.unison.output;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import uk.co.sleonard.unison.gui.GraphPreviewPanel;
import uk.co.sleonard.unison.gui.UNISoNException;

/**
 * The Class PajekNetworkFile.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
public class PajekNetworkFile {

	/** The vertices. */
	private final LinkedList<String> vertices;

	/** The directed links. */
	protected List<Relationship> directedLinks;

	/** The filename. */
	private String filename;

	/** The suffix. */
	protected String suffix;

	/** The undirected links. */
	protected List<Relationship> undirectedLinks;

	// Statically assign the suffix
	{
		this.suffix = ".net";
	}

	/**
	 * Instantiates a new pajek network file.
	 */
	public PajekNetworkFile() {
		this.vertices = new LinkedList<>();
		this.directedLinks = new Vector<>();

		this.undirectedLinks = new Vector<>();
	}

	/**
	 * Adds the node.
	 *
	 * @param owner
	 *            the owner
	 */
	private void addNode(final String owner) {

		if (!this.vertices.contains(owner)) {
			this.vertices.add(owner);
		}
	}

	/**
	 * Adds the relationship.
	 *
	 * @param ownerName
	 *            the owner name
	 * @param targetName
	 *            the target name
	 * @param links
	 *            the links
	 * @return the relationship
	 */
	public Relationship addRelationship(final String ownerName, final String targetName,
	        final List<Relationship> links) {
		// ensure the start and end points are included in the network
		this.addNode(ownerName);
		this.addNode(targetName);

		// add the link between them
		// Need to convert from the String text to the numerical index value
		Relationship link = new Relationship(this.getVerticeIndex(ownerName),
		        this.getVerticeIndex(targetName));

		if (links.contains(link)) {
			// if it exists already, then get it and add 1 to the value
			link = links.get(links.indexOf(link));

			// add it back to where it came from
			links.set(links.indexOf(link), link);

			link.incrementValue();
		}
		else {

			// System.out.println("Create " + link);
			links.add(link);
		}
		return link;
	}

	/**
	 * Creates the directed links.
	 *
	 * @param tableData
	 *            the table data
	 * @return the list
	 */
	public List<Relationship> createDirectedLinks(final Vector<Vector<String>> tableData) {
		this.directedLinks = this.createLinks(tableData, this.directedLinks);

		return this.directedLinks;
	}

	/**
	 * Creates the links.
	 *
	 * @param nodePairs
	 *            the node pairs
	 * @param links
	 *            the links
	 * @return the list
	 */
	protected List<uk.co.sleonard.unison.output.Relationship> createLinks(
	        final Vector<Vector<String>> nodePairs,
	        final List<uk.co.sleonard.unison.output.Relationship> links) {
		final Iterator<Vector<String>> iter = nodePairs.iterator();
		while (iter.hasNext()) {
			final Vector<String> row = iter.next();
			final String key = row.get(2);
			final String value = row.get(3);

			// System.out.println(key + " " + value);

			if ((null != value) && !value.equals("")) {
				this.addRelationship(key, value, links);
			}
			else {
				this.addNode(key);
			}
		}
		return links;
	}

	/**
	 * Creates the undirected links.
	 *
	 * @param tableData
	 *            the table data
	 * @return the list
	 */
	public List<Relationship> createUndirectedLinks(final Vector<Vector<String>> tableData) {
		this.undirectedLinks = this.createLinks(tableData, this.undirectedLinks);
		return this.undirectedLinks;
	}

	/**
	 * Gets the filename.
	 *
	 * @return the filename
	 */
	public String getFilename() {
		return this.filename;
	}

	/**
	 * Gets the file suffix.
	 *
	 * @return the file suffix
	 */
	public String getFileSuffix() {
		return this.suffix;
	}

	/**
	 * Gets the preview panel.
	 *
	 * @return the preview panel
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	public GraphPreviewPanel getPreviewPanel() throws UNISoNException {
		final GraphPreviewPanel graphPreviewPanel = new GraphPreviewPanel(this.vertices,
		        this.directedLinks);
		graphPreviewPanel.setVisible(true);
		graphPreviewPanel.setSize(graphPreviewPanel.getPreferredSize());
		return graphPreviewPanel;
	}

	/**
	 * Gets the vertice index.
	 *
	 * @param ownerName
	 *            the owner name
	 * @return the vertice index
	 */
	private int getVerticeIndex(final String ownerName) {
		return this.vertices.indexOf(ownerName) + 1;
	}

	/**
	 * Save to file.
	 *
	 * @param filename
	 *            the filename
	 */
	public void saveToFile(final String filenameInput) {
		this.filename = filenameInput;
		if (!filenameInput.endsWith(this.suffix)) {
			this.filename += this.suffix;
		}
		FileOutputStream out; // declare a file output object
		PrintStream p; // declare a print stream object

		// Create a new file output stream
		try {
			out = new FileOutputStream(this.filename);

			// Connect print stream to the output stream
			p = new PrintStream(out);
			this.writeData(p);

			p.close();
		}
		catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Saved to " + this.filename);
	}

	/**
	 * Write data.
	 *
	 * @param printStream
	 *            the print stream
	 */
	public void writeData(final PrintStream printStream) {
		// first print out *Vertices and number of points
		printStream.println("*Vertices " + this.vertices.size());

		// Then list all the vertices
		final Iterator<String> iter = this.vertices.iterator();
		while (iter.hasNext()) {
			final String nodeName = iter.next();
			final int nodeIndex = this.getVerticeIndex(nodeName);
			printStream.println(nodeIndex + " \"" + nodeName + "\"");
		}

		// List all the undirected links
		if (this.undirectedLinks.size() > 0) {
			printStream.println("*Edges");
			final Iterator<Relationship> linksIter = this.undirectedLinks.iterator();
			while (linksIter.hasNext()) {
				final Relationship link = linksIter.next();
				printStream
				        .println(link.getOwner() + " " + link.getTarget() + " " + link.getValue());
			}
		}

		// List all the directed links
		if (this.directedLinks.size() > 0) {
			printStream.println("*Arcs");
			final Iterator<Relationship> linksIter = this.directedLinks.iterator();
			while (linksIter.hasNext()) {
				final Relationship link = linksIter.next();
				printStream
				        .println(link.getOwner() + " " + link.getTarget() + " " + link.getValue());
			}
		}
	}

}
