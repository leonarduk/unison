package uk.co.sleonard.unison.output;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class PajekNetworkFile extends OutputFile {

	private List<String> vertices;

	// Statically assign the suffix
	{
		SUFFIX = ".net";
	}

	public PajekNetworkFile() {
		vertices = new Vector<String>();
	}

	private void addNode(String owner) {

		if (!vertices.contains(owner)) {
			vertices.add(owner);
			System.out.println("Add: " + owner);
		}
	}

	public Relationship addRelationship(String ownerName, String targetName,
			List<Relationship> links) {
		// ensure the start and end points are included in the network
		addNode(ownerName);
		addNode(targetName);

		// add the link between them
		// Need to convert from the String text to the numerical index value
		Relationship link = new Relationship(getVerticeIndex(ownerName),
				getVerticeIndex(targetName));
		if (links.contains(link)) {
			// if it exists already, then get it and add 1 to the value
			link = links.get(links.indexOf(link));
			System.out.println("Was " + link);

			link.incrementValue();
			System.out.println("Now " + link);

			// add it back to where it came from
			links.set(links.indexOf(link), link);
		} else {

			System.out.println("Create " + link);
			links.add(link);
		}
		return link;
	}

	private int getVerticeIndex(String ownerName) {
		return vertices.indexOf(ownerName) + 1;
	}

	@Override
	protected List<uk.co.sleonard.unison.output.Relationship> createLinks(
			Vector<Vector<String>> nodePairs,
			List<uk.co.sleonard.unison.output.Relationship> links) {
		Iterator<Vector<String>> iter = nodePairs.iterator();
		while (iter.hasNext()) {
			Vector<String> row = iter.next();
			String key = row.get(0);
			String value = row.get(1);

			// System.out.println(key + " " + value);

			if (null != value && !value.equals("")) {
				addRelationship(key, value, links);
			} else {
				addNode(key);
			}
		}
		return links;
	}

	@Override
	protected void writeData(PrintStream printStream) {
		// first print out *Vertices and number of points
		printStream.println("*Vertices " + vertices.size());

		// Then list all the vertices
		Iterator iter = vertices.iterator();
		while (iter.hasNext()) {
			String nodeName = (String) iter.next();
			int nodeIndex = getVerticeIndex(nodeName);
			System.out.println("Write: " + nodeName);
			printStream.println(nodeIndex + " \"" + nodeName + "\"");
		}

		// List all the undirected links
		if (undirectedLinks.size() > 0) {
			printStream.println("*Edges");
			Iterator linksIter = undirectedLinks.iterator();
			while (linksIter.hasNext()) {
				Relationship link = (Relationship) linksIter.next();
				printStream.println(link.getOwner() + " " + link.getTarget()
						+ " " + link.getValue());
			}
		}

		// List all the directed links
		if (directedLinks.size() > 0) {
			printStream.println("*Edges");
			Iterator linksIter = directedLinks.iterator();
			while (linksIter.hasNext()) {
				Relationship link = (Relationship) linksIter.next();
				printStream.println(link.getOwner() + " " + link.getTarget()
						+ " " + link.getValue());
			}
		}
	}

}
