package uk.co.sleonard.unison.gui.generated;

import static org.junit.Assert.assertFalse;

import java.lang.reflect.Field;

import org.junit.Test;

/**
 * Tests for {@link PajekPanel}.
 */
public class PajekPanelTest {

    @Test
    public void testIncMissingCheckFieldRemoved() {
        boolean found = false;
        for (Field field : PajekPanel.class.getDeclaredFields()) {
            if ("incMissingCheck".equals(field.getName())) {
                found = true;
                break;
            }
        }
        assertFalse("incMissingCheck field should have been removed", found);
    }
}
