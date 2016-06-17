package uk.co.sleonard.unison.datahandling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;

import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DAO.IpAddress;
import uk.co.sleonard.unison.datahandling.DAO.Location;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.ResultRow;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
/**
 * The Class HibernateHelperTest.
 * 
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
public class HibernateHelperTest {

	private HibernateHelper	helper;
	private Session			session;

	/**
	 * Setup.
	 */
	@Before
	public void setUp() throws Exception {
		this.helper = new HibernateHelper(null);
		this.session = mock(Session.class);
	}

	/**
	 * Test create location.
	 */
	@Test
	public void testCreateLocation() {
		/*
		 * Address http://api.hostip.info/rough.php?ip= with problem. TODO Search other.
		 */
	}

	/**
	 * Test fetch all.
	 */
	@Test
	public void testFetchAll() {
		String queryExpected = "from uk.co.sleonard.unison.datahandling.DAO.Location";
		Vector<Location> expected = new Vector<>();
		expected.add(new Location());
		Query query = mock(Query.class);

		when(this.session.createQuery(queryExpected)).thenReturn(query);
		when(this.helper.runQuery(query, Location.class)).thenReturn(expected);
		try {
			List<Location> actual = this.helper.fetchAll(Location.class, this.session);
			assertEquals(expected.size(), actual.size());
		}
		catch (NullPointerException e) {
			fail(e.getMessage());
		}

	}

	/**
	 * Test fetch news groups.
	 */
	@Test
	public void testFetchBaseNewsGroups() {
		String queryExpected = "from uk.co.sleonard.unison.datahandling.DAO.NewsGroup where lastnode is true";
		Vector<NewsGroup> expected = new Vector<>();
		expected.addElement(new NewsGroup());
		Query query = mock(Query.class);
		when(this.session.createQuery(queryExpected)).thenReturn(query);
		when(this.helper.runQuery(query, NewsGroup.class)).thenReturn(expected);
		try {
			List<NewsGroup> actual = this.helper.fetchBaseNewsGroups(this.session);
			assertEquals(expected.size(), actual.size());
		}
		catch (NullPointerException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test to fetch or insert.
	 */
	@Test
	public void testFetchOrInsert() {
		List<Object> list = new ArrayList<>();
		IpAddress expected = new IpAddress();
		IpAddress actual = (IpAddress) this.helper.fetchOrInsert(this.session, expected, list);
		try {
			verify(this.session).saveOrUpdate(Matchers.any());		// Verify if method was called
			                                                  		// (saveOrUpdate)
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		list.add(expected);
		list.add(expected);
		assertSame(expected, actual);
	}

	/**
	 * Test to find by key.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testFindByKeyIntSessionClassOfQ() {
		Message expected = new Message();
		String query = "uk.co.sleonard.unison.datahandling.DAO.Message" + ".findByKey";
		Query queryMock = mock(Query.class);

		when(this.session.getNamedQuery(query)).thenReturn(queryMock);
		when(queryMock.uniqueResult()).thenReturn(expected);
		try {
			Message actual = (Message) this.helper.findByKey(1, this.session, Message.class);
			assertEquals(expected, actual);
			actual = null;
			try {
				when(queryMock.uniqueResult()).thenThrow(NonUniqueResultException.class);
				actual = (Message) this.helper.findByKey(1, this.session, Message.class);		// If
				                                                                         		// throw
				                                                                         		// Exception
			}
			catch (RuntimeException e) {
				assertNull(actual);
			}

		}
		catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test to find by key.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testFindByKeyStringSessionClassOfQ() {
		Message expected = new Message();
		String query = "uk.co.sleonard.unison.datahandling.DAO.Message" + ".findByKey";
		Query queryMock = mock(Query.class);

		when(this.session.getNamedQuery(query)).thenReturn(queryMock);
		when(queryMock.uniqueResult()).thenReturn(expected);
		try {
			Message actual = (Message) this.helper.findByKey("key", this.session, Message.class);
			assertEquals(expected, actual);
			actual = null;
			try {
				when(queryMock.uniqueResult()).thenThrow(NonUniqueResultException.class);
				actual = (Message) this.helper.findByKey("key", this.session, Message.class);		// If
				                                                                             		// throw
				                                                                             		// Exception
			}
			catch (RuntimeException e) {
				assertNull(actual);
			}

		}
		catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test to find or create newsgroup.
	 */
	@Test
	public void testFindOrCreateNewsGroup() {
		NewsGroup expected = new NewsGroup("allin");
		expected.setFullName("alt.pl.allin");
		NewsGroup actual = null;
		Query query = mock(Query.class);

		when(this.session.getNamedQuery(Matchers.anyString())).thenReturn(query);
		actual = this.helper.findOrCreateNewsGroup(this.session, "alt.pl.allin");	// If the search
		                                                                         	// no locate
		                                                                         	// data on DB
		assertEquals(expected, actual);
		when(query.uniqueResult()).thenReturn(expected);
		actual = this.helper.findOrCreateNewsGroup(this.session, "alt.pl.allin");	// If the search
		                                                                         	// locate data
		                                                                         	// on DB.
		assertEquals(expected, actual);
	}

	/**
	 * Test to generate schema.
	 */
	@Test
	public void testGenerateSchema() {
		this.helper.generateSchema();
	}

	/**
	 * Test create the google map URL.
	 */
	@Test
	public void testGetGoogleMapURL() {
		String expected = "http://maps.google.com/maps?f=q&hl=en&geocode=&q=" + "Houston"
		        + "&ie=UTF8&z=12&om=1";
		String actual = this.helper.getGoogleMapURL("Houston");
		assertEquals(expected, actual);			// If city is Houston
		expected = "UNKNOWN LOCATION";
		actual = this.helper.getGoogleMapURL("(Private Address)");
		assertEquals(expected, actual);			// If Private Address
	}

	/**
	 * Test get hibernate session.
	 */
	@Test
	public void testGetHibernateSession() {
		try {
			Session session = this.helper.getHibernateSession();
			assertNotNull(session);
		}
		catch (UNISoNException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test get list results.
	 */
	@Test
	public void testGetListResults() {
		Vector<ResultRow> actual = null;
		Vector<ResultRow> expected = new Vector<>();
		expected.addElement(new ResultRow("topic", 1, Topic.class));
		Vector<String[]> listTopic = new Vector<>(1);
		listTopic.addElement(new String[] { new String("topic"), new String("1") });
		String query = "select topic from Topic";

		SQLQuery queryMock = mock(SQLQuery.class);
		when(this.session.createSQLQuery(query)).thenReturn(queryMock);
		when(queryMock.list()).thenReturn(listTopic);
		actual = this.helper.getListResults(query, Topic.class, this.session);
		assertEquals(expected.get(0), actual.get(0));
	}

	/**
	 * Test get newsgroup by fullname.
	 */
	@Test
	public void testGetNewsgroupByFullName() {
		NewsGroup expected = new NewsGroup("newsgroup");
		String expectedQuery = new String(
		        "from uk.co.sleonard.unison.datahandling.DAO.NewsGroup" + " where fullname=?");
		Query queryMock = mock(Query.class);

		when(this.session.createQuery(expectedQuery)).thenReturn(queryMock);
		when(queryMock.setString(anyInt(), anyString())).thenReturn(queryMock);
		when(queryMock.uniqueResult()).thenReturn(expected);
		try {
			this.helper.getNewsgroupByFullName("newsgroup", this.session);
		}
		catch (Exception e) {
			fail("ERROR: " + e);
		}
	}

	/**
	 * Test get text.
	 */
	@Test
	public void testGetText() {
		String expected = null;
		String actual = null;

		Message message = new Message();
		UsenetUser user = new UsenetUser("User", "my@email.com", "private");
		message.setPoster(user);
		expected = "From:User(my@email.com)";
		actual = this.helper.getText(message);
		assertEquals(expected, actual);					// Message

		NewsGroup newsGroup = new NewsGroup("News");
		expected = "News";
		actual = this.helper.getText(newsGroup);
		assertEquals(expected, actual);					// News
		newsGroup.setLastMessageTotal(10);
		expected = "News (10)";
		actual = this.helper.getText(newsGroup);
		assertEquals(expected, actual);					// News with Last Message Total.

		Location location = new Location();
		location.setCity("Sao Paulo");
		location.setCountryCode("BR");
		expected = "Location : Sao Paulo,BR";
		actual = this.helper.getText(location);
		assertEquals(expected, actual);					// Location

		expected = "Poster : User";
		actual = this.helper.getText(user);
		assertEquals(expected, actual);					// UsernetUser

	}

	/**
	 * Test hibernate data.
	 */
	@Ignore
	@Test
	public void testHibernateData() {
		fail("Not yet implemented");
	}

	/**
	 * Test run query.
	 */
	@Ignore
	@Test
	public void testRunQueryQuery() {
		fail("Not yet implemented");
	}

	/**
	 * Test run query.
	 */
	@Ignore
	@Test
	public void testRunQueryStringSession() {
		fail("Not yet implemented");
	}

	/**
	 * Test run sql query.
	 */
	@Ignore
	@Test
	public void testRunSQLQuery() {
		fail("Not yet implemented");
	}

	/**
	 * Test store newsgroups.
	 */
	@Test
	public void testStoreNewsgroupsListOfStringMessageSession() {
		Set<NewsGroup> newsgroupsList = new HashSet<>(); 
//		newsgroupsList.add(new NewsGroup);
		this.helper.storeNewsgroups(newsgroupsList, session);	
}

	/**
	 * Test store newsgroups.
	 */
	@Ignore
	@Test
	public void testStoreNewsgroupsSetOfNewsGroupSession() {
		NewsGroup expected = new NewsGroup();
		expected.setLastMessageTotal(1);
		expected.setFirstMessage(1);
		expected.setLastMessage(1);
		List<NewsGroup> actual = null;
		Query queryMock = mock(Query.class);
		when(this.session.getNamedQuery(Matchers.anyString())).thenReturn(queryMock);

		Set<NewsGroup> list = new HashSet<NewsGroup>(1);
		NewsGroup nntp = PowerMockito.mock(NewsGroup.class);
		PowerMockito.when(nntp.getName()).thenReturn("newsgroup");
		PowerMockito.when(nntp.getArticleCount()).thenReturn(1);
		PowerMockito.when(nntp.getFirstMessage()).thenReturn(1);
		PowerMockito.when(nntp.getLastMessage()).thenReturn(1);
		list.add(nntp);

		actual = this.helper.storeNewsgroups(list, this.session);
		assertTrue(expected.getLastMessageTotal() == actual.get(0).getLastMessageTotal());
		assertTrue(expected.getFirstMessage() == actual.get(0).getFirstMessage());
		assertTrue(expected.getLastMessage() == actual.get(0).getLastMessage());
		// TODO Method do not add anything in the list (LINE 764).
	}

	/**
	 * Test data (old).
	 */
	public void testData() throws Exception {
		Session session = null; // TODO mock this and add expected return
		String query = "SELECT  n.fullname, count(*) as total"
		        + " FROM newsgroup n, newsgroup_topic as nt, message m "
		        + " where nt.topic_id = m.topic_id " + " and n.newsgroup_id = nt.newsgroup_id "
		        + " group by n.fullname " + " order by total desc";

		try {
			this.helper.getListResults(query, NewsGroup.class, this.session);

		}
		catch (Exception e) {
			fail("ERROR" + e.getMessage());
			e.printStackTrace();
		}
		System.out.println("OK");
	}

}
