package uk.co.sleonard.unison.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.junit.Test;

import uk.co.sleonard.unison.UNISoNException;

/**
 * The Class StringUtilsTest.
 * 
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.2.0
 *
 */
public class StringUtilsTest {

	/**
	 * Test convert string to list.
	 */
	@Test
	public void testConvertStringToList() {
		Vector<String> list = StringUtils.convertStringToList("string-to-list-test", "-");
		assertEquals(4, list.size());
	}

	/**
	 * Test convert string to list if null.
	 */
	@Test
	public void testConvertStringToListIfNull() {
		final Vector<String> listEmpty = new Vector<String>();
		Vector<String> list = StringUtils.convertStringToList(null, "");
		assertEquals(listEmpty, list);
	}

	/**
	 * Test convert commas to list.
	 */
	@Test
	public void testConvertCommasToList() {
		List<String> list = StringUtils.convertCommasToList("comma, separated, string");
		assertEquals(3, list.size());
	}

	/**
	 * Test compress decompress.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testCompressDecompress() throws IOException {
		byte[] compress = StringUtils.compress("message");
		assertEquals(56, compress.length);
		String decompress = StringUtils.decompress(compress);
		assertEquals("message", decompress);
	}

	/**
	 * Test decompress if null.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testDecompressIfNull() throws IOException {
		String decompress = StringUtils.decompress(null);
		assertNull(decompress);
	}

	/**
	 * Test join.
	 */
	@Test
	public void testJoin() {
		String[] stringArray = { "string", "array", "test", "join" };
		String stringJoined = StringUtils.join(stringArray, ".");
		assertEquals("string.array.test.join", stringJoined);
	}

	/**
	 * Test loadServerList
	 */
	@Test
        public void testLoadServerList() {
                String[] actual = StringUtils.loadServerList();
                assertNotNull(actual);
                assertTrue(actual.length > 0);
        }

        /**
         * Test loadServerList with an input stream.
         *
         * @throws IOException
         *             if something goes wrong
         */
        @Test
        public void testLoadServerListFromStream() throws IOException {
                try (ByteArrayInputStream in = new ByteArrayInputStream(
                                "servers=host1,host2".getBytes(StandardCharsets.UTF_8))) {
                        String[] servers = StringUtils.loadServerList(in);
                        assertEquals(2, servers.length);
                        assertEquals("host1", servers[0]);
                        assertEquals("host2", servers[1]);
                }
        }

        /**
         * Test loadServerList with a path.
         *
         * @throws IOException
         *             if something goes wrong
         */
        @Test
        public void testLoadServerListFromPath() throws IOException {
                Path temp = Files.createTempFile("servers", ".properties");
                Files.write(temp, "servers=a,b".getBytes(StandardCharsets.UTF_8));
                try {
                        String[] servers = StringUtils.loadServerList(temp);
                        assertEquals(2, servers.length);
                }
                finally {
                        Files.deleteIfExists(temp);
                }
        }

	/**
	 * Test stringToDate
	 */
	@Test
	public void testStringToDate() throws UNISoNException {
		Date actual = null;
		String expected = null;
		assertEquals(expected, StringUtils.stringToDate(null));
		assertEquals(expected, StringUtils.stringToDate(""));

		// 20160611 -> 11 Jun 2016
		actual = StringUtils.stringToDate("20160611");
		assertTrue(actual.toString().contains("11"));
		assertTrue(actual.toString().contains("Jun"));
		assertTrue(actual.toString().contains("2016"));
		// 13012015 -> 13 Jan 2015
		actual = StringUtils.stringToDate("13012015");
		assertTrue(actual.toString().contains("13"));
		assertTrue(actual.toString().contains("Jan"));
		assertTrue(actual.toString().contains("2015"));
		// 12201628 -> 28 Dec 2016
		actual = StringUtils.stringToDate("12201628");
		assertTrue(actual.toString().contains("28"));
		assertTrue(actual.toString().contains("Dec"));
		assertTrue(actual.toString().contains("2016"));

		// 12/05/1994 -> 12 May 1994
		for (String separator : StringUtils.DATE_SEPARATORS) {
			actual = StringUtils.stringToDate("12" + separator + "05" + separator + "1994");
			assertTrue(actual.toString().contains("12"));
			assertTrue(actual.toString().contains("May"));
			assertTrue(actual.toString().contains("1994"));

		}
		// 2010/12/05 -> 12 May 2010
		for (String separator : StringUtils.DATE_SEPARATORS) {
			actual = StringUtils.stringToDate("2010" + separator + "12" + separator + "05");
			assertTrue(actual.toString().contains("05"));
			assertTrue(actual.toString().contains("Dec"));
			assertTrue(actual.toString().contains("2010"));

		}
		// 12/2014/05 -> 05 Dec 2014
		for (String separator : StringUtils.DATE_SEPARATORS) {
			actual = StringUtils.stringToDate("12" + separator + "2014" + separator + "05");
			assertTrue(actual.toString().contains("05"));
			assertTrue(actual.toString().contains("Dec"));
			assertTrue(actual.toString().contains("2014"));

		}

		// Sun, 18 Jan 2015 15:17:37 -0700 -> 12 May 1994
		actual = StringUtils.stringToDate("Sun, 18 Jan 2015 15:17:37 -0700");
		assertTrue(actual.toString().contains("18"));
		assertTrue(actual.toString().contains("Jan"));
		assertTrue(actual.toString().contains("2015"));

		// 23 Jan 2015 16:28:20 GMT -> 23 Jan 2015
		actual = StringUtils.stringToDate("23 Jan 2015 16:28:20 GMT");
		assertTrue(actual.toString().contains("23"));
		assertTrue(actual.toString().contains("Jan"));
		assertTrue(actual.toString().contains("2015"));

		// Sun, 18 Jan 2015 23:40:56 +0000 (UTC) -> 18 Jan 2015
		actual = StringUtils.stringToDate("Sun, 18 Jan 2015 23:40:56 +0000 (UTC)");
		assertTrue(actual.toString().contains("18"));
		assertTrue(actual.toString().contains("Jan"));
		assertTrue(actual.toString().contains("2015"));
	}

	/**
	 * Test stringToDate with String length 8 throw UNISoNException
	 */
	@Test(expected = UNISoNException.class)
	public void testStringToDateExceptionLength8() throws UNISoNException {
		StringUtils.stringToDate("xxxxxxxx");
	}

	/**
	 * Test stringToDate with String length 10 throw UNISoNException
	 */
        @Test(expected = UNISoNException.class)
        public void testStringToDateExceptionLength10() throws UNISoNException {
                StringUtils.stringToDate("xx/xx/xxxx");
        }

        /**
         * Ensure RFC 1123 date strings with a weekday prefix are parsed.
         */
        @Test
        public void testStringToDateRfc1123WithWeekday() throws UNISoNException {
                Date actual = StringUtils.stringToDate("Sun, 18 Jan 2015 23:40:56 +0000");
                assertTrue(actual.toString().contains("18"));
                assertTrue(actual.toString().contains("Jan"));
                assertTrue(actual.toString().contains("2015"));
        }

        /**
         * Ensure RFC 1123 date strings without a weekday prefix are parsed.
         */
        @Test
        public void testStringToDateRfc1123WithoutWeekday() throws UNISoNException {
                Date actual = StringUtils.stringToDate("18 Jan 2015 23:40:56 +0000");
                assertTrue(actual.toString().contains("18"));
                assertTrue(actual.toString().contains("Jan"));
                assertTrue(actual.toString().contains("2015"));
        }

}
