/**
 * MessageTest
 *
 * @author ${author}
 * @since 20-Jun-2016
 */
package uk.co.sleonard.unison.datahandling.DAO;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * The Class MessageTest.
 *
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.2.0
 */
public class MessageTest {

    /**
     * Test Constructors
     */
    @Test
    public void testConstructors() {
        Message actual = null;
        final Date expected1 = Calendar.getInstance().getTime();
        final String expected2 = "usenetMessage";
        final String expected3 = "subject";
        final UsenetUser expected4 = new UsenetUser();
        final Topic expected5 = new Topic();
        final Set<NewsGroup> expected6 = new HashSet<>();
        final String expected7 = "refMess";
        final byte[] expected8 = {10, 10, 10};
        actual = new Message(expected1, expected2, expected3, expected4, expected5, expected6,
                expected7, expected8);

        Assert.assertEquals(expected1, actual.getDateCreated());
        Assert.assertEquals(expected2, actual.getUsenetMessageID());
        Assert.assertEquals(expected3, actual.getSubject());
        Assert.assertEquals(expected4, actual.getPoster());
        Assert.assertEquals(expected5, actual.getTopic());
        Assert.assertEquals(expected6, actual.getNewsgroups());
        Assert.assertEquals(expected7, actual.getReferencedMessages());
        Assert.assertArrayEquals(expected8, actual.getMessageBody());

        actual = new Message(expected1, expected2, expected3, expected4, expected5, expected6,
                expected7, expected8);
        Assert.assertEquals(expected1, actual.getDateCreated());
        Assert.assertEquals(expected2, actual.getUsenetMessageID());
        Assert.assertEquals(expected3, actual.getSubject());
        Assert.assertEquals(expected4, actual.getPoster());
        Assert.assertEquals(expected5, actual.getTopic());
        Assert.assertArrayEquals(expected8, actual.getMessageBody());
    }

    @Test
    public void testEqualsAndHashCode() {
        final Date dateCreated = new Date();
        final String usenetMessageID = "ID1";
        final String subject = "Bad day";
        final UsenetUser poster = new UsenetUser();
        final Topic topic = new Topic();
        final Set<NewsGroup> newsgroups = new HashSet<>();
        final String referencedMessages = "ref";
        final byte[] messageBody = {1, 2, 3};
        final Message m1 = new Message(dateCreated, usenetMessageID, subject, poster, topic,
                newsgroups, referencedMessages, messageBody);
        final Message m2 = new Message(dateCreated, usenetMessageID, subject, poster, topic,
                new HashSet<>(newsgroups), referencedMessages,
                Arrays.copyOf(messageBody, messageBody.length));

        Assert.assertEquals(m1, m2);
        Assert.assertEquals(m1.hashCode(), m2.hashCode());

        m2.setSubject("Different");
        Assert.assertNotEquals(m1, m2);
    }

    /**
     * Test getDateCreated.
     */
    @Test
    public void testGetDateCreated() {
        final Date expected = Calendar.getInstance().getTime();
        final Message actual = new Message();
        Assert.assertNull(actual.getDateCreated());
        actual.setDateCreated(expected);
        Assert.assertEquals(0, expected.compareTo(actual.getDateCreated()));
    }

    /**
     * Test getId.
     */
    @Test
    public void testGetId() {
        Assert.assertEquals(0, new Message().getId());
    }

    /**
     * Test getMessageBody.
     */
    @Test
    public void testGetMessageBody() {
        final byte[] expecteds = {0, 1, 2, 3, 4};
        final Message actuals = new Message();
        Assert.assertNull(actuals.getMessageBody());
        actuals.setMessageBody(expecteds);
        Assert.assertArrayEquals(expecteds, actuals.getMessageBody());
    }

    /**
     * Test getNewsgroups.
     */
    @Test
    public void testGetNewsgroups() {
        final Set<NewsGroup> expected = new HashSet<>();
        final Message actual = new Message();
        Assert.assertEquals(expected.size(), actual.getNewsgroups().size());
        expected.add(new NewsGroup());
        actual.setNewsgroups(expected);
        Assert.assertEquals(expected.size(), actual.getNewsgroups().size());
    }

    /**
     * Test getPoster.
     */
    @Test
    public void testGetPoster() {
        final UsenetUser expected = new UsenetUser();
        final Message actual = new Message();
        Assert.assertNull(actual.getPoster());
        actual.setPoster(expected);
        Assert.assertEquals(expected, actual.getPoster());
    }

    /**
     * Test getReferencedMessages.
     */
    @Test
    public void testGetReferencedMessages() {
        final String expected = new String("refMess");
        final Message actual = new Message();
        Assert.assertNull(actual.getReferencedMessages());
        actual.setReferencedMessages(expected);
        Assert.assertEquals(expected, actual.getReferencedMessages());
    }

    /**
     * Test getSubject.
     */
    @Test
    public void testGetSubject() {
        final String expected = new String("subject");
        final Message actual = new Message();
        Assert.assertNull(actual.getSubject());
        actual.setSubject(expected);
        Assert.assertEquals(expected, actual.getSubject());
    }

    /**
     * Test getTopic.
     */
    @Test
    public void testGetTopic() {
        final Topic expected = new Topic();
        expected.setSubject("sub");
        final Message actual = new Message();
        Assert.assertNull(actual.getTopic());
        actual.setTopic(expected);
        Assert.assertEquals(expected.getSubject(), actual.getTopic().getSubject());
    }

    /**
     * Test getUsenetMessageId.
     */
    @Test
    public void testGetUsenetMessageID() {
        final String expected = new String("usenetMessId");
        final Message actual = new Message();
        Assert.assertNull(actual.getUsenetMessageID());
        actual.setUsenetMessageID(expected);
        Assert.assertEquals(expected, actual.getUsenetMessageID());
    }

    /**
     * Test hashCode
     */
    @Test
    public void testHashCode() {
        final TestMessage actual1 = new TestMessage();
        final TestMessage actual2 = new TestMessage();
        actual1.setSubject("abc");
        actual2.setSubject("def");
        Assert.assertTrue(actual1.hashCode() != actual2.hashCode());
    }

    /**
     * Test toString.
     */
    @Test
    public void testToString() {
        final String expected = "Subject:null";
        final Message actual = new Message();
        actual.setSubject("Subject");
        Assert.assertEquals(expected, actual.toString());
    }

}

/**
 * Extends Message to change setId(protected)
 *
 * @author Elton <elton_12_nunes@hotmail.com>
 */
@SuppressWarnings("serial")
class TestMessage extends Message {
    @Override
    public void setSubject(final String Subject) {
        super.setSubject(Subject);
    }
}
