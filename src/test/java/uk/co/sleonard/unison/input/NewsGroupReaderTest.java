/**
 * NewsGroupReaderTest
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.input;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * The Class NewsGroupReaderTest.
 *
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.2.0
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
    public void setUp() {
        this.newsGroup = new NewsGroupReader(new NewsClientImpl());
    }

    /**
     * Test getMessagesSkipped.
     */
    @Test
    public void testGetMessagesSkipped() {
        int actual = 0;
        final int expected = 1;

        actual = this.newsGroup.getMessagesSkipped();
        Assert.assertEquals(0, actual);
        this.newsGroup.incrementMessagesSkipped();
        actual = this.newsGroup.getMessagesSkipped();
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test getMessagesStored.
     */
    @Test
    public void testGetMessagesStored() {
        int actual = 0;
        final int expected = 1;

        actual = this.newsGroup.getMessagesStored();
        Assert.assertEquals(0, actual);
        this.newsGroup.incrementMessagesStored();
        actual = this.newsGroup.getMessagesStored();
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test gedNumberOfMessages.
     */
    @Test
    public void testGetNumberOfMessages() {
        final NewsClient mockNewsClient = Mockito.mock(NewsClient.class);
        Mockito.when(mockNewsClient.getMessageCount()).thenReturn(10);
        this.newsGroup.setClient(mockNewsClient);
        Assert.assertEquals(10, this.newsGroup.getNumberOfMessages());
    }

    /**
     * Ensure the supplied NewsClient is used by the reader.
     */
    @Test
    public void testSuppliedNewsClientIsUsed() {
        final NewsClient mockClient = Mockito.mock(NewsClient.class);
        final NewsGroupReader reader = new NewsGroupReader(mockClient);
        reader.getNumberOfMessages();
        Mockito.verify(mockClient).getMessageCount();
    }

    /**
     * Test getMessageCount and setMessageCount.
     */
    @Test
    public void testGetSetMessageCount() {
        final int expected = 5;
        Assert.assertEquals(0, this.newsGroup.getMessageCount());
        this.newsGroup.setMessageCount(expected);
        Assert.assertEquals(expected, this.newsGroup.getMessageCount());
    }

    /**
     * Test incrementMessagesSkipped.
     */
    @Test
    public void testIncrementMessagesSkipped() {
        this.newsGroup.incrementMessagesSkipped();
        this.newsGroup.incrementMessagesSkipped();
        Assert.assertEquals(2, this.newsGroup.getMessagesSkipped());
    }

    /**
     * Test IncrementMessagesStored.
     */
    @Test
    public void testIncrementMessagesStored() {
        this.newsGroup.incrementMessagesStored();
        this.newsGroup.incrementMessagesStored();
        Assert.assertEquals(2, this.newsGroup.getMessagesStored());
    }

    /**
     * Test isConnected.
     */
    @Test
    public void testIsConnected() {
        final boolean actual = this.newsGroup.isConnected();
        Assert.assertFalse(actual);
    }
}
