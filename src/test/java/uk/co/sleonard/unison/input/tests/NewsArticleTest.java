package uk.co.sleonard.unison.input.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import uk.co.sleonard.unison.gui.UNISoNException;
import uk.co.sleonard.unison.input.NewsArticle;

import com.ibm.icu.util.Calendar;

/**
 * The Class NewsArticleTest.
 */
public class NewsArticleTest {

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
	 */
	@Test(expected = NullPointerException.class)
	public void testGetArticleIdThrowException() {
		NewsArticle actual = new NewsArticle();
		actual.setArticleId(null);
	}

	/**
	 * Test getContent.
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
	 */
	@Test
	@Ignore
	public void testGetDate() {
		fail("Not yet implemented");
	}

	/**
	 * Test getFrom.
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
	 */
	@Test(expected=NullPointerException.class)
	public void testGetFromThrowException() {
		NewsArticle actual = new NewsArticle();
		actual.setFrom(null);
	}

	/**
	 * Test getNewsgroupsList.
	 * @throws UNISoNException 
	 */
	@Test
	public void testGetNewsgroupsList() throws UNISoNException {
		String newsgroups = "newsgroup1,newsgroup2,newsgroup3";
		NewsArticle test = new NewsArticle("article", 0, Calendar.getInstance().getTime(),
											"from", null, null, null, newsgroups, null);
		List<String> actual = test.getNewsgroupsList();
		assertEquals(3, actual.size());
	}

	/**
	 * Test getPostingHost.
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
	 */
	@Ignore
	@Test
	public void testGetReferences() {
		fail("Not yet implemented");
	}

	/**
	 * Test getSubject.
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
	 */
	@Ignore
	@Test
	public void testToString() {
		fail("Not yet implemented");
	}
	
}
