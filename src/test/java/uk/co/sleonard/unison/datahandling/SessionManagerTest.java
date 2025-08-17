package uk.co.sleonard.unison.datahandling;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for {@link SessionManager}.
 */
 class SessionManagerTest {

    @Test
    void testOpenSession() {
        assertNotNull( SessionManager.openSession());
    }
}

