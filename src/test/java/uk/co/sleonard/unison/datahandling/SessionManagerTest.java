package uk.co.sleonard.unison.datahandling;

import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for {@link SessionManager}.
 */
public class SessionManagerTest {

    @Test
    public void testOpenSession() throws Exception {
        try (Session session = SessionManager.openSession()) {
            assertNotNull(session);
        }
    }
}

