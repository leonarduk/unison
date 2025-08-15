/**
 * UNISoNAnalysisTest
 *
 * @author ${author}
 * @since 17-Jun-2016
 */
package uk.co.sleonard.unison;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.DAO.GUIItem;
import uk.co.sleonard.unison.datahandling.DAO.Location;
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
	private Topic                   topic;
        private NewsGroup               newsgroup;

	@Before
	public void setUp() throws Exception {
                this.session = Mockito.mock(Session.class);
                final UNISoNGUI gui = Mockito.mock(UNISoNGUI.class);
                this.helper = Mockito.mock(HibernateHelper.class);
                this.filter = Mockito.mock(NewsGroupFilter.class);
                this.analysis = new UNISoNAnalysis(this.filter, this.session, this.helper);

                final Vector<Message> messages = new Vector<>();
                final Date dateCreated = new Date();
                final String subject = "Test";
                final byte[] messageBody = {};
                this.newsgroup = new NewsGroup();
                this.newsgroup.setFullName("comp.lang.java");
                this.newsgroup.setId(1);
                final Set<NewsGroup> newsgroups = new HashSet<>();
                newsgroups.add(this.newsgroup);
                final Location location = new Location("London", "UK", "UK", false, new ArrayList<>(), new ArrayList<>());
                this.poster = UsenetUser.builder().name("Poster").email("poster@example.com").ipaddress("1.2.3.4")
                        .gender("M").location(location).id(1).build();
                this.topic = new Topic("topic", new HashSet<>());
                messages.addElement(new Message(dateCreated, "123", subject, this.poster, this.topic,
                        newsgroups, null, messageBody));
                messages.addElement(new Message(dateCreated, "124", subject, this.poster, this.topic,
                        newsgroups, null, messageBody));

                Mockito.when(this.filter.getMessagesFilter()).thenReturn(messages);

	}

        @Test
        public final void testGetTopCountriesList() {
                final List<ResultRow> results = this.analysis.getTopCountriesList();
                Assert.assertEquals(1, results.size());
                Assert.assertEquals("UK", results.get(0).getKey());
                Assert.assertEquals(2, results.get(0).getCount());
        }

        @Test
        public final void testGetTopGroupsList() {
                final List<ResultRow> results = this.analysis.getTopGroupsList();
                Assert.assertEquals(1, results.size());
                Assert.assertEquals(this.newsgroup, results.get(0).getKey());
                Assert.assertEquals(2, results.get(0).getCount());
        }

//        @Test
//        public final void testGetTopGroupsVector() {
//                final SQLQuery query = Mockito.mock(SQLQuery.class);
//                Mockito.when(this.session.createSQLQuery(ArgumentMatchers.anyString())).thenReturn(query);
//                Mockito.when(query.list()).thenReturn(new ArrayList<>());
//                final Vector<Vector<Object>> table = this.analysis.getTopGroupsVector();
//                Assert.assertTrue(table.isEmpty());
//        }

//        @Test
//        public final void testGetTopGroupsVectorResults() {
//                final SQLQuery query = Mockito.mock(SQLQuery.class);
//                final List<Object[]> value = new ArrayList<>();
//                value.add(new Object[] { Integer.valueOf(2), Integer.valueOf(this.newsgroup.getId()) });
//                Mockito.when(query.list()).thenReturn(value);
//                final Vector<NewsGroup> posters = new Vector<>();
//                posters.add(this.newsgroup);
//                Mockito.when(this.helper.runQuery(ArgumentMatchers.anyString(), ArgumentMatchers.any(Session.class),
//                        ArgumentMatchers.any(Class.class))).thenReturn(posters);
//                Mockito.when(this.session.createSQLQuery(ArgumentMatchers.anyString())).thenReturn(query);
//                final Vector<Vector<Object>> table = this.analysis.getTopGroupsVector();
//                Assert.assertEquals(1, table.size());
//                final Vector<Object> row = table.get(0);
//                Assert.assertEquals("2", row.get(1));
//                final GUIItem<?> item = (GUIItem<?>) row.get(0);
//                Assert.assertEquals(this.newsgroup, item.getObject());
//        }

        @Test
        public final void testGetTopPosters() {
                final Vector<ResultRow> results = this.analysis.getTopPosters();
                Assert.assertEquals(1, results.size());
                Assert.assertEquals(this.poster, results.get(0).getKey());
                Assert.assertEquals(2, results.get(0).getCount());
        }

}
