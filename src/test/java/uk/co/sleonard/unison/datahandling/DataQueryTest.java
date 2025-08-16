package uk.co.sleonard.unison.datahandling;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The Class DataQueryTest.
 *
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.2.0
 */
public class DataQueryTest {

    private DataQuery dataQuery;
    private HibernateHelper helper;

    /**
     * Setup.
     */
    @Before
    public void setUp() {
        this.helper = mock(HibernateHelper.class);
        this.dataQuery = new DataQuery(this.helper);
    }

    /**
     * Test get location string.
     */
    @Test
    public void testGetLocationString() {
        Vector<String> countries = new Vector<>();
        countries.addElement("England");
        String actual = this.dataQuery.getLocationsSQL(countries).toString();
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
        when(Integer.valueOf(message.getId())).thenReturn(Integer.valueOf(1));
        messages.add(message);
        assertEquals("'1'", this.dataQuery.getMessageIdsString(messages).toString()); // 1
        // object
        messages.add(message);
        String test = this.dataQuery.getMessageIdsString(messages).toString();
        assertEquals(Boolean.valueOf(true), Boolean.valueOf(test.contains(","))); // 2 objects
        assertNotNull(this.dataQuery.getMessageIdsString(null)); // If null
        assertNotNull(this.dataQuery.getMessageIdsString(messagesEmpty)); // If size
        // <= 0

    }

    /**
     * Test get messages.
     */
    @SuppressWarnings("rawtypes")
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
        when(this.helper.runQuery(ArgumentMatchers.anyString(), ArgumentMatchers.any(Session.class),
                ArgumentMatchers.<Class<Message>>any())).thenReturn(messages);
        Vector<Message> actual = this.dataQuery.getMessages(messages, users, session, date, date,
                filtered, news, countries); // If
        // filtered
        // is
        // true
        Vector expected = messages;
        assertEquals(expected, actual);
        filtered = false;
        actual = this.dataQuery.getMessages(messages, users, session, date, date, filtered, news,
                countries); // If
        // filtered
        // is
        // false
        assertEquals(expected, actual);
        actual = this.dataQuery.getMessages(null, null, session, null, null, filtered, null, null); // If
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
        assertEquals("apple", this.dataQuery.join(words, "-"));
        words.add("pear");
        assertEquals("apple-pear", this.dataQuery.join(words, "-"));
    }

    /**
     * Test get UsenetUser ids.
     */
    @Test
    public void testGetUsenetUserIdsString() {
        Vector<UsenetUser> users = new Vector<>();
        Vector<UsenetUser> usersEmpty = new Vector<>();
        UsenetUser user = mock(UsenetUser.class);
        when(Integer.valueOf(user.getId())).thenReturn(Integer.valueOf(1));
        users.add(user);
        assertEquals("'1'", this.dataQuery.getUsenetUserIdsString(users).toString()); // 1
        // object
        users.add(user);
        String test = this.dataQuery.getUsenetUserIdsString(users).toString();
        assertEquals(Boolean.valueOf(true), Boolean.valueOf(test.contains(","))); // 2 objects
        assertNotNull(this.dataQuery.getUsenetUserIdsString(null)); // If null
        assertNotNull(this.dataQuery.getUsenetUserIdsString(usersEmpty)); // If size
        // <= 0
    }

}
