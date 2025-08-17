package uk.co.sleonard.unison.datahandling;

import static org.junit.Assert.*;

import java.util.Date;
import org.junit.Test;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.input.NewsArticle;

/** Tests for {@link MessageFactory}. */
public class MessageFactoryTest {

  @Test
  public void testCreateMessage() throws UNISoNException {
    NewsArticle article =
        new NewsArticle("id", 1, "body", new Date(), "from", "group", "host", "refs", "subject");
    UsenetUser user = new UsenetUser();
    MessageFactory factory = new MessageFactory();
    Message message = factory.createMessage(article, null, user);
    assertNotNull(message);
    assertEquals("id", message.getUsenetMessageID());
  }
}
