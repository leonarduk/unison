/**
 * SimpleFileFilterTest
 * 
 * @author ${author}
 * @since 25-Jun-2016
 */
package uk.co.sleonard.unison.utils;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * The Class SimpleFileFilterTest.
 *
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.3.0
 *
 */
public class SimpleFileFilterTest {

	/**
	 * Test accept
	 */
	@Test
	@Ignore
	public void testAcceptFile() {
		try {
			File tempFile = null;
			SimpleFileFilter actual = null;
			tempFile = File.createTempFile("test", null); 	// Create a temp file testXXXXXX.tmp
			actual = new SimpleFileFilter("ext");
			Assert.assertFalse(actual.accept(tempFile));
			actual = new SimpleFileFilter("tmp");
			Assert.assertTrue(actual.accept(tempFile));
			tempFile.deleteOnExit();
			final File mockFile = Mockito.mock(File.class);
			Mockito.when(mockFile.isDirectory()).thenReturn(true);
			Assert.assertTrue(actual.accept(mockFile));
		}
		catch (final IOException e) {
			Assert.fail("ERROR: " + e.getMessage());
		}
	}

	/**
	 * Test getDescription
	 */
	@Test
	public void testGetDescription() {
		final String expected = "test files";
		final SimpleFileFilter actual = new SimpleFileFilter("test");
		Assert.assertEquals(expected, actual.getDescription());
	}

	/**
	 * Test Constructor
	 */
	@Test
	public void testSimpleFileFilterStringArrayString() {
		final SimpleFileFilter actual = new SimpleFileFilter(new String[] { "test", "test2" },
		        "desc");
		Assert.assertNotNull(actual);
	}

}
