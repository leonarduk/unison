package uk.co.sleonard.unison.datahandling.DAO;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * The Class ResultRowTest.
 *
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.2.0
 */
public class ResultRowTest {

    /**
     * Test toString.
     */
    @Test
    public void testToString() {
        var obj = new Object();
        var actual = new ResultRow(obj, 3, null);
        var expected = new String(actual.count() + " " + actual.key());
        assertEquals(expected, actual.toString());
    }

    /**
     * Test getKey.
     */
    @Test
    public void testGetKey() {
        var expected = new Object();
        var actual = new ResultRow(expected, 0, null);
        assertEquals(expected, actual.key());
    }

    /**
     * Test getType.
     */
    @Test
    public void testGetType() {
        var expected = ResultRow.class;
        var actual = new ResultRow(null, 0, expected);
        assertEquals(expected, actual.type());
    }

    /**
     * Test getCount.
     */
    @Test
    public void testGetCount() {
        var expected = 5;
        var actual = new ResultRow(null, expected, null);
        assertEquals(expected, actual.count());
    }

    /**
     * Test compareTo.
     */
    @Test
    public void testCompareTo() {
        var expected = new ResultRow(null, 5, ResultRow.class);
        var actual = new ResultRow(null, 3, ResultRow.class);
        assertEquals(2, actual.compareTo(expected));
    }
}
