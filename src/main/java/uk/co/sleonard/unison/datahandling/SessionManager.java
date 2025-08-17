package uk.co.sleonard.unison.datahandling;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.sleonard.unison.UNISoNException;

/**
 * Utility class responsible for creating and providing Hibernate {@link Session}
 * instances. The configuration is read from the standard Hibernate
 * configuration file on first use and the {@link SessionFactory} is cached for
 * subsequent calls.
 */
public final class SessionManager {

    private static final Logger log = LoggerFactory.getLogger(SessionManager.class);
    private static SessionFactory sessionFactory;

    private SessionManager() {
        // utility class
    }

    /**
     * Open a new {@link Session} using a lazily initialised
     * {@link SessionFactory}.
     *
     * @return a new Hibernate session
     * @throws UNISoNException if the session factory could not be created
     */
    public static synchronized Session openSession() throws UNISoNException {
        if (sessionFactory == null) {
            try {
                sessionFactory = new Configuration().configure().buildSessionFactory();
            } catch (Throwable e) {
                log.error("Failed to connect to DB", e);
                throw new UNISoNException("Failed to connect to DB", e);
            }
        }
        return sessionFactory.openSession();
    }
}

