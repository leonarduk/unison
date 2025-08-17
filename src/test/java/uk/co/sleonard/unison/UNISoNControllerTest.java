package uk.co.sleonard.unison;

import org.junit.Test;
import uk.co.sleonard.unison.UNISoNControllerFactory;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.input.DataHibernatorPoolImpl;

import javax.swing.ListModel;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Tests for {@link UNISoNController} that operate fully in memory.
 */
public class UNISoNControllerTest {

    @Test
    public void testGetAvailableGroupsModelUsesSuppliedGroups() throws UNISoNException {
        UNISoNController controller = new UNISoNControllerFactory().create(null, new DataHibernatorPoolImpl());

        Set<NewsGroup> groups = new HashSet<>();
        NewsGroup group = new NewsGroup();
        group.setFullName("test.group");
        groups.add(group);

        ListModel<NewsGroup> model = controller.getAvailableGroupsModel(groups);
        assertEquals(1, model.getSize());
        assertSame(group, model.getElementAt(0));
    }
}
