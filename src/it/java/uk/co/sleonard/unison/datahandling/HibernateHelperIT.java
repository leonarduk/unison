/**
 * HibernateHelperIT
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.datahandling;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.input.HeaderDownloadWorkerIT;
import uk.co.sleonard.unison.input.NewsArticle;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class HibernateHelperIT {

	@Test
	public final void testHibernateDataArticle() throws UNISoNException, IOException {
		final HibernateHelper helper = new HibernateHelper(null);

		final Session session = helper.getHibernateSession();

		final LinkedBlockingQueue<NewsArticle> queue = HeaderDownloadWorkerIT
		        .populateQueueWithOneRealMessage();
		final NewsArticle article = queue.poll();
		Assert.assertNotNull(article);
		// Assert.assertNotNull(helper.findUsenetUser(article, session));
//		Assert.assertTrue(helper.hibernateData(article, session));
                final UsenetUser user = helper
                        .findUsenetUser(UsenetUserHelper.parseFromField(article), session);
//		Assert.assertNotNull(user);
//		final Message message = helper.createMessage(article, null, user);
//		final Message findMessage = helper.findMessage(message, session);
//		Assert.assertNotNull(findMessage);
//		Assert.assertEquals(message.getSubject(), findMessage.getSubject());
	}

}
