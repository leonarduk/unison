/**
 * NewsGroupTest
 *
 * @author ${author}
 * @since 17-Jun-2016
 */
package uk.co.sleonard.unison.datahandling.DAO;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.net.nntp.NewsgroupInfo;
import org.apache.commons.net.nntp.NewsgroupInfoFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class MessageTest.
 *
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.2.0
 *
 */
public class NewsGroupTest {
	/** The nntp ng. */
	private NewsGroup nntpNG;

	/** The expected article count. */
	private final int expectedArticleCount = 10;

	/** The expected first article. */
	private final int expectedFirstMessage = 2;

	/** The expected last article. */
	private final int expectedLastMessage = 79;

	/** The expected newsgroup. */
	private final String expectedNewsgroup = "newsgroup";

	/** The expected posting perm. */
	private final int expectedPostingPerm = 1;

	/**
	 * Create a mock of NewsgroupInfo.
	 *
	 * @return Mock object of NewsgroupInfo
	 */
	private NewsgroupInfo generateNewsgroupInfoMock() {
		return NewsgroupInfoFactory.newsgroupInfo(this.expectedArticleCount,
		        this.expectedFirstMessage, this.expectedLastMessage, "newsgroup",
		        this.expectedPostingPerm);
	}

	/**
	 * Test testGetLastIndexDownloaded.
	 */
	@Test
	public void getLastIndexDownloaded() {
		final int expected = 10;
		final NewsGroup actual = new NewsGroup();
		Assert.assertEquals(0, actual.getLastIndexDownloaded());
		actual.setLastIndexDownloaded(expected);
		Assert.assertEquals(expected, actual.getLastIndexDownloaded());
	}

	/**
	 * Test testGetLastMessage.
	 */
	@Test
	public void getLastMessage() {
		final int expected = 10;
		final NewsGroup actual = new NewsGroup();
		Assert.assertEquals(0, actual.getLastMessage());
		actual.setLastMessage(expected);
		Assert.assertEquals(expected, actual.getLastMessage());
	}

	/**
	 * Setup.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		final NewsgroupInfo ngInfoMock = this.generateNewsgroupInfoMock();
		this.nntpNG = new NewsGroup(ngInfoMock);
	}

	/**
	 * Test compareTo.
	 */
	@Test
	public void testCompareTo() {
		final NewsGroup expected = new NewsGroup();
		expected.setName("ng1");
		final NewsGroup actual = new NewsGroup();
		actual.setName("ng1");
		Assert.assertEquals(0, actual.compareTo(expected));
	}

	/**
	 * Test getArticleCount.
	 */
	@Test
	public void testGetArticleCount() {
		Assert.assertEquals(this.expectedArticleCount, this.nntpNG.getArticleCount());
	}

	/**
	 * Test getFirstMessage.
	 */
	@Test
	public void testGetFirstMessage() {
		Assert.assertEquals(this.expectedFirstMessage, this.nntpNG.getFirstMessage());
		final int expected = 10;
		final NewsGroup actual = new NewsGroup();
		Assert.assertEquals(0, actual.getFirstMessage());
		actual.setFirstMessage(expected);
		Assert.assertEquals(expected, actual.getFirstMessage());
	}

	/**
	 * Test getFullName.
	 */
	@Test
	public void testGetFullName() {
		final String expected = "fullname";
		final NewsGroup actual = new NewsGroup();
		Assert.assertNull(actual.getFullName());
		actual.setFullName(expected);
		Assert.assertEquals(expected, actual.getFullName());
	}

	/**
	 * Test getId.
	 */
	@Test
	public void testGetId() {
		Assert.assertEquals(0, new NewsGroup().getId());
	}

	/**
	 * Test getLastMessage.
	 */
	@Test
	public void testGetLastMessage() {
		Assert.assertEquals(this.expectedLastMessage, this.nntpNG.getLastMessage());
	}

	/**
	 * Test getLastMessageTotal.
	 */
	@Test
	public void testGetLastMessageTotal() {
		final int expected = 10;
		final NewsGroup actual = new NewsGroup();
		Assert.assertEquals(0, actual.getLastMessageTotal());
		actual.setLastMessageTotal(expected);
		Assert.assertEquals(expected, actual.getLastMessageTotal());
	}

	/**
	 * Test getMessages.
	 */
	@Test
	public void testGetMessages() {
		final Set<Message> expected = new HashSet<>(0);
		final NewsGroup actual = new NewsGroup();
		Assert.assertEquals(expected.size(), actual.getMessages().size());
		expected.add(new Message());
		actual.setMessages(expected);
		Assert.assertEquals(expected.size(), actual.getMessages().size());
	}

	/**
	 * Test getName.
	 */
	@Test
	public void testGetName() {
		final String expected = "myname";
		final NewsGroup actual = new NewsGroup();
		Assert.assertNull(actual.getName());
		actual.setName(expected);
		Assert.assertEquals(expected, actual.getName());
	}

	/**
	 * Test getParentNewsGroup.
	 */
	@Test
	public void testGetParentNewsGroup() {
		final NewsGroup expected = new NewsGroup("newsname");
		final NewsGroup actual = new NewsGroup();
		Assert.assertNull(actual.getParentNewsGroup());
		actual.setParentNewsGroup(expected);
		Assert.assertEquals(expected.getName(), actual.getParentNewsGroup().getName());
	}

	/**
	 * Test getTopics.
	 */
	@Test
	public void testGetTopics() {
		final Set<Topic> expected = new HashSet<>(0);
		final NewsGroup actual = new NewsGroup();
		Assert.assertEquals(expected.size(), actual.getTopics().size());
		expected.add(new Topic());
		actual.setTopics(expected);
		Assert.assertEquals(expected.size(), actual.getTopics().size());
	}

	/**
	 * Test hashCode
	 */
	public void testHashCode() {
		final TestNewsGroup actual1 = new TestNewsGroup();
		final TestNewsGroup actual2 = new TestNewsGroup();
		actual1.setIdSuper(5);
		actual2.setIdSuper(93);
		Assert.assertTrue(actual1.hashCode() != actual2.hashCode());
	}

	/**
	 * Test isLastNode.
	 */
	@Test
	public void testIsLastNode() {
		final boolean expected = true;
		final NewsGroup actual = new NewsGroup();
		Assert.assertFalse(actual.isLastNode());
		actual.setLastNode(expected);
		Assert.assertEquals(expected, actual.isLastNode());
	}

	/**
	 * Test toString.
	 */
	@Test
	public void testToString() {
		final String expected = new String("fullname");
		final NewsGroup actual = new NewsGroup();
		actual.setFullName("fullname");
		Assert.assertEquals(expected, actual.toString());
	}

}

/**
 * Extends NewsGroup to change setId(protected)
 *
 * @author Elton <elton_12_nunes@hotmail.com>
 *
 */
@SuppressWarnings("serial")
class TestNewsGroup extends NewsGroup {
	void setIdSuper(final int id) {
		super.setId(id);
	}
}
