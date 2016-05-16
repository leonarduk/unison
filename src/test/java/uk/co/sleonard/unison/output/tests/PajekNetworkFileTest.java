package uk.co.sleonard.unison.output.tests;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.co.sleonard.unison.output.PajekNetworkFile;
import uk.co.sleonard.unison.output.Relationship;

/**
 * The Class PajekNetworkFile.
 */
public class PajekNetworkFileTest {
	private PajekNetworkFile file;

	/**
	 * Setup.
	 */
	@Before
	public void setUp() throws Exception {
		this.file = new PajekNetworkFile();
	}

	/**
	 * After.
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * test addRelationship.
	 */
	@Test
	public void testAddRelationship() {
		final List<Relationship> links = new Vector<Relationship>();
		Relationship link = this.file.addRelationship("Alf", "Bob", links);
		System.out.println("Link1: " + link);
		link = this.file.addRelationship("Alf", "Bob", links);
		System.out.println("Link2: " + link);
	}

	/**
	 * test createDirectedLinks.
	 */
	@Test
	public void testCreateDirectedLinks() {
		final Vector<Vector<String>> nodePairs = new Vector<>();
		Vector<String> vector = new Vector<>();
		vector.addElement("Alf");
		vector.addElement("Bob");
		vector.addElement("Carl");
		vector.addElement("Carol");
		nodePairs.addElement(new Vector<String>(vector));
		nodePairs.addElement(new Vector<String>(vector));
		this.file.createDirectedLinks(nodePairs);
		assertEquals(2, nodePairs.size());
	}

	/**
	 * test createUndirectedLinks.
	 */
	@Test
	public void testCreateUndirectedLinks() {
		final Vector<Vector<String>> nodePairs = new Vector<>();
		Vector<String> vector = new Vector<>();
		vector.addElement("Erick");
		vector.addElement("John");
		vector.addElement("Katy");
		vector.addElement("Jonny");
		nodePairs.addElement(new Vector<String>(vector));
		nodePairs.addElement(new Vector<String>(vector));
		this.file.createUndirectedLinks(nodePairs);
		assertEquals(2, nodePairs.size());
	}

	/**
	 * test saveToFile.
	 */
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

	/**
	 * test writeData.
	 */
	@Test
	public void testWriteData() {
		final Vector<Vector<String>> nodePairs = new Vector<Vector<String>>();
		this.file.createDirectedLinks(nodePairs);
		this.file.writeData(System.out);
	}

}
