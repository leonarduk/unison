package uk.co.sleonard.unison.datahandling;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.LoggerFactory;
import uk.co.sleonard.unison.UNISoNException;

/**
 * Utility class responsible for creating and providing Hibernate {@link Session} instances. The
 * configuration is read from the standard Hibernate configuration file on first use and the {@link
 * SessionFactory} is cached for subsequent calls.
 */
public final class SessionManager {

  private static SessionFactory sessionFactory;

  private SessionManager() {
    // utility class
  }

  /**
   * Open a new {@link Session} using a lazily initialised {@link SessionFactory}.
   *
   * @return a new Hibernate session
   * @throws UNISoNException if the session factory could not be created
   */
  public static synchronized Session openSession() throws UNISoNException {
    if (sessionFactory == null) {
      try {
        Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        Level level = root.getLevel();
        root.setLevel(Level.ERROR);
        sessionFactory = new Configuration().configure().buildSessionFactory();
        root.setLevel(level);
      } catch (Throwable e) {
        throw new UNISoNException("Failed to connect to DB", e);
      }
    }
    return sessionFactory.openSession();
  }
}
