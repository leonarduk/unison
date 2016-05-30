/**
 * GUIItemTest
 *
 * @author ${author}
 * @since 29-May-2016
 */
package uk.co.sleonard.unison.datahandling.DAO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.co.sleonard.unison.datahandling.HibernateHelper;

/**
 * The Class GUIItem.
 *
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.3.0
 *
 */
public class GUIItemTest {

	private GUIItem<NewsGroup> guiItem;

	/**
	 * SetUp
	 */
	@Before
	public void setUp() {
	}

	/**
	 * Test getGUIList
	 */
	@Test
	public void testGetGUIListObjectArrayHibernateHelper() {
		final NewsGroup[] ngArray = { new NewsGroup("ng1.uk"), new NewsGroup("ng2.uk") };
		Assert.assertEquals(2, GUIItem.getGUIList(ngArray, new HibernateHelper(null)).size());
		final Topic[] topArray = { new Topic(), new Topic(), new Topic() };
		Assert.assertEquals(3, GUIItem.getGUIList(topArray, new HibernateHelper(null)).size());
	}

	/**
	 * Test getItem and toString
	 */
	@Test
	public void testGetItemAndToString() {
		final NewsGroup expected = new NewsGroup();
		this.guiItem = new GUIItem<NewsGroup>("name", expected);
		Assert.assertEquals("name", this.guiItem.toString());
		Assert.assertNotNull(this.guiItem.getItem());
	}

}
