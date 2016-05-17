package uk.co.sleonard.unison.input.tests;

import static org.junit.Assert.*;

import static org.powermock.api.mockito.PowerMockito.*;

import org.apache.commons.net.nntp.NewsgroupInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import uk.co.sleonard.unison.input.NNTPNewsGroup;

/**
 * The Class NNTPNewsGroupTest.
 */
@RunWith(value = PowerMockRunner.class)
@PrepareForTest(value = { NewsgroupInfo.class })
public class NNTPNewsGroupTest {

	private NNTPNewsGroup nntpNG;

	private int expectedArticleCount = 10;
	private int expectedFirstArticle = 2;
	private int expectedLastArticle = 79;
	private String expectedNewsgroup = "newsgroup";
	private int expectedPostingPerm = 1;

	/**
	 * Setup.
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
	 * Create a mock of NewsgroupInfo
	 * 
	 * @return Mock object of NewsgroupInfo
	 */
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
