/**
 * RelationshipTest
 *
 * @author ${author}
 * @since 20-Jun-2016
 */
package uk.co.sleonard.unison.output;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The Class RelationshipTest.
 *
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.2.0
 */
public class RelationshipTest {

    /**
     * Setup.
     *
     * @throws Exception the exception
     */
    @BeforeEach
    public void setUp() throws Exception {
    }

    /**
     * test Equals
     */
    @Test
    public void testEquals() {
        final Relationship r1 = new Relationship(10, 10);
        final Relationship r2 = new Relationship(10, 10);
        final Relationship r3 = new Relationship(10, 11);

        Assertions.assertEquals(r1, r2);
        Assertions.assertEquals(r2, r1);
        Assertions.assertNotEquals(r1, r3);
    }

    /**
     * test getOwner.
     */
    @Test
    public void testGetOwner() {
        final int expected = 5;
        final Relationship actual = new Relationship(5, 0);
        Assertions.assertEquals(expected, actual.getOwner());
    }

    /**
     * test getTarget.
     */
    @Test
    public void testGetTarget() {
        final int expected = 3;
        final Relationship actual = new Relationship(0, 3);
        Assertions.assertEquals(expected, actual.getTarget());
    }

    /**
     * test getValue.
     */
    @Test
    public void testGetValue() {
        final int expected = 1;
        final Relationship actual = new Relationship(0, 0);
        Assertions.assertEquals(expected, actual.getValue());
    }

    /**
     * test HashCode
     */
    @Test
    public void testHashCode() {
        final Relationship r1 = new Relationship(10, 20);
        final Relationship r2 = new Relationship(10, 20);
        Assertions.assertEquals(r1.hashCode(), r2.hashCode());
        r2.incrementValue();
        Assertions.assertNotEquals(r1.hashCode(), r2.hashCode());
    }

    /**
     * test incrementValue.
     */
    @Test
    public void testIncrementValue() {
        final int expected = 3;
        final Relationship actual = new Relationship(0, 0);
        actual.incrementValue();
        actual.incrementValue();
        Assertions.assertEquals(expected, actual.getValue());
    }

    /**
     * test toString.
     */
    @Test
    public void testToString() {
        String expected = "Relationship(owner=1, target=0, value=1)";
        final Relationship actual = new Relationship(1, 0);
        Assertions.assertEquals(expected, actual.toString());
        actual.incrementValue();
        expected = "Relationship(owner=1, target=0, value=2)";
        Assertions.assertEquals(expected, actual.toString());
    }

}
