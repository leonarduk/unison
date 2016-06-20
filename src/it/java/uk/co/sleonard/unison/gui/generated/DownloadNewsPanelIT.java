/**
 * DownloadNewsPanelIT
 *
 * @author ${author}
 * @since 19-Jun-2016
 */
package uk.co.sleonard.unison.gui.generated;

import java.util.Set;

import javax.swing.ListModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.co.sleonard.unison.UNISoNController;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.utils.StringUtils;

@SuppressWarnings("ucd")
public class DownloadNewsPanelIT {

	private UNISoNController	controller;
	private DownloadNewsPanel	panel;

	@Before
	public void setUp() throws Exception {
		this.controller = UNISoNController.create(null);
		this.panel = new DownloadNewsPanel();
	}

	@Test
	public final void testGetAvailableGroupsModel() throws UNISoNException {
		final Set<NewsGroup> groups = this.controller.listNewsgroups("",
		        StringUtils.loadServerList()[0]);
		final ListModel<NewsGroup> model = this.panel.getAvailableGroupsModel(groups);
		final NewsGroup firstGroup = model.getElementAt(0);
		Assert.assertNotNull(firstGroup);
		Assert.assertTrue(org.apache.commons.lang.StringUtils.isNotEmpty(firstGroup.getFullName()));

	}

}
