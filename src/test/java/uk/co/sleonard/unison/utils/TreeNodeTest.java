package uk.co.sleonard.unison.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.co.sleonard.unison.utils.TreeNode;

/**
 * The Class TreeNodeTest.
 * 
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.3.0
 *
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
	 * Test setName
	 */
	@Test
	public void testSetName() {
		String expected = "othernode";
		TreeNode actual = new TreeNode(new Object(), "mytreenode");
		actual.setName(expected);
		assertEquals(expected, actual.toString());

	}
}
