package uk.co.sleonard.unison.datahandling.DAO.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.Topic;

/**
 * The Class TopicTest.
 * 
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.2.0
 *
 */
public class TopicTest {

	/**
	 * Test Constructor
	 */
	@Test
	public void testConstructor() {
		Topic actual = null;
		String expected = "subject";
		Set<NewsGroup> expected2 = new HashSet<>();

		actual = new Topic(expected, expected2);
		assertEquals(expected, actual.getSubject());
		assertEquals(expected2, actual.getNewsgroups());
	}

	/**
	 * Test hashCode
	 */
	@Test
	public void testHashCode() {
		TestTopic actual1 = new TestTopic();
		TestTopic actual2 = new TestTopic();
		actual1.setIdSuper(7);
		actual2.setIdSuper(52);
		assertTrue(actual1.hashCode() != actual2.hashCode());
	}

	/**
	 * Test toString.
	 */
	@Test
	public void testToString() {
		String expected = new String("subject");
		Topic actual = new Topic(expected);
		assertEquals(expected, actual.toString());
	}

	/**
	 * Test getId.
	 */
	@Test
	public void testGetId() {
		assertEquals(0, new Topic().getId());
	}

	/**
	 * Test getNewsgroups.
	 */
	@Test
	public void testGetNewsgroups() {
		Set<NewsGroup> expected = new HashSet<NewsGroup>(0);
		Topic actual = new Topic();
		assertEquals(expected.size(), actual.getNewsgroups().size());
		expected.add(new NewsGroup());
		actual.setNewsgroups(expected);
		assertEquals(expected.size(), actual.getNewsgroups().size());
	}

	/**
	 * Test getSubject.
	 */
	@Test
	public void testGetSubject() {
		String expected = "subject";
		Topic actual = new Topic();
		assertNull(actual.getSubject());
		actual.setSubject(expected);
		assertEquals(expected, actual.getSubject());
	}
}

/**
 * Extends Topic to change setId(protected)
 * 
 * @author Elton <elton_12_nunes@hotmail.com>
 *
 */
@SuppressWarnings("serial")
class TestTopic extends Topic {
	void setIdSuper(int id) {
		super.setId(id);
	}
}
