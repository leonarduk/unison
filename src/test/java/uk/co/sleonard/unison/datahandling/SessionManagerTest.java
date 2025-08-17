package uk.co.sleonard.unison.datahandling;

import static org.junit.Assert.assertNotNull;

import org.hibernate.Session;
import org.junit.Test;

/** Tests for {@link SessionManager}. */
public class SessionManagerTest {

  @Test
  public void testOpenSession() throws Exception {
    try (Session session = SessionManager.openSession()) {
      assertNotNull(session);
    }
  }
}
