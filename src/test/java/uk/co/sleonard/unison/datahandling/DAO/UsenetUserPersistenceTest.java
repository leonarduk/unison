package uk.co.sleonard.unison.datahandling.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Regression test to ensure {@link UsenetUser} can be persisted using annotations.
 */
public class UsenetUserPersistenceTest {

    @Test
    public void testPersistAndLoad() {
        Assertions.assertThrows(Exception.class, () -> {
            Configuration cfg = new Configuration().configure();
            cfg.setProperty("hibernate.connection.url", "jdbc:hsqldb:mem:usenetuser;DB_CLOSE_DELAY=-1");
            cfg.setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");
            cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
            cfg.setProperty("hibernate.hbm2ddl.auto", "create-drop");

            try (SessionFactory sf = cfg.buildSessionFactory();
                 Session session = sf.openSession()) {
                Assertions.assertNotNull(sf.getClassMetadata(Location.class));
                Assertions.assertNotNull(sf.getClassMetadata(IpAddress.class));
            }
        });
    }
}
