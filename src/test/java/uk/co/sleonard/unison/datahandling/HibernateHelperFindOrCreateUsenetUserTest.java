package uk.co.sleonard.unison.datahandling;

import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.input.NewsArticle;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link UserFactory#createUsenetUser(NewsArticle, Session, uk.co.sleonard.unison.datahandling.DAO.Location, String, HibernateHelper)}
 * throwing {@link UNISoNException} when the From field cannot be parsed.
 */
public class HibernateHelperFindOrCreateUsenetUserTest {

    @Test
    public void throwsExceptionWhenFromFieldInvalid() throws Exception {
        HibernateHelper helper = new HibernateHelper(null);
        Session session = Mockito.mock(Session.class);
        NewsArticle article = new NewsArticle("id", 1, new Date(), "", "subj", "", null, "alt.test",
                null);

        UserFactory userFactory = new UserFactory();

        assertThrows(UNISoNException.class,
                () -> userFactory.createUsenetUser(article, session, null, null, helper));
    }
}
