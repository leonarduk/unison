package uk.co.sleonard.unison.datahandling;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Utility class responsible for creating and providing Hibernate {@link Session}
 * instances. The configuration is read from the standard Hibernate
 * configuration file on first use and the {@link SessionFactory} is cached for
 * subsequent calls. All session creation is centralised here to avoid
 * scattering configuration throughout the codebase. Callers are responsible for
 * closing sessions when finished to prevent resource leaks.
 */
@Slf4j
public final class SessionManager {

    private static SessionFactory sessionFactory;

    private SessionManager() {
        // utility class
    }

    /**
     * Open a new {@link Session} using a lazily initialised
     * {@link SessionFactory}.
     *
     * @return a new Hibernate session
     */
    public static synchronized Session openSession() {
        if (sessionFactory == null) {
            log.info("Configuring session factory");
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }
        return sessionFactory.openSession();
    }
}

