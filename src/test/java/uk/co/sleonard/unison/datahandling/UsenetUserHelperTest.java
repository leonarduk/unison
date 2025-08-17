package uk.co.sleonard.unison.datahandling;

import org.junit.jupiter.api.Test;
import uk.co.sleonard.unison.datahandling.DAO.EmailAddress;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The Class UsenetUserHelperTest
 *
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.2.0
 */
public class UsenetUserHelperTest {

    /**
     * Test parse field from EmailAddress.
     */
    @Test
    public void testParseFromField() throws Exception {
        EmailAddress expected = new EmailAddress("Elton", "elton_12_nunes@hotmail.com",
                "localhost");
        EmailAddress expected2 = new EmailAddress("Elton", "elton_12_nunes@hotmail.com", "UNKNOWN");
        EmailAddress expected3 = new EmailAddress("elton_12_nunes", "elton_12_nunes@hotmail.com",
                "localhost");
        EmailAddress expected4 = new EmailAddress("elton", "elton@localhost", "localhost");
        List<EmailAddress> actualList = parseFromField();

        assertEquals(expected, actualList.get(0));
        assertEquals(expected, actualList.get(1));
        assertEquals(expected, actualList.get(2));
        assertEquals(expected2, actualList.get(3));
        assertEquals(expected2, actualList.get(4));
        assertEquals(expected3, actualList.get(5));
        assertEquals(expected3, actualList.get(6));
        assertEquals(expected3, actualList.get(7));
        assertEquals(expected4, actualList.get(8));

    }

    /**
     * Ensures an {@link IllegalArgumentException} is thrown when neither name nor
     * email can be determined.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseFromFieldThrowsWhenMissingNameAndEmail() {
        UsenetUserHelper.parseFromField("", "localhost");
    }

    /**
     * Method used by testParseFromField.
     */
    private List<EmailAddress> parseFromField() {
        List<EmailAddress> listEmail = new ArrayList<>(10);
        listEmail.add(
                UsenetUserHelper.parseFromField("<elton_12_nunes@hotmail.com> Elton", "localhost"));        // 1
        listEmail.add(UsenetUserHelper.parseFromField("\"Elton\" <elton_12_nunes@hotmail.com>",
                "localhost"));    // 1
        listEmail.add(
                UsenetUserHelper.parseFromField("(Elton) elton_12_nunes@hotmail.com", "localhost"));        // 1
        listEmail.add(UsenetUserHelper.parseFromField("Elton <elton_12_nunes@hotmail.com>", null));            // 2
        listEmail.add(UsenetUserHelper.parseFromField("Elton <elton_12_nunes@hotmail.com>", ""));                // 2
        listEmail.add(UsenetUserHelper.parseFromField("<elton_12_nunes@hotmail.com>", "localhost"));            // 3
        listEmail.add(UsenetUserHelper.parseFromField("elton_12_nunes@hotmail.com", "localhost"));                // 3
        listEmail
                .add(UsenetUserHelper.parseFromField("() elton_12_nunes@hotmail.com", "localhost"));            // 3
        listEmail.add(UsenetUserHelper.parseFromField("elton", "localhost"));                                    // 4

        return listEmail;
    }
}
