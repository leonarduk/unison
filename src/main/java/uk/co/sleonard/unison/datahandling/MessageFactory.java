package uk.co.sleonard.unison.datahandling;

import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.Topic;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.input.NewsArticle;
import uk.co.sleonard.unison.utils.StringUtils;

import java.io.IOException;
import java.util.HashSet;

/**
 * Factory responsible for building {@link Message} entities from downloaded
 * {@link NewsArticle} instances.
 */
public class MessageFactory {

    public Message createMessage(final NewsArticle article, final Topic topic, final UsenetUser poster)
            throws UNISoNException {
        byte[] body = null;
        if (null != article.getContent()) {
            try {
                body = StringUtils.compress(article.getContent().toString());
            } catch (final IOException e) {
                throw new UNISoNException("Failed to compress message", e);
            }
        }

        return new Message(
                article.getDate(),
                article.getArticleID(),
                article.getSubject(),
                poster,
                topic,
                new HashSet<>(),
                article.getReferences(),
                body);
    }
}

