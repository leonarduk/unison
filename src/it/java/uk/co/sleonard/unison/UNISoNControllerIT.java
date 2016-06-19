/**
 * UNISoNControllerIT
 *
 * @author ${author}
 * @since 19-Jun-2016
 */
package uk.co.sleonard.unison;

import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.utils.StringUtils;

public class UNISoNControllerIT {
	private static Logger		logger	= Logger.getLogger(UNISoNControllerIT.class);
	private UNISoNController	controller;

	@Before
	public void setUp() throws Exception {
		this.controller = UNISoNController.create(null);
	}

	@Test
	public final void testListnewsgroups() throws UNISoNException {
		final Set<NewsGroup> groups = this.controller.listNewsgroups("",
		        StringUtils.loadServerList()[0]);
		Assert.assertTrue(groups.size() > 0);
		UNISoNControllerIT.logger.info("Found " + groups.size());

	}

}
