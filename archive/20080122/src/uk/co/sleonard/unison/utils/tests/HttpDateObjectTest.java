package uk.co.sleonard.unison.utils.tests;

import java.text.ParseException;
import java.util.Date;

import uk.co.sleonard.unison.gui.UNISoNException;
import uk.co.sleonard.unison.utils.HttpDateObject;

import junit.framework.TestCase;

public class HttpDateObjectTest extends TestCase {

	public void testParse() {
		HttpDateObject parser = HttpDateObject.getParser();

		try {
			Date date = parser.parseDate("28/11/2007");
			System.out.println(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			fail("FAIL : " + e);
		} catch (UNISoNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
