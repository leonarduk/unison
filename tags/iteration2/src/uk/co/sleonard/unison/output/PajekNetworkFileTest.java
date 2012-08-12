package uk.co.sleonard.unison.output;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import junit.framework.TestCase;

public class PajekNetworkFileTest extends TestCase {
	private PajekNetworkFile file;

	@Override
	protected void setUp() throws Exception {
		file = new PajekNetworkFile();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	public void testWriteData() {
		Vector<Vector<String>> nodePairs = new Vector<Vector<String>>();
//		nodePairs.add(new Vector<String> ( Array. new String[] { "Alf", "Bertie"} ) );
//		nodePairs.put("Bertie", "Charlie");
//		nodePairs.put("Charlie", "Bertie");
//
//		nodePairs.put("Bertie", "Charlie");
//		nodePairs.put("Derek", "");

		file.createDirectedLinks(nodePairs);
		file.writeData(System.out);
	}

	public void testAddNode() {
		fail("Not yet implemented");
	}

	public void testAddRelationship() {
		List<Relationship> links = new Vector<Relationship>();
		Relationship link = file.addRelationship("Alf", "Bob", links);
		System.out.println("Link1: " + link);
	    link = file.addRelationship("Alf", "Bob", links);
		System.out.println("Link2: " + link);
	}

	public void testCreateDirectedLinks() {
		HashMap<String,String> nodePairs = new HashMap<String, String>();
		nodePairs.put("Alf", "Bertie");
		nodePairs.put("Bertie", "Charlie");
		nodePairs.put("Charlie", "Bertie");

		nodePairs.put("Bertie", "Charlie");
		nodePairs.put("Derek", "");

//		List links = file.createDirectedLinks(nodePairs);
//		System.out.println("THere are " + links.size());
//		ListIterator<Relationship> iter = links.listIterator();

//		while (iter.hasNext()) {
//			System.out.println(iter.next());
//		}

	}

	public void testCreateUndirectedLinks() {
		fail("Not yet implemented");
	}

	public void testSaveToFile() {
		HashMap<String, String> nodePairs = new HashMap<String, String>();
		nodePairs.put("Alf", "Bertie");
		nodePairs.put("Bertie", "Charlie");
		nodePairs.put("Charlie", "Bertie");

		nodePairs.put("Bertie", "Charlie");
		nodePairs.put("Derek", "");

//		List links = file.createDirectedLinks(nodePairs);
		file.saveToFile("UnitTest");
	}

}
