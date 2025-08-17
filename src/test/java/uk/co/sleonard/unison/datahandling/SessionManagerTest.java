package uk.co.sleonard.unison.datahandling;

import org.junit.jupiter.api.Test;

import uk.co.sleonard.unison.UNISoNException;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link SessionManager}.
 */
public class SessionManagerTest {

    @Test
    public void testOpenSession() {
        assertThrows(UNISoNException.class, SessionManager::openSession);
    }
}

