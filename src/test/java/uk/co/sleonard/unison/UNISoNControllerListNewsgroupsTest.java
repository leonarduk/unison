package uk.co.sleonard.unison;

import org.hibernate.Session;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.SessionManager;
import uk.co.sleonard.unison.input.DataHibernatorPool;
import uk.co.sleonard.unison.input.NewsArticle;
import uk.co.sleonard.unison.input.NewsClient;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link UNISoNController#listNewsgroups(String, String, NewsClient)}
 * using JUnit 5 features.
 */
class UNISoNControllerListNewsgroupsTest {

    @Nested
    class ListNewsgroups {

        @ParameterizedTest
        @CsvSource({"groupA,hostA", "groupB,hostB"})
        void delegatesToClientAndUpdatesHost(String group, String host) throws Exception {
            NewsClient client = mock(NewsClient.class);
            Set<NewsGroup> expected = Collections.singleton(new NewsGroup());
            when(client.listNewsGroups(group, host)).thenReturn(expected);

            DataHibernatorPool pool = mock(DataHibernatorPool.class);
            HibernateHelper helper = mock(HibernateHelper.class);

            try (MockedStatic<SessionManager> sessionManager = Mockito.mockStatic(SessionManager.class)) {
                Session session = mock(Session.class);
                sessionManager.when(SessionManager::openSession).thenReturn(session);

                UNISoNController controller = new UNISoNController(
                        client, helper, new LinkedBlockingQueue<NewsArticle>(), pool, null);

                Set<NewsGroup> result = controller.listNewsgroups(group, host, client);

                assertSame(expected, result);
                assertEquals(host, controller.getNntpHost());
                verify(client).listNewsGroups(group, host);
            }
        }
    }
}

