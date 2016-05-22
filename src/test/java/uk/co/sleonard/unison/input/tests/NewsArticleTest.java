package uk.co.sleonard.unison.input.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.List;

import org.apache.commons.net.nntp.Article;
import org.junit.Test;

import com.ibm.icu.util.Calendar;

import uk.co.sleonard.unison.gui.UNISoNException;
import uk.co.sleonard.unison.input.NewsArticle;

/**
 * The Class NewsArticleTest.
 * 
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
public class NewsArticleTest {

	/**
	 * Test constructor NewsArticle(final Article article).
	 * 
	 * @throws UNISoNException
	 *             Signals that an exception in application has occurred..
	 * @author Elton <elton_12_nunes@hotmail.com>
	 * 
	 */
	@Test
	public void testConstructorArticle() throws UNISoNException {
		Article expected = new Article();
		expected.setArticleId("ART");
		expected.setArticleNumber(100);
		expected.setFrom("from");
		expected.setSubject("subject");
		expected.setDate("Wed, 11 May 2016 13:10:00 GMT");
		NewsArticle actual = new NewsArticle(expected);
		assertEquals(expected.getArticleId(), actual.getArticleId());
		assertEquals(expected.getFrom(), actual.getFrom());
		assertEquals(expected.getSubject(), actual.getSubject());
	}

	/**
	 * Test getRefencesList.
	 */
	@Test
	public void testGetReferencesList() {
		NewsArticle test = new NewsArticle();
		test.setReferences("<effrl8$hmn$2@emma.aioe.org> <effupe$hr0$1@netlx020.civ.utwente.nl>");
		List<String> actual = test.getReferencesList();
		assertEquals(2, actual.size());
	}

	/**
	 * Test isFullHeader.
	 * 
	 * @author Elton <elton_12_nunes@hotmail.com>
	 * 
	 */
	@Test
	public void testIsFullHeader() {
		NewsArticle actual = new NewsArticle();
		assertFalse(actual.isFullHeader());
		actual.setPostingHost("postingHost");
		assertTrue(actual.isFullHeader());
	}

	/**
	 * Test compareTo.
	 * 
	 * @author Elton <elton_12_nunes@hotmail.com>
	 * 
	 */
	@Test
	public void testCompareTo() {
		NewsArticle other = new NewsArticle();
		other.setArticleNumber(2);
		NewsArticle actual = new NewsArticle();
		actual.setArticleNumber(2);
		assertEquals(0, actual.compareTo(other));
		actual.setArticleNumber(1);
		assertEquals(-1, actual.compareTo(other));
		actual.setArticleNumber(3);
		assertEquals(1, actual.compareTo(other));
	}

	/**
	 * Test getArticleId.
	 * 
	 * @author Elton <elton_12_nunes@hotmail.com>
	 * 
	 */
	@Test
	public void testGetArticleId() {
		String expected = "articleId";
		NewsArticle actual = new NewsArticle();
		assertNull(actual.getArticleId());
		actual.setArticleId(expected);
		assertEquals(expected, actual.getArticleId());
	}

	/**
	 * Test getArticleId expected NullPointerException.
	 * 
	 * @author Elton <elton_12_nunes@hotmail.com>
	 * 
	 */
	@Test(expected = NullPointerException.class)
	public void testGetArticleIdThrowException() {
		NewsArticle actual = new NewsArticle();
		actual.setArticleId(null);
	}

	/**
	 * Test getContent.
	 * 
	 * @author Elton <elton_12_nunes@hotmail.com>
	 * 
	 */
	@Test
	public void testGetContent() {
		String expected = "content";
		NewsArticle actual = new NewsArticle();
		assertNull(actual.getContent());
		actual.setContent(expected);
		assertEquals(expected, actual.getContent());
	}

	/**
	 * Test getDate.
	 *
	 * @throws ParseException
	 *             Signals that an parse exception in application has occurred..
	 * @throws UNISoNException
	 *             Signals that an exception in application has occurred..
	 * @author Elton <elton_12_nunes@hotmail.com>
	 * 
	 */
	@Test
	public void testGetDate() throws ParseException, UNISoNException {
		String dateExpected = "Wed, 11 May 2016 13:10:00 GMT";
		NewsArticle test = new NewsArticle();
		assertNull(test.getDate());
		test.setDate(dateExpected);
		Calendar actual = Calendar.getInstance();
		actual.setTime(test.getDate());
		assertEquals(4, actual.get(Calendar.MONTH));
	}

	/**
	 * Test getFrom.
	 * 
	 * @author Elton <elton_12_nunes@hotmail.com>
	 * 
	 */
	@Test
	public void testGetFrom() {
		String expected = "user";
		NewsArticle actual = new NewsArticle();
		assertNull(actual.getFrom());
		actual.setFrom(expected);
		assertEquals(expected, actual.getFrom());
	}

	/**
	 * Test getFrom expected NullPointerException.
	 * 
	 * @author Elton <elton_12_nunes@hotmail.com>
	 * 
	 */
	@Test(expected = NullPointerException.class)
	public void testGetFromThrowException() {
		NewsArticle actual = new NewsArticle();
		actual.setFrom(null);
	}

	/**
	 * Test getNewsgroupsList.
	 *
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	@Test
	public void testGetNewsgroupsList() throws UNISoNException {
		String newsgroups = "newsgroup1,newsgroup2,newsgroup3";
		NewsArticle test = new NewsArticle("article", 0, Calendar.getInstance().getTime(), "from",
		        null, null, null, newsgroups, null);
		List<String> actual = test.getNewsgroupsList();
		assertEquals(3, actual.size());
	}

	/**
	 * Test getPostingHost.
	 * 
	 * @author Elton <elton_12_nunes@hotmail.com>
	 * 
	 */
	@Test
	public void testGetPostingHost() {
		String expected = "posting";
		NewsArticle actual = new NewsArticle();
		assertNull(actual.getPostingHost());
		actual.setPostingHost(expected);
		assertEquals(expected, actual.getPostingHost());
	}

	/**
	 * Test getReferences.
	 * 
	 * @author Elton <elton_12_nunes@hotmail.com>
	 * 
	 */
	@Test
	public void testGetReferences() {
		String expected = "references";
		NewsArticle actual = new NewsArticle();
		assertNull(actual.getReferences());
		actual.setReferences(expected);
		assertEquals(expected, actual.getReferences());
	}

	/**
	 * Test getSubject.
	 * 
	 * @author Elton <elton_12_nunes@hotmail.com>
	 * 
	 */
	@Test
	public void testGetSubject() {
		String expected = "subject";
		NewsArticle actual = new NewsArticle();
		assertNull(actual.getSubject());
		actual.setSubject(expected);
		assertEquals(expected, actual.getSubject());
	}

	/**
	 * Test toString.
	 * 
	 * @author Elton <elton_12_nunes@hotmail.com>
	 * 
	 */
	@Test
	public void testToString() {
		NewsArticle test = new NewsArticle();
		test.setArticleNumber(100);
		assertTrue(test.toString().contains("Number: 100"));
	}

}
