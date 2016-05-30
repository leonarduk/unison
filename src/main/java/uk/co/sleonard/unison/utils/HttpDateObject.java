/*
 * HTTPDateFormat.java Copyright (C) 2004 The Free Software Foundation
 *
 * This file is part of GNU inetlib, a library.
 *
 * GNU inetlib is free software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * GNU inetlib is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this library; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 *
 * Linking this library statically or dynamically with other modules is making a combined work based
 * on this library. Thus, the terms and conditions of the GNU General Public License cover the whole
 * combination.
 *
 * As a special exception, the copyright holders of this library give you permission to link this
 * library with independent modules to produce an executable, regardless of the license terms of
 * these independent modules, and to copy and distribute the resulting executable under terms of
 * your choice, provided that you also meet, for each linked independent module, the terms and
 * conditions of the license of that module. An independent module is a module which is not derived
 * from or based on this library. If you modify this library, you may extend this exception to your
 * version of the library, but you are not obliged to do so. If you do not wish to do so, delete
 * this exception statement from your version.
 */

package uk.co.sleonard.unison.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import uk.co.sleonard.unison.UNISoNException;

/**
 * HTTP date formatter and parser. Formats dates according to RFC 822 (updated by RFC 1123). Parses
 * dates according to the above, <i>or</i> RFC 1036, <i>or</i> the ANSI C <code>asctime()</code>
 * format.
 *
 * @author Chris Burdess <dog@gnu.org>
 * @since v1.0.0
 *
 */
public class HttpDateObject extends DateFormat {

	/** The Constant DAYS_OF_WEEK. */
	static final String[] DAYS_OF_WEEK = { null, "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };

	/** The instance. */
	private static HttpDateObject instance;

	/** The Constant MONTHS. */
	static final String[] MONTHS = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep",
	        "Oct", "Nov", "Dec" };

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4375514658403735218L;

	/**
	 * Gets the parser.
	 *
	 * @return the parser
	 */
	public static HttpDateObject getParser() {
		if (null == HttpDateObject.instance) {
			HttpDateObject.instance = new HttpDateObject();
		}
		return HttpDateObject.instance;

	}

	/**
	 * Instantiates a new http date object.
	 */
	public HttpDateObject() {
		this.calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		this.numberFormat = new DecimalFormat();
	}

	/**
	 * Appends the textual value for the specified field to the given string buffer. This method
	 * should be avoided, use <code>format(Date)</code> instead.
	 *
	 * @param date
	 *            the Date object
	 * @param buf
	 *            the buffer to append to
	 * @param field
	 *            the current field position
	 * @return the modified buffer
	 */
	@Override
	public StringBuffer format(final Date date, final StringBuffer buf, final FieldPosition field) {
		this.calendar.clear();
		this.calendar.setTime(date);
		buf.setLength(0);

		// Day of week
		buf.append(HttpDateObject.DAYS_OF_WEEK[this.calendar.get(Calendar.DAY_OF_WEEK)]);
		buf.append(',');
		buf.append(' ');

		// Day of month
		final int day = this.calendar.get(Calendar.DAY_OF_MONTH);
		buf.append(Character.forDigit(day / 10, 10));
		buf.append(Character.forDigit(day % 10, 10));
		buf.append(' ');

		// Month
		buf.append(HttpDateObject.MONTHS[this.calendar.get(Calendar.MONTH)]);
		buf.append(' ');

		// Year
		final int year = this.calendar.get(Calendar.YEAR);
		if (year < 1000) {
			buf.append('0');
			if (year < 100) {
				buf.append('0');
				if (year < 10) {
					buf.append('0');
				}
			}
		}
		buf.append(Integer.toString(year));
		buf.append(' ');

		// Hour
		final int hour = this.calendar.get(Calendar.HOUR_OF_DAY);
		buf.append(Character.forDigit(hour / 10, 10));
		buf.append(Character.forDigit(hour % 10, 10));
		buf.append(':');

		// Minute
		final int minute = this.calendar.get(Calendar.MINUTE);
		buf.append(Character.forDigit(minute / 10, 10));
		buf.append(Character.forDigit(minute % 10, 10));
		buf.append(':');

		// Second
		final int second = this.calendar.get(Calendar.SECOND);
		buf.append(Character.forDigit(second / 10, 10));
		buf.append(Character.forDigit(second % 10, 10));
		buf.append(' ');

		// Timezone
		// Get time offset in minutes
		int zoneOffset = (this.calendar.get(Calendar.ZONE_OFFSET)
		        + this.calendar.get(Calendar.DST_OFFSET)) / 60000;

		// Apply + or - appropriately
		if (zoneOffset < 0) {
			zoneOffset = -zoneOffset;
			buf.append('-');
		}
		else {
			buf.append('+');
		}

		// Set the 2 2-char fields as specified above
		final int tzhours = zoneOffset / 60;
		buf.append(Character.forDigit(tzhours / 10, 10));
		buf.append(Character.forDigit(tzhours % 10, 10));
		final int tzminutes = zoneOffset % 60;
		buf.append(Character.forDigit(tzminutes / 10, 10));
		buf.append(Character.forDigit(tzminutes % 10, 10));

		field.setBeginIndex(0);
		field.setEndIndex(buf.length());
		return buf;
	}

	/**
	 * Parses the given date in the current TimeZone.
	 *
	 * @param text
	 *            the formatted date to be parsed
	 * @param pos
	 *            the current parse position
	 * @return the date
	 */
	@Override
	public Date parse(final String text, final ParsePosition pos) {
		int date;
		int month;
		int year;
		int hour;
		int minute;
		int second;

		String monthText;
		int start = 0;
		int end = -1;
		final int len = text.length();
		this.calendar.clear();
		pos.setIndex(start);
		try {
			// Advance to date
			if (Character.isLetter(text.charAt(start))) {
				start = this.skipNonWhitespace(text, start);
			}
			// Determine mode
			switch (start) {
				case 3:
					// asctime
					start = this.skipWhitespace(text, start);
					pos.setIndex(start);
					end = this.skipNonWhitespace(text, start + 1);
					monthText = text.substring(start, end);
					month = -1;
					for (int i = 0; i < 12; i++) {
						if (HttpDateObject.MONTHS[i].equals(monthText)) {
							month = i;
							break;
						}
					}
					if (month == -1) {
						pos.setErrorIndex(end);
						return null;
					}
					// Advance to date
					start = this.skipWhitespace(text, end + 1);
					pos.setIndex(start);
					end = this.skipNonWhitespace(text, start + 1);
					date = Integer.parseInt(text.substring(start, end));
					// Advance to hour
					start = this.skipWhitespace(text, end + 1);
					pos.setIndex(start);
					end = this.skipTo(text, start + 1, ':');
					hour = Integer.parseInt(text.substring(start, end));
					// Advance to minute
					start = end + 1;
					pos.setIndex(start);
					end = this.skipTo(text, start + 1, ':');
					minute = Integer.parseInt(text.substring(start, end));
					// Advance to second
					start = end + 1;
					pos.setIndex(start);
					end = this.skipNonWhitespace(text, start + 1);
					second = Integer.parseInt(text.substring(start, end));
					// Advance to year
					start = this.skipWhitespace(text, end + 1);
					pos.setIndex(start);
					end = this.skipNonWhitespace(text, start + 1);
					year = Integer.parseInt(text.substring(start, end));
					break;
				case 0:
				case 4:
					// rfc822
					start = this.skipWhitespace(text, start);
					pos.setIndex(start);
					end = this.skipNonWhitespace(text, start + 1);
					date = Integer.parseInt(text.substring(start, end));
					// Advance to month
					start = this.skipWhitespace(text, end + 1);
					pos.setIndex(start);
					end = this.skipNonWhitespace(text, start + 1);
					monthText = text.substring(start, end);
					month = -1;
					for (int i = 0; i < 12; i++) {
						if (HttpDateObject.MONTHS[i].equals(monthText)) {
							month = i;
							break;
						}
					}
					if (month == -1) {
						pos.setErrorIndex(end);
						return null;
					}
					// Advance to year
					start = this.skipWhitespace(text, end + 1);
					pos.setIndex(start);
					end = this.skipNonWhitespace(text, start + 1);
					year = Integer.parseInt(text.substring(start, end));
					// Advance to hour
					start = this.skipWhitespace(text, end + 1);
					pos.setIndex(start);
					end = this.skipTo(text, start + 1, ':');
					hour = Integer.parseInt(text.substring(start, end));
					// Advance to minute
					start = end + 1;
					pos.setIndex(start);
					end = this.skipTo(text, start + 1, ':');
					minute = Integer.parseInt(text.substring(start, end));
					// Advance to second
					start = end + 1;
					pos.setIndex(start);
					end = start + 1;
					while ((end < len) && !Character.isWhitespace(text.charAt(end))) {
						end++;
					}
					second = Integer.parseInt(text.substring(start, end));
					break;
				default:
					// rfc850(obsolete)
					start = this.skipWhitespace(text, start);
					pos.setIndex(start);
					end = this.skipTo(text, start + 1, '-');
					date = Integer.parseInt(text.substring(start, end));
					// Advance to month
					start = end + 1;
					pos.setIndex(start);
					end = this.skipTo(text, start + 1, '-');
					monthText = text.substring(start, end);
					month = -1;
					for (int i = 0; i < 12; i++) {
						if (HttpDateObject.MONTHS[i].equals(monthText)) {
							month = i;
							break;
						}
					}
					if (month == -1) {
						pos.setErrorIndex(end);
						return null;
					}
					// Advance to year
					start = end + 1;
					pos.setIndex(start);
					end = this.skipNonWhitespace(text, start + 1);
					year = 1900 + Integer.parseInt(text.substring(start, end));
					// Advance to hour
					start = this.skipWhitespace(text, end + 1);
					pos.setIndex(start);
					end = this.skipTo(text, start + 1, ':');
					hour = Integer.parseInt(text.substring(start, end));
					// Advance to minute
					start = end + 1;
					pos.setIndex(start);
					end = this.skipTo(text, start + 1, ':');
					minute = Integer.parseInt(text.substring(start, end));
					// Advance to second
					start = end + 1;
					pos.setIndex(start);
					end = start + 1;
					while ((end < len) && !Character.isWhitespace(text.charAt(end))) {
						end++;
					}
					second = Integer.parseInt(text.substring(start, end));
			}

			this.setCalendar(date, month, year, hour, minute, second);

			if (end != len) {
				// Timezone
				start = this.skipWhitespace(text, end + 1);
				end = start + 1;
				while ((end < len) && !Character.isWhitespace(text.charAt(end))) {
					end++;
				}
				final char pm = text.charAt(start);
				if (Character.isLetter(pm)) {
					final TimeZone tz = TimeZone.getTimeZone(text.substring(start, end));
					this.calendar.set(Calendar.ZONE_OFFSET, tz.getRawOffset());
				}
				else {
					int zoneOffset = 0;
					zoneOffset += 600 * Character.digit(text.charAt(++start), 10);
					zoneOffset += 60 * Character.digit(text.charAt(++start), 10);
					zoneOffset += 10 * Character.digit(text.charAt(++start), 10);
					zoneOffset += Character.digit(text.charAt(++start), 10);
					zoneOffset *= 60000; // minutes -> ms
					if ('-' == pm) {
						zoneOffset = -zoneOffset;
					}
					this.calendar.set(Calendar.ZONE_OFFSET, zoneOffset);
				}
			}
			pos.setIndex(end);

			return this.calendar.getTime();
		}
		catch (final NumberFormatException e) {
			pos.setErrorIndex(Math.max(start, end));
		}
		catch (final StringIndexOutOfBoundsException e) {
			pos.setErrorIndex(Math.max(start, end));
		}
		return null;
	}

	/**
	 * Parses the date.
	 *
	 * @param text
	 *            the text
	 * @return the date
	 * @throws ParseException
	 *             the parse exception
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	public Date parseDate(final String text) throws ParseException, UNISoNException {
		if ((null == text) || text.equals("")) {
			return null;
		}
		if (text.length() == 8) {
			final DateFormat fmt = new SimpleDateFormat("yyyyMMdd");
			try {
				return fmt.parse(text);
			}
			catch (final ParseException e) {
				throw new UNISoNException("Failed to parse date:" + text, e);
			}
		}
		else if ((text.length() == 10) && text.contains("/")) {
			final DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
			try {
				return fmt.parse(text);
			}
			catch (final ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
		return this.parse(text);
	}

	/**
	 * Don't allow setting the calendar.
	 *
	 * @param newCalendar
	 *            the new calendar
	 */
	@Override
	public void setCalendar(final Calendar newCalendar) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets the calendar.
	 *
	 * @param date
	 *            the date
	 * @param month
	 *            the month
	 * @param year
	 *            the year
	 * @param hour
	 *            the hour
	 * @param minute
	 *            the minute
	 * @param second
	 *            the second
	 */
	public void setCalendar(final int date, final int month, final int year, final int hour,
	        final int minute, final int second) {
		this.calendar.set(Calendar.YEAR, year);
		this.calendar.set(Calendar.MONTH, month);
		this.calendar.set(Calendar.DAY_OF_MONTH, date);
		this.calendar.set(Calendar.HOUR, hour);
		this.calendar.set(Calendar.MINUTE, minute);
		this.calendar.set(Calendar.SECOND, second);
	}

	/**
	 * Don't allow setting the NumberFormat.
	 *
	 * @param newNumberFormat
	 *            the new number format
	 */
	@Override
	public void setNumberFormat(final NumberFormat newNumberFormat) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Skip non whitespace.
	 *
	 * @param text
	 *            the text
	 * @param pos
	 *            the pos
	 * @return the int
	 */
	private int skipNonWhitespace(final String text, final int pos) {
		int pos2 = pos;
		while (!Character.isWhitespace(text.charAt(pos2))) {
			pos2++;
		}
		return pos2;
	}

	/**
	 * Skip to.
	 *
	 * @param text
	 *            the text
	 * @param pos
	 *            the pos
	 * @param c
	 *            the c
	 * @return the int
	 */
	private int skipTo(final String text, final int pos, final char c) {
		int pos2 = pos;
		while (text.charAt(pos2) != c) {
			pos2++;
		}
		return pos2;
	}

	/**
	 * Skip whitespace.
	 *
	 * @param text
	 *            the text
	 * @param pos
	 *            the pos
	 * @return the int
	 */
	private int skipWhitespace(final String text, final int pos) {
		int pos2 = pos;
		while (Character.isWhitespace(text.charAt(pos2))) {
			pos2++;
		}
		return pos2;
	}

}
