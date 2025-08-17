package uk.co.sleonard.unison;

import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.input.DataHibernatorPoolImpl;
import uk.co.sleonard.unison.input.NewsClient;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Tests for {@link UNISoNController} that operate fully in memory.
 */
public class UNISoNControllerTest {

    @Test
    public void testGetAvailableGroupsModelUsesSuppliedGroups() throws UNISoNException {
        NewsClient client = Mockito.mock(NewsClient.class);
        HibernateHelper helper = Mockito.mock(HibernateHelper.class);
        Session session = Mockito.mock(Session.class);
        Mockito.when(helper.getHibernateSession()).thenReturn(session);

        UNISoNController controller = new UNISoNController(client, helper, new java.util.concurrent.LinkedBlockingQueue<>(), new DataHibernatorPoolImpl(), null);

        Set<NewsGroup> groups = new HashSet<>();
        NewsGroup group = new NewsGroup();
        group.setFullName("test.group");
        groups.add(group);

        ListModel<NewsGroup> model = controller.getAvailableGroupsModel(groups);
        assertEquals(1, model.getSize());
        assertSame(group, model.getElementAt(0));
    }
}
