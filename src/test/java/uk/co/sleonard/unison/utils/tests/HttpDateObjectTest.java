package uk.co.sleonard.unison.utils.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;

import com.ibm.icu.util.Calendar;

import uk.co.sleonard.unison.gui.UNISoNException;
import uk.co.sleonard.unison.utils.HttpDateObject;

/**
 * The Class HttpDateObject.
 */
public class HttpDateObjectTest{

	/**
	 * Test parse
	 */
	@Test
	public void testParse() {
		HttpDateObject parser = HttpDateObject.getParser();

		try {
			Date date = parser.parseDate("28/11/2007");
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			System.out.println(date);
			assertEquals(28, cal.get(Calendar.DAY_OF_MONTH));
		} catch (ParseException e) {
			fail("FAIL : " + e);
		} catch (UNISoNException e) {
			fail("FAIL : " + e);
		}
	}
	
	/**
	 * Test format
	 */
	@Test
	@Ignore
	public void testFormat(){
	}
}
