package uk.co.sleonard.unison.input.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import uk.co.sleonard.unison.gui.UNISoNController;
import uk.co.sleonard.unison.input.NewsClient;
import uk.co.sleonard.unison.input.NewsGroupReader;

/**
 * The Class NewsGroupReaderTest.
 * 
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.0.0
 *
 */
public class NewsGroupReaderTest {

	/** The news group. */
	private NewsGroupReader newsGroup;

	/**
	 * Setup.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		UNISoNController uniController = mock(UNISoNController.class);
		this.newsGroup = new NewsGroupReader(uniController);
	}

	/**
	 * Test getMessagesSkipped.
	 */
	@Test
	public void testGetMessagesSkipped() {
		int actual = 0;
		int expected = 1;

		actual = this.newsGroup.getMessagesSkipped();
		assertEquals(0, actual);
		this.newsGroup.incrementMessagesSkipped();
		actual = this.newsGroup.getMessagesSkipped();
		assertEquals(expected, actual);
	}

	/**
	 * Test getMessagesStored.
	 */
	@Test
	public void testGetMessagesStored() {
		int actual = 0;
		int expected = 1;

		actual = this.newsGroup.getMessagesStored();
		assertEquals(0, actual);
		this.newsGroup.incrementMessagesStored();
		actual = this.newsGroup.getMessagesStored();
		assertEquals(expected, actual);
	}

	/**
	 * Test gedNumberOfMessages.
	 */
	@Test
	public void testGetNumberOfMessages() {
		NewsClient mockNewsClient = mock(NewsClient.class);
		this.newsGroup.client = mockNewsClient;
		when(mockNewsClient.getMessageCount()).thenReturn(10);
		assertEquals(10, this.newsGroup.getNumberOfMessages());
	}

	/**
	 * Test incrementMessagesSkipped.
	 */
	@Test
	public void testIncrementMessagesSkipped() {
		this.newsGroup.incrementMessagesSkipped();
		this.newsGroup.incrementMessagesSkipped();
		assertEquals(2, this.newsGroup.getMessagesSkipped());
	}

	/**
	 * Test IncrementMessagesStored.
	 */
	@Test
	public void testIncrementMessagesStored() {
		this.newsGroup.incrementMessagesStored();
		this.newsGroup.incrementMessagesStored();
		assertEquals(2, this.newsGroup.getMessagesStored());
	}

	/**
	 * Test isConnected.
	 */
	@Test
	public void testIsConnected() {
		boolean actual = false;
		actual = this.newsGroup.isConnected();
		assertFalse(actual);
	}

	/**
	 * Test getMessageCount and setMessageCount.
	 */
	@Test
	public void testGetSetMessageCount() {
		int expected = 5;
		assertEquals(0, this.newsGroup.getMessageCount());
		this.newsGroup.setMessageCount(expected);
		assertEquals(expected, this.newsGroup.getMessageCount());
	}

}
