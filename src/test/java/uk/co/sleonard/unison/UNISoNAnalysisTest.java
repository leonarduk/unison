/**
 * UNISoNAnalysisTest
 *
 * @author ${author}
 * @since 17-Jun-2016
 */
package uk.co.sleonard.unison;

import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import uk.co.sleonard.unison.datahandling.DAO.*;
import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.gui.UNISoNGUI;

import java.util.*;

public class UNISoNAnalysisTest {

    private NewsGroupFilter filter;
    private Session session;
    private HibernateHelper helper;
    private UNISoNAnalysis analysis;
    private UsenetUser poster;
    private Topic topic;
    private NewsGroup newsgroup;

    @BeforeEach
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
        Assertions.assertEquals(1, results.size());
        Assertions.assertEquals("UK", results.get(0).key());
        Assertions.assertEquals(2, results.get(0).count());
      
    }

    @Test
    public final void testGetTopGroupsList() {
        final List<ResultRow> results = this.analysis.getTopGroupsList();

      Assertions.assertEquals(1, results.size());
        Assertions.assertEquals(this.newsgroup, results.get(0).key());
        Assertions.assertEquals(2, results.get(0).count());

    }

    @Test
    public final void testGetTopPosters() {
        final Vector<ResultRow> results = this.analysis.getTopPosters();

      Assertions.assertEquals(1, results.size());
        Assertions.assertEquals(this.poster, results.get(0).key());
        Assertions.assertEquals(2, results.get(0).count());

    }

}
