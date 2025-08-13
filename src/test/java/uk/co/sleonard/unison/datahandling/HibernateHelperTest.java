package uk.co.sleonard.unison.datahandling;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.input.NewsArticle;

/**
 * Tests for {@link HibernateHelper}.
 */
public class HibernateHelperTest {

    @Test
    public void testCreateMessageUsesArticleIdAndMutableNewsgroups() throws UNISoNException {
        HibernateHelper helper = new HibernateHelper(null);

        NewsArticle article = new NewsArticle(
                "article-id",
                1,
                new Date(),
                "from@example.com",
                "Test subject",
                "",
                "Body",
                null,
                "host");

        Topic topic = new Topic();
        UsenetUser poster = new UsenetUser("name", "email@example.com", "127.0.0.1", "M", null);

        Message message = helper.createMessage(article, topic, poster);

        assertEquals(article.getArticleID(), message.getUsenetMessageID());
        assertTrue(message.getNewsgroups().isEmpty());

        NewsGroup group = new NewsGroup();
        group.setName("test.group");
        assertTrue(message.getNewsgroups().add(group));
        assertEquals(1, message.getNewsgroups().size());
    }
}

