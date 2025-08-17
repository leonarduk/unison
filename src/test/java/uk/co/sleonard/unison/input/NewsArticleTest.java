/**
 * NewsArticleTest
 *
 * @author ${author}
 * @since 20-Jun-2016
 */
package uk.co.sleonard.unison.input;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.co.sleonard.unison.UNISoNException;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

/**
 * The Class NewsArticleTest.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 */
public class NewsArticleTest {

    /**
     * Test getArticleId.
     */
    @Test
    public void testGetArticleId() {
        final String expected = "articleId";
        final NewsArticle actual = new NewsArticle();
        Assertions.assertNull(actual.getArticleID());
        actual.setArticleId(expected);
        Assertions.assertEquals(expected, actual.getArticleID());
    }

    /**
     * Test getArticleId expected NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void testGetArticleIdThrowException() {
        final NewsArticle actual = new NewsArticle();
        actual.setArticleId(null);
    }

    /**
     * Test getContent.
     */
    @Test
    public void testGetContent() {
        final String expected = "content";
        final NewsArticle actual = new NewsArticle();
        Assertions.assertNull(actual.getContent());
        actual.setContent(expected);
        Assertions.assertEquals(expected, actual.getContent());
    }

    /**
     * Test getDate.
     *
     * @throws ParseException  Signals that an parse exception in application has occurred..
     * @throws UNISoNException Signals that an exception in application has occurred..
     */
    @Test
    public void testGetDate() throws ParseException, UNISoNException {
        final String dateExpected = "Wed, 11 May 2016 13:10:00 GMT";
        final NewsArticle test = new NewsArticle();
        Assertions.assertNull(test.getDate());
        test.setDate(dateExpected);
        final Calendar actual = Calendar.getInstance();
        actual.setTime(test.getDate());
        Assertions.assertEquals(4, actual.get(Calendar.MONTH));
    }

    /**
     * Test getFrom.
     */
    @Test
    public void testGetFrom() {
        final String expected = "user";
        final NewsArticle actual = new NewsArticle();
        Assertions.assertNull(actual.getFrom());
        actual.setFrom(expected);
        Assertions.assertEquals(expected, actual.getFrom());
    }

    /**
     * Test getFrom expected NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void testGetFromThrowException() {
        final NewsArticle actual = new NewsArticle();
        actual.setFrom(null);
    }

    /**
     * Test getNewsgroupsList.
     *
     * @throws UNISoNException the UNI so n exception
     */
    @Test
    public void testGetNewsgroupsList() throws UNISoNException {
        final String newsgroups = "newsgroup1,newsgroup2,newsgroup3";
        final NewsArticle test = new NewsArticle("article", 0, Calendar.getInstance().getTime(),
                "from", null, null, null, newsgroups, null);
        final List<String> actual = test.getNewsgroupsList();
        Assertions.assertEquals(3, actual.size());
    }

    /**
     * Test getPostingHost.
     */
    @Test
    public void testGetPostingHost() {
        final String expected = "posting";
        final NewsArticle actual = new NewsArticle();
        Assertions.assertNull(actual.getPostingHost());
        actual.setPostingHost(expected);
        Assertions.assertEquals(expected, actual.getPostingHost());
    }

    /**
     * Test getReferences.
     */
    @Test
    public void testGetReferences() {
        final String expected = "references";
        final NewsArticle actual = new NewsArticle();
        Assertions.assertNull(actual.getReferences());
        actual.setReferences(expected);
        Assertions.assertEquals(expected, actual.getReferences());
    }

    /**
     * Test getRefencesList.
     */
    @Test
    public void testGetReferencesList() {
        final NewsArticle test = new NewsArticle();
        test.setReferences("<effrl8$hmn$2@emma.aioe.org> <effupe$hr0$1@netlx020.civ.utwente.nl>");
        final List<String> actual = test.getReferencesList();
        Assertions.assertEquals(2, actual.size());
    }

    /**
     * Test getSubject.
     */
    @Test
    public void testGetSubject() {
        final String expected = "subject";
        final NewsArticle actual = new NewsArticle();
        Assertions.assertNull(actual.getSubject());
        actual.setSubject(expected);
        Assertions.assertEquals(expected, actual.getSubject());
    }

    /**
     * Test isFullHeader.
     */
    @Test
    public void testIsFullHeader() {
        final NewsArticle actual = new NewsArticle();
        Assertions.assertFalse(actual.isFullHeader());
        actual.setPostingHost("postingHost");
        Assertions.assertTrue(actual.isFullHeader());
    }

    /**
     * Test toString.
     */
    @Test
    public void testToString() {
        final NewsArticle test = new NewsArticle();
        test.setArticleNumber(100);
        Assertions.assertTrue(test.toString().contains("Number: 100"));
    }

}
