package uk.co.sleonard.unison.output.tests;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import uk.co.sleonard.unison.output.PajekNetworkFile;
import uk.co.sleonard.unison.output.Relationship;

/**
 * The Class PajekNetworkFile.
 */
public class PajekNetworkFileTest{
	private PajekNetworkFile file;

	@Before
	public void setUp() throws Exception {
		this.file = new PajekNetworkFile();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	@Ignore
	public void testAddNode() {
//		Assert.fail("Not yet implemented");
	}

	@Test
	public void testAddRelationship() {
		final List<Relationship> links = new Vector<Relationship>();
		Relationship link = this.file.addRelationship("Alf", "Bob", links);
		System.out.println("Link1: " + link);
		link = this.file.addRelationship("Alf", "Bob", links);
		System.out.println("Link2: " + link);
	}

	@Test
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

	@Test
	@Ignore
	public void testCreateUndirectedLinks() {
//		Assert.fail("Not yet implemented");
	}

	@Test
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

	@Test
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
