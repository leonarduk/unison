/**
 * MessageStoreViewerTest
 */
package uk.co.sleonard.unison.gui.generated;

import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import uk.co.sleonard.unison.NewsGroupFilter;
import uk.co.sleonard.unison.UNISoNController;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.gui.UNISoNGUI;
import uk.co.sleonard.unison.utils.TreeNode;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link MessageStoreViewer}.
 */
public class MessageStoreViewerTest {

    private UNISoNController controller;
    private NewsGroupFilter filter;

    @BeforeEach
    public void setUp() throws Exception {
        this.controller = Mockito.mock(UNISoNController.class);
        this.filter = Mockito.mock(NewsGroupFilter.class);
        final HibernateHelper helper = Mockito.mock(HibernateHelper.class);
        final Session session = Mockito.mock(Session.class);
        final UNISoNGUI gui = Mockito.mock(UNISoNGUI.class);

        Mockito.when(this.controller.helper()).thenReturn(helper);
        Mockito.when(helper.getHibernateSession()).thenReturn(session);
        Mockito.when(this.controller.getFilter()).thenReturn(this.filter);
        Mockito.when(this.controller.getGui()).thenReturn(gui);

    }

    @Test
    public void testRefreshTopicHierarchyFiltersBySelectedNewsgroup() throws Exception {
        final NewsGroup selected = new NewsGroup();
        selected.setFullName("group1");

        final NewsGroup other = new NewsGroup();
        other.setFullName("group2");

        final Set<NewsGroup> selectedSet = new HashSet<>();
        selectedSet.add(selected);
        final Topic topic1 = new Topic("t1", selectedSet);

        final Set<NewsGroup> otherSet = new HashSet<>();
        otherSet.add(other);
        final Topic topic2 = new Topic("t2", otherSet);

        final Set<Topic> topics = new HashSet<>();
        topics.add(topic1);
        topics.add(topic2);
        selected.setTopics(topics);

        Mockito.when(this.filter.getSelectedNewsgroup()).thenReturn(selected);
        Mockito.when(this.filter.getTopicsFilter()).thenReturn(topics);

        final MessageStoreViewer viewer = new MessageStoreViewer(this.controller);
        viewer.notifySelectedNewsGroupObservers();

        final Field rootField = MessageStoreViewer.class.getDeclaredField("topicRoot");
        rootField.setAccessible(true);
        final TreeNode root = (TreeNode) rootField.get(viewer);

        assertEquals(1, root.getChildCount());
        final TreeNode child = (TreeNode) root.getChildAt(0);
        assertEquals(topic1, child.getUserObject());
    }
}
