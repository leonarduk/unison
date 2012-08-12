//package uk.co.sleonard.unison.output;
//
//import java.io.PrintStream;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Vector;
//
//
//@Deprecated
//public class NetMinerDataFile  {
//
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//
//	}
//
//	private List<Relationship> directedLinks;
//
//	private List<Relationship> undirectedLinks;
//
//	private List<String> vertices;
//
//	// Statically assign the suffix
//	{
////		SUFFIX = ".ntf";
//	}
//
//	public NetMinerDataFile() {
//
//		directedLinks = new Vector<Relationship>();
//
//		undirectedLinks = new Vector<Relationship>();
//		vertices = new Vector<String>();
//
//	}
//
//	private void addNode(String owner) {
//
//		if (!vertices.contains(owner)) {
//			vertices.add(owner);
//			System.out.println("Add: " + owner);
//		}
//	}
//
//	public Relationship addRelationship(String ownerName, String targetName,
//			List<Relationship> links) {
//		// ensure the start and end points are included in the network
//		addNode(ownerName);
//		addNode(targetName);
//
//		// add the link between them
//		// Need to convert from the String text to the numerical index value
//		Relationship link = new Relationship(getVerticeIndex(ownerName),
//				getVerticeIndex(targetName));
//		if (links.contains(link)) {
//			// if it exists already, then get it and add 1 to the value
//			link = links.get(links.indexOf(link));
//			System.out.println("Was " + link);
//
//			link.incrementValue();
//			System.out.println("Now " + link);
//
//			// add it back to where it came from
//			links.set(links.indexOf(link), link);
//		} else {
//
//			System.out.println("Create " + link);
//			links.add(link);
//		}
//		return link;
//	}
//
//	private int getVerticeIndex(String ownerName) {
//		return vertices.indexOf(ownerName) + 1;
//	}
//
//	public List<Relationship> createDirectedLinks(HashMap<String,?> nodePairs) {
//		directedLinks = createLinks(nodePairs, directedLinks);
//
//		return directedLinks;
//	}
//
//	protected List<Relationship> createLinks(HashMap<String, ?> nodePairs,
//			List<Relationship> links) {
//		Iterator<String> iter = nodePairs.keySet().iterator();
//		while (iter.hasNext()) {
//			String key = iter.next();
//			String value = (String) nodePairs.get(key);
//
//			// System.out.println(key + " " + value);
//
//			if (null != value && !value.equals("")) {
//				addRelationship(key, value, links);
//			} else {
//				addNode(key);
//			}
//		}
//		return links;
//	}
//
//	public List<Relationship> createUndirectedLinks(HashMap<String,?> nodePairs) {
//		undirectedLinks = createLinks(nodePairs, undirectedLinks);
//		return undirectedLinks;
//	}
//
//	protected void writeData(PrintStream printStream) {
//		printStream.println("NTF=2.4");
////		printStream.println("TITLE=" + getFilename());
//		printStream.println("N=" + vertices.size());
//		printStream.print("NODELIST=");
//
//		// Then list all the vertices
//		Iterator<String> iter = vertices.iterator();
//		while (iter.hasNext()) {
//			String nodeName = iter.next();
//			System.out.println("Write: " + nodeName);
//			printStream.print(nodeName);
//			if (iter.hasNext()) {
//				printStream.print(",");
//			} else {
//				printStream.println();
//			}
//		}
//		printStream.print("ADJ#=1");
//
//		printStream.print("ADJNAME=No Name");
//
//		printStream.print("TYPE=FULLMATRIX,DIAGONAL=NO");
//
//		printStream.print("DATA");
//
//		// , sep matrix
//
//		// List all the directed links
//		if (directedLinks.size() > 0) {
//			printStream.print("DIRECTION=YES,WEIGHT=YES");
//			printStream.println("*Edges");
//			Iterator<Relationship> linksIter = directedLinks.iterator();
//			while (linksIter.hasNext()) {
//				Relationship link = linksIter.next();
//				printStream.println(link.getOwner() + " " + link.getTarget()
//						+ " " + link.getValue());
//			}
//		}
//	}
//}
