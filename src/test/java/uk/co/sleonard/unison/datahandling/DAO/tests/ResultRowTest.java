package uk.co.sleonard.unison.datahandling.DAO.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.co.sleonard.unison.datahandling.DAO.ResultRow;

/**
 * The Class ResultRowTest.
 */
public class ResultRowTest {

	/**
	 * Test toString.
	 */
	@Test
	public void testToString() {
		Object obj = new Object();
		ResultRow actual = new ResultRow(obj, 3, null);
		String expected = new String(actual.getCount() + " " + actual.getKey());
		assertEquals(expected, actual.toString());
	}

	/**
	 * Test getKey.
	 */
	@Test
	public void testGetKey() {
		Object expected = new Object();
		ResultRow actual = new ResultRow(expected, 0, null);
		assertEquals(expected, actual.getKey());
	}

	/**
	 * Test getType.
	 */
	@Test
	public void testGetType() {
		Class<?> expected = ResultRow.class;
		ResultRow actual = new ResultRow(null, 0, expected);
		assertEquals(expected, actual.getType());
	}

	/**
	 * Test getCount.
	 */
	@Test
	public void testGetCount() {
		int expected = 5;
		ResultRow actual = new ResultRow(null, expected, null);
		assertEquals(expected, actual.getCount());
	}

	/**
	 * Test compareTo.
	 */
	@Test
	public void testCompareTo() {
		ResultRow expected = new ResultRow(null, 5, ResultRow.class);
		ResultRow actual = new ResultRow(null, 3, ResultRow.class);
		assertEquals(2, actual.compareTo(expected));
	}
}
