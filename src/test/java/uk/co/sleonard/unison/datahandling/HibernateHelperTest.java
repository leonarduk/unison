//package uk.co.sleonard.unison.datahandling;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.fail;
//import static org.mockito.Matchers.anyInt;
//import static org.mockito.Matchers.anyString;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import java.sql.BatchUpdateException;
//import java.sql.SQLException;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.Vector;
//
//import org.hibernate.NonUniqueResultException;
//import org.hibernate.Query;
//import org.hibernate.SQLQuery;
//import org.hibernate.Session;
//import org.hibernate.Transaction;import org.hibernate.exception.GenericJDBCException;
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.mockito.Matchers;
//import org.mockito.Mockito;
//import org.powermock.api.mockito.PowerMockito;
//
//import uk.co.sleonard.unison.UNISoNException;
//import uk.co.sleonard.unison.datahandling.DAO.Location;
//import uk.co.sleonard.unison.datahandling.DAO.Message;
//import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
//import uk.co.sleonard.unison.datahandling.DAO.ResultRow;
//import uk.co.sleonard.unison.datahandling.DAO.Topic;
//import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
//import uk.co.sleonard.unison.input.NewsArticle;
///**
// * The Class HibernateHelperTest.
// *
// * @author Stephen <github@leonarduk.com>
// * @since v1.0.0
// *
// */
//public class HibernateHelperTest {
//
//	private HibernateHelper	helper;
//	private Session			session;
//
//	/**
//	 * Setup.
//	 */
//	@Before
//	public void setUp() throws Exception {
//		this.helper = new HibernateHelper(null);
//		this.session = mock(Session.class);
//		when(this.session.beginTransaction()).thenReturn(mock(Transaction.class));
//		when(this.session.getNamedQuery(Matchers.anyString())).thenReturn(mock(Query.class));
//	}
//
//	/**
//	 * Test create location.
//	 */
//	@Test
//	public void testCreateLocation() {
//		/*
//		 * Address http://api.hostip.info/rough.php?ip= with problem. TODO Search other.
//		 */
//	}
//
//
//
//	/**
//	 * Test to find by key.
//	 */
//	@SuppressWarnings("unchecked")
//	@Test
//	public void testFindByKeyStringSessionClassOfQ() {
//		Message expected = new Message();
//		String query = "uk.co.sleonard.unison.datahandling.DAO.Message" + ".findByKey";
//		Query queryMock = mock(Query.class);
//
//		when(this.session.getNamedQuery(query)).thenReturn(queryMock);
//		when(queryMock.uniqueResult()).thenReturn(expected);
//		try {
//			Message actual = (Message) this.helper.findByKey("key", this.session, Message.class);
//			assertEquals(expected, actual);
//			actual = null;
//			try {
//				when(queryMock.uniqueResult()).thenThrow(NonUniqueResultException.class);
//				actual = (Message) this.helper.findByKey("key", this.session, Message.class);		// If
//				                                                                             		// throw
//				                                                                             		// Exception
//			}
//			catch (RuntimeException e) {
//				assertNull(actual);
//			}
//
//		}
//		catch (Exception e) {
//			fail(e.getMessage());
//		}
//	}
//
//	/**
//	 * Test to find or create newsgroup.
//	 */
//	@Test
//	public void testFindOrCreateNewsGroup() {
//		NewsGroup expected = new NewsGroup("alt.news", null, null, null, 1,
//		        2, 1, 2, "alt.news", true);
//
//		expected.setFullName("alt.pl.allin");
//		NewsGroup actual = null;
//		Query query = mock(Query.class);
//
//		when(this.session.getNamedQuery(Matchers.anyString())).thenReturn(query);
//		actual = this.helper.findOrCreateNewsGroup(this.session, "alt.pl.allin");	// If the search
//		                                                                         	// no locate
//		                                                                         	// data on DB
//		assertEquals(expected, actual);
//		when(query.uniqueResult()).thenReturn(expected);
//		actual = this.helper.findOrCreateNewsGroup(this.session, "alt.pl.allin");	// If the search
//		                                                                         	// locate data
//		                                                                         	// on DB.
//		assertEquals(expected, actual);
//	}
//
//	/**
//	 * Test to generate schema.
//	 */
//	@Test
//	public void testGenerateSchema() {
//		this.helper.generateSchema();
//	}
//
//
//	/**
//	 * Test get hibernate session.
//	 */
//	@Test
//	public void testGetHibernateSession() {
//		try {
//			Session session = this.helper.getHibernateSession();
//			assertNotNull(session);
//		}
//		catch (UNISoNException e) {
//			fail(e.getMessage());
//		}
//	}
//
//	/**
//	 * Test get list results.
//	 */
//	@Test
//	public void testGetListResults() {
//		Vector<ResultRow> actual = null;
//		Vector<ResultRow> expected = new Vector<>();
//		expected.addElement(new ResultRow("topic", 1, Topic.class));
//		Vector<String[]> listTopic = new Vector<>(1);
//		listTopic.addElement(new String[] { new String("topic"), new String("1") });
//		String query = "select topic from Topic";
//
//		SQLQuery queryMock = mock(SQLQuery.class);
//		when(this.session.createSQLQuery(query)).thenReturn(queryMock);
//		when(queryMock.list()).thenReturn(listTopic);
//		actual = this.helper.getListResults(query, Topic.class, this.session);
//		assertEquals(expected.get(0), actual.get(0));
//	}
//
//	/**
//	 * Test get newsgroup by fullname.
//	 */
//	@Test
//	public void testGetNewsgroupByFullName() {
//		NewsGroup expected = new NewsGroup("alt.news", null, null, null, 1,
//		        2, 1, 2, "alt.news", true);
//		String expectedQuery = new String(
//		        "from uk.co.sleonard.unison.datahandling.DAO.NewsGroup" + " where fullname=?");
//		Query queryMock = mock(Query.class);
//
//		when(this.session.createQuery(expectedQuery)).thenReturn(queryMock);
//		when(queryMock.setString(anyInt(), anyString())).thenReturn(queryMock);
//		when(queryMock.uniqueResult()).thenReturn(expected);
//		try {
//			this.helper.getNewsgroupByFullName("newsgroup", this.session);
//		}
//		catch (Exception e) {
//			fail("ERROR: " + e);
//		}
//	}
//
//	/**
//	 * Test get text.
//	 */
//	@Test
//	public void testGetText() {
//		String expected = null;
//		String actual = null;
//
//		Message message = new Message();
//		UsenetUser user = new UsenetUser("User", "my@email.com", "private", actual, null);
//		message.setPoster(user);
//		expected = "From:User(my@email.com)";
//		actual = this.helper.getText(message);
//		assertEquals(expected, actual);					// Message
//
//		NewsGroup newsGroup = new NewsGroup("alt.news", null, null, null, 1,
//		        2, 1, 2, "alt.news", true);
//		expected = "alt.news (2)";
//		actual = this.helper.getText(newsGroup);
//		assertEquals(expected, actual);					// News
//		newsGroup.setLastMessageTotal(10);
//		expected = "alt.news (10)";
//		actual = this.helper.getText(newsGroup);
//		assertEquals(expected, actual);					// News with Last Message Total.
//
//		Location location = new Location();
//		location.setCity("Sao Paulo");
//		location.setCountryCode("BR");
//		expected = "Location : Sao Paulo,BR";
//		actual = this.helper.getText(location);
//		assertEquals(expected, actual);					// Location
//
//		expected = "Poster : User";
//		actual = this.helper.getText(user);
//		assertEquals(expected, actual);					// UsernetUser
//
//	}
//
//	/**
//	 * Test hibernate data.
//	 * @throws UNISoNException
//	 */
//	@Test
//	public void testHibernateData() throws UNISoNException {
//String articleID = "124A";
//int articleNumber = 4567;
//Date date = new Date();
//String from = "test@email.com";
//String subject = "Interesting chat";
//String references = "";
//String content = "This is interesting";
//String newsgroups = "alt.interesting";
//String postingHost = "testserver";
//NewsArticle article = new NewsArticle(articleID, articleNumber, date, from, subject, references, content, newsgroups, postingHost);
//this.helper.hibernateData(article, session);	}
//
//	@Test
//	public void testHibernateDataGenericJDBCException() throws UNISoNException {
//String articleID = "124A";
//int articleNumber = 4567;
//Date date = new Date();
//String from = "test@email.com";
//String subject = "Interesting chat";
//String references = "";
//String content = "This is interesting";
//String newsgroups = "alt.interesting";
//String postingHost = "testserver";
//GenericJDBCException genericJDBCException = new GenericJDBCException("test", new BatchUpdateException(new int[]{1,2},new SQLException()), "select");
//Mockito.when(this.session.beginTransaction()).thenThrow(genericJDBCException);
//NewsArticle article = new NewsArticle(articleID, articleNumber, date, from, subject, references, content, newsgroups, postingHost);
//this.helper.hibernateData(article, session);	}
//
//
//
//	/**
//	 * Test store newsgroups.
//	 */
//	@Test
//	public void testStoreNewsgroupsListOfStringMessageSession() {
//		Set<NewsGroup> newsgroupsList = new HashSet<>();
////		newsgroupsList.add(new NewsGroup);
//		this.helper.storeNewsgroups(newsgroupsList, session);
//}
//
//	/**
//	 * Test store newsgroups.
//	 */
//	@Ignore
//	@Test
//	public void testStoreNewsgroupsSetOfNewsGroupSession() {
//		NewsGroup expected = new NewsGroup();
//		expected.setLastMessageTotal(1);
//		expected.setFirstMessage(1);
//		expected.setLastMessage(1);
//		List<NewsGroup> actual = null;
//		Query queryMock = mock(Query.class);
//		when(this.session.getNamedQuery(Matchers.anyString())).thenReturn(queryMock);
//
//		Set<NewsGroup> list = new HashSet<NewsGroup>(1);
//		NewsGroup nntp = PowerMockito.mock(NewsGroup.class);
//		PowerMockito.when(nntp.getName()).thenReturn("newsgroup");
//		PowerMockito.when(nntp.getArticleCount()).thenReturn(1);
//		PowerMockito.when(nntp.getFirstMessage()).thenReturn(1);
//		PowerMockito.when(nntp.getLastMessage()).thenReturn(1);
//		list.add(nntp);
//
//		actual = this.helper.storeNewsgroups(list, this.session);
//		assertTrue(expected.getLastMessageTotal() == actual.get(0).getLastMessageTotal());
//		assertTrue(expected.getFirstMessage() == actual.get(0).getFirstMessage());
//		assertTrue(expected.getLastMessage() == actual.get(0).getLastMessage());
//		// TODO Method do not add anything in the list (LINE 764).
//	}
//
//	/**
//	 * Test data (old).
//	 */
//	public void testData() throws Exception {
//		String query = "SELECT  n.fullname, count(*) as total"
//		        + " FROM newsgroup n, newsgroup_topic as nt, message m "
//		        + " where nt.topic_id = m.topic_id " + " and n.newsgroup_id = nt.newsgroup_id "
//		        + " group by n.fullname " + " order by total desc";
//
//		try {
//			this.helper.getListResults(query, NewsGroup.class, this.session);
//
//		}
//		catch (Exception e) {
//			fail("ERROR" + e.getMessage());
//			e.printStackTrace();
//		}
//		System.out.println("OK");
//	}
//
//}
