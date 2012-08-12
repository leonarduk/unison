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

public class PajekNetworkFile {

	private final LinkedList<String> vertices;
	protected List<Relationship> directedLinks;
	private String filename;
	protected String SUFFIX;
	protected List<Relationship> undirectedLinks;

	// Statically assign the suffix
	{
		this.SUFFIX = ".net";
	}

	public GraphPreviewPanel getPreviewPanel() throws UNISoNException {
		GraphPreviewPanel graphPreviewPanel = new GraphPreviewPanel(vertices,
				directedLinks);
		graphPreviewPanel.setVisible(true);
		graphPreviewPanel.setSize(graphPreviewPanel.getPreferredSize());
		return graphPreviewPanel;
	}

	public PajekNetworkFile() {
		this.vertices = new LinkedList<String>();
		this.directedLinks = new Vector<Relationship>();

		this.undirectedLinks = new Vector<Relationship>();
	}

	private void addNode(final String owner) {

		if (!this.vertices.contains(owner)) {
			this.vertices.add(owner);
		}
	}

	public Relationship addRelationship(final String ownerName,
			final String targetName, final List<Relationship> links) {
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
		} else {

			// System.out.println("Create " + link);
			links.add(link);
		}
		return link;
	}

	/**
	 * 
	 * @param nodePairs
	 * @param links
	 * @return
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
			} else {
				this.addNode(key);
			}
		}
		return links;
	}

	private int getVerticeIndex(final String ownerName) {
		return this.vertices.indexOf(ownerName) + 1;
	}

	public void writeData(final PrintStream printStream) {
		// first print out *Vertices and number of points
		printStream.println("*Vertices " + this.vertices.size());

		// Then list all the vertices
		final Iterator<String> iter = this.vertices.iterator();
		while (iter.hasNext()) {
			final String nodeName = (String) iter.next();
			final int nodeIndex = this.getVerticeIndex(nodeName);
			printStream.println(nodeIndex + " \"" + nodeName + "\"");
		}

		// List all the undirected links
		if (this.undirectedLinks.size() > 0) {
			printStream.println("*Edges");
			final Iterator<Relationship> linksIter = this.undirectedLinks
					.iterator();
			while (linksIter.hasNext()) {
				final Relationship link = (Relationship) linksIter.next();
				printStream.println(link.getOwner() + " " + link.getTarget()
						+ " " + link.getValue());
			}
		}

		// List all the directed links
		if (this.directedLinks.size() > 0) {
			printStream.println("*Arcs");
			final Iterator<Relationship> linksIter = this.directedLinks
					.iterator();
			while (linksIter.hasNext()) {
				final Relationship link = (Relationship) linksIter.next();
				printStream.println(link.getOwner() + " " + link.getTarget()
						+ " " + link.getValue());
			}
		}
	}

	public List<Relationship> createDirectedLinks(
			final Vector<Vector<String>> tableData) {
		this.directedLinks = this.createLinks(tableData, this.directedLinks);

		return this.directedLinks;
	}

	public List<Relationship> createUndirectedLinks(
			final Vector<Vector<String>> tableData) {
		this.undirectedLinks = this
				.createLinks(tableData, this.undirectedLinks);
		return this.undirectedLinks;
	}

	public String getFilename() {
		return this.filename;
	}

	public String getFileSuffix() {
		return this.SUFFIX;
	}

	public void saveToFile(String filename) {
		if (!filename.endsWith(this.SUFFIX)) {
			filename += this.SUFFIX;
		}
		this.filename = filename;
		FileOutputStream out; // declare a file output object
		PrintStream p; // declare a print stream object

		// Create a new file output stream
		try {
			out = new FileOutputStream(filename);

			// Connect print stream to the output stream
			p = new PrintStream(out);
			this.writeData(p);

			p.close();
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Saved to " + filename);
	}

}
