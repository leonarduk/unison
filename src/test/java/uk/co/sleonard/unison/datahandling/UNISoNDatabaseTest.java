/**
 * UNISoNDatabaseTest
 *
 * @author ${author}
 * @since 20-Jun-2016
 */
package uk.co.sleonard.unison.datahandling;

import java.util.Date;
import java.util.Vector;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import uk.co.sleonard.unison.NewsGroupFilter;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;

public class UNISoNDatabaseTest {

	private UNISoNDatabase	database;
	private NewsGroupFilter	filter2;
	private Session			session2;
	private HibernateHelper	helper2;
	private int				i;

	@Before
	public void setUp() throws Exception {
		this.filter2 = Mockito.mock(NewsGroupFilter.class);
		final Vector<Message> mesgs = new Vector<>();
		Mockito.when(this.filter2.getMessagesFilter()).thenReturn(mesgs);
		this.helper2 = Mockito.mock(HibernateHelper.class);
		final DataQuery dataquery = Mockito.mock(DataQuery.class);
		this.database = new UNISoNDatabase(this.filter2, this.session2, this.helper2, dataquery);
		this.i = 0;
	}

	@SuppressWarnings("unchecked")
	@Test
	public final void testGetMessages() {
		final Topic topic = new Topic("topic");
		final Vector<Message> messages = new Vector<>();
		final byte[] messageBody = "eggs".getBytes();
		final Message msg = new Message(new Date(), "234", "All about me",
		        new UsenetUser("poster", "poster@email.com", "127.0.0.1"), new Topic("topic"),
		        messageBody);
		messages.add(msg);
		Mockito.when(this.helper2.runQuery(Matchers.anyString(), Matchers.any(Session.class),
		        Matchers.any(Class.class))).thenReturn(messages);
		this.database.getMessages(topic, this.session2);
	}

	@Test
	public final void testNotifyObservers() {
		Assert.assertEquals(0, this.i);
		this.database.addObserver((o, arg) -> {
			this.i++;
		});
		this.database.notifyObservers();
		Assert.assertEquals(1, this.i);
	}

	@Test
	public final void testRefreshDataFromDatabase() {
		this.database.refreshDataFromDatabase();
	}

}
