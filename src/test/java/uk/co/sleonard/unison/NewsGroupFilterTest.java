/**
 * NewsGroupFilterTest
 *
 * @author ${author}
 * @since 23-Jun-2016
 */
package uk.co.sleonard.unison;

import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.datahandling.HibernateHelper;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class NewsGroupFilterTest {

    private NewsGroupFilter newsGroupFilter;
    private HibernateHelper helper;
    private Session session;

    @BeforeEach
    public void setUp() throws Exception {
        this.helper = Mockito.mock(HibernateHelper.class);
        this.session = Mockito.mock(Session.class);
        this.newsGroupFilter = new NewsGroupFilter(this.session, this.helper);
    }

    @Test
    public final void testCountriesFilter() {
        Assertions.assertEquals(0, this.newsGroupFilter.getCountriesFilter().size());
        final Set<String> countriesFilter = new HashSet<>();
        countriesFilter.add("UK");
        this.newsGroupFilter.setCountriesFilter(countriesFilter);
        Assertions.assertEquals(1, this.newsGroupFilter.getCountriesFilter().size());
        this.newsGroupFilter.clear();
        Assertions.assertEquals(0, this.newsGroupFilter.getCountriesFilter().size());
    }

    @Test
    public final void testDates() {
        final Date fromDate2 = new Date();
        final Date toDate2 = new Date();
        Assertions.assertNull(this.newsGroupFilter.getFromDate());
        Assertions.assertNull(this.newsGroupFilter.getToDate());
        this.newsGroupFilter.setDates(fromDate2, toDate2);
        this.newsGroupFilter.setFromDate(fromDate2);
        Assertions.assertEquals(fromDate2, this.newsGroupFilter.getFromDate());

        this.newsGroupFilter.setToDate(toDate2);
        Assertions.assertEquals(toDate2, this.newsGroupFilter.getToDate());

    }

    @Test
    public final void testFiltered() {
        Assertions.assertFalse(this.newsGroupFilter.isFiltered());
        this.newsGroupFilter.setFiltered(true);
        Assertions.assertTrue(this.newsGroupFilter.isFiltered());
    }

    @Test
    public final void testMessage() {
        Assertions.assertNull(this.newsGroupFilter.getMessage());
        final Message expected = new Message();
        expected.setSubject("test");
        this.newsGroupFilter.setMessage(expected);
        Assertions.assertEquals(expected, this.newsGroupFilter.getMessage());
    }

    @Test
    public final void testMessagesFilter() {
        Assertions.assertEquals(0, this.newsGroupFilter.getMessagesFilter().size());
        final Vector<Message> filter = new Vector<>();
        filter.add(Mockito.mock(Message.class));
        this.newsGroupFilter.setMessagesFilter(filter);
        Assertions.assertEquals(1, this.newsGroupFilter.getMessagesFilter().size());
        this.newsGroupFilter.clear();
        Assertions.assertEquals(0, this.newsGroupFilter.getMessagesFilter().size());
    }

    @Test
    public final void testNewsgroupFilter() {
        Assertions.assertEquals(0, this.newsGroupFilter.getNewsgroupFilter().size());
        final Set<NewsGroup> filter = new HashSet<>();
        filter.add(Mockito.mock(NewsGroup.class));
        this.newsGroupFilter.setNewsgroupFilter(filter);
        Assertions.assertEquals(1, this.newsGroupFilter.getNewsgroupFilter().size());
        this.newsGroupFilter.clear();
        Assertions.assertEquals(0, this.newsGroupFilter.getNewsgroupFilter().size());
    }

    @Test
    public final void testSelectedCountries() {
        this.newsGroupFilter.setFiltered(true);
        Assertions.assertEquals(0, this.newsGroupFilter.getSelectedCountries().size());
        final Set<String> topsNewsgroups = new HashSet<>();
        topsNewsgroups.add("UK");
        this.newsGroupFilter.setSelectedCountries(topsNewsgroups);
        Assertions.assertEquals(1, this.newsGroupFilter.getSelectedCountries().size());
        this.newsGroupFilter.clear();
        Assertions.assertEquals(0, this.newsGroupFilter.getSelectedCountries().size());

    }

    @Test
    public final void testSelectedMessage() {
        this.newsGroupFilter.setFiltered(false);
        Assertions.assertNull(this.newsGroupFilter.getSelectedMessage());
        final Message expected = new Message();
        expected.setSubject("test");
        this.newsGroupFilter.setMessage(expected);
        Assertions.assertEquals(expected, this.newsGroupFilter.getSelectedMessage());
    }

    @Test
    public final void testSelectedMessageFiltered() {
        this.newsGroupFilter.setFiltered(true);
        Assertions.assertNull(this.newsGroupFilter.getSelectedMessage());
    }

    @Test
    public final void testSelectedMessages() {
        this.newsGroupFilter.setFiltered(true);
        Assertions.assertEquals(0, this.newsGroupFilter.getSelectedMessages().size());
        final Vector<Message> topsNewsgroups = new Vector<>();
        topsNewsgroups.add(Mockito.mock(Message.class));
        this.newsGroupFilter.setSelectedMessages(topsNewsgroups);
        Assertions.assertEquals(1, this.newsGroupFilter.getSelectedMessages().size());
        this.newsGroupFilter.clear();
        Assertions.assertEquals(0, this.newsGroupFilter.getSelectedMessages().size());

    }

//    @Test
//    public final void testSelectedNewsgroup() {
//        this.newsGroupFilter.setFiltered(false);
//        Assertions.assertNull(this.newsGroupFilter.getSelectedNewsgroup());
//        final NewsGroup expected = new NewsGroup();
//        final String fullName = "Test";
//        expected.setFullName(fullName);
//        Mockito.when(this.helper.getNewsgroupByFullName(fullName, this.session))
//                .thenReturn(expected);
//        this.newsGroupFilter.setSelectedNewsgroup(expected.getFullName());
//        Assertions.assertEquals(expected, this.newsGroupFilter.getSelectedNewsgroup());
//    }

    @Test
    public final void testSelectedNewsgroupFiltered() {
        this.newsGroupFilter.setFiltered(true);
        Assertions.assertNull(this.newsGroupFilter.getSelectedNewsgroup());
    }

    @Test
    public final void testSelectedNewsgroups() {
        this.newsGroupFilter.setFiltered(true);
        Assertions.assertEquals(0, this.newsGroupFilter.getSelectedNewsgroups().size());
        final Vector<NewsGroup> topsNewsgroups = new Vector<>();
        topsNewsgroups.add(Mockito.mock(NewsGroup.class));
        this.newsGroupFilter.setSelectedNewsgroups(topsNewsgroups);
        Assertions.assertEquals(1, this.newsGroupFilter.getSelectedNewsgroups().size());
        this.newsGroupFilter.clear();
        Assertions.assertEquals(0, this.newsGroupFilter.getSelectedNewsgroups().size());
    }

    @Test
    public final void testSelectedNewsgroupsNotFiltered() {
        this.newsGroupFilter.setFiltered(false);
        Assertions.assertNull(this.newsGroupFilter.getSelectedNewsgroups());
    }

    @Test
    public final void testSelectedPosters() {
        this.newsGroupFilter.setFiltered(true);
        Assertions.assertEquals(0, this.newsGroupFilter.getSelectedPosters().size());
        final Vector<UsenetUser> topsNewsgroups = new Vector<>();
        topsNewsgroups.add(Mockito.mock(UsenetUser.class));
        this.newsGroupFilter.setSelectedPosters(topsNewsgroups);
        Assertions.assertEquals(1, this.newsGroupFilter.getSelectedPosters().size());
        this.newsGroupFilter.clear();
        Assertions.assertEquals(0, this.newsGroupFilter.getSelectedPosters().size());
    }

    @Test
    public final void testSelectedPostersNotFiltered() {
        this.newsGroupFilter.setFiltered(false);
        Assertions.assertNull(this.newsGroupFilter.getSelectedPosters());
    }

    @Test
    public final void testTopicsFilter() {
        Assertions.assertEquals(0, this.newsGroupFilter.getTopicsFilter().size());
        final Set<Topic> countriesFilter = new HashSet<>();
        countriesFilter.add(Mockito.mock(Topic.class));
        this.newsGroupFilter.setTopicsFilter(countriesFilter);
        Assertions.assertEquals(1, this.newsGroupFilter.getTopicsFilter().size());
        this.newsGroupFilter.clear();
        Assertions.assertEquals(0, this.newsGroupFilter.getTopicsFilter().size());

    }

    @Test
    public final void testTopsNewsgroups() {
        Assertions.assertEquals(0, this.newsGroupFilter.getTopsNewsgroups().size());
        final Set<NewsGroup> topsNewsgroups = new HashSet<>();
        topsNewsgroups.add(Mockito.mock(NewsGroup.class));
        this.newsGroupFilter.setTopsNewsgroups(topsNewsgroups);
        Assertions.assertEquals(1, this.newsGroupFilter.getTopsNewsgroups().size());
        this.newsGroupFilter.clear();
        Assertions.assertEquals(0, this.newsGroupFilter.getTopsNewsgroups().size());
    }

    @Test
    public final void testUsenetUsersFilter() {
        Assertions.assertEquals(0, this.newsGroupFilter.getUsenetUsersFilter().size());
        final Vector<UsenetUser> filter = new Vector<>();
        filter.add(Mockito.mock(UsenetUser.class));
        this.newsGroupFilter.setUsenetUsersFilter(filter);
        Assertions.assertEquals(1, this.newsGroupFilter.getUsenetUsersFilter().size());
        this.newsGroupFilter.clear();
        Assertions.assertEquals(0, this.newsGroupFilter.getUsenetUsersFilter().size());

    }

}
