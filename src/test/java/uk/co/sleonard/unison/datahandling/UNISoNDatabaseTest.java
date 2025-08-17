/**
 * UNISoNDatabaseTest
 *
 * @author ${author}
 * @since 20-Jun-2016
 */
package uk.co.sleonard.unison.datahandling;

import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import uk.co.sleonard.unison.NewsGroupFilter;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class UNISoNDatabaseTest {

    private UNISoNDatabase database;
    private NewsGroupFilter filter2;
    private Session session2;
    private DataQuery dataQuery;
    private HibernateHelper helper2;
    private int i;

    @BeforeEach
    public void setUp() throws Exception {
        this.filter2 = Mockito.mock(NewsGroupFilter.class);
        final Vector<Message> mesgs = new Vector<>();
        Mockito.when(this.filter2.getMessagesFilter()).thenReturn(mesgs);
        Mockito.when(this.filter2.getUsenetUsersFilter()).thenReturn(new Vector<>());
        Mockito.when(this.filter2.getCountriesFilter()).thenReturn(new HashSet<>());
        Mockito.when(this.filter2.getTopicsFilter()).thenReturn(new HashSet<>());
        Mockito.when(this.filter2.getNewsgroupFilter()).thenReturn(new HashSet<>());
        Mockito.when(this.filter2.getTopsNewsgroups()).thenReturn(new HashSet<>());
        this.helper2 = Mockito.mock(HibernateHelper.class);
        this.dataQuery = Mockito.mock(DataQuery.class);
        this.database = new UNISoNDatabase(this.filter2, this.session2, this.helper2, this.dataQuery);
        this.i = 0;
    }

    @SuppressWarnings("unchecked")
    @Test
    public final void testGetMessages() {
        final Topic topic = new Topic("topic", null);
        final Vector<Message> messages = new Vector<>();
        final byte[] messageBody = "eggs".getBytes();
        final Message msg = new Message(new Date(), "234", "All about me",
                new UsenetUser("poster", "poster@email.com", "127.0.0.1", null, null),
                new Topic("topic", null), null, null, messageBody);
        messages.add(msg);
        Mockito.when(this.helper2.runQuery(ArgumentMatchers.anyString(), ArgumentMatchers.nullable(Session.class),
                ArgumentMatchers.any(Class.class))).thenReturn(messages);
        final Set<Message> result = this.database.getMessages(topic, this.session2);
        Assertions.assertEquals(1, result.size());
        Assertions.assertTrue(result.contains(msg));
    }

    @Test
    public final void testNotifyListeners() {
        Assertions.assertEquals(0, this.i);
        this.database.addDataChangeListener(evt -> {
            this.i++;
        });
        this.database.notifyListeners();
        Assertions.assertEquals(1, this.i);
    }

    @Test
    public final void testRefreshDataFromDatabase() {
        this.database.refreshDataFromDatabase();
        Mockito.verify(this.dataQuery).getMessages(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.eq(this.session2), ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.anyBoolean(), ArgumentMatchers.any(), ArgumentMatchers.any());
        Mockito.verify(this.filter2).clear();
    }

}
