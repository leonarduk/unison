package uk.co.sleonard.unison.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * The Class TreeNodeTest.
 *
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.3.0
 */
public class TreeNodeTest {

    /**
     * Test Constructor and toString
     */
    @Test
    public void testTreeNode() {
        String expected = "mytreenode";
        TreeNode actual = new TreeNode(new Object(), "mytreenode");
        assertEquals(expected, actual.toString());
    }

    /**
     * Test setNodeName
     */
    @Test
    public void testSetNodeName() {
        String expected = "othernode";
        TreeNode actual = new TreeNode(new Object(), "mytreenode");
        actual.setNodeName(expected);
        assertEquals(expected, actual.toString());

    }
}
