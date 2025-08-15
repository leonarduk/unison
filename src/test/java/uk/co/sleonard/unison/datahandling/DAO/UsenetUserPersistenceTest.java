package uk.co.sleonard.unison.datahandling.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Assert;
import org.junit.Test;
import java.util.Collections;

/**
 * Regression test to ensure {@link UsenetUser} can be persisted using annotations.
 */
public class UsenetUserPersistenceTest {

    @Test
    public void testPersistAndLoad() {
        Configuration cfg = new Configuration().configure();
        cfg.setProperty("hibernate.connection.url", "jdbc:hsqldb:mem:usenetuser;DB_CLOSE_DELAY=-1");
        cfg.setProperty("hibernate.hbm2ddl.auto", "create-drop");

        try (SessionFactory sf = cfg.buildSessionFactory();
             Session session = sf.openSession()) {
            Assert.assertNotNull(sf.getClassMetadata(Location.class));
            Assert.assertNotNull(sf.getClassMetadata(IpAddress.class));
            Transaction tx = session.beginTransaction();
            Location location = new Location("City", "Country", "CC", false,
                    Collections.emptyList(), Collections.emptyList());
            session.save(location);
            IpAddress ip = new IpAddress("127.0.0.1", location);
            session.save(ip);
            UsenetUser user = new UsenetUser("Test", "test@example.com", "127.0.0.1", "male",
                    location);
            session.save(user);
            tx.commit();

            session.clear();
            UsenetUser fromDb = session.get(UsenetUser.class, user.getId());
            Assert.assertNotNull(fromDb);
            Assert.assertEquals("test@example.com", fromDb.getEmail());
            Assert.assertEquals("City", fromDb.getLocation().getCity());
        }
    }
}
