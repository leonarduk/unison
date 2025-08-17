/**
 * UNISoNControllerIT
 *
 * @author ${author}
 * @since 19-Jun-2016
 */
package uk.co.sleonard.unison;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.input.DataHibernatorPoolImpl;
import uk.co.sleonard.unison.utils.StringUtils;

import javax.swing.*;
import java.util.Set;

@Slf4j
public class UNISoNControllerIT {
    private UNISoNController controller;

    @Before
    public void setUp() throws Exception {
        this.controller = new UNISoNControllerFactory().create(null, new DataHibernatorPoolImpl());
    }

    @Test
    public final void testGetAvailableGroupsModel() throws UNISoNException {
        final String[] servers = StringUtils.loadServerList();
        Assertions.assertTrue(servers.length > 0);
        final Set<NewsGroup> groups = this.controller.listNewsgroups("", servers[0],
                this.controller.getNntpReader().getClient());
        final ListModel<NewsGroup> model = this.controller.getAvailableGroupsModel(groups);
        final NewsGroup firstGroup = model.getElementAt(0);
        Assertions.assertNotNull(firstGroup);
        Assertions.assertTrue(org.apache.commons.lang3.StringUtils.isNotEmpty(firstGroup.getFullName()));

    }

    @Test
    public final void testListnewsgroups() throws UNISoNException {
        final String[] servers = StringUtils.loadServerList();
        Assertions.assertTrue(servers.length > 0);
        final Set<NewsGroup> groups = this.controller.listNewsgroups("", servers[0],
                this.controller.getNntpReader().getClient());
        Assertions.assertTrue(groups.size() > 0);
        log.info("Found " + groups.size());

    }

}
