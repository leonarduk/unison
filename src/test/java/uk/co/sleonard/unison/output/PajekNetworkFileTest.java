/**
 * PajekNetworkFileTest
 *
 * @author ${author}
 * @since 20-Jun-2016
 */
package uk.co.sleonard.unison.output;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * The Class PajekNetworkFile.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 */
public class PajekNetworkFileTest {

    /**
     * The file.
     */
    private PajekNetworkFile file;

    /**
     * Generate Vector<Vector<String>> with test data.
     *
     * @return Vector<Vector < String>> filled.
     */
    private Vector<Vector> generateNodePairs() {
        final Vector<Vector> nodePairs = new Vector<>();
        final Vector<String> vector = new Vector<>();
        vector.addElement("Alf");
        vector.addElement("Bob");
        vector.addElement("Carl");
        vector.addElement("Carol");
        nodePairs.addElement(new Vector<>(vector));
        nodePairs.addElement(new Vector<>(vector));
        return nodePairs;
    }

    /**
     * Setup.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        this.file = new PajekNetworkFile();
    }

    /**
     * test addRelationship.
     */
    @Test
    public void testAddRelationship() {
        final List<Relationship> links = new Vector<>();
        Relationship link = this.file.addRelationship("Alf", "Bob", links);
        Assert.assertEquals(1, link.getValue());
        link = this.file.addRelationship("Alf", "Bob", links);
        Assert.assertEquals(2, link.getValue());
        Assert.assertEquals(1, links.size());
    }

    /**
     * test createDirectedLinks.
     */
    @Test
    public void testCreateDirectedLinks() {
        final Vector<Vector> nodePairs = this.generateNodePairs();
        this.file.createDirectedLinks(nodePairs);
        Assert.assertEquals(2, nodePairs.size());
    }

    /**
     * Test getFilename.
     */
    @Test
    public void testGetFilename() {
        final String expected = "UnitTest.net";
        this.testSaveToFile();
        Assert.assertEquals(expected, this.file.getFilename());
    }

    /**
     * Test getFileSuffix.
     */
    @Test
    public void testGetFileSuffix() {
        final String expected = ".net";
        Assert.assertEquals(expected, this.file.getFileSuffix());
    }

    /**
     * test saveToFile.
     */
    @Test
    public void testSaveToFile() {
        final HashMap<String, String> nodePairs = new HashMap<>();
        nodePairs.put("Alf", "Bertie");
        nodePairs.put("Bertie", "Charlie");
        nodePairs.put("Charlie", "Bertie");
        nodePairs.put("Bertie", "Charlie");
        nodePairs.put("Derek", "");
        this.file.saveToFile("UnitTest");
        this.file.saveToFile("UnitTest.net");
    }

    /**
     * saveToFile should reject a null or empty filename.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSaveToFileWithInvalidFilename() {
        this.file.saveToFile(" ");
    }

    /**
     * test writeData.
     */
    @Test
    public void testWriteData() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final Vector<Vector> nodePairs = this.generateNodePairs();
        this.file.createDirectedLinks(nodePairs);
        this.file.writeData(new PrintStream(outContent));
        Assert.assertTrue(outContent.toString().contains("*Vertices"));
        this.file.writeData(new PrintStream(outContent));
        Assert.assertTrue(outContent.toString().contains("*Arcs"));

    }

}
