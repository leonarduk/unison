package uk.co.sleonard.unison.datahandling.DAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.Topic;

/**
 * The Class MessageTest.
 * 
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.2.0
 *
 */
public class NewsGroupTest {

	/**
	 * Test getFirstMessage.
	 */
	@Test
	public void testGetFirstMessage() {
		int expected = 10;
		NewsGroup actual = new NewsGroup();
		assertEquals(0, actual.getFirstMessage());
		actual.setFirstMessage(expected);
		assertEquals(expected, actual.getFirstMessage());
	}

	/**
	 * Test getFullName.
	 */
	@Test
	public void testGetFullName() {
		String expected = "fullname";
		NewsGroup actual = new NewsGroup();
		assertNull(actual.getFullName());
		actual.setFullName(expected);
		assertEquals(expected, actual.getFullName());
	}

	/**
	 * Test getId.
	 */
	@Test
	public void testGetId() {
		assertEquals(0, new NewsGroup().getId());
	}

	/**
	 * Test testGetLastIndexDownloaded.
	 */
	@Test
	public void getLastIndexDownloaded() {
		int expected = 10;
		NewsGroup actual = new NewsGroup();
		assertEquals(0, actual.getLastIndexDownloaded());
		actual.setLastIndexDownloaded(expected);
		assertEquals(expected, actual.getLastIndexDownloaded());
	}

	/**
	 * Test testGetLastMessage.
	 */
	@Test
	public void getLastMessage() {
		int expected = 10;
		NewsGroup actual = new NewsGroup();
		assertEquals(0, actual.getLastMessage());
		actual.setLastMessage(expected);
		assertEquals(expected, actual.getLastMessage());
	}

	/**
	 * Test getLastMessageTotal.
	 */
	@Test
	public void testGetLastMessageTotal() {
		int expected = 10;
		NewsGroup actual = new NewsGroup();
		assertEquals(0, actual.getLastMessageTotal());
		actual.setLastMessageTotal(expected);
		assertEquals(expected, actual.getLastMessageTotal());
	}

	/**
	 * Test isLastNode.
	 */
	@Test
	public void testIsLastNode() {
		boolean expected = true;
		NewsGroup actual = new NewsGroup();
		assertFalse(actual.isLastNode());
		actual.setLastNode(expected);
		assertEquals(expected, actual.isLastNode());
	}

	/**
	 * Test getMessages.
	 */
	@Test
	public void testGetMessages() {
		Set<Message> expected = new HashSet<>(0);
		NewsGroup actual = new NewsGroup();
		assertEquals(expected.size(), actual.getMessages().size());
		expected.add(new Message());
		actual.setMessages(expected);
		assertEquals(expected.size(), actual.getMessages().size());
	}

	/**
	 * Test getName.
	 */
	@Test
	public void testGetName() {
		String expected = "myname";
		NewsGroup actual = new NewsGroup();
		assertNull(actual.getName());
		actual.setName(expected);
		assertEquals(expected, actual.getName());
	}

	/**
	 * Test getParentNewsGroup.
	 */
	@Test
	public void testGetParentNewsGroup() {
		NewsGroup expected = new NewsGroup("newsname");
		NewsGroup actual = new NewsGroup();
		assertNull(actual.getParentNewsGroup());
		actual.setParentNewsGroup(expected);
		assertEquals(expected.getName(), actual.getParentNewsGroup().getName());
	}

	/**
	 * Test getTopics.
	 */
	@Test
	public void testGetTopics() {
		Set<Topic> expected = new HashSet<>(0);
		NewsGroup actual = new NewsGroup();
		assertEquals(expected.size(), actual.getTopics().size());
		expected.add(new Topic());
		actual.setTopics(expected);
		assertEquals(expected.size(), actual.getTopics().size());
	}

	/**
	 * Test toString.
	 */
	@Test
	public void testToString() {
		String expected = new String("fullname");
		NewsGroup actual = new NewsGroup();
		actual.setFullName("fullname");
		assertEquals(expected, actual.toString());
	}

	/**
	 * Test compareTo.
	 */
	@Test
	public void testCompareTo() {
		NewsGroup expected = new NewsGroup();
		expected.setFullName("ng1");
		NewsGroup actual = new NewsGroup();
		actual.setFullName("ng1");
		assertEquals(0, actual.compareTo(expected));
	}

	/**
	 * Test hashCode
	 */
	public void testHashCode() {
		TestNewsGroup actual1 = new TestNewsGroup();
		TestNewsGroup actual2 = new TestNewsGroup();
		actual1.setIdSuper(5);
		actual2.setIdSuper(93);
		assertTrue(actual1.hashCode() != actual2.hashCode());
	}

}

/**
 * Extends NewsGroup to change setId(protected)
 * 
 * @author Elton <elton_12_nunes@hotmail.com>
 *
 */
@SuppressWarnings("serial")
class TestNewsGroup extends NewsGroup {
	void setIdSuper(int id) {
		super.setId(id);
	}
}
