package uk.co.sleonard.unison.datahandling;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashSet;
import org.junit.Test;
import uk.co.sleonard.unison.datahandling.DAO.Message;

/** Tests for {@link MessageCacheService}. */
public class MessageCacheServiceTest {

  @Test
  public void testPutAndGet() {
    MessageCacheService service = new MessageCacheService();
    Message message =
        new Message(new Date(), "id1", "subject", null, null, new HashSet<>(), null, null);
    service.put(message);
    Message cached = service.get("id1");
    assertNotNull(cached);
    assertEquals(message, cached);
  }
}
