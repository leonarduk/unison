/**
 * UNISoNAnalysisTest
 *
 * @author ${author}
 * @since 17-Jun-2016
 */
package uk.co.sleonard.unison;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.ResultRow;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.gui.UNISoNGUI;

public class UNISoNAnalysisTest {

	private NewsGroupFilter	filter;
	private Session			session;
	private HibernateHelper	helper;
	private UNISoNAnalysis	analysis;
	private UsenetUser		poster;
	private Topic			topic;

	@Before
	public void setUp() throws Exception {
		this.session = Mockito.mock(Session.class);
		final UNISoNGUI gui = Mockito.mock(UNISoNGUI.class);
		this.helper = new HibernateHelper(gui);
		this.filter = Mockito.mock(NewsGroupFilter.class);
		this.analysis = new UNISoNAnalysis(this.filter, this.session, this.helper);

		final Vector<Message> messages = new Vector<>();
		final Date DateCreated = new Date();
		final String Subject = "Test";
		final byte[] MessageBody = {};
		final Set<NewsGroup> newsgroups = new HashSet<>();
		messages.addElement(new Message(DateCreated, "123", Subject, this.poster, this.topic,
		        newsgroups, null, MessageBody));
		messages.addElement(new Message(DateCreated, "124", Subject, this.poster, this.topic,
		        newsgroups, null, MessageBody));

		Mockito.when(this.filter.getMessagesFilter()).thenReturn(messages);

	}

	@Test
	public final void testGetTopCountriesList() {
		this.analysis.getTopCountriesList();
	}

	@Test
	public final void testGetTopGroupsList() {
		this.analysis.getTopGroupsList();
	}

	@Test
	public final void testGetTopGroupsVector() {
		final SQLQuery query = Mockito.mock(SQLQuery.class);
		Mockito.when(this.session.createSQLQuery(Matchers.anyString())).thenReturn(query);
		this.analysis.getTopGroupsVector();
	}

	@Test
	public final void testGetTopPosters() {
		final Vector<ResultRow> results = this.analysis.getTopPosters();
		System.out.println(results);
	}

}
