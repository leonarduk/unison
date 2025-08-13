/**
 * UNISoNControllerIT
 *
 * @author ${author}
 * @since 19-Jun-2016
 */
package uk.co.sleonard.unison;

import java.util.Set;

import javax.swing.ListModel;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.input.DataHibernatorPoolImpl;
import uk.co.sleonard.unison.utils.StringUtils;

@Slf4j
@Ignore("Requires a live NNTP server and is disabled to avoid external network calls")
public class UNISoNControllerIT {
	private UNISoNController	controller;

	@Before
	public void setUp() throws Exception {
		this.controller = UNISoNController.create(null, new DataHibernatorPoolImpl());
	}

	@Test
	public final void testGetAvailableGroupsModel() throws UNISoNException {
		final Set<NewsGroup> groups = this.controller.listNewsgroups("",
		        StringUtils.loadServerList()[0], this.controller.getNntpReader().getClient());
		final ListModel<NewsGroup> model = this.controller.getAvailableGroupsModel(groups);
		final NewsGroup firstGroup = model.getElementAt(0);
		Assert.assertNotNull(firstGroup);
                Assert.assertTrue(org.apache.commons.lang3.StringUtils.isNotEmpty(firstGroup.getFullName()));

	}

	@Test
	public final void testListnewsgroups() throws UNISoNException {
		final Set<NewsGroup> groups = this.controller.listNewsgroups("",
		        StringUtils.loadServerList()[0], this.controller.getNntpReader().getClient());
		Assert.assertTrue(groups.size() > 0);
		log.info("Found " + groups.size());

	}

}
