package uk.co.sleonard.unison.gui.generated;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import uk.co.sleonard.unison.UNISoNController;
import uk.co.sleonard.unison.NewsGroupFilter;
import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.utils.TreeNode;

public class MessageStoreViewerTest {

    @Test
    public void testTopicHierarchyFiltersBySelectedGroup() throws Exception {
        System.setProperty("java.awt.headless", "true");

        // Create groups and topics
        NewsGroup group1 = new NewsGroup();
        group1.setFullName("group1");
        Topic topic1 = new Topic();
        topic1.setSubject("topic1");
        topic1.setNewsgroups(new HashSet<>(Arrays.asList(group1)));
        group1.setTopics(new HashSet<>(Arrays.asList(topic1)));

        NewsGroup group2 = new NewsGroup();
        group2.setFullName("group2");
        Topic topic2 = new Topic();
        topic2.setSubject("topic2");
        topic2.setNewsgroups(new HashSet<>(Arrays.asList(group2)));
        group2.setTopics(new HashSet<>(Arrays.asList(topic2)));

        Set<Topic> topicsFilter = new HashSet<>(Arrays.asList(topic1, topic2));

        // Mock controller and dependencies
        NewsGroupFilter filter = Mockito.mock(NewsGroupFilter.class);
        Mockito.when(filter.getTopicsFilter()).thenReturn(topicsFilter);
        HibernateHelper helper = Mockito.mock(HibernateHelper.class);
        Mockito.when(helper.getHibernateSession()).thenReturn(Mockito.mock(Session.class));
        Mockito.when(helper.getText(Mockito.any())).thenReturn("");
        UNISoNController controller = Mockito.mock(UNISoNController.class);
        Mockito.when(controller.helper()).thenReturn(helper);
        Mockito.doNothing().when(controller).switchFiltered(Mockito.anyBoolean());
        Mockito.when(controller.getFilter()).thenReturn(filter);

        try (MockedStatic<UNISoNController> controllerStatic = Mockito.mockStatic(UNISoNController.class)) {
            controllerStatic.when(UNISoNController::getInstance).thenReturn(controller);

            MessageStoreViewer viewer = new MessageStoreViewer();

            // Select first group
            Mockito.when(filter.getSelectedNewsgroup()).thenReturn(group1);
            viewer.notifySelectedNewsGroupObservers();
            Field topicRootField = MessageStoreViewer.class.getDeclaredField("topicRoot");
            topicRootField.setAccessible(true);
            TreeNode topicRoot = (TreeNode) topicRootField.get(viewer);
            assertEquals(1, topicRoot.getChildCount());
            assertEquals(topic1, ((TreeNode) topicRoot.getChildAt(0)).getUserObject());

            // Select second group
            Mockito.when(filter.getSelectedNewsgroup()).thenReturn(group2);
            viewer.notifySelectedNewsGroupObservers();
            assertEquals(1, topicRoot.getChildCount());
            assertEquals(topic2, ((TreeNode) topicRoot.getChildAt(0)).getUserObject());
        }
    }
}
