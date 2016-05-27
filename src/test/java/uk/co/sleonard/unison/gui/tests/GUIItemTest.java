package uk.co.sleonard.unison.gui.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.gui.GUIItem;

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
		NewsGroup[] ngArray = { new NewsGroup("ng1.uk"), new NewsGroup("ng2.uk") };
		assertEquals(2, GUIItem.getGUIList(ngArray, new HibernateHelper(null)).size());
		Topic[] topArray = { new Topic(), new Topic(), new Topic() };
		assertEquals(3, GUIItem.getGUIList(topArray, new HibernateHelper(null)).size());
	}

	/**
	 * Test getItem and toString
	 */
	@Test
	public void testGetItemAndToString() {
		NewsGroup expected = new NewsGroup();
		guiItem = new GUIItem<NewsGroup>("name", expected);
		assertEquals("name", guiItem.toString());
		assertNotNull(guiItem.getItem());
	}

}
