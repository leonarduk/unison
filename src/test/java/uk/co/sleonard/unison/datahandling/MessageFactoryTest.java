package uk.co.sleonard.unison.datahandling;

import org.junit.jupiter.api.Test;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.input.NewsArticle;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for {@link MessageFactory}.
 */
public class MessageFactoryTest {

    @Test
    public void testCreateMessage() throws UNISoNException {
        NewsArticle article = new NewsArticle("id", 1, "body", new Date(), "from", "group", "host", "refs", "subject");
        UsenetUser user = new UsenetUser();
        MessageFactory factory = new MessageFactory();
        Message message = factory.createMessage(article, null, user);
        assertNotNull(message);
        assertEquals("id", message.getUsenetMessageID());
    }
}

