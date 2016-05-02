package uk.co.sleonard.unison.datahandling.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Matchers;

import uk.co.sleonard.unison.datahandling.DataQuery;
import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.DAO.Location;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;

/**
 * The Class DataQueryTest.
 */

public class DataQueryTest {

	private DataQuery dataQuery;
	private HibernateHelper helper;

	@Before
	public void setUp() {
		helper = mock(HibernateHelper.class);
		dataQuery = new DataQuery(helper);
	}

	@Test
	public void testGetLocations() {
		Vector<String> countries = new Vector<>();
		countries.addElement("England");
		Session session = mock(Session.class);
		boolean filtered = true;
		Vector locations = new Vector<Location>();
		locations.addElement(new Location());
		when(helper.runQuery(Matchers.anyString(), Matchers.any(Session.class))).thenReturn(locations);
		Vector<Location> actual = dataQuery.getLocations(countries, session, filtered);
		Vector expected = locations;
		assertEquals(expected, actual);
	}

	@Test
	public void testGetLocationString() {
		Vector<String> countries = new Vector<>();
		countries.addElement("England");
		String actual = dataQuery.getLocationsSQL(countries).toString();
		String expected = " FROM uk.co.sleonard.unison.datahandling.DAO.Location where country in ( 'England') ";
		assertEquals(expected, actual);
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
		assertEquals("'1'", dataQuery.getMessageIdsString(messages).toString()); // 1
																					// object
		messages.add(message);
		String test = dataQuery.getMessageIdsString(messages).toString();
		assertEquals(true, test.contains(",")); // 2 objects
		assertNotNull(dataQuery.getMessageIdsString(null)); // If null
		assertNotNull(dataQuery.getMessageIdsString(messagesEmpty)); // If size
																		// <= 0

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
		assertEquals("apple", dataQuery.join(words, "-"));
		words.add("pear");
		assertEquals("apple-pear", dataQuery.join(words, "-"));
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
		assertEquals("'1'", dataQuery.getUsenetUserIdsString(users).toString()); // 1
																					// object
		users.add(user);
		String test = dataQuery.getUsenetUserIdsString(users).toString();
		assertEquals(true, test.contains(",")); // 2 objects
		assertNotNull(dataQuery.getUsenetUserIdsString(null)); // If null
		assertNotNull(dataQuery.getUsenetUserIdsString(usersEmpty)); // If size
																		// <= 0
	}

}
