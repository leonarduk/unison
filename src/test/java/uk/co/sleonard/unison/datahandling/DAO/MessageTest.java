package uk.co.sleonard.unison.datahandling.DAO;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.ibm.icu.util.Calendar;

import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;

/**
 * The Class MessageTest.
 * 
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.2.0
 *
 */
public class MessageTest {

	/**
	 * Test Constructors
	 */
	@Test
	public void testConstructors() {
		Message actual = null;
		Date expected1 = Calendar.getInstance().getTime();
		String expected2 = "usenetMessage";
		String expected3 = "subject";
		UsenetUser expected4 = new UsenetUser();
		Topic expected5 = new Topic();
		Set<NewsGroup> expected6 = new HashSet<>();
		String expected7 = "refMess";
		byte[] expected8 = { 10, 10, 10 };
		actual = new Message(expected1, expected2, expected3, expected4, expected5, expected6,
		        expected7, expected8);

		assertEquals(expected1, actual.getDateCreated());
		assertEquals(expected2, actual.getUsenetMessageID());
		assertEquals(expected3, actual.getSubject());
		assertEquals(expected4, actual.getPoster());
		assertEquals(expected5, actual.getTopic());
		assertEquals(expected6, actual.getNewsgroups());
		assertEquals(expected7, actual.getReferencedMessages());
		assertArrayEquals(expected8, actual.getMessageBody());

		actual = new Message(expected1, expected2, expected3, expected4, expected5, expected8);
		assertEquals(expected1, actual.getDateCreated());
		assertEquals(expected2, actual.getUsenetMessageID());
		assertEquals(expected3, actual.getSubject());
		assertEquals(expected4, actual.getPoster());
		assertEquals(expected5, actual.getTopic());
		assertArrayEquals(expected8, actual.getMessageBody());
	}

	/**
	 * Test toString.
	 */
	@Test
	public void testToString() {
		String expected = "Subject:null";
		Message actual = new Message();
		actual.setSubject("Subject");
		assertEquals(expected, actual.toString());
	}

	/**
	 * Test getDateCreated.
	 */
	@Test
	public void testGetDateCreated() {
		Date expected = Calendar.getInstance().getTime();
		Message actual = new Message();
		assertNull(actual.getDateCreated());
		actual.setDateCreated(expected);
		assertEquals(0, expected.compareTo(actual.getDateCreated()));
	}

	/**
	 * Test getId.
	 */
	@Test
	public void testGetId() {
		assertEquals(0, new Message().getId());
	}

	/**
	 * Test getMessageBody.
	 */
	@Test
	public void testGetMessageBody() {
		byte[] expecteds = { 0, 1, 2, 3, 4 };
		Message actuals = new Message();
		assertNull(actuals.getMessageBody());
		actuals.setMessageBody(expecteds);
		assertArrayEquals(expecteds, actuals.getMessageBody());
	}

	/**
	 * Test getNewsgroups.
	 */
	@Test
	public void testGetNewsgroups() {
		Set<NewsGroup> expected = new HashSet<>();
		Message actual = new Message();
		assertEquals(expected.size(), actual.getNewsgroups().size());
		expected.add(new NewsGroup());
		actual.setNewsgroups(expected);
		assertEquals(expected.size(), actual.getNewsgroups().size());
	}

	/**
	 * Test getPoster.
	 */
	@Test
	public void testGetPoster() {
		UsenetUser expected = new UsenetUser();
		Message actual = new Message();
		assertNull(actual.getPoster());
		actual.setPoster(expected);
		assertEquals(expected, actual.getPoster());
	}

	/**
	 * Test getReferencedMessages.
	 */
	@Test
	public void testGetReferencedMessages() {
		String expected = new String("refMess");
		Message actual = new Message();
		assertNull(actual.getReferencedMessages());
		actual.setReferencedMessages(expected);
		assertEquals(expected, actual.getReferencedMessages());
	}

	/**
	 * Test getSubject.
	 */
	@Test
	public void testGetSubject() {
		String expected = new String("subject");
		Message actual = new Message();
		assertNull(actual.getSubject());
		actual.setSubject(expected);
		assertEquals(expected, actual.getSubject());
	}

	/**
	 * Test getTopic.
	 */
	@Test
	public void testGetTopic() {
		Topic expected = new Topic();
		expected.setSubject("sub");
		Message actual = new Message();
		assertNull(actual.getTopic());
		actual.setTopic(expected);
		assertEquals(expected.getSubject(), actual.getTopic().getSubject());
	}

	/**
	 * Test getUsenetMessageId.
	 */
	@Test
	public void testGetUsenetMessageID() {
		String expected = new String("usenetMessId");
		Message actual = new Message();
		assertNull(actual.getUsenetMessageID());
		actual.setUsenetMessageID(expected);
		assertEquals(expected, actual.getUsenetMessageID());
	}

	/**
	 * Test hashCode
	 */
	@Test
	public void testHashCode() {
		TestMessage actual1 = new TestMessage();
		TestMessage actual2 = new TestMessage();
		actual1.setIdSuper(1);
		actual2.setIdSuper(2);
		assertTrue(actual1.hashCode() != actual2.hashCode());
	}

}

/**
 * Extends Message to change setId(protected)
 * 
 * @author Elton <elton_12_nunes@hotmail.com>
 *
 */
@SuppressWarnings("serial")
class TestMessage extends Message {
	void setIdSuper(int id) {
		super.setId(id);
	}
}
