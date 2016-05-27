package uk.co.sleonard.unison.gui.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.mockito.Mockito;

import uk.co.sleonard.unison.gui.SimpleFileFilter;

/**
 * The Class SimpleFileFilterTest.
 * 
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since v1.3.0
 *
 */
public class SimpleFileFilterTest {

	/**
	 * Test Constructor
	 */
	@Test
	public void testSimpleFileFilterStringArrayString() {
		SimpleFileFilter actual = new SimpleFileFilter(new String[] { "test", "test2" }, "desc");
		assertNotNull(actual);
	}

	/**
	 * Test accept
	 */
	@Test
	public void testAcceptFile() {
		try {
			File tempFile = null;
			SimpleFileFilter actual = null;
			tempFile = File.createTempFile("test", null); 	// Create a temp file testXXXXXX.tmp
			actual = new SimpleFileFilter("ext");
			assertFalse(actual.accept(tempFile));
			actual = new SimpleFileFilter("tmp");
			assertTrue(actual.accept(tempFile));
			tempFile.deleteOnExit();
			File mockFile = Mockito.mock(File.class);
			Mockito.when(mockFile.isDirectory()).thenReturn(true);
			assertTrue(actual.accept(mockFile));
		}
		catch (IOException e) {
			fail("ERROR: " + e.getMessage());
		}
	}

	/**
	 * Test getDescription
	 */
	@Test
	public void testGetDescription() {
		String expected = "test files";
		SimpleFileFilter actual = new SimpleFileFilter("test");
		assertEquals(expected, actual.getDescription());
	}

}
