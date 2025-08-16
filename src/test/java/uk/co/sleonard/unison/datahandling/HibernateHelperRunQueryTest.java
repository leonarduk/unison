package uk.co.sleonard.unison.datahandling;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HibernateHelperRunQueryTest {

    private HibernateHelper helper;
    private Session session;

    @Before
    public void setUp() throws UNISoNException {
        this.helper = new HibernateHelper(null);
        this.session = this.helper.getHibernateSession();

        // Ensure a clean database state before seeding
        this.session.beginTransaction();
        this.session.createQuery("delete from Message").executeUpdate();
        this.session.createQuery("delete from Topic").executeUpdate();
        this.session.createQuery("delete from UsenetUser").executeUpdate();
        this.session.getTransaction().commit();

        // Seed the in-memory database with a single message
        this.session.beginTransaction();
        UsenetUser poster = new UsenetUser("poster", "poster@example.com", "127.0.0.1", null, null);
        this.session.save(poster);
        Topic topic = new Topic("topic", new HashSet<>());
        this.session.save(topic);
        Message msg = new Message(new Date(), "msg-1", "Duke Nukem Hall of Shame (update)", poster, topic,
                new HashSet<>(), null, null);
        this.session.save(msg);
        this.session.getTransaction().commit();
    }

    @After
    public void tearDown() {
        if (this.session != null) {
            this.session.close();
        }
    }

    @Test
    public void testRunQueryBindsParameters() {
        Map<String, Object> params = new HashMap<>();
        params.put("subject", "Duke Nukem Hall of Shame (update)");
        Vector<Message> results = this.helper.runQuery(
                "from Message m where m.subject = :subject",
                params,
                this.session,
                Message.class);
        assertEquals(1, results.size());
    }

    @Test
    public void testRunQueryPreventsSqlInjection() {
        Map<String, Object> params = new HashMap<>();
        params.put("subject", "Duke Nukem Hall of Shame (update)' OR '1'='1");
        Vector<Message> results = this.helper.runQuery(
                "from Message m where m.subject = :subject",
                params,
                this.session,
                Message.class);
        assertTrue(results.isEmpty());
    }
}
