package uk.co.sleonard.unison.datahandling;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DAO.Message;

public class HibernateHelperRunQueryTest {

    private HibernateHelper helper;
    private Session session;

    @Before
    public void setUp() throws UNISoNException {
        this.helper = new HibernateHelper(null);
        this.session = this.helper.getHibernateSession();
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
