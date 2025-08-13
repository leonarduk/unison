package uk.co.sleonard.unison.datahandling.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Assert;
import org.junit.Test;

/**
 * Regression test to ensure {@link UsenetUser} can be persisted using annotations.
 */
public class UsenetUserPersistenceTest {

    @Test
    public void testPersistAndLoad() {
        Configuration cfg = new Configuration();
        cfg.setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");
        cfg.setProperty("hibernate.connection.url", "jdbc:hsqldb:mem:usenetuser;DB_CLOSE_DELAY=-1");
        cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        cfg.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        cfg.setProperty("hibernate.show_sql", "false");
        cfg.addAnnotatedClass(UsenetUser.class);
        cfg.addResource("dao/Location.hbm.xml");

        try (SessionFactory sf = cfg.buildSessionFactory();
             Session session = sf.openSession()) {
            Transaction tx = session.beginTransaction();
            UsenetUser user = new UsenetUser("Test", "test@example.com", "127.0.0.1", "male", null);
            session.save(user);
            tx.commit();

            session.clear();
            UsenetUser fromDb = session.get(UsenetUser.class, user.getId());
            Assert.assertNotNull(fromDb);
            Assert.assertEquals("test@example.com", fromDb.getEmail());
        }
    }
}
