package uk.co.sleonard.unison.utils.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import com.ibm.icu.util.Calendar;

import uk.co.sleonard.unison.gui.UNISoNException;
import uk.co.sleonard.unison.utils.HttpDateObject;

/**
 * The Class HttpDateObjectTest.
 * 
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
public class HttpDateObjectTest {

	private HttpDateObject hdo;

	/**
	 * SetUp
	 */
	@Before
	public void setUp() {
		this.hdo = HttpDateObject.getParser();
	}

	/**
	 * Test main
	 */
	@Test
	public void testMain() {
		HttpDateObject.main(null);
	}

	/**
	 * Test format
	 */
	@Test
	public void testFormat() {
		Calendar expected = Calendar.getInstance();
		expected.set(2016, Calendar.MAY, 26);
		StringBuffer stb = new StringBuffer();

		this.hdo.format(expected.getTime(), stb, new FieldPosition(0));
		assertTrue(stb.toString().contains("Thu, 26 May 2016"));

		expected.set(Calendar.YEAR, 800);
		this.hdo.format(expected.getTime(), stb, new FieldPosition(0));
		assertTrue(stb.toString().contains("Tue, 26 May 0800"));

		expected.set(Calendar.YEAR, 80);
		this.hdo.format(expected.getTime(), stb, new FieldPosition(0));
		assertTrue(stb.toString().contains("Fri, 26 May 0080"));

		expected.set(Calendar.YEAR, 8);
		this.hdo.format(expected.getTime(), stb, new FieldPosition(0));
		assertTrue(stb.toString().contains("Sat, 26 May 0008"));
	}

	/**
	 * Test parseDate.
	 */
	@Test
	public void testParseDate() {
		Date expected = null;
		try {
			assertNull(this.hdo.parseDate(null));
			assertNull(this.hdo.parseDate(""));
			expected = new GregorianCalendar(2016, Calendar.MAY, 26).getTime();
			assertEquals(expected, this.hdo.parseDate("20160526"));
			assertEquals(expected, this.hdo.parseDate("26/05/2016"));
		}
		catch (ParseException | UNISoNException e) {
			fail("ERROR: " + e.getMessage());
		}
	}

	/**
	 * Test setCalendar expect exception
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testSetCalendar() {
		this.hdo.setCalendar(new GregorianCalendar());
	}

	/**
	 * Test setNumberFormat expect exception
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testSetNumberFormat() {
		this.hdo.setNumberFormat(new DecimalFormat(""));
	}

}
