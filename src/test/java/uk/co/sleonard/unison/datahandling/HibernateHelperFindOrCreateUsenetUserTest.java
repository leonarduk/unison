package uk.co.sleonard.unison.datahandling;

import static org.junit.Assert.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import org.hibernate.Session;
import org.junit.Test;
import org.mockito.Mockito;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.input.NewsArticle;

/** Tests for {@link HibernateHelper#findOrCreateUsenetUser(NewsArticle, Session, String)}. */
public class HibernateHelperFindOrCreateUsenetUserTest {

  @Test
  public void throwsExceptionWhenFromFieldInvalid() throws Exception {
    HibernateHelper helper = new HibernateHelper(null);
    Session session = Mockito.mock(Session.class);
    NewsArticle article =
        new NewsArticle("id", 1, new Date(), "", "subj", "", null, "alt.test", "127.0.0.1");

    Method m =
        HibernateHelper.class.getDeclaredMethod(
            "findOrCreateUsenetUser", NewsArticle.class, Session.class, String.class);
    m.setAccessible(true);

    assertThrows(
        UNISoNException.class,
        () -> {
          try {
            m.invoke(helper, article, session, null);
          } catch (InvocationTargetException e) {
            throw e.getCause();
          }
        });
  }
}
