package uk.co.sleonard.unison.datahandling.DAO.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.Topic;

/**
 * The Class TopicTest.
 */
public class TopicTest {

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
