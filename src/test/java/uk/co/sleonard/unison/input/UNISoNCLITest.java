package uk.co.sleonard.unison.input;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Test;

/**
 * Tests for {@link UNISoNCLI} argument parsing.
 */
public class UNISoNCLITest {

    @Test
    public void parseArgsReturnsCommandAndArgument() {
        final Optional<UNISoNCLI.ParsedArgs> parsed = UNISoNCLI.parseArgs(
                new String[] { "quickdownload", "pattern" });
        assertTrue(parsed.isPresent());
        assertEquals(UNISoNCLI.Command.QUICKDOWNLOAD, parsed.get().command);
        assertEquals("pattern", parsed.get().argument);
    }

    @Test
    public void parseArgsReturnsEmptyOnInvalidCommand() {
        assertFalse(UNISoNCLI.parseArgs(new String[] { "invalid" }).isPresent());
    }
}

