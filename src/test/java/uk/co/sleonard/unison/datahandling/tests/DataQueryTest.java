package uk.co.sleonard.unison.datahandling.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.junit.Ignore;
import org.junit.Test;

import uk.co.sleonard.unison.datahandling.DataQuery;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;

/**
 * The Class DataQueryTest.
 */

public class DataQueryTest {

	@Ignore
	@Test
	public void testGetLocations() {
		
	}

	/**
	 * Test get message ids.
	 */
	@Test
	public void testGetMessageIdsString() {
		Vector<Message> messages = new Vector<>();
		Vector<Message> messagesEmpty = new Vector<>();
		
		Message message = mock(Message.class);
		when(message.getId()).thenReturn(1);
		messages.add(message);
		assertEquals("'1'", DataQuery.getMessageIdsString(messages).toString());	//1 object
		messages.add(message);
		String test = DataQuery.getMessageIdsString(messages).toString();
		assertEquals(true, test.contains(","));					//2 objects
		assertNotNull(DataQuery.getMessageIdsString(null));		//If null
		assertNotNull(DataQuery.getMessageIdsString(messagesEmpty)); //If size <= 0
		
	}

	@Ignore
	@Test
	public void testGetMessages() {
		
	}

	/**
	 * Test join strings.
	 */
	@Test
	public void testJoin() {
		List<String> words = new ArrayList<>();
		
		words.add("apple");
		assertEquals("apple", DataQuery.join(words, "-"));
		words.add("pear");
		assertEquals("apple-pear", DataQuery.join(words, "-"));
	}

	/**
	 * Test get UsenetUser ids.
	 */
	@Test
	public void testGetUsenetUserIdsString() {
		Vector<UsenetUser> users = new Vector<>();
		Vector<UsenetUser> usersEmpty = new Vector<>();
		
		UsenetUser user = mock(UsenetUser.class);
		when(user.getId()).thenReturn(1);
		users.add(user);
		assertEquals("'1'", DataQuery.getUsenetUserIdsString(users).toString());	//1 object
		users.add(user);
		String test = DataQuery.getUsenetUserIdsString(users).toString();
		assertEquals(true, test.contains(","));					//2 objects
		assertNotNull(DataQuery.getUsenetUserIdsString(null));		//If null
		assertNotNull(DataQuery.getUsenetUserIdsString(usersEmpty)); //If size <= 0
	}

}
