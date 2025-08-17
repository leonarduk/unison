package uk.co.sleonard.unison.datahandling;

import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DAO.Location;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.input.NewsArticle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for {@link UserFactory}.
 */
public class UserFactoryTest {

    @Test
    public void testCreateUsenetUser() throws UNISoNException {
        UserFactory factory = new UserFactory();
        Session session = Mockito.mock(Session.class);
        HibernateHelper helper = Mockito.mock(HibernateHelper.class);
        NewsArticle article = new NewsArticle("id", 1, null, new java.util.Date(), "Alice <alice@example.com>", null, null, null, "subj");
        UsenetUser user = factory.createUsenetUser(article, session, (Location) null, null, helper);
        assertNotNull(user);
        assertEquals("alice@example.com", user.getEmail());
        Mockito.verify(session).saveOrUpdate(user);
    }
}

