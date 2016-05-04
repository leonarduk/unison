package uk.co.sleonard.unison.datahandling.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Matchers;

import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.DAO.IpAddress;
import uk.co.sleonard.unison.datahandling.DAO.Location;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.gui.UNISoNController;
import uk.co.sleonard.unison.gui.UNISoNException;

/**
 * The Class HibernateHelperTest.
 */
public class HibernateHelperTest {

	private HibernateHelper helper;
	private Session session;

	@Before
	public void setUp() throws Exception {
		UNISoNController uniController = mock(UNISoNController.class);
		this.helper = new HibernateHelper(uniController);
		this.session = mock(Session.class);
	}
	/**
	 * Test create location.
	 */
	@Ignore
	@Test
	public void testCreateLocation() {
		fail("Not yet implemented");
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
		try{
			List<Location> actual = (List<Location>) this.helper.fetchAll(Location.class, this.session);
			assertEquals(expected.size(), actual.size());
		}catch (NullPointerException e){
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
		try{
			List<NewsGroup> actual = (List<NewsGroup>) this.helper.fetchBaseNewsGroups(this.session);
			assertEquals(expected.size(), actual.size());
		}catch (NullPointerException e){
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
		try{
			verify(this.session).saveOrUpdate(Matchers.any());		//Verify if method was called (saveOrUpdate)
		}catch (Exception e){
			fail(e.getMessage());
		}
		list.add(expected);list.add(expected);
		assertSame(expected, actual);
	}

	/**
	 * Test to find by key.
	 */
	@Test
	public void testFindByKeyIntSessionClassOfQ() {
		Message expected = new Message();
		String query = "uk.co.sleonard.unison.datahandling.DAO.Message" + ".findByKey";
		Query queryMock = mock(Query.class);
		
		when(this.session.getNamedQuery(query)).thenReturn(queryMock);
		when(queryMock.uniqueResult()).thenReturn(expected);
		try{
			Message actual = (Message) this.helper.findByKey(1, this.session, Message.class);
			assertEquals(expected, actual);
			actual = null;
			try{
				when(queryMock.uniqueResult()).thenThrow(NonUniqueResultException.class);
				actual = (Message) this.helper.findByKey(1, this.session, Message.class);		//If throw Exception 
			}catch (RuntimeException e){
				assertNull(actual);
			}
			
		}catch (Exception e){
			fail(e.getMessage());
		}
	}

	/**
	 * Test to find by key.
	 */
	@Test
	public void testFindByKeyStringSessionClassOfQ() {
		Message expected = new Message();
		String query = "uk.co.sleonard.unison.datahandling.DAO.Message" + ".findByKey";
		Query queryMock = mock(Query.class);
		
		when(this.session.getNamedQuery(query)).thenReturn(queryMock);
		when(queryMock.uniqueResult()).thenReturn(expected);
		try{
			Message actual = (Message) this.helper.findByKey("key", this.session, Message.class);
			assertEquals(expected, actual);
			actual = null;
			try{
				when(queryMock.uniqueResult()).thenThrow(NonUniqueResultException.class);
				actual = (Message) this.helper.findByKey("key", this.session, Message.class);		//If throw Exception 
			}catch (RuntimeException e){
				assertNull(actual);
			}
			
		}catch (Exception e){
			fail(e.getMessage());
		}
	}

	/**
	 * Test to find or create newsgroup.
	 */
	@Ignore
	@Test
	public void testFindOrCreateNewsGroup() {
		fail("Not yet implemented");
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
		String expected = "http://maps.google.com/maps?f=q&hl=en&geocode=&q=" + "Houston" + "&ie=UTF8&z=12&om=1";
		String actual = this.helper.getGoogleMapURL("Houston");
		assertEquals(expected, actual);			//If city is Houston
		expected = "UNKNOWN LOCATION";
		actual = this.helper.getGoogleMapURL("(Private Address)");
		assertEquals(expected, actual);			//If Private Address
	}

	/**
	 * Test get hibernate session.
	 */
	@Test
	public void testGetHibernateSession() {
		try {
			Session session = this.helper.getHibernateSession();
			assertNotNull(session);
		} catch (UNISoNException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test get list results.
	 */
	@Ignore
	@Test
	public void testGetListResults() {
		fail("Not yet implemented");
	}

	/**
	 * Test get newsgroup by fullname.
	 */
	@Ignore
	@Test
	public void testGetNewsgroupByFullName() {
		fail("Not yet implemented");
	}

	/**
	 * Test get text.
	 */
	@Ignore
	@Test
	public void testGetText() {
		fail("Not yet implemented");
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
	@Ignore
	@Test
	public void testStoreNewsgroupsListOfStringMessageSession() {
		fail("Not yet implemented");
	}

	/**
	 * Test store newsgroups.
	 */
	@Ignore
	@Test
	public void testStoreNewsgroupsSetOfNNTPNewsGroupSession() {
		fail("Not yet implemented");
	}
	
	/**
	 * Test data (old).
	 */
	public void testData() throws Exception {
		Session session = null; //TODO mock this and add expected return
		String query = "SELECT  n.fullname, count(*) as total"
				+ " FROM newsgroup n, newsgroup_topic as nt, message m "
				+ " where nt.topic_id = m.topic_id "
				+ " and n.newsgroup_id = nt.newsgroup_id "
				+ " group by n.fullname " + " order by total desc";

		try {
			this.helper.getListResults(query, NewsGroup.class, session);

		} catch (Exception e) {
			fail("ERROR" + e.getMessage());
			e.printStackTrace();
		}
		System.out.println("OK");
	}


}
