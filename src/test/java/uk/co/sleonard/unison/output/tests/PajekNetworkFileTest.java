package uk.co.sleonard.unison.output.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.co.sleonard.unison.gui.UNISoNException;
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
	 * test getPreviewPanel
	 */
	@Test
	public void testGetPreviewPanel() {
		try {
			assertNotNull(this.file.getPreviewPanel());
		} catch (UNISoNException e) {
			fail("ERROR : " + e.getMessage());
		}
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
		final Vector<Vector<String>> nodePairs = generateNodePairs();
		this.file.createDirectedLinks(nodePairs);
		assertEquals(2, nodePairs.size());
	}

	/**
	 * test createUndirectedLinks.
	 */
	@Test
	public void testCreateUndirectedLinks() {
		final Vector<Vector<String>> nodePairs = generateNodePairs();
		this.file.createUndirectedLinks(nodePairs);
		assertEquals(2, nodePairs.size());
	}

	/**
	 * Test getFilename
	 */
	@Test
	public void testGetFilename() {
		String expected = "UnitTest.net";
		testSaveToFile();
		assertEquals(expected, this.file.getFilename());
	}

	/**
	 * Test getFileSuffix
	 */
	@Test
	public void testGetFileSuffix() {
		String expected = ".net";
		assertEquals(expected, this.file.getFileSuffix());
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
		this.file.saveToFile("UnitTest");
		this.file.saveToFile("UnitTest.net");
	}

	/**
	 * test writeData.
	 */
	@Test
	public void testWriteData() {
		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		final Vector<Vector<String>> nodePairs = generateNodePairs();
		this.file.createDirectedLinks(nodePairs);
		this.file.writeData(new PrintStream(outContent));
		assertTrue(outContent.toString().contains("*Vertices"));
		this.file.createUndirectedLinks(nodePairs);
		this.file.writeData(new PrintStream(outContent));
		assertTrue(outContent.toString().contains("*Edges"));
		assertTrue(outContent.toString().contains("*Arcs"));

	}

	/**
	 * Generate Vector<Vector<String>> with test data.
	 * @return Vector<Vector<String>> filled.
	 */
	private Vector<Vector<String>> generateNodePairs() {
		final Vector<Vector<String>> nodePairs = new Vector<>();
		Vector<String> vector = new Vector<>();
		vector.addElement("Alf");
		vector.addElement("Bob");
		vector.addElement("Carl");
		vector.addElement("Carol");
		nodePairs.addElement(new Vector<String>(vector));
		nodePairs.addElement(new Vector<String>(vector));
		return nodePairs;
	}

}
