package uk.co.sleonard.unison.datahandling.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import uk.co.sleonard.unison.datahandling.DataQuery;
import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.DAO.Location;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;

import com.ibm.icu.util.Calendar;

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

	/**
	 * Test get location.
	 */
	@Test
	public void testGetLocations() {
		Vector<String> countries = new Vector<>();
		countries.addElement("England");
		Session session = mock(Session.class);
		boolean filtered = true;
		Vector locations = new Vector<Location>();
		locations.addElement(new Location());
		when(helper.runQuery(Matchers.anyString(), Matchers.any(Session.class), 
				Matchers.<Class<Location>>any())).thenReturn(locations);
		Vector<Location> actual = dataQuery.getLocations(countries, session, filtered);
		Vector expected = locations;
		assertEquals(expected, actual);
	}

	/**
	 * Test get location string.
	 */
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

	/**
	 * Test get messages.
	 */
	@Test
	public void testGetMessages() {
		Vector<Message> messages = new Vector<>();
		Vector<UsenetUser> users = new Vector<>();
		List<NewsGroup> news = new ArrayList<>();
		Set<String> countries = new HashSet<>();
		Date date = Calendar.getInstance().getTime();

		messages.addElement(new Message());
		users.addElement(new UsenetUser());
		news.add(new NewsGroup());
		countries.add("Brazil");
		Session session = mock(Session.class);
		boolean filtered = true;
		when(helper.runQuery(Matchers.anyString(), Matchers.any(Session.class), Matchers.<Class<Message>>any())).thenReturn(messages);
		Vector<Message> actual = dataQuery.getMessages(messages, users, session, date, date, filtered, news, countries); // If
																															// filtered
																															// is
																															// true
		Vector expected = messages;
		assertEquals(expected, actual);
		filtered = false;
		actual = dataQuery.getMessages(messages, users, session, date, date, filtered, news, countries); // If
																											// filtered
																											// is
																											// false
		assertEquals(expected, actual);
		actual = dataQuery.getMessages(null, null, session, null, null, filtered, null, null); // If
																								// all
																								// parameters
																								// is
																								// null
		assertEquals(expected, actual);
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
