/**
 * HttpDateObjectTest
 * 
 * @author ${author}
 * @since 04-Jun-2016
 */
package uk.co.sleonard.unison.utils;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.ibm.icu.util.Calendar;

import uk.co.sleonard.unison.UNISoNException;

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
	 * Test format
	 */
	@Test
	@Ignore
	public void testFormat() {
		final Calendar expected = Calendar.getInstance();
		expected.set(2016, Calendar.MAY, 26);
		final StringBuffer stb = new StringBuffer();

		this.hdo.format(expected.getTime(), stb, new FieldPosition(0));
		Assert.assertTrue(stb.toString().contains("Thu, 26 May 2016"));

		expected.set(Calendar.YEAR, 800);
		this.hdo.format(expected.getTime(), stb, new FieldPosition(0));
		Assert.assertTrue(stb.toString().contains("Tue, 26 May 0800"));

		expected.set(Calendar.YEAR, 80);
		this.hdo.format(expected.getTime(), stb, new FieldPosition(0));
		Assert.assertTrue(stb.toString().contains("Fri, 26 May 0080"));

		expected.set(Calendar.YEAR, 8);
		this.hdo.format(expected.getTime(), stb, new FieldPosition(0));
		Assert.assertTrue(stb.toString().contains("Sat, 26 May 0008"));
	}

	/**
	 * Test parseDate.
	 */
	@Test
	public void testParseDate() {
		Date expected = null;
		try {
			Assert.assertNull(this.hdo.parseDate(null));
			Assert.assertNull(this.hdo.parseDate(""));
			expected = new GregorianCalendar(2016, Calendar.MAY, 26).getTime();
			Assert.assertEquals(expected, this.hdo.parseDate("20160526"));
			Assert.assertEquals(expected, this.hdo.parseDate("26/05/2016"));
		}
		catch (ParseException | UNISoNException e) {
			Assert.fail("ERROR: " + e.getMessage());
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
