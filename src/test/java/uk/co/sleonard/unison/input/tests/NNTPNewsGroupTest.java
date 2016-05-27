package uk.co.sleonard.unison.input.tests;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import org.apache.commons.net.nntp.NewsgroupInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import uk.co.sleonard.unison.input.NNTPNewsGroup;

/**
 * The Class NNTPNewsGroupTest.
 * 
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.2.0
 *
 */
@RunWith(value = PowerMockRunner.class)
@PrepareForTest(value = { NewsgroupInfo.class })
public class NNTPNewsGroupTest {

	/** The nntp ng. */
	private NNTPNewsGroup nntpNG;

	/** The expected article count. */
	private int expectedArticleCount = 10;

	/** The expected first article. */
	private int expectedFirstArticle = 2;

	/** The expected last article. */
	private int expectedLastArticle = 79;

	/** The expected newsgroup. */
	private String expectedNewsgroup = "newsgroup";

	/** The expected posting perm. */
	private int expectedPostingPerm = 1;

	/**
	 * Setup.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		NewsgroupInfo ngInfoMock = generateNewsgroupInfoMock();
		this.nntpNG = new NNTPNewsGroup(ngInfoMock);
	}

	/**
	 * Test toString.
	 */
	@Test
	public void testToString() {
		String expected = "newsgroup (10)";
		assertEquals(expected, nntpNG.toString());
	}

	/**
	 * Test compareTo.
	 */
	@Test
	public void testCompareTo() {
		NewsgroupInfo ngInfo = mock(NewsgroupInfo.class);
		when(ngInfo.getNewsgroup()).thenReturn("newsgroup");
		NNTPNewsGroup nntpNG = new NNTPNewsGroup(ngInfo);

		assertEquals(0, this.nntpNG.compareTo(nntpNG));
	}

	/**
	 * Test getArticleCount.
	 */
	@Test
	public void testGetArticleCount() {
		assertEquals(expectedArticleCount, this.nntpNG.getArticleCount());
	}

	/**
	 * Test getFirstArticle.
	 */
	@Test
	public void testGetFirstArticle() {
		assertEquals(expectedFirstArticle, this.nntpNG.getFirstArticle());
	}

	/**
	 * Test getLastArticle.
	 */
	@Test
	public void testGetLastArticle() {
		assertEquals(expectedLastArticle, this.nntpNG.getLastArticle());
	}

	/**
	 * Test getNewsGroup.
	 */
	@Test
	public void testGetNewsgroup() {
		assertEquals(expectedNewsgroup, this.nntpNG.getNewsgroup());
	}

	/**
	 * Test getPostingPermission.
	 */
	@Test
	public void testGetPostingPermission() {
		assertEquals(expectedPostingPerm, this.nntpNG.getPostingPermission());
	}

	/**
	 * Create a mock of NewsgroupInfo.
	 *
	 * @return Mock object of NewsgroupInfo
	 */
	@SuppressWarnings("deprecation")
	private NewsgroupInfo generateNewsgroupInfoMock() {
		NewsgroupInfo ngInfoMock = mock(NewsgroupInfo.class);
		when(ngInfoMock.getArticleCount()).thenReturn(expectedArticleCount);
		when(ngInfoMock.getFirstArticle()).thenReturn(expectedFirstArticle);
		when(ngInfoMock.getLastArticle()).thenReturn(expectedLastArticle);
		when(ngInfoMock.getNewsgroup()).thenReturn("newsgroup");
		when(ngInfoMock.getPostingPermission()).thenReturn(expectedPostingPerm);
		return ngInfoMock;
	}

}
