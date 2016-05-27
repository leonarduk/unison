package uk.co.sleonard.unison.output.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import uk.co.sleonard.unison.output.Relationship;

/**
 * The Class RelationshipTest.
 * 
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.2.0
 *
 */
public class RelationshipTest {

	/**
	 * Setup.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * test getOwner.
	 */
	@Test
	public void testGetOwner() {
		int expected = 5;
		Relationship actual = new Relationship(5, 0);
		assertEquals(expected, actual.getOwner());
	}

	/**
	 * test getTarget.
	 */
	@Test
	public void testGetTarget() {
		int expected = 3;
		Relationship actual = new Relationship(0, 3);
		assertEquals(expected, actual.getTarget());
	}

	/**
	 * test getValue.
	 */
	@Test
	public void testGetValue() {
		int expected = 1;
		Relationship actual = new Relationship(0, 0);
		assertEquals(expected, actual.getValue());
	}

	/**
	 * test incrementValue.
	 */
	@Test
	public void testIncrementValue() {
		int expected = 3;
		Relationship actual = new Relationship(0, 0);
		actual.incrementValue();
		actual.incrementValue();
		assertEquals(expected, actual.getValue());
	}

	/**
	 * test isDirected and makeUndirected.
	 */
	@Test
	public void testIsDirectedAndMakeUndirected() {
		Relationship actual = new Relationship(0, 0);
		assertTrue(actual.isDirected());
		actual.makeUndirected();
		assertFalse(actual.isDirected());
	}

	/**
	 * test toString.
	 */
	@Test
	public void testToString() {
		String expected = "1 0 1";
		Relationship actual = new Relationship(1, 0);
		assertEquals(expected, actual.toString());
		actual.incrementValue();
		expected = "1 0 2";
		assertEquals(expected, actual.toString());
	}

	/**
	 * test HashCode
	 */
	@Test
	public void testHashCode() {
		Relationship test = new Relationship(10, 20);
		assertEquals(31, test.hashCode());
		test.makeUndirected();
		assertEquals(30, test.hashCode());
	}

	/**
	 * test Equals
	 */
	@Test
	public void testEquals() {
		assertEquals(new Relationship(10, 10), new Relationship(10, 10));
		assertFalse(new Relationship(10, 10).equals(new Object()));
	}

}
