/**
 * GUIItemTest
 *
 * @author ${author}
 * @since 29-May-2016
 */
package uk.co.sleonard.unison.datahandling.DAO;

import org.junit.Assert;
import org.junit.Test;

/**
 * The Class GUIItem.
 *
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.3.0
 */
public class GUIItemTest {

    private GUIItem<NewsGroup> guiItem;

    /**
     * Test getItem and toString
     */
    @Test
    public void testGetItemAndToString() {
        final NewsGroup expected = new NewsGroup();
        this.guiItem = new GUIItem<>("name", expected);
        Assert.assertEquals("name", this.guiItem.toString());
        Assert.assertNotNull(this.guiItem.object());
    }

}
