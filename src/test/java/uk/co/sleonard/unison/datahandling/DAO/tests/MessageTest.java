package uk.co.sleonard.unison.datahandling.DAO.tests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
 */
public class MessageTest {

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

}
