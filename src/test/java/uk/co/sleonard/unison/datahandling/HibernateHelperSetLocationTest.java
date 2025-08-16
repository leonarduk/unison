package uk.co.sleonard.unison.datahandling;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.Test;
import org.mockito.Mockito;
import uk.co.sleonard.unison.datahandling.DAO.Location;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.input.NewsArticle;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;

public class HibernateHelperSetLocationTest {

    @Test
    public void testSetLocationCalledOnceWhenLocationChanges() throws Exception {
        HibernateHelper helper = new HibernateHelper(null);
        Session session = Mockito.mock(Session.class);
        Query query = Mockito.mock(Query.class);
        Mockito.when(session.getNamedQuery(Mockito.anyString())).thenReturn(query);
        Mockito.when(query.setParameter(Mockito.anyString(), Mockito.any())).thenReturn(query);
        UsenetUser poster = Mockito.mock(UsenetUser.class);
        Mockito.when(query.uniqueResult()).thenReturn(poster);

        Location oldLocation = new Location("OldCity", "OldCountry", "OC", false, new ArrayList<>(), new ArrayList<>());
        Location newLocation = new Location("NewCity", "NewCountry", "NC", false, new ArrayList<>(), new ArrayList<>());
        Mockito.when(poster.getLocation()).thenReturn(oldLocation);

        NewsArticle article = new NewsArticle("id", 1, new Date(), "name@example.com", "subject", "refs", "content", "group", null);

        Method method = HibernateHelper.class.getDeclaredMethod("createUsenetUser", NewsArticle.class, Session.class, Location.class, String.class);
        method.setAccessible(true);
        method.invoke(helper, article, session, newLocation, null);

        Mockito.verify(poster, Mockito.times(1)).setLocation(newLocation);
    }
}
