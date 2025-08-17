/**
 * TopicTest
 *
 * @author ${author}
 * @since 20-Jun-2016
 */
package uk.co.sleonard.unison.datahandling.DAO;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Extends Topic to change setId(protected)
 *
 * @author Elton <elton_12_nunes@hotmail.com>
 */
@SuppressWarnings("serial")
class TestTopic extends Topic {
}

/**
 * The Class TopicTest.
 *
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.2.0
 */
public class TopicTest {

    /**
     * Test Constructor
     */
    @Test
    public void testConstructor() {
        Topic actual = null;
        final String expected = "subject";
        final Set<NewsGroup> expected2 = new HashSet<>();

        actual = new Topic(expected, expected2);
        Assertions.assertEquals(expected, actual.getSubject());
        Assertions.assertEquals(expected2, actual.getNewsgroups());
    }

    /**
     * Test getId.
     */
    @Test
    public void testGetId() {
        Assertions.assertEquals(0, new Topic().getId());
    }

    /**
     * Test getNewsgroups.
     */
    @Test
    public void testGetNewsgroups() {
        final Set<NewsGroup> expected = new HashSet<>(0);
        final Topic actual = new Topic();
        Assertions.assertEquals(expected.size(), actual.getNewsgroups().size());
        expected.add(new NewsGroup());
        actual.setNewsgroups(expected);
        Assertions.assertEquals(expected.size(), actual.getNewsgroups().size());
    }

    /**
     * Test getSubject.
     */
    @Test
    public void testGetSubject() {
        final String expected = "subject";
        final Topic actual = new Topic();
        Assertions.assertNull(actual.getSubject());
        actual.setSubject(expected);
        Assertions.assertEquals(expected, actual.getSubject());
    }

    /**
     * Test hashCode
     */
    @Test
    public void testHashCode() {
        final TestTopic actual1 = new TestTopic();
        final TestTopic actual2 = new TestTopic();
        actual1.setSubject("abc");
        actual2.setSubject("def");
        Assertions.assertTrue(actual1.hashCode() != actual2.hashCode());
    }

    /**
     * Test toString.
     */
    @Test
    public void testToString() {
        final String expected = new String("subject");
        final Topic actual = new Topic(expected, null);
        Assertions.assertEquals(expected, actual.toString());
    }
}
