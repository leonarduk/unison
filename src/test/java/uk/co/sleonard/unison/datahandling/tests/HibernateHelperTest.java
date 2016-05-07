package uk.co.sleonard.unison.datahandling.tests;

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

import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.DAO.IpAddress;
import uk.co.sleonard.unison.datahandling.DAO.Location;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.ResultRow;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.gui.UNISoNController;
import uk.co.sleonard.unison.gui.UNISoNException;
import uk.co.sleonard.unison.input.NNTPNewsGroup;

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
	@Test
	public void testCreateLocation() {
		/*Address http://api.hostip.info/rough.php?ip= with problem.
		 *TODO Search other.
		 */
	}

	/**
	 * Test fetch all.
	 */
	@Test
	public void testFetchAll() {
		String queryExpected = Messages.getString("0"); //$NON-NLS-1$
		Vector<Location> expected = new Vector<>();
		expected.add(new Location());
		Query query = mock(Query.class);
		
		when(this.session.createQuery(queryExpected)).thenReturn(query);
		when(this.helper.runQuery(query, Location.class)).thenReturn(expected);
		try{
			List<Location> actual = this.helper.fetchAll(Location.class, this.session);
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
		String queryExpected = Messages.getString("1"); //$NON-NLS-1$
		Vector<NewsGroup> expected = new Vector<>();
		expected.addElement(new NewsGroup());
		Query query = mock(Query.class);
		when(this.session.createQuery(queryExpected)).thenReturn(query);
		when(this.helper.runQuery(query, NewsGroup.class)).thenReturn(expected);
		try{
			List<NewsGroup> actual = this.helper.fetchBaseNewsGroups(this.session);
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
	@SuppressWarnings("unchecked")
	@Test
	public void testFindByKeyIntSessionClassOfQ() {
		Message expected = new Message();
		String query = Messages.getString("2") + Messages.getString("3"); //$NON-NLS-1$ //$NON-NLS-2$
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
	@SuppressWarnings("unchecked")
	@Test
	public void testFindByKeyStringSessionClassOfQ() {
		Message expected = new Message();
		String query = Messages.getString("4") + Messages.getString("5"); //$NON-NLS-1$ //$NON-NLS-2$
		Query queryMock = mock(Query.class);
		
		when(this.session.getNamedQuery(query)).thenReturn(queryMock);
		when(queryMock.uniqueResult()).thenReturn(expected);
		try{
			Message actual = (Message) this.helper.findByKey(Messages.getString("6"), this.session, Message.class); //$NON-NLS-1$
			assertEquals(expected, actual);
			actual = null;
			try{
				when(queryMock.uniqueResult()).thenThrow(NonUniqueResultException.class);
				actual = (Message) this.helper.findByKey(Messages.getString("7"), this.session, Message.class);		//If throw Exception  //$NON-NLS-1$
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
	@Test
	public void testFindOrCreateNewsGroup() {
		NewsGroup expected = new NewsGroup(Messages.getString("8")); //$NON-NLS-1$
		expected.setFullName(Messages.getString("9")); //$NON-NLS-1$
		NewsGroup actual = null;
		Query query = mock(Query.class);
		
		when(this.session.getNamedQuery(Matchers.anyString())).thenReturn(query);
		actual = this.helper.findOrCreateNewsGroup(this.session, Messages.getString("10"));	//If the search no locate data on DB //$NON-NLS-1$
		assertEquals(expected, actual);
		when(query.uniqueResult()).thenReturn(expected);
		actual = this.helper.findOrCreateNewsGroup(this.session, Messages.getString("11"));	//If the search locate data on DB. //$NON-NLS-1$
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
		String expected = Messages.getString("12") + Messages.getString("13") + Messages.getString("14"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String actual = this.helper.getGoogleMapURL(Messages.getString("15")); //$NON-NLS-1$
		assertEquals(expected, actual);			//If city is Houston
		expected = Messages.getString("16"); //$NON-NLS-1$
		actual = this.helper.getGoogleMapURL(Messages.getString("17")); //$NON-NLS-1$
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
	@Test
	public void testGetListResults() {
		Vector<ResultRow> actual = null;
		Vector<ResultRow> expected = new Vector<>();
		expected.addElement(new ResultRow(Messages.getString("18"), 1, Topic.class)); //$NON-NLS-1$
		Vector<String[]> listTopic = new Vector<>(1);
		listTopic.addElement(new String[] {new String(Messages.getString("19")), new String(Messages.getString("20"))}); //$NON-NLS-1$ //$NON-NLS-2$
		String query = Messages.getString("21"); //$NON-NLS-1$
		
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
		NewsGroup expected = new NewsGroup(Messages.getString("22")); //$NON-NLS-1$
		String expectedQuery = new String(Messages.getString("23") //$NON-NLS-1$
										+ Messages.getString("24")); //$NON-NLS-1$
		Query queryMock = mock(Query.class);
		
		when(this.session.createQuery(expectedQuery)).thenReturn(queryMock);
		when(queryMock.setString(anyInt(), anyString())).thenReturn(queryMock);
		when(queryMock.uniqueResult()).thenReturn(expected);
		try{
			this.helper.getNewsgroupByFullName(Messages.getString("25"), this.session);		 //$NON-NLS-1$
		}catch (Exception e){
			fail(Messages.getString("26") + e); //$NON-NLS-1$
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
		UsenetUser user = new UsenetUser(Messages.getString("27"), Messages.getString("28"), Messages.getString("29")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		message.setPoster(user);
		expected = Messages.getString("30"); //$NON-NLS-1$
		actual = this.helper.getText(message);	
		assertEquals(expected, actual);					//Message
		
		NewsGroup newsGroup = new NewsGroup(Messages.getString("31")); //$NON-NLS-1$
		expected = Messages.getString("32"); //$NON-NLS-1$
		actual = this.helper.getText(newsGroup);
		assertEquals(expected, actual);					//News
		newsGroup.setLastMessageTotal(10);
		expected = Messages.getString("33"); //$NON-NLS-1$
		actual = this.helper.getText(newsGroup);
		assertEquals(expected, actual);					//News with Last Message Total.
		
		Location location = new Location();
		location.setCity(Messages.getString("34")); //$NON-NLS-1$
		location.setCountryCode(Messages.getString("35")); //$NON-NLS-1$
		expected = Messages.getString("36"); //$NON-NLS-1$
		actual = this.helper.getText(location);
		assertEquals(expected, actual);					//Location
		
		expected = Messages.getString("37"); //$NON-NLS-1$
		actual = this.helper.getText(user);
		assertEquals(expected, actual);					//UsernetUser
		
	}

	/**
	 * Test hibernate data.
	 */
	@Ignore
	@Test
	public void testHibernateData() {
		fail(Messages.getString("38")); //$NON-NLS-1$
	}

	/**
	 * Test run query.
	 */
	@Ignore
	@Test
	public void testRunQueryQuery() {
		fail(Messages.getString("39")); //$NON-NLS-1$
	}

	/**
	 * Test run query.
	 */
	@Ignore
	@Test
	public void testRunQueryStringSession() {
		fail(Messages.getString("40")); //$NON-NLS-1$
	}

	/**
	 * Test run sql query.
	 */
	@Ignore
	@Test
	public void testRunSQLQuery() {
		fail(Messages.getString("41")); //$NON-NLS-1$
	}

	/**
	 * Test store newsgroups.
	 */
	@Ignore
	@Test
	public void testStoreNewsgroupsListOfStringMessageSession() {
		fail(Messages.getString("42")); //$NON-NLS-1$
	}

	/**
	 * Test store newsgroups.
	 */
	@Ignore
	@Test
	public void testStoreNewsgroupsSetOfNNTPNewsGroupSession() {
		
		NewsGroup expected = new NewsGroup();
		expected.setLastMessageTotal(1);
		expected.setFirstMessage(1);
		expected.setLastMessage(1);
		List<NewsGroup> actual = null;
		Query queryMock = mock(Query.class);
		when(this.session.getNamedQuery(Matchers.anyString())).thenReturn(queryMock);
		
		Set<NNTPNewsGroup> list = new HashSet<NNTPNewsGroup>(1);
		NNTPNewsGroup nntp = PowerMockito.mock(NNTPNewsGroup.class);
		PowerMockito.when(nntp.getNewsgroup()).thenReturn(Messages.getString("43")); //$NON-NLS-1$
		PowerMockito.when(nntp.getArticleCount()).thenReturn(1);
		PowerMockito.when(nntp.getFirstArticle()).thenReturn(1);
		PowerMockito.when(nntp.getLastArticle()).thenReturn(1);
		list.add(nntp);
		
		actual = this.helper.storeNewsgroups(list, this.session);
		assertTrue(expected.getLastMessageTotal() == actual.get(0).getLastMessageTotal());
		assertTrue(expected.getFirstMessage() == actual.get(0).getFirstMessage());
		assertTrue(expected.getLastMessage() == actual.get(0).getLastMessage());
		//TODO Method do not add anything in the list (LINE 764).  
	}
	
	/**
	 * Test data (old).
	 */
	public void testData() throws Exception {
		Session session = null; //TODO mock this and add expected return
		String query = Messages.getString("44") //$NON-NLS-1$
				+ Messages.getString("45") //$NON-NLS-1$
				+ Messages.getString("46") //$NON-NLS-1$
				+ Messages.getString("47") //$NON-NLS-1$
				+ Messages.getString("48") + Messages.getString("49"); //$NON-NLS-1$ //$NON-NLS-2$

		try {
			this.helper.getListResults(query, NewsGroup.class, this.session);

		} catch (Exception e) {
			fail(Messages.getString("50") + e.getMessage()); //$NON-NLS-1$
			e.printStackTrace();
		}
		System.out.println(Messages.getString("51")); //$NON-NLS-1$
	}


}
