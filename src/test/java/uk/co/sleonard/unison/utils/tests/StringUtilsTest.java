package uk.co.sleonard.unison.utils.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.junit.Test;

import uk.co.sleonard.unison.utils.StringUtils;

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
}
