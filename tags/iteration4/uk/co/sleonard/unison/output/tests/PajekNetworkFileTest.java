package uk.co.sleonard.unison.output.tests;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import junit.framework.TestCase;
import uk.co.sleonard.unison.output.PajekNetworkFile;
import uk.co.sleonard.unison.output.Relationship;

public class PajekNetworkFileTest extends TestCase {
	private PajekNetworkFile file;

	@Override
	protected void setUp() throws Exception {
		this.file = new PajekNetworkFile();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testAddNode() {
//		Assert.fail("Not yet implemented");
	}

	public void testAddRelationship() {
		final List<Relationship> links = new Vector<Relationship>();
		Relationship link = this.file.addRelationship("Alf", "Bob", links);
		System.out.println("Link1: " + link);
		link = this.file.addRelationship("Alf", "Bob", links);
		System.out.println("Link2: " + link);
	}

	public void testCreateDirectedLinks() {
		final HashMap<String, String> nodePairs = new HashMap<String, String>();
		nodePairs.put("Alf", "Bertie");
		nodePairs.put("Bertie", "Charlie");
		nodePairs.put("Charlie", "Bertie");

		nodePairs.put("Bertie", "Charlie");
		nodePairs.put("Derek", "");

		// List links = file.createDirectedLinks(nodePairs);
		// System.out.println("THere are " + links.size());
		// ListIterator<Relationship> iter = links.listIterator();

		// while (iter.hasNext()) {
		// System.out.println(iter.next());
		// }

	}

	public void testCreateUndirectedLinks() {
//		Assert.fail("Not yet implemented");
	}

	public void testSaveToFile() {
		final HashMap<String, String> nodePairs = new HashMap<String, String>();
		nodePairs.put("Alf", "Bertie");
		nodePairs.put("Bertie", "Charlie");
		nodePairs.put("Charlie", "Bertie");

		nodePairs.put("Bertie", "Charlie");
		nodePairs.put("Derek", "");

		// List links = file.createDirectedLinks(nodePairs);
		this.file.saveToFile("UnitTest");
	}

	public void testWriteData() {
		final Vector<Vector<String>> nodePairs = new Vector<Vector<String>>();
		// nodePairs.add(new Vector<String> ( Array. new String[] { "Alf",
		// "Bertie"} ) );
		// nodePairs.put("Bertie", "Charlie");
		// nodePairs.put("Charlie", "Bertie");
		//
		// nodePairs.put("Bertie", "Charlie");
		// nodePairs.put("Derek", "");

		this.file.createDirectedLinks(nodePairs);
		this.file.writeData(System.out);
	}

}
