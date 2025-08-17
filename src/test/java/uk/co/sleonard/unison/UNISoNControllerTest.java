package uk.co.sleonard.unison;

import org.junit.jupiter.api.Test;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.input.DataHibernatorPoolImpl;

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
